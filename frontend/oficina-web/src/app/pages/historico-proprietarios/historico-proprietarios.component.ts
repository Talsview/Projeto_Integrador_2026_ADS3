import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { finalize, forkJoin } from 'rxjs';
import { ClienteApiService } from '../../core/services/cliente-api.service';
import { VeiculoApiService } from '../../core/services/veiculo-api.service';
import { ClienteResumo } from '../../models/cliente.model';
import { HistoricoProprietario, TransferenciaProprietario } from '../../models/veiculo.model';

interface ClienteHistoricoResumo {
  clienteId: number;
  nomeCliente: string;
  totalRegistros: number;
  totalVeiculos: number;
  totalVeiculosAtuais: number;
  veiculosResumo: string;
}

@Component({
  selector: 'app-historico-proprietarios',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './historico-proprietarios.component.html',
  styleUrl: './historico-proprietarios.component.css'
})
export class HistoricoProprietariosComponent implements OnInit {
  historicos: HistoricoProprietario[] = [];
  clientesHistorico: ClienteHistoricoResumo[] = [];
  clientesHistoricoFiltrados: ClienteHistoricoResumo[] = [];
  clientes: ClienteResumo[] = [];
  clientesFiltrados: ClienteResumo[] = [];
  clienteSelecionado?: ClienteHistoricoResumo;
  possesCliente: HistoricoProprietario[] = [];
  historicoParaTransferencia?: HistoricoProprietario;

  termoHistorico = '';
  termoCliente = '';
  carregando = false;
  processando = false;
  erro?: string;
  mensagem?: string;
  errosCampo: Record<string, string> = {};

  transferencia: TransferenciaProprietario = this.formularioTransferenciaInicial();

  /**
   * Função: recebe os serviços utilizados pela tela de histórico de proprietários.
   * Uso no sistema: permite consultar todos os vínculos de posse e registrar transferência sem
   * acessar diretamente a API fora da camada de serviço Angular.
   */
  constructor(
    private readonly veiculoApi: VeiculoApiService,
    private readonly clienteApi: ClienteApiService,
    private readonly route: ActivatedRoute,
    private readonly cdr: ChangeDetectorRef
  ) {}

  /**
   * Função: inicializa a aba carregando histórico consolidado e clientes disponíveis.
   * Uso no sistema: prepara uma visão por cliente, evitando duplicidade quando o mesmo cliente possui
   * ou já possuiu mais de um veículo.
   */
  ngOnInit(): void {
    this.carregarDadosIniciais();
  }

  /**
   * Função: carrega todos os registros de posse e os clientes ativos para transferência.
   * Uso no sistema: mantém proprietários antigos visíveis e deixa a troca de proprietário disponível.
   */
  carregarDadosIniciais(clienteIdParaPreservar?: number): void {
    this.carregando = true;
    this.erro = undefined;
    this.atualizarTela();

    forkJoin({
      historicos: this.veiculoApi.listarHistoricoProprietariosConsolidado(),
      clientes: this.clienteApi.listar()
    }).pipe(finalize(() => {
      this.carregando = false;
      this.atualizarTela();
    })).subscribe({
      next: resultado => {
        this.historicos = [...resultado.historicos];
        this.clientes = [...resultado.clientes];
        this.clientesFiltrados = [...resultado.clientes];
        this.montarResumoClientes();
        this.pesquisarHistorico();
        this.selecionarRegistroInicial(clienteIdParaPreservar);
        this.atualizarTela();
      },
      error: e => {
        this.erro = e.message ?? 'Não foi possível carregar o histórico de proprietários.';
        this.atualizarTela();
      }
    });
  }

  /**
   * Função: aplica filtro local sobre os clientes presentes no histórico.
   * Uso no sistema: permite localizar proprietário por nome, placa, marca, modelo ou status de posse.
   */
  pesquisarHistorico(): void {
    const termo = this.normalizarTexto(this.termoHistorico);
    if (!termo) {
      this.clientesHistoricoFiltrados = [...this.clientesHistorico];
      this.atualizarTela();
      return;
    }

    this.clientesHistoricoFiltrados = this.clientesHistorico.filter(cliente => {
      const posses = this.historicos.filter(h => Number(h.clienteId) === Number(cliente.clienteId));
      const textoPosses = posses.map(h => `${h.placaVeiculo ?? ''} ${h.nomeMarcaVeiculo ?? ''} ${h.nomeModeloVeiculo ?? ''} ${this.rotuloHistorico(h)}`).join(' ');
      const texto = this.normalizarTexto(`${cliente.nomeCliente} ${cliente.veiculosResumo} ${textoPosses}`);
      return texto.includes(termo);
    });
    this.atualizarTela();
  }

  /**
   * Função: aplica filtro local sobre os clientes ativos que podem receber uma transferência.
   * Uso no sistema: facilita selecionar o novo proprietário sem duplicar cadastro de cliente.
   */
  pesquisarCliente(): void {
    const termo = this.normalizarTexto(this.termoCliente);
    if (!termo) {
      this.clientesFiltrados = [...this.clientes];
      this.atualizarTela();
      return;
    }

    this.clientesFiltrados = this.clientes.filter(c => {
      const texto = this.normalizarTexto(`${c.nome ?? ''} ${c.documento ?? ''}`);
      return texto.includes(termo);
    });
    this.atualizarTela();
  }

  /**
   * Função: seleciona um cliente do histórico e apresenta todas as posses relacionadas a ele.
   * Uso no sistema: se o cliente tem dois veículos, ele aparece uma única vez na lista e os veículos
   * aparecem agrupados no detalhe com início e fim de posse.
   */
  selecionarCliente(cliente: ClienteHistoricoResumo): void {
    this.clienteSelecionado = cliente;
    this.possesCliente = this.historicos
      .filter(h => Number(h.clienteId) === Number(cliente.clienteId))
      .sort((a, b) => this.compararPosses(a, b));
    this.historicoParaTransferencia = this.possesCliente.find(h => h.proprietarioAtual) ?? undefined;
    this.transferencia = this.formularioTransferenciaInicial();
    this.termoCliente = '';
    this.clientesFiltrados = [...this.clientes];
    this.errosCampo = {};
    this.mensagem = undefined;
    this.erro = undefined;
    this.atualizarTela();
  }

  /**
   * Função: define qual veículo atual será transferido.
   * Uso no sistema: permite transferir um veículo específico quando o cliente possui mais de um veículo.
   */
  selecionarPosseParaTransferencia(posse: HistoricoProprietario): void {
    if (!posse.proprietarioAtual) {
      this.erro = 'Somente a posse atual pode ser transferida.';
      this.atualizarTela();
      return;
    }
    this.historicoParaTransferencia = posse;
    this.transferencia = this.formularioTransferenciaInicial();
    this.errosCampo = {};
    this.mensagem = undefined;
    this.erro = undefined;
    this.atualizarTela();
  }

  /**
   * Função: registra uma nova posse para o veículo selecionado.
   * Uso no sistema: encerra o proprietário atual e cria um novo registro histórico, mantendo o cliente
   * anterior visível na consulta com data de fim de posse.
   */
  transferirProprietario(): void {
    this.mensagem = undefined;
    this.erro = undefined;

    if (!this.validarTransferencia()) {
      this.erro = 'Corrija os campos destacados antes de transferir o proprietário.';
      this.atualizarTela();
      return;
    }

    const veiculoId = Number(this.historicoParaTransferencia?.veiculoId);
    const clienteSelecionadoId = Number(this.clienteSelecionado?.clienteId ?? 0);
    this.processando = true;
    this.atualizarTela();

    this.veiculoApi.transferirProprietario(veiculoId, this.transferencia)
      .pipe(finalize(() => {
        this.processando = false;
        this.atualizarTela();
      }))
      .subscribe({
        next: () => {
          this.mensagem = 'Proprietário transferido. O histórico anterior foi preservado.';
          this.transferencia = this.formularioTransferenciaInicial();
          this.termoCliente = '';
          this.carregarDadosIniciais(clienteSelecionadoId);
        },
        error: e => {
          this.erro = e.message ?? 'Não foi possível transferir o proprietário.';
          this.atualizarTela();
        }
      });
  }

  /**
   * Função: limpa a seleção da tela de histórico.
   * Uso no sistema: permite iniciar nova consulta sem manter dados anteriores na área de detalhe.
   */
  limparSelecao(): void {
    this.clienteSelecionado = undefined;
    this.possesCliente = [];
    this.historicoParaTransferencia = undefined;
    this.transferencia = this.formularioTransferenciaInicial();
    this.errosCampo = {};
    this.mensagem = undefined;
    this.erro = undefined;
    this.atualizarTela();
  }

  /**
   * Função: monta uma descrição resumida do veículo histórico.
   * Uso no sistema: apresenta placa, marca, modelo e ano sem depender do cadastro atual do veículo.
   */
  descricaoVeiculo(posse: HistoricoProprietario | undefined): string {
    if (!posse) return 'Nenhum veículo selecionado';
    return `${posse.placaVeiculo ?? '-'} — ${posse.nomeMarcaVeiculo ?? '-'} / ${posse.nomeModeloVeiculo ?? '-'} — Ano ${posse.anoVeiculo ?? '-'}/${posse.anoModelo ?? '-'}`;
  }

  /**
   * Função: identifica visualmente se o registro histórico representa a posse atual.
   * Uso no sistema: diferencia o proprietário vigente dos proprietários anteriores.
   */
  rotuloHistorico(h: HistoricoProprietario): string {
    return h.proprietarioAtual ? 'Atual' : 'Anterior';
  }

  /**
   * Função: retorna a data de fim de posse em texto amigável.
   * Uso no sistema: deixa claro quando a posse está encerrada e quando continua vigente.
   */
  textoFimPosse(h: HistoricoProprietario): string {
    return h.dataFimPosse ? '' : 'Posse vigente';
  }

  /**
   * Função: monta a lista única de clientes com base no histórico consolidado.
   * Uso no sistema: evita duplicar o mesmo cliente quando ele possui mais de um veículo.
   */
  private montarResumoClientes(): void {
    const mapa = new Map<number, ClienteHistoricoResumo>();

    this.historicos.forEach(h => {
      const clienteId = Number(h.clienteId ?? 0);
      if (!clienteId) return;

      const existente = mapa.get(clienteId) ?? {
        clienteId,
        nomeCliente: h.nomeCliente || 'Cliente sem nome',
        totalRegistros: 0,
        totalVeiculos: 0,
        totalVeiculosAtuais: 0,
        veiculosResumo: ''
      };

      existente.totalRegistros += 1;
      if (h.proprietarioAtual) existente.totalVeiculosAtuais += 1;
      mapa.set(clienteId, existente);
    });

    const resumos = Array.from(mapa.values()).map(cliente => {
      const posses = this.historicos.filter(h => Number(h.clienteId) === Number(cliente.clienteId));
      const placas = Array.from(new Set(posses.map(h => h.placaVeiculo).filter(Boolean) as string[]));
      return {
        ...cliente,
        totalVeiculos: placas.length,
        veiculosResumo: placas.length ? placas.join(', ') : '-'
      };
    });

    this.clientesHistorico = resumos.sort((a, b) => a.nomeCliente.localeCompare(b.nomeCliente));
    this.clientesHistoricoFiltrados = [...this.clientesHistorico];
  }

  /**
   * Função: restaura a seleção depois de recarregar a tela ou abre a seleção por query param.
   * Uso no sistema: mantém a consulta estável após transferência ou ao abrir por atalho vindo de veículos.
   */
  private selecionarRegistroInicial(clienteIdParaPreservar?: number): void {
    const veiculoIdRota = Number(this.route.snapshot.queryParamMap.get('veiculoId') ?? 0);
    let clienteId = Number(clienteIdParaPreservar ?? 0);

    if (!clienteId && veiculoIdRota) {
      const posseAtualDoVeiculo = this.historicos.find(h => Number(h.veiculoId) === veiculoIdRota && h.proprietarioAtual);
      const primeiraPosseDoVeiculo = this.historicos.find(h => Number(h.veiculoId) === veiculoIdRota);
      clienteId = Number((posseAtualDoVeiculo ?? primeiraPosseDoVeiculo)?.clienteId ?? 0);
    }

    if (!clienteId && this.clienteSelecionado?.clienteId) {
      clienteId = Number(this.clienteSelecionado.clienteId);
    }

    if (!clienteId) return;
    const cliente = this.clientesHistorico.find(c => Number(c.clienteId) === clienteId);
    if (cliente) this.selecionarCliente(cliente);
  }

  /**
   * Função: valida a transferência antes de enviar para o backend.
   * Uso no sistema: evita requisições incompletas e impede transferir o veículo para o mesmo proprietário.
   */
  private validarTransferencia(): boolean {
    this.errosCampo = {};
    if (!this.historicoParaTransferencia?.veiculoId) this.errosCampo['veiculo'] = 'Selecione o veículo atual que será transferido.';
    if (!this.transferencia.novoClienteId || Number(this.transferencia.novoClienteId) <= 0) {
      this.errosCampo['novoClienteId'] = 'Selecione o novo proprietário.';
    }
    if (Number(this.transferencia.novoClienteId) === Number(this.historicoParaTransferencia?.clienteId)) {
      this.errosCampo['novoClienteId'] = 'O novo proprietário deve ser diferente do proprietário atual.';
    }
    if (!this.transferencia.dataInicioPosse) {
      this.errosCampo['dataInicioPosse'] = 'Informe a data de início da nova posse.';
    }
    return Object.keys(this.errosCampo).length === 0;
  }

  /**
   * Função: cria o formulário inicial de transferência.
   * Uso no sistema: padroniza o estado da tela ao abrir ou concluir uma transferência.
   */
  private formularioTransferenciaInicial(): TransferenciaProprietario {
    return { novoClienteId: 0, dataInicioPosse: new Date().toISOString().substring(0, 10), observacao: '' };
  }

  /**
   * Função: compara registros de posse para exibir atuais primeiro e depois históricos por veículo/data.
   * Uso no sistema: melhora a leitura quando o cliente possui mais de um veículo ou várias posses.
   */
  private compararPosses(a: HistoricoProprietario, b: HistoricoProprietario): number {
    if (Boolean(a.proprietarioAtual) !== Boolean(b.proprietarioAtual)) {
      return a.proprietarioAtual ? -1 : 1;
    }
    const placa = (a.placaVeiculo ?? '').localeCompare(b.placaVeiculo ?? '');
    if (placa !== 0) return placa;
    return (b.dataInicioPosse ?? '').localeCompare(a.dataInicioPosse ?? '');
  }

  /**
   * Função: normaliza texto usado em pesquisa local.
   * Uso no sistema: melhora a busca independentemente de acentos e letras maiúsculas.
   */
  private normalizarTexto(valor: string): string {
    return (valor ?? '').toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g, '').trim();
  }

  /**
   * Função: força atualização visual após operações assíncronas.
   * Uso no sistema: mantém cards e tabelas atualizados depois das chamadas REST.
   */
  private atualizarTela(): void {
    this.cdr.detectChanges();
  }
}
