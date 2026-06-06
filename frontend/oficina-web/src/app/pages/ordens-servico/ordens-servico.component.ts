import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { OrdemServicoApiService } from '../../core/services/ordem-servico-api.service';
import { OrdemServicoResumo, StatusFluxoOrdemServico } from '../../models/ordem-servico.model';

@Component({
  selector: 'app-ordens-servico',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './ordens-servico.component.html'
})
export class OrdensServicoComponent implements OnInit {
  ordens: OrdemServicoResumo[] = [];
  termo = '';
  ordemSelecionada?: OrdemServicoResumo;
  novoStatus: StatusFluxoOrdemServico = 'EXECUCAO';
  observacaoStatus = '';
  erro?: string;
  mensagem?: string;
  carregando = false;

  constructor(private readonly ordemApi: OrdemServicoApiService) {}

  ngOnInit(): void {
    this.listar();
  }

  listar(): void {
    this.carregando = true;
    this.erro = undefined;
    this.ordemApi.listar().subscribe({
      next: ordens => this.ordens = ordens,
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
    this.ordemApi.pesquisar(consulta).subscribe({
      next: ordens => this.ordens = ordens,
      error: error => this.erro = error.message,
      complete: () => this.carregando = false
    });
  }

  selecionar(ordem: OrdemServicoResumo): void {
    this.ordemSelecionada = ordem;
    this.mensagem = undefined;
    this.erro = undefined;
  }

  alterarStatus(): void {
    if (!this.ordemSelecionada?.id) {
      this.erro = 'Selecione uma ordem de serviço.';
      return;
    }
    this.ordemApi.alterarStatus(this.ordemSelecionada.id, {
      novoStatus: this.novoStatus,
      observacao: this.observacaoStatus
    }).subscribe({
      next: () => {
        this.mensagem = 'Status alterado com sucesso.';
        this.observacaoStatus = '';
        this.listar();
      },
      error: error => this.erro = error.message
    });
  }
}
