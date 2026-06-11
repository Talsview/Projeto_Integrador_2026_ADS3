import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize, switchMap } from 'rxjs';
import { ServicoApiService } from '../../core/services/servico-api.service';
import { numeroNaoNegativo, textoCadastroValido } from '../../core/validation/field-validation';
import { Servico, TipoServico } from '../../models/servico.model';

@Component({ selector: 'app-servicos', standalone: true, imports: [CommonModule, FormsModule], templateUrl: './servicos.component.html' })
export class ServicosComponent implements OnInit {
  servicos: Servico[] = [];
  termo = '';
  mensagem?: string;
  erro?: string;
  carregando = false;
  processando = false;
  tipos: TipoServico[] = ['INTERNO', 'TERCEIRIZADO'];
  form: Servico = this.formularioInicial();

  constructor(private readonly servicoApi: ServicoApiService, private readonly cdr: ChangeDetectorRef) {}
  ngOnInit(): void { this.listar(); }

  listar(): void {
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    this.servicoApi.listar().pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({
      next: servicos => { this.servicos = [...servicos]; this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  pesquisar(): void {
    const t = this.termo.trim();
    if (!t) { this.listar(); return; }
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    this.servicoApi.pesquisar(t).pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({
      next: s => { this.servicos = [...s]; this.atualizarTela(); },
      error: e => { this.erro = e.message; this.atualizarTela(); }
    });
  }

  salvar(): void {
    this.mensagem = undefined; this.erro = undefined;
    const erroValidacao = this.validarFormulario();
    if (erroValidacao) { this.erro = erroValidacao; this.atualizarTela(); return; }
    this.processando = true; this.atualizarTela();
    const acao = this.form.id ? this.servicoApi.atualizar(this.form.id, this.form) : this.servicoApi.criar(this.form);
    acao.pipe(switchMap(() => this.servicoApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: servicos => { this.mensagem = 'Serviço salvo.'; this.servicos = [...servicos]; this.limpar(); this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  editar(servico: Servico): void { this.form = { ...servico }; this.atualizarTela(); }

  excluir(servico: Servico): void {
    if (!servico.id) return;
    this.processando = true; this.erro = undefined; this.atualizarTela();
    this.servicoApi.excluir(servico.id).pipe(switchMap(() => this.servicoApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: servicos => { this.mensagem = 'Serviço inativado.'; this.servicos = [...servicos]; this.atualizarTela(); },
      error: e => { this.erro = e.message; this.atualizarTela(); }
    });
  }

  limpar(): void { this.form = this.formularioInicial(); this.atualizarTela(); }
  private validarFormulario(): string | undefined {
    if (!textoCadastroValido(this.form.nomeServico, true)) return 'Informe um nome de serviço válido.';
    if (!this.form.tipoServico) return 'Selecione o tipo do serviço.';
    if (!numeroNaoNegativo(this.form.valorBase)) return 'O valor base do serviço não pode ser negativo.';
    if (!numeroNaoNegativo(this.form.prazoGarantiaDias)) return 'O prazo de garantia não pode ser negativo.';
    return undefined;
  }

  private formularioInicial(): Servico { return { nomeServico: '', descricao: '', prazoGarantiaDias: 90, valorBase: 0, tipoServico: 'INTERNO', observacaoInterna: '', observacaoTerceirizacao: '' }; }
  private atualizarTela(): void { this.cdr.detectChanges(); }
}
