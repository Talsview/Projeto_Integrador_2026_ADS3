# Etapa 60 — Comentários explicativos nos métodos e nos padrões de projeto

## Objetivo

Esta etapa teve como objetivo preparar o código-fonte para a entrega acadêmica e para a defesa oral do Projeto Integrador, deixando os métodos comentados de forma simples, direta e compreensível.

## Escopo da atualização

Foram adicionados comentários explicativos nos principais métodos do backend e do frontend, descrevendo a função de cada método no sistema.

Também foram reforçados os comentários nos trechos onde aparecem padrões de projeto, incluindo a justificativa de uso de cada padrão e sua relação com funcionalidades reais do sistema.

## Padrões de projeto comentados

Os comentários foram reforçados principalmente nos seguintes padrões:

- Singleton — monitoramento local da conexão com o PostgreSQL;
- Factory Method — criação organizada de cliente Pessoa Física e Pessoa Jurídica;
- Adapter — adaptação de VeiculoModel e histórico de proprietário para DTOs de resposta;
- Iterator — percurso da fila e da lista linear sem expor os nós internos;
- Template Method — ordenação manual com algoritmo fixo e critérios variáveis;
- Decorator — auditoria persistente em notificações operacionais.

## Critério utilizado nos comentários

Os comentários foram escritos com linguagem simples, explicando:

- o que o método faz;
- qual parte da regra do sistema ele apoia;
- quando aplicável, qual padrão de projeto está envolvido;
- por que o padrão está no sistema e qual funcionalidade real ele agrega.

## Observação importante

Esta etapa não altera regra de negócio, banco de dados, fluxo da Ordem de Serviço, telas, endpoints ou estrutura do projeto. A atualização é documental no próprio código, com foco em clareza, manutenção e apresentação acadêmica.
