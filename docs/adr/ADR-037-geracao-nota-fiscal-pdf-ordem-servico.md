# ADR-037 - Geração de Nota Fiscal / Recibo em PDF por Ordem de Serviço

## Status

Aprovada.

## Contexto

O sistema da oficina mecânica AV CAR AUTO CENTER precisa gerar documentos em PDF baseados nas Ordens de Serviço registradas. Os documentos enviados pela oficina possuem estrutura operacional composta por cabeçalho da empresa, dados do cliente, dados do veículo, peças, serviços, totais e assinaturas.

A aplicação deve continuar funcionando em ambiente local, com backend Spring Boot e frontend Angular, sem alterar os endpoints já existentes.

## Decisão

Foi criada uma nova funcionalidade de geração de PDF no backend por meio do pacote:

```text
br.com.avcar.oficina.business.notafiscal
```

O endpoint criado foi:

```http
GET /api/notas-fiscais/ordens-servico/{id}/pdf
```

A geração do PDF foi implementada com a biblioteca OpenPDF, adicionada ao `pom.xml`.

No frontend Angular, foi adicionado o botão **Nota PDF** na listagem de Ordens de Serviço, permitindo baixar o documento gerado para a OS selecionada.

## Justificativa

A geração no backend garante maior integridade e rastreabilidade, pois o PDF é montado diretamente a partir dos dados persistidos no banco: cliente, veículo, peças, fornecedores, serviços, colaboradores, pagamentos e status da OS.

Essa abordagem evita que o frontend tenha que reconstruir regras de negócio ou montar documentos fiscais com dados incompletos.

## Consequências

### Positivas

- Geração centralizada e padronizada de PDF.
- Preservação das regras de negócio no backend.
- Integração direta com a tela de Ordens de Serviço.
- Documento com aparência próxima aos modelos de OS reais fornecidos.
- Melhor rastreabilidade entre OS, cliente, veículo, serviços, peças, fornecedor e pagamento.

### Atenções

- O documento gerado é um comprovante/nota simplificada interna.
- Ele não substitui emissão fiscal eletrônica oficial autorizada por prefeitura ou SEFAZ.
- Caso o projeto evolua para emissão fiscal oficial, será necessária integração específica com sistema fiscal autorizado.
