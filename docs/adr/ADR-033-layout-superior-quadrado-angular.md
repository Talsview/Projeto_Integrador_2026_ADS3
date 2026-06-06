# ADR-033 — Layout superior quadrado para o frontend Angular

## Status

Aprovada.

## Contexto

A versão anterior do frontend utilizava menu lateral escuro, estilo semelhante a dashboard administrativo moderno. Após avaliação visual, foi solicitado que o sistema tivesse aparência mais quadrada, objetiva e com abas na parte superior, aproximando-se de um sistema de cadastro operacional utilizado em oficina mecânica.

## Decisão

Foi decidido substituir o menu lateral por uma navegação superior em abas, agrupando as funcionalidades por área operacional.

Os grupos definidos foram:

```text
Operação
Cadastros
Gestão
```

A interface também passou a utilizar:

```text
bordas menores;
sombras discretas;
botões mais retos;
cards menos decorativos;
navegação horizontal com rolagem quando necessário.
```

## Consequências

```text
1. O sistema fica mais simples e direto para uso em cadastros.
2. As telas ocupam melhor a largura disponível.
3. O menu superior facilita a localização dos módulos principais.
4. A comunicação com o backend não foi alterada.
5. As rotas e services Angular foram preservados.
```
