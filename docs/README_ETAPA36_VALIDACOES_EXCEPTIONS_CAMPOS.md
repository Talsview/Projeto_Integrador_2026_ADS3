# Etapa 36 — Validações de campos, exceções e regras de negócio

Esta etapa reforça as validações do sistema AV CAR AUTO CENTER em duas camadas: frontend Angular e backend Spring Boot. O objetivo é impedir que o usuário cadastre informações incompatíveis com o domínio da oficina mecânica e garantir que a API também bloqueie dados inválidos quando acessada via Swagger, Postman ou outro cliente externo.

## Estratégia aplicada

- Angular valida antes de enviar, melhorando a experiência do usuário.
- Backend valida novamente, garantindo integridade real das regras.
- Banco de dados continua protegido por chaves, constraints e unicidade.

## Validações implementadas

### Clientes
- Nome de pessoa física sem números ou caracteres especiais indevidos.
- Nome empresarial com caracteres controlados.
- CPF válido por cálculo dos dígitos verificadores.
- CNPJ válido por cálculo dos dígitos verificadores.
- E-mail em formato válido.
- Telefone com DDD.
- Data de nascimento não futura.
- Razão social obrigatória para pessoa jurídica.

### Veículos
- Placa nos formatos ABC1234 ou ABC1D23.
- Chassi com 17 caracteres válidos quando informado.
- Ano de fabricação entre 1900 e ano atual + 1.
- Ano modelo coerente com ano de fabricação.
- Quilometragem não negativa.
- Proprietário atual obrigatório.
- Data de início de posse não futura.

### Colaboradores e funções
- Nome de colaborador sem números indevidos.
- E-mail e telefone válidos.
- Data de admissão não futura.
- Pelo menos uma função por colaborador.
- Nome de função obrigatório, não numérico e sem caracteres inválidos.

### Marcas, modelos e serviços
- Marcas e modelos com nomes válidos.
- Modelo vinculado a marca.
- Serviço com tipo obrigatório: INTERNO ou TERCEIRIZADO.
- Valor base e prazo de garantia não negativos.

### Empresas terceirizadas, fornecedores e peças
- CNPJ válido quando informado.
- E-mail e telefone válidos.
- Peça com nome válido.
- Código nacional com limite de tamanho.
- Anos de aplicação coerentes.
- Prazo de garantia não negativo.

### Ordens de serviço
- Cliente e veículo obrigatórios.
- Data de abertura não futura.
- Datas de aprovação e finalização coerentes.
- Número de OS com caracteres controlados.
- Observação com limite de tamanho.
- Fluxo de status preservado: ORCAMENTO, EXECUCAO, PAGAMENTO e FINALIZADO.

### Serviços e peças da OS
- Serviço obrigatório.
- Colaborador responsável obrigatório.
- Peça e fornecedor obrigatórios.
- Quantidade maior que zero.
- Valores não negativos.
- Datas de execução e terceirização coerentes.

### Pagamentos
- Ordem de serviço obrigatória.
- Forma e status obrigatórios.
- Valor maior que zero.
- Valor pago não pode ultrapassar saldo pendente.
- Data de pagamento não futura.
- Data de pagamento não anterior à abertura da OS.
- Pagamento cancelado ou estornado não deve possuir data efetiva.

### Garantias
- Garantia aguardando finalização da OS não pode ser acionada.
- Garantia encerrada não pode ser acionada novamente.
- Garantia expirada não pode ser acionada.
- Garantia já acionada não pode ser acionada novamente.
- Garantia ainda não iniciada não pode ser encerrada manualmente.

### Nota fiscal / recibo interno em PDF
- Não gera PDF para OS inexistente.
- Não gera documento para OS sem cliente válido.
- Não gera documento para OS sem veículo válido.
- Não gera documento para OS sem serviços ou peças.
- Não gera documento para OS com data de abertura futura.

## Arquivos principais alterados

### Backend
- `core/validation/ValidationUtils.java`
- `core/validation/DocumentoValidationUtils.java`
- `business/pessoa/validation/*`
- `business/veiculo/validation/*`
- `business/servico/validation/*`
- `business/peca/validation/*`
- `business/ordemservico/validation/*`
- `business/pagamento/validation/PagamentoValidation.java`
- `business/pagamento/service/PagamentoService.java`
- `business/garantia/service/GarantiaService.java`
- `business/notafiscal/service/NotaFiscalPdfService.java`

### Frontend Angular
- `src/app/core/validation/documento-validation.ts`
- `src/app/core/validation/field-validation.ts`
- telas de clientes, veículos, colaboradores, funções, marcas/modelos, serviços, empresas terceirizadas, peças/fornecedores, ordens de serviço, itens da OS e pagamentos.

## Resultado

O sistema passa a se comportar de forma mais próxima de um software real de oficina, impedindo registros incorretos antes da persistência e exibindo mensagens claras ao usuário.
