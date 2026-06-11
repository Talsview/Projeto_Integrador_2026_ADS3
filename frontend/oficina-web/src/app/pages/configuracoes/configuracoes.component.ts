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
  readonly versaoSistema = 'Etapa 57';
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

  /**
   * Função: Recebe os serviços necessários para esta classe, como HttpClient, APIs ou dependências
   * de navegação.
   * Uso no sistema: permite que o Angular injete dependências sem criação manual dentro dos métodos.
   */
  constructor(
    private readonly dashboardService: DashboardService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  /**
   * Função: Inicializa a tela carregando listas, filtros e dados necessários para o primeiro uso.
   * Uso no sistema: prepara o estado visual antes da interação do usuário.
   */
  ngOnInit(): void {
    this.carregarPreferencias();
    this.aplicarPreferencias();
    this.verificarBanco();
  }

  /**
   * Função: Controla na tela a etapa verificar banco.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
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

  /**
   * Função: Valida os campos da tela, envia os dados para a API e atualiza a listagem após a
   * gravação.
   * Uso no sistema: concentra o fluxo de cadastro/edição iniciado pelo usuário.
   */
  salvarPreferencias(): void {
    localStorage.setItem('avcar-preferencias', JSON.stringify(this.preferencias));
    this.aplicarPreferencias();
    this.mensagem = 'Preferências salvas.';
    this.erro = undefined;
    this.atualizarTela();
  }

  /**
   * Função: Controla na tela a etapa restaurar preferencias.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  restaurarPreferencias(): void {
    this.preferencias = { animacoesSutis: true, modoCompacto: false, avisosOperacionais: true };
    localStorage.removeItem('avcar-preferencias');
    this.aplicarPreferencias();
    this.mensagem = 'Preferências restauradas.';
    this.erro = undefined;
    this.atualizarTela();
  }

  /**
   * Função: Controla na tela a etapa copiar comandos.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  copiarComandos(): void {
    const texto = this.comandos.join('\n');
    navigator.clipboard?.writeText(texto)
      .then(() => { this.mensagem = 'Copiado.'; this.erro = undefined; this.atualizarTela(); })
      .catch(() => { this.erro = 'Não foi possível copiar. Copie manualmente.'; this.atualizarTela(); });
  }

  /**
   * Função: Controla na tela a etapa abrir swagger.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  abrirSwagger(): void {
    window.open(this.swaggerUrl, '_blank', 'noopener');
  }

  /**
   * Função: Controla na tela a etapa carregar preferencias.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private carregarPreferencias(): void {
    const armazenadas = localStorage.getItem('avcar-preferencias');
    if (!armazenadas) return;
    try {
      this.preferencias = { ...this.preferencias, ...JSON.parse(armazenadas) };
    } catch {
      localStorage.removeItem('avcar-preferencias');
    }
  }

  /**
   * Função: Controla na tela a etapa aplicar preferencias.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private aplicarPreferencias(): void {
    document.body.classList.toggle('reduce-motion', !this.preferencias.animacoesSutis);
    document.body.classList.toggle('compact-mode', this.preferencias.modoCompacto);
    document.body.classList.toggle('hide-operational-hints', !this.preferencias.avisosOperacionais);
  }

  /**
   * Função: Controla na tela a etapa atualizar tela.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private atualizarTela(): void { this.cdr.detectChanges(); }
}
