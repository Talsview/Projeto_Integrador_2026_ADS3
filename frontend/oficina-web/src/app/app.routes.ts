import { Routes } from '@angular/router';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { ClientesComponent } from './pages/clientes/clientes.component';
import { VeiculosComponent } from './pages/veiculos/veiculos.component';
import { OrdensServicoComponent } from './pages/ordens-servico/ordens-servico.component';
import { EstruturaDadosComponent } from './pages/estrutura-dados/estrutura-dados.component';
import { PadroesProjetoComponent } from './pages/padroes-projeto/padroes-projeto.component';

export const routes: Routes = [
  { path: '', component: DashboardComponent, title: 'Painel - Oficina' },
  { path: 'clientes', component: ClientesComponent, title: 'Clientes' },
  { path: 'veiculos', component: VeiculosComponent, title: 'Veículos' },
  { path: 'ordens-servico', component: OrdensServicoComponent, title: 'Ordens de Serviço' },
  { path: 'estrutura-dados', component: EstruturaDadosComponent, title: 'Estrutura de Dados' },
  { path: 'padroes-projeto', component: PadroesProjetoComponent, title: 'Padrões de Projeto' },
  { path: '**', redirectTo: '' }
];
