# Etapa 41 — Numeração automática da OS e simplificação da tela de Pagamentos

## Objetivo

Esta etapa ajusta dois pontos operacionais identificados durante a validação visual e funcional do sistema AV CAR AUTO CENTER:

1. Remover os botões manuais de atualização da tela de Pagamentos, pois a própria tela já carrega e atualiza automaticamente pagamentos, resumo financeiro e status da Ordem de Serviço.
2. Impedir que o usuário digite manualmente o número da Ordem de Serviço, transferindo essa responsabilidade integralmente para o backend.

## Alterações realizadas

### Tela de Pagamentos

Foram removidos os botões visíveis:

- Atualizar pagamentos;
- Atualizar ordens.

A atualização passa a ocorrer automaticamente nos seguintes momentos:

- ao abrir a tela;
- ao selecionar uma Ordem de Serviço;
- após salvar pagamento;
- após editar pagamento;
- após alterar status do pagamento;
- após inativar pagamento.

Com isso, a interface fica mais limpa e evita que o usuário precise acionar manualmente uma atualização que já faz parte do comportamento esperado do sistema.

### Tela de Ordens de Serviço

O campo “Número da OS” deixou de ser editável.

Na abertura de uma nova OS, a tela passa a informar que o número será gerado automaticamente ao salvar.

Na edição de uma OS existente, o número é exibido somente para consulta, sem permitir alteração manual.

### Backend

A geração do número da OS passou a ser feita exclusivamente no service de Ordem de Serviço.

A numeração considera todas as ordens já cadastradas no banco, inclusive as inativas. Dessa forma, se a OS número 2 for inativada, nenhuma nova ordem assumirá o número 2 novamente.

Essa decisão preserva:

- histórico;
- rastreabilidade;
- integridade documental;
- consistência das consultas;
- segurança na emissão de documentos PDF.

## Regra de negócio implementada

O número da Ordem de Serviço é sequencial, automático e não reutilizável.

Exemplo:

```text
OS 1 criada
OS 2 criada
OS 2 inativada
Próxima OS criada = OS 3
```

A OS 2 permanece existindo no histórico como registro inativo, por isso seu número não pode ser reaproveitado.

## Arquivos alterados

```text
frontend/oficina-web/src/app/pages/pagamentos/pagamentos.component.html
frontend/oficina-web/src/app/pages/ordens-servico/ordens-servico.component.html
frontend/oficina-web/src/styles.css
src/main/java/br/com/avcar/oficina/business/ordemservico/mapper/OrdemServicoMapper.java
src/main/java/br/com/avcar/oficina/business/ordemservico/repository/IOrdemServicoRepository.java
src/main/java/br/com/avcar/oficina/business/ordemservico/service/OrdemServicoService.java
src/main/java/br/com/avcar/oficina/business/ordemservico/validation/OrdemServicoValidation.java
```

## Testes recomendados

1. Acessar Pagamentos e verificar que os botões manuais de atualização não aparecem mais.
2. Selecionar uma OS em Pagamentos e conferir se o resumo e a lista carregam automaticamente.
3. Cadastrar uma nova OS e verificar que o número não é digitado pelo usuário.
4. Inativar uma OS e cadastrar outra, conferindo que o número anterior não é reutilizado.
5. Testar via Swagger/Postman enviando `numeroOs` no corpo da requisição e verificar que o backend ignora o valor informado e gera a numeração correta.
