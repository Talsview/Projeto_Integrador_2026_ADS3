import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize, forkJoin } from 'rxjs';
import { ClienteApiService } from '../../core/services/cliente-api.service';
import { GarantiaApiService } from '../../core/services/garantia-api.service';
import { OrdemServicoApiService } from '../../core/services/ordem-servico-api.service';
import { PagamentoApiService } from '../../core/services/pagamento-api.service';
import { VeiculoApiService } from '../../core/services/veiculo-api.service';
import { GarantiaPeca, GarantiaServico } from '../../models/garantia.model';
import { OrdemServicoResumo, StatusFluxoOrdemServico } from '../../models/ordem-servico.model';
import { Pagamento } from '../../models/pagamento.model';

interface IndicadorRelatorio {
  titulo: string;
  valor: string | number;
  detalhe: string;
  classe?: string;
}

interface StatusResumo {
  status: string;
  quantidade: number;
  percentual: number;
}

interface LinhaPlanilha {
  valor: string | number;
  tipo?: 'String' | 'Number';
  estilo?: string;
}

@Component({
  selector: 'app-relatorios',
  standalone: true,
  imports: [CommonModule, FormsModule, CurrencyPipe, DatePipe],
  templateUrl: './relatorios.component.html'
})
export class RelatoriosComponent implements OnInit {
  carregando = false;
  erro?: string;
  mensagem?: string;
  dataInicio = '';
  dataFim = '';

  indicadores: IndicadorRelatorio[] = [];
  resumoStatus: StatusResumo[] = [];
  ultimasOrdens: OrdemServicoResumo[] = [];
  garantiasAtivas: Array<GarantiaPeca | GarantiaServico> = [];

  private ordens: OrdemServicoResumo[] = [];
  private pagamentos: Pagamento[] = [];
  private garantiasPecas: GarantiaPeca[] = [];
  private garantiasServicos: GarantiaServico[] = [];
  private totalClientes = 0;
  private totalVeiculos = 0;

  constructor(
    private readonly clienteApi: ClienteApiService,
    private readonly veiculoApi: VeiculoApiService,
    private readonly ordemApi: OrdemServicoApiService,
    private readonly pagamentoApi: PagamentoApiService,
    private readonly garantiaApi: GarantiaApiService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.definirPeriodoPadrao();
    this.carregarRelatorio();
  }

  carregarRelatorio(): void {
    this.carregando = true;
    this.erro = undefined;
    this.mensagem = undefined;
    this.atualizarTela();

    forkJoin({
      clientes: this.clienteApi.listar(),
      veiculos: this.veiculoApi.listar(),
      ordens: this.ordemApi.listar(),
      pagamentos: this.pagamentoApi.listar(),
      garantiasPecas: this.garantiaApi.listarGarantiasPecas(),
      garantiasServicos: this.garantiaApi.listarGarantiasServicos()
    }).pipe(
      finalize(() => { this.carregando = false; this.atualizarTela(); })
    ).subscribe({
      next: dados => {
        this.totalClientes = dados.clientes.length;
        this.totalVeiculos = dados.veiculos.length;
        this.ordens = dados.ordens;
        this.pagamentos = dados.pagamentos;
        this.garantiasPecas = dados.garantiasPecas;
        this.garantiasServicos = dados.garantiasServicos;
        this.montarRelatorio();
        this.mensagem = 'Relatórios atualizados com dados do backend.';
        this.atualizarTela();
      },
      error: e => { this.erro = e.message ?? 'Não foi possível carregar os relatórios.'; this.atualizarTela(); }
    });
  }

  exportarPlanilhaExcel(): void {
    const ordensPeriodo = this.ordensFiltradas();
    const pagamentosPeriodo = this.pagamentosFiltrados();
    const garantiasPeriodo = [...this.garantiasPecas, ...this.garantiasServicos].filter(g => this.estaNoPeriodo(g.dataInicio) || this.estaNoPeriodo(g.dataFim));
    const geradoEm = new Date().toLocaleString('pt-BR');
    const periodo = `${this.formatarDataCurta(this.dataInicio)} até ${this.formatarDataCurta(this.dataFim)}`;

    const workbook = this.montarWorkbookExcelXml([
      this.montarAbaResumo(periodo, geradoEm),
      this.montarAbaOrdens(ordensPeriodo),
      this.montarAbaPagamentos(pagamentosPeriodo),
      this.montarAbaGarantias(garantiasPeriodo)
    ]);

    const blob = new Blob([workbook], { type: 'application/vnd.ms-excel;charset=utf-8;' });
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `relatorio-gerencial-av-car-${this.dataInicio || 'inicio'}-${this.dataFim || 'fim'}.xls`;
    link.click();
    window.URL.revokeObjectURL(url);
    this.mensagem = 'Planilha Excel gerada com formatação profissional.';
    this.atualizarTela();
  }

  ordensFiltradas(): OrdemServicoResumo[] {
    return this.ordens.filter(os => this.estaNoPeriodo(os.dataAbertura));
  }

  pagamentosFiltrados(): Pagamento[] {
    return this.pagamentos.filter(p => this.estaNoPeriodo(p.dataPagamento));
  }

  private montarRelatorio(): void {
    const ordensPeriodo = this.ordensFiltradas();
    const pagamentosPeriodo = this.pagamentosFiltrados();
    const receitaRecebida = pagamentosPeriodo
      .filter(p => p.statusPagamento === 'PAGO')
      .reduce((total, p) => total + Number(p.valorPago ?? 0), 0);
    const ticketMedio = ordensPeriodo.length ? receitaRecebida / ordensPeriodo.length : 0;
    const garantias = [...this.garantiasPecas, ...this.garantiasServicos];
    const garantiasAtivas = garantias.filter(g => g.statusGarantia === 'VIGENTE' || g.statusGarantia === 'ACIONADA');

    this.indicadores = [
      { titulo: 'Clientes cadastrados', valor: this.totalClientes, detalhe: 'Base ativa retornada pela API', classe: 'info' },
      { titulo: 'Veículos cadastrados', valor: this.totalVeiculos, detalhe: 'Veículos com histórico de proprietário', classe: 'info' },
      { titulo: 'OS no período', valor: ordensPeriodo.length, detalhe: 'Ordens abertas no intervalo selecionado', classe: 'warning' },
      { titulo: 'Receita recebida', valor: this.formatarMoeda(receitaRecebida), detalhe: 'Pagamentos com status PAGO', classe: 'success' },
      { titulo: 'Ticket médio', valor: this.formatarMoeda(ticketMedio), detalhe: 'Receita recebida / quantidade de OS', classe: 'info' },
      { titulo: 'Garantias ativas', valor: garantiasAtivas.length, detalhe: 'Garantias vigentes ou acionadas', classe: 'danger' }
    ];

    this.resumoStatus = this.montarResumoStatus(ordensPeriodo);
    this.ultimasOrdens = [...ordensPeriodo]
      .sort((a, b) => this.dataMs(b.dataAbertura) - this.dataMs(a.dataAbertura))
      .slice(0, 8);
    this.garantiasAtivas = garantiasAtivas.slice(0, 8);
  }

  private montarResumoStatus(ordens: OrdemServicoResumo[]): StatusResumo[] {
    const status: StatusFluxoOrdemServico[] = ['ORCAMENTO', 'EXECUCAO', 'PAGAMENTO', 'FINALIZADO'];
    return status.map(item => {
      const quantidade = ordens.filter(os => os.statusAtual === item).length;
      const percentual = ordens.length ? Math.round((quantidade / ordens.length) * 100) : 0;
      return { status: item, quantidade, percentual };
    });
  }

  private estaNoPeriodo(data?: string): boolean {
    if (!data) return true;
    const valor = this.normalizarData(data);
    if (!valor) return true;
    const inicio = this.dataInicio ? new Date(`${this.dataInicio}T00:00:00`).getTime() : Number.NEGATIVE_INFINITY;
    const fim = this.dataFim ? new Date(`${this.dataFim}T23:59:59`).getTime() : Number.POSITIVE_INFINITY;
    return valor >= inicio && valor <= fim;
  }

  private definirPeriodoPadrao(): void {
    const hoje = new Date();
    const inicio = new Date(hoje.getFullYear(), hoje.getMonth(), 1);
    this.dataInicio = inicio.toISOString().slice(0, 10);
    this.dataFim = hoje.toISOString().slice(0, 10);
  }

  private normalizarData(data: string): number | null {
    const parsed = new Date(data).getTime();
    return Number.isNaN(parsed) ? null : parsed;
  }

  private dataMs(data?: string): number {
    return data ? (this.normalizarData(data) ?? 0) : 0;
  }

  private formatarMoeda(valor: number): string {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(valor);
  }

  private formatarDataCurta(data?: string): string {
    if (!data) return 'Não informado';
    const parsed = new Date(`${data}T00:00:00`);
    return Number.isNaN(parsed.getTime()) ? data : parsed.toLocaleDateString('pt-BR');
  }

  private montarAbaResumo(periodo: string, geradoEm: string): string {
    const linhasIndicadores = this.indicadores.map(indicador => this.row([
      this.cell(indicador.titulo, 'Text'),
      this.cell(String(indicador.valor), 'KpiValue'),
      this.cell(indicador.detalhe, 'Text')
    ]));

    const linhasStatus = this.resumoStatus.map(status => this.row([
      this.cell(status.status, this.estiloStatus(status.status)),
      this.cell(status.quantidade, 'Number', 'Number'),
      this.cell(`${status.percentual}%`, 'TextCenter')
    ]));

    return this.worksheet('Resumo Gerencial', [
      this.columns([180, 120, 360, 30, 150, 120, 120]),
      this.row([this.cell('AV CAR AUTO CENTER', 'Title', 'String', 7)]),
      this.row([this.cell('Relatório Gerencial da Oficina', 'Subtitle', 'String', 7)]),
      this.row([this.cell(`Período: ${periodo}`, 'Info', 'String', 7)]),
      this.row([this.cell(`Gerado em: ${geradoEm}`, 'Info', 'String', 7)]),
      this.row([]),
      this.row([this.cell('Indicadores principais', 'Section', 'String', 7)]),
      this.row([this.cell('Indicador', 'Header'), this.cell('Valor', 'Header'), this.cell('Descrição', 'Header')]),
      ...linhasIndicadores,
      this.row([]),
      this.row([this.cell('Resumo por status das Ordens de Serviço', 'Section', 'String', 7)]),
      this.row([this.cell('Status', 'Header'), this.cell('Quantidade', 'Header'), this.cell('Percentual', 'Header')]),
      ...linhasStatus,
      this.row([]),
      this.row([this.cell('Observação', 'Section', 'String', 7)]),
      this.row([this.cell('Planilha gerada automaticamente pelo módulo Relatórios do sistema AV CAR AUTO CENTER. Os dados são obtidos da API REST do backend e respeitam o período selecionado na tela.', 'TextWrap', 'String', 7)])
    ]);
  }

  private montarAbaOrdens(ordens: OrdemServicoResumo[]): string {
    const linhas = ordens.map(os => this.row([
      this.cell(os.numeroOs ?? '', 'TextCenter'),
      this.cell(os.nomeCliente ?? '', 'Text'),
      this.cell(os.placaVeiculo ?? '', 'TextCenter'),
      this.cell(os.descricaoVeiculo ?? '', 'Text'),
      this.cell(os.statusAtual ?? '', this.estiloStatus(os.statusAtual)),
      this.cell(os.prioridade ?? '', 'TextCenter'),
      this.cell(Number(os.valorTotal ?? 0), 'Currency', 'Number'),
      this.cell(this.formatarDataHora(os.dataAbertura), 'TextCenter'),
      this.cell(this.formatarDataHora(os.dataFinalizacao), 'TextCenter')
    ]));

    return this.worksheet('Ordens de Serviço', [
      this.columns([90, 220, 90, 220, 110, 100, 110, 150, 150]),
      this.row([this.cell('Ordens de Serviço do Período', 'Title', 'String', 9)]),
      this.row([this.cell('Número', 'Header'), this.cell('Cliente', 'Header'), this.cell('Placa', 'Header'), this.cell('Veículo', 'Header'), this.cell('Status', 'Header'), this.cell('Prioridade', 'Header'), this.cell('Valor total', 'Header'), this.cell('Abertura', 'Header'), this.cell('Finalização', 'Header')]),
      ...linhas
    ]);
  }

  private montarAbaPagamentos(pagamentos: Pagamento[]): string {
    const totalPago = pagamentos.filter(p => p.statusPagamento === 'PAGO').reduce((total, p) => total + Number(p.valorPago ?? 0), 0);
    const linhas = pagamentos.map(p => this.row([
      this.cell(p.numeroOs ?? String(p.idOrdemServico ?? ''), 'TextCenter'),
      this.cell(p.formaPagamento ?? '', 'TextCenter'),
      this.cell(p.statusPagamento ?? '', this.estiloPagamento(p.statusPagamento)),
      this.cell(Number(p.valorPago ?? 0), 'Currency', 'Number'),
      this.cell(this.formatarDataHora(p.dataPagamento), 'TextCenter'),
      this.cell(p.observacao ?? '', 'TextWrap')
    ]));

    return this.worksheet('Pagamentos', [
      this.columns([110, 140, 120, 120, 150, 360]),
      this.row([this.cell('Pagamentos do Período', 'Title', 'String', 6)]),
      this.row([this.cell(`Total recebido: ${this.formatarMoeda(totalPago)}`, 'Section', 'String', 6)]),
      this.row([this.cell('OS', 'Header'), this.cell('Forma', 'Header'), this.cell('Status', 'Header'), this.cell('Valor pago', 'Header'), this.cell('Data', 'Header'), this.cell('Observação', 'Header')]),
      ...linhas
    ]);
  }

  private montarAbaGarantias(garantias: Array<GarantiaPeca | GarantiaServico>): string {
    const linhas = garantias.map(g => this.row([
      this.cell(g.id ?? '', 'TextCenter'),
      this.cell('nomePeca' in g ? 'PEÇA' : 'SERVIÇO', 'TextCenter'),
      this.cell(('nomePeca' in g ? g.nomePeca : g.nomeServico) ?? '', 'Text'),
      this.cell(g.statusGarantia ?? '', this.estiloGarantia(g.statusGarantia)),
      this.cell(String(g.prazoDias ?? ''), 'TextCenter'),
      this.cell(this.formatarDataHora(g.dataInicio), 'TextCenter'),
      this.cell(this.formatarDataHora(g.dataFim), 'TextCenter'),
      this.cell(this.formatarDataHora(g.dataAcionamento), 'TextCenter'),
      this.cell(g.responsavelAnalise ?? '', 'Text'),
      this.cell(g.observacao ?? '', 'TextWrap')
    ]));

    return this.worksheet('Garantias', [
      this.columns([80, 90, 240, 120, 90, 130, 130, 150, 180, 300]),
      this.row([this.cell('Garantias de Peças e Serviços', 'Title', 'String', 10)]),
      this.row([this.cell('ID', 'Header'), this.cell('Tipo', 'Header'), this.cell('Item', 'Header'), this.cell('Status', 'Header'), this.cell('Prazo', 'Header'), this.cell('Início', 'Header'), this.cell('Fim', 'Header'), this.cell('Acionamento', 'Header'), this.cell('Responsável', 'Header'), this.cell('Observação', 'Header')]),
      ...linhas
    ]);
  }

  private montarWorkbookExcelXml(worksheets: string[]): string {
    return `<?xml version="1.0" encoding="UTF-8"?>
<?mso-application progid="Excel.Sheet"?>
<Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet"
 xmlns:o="urn:schemas-microsoft-com:office:office"
 xmlns:x="urn:schemas-microsoft-com:office:excel"
 xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"
 xmlns:html="http://www.w3.org/TR/REC-html40">
 <DocumentProperties xmlns="urn:schemas-microsoft-com:office:office">
  <Author>AV CAR AUTO CENTER</Author>
  <Company>AV CAR AUTO CENTER</Company>
  <Title>Relatório Gerencial</Title>
 </DocumentProperties>
 <Styles>
  <Style ss:ID="Default" ss:Name="Normal"><Alignment ss:Vertical="Center"/><Font ss:FontName="Arial" ss:Size="10"/><Interior/><NumberFormat/><Protection/></Style>
  <Style ss:ID="Title"><Alignment ss:Horizontal="Center" ss:Vertical="Center"/><Font ss:FontName="Arial" ss:Size="18" ss:Bold="1" ss:Color="#FFFFFF"/><Interior ss:Color="#0B2239" ss:Pattern="Solid"/></Style>
  <Style ss:ID="Subtitle"><Alignment ss:Horizontal="Center" ss:Vertical="Center"/><Font ss:FontName="Arial" ss:Size="12" ss:Bold="1" ss:Color="#F4511E"/><Interior ss:Color="#132F4C" ss:Pattern="Solid"/></Style>
  <Style ss:ID="Info"><Alignment ss:Horizontal="Center" ss:Vertical="Center"/><Font ss:FontName="Arial" ss:Size="10" ss:Color="#334155"/><Interior ss:Color="#E8EEF5" ss:Pattern="Solid"/></Style>
  <Style ss:ID="Section"><Alignment ss:Horizontal="Left" ss:Vertical="Center"/><Font ss:FontName="Arial" ss:Size="12" ss:Bold="1" ss:Color="#0B2239"/><Interior ss:Color="#FFE9DF" ss:Pattern="Solid"/></Style>
  <Style ss:ID="Header"><Alignment ss:Horizontal="Center" ss:Vertical="Center"/><Font ss:FontName="Arial" ss:Size="10" ss:Bold="1" ss:Color="#FFFFFF"/><Interior ss:Color="#F4511E" ss:Pattern="Solid"/><Borders><Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1" ss:Color="#0B2239"/></Borders></Style>
  <Style ss:ID="Text"><Alignment ss:Vertical="Center"/><Font ss:FontName="Arial" ss:Size="10" ss:Color="#0B2239"/><Interior ss:Color="#FFFFFF" ss:Pattern="Solid"/><Borders><Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1" ss:Color="#D6DEE8"/></Borders></Style>
  <Style ss:ID="TextCenter"><Alignment ss:Horizontal="Center" ss:Vertical="Center"/><Font ss:FontName="Arial" ss:Size="10" ss:Color="#0B2239"/><Interior ss:Color="#FFFFFF" ss:Pattern="Solid"/><Borders><Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1" ss:Color="#D6DEE8"/></Borders></Style>
  <Style ss:ID="TextWrap"><Alignment ss:Vertical="Center" ss:WrapText="1"/><Font ss:FontName="Arial" ss:Size="10" ss:Color="#0B2239"/><Interior ss:Color="#FFFFFF" ss:Pattern="Solid"/><Borders><Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1" ss:Color="#D6DEE8"/></Borders></Style>
  <Style ss:ID="KpiValue"><Alignment ss:Horizontal="Center" ss:Vertical="Center"/><Font ss:FontName="Arial" ss:Size="12" ss:Bold="1" ss:Color="#F4511E"/><Interior ss:Color="#FFF3ED" ss:Pattern="Solid"/><Borders><Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1" ss:Color="#FFD2C2"/></Borders></Style>
  <Style ss:ID="Currency"><Alignment ss:Horizontal="Right" ss:Vertical="Center"/><Font ss:FontName="Arial" ss:Size="10" ss:Bold="1" ss:Color="#0B2239"/><Interior ss:Color="#FFFFFF" ss:Pattern="Solid"/><NumberFormat ss:Format="Currency"/><Borders><Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1" ss:Color="#D6DEE8"/></Borders></Style>
  <Style ss:ID="Number"><Alignment ss:Horizontal="Center" ss:Vertical="Center"/><Font ss:FontName="Arial" ss:Size="10" ss:Color="#0B2239"/><Interior ss:Color="#FFFFFF" ss:Pattern="Solid"/><Borders><Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1" ss:Color="#D6DEE8"/></Borders></Style>
  <Style ss:ID="StatusOk"><Alignment ss:Horizontal="Center"/><Font ss:Bold="1" ss:Color="#065F46"/><Interior ss:Color="#D1FAE5" ss:Pattern="Solid"/></Style>
  <Style ss:ID="StatusWarn"><Alignment ss:Horizontal="Center"/><Font ss:Bold="1" ss:Color="#92400E"/><Interior ss:Color="#FEF3C7" ss:Pattern="Solid"/></Style>
  <Style ss:ID="StatusInfo"><Alignment ss:Horizontal="Center"/><Font ss:Bold="1" ss:Color="#1E3A8A"/><Interior ss:Color="#DBEAFE" ss:Pattern="Solid"/></Style>
  <Style ss:ID="StatusDanger"><Alignment ss:Horizontal="Center"/><Font ss:Bold="1" ss:Color="#991B1B"/><Interior ss:Color="#FEE2E2" ss:Pattern="Solid"/></Style>
 </Styles>
 ${worksheets.join('\n')}
</Workbook>`;
  }

  private worksheet(nome: string, conteudo: string[]): string {
    return `<Worksheet ss:Name="${this.xml(nome)}"><Table>${conteudo.join('\n')}</Table><WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel"><FreezePanes/><FrozenNoSplit/><SplitHorizontal>2</SplitHorizontal><TopRowBottomPane>2</TopRowBottomPane><ProtectObjects>False</ProtectObjects><ProtectScenarios>False</ProtectScenarios></WorksheetOptions></Worksheet>`;
  }

  private columns(widths: number[]): string {
    return widths.map(width => `<Column ss:AutoFitWidth="0" ss:Width="${width}"/>`).join('');
  }

  private row(cells: string[]): string {
    return `<Row ss:Height="24">${cells.join('')}</Row>`;
  }

  private cell(valor: string | number, estilo = 'Text', tipo: 'String' | 'Number' = 'String', mergeAcross = 0): string {
    const merge = mergeAcross > 0 ? ` ss:MergeAcross="${mergeAcross - 1}"` : '';
    const valorSeguro = tipo === 'Number' ? String(Number(valor) || 0) : this.xml(String(valor ?? ''));
    return `<Cell ss:StyleID="${estilo}"${merge}><Data ss:Type="${tipo}">${valorSeguro}</Data></Cell>`;
  }

  private xml(valor: string): string {
    return valor.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&apos;');
  }

  private formatarDataHora(data?: string): string {
    if (!data) return '';
    const parsed = new Date(data);
    return Number.isNaN(parsed.getTime()) ? data : parsed.toLocaleString('pt-BR');
  }

  private estiloStatus(status?: string): string {
    if (status === 'FINALIZADO') return 'StatusOk';
    if (status === 'PAGAMENTO') return 'StatusWarn';
    if (status === 'EXECUCAO') return 'StatusInfo';
    return 'StatusDanger';
  }

  private estiloPagamento(status?: string): string {
    if (status === 'PAGO') return 'StatusOk';
    if (status === 'PENDENTE') return 'StatusWarn';
    return 'StatusDanger';
  }

  private estiloGarantia(status?: string): string {
    if (status === 'VIGENTE') return 'StatusOk';
    if (status === 'ACIONADA') return 'StatusWarn';
    if (status === 'EXPIRADA' || status === 'ENCERRADA') return 'StatusDanger';
    return 'StatusInfo';
  }

  private atualizarTela(): void { this.cdr.detectChanges(); }
}
