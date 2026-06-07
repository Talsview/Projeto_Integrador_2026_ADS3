# ADR-047 — Seeds completas e verificação SQL unificada

## Status

Aceita.

## Contexto

O projeto possuía dados iniciais suficientes para iniciar algumas telas, porém a base ainda era limitada para demonstrar o sistema completo da oficina mecânica. Também existiam vários scripts pequenos de verificação SQL, criados ao longo das etapas do desenvolvimento.

Essa fragmentação dificultava a execução no pgAdmin, pois era necessário abrir vários arquivos para conferir clientes, veículos, colaboradores, ordens de serviço, pagamentos, garantias e dados usados no PDF.

## Decisão

Foi decidido ampliar a seed principal e unificar os scripts de verificação em um único arquivo.

A seed passou a incluir dados completos de demonstração para:

- clientes pessoa física e jurídica;
- colaboradores com funções;
- marcas, modelos e veículos;
- histórico de proprietários;
- serviços internos e terceirizados;
- fornecedores e peças;
- ordens de serviço em diferentes status;
- itens de serviço;
- itens de peça;
- garantias;
- pagamentos;
- cenários aptos para geração de PDF/recibo interno.

As verificações foram consolidadas em:

```text
database/03_verificacoes/03_verificacao_geral_sistema.sql
```

## Consequências

### Positivas

- O banco fica mais completo para apresentação.
- As telas do Angular carregam dados reais logo após a seed.
- Os testes de pagamento, garantia, OS e PDF ficam mais simples.
- A verificação no pgAdmin passa a ser feita com um único arquivo.
- O script completo de banco fica mais útil para replicação em outra máquina.

### Negativas

- A seed ficou mais extensa.
- Alterações futuras no modelo físico exigirão atenção para manter a seed e a verificação sincronizadas.

## Justificativa acadêmica

A decisão melhora a rastreabilidade e facilita a demonstração do sistema funcionando, especialmente porque a atividade exige apresentação do software, validações, banco de dados e documentação técnica coerente com o funcionamento da oficina.
