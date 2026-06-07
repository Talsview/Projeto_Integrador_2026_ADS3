import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize, switchMap } from 'rxjs';
import { ClienteApiService } from '../../core/services/cliente-api.service';
import { cnpjValido, cpfValido, formatarCnpj, formatarCpf, somenteDigitos } from '../../core/validation/documento-validation';
import { emailValido, nomePessoaValido, telefoneValido, textoCadastroValido } from '../../core/validation/field-validation';
import { ClientePessoaFisica, ClientePessoaJuridica, ClienteResumo, TipoCliente } from '../../models/cliente.model';

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './clientes.component.html'
})
export class ClientesComponent implements OnInit {
  clientes: ClienteResumo[] = [];
  termo = '';
  tipoCliente: TipoCliente = 'PESSOA_FISICA';
  mensagem?: string;
  erro?: string;
  errosCampo: Record<string, string> = {};

  consultando = false;
  salvando = false;
  sincronizandoTabela = false;

  form = {
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

  constructor(
    private readonly clienteApi: ClienteApiService,
    private readonly changeDetector: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.listar();
  }

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

    const acao = this.tipoCliente === 'PESSOA_FISICA'
      ? this.clienteApi.criarPessoaFisica(this.montarPayloadPessoaFisica())
      : this.clienteApi.criarPessoaJuridica(this.montarPayloadPessoaJuridica());

    acao.pipe(
      switchMap(() => this.clienteApi.listar()),
      finalize(() => {
        this.salvando = false;
        this.sincronizandoTabela = false;
        this.atualizarTela();
      })
    ).subscribe({
      next: clientesAtualizados => {
        this.clientes = [...clientesAtualizados];
        this.mensagem = 'Cliente salvo com sucesso. A tabela foi atualizada automaticamente.';
        this.limpar();
        this.atualizarTela();
      },
      error: (error: Error) => {
        this.erro = error.message ?? 'Não foi possível salvar o cliente.';
        this.atualizarTela();
      }
    });
  }

  limpar(): void {
    this.form = {
      nome: '', telefone: '', email: '', endereco: '', cpf: '', rg: '', dataNascimento: '',
      cnpj: '', razaoSocial: '', nomeFantasia: '', inscricaoEstadual: ''
    };
    this.errosCampo = {};
    this.atualizarTela();
  }

  aoAlterarTipoCliente(): void {
    this.errosCampo = {};
    this.erro = undefined;
    this.mensagem = undefined;
    this.atualizarTela();
  }

  formatarCpfCampo(): void {
    this.form.cpf = formatarCpf(this.form.cpf);
    this.validarCpfSePreenchido();
  }

  formatarCnpjCampo(): void {
    this.form.cnpj = formatarCnpj(this.form.cnpj);
    this.validarCnpjSePreenchido();
  }

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

  private validarFormulario(): boolean {
    this.errosCampo = {};

    if (this.tipoCliente === 'PESSOA_FISICA' && !nomePessoaValido(this.form.nome)) {
      this.errosCampo['nome'] = 'Informe um nome válido, sem números ou caracteres especiais indevidos.';
    }
    if (this.tipoCliente === 'PESSOA_JURIDICA' && !textoCadastroValido(this.form.nome, true)) {
      this.errosCampo['nome'] = 'Informe um nome válido para a empresa.';
    }

    if (!telefoneValido(this.form.telefone)) {
      this.errosCampo['telefone'] = 'Informe um telefone válido com DDD.';
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

  private dataMaiorQueHoje(data: string): boolean {
    const dataInformada = new Date(`${data}T00:00:00`);
    const hoje = new Date();
    hoje.setHours(0, 0, 0, 0);
    return dataInformada.getTime() > hoje.getTime();
  }

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

  private atualizarTela(): void {
    this.changeDetector.detectChanges();
  }
}
