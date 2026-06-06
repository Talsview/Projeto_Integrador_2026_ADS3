# ADR-024 — Consolidação do Script SQL do Banco de Dados

## Status

Aceita.

## Contexto

Durante a evolução do projeto, novos scripts SQL foram criados a cada etapa para registrar ajustes, verificações e validações técnicas. Embora essa abordagem seja útil para rastrear a evolução acadêmica do sistema, ela dificulta a execução prática do banco no pgAdmin, pois o aluno precisa identificar quais arquivos são obrigatórios e em qual ordem devem ser executados.

## Decisão

Foi criado o arquivo único:

```text
database/00_SCRIPT_COMPLETO_BANCO.sql
```

Esse arquivo consolida a criação do modelo físico e os dados iniciais obrigatórios do sistema.

## Consequências positivas

```text
1. Reduz a chance de erro ao executar scripts no pgAdmin.
2. Facilita a instalação local do banco de dados.
3. Mantém o projeto mais claro para apresentação acadêmica.
4. Preserva a compatibilidade com spring.jpa.hibernate.ddl-auto=validate.
5. Permite que os scripts menores continuem existindo apenas como histórico e verificação.
```

## Consequências negativas

```text
1. O script completo fica maior.
2. Alterações futuras no banco precisam atualizar o script consolidado.
```

## Justificativa

Como o sistema deve funcionar localmente e o banco será criado manualmente, a existência de um script único melhora a organização operacional e reduz dependências durante a execução do projeto em ambiente de apresentação.
