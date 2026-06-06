import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize, switchMap } from 'rxjs';
import { ClienteApiService } from '../../core/services/cliente-api.service';
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
    this.atualizarTela();
  }

  private montarPayloadPessoaFisica(): ClientePessoaFisica {
    return {
      nome: this.form.nome,
      telefone: this.form.telefone,
      email: this.form.email,
      endereco: this.form.endereco,
      cpf: this.form.cpf,
      rg: this.form.rg,
      dataNascimento: this.form.dataNascimento
    };
  }

  private montarPayloadPessoaJuridica(): ClientePessoaJuridica {
    return {
      nome: this.form.nome,
      telefone: this.form.telefone,
      email: this.form.email,
      endereco: this.form.endereco,
      cnpj: this.form.cnpj,
      razaoSocial: this.form.razaoSocial,
      nomeFantasia: this.form.nomeFantasia,
      inscricaoEstadual: this.form.inscricaoEstadual
    };
  }

  private atualizarTela(): void {
    this.changeDetector.detectChanges();
  }
}
