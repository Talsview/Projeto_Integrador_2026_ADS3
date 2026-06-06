import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs';
import { MarcaApiService } from '../../core/services/marca-api.service';
import { ModeloApiService } from '../../core/services/modelo-api.service';
import { Marca, Modelo } from '../../models/veiculo.model';

@Component({
  selector: 'app-marcas-modelos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './marcas-modelos.component.html'
})
export class MarcasModelosComponent implements OnInit {
  marcas: Marca[] = [];
  modelos: Modelo[] = [];
  termoMarca = '';
  termoModelo = '';
  mensagem?: string;
  erro?: string;
  carregando = false;
  processando = false;

  marcaForm: Marca = { nomeMarca: '' };
  modeloForm: Modelo = { marcaId: 0, nomeModelo: '' };

  constructor(private readonly marcaApi: MarcaApiService, private readonly modeloApi: ModeloApiService) {}

  ngOnInit(): void { this.listarTudo(); }

  listarTudo(): void { this.listarMarcas(); this.listarModelos(); }

  listarMarcas(): void {
    this.carregando = true;
    this.marcaApi.listar().pipe(finalize(() => this.carregando = false)).subscribe({
      next: marcas => this.marcas = marcas,
      error: error => this.erro = error.message
    });
  }

  listarModelos(): void {
    this.carregando = true;
    this.modeloApi.listar().pipe(finalize(() => this.carregando = false)).subscribe({
      next: modelos => this.modelos = modelos,
      error: error => this.erro = error.message
    });
  }

  pesquisarMarcas(): void {
    const termo = this.termoMarca.trim();
    if (!termo) { this.listarMarcas(); return; }
    this.carregando = true;
    this.marcaApi.pesquisar(termo).pipe(finalize(() => this.carregando = false)).subscribe({
      next: marcas => this.marcas = marcas,
      error: error => this.erro = error.message
    });
  }

  pesquisarModelos(): void {
    const termo = this.termoModelo.trim();
    if (!termo) { this.listarModelos(); return; }
    this.carregando = true;
    this.modeloApi.pesquisar(termo).pipe(finalize(() => this.carregando = false)).subscribe({
      next: modelos => this.modelos = modelos,
      error: error => this.erro = error.message
    });
  }

  salvarMarca(): void {
    this.processando = true;
    const acao = this.marcaForm.id ? this.marcaApi.atualizar(this.marcaForm.id, this.marcaForm) : this.marcaApi.criar(this.marcaForm);
    acao.pipe(finalize(() => this.processando = false)).subscribe({
      next: marca => {
        this.mensagem = 'Marca salva com sucesso.';
        this.inserirOuAtualizarMarca(marca);
        this.marcaForm = { nomeMarca: '' };
        this.listarMarcas();
      },
      error: error => this.erro = error.message
    });
  }

  salvarModelo(): void {
    this.processando = true;
    const acao = this.modeloForm.id ? this.modeloApi.atualizar(this.modeloForm.id, this.modeloForm) : this.modeloApi.criar(this.modeloForm);
    acao.pipe(finalize(() => this.processando = false)).subscribe({
      next: modelo => {
        this.mensagem = 'Modelo salvo com sucesso.';
        this.inserirOuAtualizarModelo(modelo);
        this.modeloForm = { marcaId: 0, nomeModelo: '' };
        this.listarModelos();
      },
      error: error => this.erro = error.message
    });
  }

  editarMarca(marca: Marca): void { this.marcaForm = { ...marca }; }
  editarModelo(modelo: Modelo): void { this.modeloForm = { ...modelo }; }

  excluirMarca(marca: Marca): void {
    if (!marca.id) return;
    this.processando = true;
    this.marcaApi.excluir(marca.id).pipe(finalize(() => this.processando = false)).subscribe({
      next: () => { this.mensagem = 'Marca inativada.'; this.marcas = this.marcas.filter(item => item.id !== marca.id); this.listarMarcas(); },
      error: error => this.erro = error.message
    });
  }

  excluirModelo(modelo: Modelo): void {
    if (!modelo.id) return;
    this.processando = true;
    this.modeloApi.excluir(modelo.id).pipe(finalize(() => this.processando = false)).subscribe({
      next: () => { this.mensagem = 'Modelo inativado.'; this.modelos = this.modelos.filter(item => item.id !== modelo.id); this.listarModelos(); },
      error: error => this.erro = error.message
    });
  }

  private inserirOuAtualizarMarca(marca: Marca | null | undefined): void {
    if (!marca?.id) return;
    const existe = this.marcas.some(item => item.id === marca.id);
    this.marcas = existe ? this.marcas.map(item => item.id === marca.id ? marca : item) : [marca, ...this.marcas];
  }

  private inserirOuAtualizarModelo(modelo: Modelo | null | undefined): void {
    if (!modelo?.id) return;
    const existe = this.modelos.some(item => item.id === modelo.id);
    this.modelos = existe ? this.modelos.map(item => item.id === modelo.id ? modelo : item) : [modelo, ...this.modelos];
  }
}
