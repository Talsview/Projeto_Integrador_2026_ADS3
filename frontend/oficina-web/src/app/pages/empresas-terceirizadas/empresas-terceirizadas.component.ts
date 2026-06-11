import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InativosPanelComponent } from '../../shared/components/inativos-panel/inativos-panel.component';
import { finalize, switchMap } from 'rxjs';
import { EmpresaTerceirizadaApiService } from '../../core/services/empresa-terceirizada-api.service';
import { cnpjValido, formatarCnpj, somenteDigitos } from '../../core/validation/documento-validation';
import { emailValido, formatarTelefone, telefoneValido, textoCadastroValido } from '../../core/validation/field-validation';
import { EmpresaTerceirizada } from '../../models/servico.model';

@Component({ selector: 'app-empresas-terceirizadas', standalone: true, imports: [CommonModule, FormsModule, InativosPanelComponent], templateUrl: './empresas-terceirizadas.component.html' })
export class EmpresasTerceirizadasComponent implements OnInit {
  empresas: EmpresaTerceirizada[] = [];
  empresasInativos: EmpresaTerceirizada[] = [];
  mostrarInativos = false;
  carregandoInativos = false;
  termo = '';
  mensagem?: string;
  erro?: string;
  carregando = false;
  processando = false;
  form: EmpresaTerceirizada = this.formularioInicial();
  errosCampo: Record<string, string> = {};

  /**
   * Função: Recebe os serviços necessários para esta classe, como HttpClient, APIs ou dependências
   * de navegação.
   * Uso no sistema: permite que o Angular injete dependências sem criação manual dentro dos métodos.
   */
  constructor(private readonly empresaApi: EmpresaTerceirizadaApiService, private readonly cdr: ChangeDetectorRef) {}

  /**
   * Função: Inicializa a tela carregando listas, filtros e dados necessários para o primeiro uso.
   * Uso no sistema: prepara o estado visual antes da interação do usuário.
   */
  ngOnInit(): void { this.listar(); }

  listar(): void {
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    this.empresaApi.listar().pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({
      next: e => { this.empresas = [...e]; this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  /**
   * Função: Atualiza os filtros da tela e recarrega a lista com os registros compatíveis.
   * Uso no sistema: facilita localizar clientes, veículos, OS, peças ou cadastros inativos.
   */
  pesquisar(): void {
    const t = this.termo.trim();
    if (!t) { this.listar(); return; }
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    this.empresaApi.pesquisar(t).pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({
      next: e => { this.empresas = [...e]; this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  /**
   * Função: Valida os campos da tela, envia os dados para a API e atualiza a listagem após a
   * gravação.
   * Uso no sistema: concentra o fluxo de cadastro/edição iniciado pelo usuário.
   */
  salvar(): void {
    this.mensagem = undefined; this.erro = undefined;
    if (!this.validarFormulario()) { this.erro = 'Corrija os campos destacados antes de salvar a empresa terceirizada.'; this.atualizarTela(); return; }
    this.processando = true; this.atualizarTela();
    const acao = this.form.id ? this.empresaApi.atualizar(this.form.id, this.form) : this.empresaApi.criar(this.form);
    acao.pipe(switchMap(() => this.empresaApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: empresas => { this.mensagem = 'Empresa salva.'; this.empresas = [...empresas]; this.limpar(); this.atualizarTela(); },
      error: e => { this.erro = e.message; this.atualizarTela(); }
    });
  }

  editar(empresa: EmpresaTerceirizada): void { this.form = { ...empresa }; this.atualizarTela(); }

  /**
   * Função: Solicita confirmação e envia a inativação do registro para a API.
   * Uso no sistema: remove o item da listagem principal sem apagar seu histórico no banco.
   */
  excluir(empresa: EmpresaTerceirizada): void {
    if (!empresa.id) return;
    this.processando = true; this.erro = undefined; this.atualizarTela();
    this.empresaApi.excluir(empresa.id).pipe(switchMap(() => this.empresaApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: empresas => { this.mensagem = 'Empresa inativada.'; this.empresas = [...empresas]; this.atualizarTela(); },
      error: e => { this.erro = e.message; this.atualizarTela(); }
    });
  }

  limpar(): void { this.form = this.formularioInicial(); this.errosCampo = {}; this.atualizarTela(); }
  /**
   * Função: Controla na tela a etapa formatar telefone campo.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  formatarTelefoneCampo(): void {
    this.form.telefone = formatarTelefone(this.form.telefone);
    this.validarTelefoneSePreenchido();
    this.atualizarTela();
  }

  /**
   * Função: Controla na tela a etapa formatar cnpj campo.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  formatarCnpjCampo(): void {
    this.form.cnpj = formatarCnpj(this.form.cnpj);
    this.validarCnpjSePreenchido();
    this.atualizarTela();
  }

  /**
   * Função: Controla na tela a etapa validar telefone se preenchido.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  validarTelefoneSePreenchido(): void {
    if (!telefoneValido(this.form.telefone)) {
      this.errosCampo['telefone'] = 'Informe somente números no telefone, com DDD. Exemplo: (62) 99999-9999.';
      return;
    }
    delete this.errosCampo['telefone'];
  }

  /**
   * Função: Controla na tela a etapa validar cnpj se preenchido.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  validarCnpjSePreenchido(): void {
    const cnpj = somenteDigitos(this.form.cnpj);
    if (!cnpj) {
      delete this.errosCampo['cnpj'];
      return;
    }
    if (cnpj.length < 14) {
      this.errosCampo['cnpj'] = 'O CNPJ deve possuir 14 dígitos.';
      return;
    }
    if (!cnpjValido(cnpj)) {
      this.errosCampo['cnpj'] = 'CNPJ inválido. Informe um CNPJ real ou deixe o campo vazio.';
      return;
    }
    delete this.errosCampo['cnpj'];
  }

  /**
   * Função: Controla na tela a etapa validar formulario.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private validarFormulario(): boolean {
    this.errosCampo = {};
    if (!textoCadastroValido(this.form.nomeEmpresa, true)) this.errosCampo['nomeEmpresa'] = 'Informe um nome de empresa terceirizada válido.';
    const cnpj = somenteDigitos(this.form.cnpj);
    if (cnpj && cnpj.length !== 14) this.errosCampo['cnpj'] = 'O CNPJ deve possuir 14 dígitos.';
    if (cnpj && cnpj.length === 14 && !cnpjValido(cnpj)) this.errosCampo['cnpj'] = 'CNPJ inválido. Informe um CNPJ real ou deixe o campo vazio.';
    if (!telefoneValido(this.form.telefone)) this.errosCampo['telefone'] = 'Informe somente números no telefone, com DDD. Exemplo: (62) 99999-9999.';
    if (!emailValido(this.form.email)) this.errosCampo['email'] = 'Informe um e-mail válido.';
    return Object.keys(this.errosCampo).length === 0;
  }

  /**
   * Função: Controla na tela a etapa formulario inicial.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private formularioInicial(): EmpresaTerceirizada { return { nomeEmpresa: '', cnpj: '', telefone: '', email: '', endereco: '' }; }

  /**
   * Função: Controla na tela a etapa abrir inativos.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  abrirInativos(): void {
    this.mostrarInativos = true;
    this.carregarInativos();
  }

  /**
   * Função: Fecha painel, modal ou menu aberto e retorna a tela ao estado padrão.
   * Uso no sistema: controla a navegação visual sem alterar dados do banco.
   */
  fecharInativos(): void {
    this.mostrarInativos = false;
    this.atualizarTela();
  }

  /**
   * Função: Controla na tela a etapa carregar inativos.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  carregarInativos(): void {
    this.carregandoInativos = true;
    this.erro = undefined;
    this.atualizarTela();
    this.empresaApi.listarInativos()
      .pipe(finalize(() => { this.carregandoInativos = false; this.atualizarTela(); }))
      .subscribe({
        next: registros => { this.empresasInativos = [...registros]; this.atualizarTela(); },
        error: e => { this.erro = e.message ?? 'Não foi possível carregar os inativos.'; this.atualizarTela(); }
      });
  }

  /**
   * Função: Envia à API a reativação do registro escolhido na tela de inativos.
   * Uso no sistema: permite recuperar cadastros sem criar duplicidade.
   */
  ativarInativo(registro: EmpresaTerceirizada): void {
    if (!registro.id) return;
    this.processando = true;
    this.erro = undefined;
    this.mensagem = undefined;
    this.atualizarTela();
    this.empresaApi.ativar(registro.id)
      .pipe(switchMap(() => this.empresaApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); }))
      .subscribe({
        next: registros => {
          this.empresas = [...registros];
          this.mensagem = 'Cadastro ativado.';
          this.carregarInativos();
          this.atualizarTela();
        },
        error: e => { this.erro = e.message ?? 'Não foi possível ativar o cadastro.'; this.atualizarTela(); }
      });
  }

  private atualizarTela(): void { this.cdr.detectChanges(); }
}
