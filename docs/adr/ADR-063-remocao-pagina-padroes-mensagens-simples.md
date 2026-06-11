# ADR-063 — Remoção da tela de Padrões de Projeto e padronização de mensagens simples

## Status

Aceita.

## Contexto

A tela de Padrões de Projeto havia sido criada para evidenciar academicamente os padrões aplicados no sistema. Entretanto, para a operação real da oficina, essa tela não agrega funcionalidade direta ao fluxo de atendimento, cadastro, orçamento, execução, pagamento ou garantia.

Além disso, algumas mensagens de conclusão estavam longas e artificiais, utilizando textos como “salvo com sucesso” e “tabela atualizada automaticamente”. Esse tipo de mensagem deixava a interface mais carregada e menos natural.

## Decisão

Remover a tela de Padrões de Projeto da interface Angular e simplificar as mensagens de conclusão do sistema.

As mensagens passam a seguir o padrão objetivo:

- `Salvo.`
- `Atualizado.`
- `Inativado.`
- `Enviado para execução.`
- `Enviado para pagamento.`
- `PDF gerado.`

## Consequências positivas

- Interface mais limpa.
- Menos poluição visual.
- Mensagens mais próximas de um sistema real.
- Navegação de Gestão focada em Relatórios e Configurações.
- Os padrões continuam documentados no material acadêmico, sem ocupar uma tela operacional.

## Consequências negativas

- A evidência visual dos padrões de projeto deixa de existir no menu do sistema.
- A defesa dos padrões passa a depender da documentação técnica e do código-fonte.

## Justificativa

A decisão melhora a experiência do usuário final sem remover a implementação técnica dos padrões de projeto. A documentação acadêmica continua registrando os padrões existentes, enquanto a interface permanece focada no uso operacional da oficina.
