# Etapa 30 — Layout superior quadrado do Angular

## Objetivo

Refatorar o frontend Angular para atender à solicitação de abandonar o menu lateral e utilizar navegação por abas na parte superior da aplicação, deixando o sistema com visual mais quadrado, simples e próximo de um sistema administrativo de cadastro de oficina mecânica.

## Alterações realizadas

```text
1. Substituição da sidebar lateral por cabeçalho superior.
2. Criação de navegação horizontal por abas.
3. Agrupamento das abas em Operação, Cadastros e Gestão.
4. Redução de bordas arredondadas em cards, botões, campos e tabelas.
5. Remoção de elementos visuais excessivamente decorativos.
6. Ajuste do painel inicial para evitar sobreposição de textos nos cards.
7. Preservação das rotas, endpoints e services existentes.
```

## Organização das abas superiores

```text
Início
Operação: Ordens de Serviço, Serviços e Peças da OS, Fila de Atendimento, Pagamentos, Garantias
Cadastros: Clientes, Veículos, Colaboradores, Funções, Marcas e Modelos, Serviços, Empresas Terceirizadas, Peças e Fornecedores
Gestão: Relatórios, Configurações
```

## Observação técnica

A alteração foi concentrada principalmente no `AppComponent`, nos estilos globais e no painel inicial. O objetivo foi mudar a experiência visual e a navegação sem alterar a integração já estabilizada entre Angular, backend Spring Boot e PostgreSQL.
