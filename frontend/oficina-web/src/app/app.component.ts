import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
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
