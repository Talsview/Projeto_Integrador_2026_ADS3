import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs';
import { ColaboradorApiService } from '../../core/services/colaborador-api.service';
import { EmpresaTerceirizadaApiService } from '../../core/services/empresa-terceirizada-api.service';
import { FornecedorApiService } from '../../core/services/fornecedor-api.service';
import { ItemPecaApiService } from '../../core/services/item-peca-api.service';
import { ItemServicoApiService } from '../../core/services/item-servico-api.service';
import { OrdemServicoApiService } from '../../core/services/ordem-servico-api.service';
import { PecaApiService } from '../../core/services/peca-api.service';
import { ServicoApiService } from '../../core/services/servico-api.service';
import { ColaboradorResumo } from '../../models/pessoa.model';
import { Fornecedor, ItemPeca, Peca } from '../../models/peca.model';
import { ItemServico, OrdemServicoResumo } from '../../models/ordem-servico.model';
import { EmpresaTerceirizada, Servico } from '../../models/servico.model';

@Component({ selector: 'app-itens-os', standalone: true, imports: [CommonModule, FormsModule], templateUrl: './itens-os.component.html' })
export class ItensOsComponent implements OnInit {
  ordens: OrdemServicoResumo[] = [];
  servicos: Servico[] = [];
  colaboradores: ColaboradorResumo[] = [];
  empresas: EmpresaTerceirizada[] = [];
  pecas: Peca[] = [];
  fornecedores: Fornecedor[] = [];
  itensServico: ItemServico[] = [];
  itensPeca: ItemPeca[] = [];
  idOrdemSelecionada = 0;
  mensagem?: string;
  erro?: string;
  carregando = false;
  processando = false;

  itemServicoForm: ItemServico = { idOrdemServico: 0, idServico: 0, idColaborador: 0, quantidade: 1, valorUnitario: 0, descricaoExecucao: '', idEmpresaTerceirizada: undefined };
  itemPecaForm: ItemPeca = { idOrdemServico: 0, idPeca: 0, idFornecedor: 0, quantidade: 1, valorUnitario: 0, observacao: '' };

  constructor(
    private readonly ordemApi: OrdemServicoApiService,
    private readonly servicoApi: ServicoApiService,
    private readonly colaboradorApi: ColaboradorApiService,
    private readonly empresaApi: EmpresaTerceirizadaApiService,
    private readonly pecaApi: PecaApiService,
    private readonly fornecedorApi: FornecedorApiService,
    private readonly itemServicoApi: ItemServicoApiService,
    private readonly itemPecaApi: ItemPecaApiService
  ) {}

  ngOnInit(): void { this.carregarApoio(); }
  carregarApoio(): void { this.ordemApi.listar().subscribe({ next: o => this.ordens = o }); this.servicoApi.listar().subscribe({ next: s => this.servicos = s }); this.colaboradorApi.listar().subscribe({ next: c => this.colaboradores = c }); this.empresaApi.listar().subscribe({ next: e => this.empresas = e }); this.pecaApi.listar().subscribe({ next: p => this.pecas = p }); this.fornecedorApi.listar().subscribe({ next: f => this.fornecedores = f }); }
  carregarItens(): void {
    if (!this.idOrdemSelecionada) return;
    this.carregando = true;
    this.itemServicoApi.listarPorOrdemServico(this.idOrdemSelecionada).pipe(finalize(() => this.carregando = false)).subscribe({ next: i => this.itensServico = i, error: e => this.erro = e.message });
    this.itemPecaApi.listarPorOrdemServico(this.idOrdemSelecionada).subscribe({ next: i => this.itensPeca = i, error: e => this.erro = e.message });
    this.itemServicoForm.idOrdemServico = this.idOrdemSelecionada;
    this.itemPecaForm.idOrdemServico = this.idOrdemSelecionada;
  }
  salvarItemServico(): void {
    const payload = { ...this.itemServicoForm, idOrdemServico: this.idOrdemSelecionada };
    this.processando = true;
    this.itemServicoApi.criar(payload).pipe(finalize(() => this.processando = false)).subscribe({ next: item => { this.mensagem = 'Serviço incluído na OS.'; this.itensServico = item?.id ? [item, ...this.itensServico] : this.itensServico; this.itemServicoForm = { idOrdemServico: this.idOrdemSelecionada, idServico: 0, idColaborador: 0, quantidade: 1, valorUnitario: 0, descricaoExecucao: '' }; this.carregarItens(); }, error: e => this.erro = e.message });
  }
  salvarItemPeca(): void {
    const payload = { ...this.itemPecaForm, idOrdemServico: this.idOrdemSelecionada };
    this.processando = true;
    this.itemPecaApi.criar(payload).pipe(finalize(() => this.processando = false)).subscribe({ next: item => { this.mensagem = 'Peça incluída na OS.'; this.itensPeca = item?.id ? [item, ...this.itensPeca] : this.itensPeca; this.itemPecaForm = { idOrdemServico: this.idOrdemSelecionada, idPeca: 0, idFornecedor: 0, quantidade: 1, valorUnitario: 0, observacao: '' }; this.carregarItens(); }, error: e => this.erro = e.message });
  }
  excluirItemServico(item: ItemServico): void { if (!item.id) return; this.processando = true; this.itemServicoApi.excluir(item.id).pipe(finalize(() => this.processando = false)).subscribe({ next: () => { this.itensServico = this.itensServico.filter(i => i.id !== item.id); this.carregarItens(); }, error: e => this.erro = e.message }); }
  excluirItemPeca(item: ItemPeca): void { if (!item.id) return; this.processando = true; this.itemPecaApi.excluir(item.id).pipe(finalize(() => this.processando = false)).subscribe({ next: () => { this.itensPeca = this.itensPeca.filter(i => i.id !== item.id); this.carregarItens(); }, error: e => this.erro = e.message }); }
}
