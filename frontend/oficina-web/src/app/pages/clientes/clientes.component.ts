import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs';
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

  /**
   * Estados separados para evitar que o botão de salvar fique preso em "Processando..."
   * quando a tela estiver apenas atualizando a tabela.
   */
  consultando = false;
  salvando = false;

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

  constructor(private readonly clienteApi: ClienteApiService) {}

  ngOnInit(): void {
    this.listar();
  }

  listar(): void {
    this.consultando = true;
    this.erro = undefined;

    this.clienteApi.listar()
      .pipe(finalize(() => this.consultando = false))
      .subscribe({
        next: clientes => this.clientes = clientes,
        error: error => this.erro = error.message ?? 'Não foi possível consultar os clientes.'
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

    this.clienteApi.pesquisar(consulta)
      .pipe(finalize(() => this.consultando = false))
      .subscribe({
        next: clientes => this.clientes = clientes,
        error: error => this.erro = error.message ?? 'Não foi possível pesquisar clientes.'
      });
  }

  salvar(): void {
    this.mensagem = undefined;
    this.erro = undefined;
    this.salvando = true;

    const acao = this.tipoCliente === 'PESSOA_FISICA'
      ? this.clienteApi.criarPessoaFisica(this.montarPayloadPessoaFisica())
      : this.clienteApi.criarPessoaJuridica(this.montarPayloadPessoaJuridica());

    acao.pipe(finalize(() => this.salvando = false)).subscribe({
      next: clienteSalvo => {
        this.mensagem = 'Cliente salvo com sucesso.';
        this.inserirOuAtualizarNaTabela(clienteSalvo);
        this.limpar();

        // Atualização de conferência para manter a lista fiel ao banco, sem travar o botão Salvar.
        this.listar();
      },
      error: (error: Error) => {
        this.erro = error.message ?? 'Não foi possível salvar o cliente.';
      }
    });
  }

  limpar(): void {
    this.form = {
      nome: '', telefone: '', email: '', endereco: '', cpf: '', rg: '', dataNascimento: '',
      cnpj: '', razaoSocial: '', nomeFantasia: '', inscricaoEstadual: ''
    };
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

  private inserirOuAtualizarNaTabela(clienteSalvo: ClienteResumo | null | undefined): void {
    if (!clienteSalvo?.id) {
      return;
    }

    const indice = this.clientes.findIndex(cliente => cliente.id === clienteSalvo.id);
    if (indice >= 0) {
      this.clientes = this.clientes.map(cliente => cliente.id === clienteSalvo.id ? clienteSalvo : cliente);
      return;
    }

    this.clientes = [clienteSalvo, ...this.clientes];
  }
}
