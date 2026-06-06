import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { VeiculoApiService } from '../../core/services/veiculo-api.service';
import { VeiculoResumo } from '../../models/veiculo.model';

@Component({
  selector: 'app-veiculos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './veiculos.component.html'
})
export class VeiculosComponent implements OnInit {
  veiculos: VeiculoResumo[] = [];
  termo = '';
  erro?: string;
  carregando = false;

  constructor(private readonly veiculoApi: VeiculoApiService) {}

  ngOnInit(): void {
    this.listar();
  }

  listar(): void {
    this.carregando = true;
    this.erro = undefined;
    this.veiculoApi.listar().subscribe({
      next: veiculos => this.veiculos = veiculos,
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
    this.veiculoApi.pesquisar(consulta).subscribe({
      next: veiculos => this.veiculos = veiculos,
      error: error => this.erro = error.message,
      complete: () => this.carregando = false
    });
  }
}
