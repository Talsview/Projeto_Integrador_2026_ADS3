import { CommonModule } from '@angular/common';
import { Component, HostListener } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { environment } from '../environments/environment';
import { MenuGroup } from './core/models/menu-item.model';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {

  protected menuAberto?: string;

  /**
   * Função: Controla na tela a etapa alternar menu.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  protected alternarMenu(titulo: string, event: Event): void {
    event.preventDefault();
    event.stopPropagation();
    this.menuAberto = this.menuAberto === titulo ? undefined : titulo;
  }

  /**
   * Função: Fecha painel, modal ou menu aberto e retorna a tela ao estado padrão.
   * Uso no sistema: controla a navegação visual sem alterar dados do banco.
   */
  protected fecharMenu(): void {
    this.menuAberto = undefined;
  }

  @HostListener('document:click', ['$event'])
  /**
   * Função: Controla na tela a etapa ao clicar no documento.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  protected aoClicarNoDocumento(event: MouseEvent): void {
    this.fecharMenu();
    this.animarConteudoLongo(event.target as HTMLElement);
  }

  @HostListener('document:focusin', ['$event'])
  /**
   * Função: Controla na tela a etapa ao focar no documento.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  protected aoFocarNoDocumento(event: FocusEvent): void {
    this.animarConteudoLongo(event.target as HTMLElement);
  }

  @HostListener('document:keydown.escape')
  /**
   * Função: Fecha painel, modal ou menu aberto e retorna a tela ao estado padrão.
   * Uso no sistema: controla a navegação visual sem alterar dados do banco.
   */
  protected fecharMenuAoPressionarEscape(): void {
    this.fecharMenu();
  }

  /**
   * Função: Controla na tela a etapa animar conteudo longo.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private animarConteudoLongo(elemento: HTMLElement | null): void {
    const alvo = this.encontrarElementoAnimavel(elemento);
    if (!alvo || document.body.classList.contains('reduce-motion')) return;

    const excesso = alvo.scrollWidth - alvo.clientWidth;
    if (excesso <= 12) return;

    const texto = this.obterTextoDoElemento(alvo);
    if (texto) alvo.setAttribute('title', texto);

    alvo.classList.remove('text-overflow-animating');
    alvo.scrollLeft = 0;

    window.requestAnimationFrame(() => {
      alvo.classList.add('text-overflow-animating');
      this.animarScrollHorizontal(alvo, excesso);
    });
  }

  /**
   * Função: Controla na tela a etapa encontrar elemento animavel.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private encontrarElementoAnimavel(elemento: HTMLElement | null): HTMLElement | null {
    if (!elemento) return null;
    const seletor = 'input:not([type="checkbox"]):not([type="radio"]), textarea, select, button, .dropdown-item, .primary-shortcut, .api-link';
    if (elemento.matches(seletor)) return elemento;
    const encontrado = elemento.closest(seletor);
    return encontrado instanceof HTMLElement ? encontrado : null;
  }

  /**
   * Função: Controla na tela a etapa obter texto do elemento.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private obterTextoDoElemento(elemento: HTMLElement): string {
    if (elemento instanceof HTMLInputElement || elemento instanceof HTMLTextAreaElement) {
      return elemento.value || elemento.placeholder || '';
    }
    if (elemento instanceof HTMLSelectElement) {
      return elemento.selectedOptions?.[0]?.textContent?.trim() ?? '';
    }
    return elemento.textContent?.trim() ?? '';
  }

  /**
   * Função: Controla na tela a etapa animar scroll horizontal.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private animarScrollHorizontal(elemento: HTMLElement, excesso: number): void {
    const duracao = Math.min(2800, Math.max(1200, excesso * 28));
    const inicio = performance.now();

    const executar = (agora: number) => {
      const progresso = Math.min(1, (agora - inicio) / duracao);
      const suavizado = progresso < .5
        ? 2 * progresso * progresso
        : 1 - Math.pow(-2 * progresso + 2, 2) / 2;
      elemento.scrollLeft = excesso * suavizado;

      if (progresso < 1) {
        window.requestAnimationFrame(executar);
        return;
      }

      window.setTimeout(() => {
        elemento.scrollLeft = 0;
        elemento.classList.remove('text-overflow-animating');
      }, 450);
    };

    window.requestAnimationFrame(executar);
  }

  protected readonly swaggerUrl = environment.swaggerUrl;

  protected readonly menuGroups: MenuGroup[] = [
    {
      title: 'Operação',
      items: [
        { label: 'Ordens de Serviço', route: '/ordens-servico', icon: 'OS', description: 'Abertura e consulta' },
        { label: 'Serviços e Peças da OS', route: '/itens-os', icon: 'SP', description: 'Itens executados' },
        { label: 'Fila de Atendimento', route: '/fila-atendimento', icon: 'FA', description: 'Prioridade e chegada' },
        { label: 'Pagamentos', route: '/pagamentos', icon: 'PG', description: 'Financeiro da OS' },
        { label: 'Garantias', route: '/garantias', icon: 'GT', description: 'Peças e serviços' }
      ]
    },
    {
      title: 'Cadastros',
      items: [
        { label: 'Clientes', route: '/clientes', icon: 'CL', description: 'PF e PJ' },
        { label: 'Veículos', route: '/veiculos', icon: 'VE', description: 'Proprietários e histórico' },
        { label: 'Colaboradores', route: '/colaboradores', icon: 'CO', description: 'Equipe da oficina' },
        { label: 'Funções', route: '/funcoes', icon: 'FN', description: 'Cargos e atribuições' },
        { label: 'Marcas e Modelos', route: '/marcas-modelos', icon: 'MM', description: 'Classificação dos veículos' },
        { label: 'Serviços', route: '/servicos', icon: 'SV', description: 'Internos e terceirizados' },
        { label: 'Empresas Terceirizadas', route: '/empresas-terceirizadas', icon: 'ET', description: 'Parceiros externos' },
        { label: 'Peças e Fornecedores', route: '/pecas-fornecedores', icon: 'PF', description: 'Origem das peças' }
      ]
    },
    {
      title: 'Gestão',
      items: [
        { label: 'Relatórios', route: '/relatorios', icon: 'RL', description: 'Consultas gerenciais' },
        { label: 'Configurações', route: '/configuracoes', icon: 'CF', description: 'Ambiente local' }
      ]
    }
  ];
}
