# ADR-051 — Exportação de Relatórios para Planilha Excel

## Status

Aceito.

## Data

15/06/2026.

## Contexto

A tela de Relatórios possuía exportação simples em CSV. Embora funcional, o CSV não oferecia uma apresentação visual adequada para entrega acadêmica, conferência gerencial ou uso administrativo da oficina. O usuário solicitou que os relatórios fossem exportados para uma planilha bonita, atrativa e mais profissional.

## Decisão

Foi decidido substituir a exportação CSV por uma exportação de planilha Excel em formato `.xls`, gerada no frontend Angular por meio de XML Spreadsheet 2003.

A planilha exportada contém abas separadas para:

- Resumo Gerencial;
- Ordens de Serviço;
- Pagamentos;
- Garantias.

## Justificativa

O formato XML Spreadsheet 2003 permite criar uma planilha com múltiplas abas, estilos, cores, larguras de coluna, bordas e formatação sem adicionar dependências externas ao Angular.

Essa abordagem preserva a simplicidade do projeto, evita riscos de build com bibliotecas pesadas e atende ao objetivo de gerar uma planilha mais profissional.

## Consequências positivas

- Planilha visualmente mais organizada;
- Exportação compatível com Excel e LibreOffice;
- Dados separados por assunto;
- Não altera backend;
- Não cria nova dependência externa;
- Mantém funcionamento local do sistema.

## Consequências negativas / riscos

- O arquivo é `.xls`, não `.xlsx`.
- Algumas versões do Excel podem exibir aviso informando que o formato/extensão não correspondem a um arquivo binário tradicional. Mesmo assim, o arquivo abre corretamente por ser um formato XML aceito pelo Excel.

## Mitigações

A documentação informa que a planilha é gerada em formato XML Spreadsheet compatível com Excel. Caso seja necessário no futuro, a exportação pode evoluir para `.xlsx` usando uma biblioteca específica.

## Alternativas consideradas

- Manter CSV simples: rejeitado por baixa qualidade visual.
- Usar ExcelJS: rejeitado neste momento por adicionar dependência pesada ao frontend.
- Criar exportação no backend: rejeitado por exigir novo endpoint e maior complexidade.

## Follow-up

Em evolução futura, pode-se incluir:

- gráficos na planilha;
- exportação `.xlsx` real;
- filtros automáticos;
- abas por cliente ou por período;
- relatório financeiro detalhado.
