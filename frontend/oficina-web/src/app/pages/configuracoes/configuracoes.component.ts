import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { environment } from '../../../environments/environment';
import { DashboardService } from '../../core/services/dashboard.service';
import { DatabaseStatus } from '../../core/models/database-status.model';

interface PreferenciasSistema {
  animacoesSutis: boolean;
  modoCompacto: boolean;
  avisosOperacionais: boolean;
}

@Component({
  selector: 'app-configuracoes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './configuracoes.component.html'
})
export class ConfiguracoesComponent implements OnInit {
  readonly apiBaseUrl = environment.apiBaseUrl;
  readonly swaggerUrl = environment.swaggerUrl;
  readonly versaoSistema = 'Etapa 48';
  readonly comandos = [
    String.raw`cd C:\Users\Davi\Documents\NetBeansProjects\car-repair`,
    'mvn.cmd spring-boot:run',
    String.raw`cd C:\Users\Davi\Documents\NetBeansProjects\car-repair\frontend\oficina-web`,
    'npm.cmd start'
  ];

  statusBanco?: DatabaseStatus;
  carregandoStatus = false;
  mensagem?: string;
  erro?: string;
  preferencias: PreferenciasSistema = {
    animacoesSutis: true,
    modoCompacto: false,
    avisosOperacionais: true
  };

  constructor(
    private readonly dashboardService: DashboardService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.carregarPreferencias();
    this.aplicarPreferencias();
    this.verificarBanco();
  }

  verificarBanco(): void {
    this.carregandoStatus = true;
    this.erro = undefined;
    this.mensagem = undefined;
    this.atualizarTela();
    this.dashboardService.verificarBanco().subscribe({
      next: status => {
        this.statusBanco = status;
        this.carregandoStatus = false;
        this.mensagem = status.mensagem;
        this.atualizarTela();
      },
      error: e => {
        this.carregandoStatus = false;
        this.erro = e.message ?? 'Não foi possível verificar a conexão local.';
        this.atualizarTela();
      }
    });
  }

  salvarPreferencias(): void {
    localStorage.setItem('avcar-preferencias', JSON.stringify(this.preferencias));
    this.aplicarPreferencias();
    this.mensagem = 'Preferências salvas neste navegador.';
    this.erro = undefined;
    this.atualizarTela();
  }

  restaurarPreferencias(): void {
    this.preferencias = { animacoesSutis: true, modoCompacto: false, avisosOperacionais: true };
    localStorage.removeItem('avcar-preferencias');
    this.aplicarPreferencias();
    this.mensagem = 'Preferências restauradas para o padrão do sistema.';
    this.erro = undefined;
    this.atualizarTela();
  }

  copiarComandos(): void {
    const texto = this.comandos.join('\n');
    navigator.clipboard?.writeText(texto)
      .then(() => { this.mensagem = 'Comandos copiados para a área de transferência.'; this.erro = undefined; this.atualizarTela(); })
      .catch(() => { this.erro = 'Não foi possível copiar automaticamente. Selecione e copie os comandos manualmente.'; this.atualizarTela(); });
  }

  abrirSwagger(): void {
    window.open(this.swaggerUrl, '_blank', 'noopener');
  }

  private carregarPreferencias(): void {
    const armazenadas = localStorage.getItem('avcar-preferencias');
    if (!armazenadas) return;
    try {
      this.preferencias = { ...this.preferencias, ...JSON.parse(armazenadas) };
    } catch {
      localStorage.removeItem('avcar-preferencias');
    }
  }

  private aplicarPreferencias(): void {
    document.body.classList.toggle('reduce-motion', !this.preferencias.animacoesSutis);
    document.body.classList.toggle('compact-mode', this.preferencias.modoCompacto);
    document.body.classList.toggle('hide-operational-hints', !this.preferencias.avisosOperacionais);
  }

  private atualizarTela(): void { this.cdr.detectChanges(); }
}
