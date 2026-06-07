# Etapa 49 — Exportação de Relatórios para Planilha Excel

## Objetivo

Esta etapa melhora a tela **Relatórios** do frontend Angular, substituindo a exportação simples em CSV por uma planilha Excel visualmente mais profissional e organizada.

## Alterações implementadas

- Botão **Exportar CSV** substituído por **Exportar planilha Excel**.
- A exportação agora gera arquivo `.xls` compatível com Excel, LibreOffice Calc e Google Sheets.
- A planilha é gerada diretamente no navegador, sem alterar endpoints do backend.
- Não foi adicionada dependência externa, evitando aumento desnecessário do projeto.

## Abas geradas na planilha

A planilha exportada contém quatro abas:

1. **Resumo Gerencial**
   - Título do sistema;
   - Período selecionado;
   - Data e hora de geração;
   - Indicadores principais;
   - Resumo por status das Ordens de Serviço.

2. **Ordens de Serviço**
   - Número da OS;
   - Cliente;
   - Placa;
   - Veículo;
   - Status;
   - Prioridade;
   - Valor total;
   - Data de abertura;
   - Data de finalização.

3. **Pagamentos**
   - OS;
   - Forma de pagamento;
   - Status;
   - Valor pago;
   - Data do pagamento;
   - Observação.

4. **Garantias**
   - ID;
   - Tipo;
   - Item;
   - Status;
   - Prazo;
   - Data de início;
   - Data fim;
   - Data de acionamento;
   - Responsável;
   - Observação.

## Padrão visual aplicado

A planilha utiliza:

- cabeçalho azul automotivo escuro;
- destaque laranja da identidade visual da oficina;
- colunas com largura definida;
- bordas leves;
- indicadores em destaque;
- status com cores diferentes;
- abas separadas por assunto;
- valores monetários formatados.

## Arquivos alterados

```text
frontend/oficina-web/src/app/pages/relatorios/relatorios.component.ts
frontend/oficina-web/src/app/pages/relatorios/relatorios.component.html
frontend/oficina-web/package.json
README.md
```

## Observação técnica

A planilha é gerada em formato XML Spreadsheet 2003 com extensão `.xls`. Essa escolha permite formatação visual, múltiplas abas e compatibilidade com ferramentas de planilha, sem exigir bibliotecas externas como ExcelJS ou SheetJS.
