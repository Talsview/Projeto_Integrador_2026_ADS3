# Etapa 47 — Combobox pesquisável em Ordens de Serviço

## Objetivo

Melhorar a usabilidade da tela de Ordens de Serviço para cenários em que a oficina possua muitos clientes e veículos cadastrados.

Antes desta etapa, o usuário precisava abrir uma lista simples de clientes. Em um banco com centenas ou milhares de registros, esse comportamento dificultaria a abertura de uma OS.

## Alterações realizadas

### Tela Ordens de Serviço

Arquivo alterado:

```text
frontend/oficina-web/src/app/pages/ordens-servico/ordens-servico.component.ts
frontend/oficina-web/src/app/pages/ordens-servico/ordens-servico.component.html
```

Foram adicionados campos de pesquisa antes dos selects de Cliente e Veículo:

- pesquisar cliente por nome, CPF/CNPJ, telefone ou e-mail;
- pesquisar veículo por placa, modelo ou proprietário;
- botão Buscar em cada campo;
- pesquisa por Enter no campo de texto;
- manutenção do select para preservar o comportamento simples do cadastro;
- filtro de veículos por proprietário atual após selecionar o cliente.

## Resultado esperado

Na abertura de uma nova OS, o usuário agora consegue localizar o cliente sem navegar manualmente por uma lista grande. Depois de escolher o cliente, o sistema continua permitindo apenas veículos vinculados a esse cliente como proprietário atual.

## Regra de negócio preservada

A OS continua exigindo:

- cliente obrigatório;
- veículo obrigatório;
- veículo pertencente ao cliente selecionado;
- número da OS gerado automaticamente pelo backend;
- numeração de OS inativada não reutilizada.
