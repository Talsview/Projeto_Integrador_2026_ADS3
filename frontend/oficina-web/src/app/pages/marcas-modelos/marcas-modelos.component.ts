import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize, forkJoin, switchMap } from 'rxjs';
import { MarcaApiService } from '../../core/services/marca-api.service';
import { ModeloApiService } from '../../core/services/modelo-api.service';
import { Marca, Modelo } from '../../models/veiculo.model';

@Component({ selector: 'app-marcas-modelos', standalone: true, imports: [CommonModule, FormsModule], templateUrl: './marcas-modelos.component.html' })
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

  constructor(
    private readonly marcaApi: MarcaApiService,
    private readonly modeloApi: ModeloApiService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void { this.listarTudo(); }

  listarTudo(): void {
    this.carregando = true;
    this.erro = undefined;
    this.atualizarTela();
    forkJoin({ marcas: this.marcaApi.listar(), modelos: this.modeloApi.listar() })
      .pipe(finalize(() => { this.carregando = false; this.atualizarTela(); }))
      .subscribe({
        next: resultado => { this.marcas = [...resultado.marcas]; this.modelos = [...resultado.modelos]; this.atualizarTela(); },
        error: error => { this.erro = error.message; this.atualizarTela(); }
      });
  }

  listarMarcas(): void {
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    this.marcaApi.listar().pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({
      next: marcas => { this.marcas = [...marcas]; this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  listarModelos(): void {
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    this.modeloApi.listar().pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({
      next: modelos => { this.modelos = [...modelos]; this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  pesquisarMarcas(): void {
    const termo = this.termoMarca.trim();
    if (!termo) { this.listarMarcas(); return; }
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    this.marcaApi.pesquisar(termo).pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({
      next: marcas => { this.marcas = [...marcas]; this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  pesquisarModelos(): void {
    const termo = this.termoModelo.trim();
    if (!termo) { this.listarModelos(); return; }
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    this.modeloApi.pesquisar(termo).pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({
      next: modelos => { this.modelos = [...modelos]; this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  salvarMarca(): void {
    this.mensagem = undefined; this.erro = undefined; this.processando = true; this.atualizarTela();
    const acao = this.marcaForm.id ? this.marcaApi.atualizar(this.marcaForm.id, this.marcaForm) : this.marcaApi.criar(this.marcaForm);
    acao.pipe(switchMap(() => this.marcaApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: marcas => { this.mensagem = 'Marca salva com sucesso. A lista foi atualizada automaticamente.'; this.marcas = [...marcas]; this.marcaForm = { nomeMarca: '' }; this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  salvarModelo(): void {
    this.mensagem = undefined; this.erro = undefined; this.processando = true; this.atualizarTela();
    const acao = this.modeloForm.id ? this.modeloApi.atualizar(this.modeloForm.id, this.modeloForm) : this.modeloApi.criar(this.modeloForm);
    acao.pipe(switchMap(() => this.modeloApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: modelos => { this.mensagem = 'Modelo salvo com sucesso. A lista foi atualizada automaticamente.'; this.modelos = [...modelos]; this.modeloForm = { marcaId: 0, nomeModelo: '' }; this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  editarMarca(marca: Marca): void { this.marcaForm = { ...marca }; this.atualizarTela(); }
  editarModelo(modelo: Modelo): void { this.modeloForm = { ...modelo }; this.atualizarTela(); }

  excluirMarca(marca: Marca): void {
    if (!marca.id) return;
    this.processando = true; this.erro = undefined; this.atualizarTela();
    this.marcaApi.excluir(marca.id).pipe(switchMap(() => this.marcaApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: marcas => { this.mensagem = 'Marca inativada. A lista foi atualizada automaticamente.'; this.marcas = [...marcas]; this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  excluirModelo(modelo: Modelo): void {
    if (!modelo.id) return;
    this.processando = true; this.erro = undefined; this.atualizarTela();
    this.modeloApi.excluir(modelo.id).pipe(switchMap(() => this.modeloApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: modelos => { this.mensagem = 'Modelo inativado. A lista foi atualizada automaticamente.'; this.modelos = [...modelos]; this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  private atualizarTela(): void { this.cdr.detectChanges(); }
}
