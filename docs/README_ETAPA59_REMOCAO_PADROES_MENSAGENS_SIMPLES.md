# Etapa 59 — Remoção da tela de Padrões de Projeto e simplificação das mensagens

## Objetivo

Esta etapa ajusta a interface do sistema para deixá-la mais natural e menos carregada visualmente. A tela específica de **Padrões de Projeto** foi retirada da navegação e as mensagens de conclusão das telas foram simplificadas.

## Alterações realizadas

### 1. Remoção da página de Padrões de Projeto

Foram removidos da interface Angular:

- item **Padrões de Projeto** do menu **Gestão**;
- rota `/padroes-projeto`;
- importação do componente de padrões de projeto;
- arquivos do componente de padrões de projeto;
- modelo TypeScript usado apenas por essa tela;
- método de consulta de padrões no `DashboardService`, que não era mais utilizado pela interface.

Os padrões de projeto continuam documentados no material acadêmico e permanecem implementados no código do backend. A remoção foi apenas da tela visual.

### 2. Mensagens mais simples

As mensagens de confirmação deixaram de usar textos longos, como:

```text
Cadastro salvo com sucesso. A tabela foi atualizada automaticamente.
```

E passaram a usar mensagens objetivas:

```text
Salvo.
Atualizado.
Inativado.
Enviado para execução.
Enviado para pagamento.
PDF gerado.
```

### 3. Telas ajustadas

Foram revisadas mensagens nas principais telas do sistema:

- Clientes;
- Colaboradores;
- Funções;
- Marcas e Modelos;
- Veículos;
- Serviços;
- Empresas Terceirizadas;
- Peças e Fornecedores;
- Ordens de Serviço;
- Serviços e Peças da OS;
- Fila de Atendimento;
- Pagamentos;
- Garantias;
- Relatórios;
- Configurações;
- Dashboard;
- Interceptor global de erros da API.

### 4. Fila de Atendimento

A tela da Fila de Atendimento foi simplificada, mantendo a regra funcional:

```text
ORÇAMENTO → EXECUÇÃO → PAGAMENTO → FINALIZADO
```

A mensagem de fila vazia também foi simplificada para:

```text
Nenhuma OS em execução.
```

### 5. Relatórios

As mensagens do módulo de relatórios foram simplificadas, principalmente nos casos de falha parcial de carregamento dos dados.

## Arquivos alterados

```text
frontend/oficina-web/src/app/app.component.ts
frontend/oficina-web/src/app/app.routes.ts
frontend/oficina-web/src/app/core/services/dashboard.service.ts
frontend/oficina-web/src/app/core/interceptors/api-error.interceptor.ts
frontend/oficina-web/src/app/core/services/base-api.service.ts
frontend/oficina-web/src/app/pages/clientes/clientes.component.ts
frontend/oficina-web/src/app/pages/clientes/clientes.component.html
frontend/oficina-web/src/app/pages/colaboradores/colaboradores.component.ts
frontend/oficina-web/src/app/pages/configuracoes/configuracoes.component.ts
frontend/oficina-web/src/app/pages/dashboard/dashboard.component.ts
frontend/oficina-web/src/app/pages/empresas-terceirizadas/empresas-terceirizadas.component.ts
frontend/oficina-web/src/app/pages/estrutura-dados/estrutura-dados.component.ts
frontend/oficina-web/src/app/pages/estrutura-dados/estrutura-dados.component.html
frontend/oficina-web/src/app/pages/funcoes/funcoes.component.ts
frontend/oficina-web/src/app/pages/funcoes/funcoes.component.html
frontend/oficina-web/src/app/pages/garantias/garantias.component.ts
frontend/oficina-web/src/app/pages/itens-os/itens-os.component.ts
frontend/oficina-web/src/app/pages/itens-os/itens-os.component.html
frontend/oficina-web/src/app/pages/marcas-modelos/marcas-modelos.component.ts
frontend/oficina-web/src/app/pages/ordens-servico/ordens-servico.component.ts
frontend/oficina-web/src/app/pages/pagamentos/pagamentos.component.ts
frontend/oficina-web/src/app/pages/pagamentos/pagamentos.component.html
frontend/oficina-web/src/app/pages/pecas-fornecedores/pecas-fornecedores.component.ts
frontend/oficina-web/src/app/pages/relatorios/relatorios.component.ts
frontend/oficina-web/src/app/pages/servicos/servicos.component.ts
frontend/oficina-web/src/app/pages/veiculos/veiculos.component.ts
```

## Arquivos removidos da interface Angular

```text
frontend/oficina-web/src/app/pages/padroes-projeto/
frontend/oficina-web/src/app/models/padrao-projeto.model.ts
```

## Observação

Esta etapa não altera o banco de dados, o backend de negócio, o fluxo da OS, a regra de peças/fornecedores nem as configurações de JDK/Maven. A alteração é concentrada na interface e na forma como as mensagens são apresentadas ao usuário.
