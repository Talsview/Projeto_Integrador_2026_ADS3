# Etapa 16 — Correção dos estados dos botões e atualização automática das tabelas no Angular

## 1. Objetivo

Esta etapa corrige o comportamento observado no frontend Angular em que alguns botões permaneciam exibindo mensagens como **Processando...** ou **Processando comunicação com o backend...**, mesmo após a operação já ter sido concluída no banco de dados.

O ajuste foi aplicado para melhorar a experiência de uso da camada View e deixar o sistema mais adequado para apresentação acadêmica e uso operacional na oficina mecânica AV CAR AUTO CENTER.

## 2. Problema identificado

Na etapa anterior, alguns componentes utilizavam a mesma variável de estado para duas responsabilidades diferentes:

```text
1. Controlar carregamento da tabela.
2. Controlar processamento de botões de salvar, excluir ou alterar status.
```

Isso fazia com que a interface pudesse ficar visualmente presa em estado de processamento, mesmo quando a comunicação com o backend já havia retornado dados.

## 3. Correção aplicada

Foram separados os estados de tela:

```text
consultando / carregando → usado para consulta e atualização de tabelas.
salvando / processando   → usado para botões de ações, como salvar, excluir, acionar ou alterar status.
```

Além disso, foi aplicado o operador `finalize` do RxJS nas chamadas HTTP, garantindo que o estado de carregamento seja encerrado tanto em caso de sucesso quanto em caso de erro.

## 4. Atualização automática das tabelas

Após ações como salvar, editar, excluir, adicionar item ou alterar status, a tela agora atualiza a tabela de duas formas:

```text
1. Atualização imediata em memória, quando a API retorna o objeto salvo.
2. Reconsulta ao backend para garantir fidelidade aos dados persistidos.
```

Com isso, ao clicar em salvar, a tabela reflete a mudança sem exigir que o usuário clique manualmente em “Listar todos”.

## 5. Componentes ajustados

Foram revisados os seguintes componentes Angular:

```text
ClientesComponent
FuncoesComponent
ColaboradoresComponent
MarcasModelosComponent
VeiculosComponent
ServicosComponent
EmpresasTerceirizadasComponent
PecasFornecedoresComponent
OrdensServicoComponent
ItensOsComponent
PagamentosComponent
GarantiasComponent
```

## 6. Arquivos técnicos alterados

```text
frontend/oficina-web/src/app/core/services/base-api.service.ts
frontend/oficina-web/src/app/core/interceptors/api-error.interceptor.ts
frontend/oficina-web/src/app/core/services/cliente-api.service.ts
frontend/oficina-web/src/app/pages/clientes/clientes.component.ts
frontend/oficina-web/src/app/pages/clientes/clientes.component.html
frontend/oficina-web/src/app/pages/funcoes/funcoes.component.ts
frontend/oficina-web/src/app/pages/colaboradores/colaboradores.component.ts
frontend/oficina-web/src/app/pages/veiculos/veiculos.component.ts
frontend/oficina-web/src/app/pages/servicos/servicos.component.ts
frontend/oficina-web/src/app/pages/ordens-servico/ordens-servico.component.ts
frontend/oficina-web/src/app/pages/marcas-modelos/marcas-modelos.component.ts
frontend/oficina-web/src/app/pages/empresas-terceirizadas/empresas-terceirizadas.component.ts
frontend/oficina-web/src/app/pages/pecas-fornecedores/pecas-fornecedores.component.ts
frontend/oficina-web/src/app/pages/pagamentos/pagamentos.component.ts
frontend/oficina-web/src/app/pages/itens-os/itens-os.component.ts
frontend/oficina-web/src/app/pages/garantias/garantias.component.ts
```

## 7. Tratamento de lentidão da API

Foi adicionado tempo limite de 10 segundos nas chamadas genéricas da API. Caso o backend ou o PostgreSQL não respondam dentro desse prazo, o frontend apresenta uma mensagem clara ao usuário.

Mensagem prevista:

```text
A API demorou para responder. Verifique se o backend e o PostgreSQL estão em execução.
```

## 8. Resultado esperado

Após esta etapa, o comportamento esperado é:

```text
1. Ao clicar em Salvar, o botão mostra “Salvando...” ou “Processando...” apenas durante a operação.
2. Ao concluir a operação, o botão volta ao estado normal.
3. A tabela é atualizada automaticamente.
4. Mensagens de processamento não ficam presas na tela.
5. Falhas de comunicação retornam mensagem clara ao usuário.
```

## 9. Justificativa acadêmica

A correção melhora a usabilidade, a responsividade e a previsibilidade da camada View, atendendo aos critérios de qualidade do projeto integrador. A separação entre estados de consulta e estados de processamento reforça a organização da interface e evita ambiguidades na comunicação entre usuário, frontend e backend.
