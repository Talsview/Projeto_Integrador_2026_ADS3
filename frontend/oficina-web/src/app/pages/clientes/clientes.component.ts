import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
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
  carregando = false;

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
    this.carregando = true;
    this.erro = undefined;
    this.clienteApi.listar().subscribe({
      next: clientes => this.clientes = clientes,
      error: error => this.erro = error.message,
      complete: () => this.carregando = false
    });
  }

  pesquisar(): void {
    const consulta = this.termo.trim();
    if (!consulta) {
      this.listar();
      return;
    }
    this.carregando = true;
    this.clienteApi.pesquisar(consulta).subscribe({
      next: clientes => this.clientes = clientes,
      error: error => this.erro = error.message,
      complete: () => this.carregando = false
    });
  }

  salvar(): void {
    this.mensagem = undefined;
    this.erro = undefined;

    if (this.tipoCliente === 'PESSOA_FISICA') {
      const payload: ClientePessoaFisica = {
        nome: this.form.nome,
        telefone: this.form.telefone,
        email: this.form.email,
        endereco: this.form.endereco,
        cpf: this.form.cpf,
        rg: this.form.rg,
        dataNascimento: this.form.dataNascimento
      };
      this.clienteApi.criarPessoaFisica(payload).subscribe(this.resultadoSalvar());
      return;
    }

    const payload: ClientePessoaJuridica = {
      nome: this.form.nome,
      telefone: this.form.telefone,
      email: this.form.email,
      endereco: this.form.endereco,
      cnpj: this.form.cnpj,
      razaoSocial: this.form.razaoSocial,
      nomeFantasia: this.form.nomeFantasia,
      inscricaoEstadual: this.form.inscricaoEstadual
    };
    this.clienteApi.criarPessoaJuridica(payload).subscribe(this.resultadoSalvar());
  }

  limpar(): void {
    this.form = {
      nome: '', telefone: '', email: '', endereco: '', cpf: '', rg: '', dataNascimento: '',
      cnpj: '', razaoSocial: '', nomeFantasia: '', inscricaoEstadual: ''
    };
  }

  private resultadoSalvar() {
    return {
      next: () => {
        this.mensagem = 'Cliente salvo com sucesso.';
        this.limpar();
        this.listar();
      },
      error: (error: Error) => this.erro = error.message
    };
  }
}
