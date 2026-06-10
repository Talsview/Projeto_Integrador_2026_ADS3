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

  protected alternarMenu(titulo: string, event: Event): void {
    event.preventDefault();
    event.stopPropagation();
    this.menuAberto = this.menuAberto === titulo ? undefined : titulo;
  }

  protected fecharMenu(): void {
    this.menuAberto = undefined;
  }

  @HostListener('document:click', ['$event'])
  protected aoClicarNoDocumento(event: MouseEvent): void {
    this.fecharMenu();
    this.animarConteudoLongo(event.target as HTMLElement);
  }

  @HostListener('document:focusin', ['$event'])
  protected aoFocarNoDocumento(event: FocusEvent): void {
    this.animarConteudoLongo(event.target as HTMLElement);
  }

  @HostListener('document:keydown.escape')
  protected fecharMenuAoPressionarEscape(): void {
    this.fecharMenu();
  }

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

  private encontrarElementoAnimavel(elemento: HTMLElement | null): HTMLElement | null {
    if (!elemento) return null;
    const seletor = 'input:not([type="checkbox"]):not([type="radio"]), textarea, select, button, .dropdown-item, .primary-shortcut, .api-link';
    if (elemento.matches(seletor)) return elemento;
    const encontrado = elemento.closest(seletor);
    return encontrado instanceof HTMLElement ? encontrado : null;
  }

  private obterTextoDoElemento(elemento: HTMLElement): string {
    if (elemento instanceof HTMLInputElement || elemento instanceof HTMLTextAreaElement) {
      return elemento.value || elemento.placeholder || '';
    }
    if (elemento instanceof HTMLSelectElement) {
      return elemento.selectedOptions?.[0]?.textContent?.trim() ?? '';
    }
    return elemento.textContent?.trim() ?? '';
  }

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
        { label: 'Configurações', route: '/configuracoes', icon: 'CF', description: 'Ambiente local' },
        { label: 'Padrões de Projeto', route: '/padroes-projeto', icon: 'PP', description: 'Evidência acadêmica' }
      ]
    }
  ];
}
