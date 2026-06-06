# Etapa 34 - Nota Fiscal / Recibo em PDF por Ordem de Serviço

## Objetivo

Esta etapa adiciona ao sistema AV CAR AUTO CENTER a geração de documento em PDF baseado nas Ordens de Serviço da oficina. O documento segue a estrutura observada nos arquivos reais de OS fornecidos como referência, com cabeçalho da oficina, dados do cliente, dados do veículo, peças/produtos, serviços executados, totais, pagamentos e assinaturas.

## Observação importante

O documento gerado pelo sistema é uma **Nota Fiscal / Comprovante interno simplificado**. Ele não substitui emissão fiscal eletrônica autorizada por prefeitura, SEFAZ ou outro órgão fiscal. Para fins acadêmicos e operacionais, ele atende à necessidade de geração local de PDF a partir dos dados já cadastrados no sistema.

## Backend

Novo pacote criado:

```text
src/main/java/br/com/avcar/oficina/business/notafiscal
```

Arquivos principais:

```text
controller/NotaFiscalController.java
service/NotaFiscalPdfService.java
```

Novo endpoint REST:

```http
GET /api/notas-fiscais/ordens-servico/{id}/pdf
```

Retorno:

```text
application/pdf
```

Nome do arquivo retornado:

```text
nota-fiscal-os-<numero-da-os>.pdf
```

## Frontend Angular

Arquivos atualizados:

```text
frontend/oficina-web/src/app/core/services/ordem-servico-api.service.ts
frontend/oficina-web/src/app/pages/ordens-servico/ordens-servico.component.ts
frontend/oficina-web/src/app/pages/ordens-servico/ordens-servico.component.html
```

Na tela **Ordens de Serviço**, foi adicionado o botão:

```text
Nota PDF
```

Esse botão baixa automaticamente o PDF da OS selecionada.

## Dependência adicionada ao Maven

```xml
<dependency>
    <groupId>com.github.librepdf</groupId>
    <artifactId>openpdf</artifactId>
    <version>1.3.43</version>
</dependency>
```

A dependência é utilizada apenas para geração do PDF localmente no backend Spring Boot.

## Dados exibidos no PDF

O documento contém:

- dados da oficina;
- número da OS;
- data e hora de emissão;
- dados do cliente;
- documento CPF/CNPJ quando disponível;
- telefone e e-mail;
- endereço;
- veículo, marca, modelo, placa, cor, ano, entrada e quilometragem;
- peças/produtos aplicados na OS;
- fornecedor da peça, quando disponível;
- serviços executados;
- colaborador responsável pelo serviço;
- totais de serviços, peças, valor pago e saldo pendente;
- pagamentos registrados;
- observações e assinatura.

## Como testar

1. Subir o backend:

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair
mvn spring-boot:run
```

2. Subir o frontend:

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair\frontend\oficina-web
npm.cmd install
npm.cmd start
```

3. Acessar a tela:

```text
Ordens de Serviço
```

4. Clicar em:

```text
Nota PDF
```

## Critério de validação

A funcionalidade estará correta quando o sistema baixar um arquivo PDF contendo os dados reais da OS selecionada, sem depender de clique adicional ou geração manual externa.
