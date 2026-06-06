# ADR-017 — Ajuste de desempenho na integração Angular e Backend

## Status

Aprovada.

## Contexto

Durante a execução do frontend Angular no VS Code, a tela inicial apresentou lentidão aparente na comunicação com o backend. O botão de verificação da API permanecia em estado de carregamento quando ocorria erro de comunicação ou demora na resposta do endpoint de banco.

O sistema precisa funcionar localmente e deve fornecer diagnóstico rápido ao usuário, pois a indisponibilidade do PostgreSQL ou do backend Spring Boot pode ser confundida com falha do frontend.

## Decisão

Foi decidido ajustar a comunicação entre Angular e backend da seguinte forma:

```text
1. Aplicar timeout de 3 segundos nas chamadas de diagnóstico do dashboard.
2. Usar finalize no Angular para encerrar o carregamento em sucesso ou erro.
3. Retornar mensagem amigável quando o backend não responder.
4. Alterar a verificação JDBC para conexão curta e com timeout de login.
5. Retornar tempo de resposta no endpoint /api/database/status.
```

## Consequências positivas

```text
A tela não fica travada em "Verificando...".
O usuário identifica rapidamente se o backend está fora do ar.
O backend evita manter conexão JDBC antiga apenas para diagnóstico.
A integração fica mais adequada para apresentação acadêmica.
O sistema melhora em usabilidade, robustez e diagnóstico operacional.
```

## Consequências negativas

```text
O timeout de 3 segundos pode indicar falha caso a máquina esteja extremamente lenta.
A verificação de banco abre uma conexão curta a cada consulta de status, mas isso é aceitável por ser um endpoint de diagnóstico.
```

## Relação com padrões de projeto

O padrão Singleton permanece aplicado em `DatabaseConnectionSingleton`, agora com responsabilidade de centralizar a verificação rápida da conexão local, sem manter conexão aberta desnecessariamente.
