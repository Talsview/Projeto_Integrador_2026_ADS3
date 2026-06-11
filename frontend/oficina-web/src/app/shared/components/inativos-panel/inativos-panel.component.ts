import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-inativos-panel',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './inativos-panel.component.html'
})
export class InativosPanelComponent {
  @Input() titulo = 'Cadastros inativos';
  @Input() descricao = 'Pesquise registros inativos e reative quando necessário.';
  @Input() registros: any[] = [];
  @Input() camposTexto: string[] = [];
  @Input() campoPrincipal = 'nome';
  @Input() campoSecundario?: string;
  @Input() carregando = false;
  @Input() processando = false;

  @Output() ativarRegistro = new EventEmitter<any>();
  @Output() fechar = new EventEmitter<void>();

  filtroTexto = '';
  dataInicial = '';
  dataFinal = '';

  registrosFiltrados(): any[] {
    const termo = this.normalizar(this.filtroTexto);
    return (this.registros ?? []).filter(registro => {
      const textoOk = !termo || this.camposParaBusca(registro).some(valor => this.normalizar(valor).includes(termo));
      const dataOk = this.filtrarPorData(registro);
      return textoOk && dataOk;
    });
  }

  rotuloPrincipal(registro: any): string {
    return this.valorCampo(registro, this.campoPrincipal) || `ID ${registro?.id ?? '-'}`;
  }

  rotuloSecundario(registro: any): string {
    if (!this.campoSecundario) {
      return '';
    }
    return this.valorCampo(registro, this.campoSecundario);
  }

  dataReferencia(registro: any): string {
    const data = registro?.dataHoraAtualizacao ?? registro?.dataHoraCriacao;
    if (!data) {
      return '-';
    }
    return new Date(data).toLocaleString('pt-BR');
  }

  limparFiltros(): void {
    this.filtroTexto = '';
    this.dataInicial = '';
    this.dataFinal = '';
  }

  ativar(registro: any): void {
    this.ativarRegistro.emit(registro);
  }

  private camposParaBusca(registro: any): string[] {
    const campos = this.camposTexto?.length ? this.camposTexto : [this.campoPrincipal, this.campoSecundario ?? ''];
    return campos.map(campo => this.valorCampo(registro, campo)).filter(Boolean);
  }

  private valorCampo(registro: any, campo?: string): string {
    if (!registro || !campo) {
      return '';
    }
    const valor = campo.split('.').reduce((obj, parte) => obj?.[parte], registro);
    return valor === undefined || valor === null ? '' : String(valor);
  }

  private filtrarPorData(registro: any): boolean {
    const dataRegistro = registro?.dataHoraAtualizacao ?? registro?.dataHoraCriacao;
    if (!this.dataInicial && !this.dataFinal) {
      return true;
    }
    if (!dataRegistro) {
      return false;
    }

    const data = new Date(dataRegistro);
    if (this.dataInicial) {
      const inicio = new Date(`${this.dataInicial}T00:00:00`);
      if (data < inicio) {
        return false;
      }
    }
    if (this.dataFinal) {
      const fim = new Date(`${this.dataFinal}T23:59:59`);
      if (data > fim) {
        return false;
      }
    }
    return true;
  }

  private normalizar(valor: any): string {
    return String(valor ?? '')
      .toLowerCase()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .trim();
  }
}
