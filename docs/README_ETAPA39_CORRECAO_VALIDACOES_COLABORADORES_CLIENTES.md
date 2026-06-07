# Etapa 39 — Correção das validações de colaboradores e ações de clientes

Esta etapa corrige dois pontos identificados durante a validação funcional do sistema:

1. A tela de Colaboradores não apresentava validação visual adequada por campo e permitia tentativa de entrada inválida em campos como telefone.
2. A tela de Clientes não possuía ações visíveis para editar e inativar registros já cadastrados.

## Correções aplicadas

### Colaboradores

- Inclusão de validação por campo no Angular.
- Inclusão de mensagens próximas aos campos inválidos.
- Validação de nome, telefone, e-mail, data de admissão e funções.
- Telefone passa a remover letras automaticamente e rejeitar conteúdo inválido.
- Botão Salvar muda para Atualizar quando um colaborador está em edição.
- Edição passa a carregar o detalhe do colaborador pela API.
- Inativação solicita confirmação antes da operação.

### Clientes

- Inclusão da coluna Ações na listagem.
- Inclusão dos botões Editar e Inativar.
- Edição carrega o cliente detalhado pela API.
- Atualização usa os endpoints corretos para pessoa física e pessoa jurídica.
- Inativação atualiza automaticamente a tabela após sucesso.
- O tipo de cliente fica bloqueado durante edição para preservar a especialização exclusiva do modelo.

### Backend

- Ajuste da validação de telefone no `ValidationUtils` para rejeitar letras mesmo quando o campo não é obrigatório.

## Arquivos principais alterados

- `frontend/oficina-web/src/app/pages/colaboradores/colaboradores.component.ts`
- `frontend/oficina-web/src/app/pages/colaboradores/colaboradores.component.html`
- `frontend/oficina-web/src/app/pages/clientes/clientes.component.ts`
- `frontend/oficina-web/src/app/pages/clientes/clientes.component.html`
- `frontend/oficina-web/src/app/core/services/cliente-api.service.ts`
- `frontend/oficina-web/src/app/models/cliente.model.ts`
- `frontend/oficina-web/src/app/core/validation/field-validation.ts`
- `src/main/java/br/com/avcar/oficina/core/validation/ValidationUtils.java`

## Resultado esperado

- Colaboradores não devem ser salvos com telefone inválido, nome inválido, e-mail inválido, data futura ou sem função.
- Clientes podem ser editados e inativados diretamente pela tabela.
- As tabelas são atualizadas automaticamente após salvar, atualizar ou inativar.
