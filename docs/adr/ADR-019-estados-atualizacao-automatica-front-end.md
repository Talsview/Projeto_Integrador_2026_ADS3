# ADR-019 — Separação de estados de processamento e atualização automática das tabelas no frontend

## Status

Aceita.

## Contexto

Durante os testes da camada View em Angular, foi identificado que alguns botões permaneciam com a indicação visual de processamento mesmo após a resposta do backend. Também foi observado que, após operações de cadastro, o usuário esperava que a tabela fosse atualizada automaticamente.

## Decisão

Foi decidido separar os estados visuais dos componentes Angular em estados específicos:

```text
consultando / carregando → consultas e atualização de tabelas.
salvando / processando   → ações de gravação, alteração, exclusão e acionamentos.
```

Também foi decidido aplicar o operador `finalize` do RxJS nas chamadas HTTP, garantindo encerramento do estado de processamento tanto no sucesso quanto no erro.

Além disso, as tabelas passaram a ser atualizadas imediatamente em memória quando possível e, em seguida, sincronizadas novamente com o backend.

## Consequências positivas

```text
1. Botões não ficam presos em estado de processamento.
2. Tabelas são atualizadas automaticamente após salvar, alterar ou excluir.
3. O usuário recebe resposta visual mais rápida.
4. O frontend fica mais organizado para manutenção.
5. A integração com o backend torna-se mais previsível.
```

## Consequências negativas

```text
1. Os componentes passam a ter mais variáveis de estado.
2. Algumas telas executam reconsulta ao backend após atualizar a lista localmente.
```

## Justificativa

A decisão é adequada porque melhora a usabilidade da aplicação e reduz a chance de o usuário interpretar que o sistema está travado. Para uma oficina mecânica local, a interface precisa ser objetiva e confiável, especialmente em módulos de cadastro, ordem de serviço, peças, pagamentos e garantias.
