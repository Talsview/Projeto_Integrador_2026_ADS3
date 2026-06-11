import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InativosPanelComponent } from '../../shared/components/inativos-panel/inativos-panel.component';
import { finalize, switchMap } from 'rxjs';
import { ClienteApiService } from '../../core/services/cliente-api.service';
import { cnpjValido, cpfValido, formatarCnpj, formatarCpf, somenteDigitos } from '../../core/validation/documento-validation';
import { emailValido, formatarTelefone, nomePessoaValido, telefoneValido, textoCadastroValido } from '../../core/validation/field-validation';
import { ClienteDetalhe, ClientePessoaFisica, ClientePessoaJuridica, ClienteResumo, TipoCliente } from '../../models/cliente.model';

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [CommonModule, FormsModule, InativosPanelComponent],
  templateUrl: './clientes.component.html'
})
export class ClientesComponent implements OnInit {
  clientes: ClienteResumo[] = [];
  clientesInativos: ClienteResumo[] = [];
  mostrarInativos = false;
  carregandoInativos = false;
  termo = '';
  tipoCliente: TipoCliente = 'PESSOA_FISICA';
  mensagem?: string;
  erro?: string;
  errosCampo: Record<string, string> = {};

  consultando = false;
  salvando = false;
  sincronizandoTabela = false;
  clienteEditandoId?: number;

  form = this.formularioInicial();

  /**
   * Função: Recebe os serviços necessários para esta classe, como HttpClient, APIs ou dependências
   * de navegação.
   * Uso no sistema: permite que o Angular injete dependências sem criação manual dentro dos métodos.
   */
  constructor(
    private readonly clienteApi: ClienteApiService,
    private readonly changeDetector: ChangeDetectorRef
  ) {}

  /**
   * Função: Inicializa a tela carregando listas, filtros e dados necessários para o primeiro uso.
   * Uso no sistema: prepara o estado visual antes da interação do usuário.
   */
  ngOnInit(): void {
    this.listar();
  }

  /**
   * Função: Controla na tela a etapa listar.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  listar(): void {
    this.consultando = true;
    this.erro = undefined;
    this.atualizarTela();

    this.clienteApi.listar()
      .pipe(finalize(() => {
        this.consultando = false;
        this.atualizarTela();
      }))
      .subscribe({
        next: clientes => {
          this.clientes = [...clientes];
          this.atualizarTela();
        },
        error: error => {
          this.erro = error.message ?? 'Não foi possível consultar os clientes.';
          this.atualizarTela();
        }
      });
  }

  /**
   * Função: Atualiza os filtros da tela e recarrega a lista com os registros compatíveis.
   * Uso no sistema: facilita localizar clientes, veículos, OS, peças ou cadastros inativos.
   */
  pesquisar(): void {
    const consulta = this.termo.trim();
    if (!consulta) {
      this.listar();
      return;
    }

    this.consultando = true;
    this.erro = undefined;
    this.atualizarTela();

    this.clienteApi.pesquisar(consulta)
      .pipe(finalize(() => {
        this.consultando = false;
        this.atualizarTela();
      }))
      .subscribe({
        next: clientes => {
          this.clientes = [...clientes];
          this.atualizarTela();
        },
        error: error => {
          this.erro = error.message ?? 'Não foi possível pesquisar clientes.';
          this.atualizarTela();
        }
      });
  }

  /**
   * Função: Valida os campos da tela, envia os dados para a API e atualiza a listagem após a
   * gravação.
   * Uso no sistema: concentra o fluxo de cadastro/edição iniciado pelo usuário.
   */
  salvar(): void {
    this.mensagem = undefined;
    this.erro = undefined;

    if (!this.validarFormulario()) {
      this.erro = 'Corrija os campos destacados antes de salvar o cliente.';
      this.atualizarTela();
      return;
    }

    this.salvando = true;
    this.sincronizandoTabela = true;
    this.atualizarTela();

    const acao = this.montarAcaoSalvarOuAtualizar();

    acao.pipe(
      switchMap(() => this.clienteApi.listar()),
      finalize(() => {
        this.salvando = false;
        this.sincronizandoTabela = false;
        this.atualizarTela();
      })
    ).subscribe({
      next: clientesAtualizados => {
        const editando = Boolean(this.clienteEditandoId);
        this.clientes = [...clientesAtualizados];
        this.limpar(false);
        this.mensagem = editando
          ? 'Cliente atualizado.'
          : 'Cliente cadastrado.';
        this.atualizarTela();
      },
      error: (error: Error) => {
        this.erro = error.message ?? 'Não foi possível salvar o cliente.';
        this.atualizarTela();
      }
    });
  }

  /**
   * Função: Carrega os dados selecionados para o formulário, permitindo conferência ou alteração.
   * Uso no sistema: evita redigitação e mantém a edição vinculada ao registro correto.
   */
  editar(cliente: ClienteResumo): void {
    if (!cliente.id) return;

    this.consultando = true;
    this.erro = undefined;
    this.mensagem = undefined;
    this.errosCampo = {};
    this.atualizarTela();

    this.clienteApi.buscarDetalhado(cliente.id)
      .pipe(finalize(() => {
        this.consultando = false;
        this.atualizarTela();
      }))
      .subscribe({
        next: detalhe => this.preencherFormularioEdicao(detalhe),
        error: error => {
          this.erro = error.message ?? 'Não foi possível carregar o cliente para edição.';
          this.atualizarTela();
        }
      });
  }

  /**
   * Função: Solicita confirmação e envia a inativação do registro para a API.
   * Uso no sistema: remove o item da listagem principal sem apagar seu histórico no banco.
   */
  inativar(cliente: ClienteResumo): void {
    if (!cliente.id) return;
    const confirmou = window.confirm(`Deseja realmente inativar o cliente ${cliente.nome ?? ''}?`);
    if (!confirmou) return;

    this.consultando = true;
    this.erro = undefined;
    this.mensagem = undefined;
    this.atualizarTela();

    this.clienteApi.inativarEListar(cliente.id)
      .pipe(finalize(() => {
        this.consultando = false;
        this.atualizarTela();
      }))
      .subscribe({
        next: clientesAtualizados => {
          this.clientes = [...clientesAtualizados];
          this.mensagem = 'Cliente inativado.';
          if (this.clienteEditandoId === cliente.id) {
            this.limpar(false);
          }
          this.atualizarTela();
        },
        error: error => {
          this.erro = error.message ?? 'Não foi possível inativar o cliente.';
          this.atualizarTela();
        }
      });
  }


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
    this.clienteApi.listarInativos()
      .pipe(finalize(() => { this.carregandoInativos = false; this.atualizarTela(); }))
      .subscribe({
        next: registros => { this.clientesInativos = [...registros]; this.atualizarTela(); },
        error: e => { this.erro = e.message ?? 'Não foi possível carregar os inativos.'; this.atualizarTela(); }
      });
  }

  /**
   * Função: Envia à API a reativação do registro escolhido na tela de inativos.
   * Uso no sistema: permite recuperar cadastros sem criar duplicidade.
   */
  ativarInativo(registro: ClienteResumo): void {
    if (!registro.id) return;
    this.consultando = true;
    this.erro = undefined;
    this.mensagem = undefined;
    this.atualizarTela();
    this.clienteApi.ativar(registro.id)
      .pipe(switchMap(() => this.clienteApi.listar()), finalize(() => { this.consultando = false; this.atualizarTela(); }))
      .subscribe({
        next: registros => {
          this.clientes = [...registros];
          this.mensagem = 'Cadastro ativado.';
          this.carregarInativos();
          this.atualizarTela();
        },
        error: e => { this.erro = e.message ?? 'Não foi possível ativar o cadastro.'; this.atualizarTela(); }
      });
  }

  /**
   * Função: Limpa formulário, filtros ou estados temporários usados na tela.
   * Uso no sistema: permite iniciar um novo cadastro ou consulta sem dados anteriores interferindo.
   */
  limpar(limparMensagens = true): void {
    this.form = this.formularioInicial();
    this.errosCampo = {};
    this.clienteEditandoId = undefined;
    this.tipoCliente = 'PESSOA_FISICA';
    if (limparMensagens) {
      this.mensagem = undefined;
      this.erro = undefined;
    }
    this.atualizarTela();
  }

  /**
   * Função: Controla na tela a etapa ao alterar tipo cliente.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  aoAlterarTipoCliente(): void {
    if (this.clienteEditandoId) {
      return;
    }
    this.errosCampo = {};
    this.erro = undefined;
    this.mensagem = undefined;
    this.atualizarTela();
  }

  /**
   * Função: Controla na tela a etapa formatar cpf campo.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  formatarCpfCampo(): void {
    this.form.cpf = formatarCpf(this.form.cpf);
    this.validarCpfSePreenchido();
  }

  /**
   * Função: Controla na tela a etapa formatar cnpj campo.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  formatarCnpjCampo(): void {
    this.form.cnpj = formatarCnpj(this.form.cnpj);
    this.validarCnpjSePreenchido();
  }

  /**
   * Função: Controla na tela a etapa formatar telefone campo.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  formatarTelefoneCampo(): void {
    this.form.telefone = formatarTelefone(this.form.telefone);
    this.validarTelefoneSePreenchido();
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
   * Função: Controla na tela a etapa validar cpf se preenchido.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  validarCpfSePreenchido(): void {
    const cpf = somenteDigitos(this.form.cpf);
    if (!cpf) {
      delete this.errosCampo['cpf'];
      return;
    }
    if (cpf.length === 11 && !cpfValido(cpf)) {
      this.errosCampo['cpf'] = 'CPF inválido. Informe um CPF real, com dígitos verificadores válidos.';
      return;
    }
    if (cpf.length > 0 && cpf.length < 11) {
      this.errosCampo['cpf'] = 'O CPF deve possuir 11 dígitos.';
      return;
    }
    delete this.errosCampo['cpf'];
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
    if (cnpj.length === 14 && !cnpjValido(cnpj)) {
      this.errosCampo['cnpj'] = 'CNPJ inválido. Informe um CNPJ real, com dígitos verificadores válidos.';
      return;
    }
    if (cnpj.length > 0 && cnpj.length < 14) {
      this.errosCampo['cnpj'] = 'O CNPJ deve possuir 14 dígitos.';
      return;
    }
    delete this.errosCampo['cnpj'];
  }

  /**
   * Função: Valida os campos da tela, envia os dados para a API e atualiza a listagem após a
   * gravação.
   * Uso no sistema: concentra o fluxo de cadastro/edição iniciado pelo usuário.
   */
  private montarAcaoSalvarOuAtualizar() {
    if (this.tipoCliente === 'PESSOA_FISICA') {
      const payload = this.montarPayloadPessoaFisica();
      return this.clienteEditandoId
        ? this.clienteApi.atualizarPessoaFisica(this.clienteEditandoId, payload)
        : this.clienteApi.criarPessoaFisica(payload);
    }

    const payload = this.montarPayloadPessoaJuridica();
    return this.clienteEditandoId
      ? this.clienteApi.atualizarPessoaJuridica(this.clienteEditandoId, payload)
      : this.clienteApi.criarPessoaJuridica(payload);
  }

  /**
   * Função: Controla na tela a etapa preencher formulario edicao.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private preencherFormularioEdicao(detalhe: ClienteDetalhe): void {
    this.clienteEditandoId = detalhe.id;
    this.tipoCliente = detalhe.tipoCliente ?? 'PESSOA_FISICA';
    this.form = {
      nome: detalhe.nome ?? '',
      telefone: formatarTelefone(detalhe.telefone ?? ''),
      email: detalhe.email ?? '',
      endereco: detalhe.endereco ?? '',
      cpf: detalhe.cpf ? formatarCpf(detalhe.cpf) : '',
      rg: detalhe.rg ?? '',
      dataNascimento: detalhe.dataNascimento ?? '',
      cnpj: detalhe.cnpj ? formatarCnpj(detalhe.cnpj) : '',
      razaoSocial: detalhe.razaoSocial ?? '',
      nomeFantasia: detalhe.nomeFantasia ?? '',
      inscricaoEstadual: detalhe.inscricaoEstadual ?? ''
    };
    this.atualizarTela();
  }

  /**
   * Função: Controla na tela a etapa formulario inicial.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private formularioInicial() {
    return {
      nome: '',
      telefone: '',
      email: '',
      endereco: '',
      cpf: '',
      rg: '',
      dataNascimento: '',
      cnpj: '',
      razaoSocial: '',
      nomeFantasia: '',
      inscricaoEstadual: ''
    };
  }

  /**
   * Função: Controla na tela a etapa validar formulario.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private validarFormulario(): boolean {
    this.errosCampo = {};

    if (this.tipoCliente === 'PESSOA_FISICA' && !nomePessoaValido(this.form.nome)) {
      this.errosCampo['nome'] = 'Informe um nome válido, sem números ou caracteres especiais indevidos.';
    }
    if (this.tipoCliente === 'PESSOA_JURIDICA' && !textoCadastroValido(this.form.nome, true)) {
      this.errosCampo['nome'] = 'Informe um nome válido para a empresa.';
    }

    if (!telefoneValido(this.form.telefone)) {
      this.errosCampo['telefone'] = 'Informe somente números no telefone, com DDD. Exemplo: (62) 99999-9999.';
    }

    if (!emailValido(this.form.email)) {
      this.errosCampo['email'] = 'Informe um e-mail válido.';
    }

    if (this.tipoCliente === 'PESSOA_FISICA') {
      const cpf = somenteDigitos(this.form.cpf);
      if (!cpf) {
        this.errosCampo['cpf'] = 'O CPF é obrigatório para pessoa física.';
      } else if (cpf.length !== 11) {
        this.errosCampo['cpf'] = 'O CPF deve possuir 11 dígitos.';
      } else if (!cpfValido(cpf)) {
        this.errosCampo['cpf'] = 'CPF inválido. Informe um CPF real, com dígitos verificadores válidos.';
      }

      if (this.form.dataNascimento && this.dataMaiorQueHoje(this.form.dataNascimento)) {
        this.errosCampo['dataNascimento'] = 'A data de nascimento não pode ser futura.';
      }
    }

    if (this.tipoCliente === 'PESSOA_JURIDICA') {
      const cnpj = somenteDigitos(this.form.cnpj);
      if (!cnpj) {
        this.errosCampo['cnpj'] = 'O CNPJ é obrigatório para pessoa jurídica.';
      } else if (cnpj.length !== 14) {
        this.errosCampo['cnpj'] = 'O CNPJ deve possuir 14 dígitos.';
      } else if (!cnpjValido(cnpj)) {
        this.errosCampo['cnpj'] = 'CNPJ inválido. Informe um CNPJ real, com dígitos verificadores válidos.';
      }

      if (!this.form.razaoSocial.trim()) {
        this.errosCampo['razaoSocial'] = 'A razão social é obrigatória para pessoa jurídica.';
      }
    }

    return Object.keys(this.errosCampo).length === 0;
  }

  /**
   * Função: Controla na tela a etapa data maior que hoje.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private dataMaiorQueHoje(data: string): boolean {
    const dataInformada = new Date(`${data}T00:00:00`);
    const hoje = new Date();
    hoje.setHours(0, 0, 0, 0);
    return dataInformada.getTime() > hoje.getTime();
  }

  /**
   * Função: Controla na tela a etapa montar payload pessoa fisica.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private montarPayloadPessoaFisica(): ClientePessoaFisica {
    return {
      nome: this.form.nome.trim(),
      telefone: this.form.telefone.trim(),
      email: this.form.email.trim(),
      endereco: this.form.endereco.trim(),
      cpf: somenteDigitos(this.form.cpf),
      rg: this.form.rg.trim(),
      dataNascimento: this.form.dataNascimento
    };
  }

  /**
   * Função: Controla na tela a etapa montar payload pessoa juridica.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private montarPayloadPessoaJuridica(): ClientePessoaJuridica {
    return {
      nome: this.form.nome.trim(),
      telefone: this.form.telefone.trim(),
      email: this.form.email.trim(),
      endereco: this.form.endereco.trim(),
      cnpj: somenteDigitos(this.form.cnpj),
      razaoSocial: this.form.razaoSocial.trim(),
      nomeFantasia: this.form.nomeFantasia.trim(),
      inscricaoEstadual: this.form.inscricaoEstadual.trim()
    };
  }

  /**
   * Função: Controla na tela a etapa atualizar tela.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private atualizarTela(): void {
    this.changeDetector.detectChanges();
  }
}
