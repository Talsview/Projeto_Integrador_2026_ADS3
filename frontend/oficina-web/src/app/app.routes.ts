import { Routes } from '@angular/router';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { ClientesComponent } from './pages/clientes/clientes.component';
import { FuncoesComponent } from './pages/funcoes/funcoes.component';
import { ColaboradoresComponent } from './pages/colaboradores/colaboradores.component';
import { MarcasModelosComponent } from './pages/marcas-modelos/marcas-modelos.component';
import { VeiculosComponent } from './pages/veiculos/veiculos.component';
import { ServicosComponent } from './pages/servicos/servicos.component';
import { EmpresasTerceirizadasComponent } from './pages/empresas-terceirizadas/empresas-terceirizadas.component';
import { PecasFornecedoresComponent } from './pages/pecas-fornecedores/pecas-fornecedores.component';
import { OrdensServicoComponent } from './pages/ordens-servico/ordens-servico.component';
import { ItensOsComponent } from './pages/itens-os/itens-os.component';
import { PagamentosComponent } from './pages/pagamentos/pagamentos.component';
import { GarantiasComponent } from './pages/garantias/garantias.component';
import { EstruturaDadosComponent } from './pages/estrutura-dados/estrutura-dados.component';
import { PadroesProjetoComponent } from './pages/padroes-projeto/padroes-projeto.component';

export const routes: Routes = [
  { path: '', component: DashboardComponent, title: 'Painel - Oficina' },
  { path: 'clientes', component: ClientesComponent, title: 'Clientes' },
  { path: 'funcoes', component: FuncoesComponent, title: 'Funções' },
  { path: 'colaboradores', component: ColaboradoresComponent, title: 'Colaboradores' },
  { path: 'marcas-modelos', component: MarcasModelosComponent, title: 'Marcas e Modelos' },
  { path: 'veiculos', component: VeiculosComponent, title: 'Veículos' },
  { path: 'servicos', component: ServicosComponent, title: 'Serviços' },
  { path: 'empresas-terceirizadas', component: EmpresasTerceirizadasComponent, title: 'Empresas Terceirizadas' },
  { path: 'pecas-fornecedores', component: PecasFornecedoresComponent, title: 'Peças e Fornecedores' },
  { path: 'ordens-servico', component: OrdensServicoComponent, title: 'Ordens de Serviço' },
  { path: 'itens-os', component: ItensOsComponent, title: 'Itens da OS' },
  { path: 'pagamentos', component: PagamentosComponent, title: 'Pagamentos' },
  { path: 'garantias', component: GarantiasComponent, title: 'Garantias' },
  { path: 'estrutura-dados', component: EstruturaDadosComponent, title: 'Estrutura de Dados' },
  { path: 'padroes-projeto', component: PadroesProjetoComponent, title: 'Padrões de Projeto' },
  { path: '**', redirectTo: '' }
];
