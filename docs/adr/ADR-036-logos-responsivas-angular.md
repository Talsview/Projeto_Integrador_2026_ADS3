# ADR-036 — Implementação de Logos Responsivas no Frontend Angular

## Status

Aceita.

## Contexto

O sistema AV CAR AUTO CENTER precisava utilizar uma identidade visual mais adequada ao contexto de oficina mecânica, com logo principal, favicon e variações para diferentes tamanhos de tela. A interface Angular já possuía comunicação funcional com o backend, porém a marca visual precisava ser incorporada de forma responsiva e organizada.

## Decisão

Foi decidido implementar um conjunto de logos em múltiplos tamanhos dentro de `public/assets/branding`, utilizando o recurso `picture` no cabeçalho do Angular para selecionar automaticamente a imagem mais adequada conforme a largura da tela.

Também foram configurados favicons, ícones para dispositivos móveis e arquivo `site.webmanifest`, garantindo melhor identificação visual na aba do navegador e em possíveis atalhos instalados no sistema operacional.

## Consequências

- A logo horizontal é usada no desktop.
- A logo reduzida é usada em telas médias.
- O ícone é usado em telas pequenas.
- O favicon da aba do navegador passa a usar a marca da AV CAR AUTO CENTER.
- A identidade visual fica mais consistente com a proposta do sistema de gestão de oficina.
- Não houve alteração em rotas, services, endpoints ou comunicação com o backend.

## Arquivos impactados

```text
frontend/oficina-web/src/index.html
frontend/oficina-web/src/app/app.component.html
frontend/oficina-web/src/app/app.component.css
frontend/oficina-web/src/styles.css
frontend/oficina-web/public/assets/branding
```
