# ADR-029 — Otimização de código e redução de redundâncias

## Situação

Após a estabilização da comunicação entre Angular, backend e banco de dados, foi identificada a necessidade de preparar o código para a próxima etapa visual, reduzindo redundâncias e pontos de manutenção repetidos.

## Decisão

Foi autorizada a modificação do código para otimização técnica, com foco em:

- centralizar operações comuns de API no `BaseApiService`;
- remover telas técnicas desnecessárias do menu operacional;
- transformar o menu principal do Angular em uma estrutura orientada por dados;
- criar script de auditoria de redundâncias;
- manter a organização dos scripts SQL por finalidade.

## Consequências

A manutenção do frontend fica mais simples, pois novas abas podem ser adicionadas alterando a estrutura de menu em TypeScript, sem repetir blocos HTML. O script de auditoria também permite identificar padrões de duplicação antes de novas entregas.

## Relação com o projeto

A decisão preserva a arquitetura monolítica em camadas exigida no Projeto Integrador e prepara o sistema para receber a reformulação visual profissional da oficina.
