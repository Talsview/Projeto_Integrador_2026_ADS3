# Etapa 62 — Revisão dos comentários técnicos do código

## Objetivo

Esta etapa revisa os comentários adicionados anteriormente, substituindo descrições genéricas por explicações mais úteis para leitura, manutenção e defesa acadêmica do sistema.

A melhoria foi solicitada porque alguns comentários estavam vagos, por exemplo: "executa a lógica do método". A partir desta etapa, os comentários procuram explicar:

- o que o método faz;
- em que parte do sistema ele é usado;
- qual regra ou fluxo ele apoia;
- quando aplicável, qual padrão de projeto está envolvido;
- por que aquele padrão foi aplicado no sistema.

## Escopo da atualização

Foram revisados comentários no backend Java e no frontend Angular, com foco em:

- Controllers;
- Services;
- Mappers;
- Validations;
- Repositories;
- componentes Angular;
- serviços Angular;
- classes de estrutura de dados;
- classes ligadas aos padrões de projeto.

## Padrões de projeto reforçados nos comentários

### Singleton

Os comentários da classe `DatabaseConnectionSingleton` foram detalhados para explicar que o padrão mantém uma única instância de monitoramento da conexão local com o PostgreSQL, armazenando diagnóstico, cache, falhas consecutivas e última mudança de status.

### Decorator

Os comentários do `NotificadorAuditoriaDecorator` foram ajustados para deixar claro que ele complementa a notificação operacional com auditoria persistida no banco, sem alterar diretamente o notificador principal.

### Factory Method

Os comentários das fábricas de cliente explicam que o padrão separa a criação de cliente Pessoa Física e Pessoa Jurídica, respeitando a especialização do modelo de dados.

### Adapter

Os comentários do adapter de veículo explicam que ele transforma o modelo interno com histórico de proprietário em DTOs mais adequados ao frontend.

### Iterator

Os comentários dos iteradores explicam que eles permitem percorrer fila e lista linear sem expor a estrutura interna.

### Template Method

Os comentários dos ordenadores explicam que o algoritmo de ordenação manual fica concentrado na classe base, enquanto as subclasses mudam apenas o critério de comparação.

## Melhorias aplicadas

- Removidas frases vagas como "executa a lógica do método".
- Substituídas descrições genéricas por comentários ligados ao domínio da oficina.
- Ajustados comentários de métodos de OS, pagamentos, garantias, veículos, peças, serviços e relatórios.
- Ajustados comentários de métodos auxiliares do PDF para explicar sua função visual no documento.
- Ajustados comentários dos métodos de estrutura de dados para explicar fila, busca, ordenação e recursividade.
- Mantido o código funcional sem alteração de regra de negócio.

## Observação técnica

Esta etapa altera somente comentários e documentação. Não houve mudança em:

- banco de dados;
- endpoints;
- regras de negócio;
- telas;
- fluxo da Ordem de Serviço;
- JDK;
- Maven;
- dependências.
