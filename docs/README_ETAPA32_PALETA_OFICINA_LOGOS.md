# Etapa 32 — Paleta de oficina e implementação das logos no Angular

## Objetivo

Aplicar uma identidade visual mais próxima de um sistema real de oficina mecânica, utilizando a marca AV CAR AUTO CENTER no topo do sistema e a versão em ícone como favicon da aba do navegador.

## Decisões aplicadas

- A logo completa foi aplicada no cabeçalho superior do sistema.
- A logo em formato de ícone foi aplicada como favicon.
- A paleta foi ajustada para azul automotivo escuro, grafite, cinza metálico, branco e laranja.
- O menu superior por categorias foi mantido para facilitar a navegação.
- O visual foi mantido mais quadrado, com menos arredondamento e menos aparência de dashboard genérico.

## Arquivos alterados

- `frontend/oficina-web/src/index.html`
- `frontend/oficina-web/src/app/app.component.html`
- `frontend/oficina-web/src/app/app.component.css`
- `frontend/oficina-web/src/styles.css`
- `frontend/oficina-web/src/app/pages/dashboard/dashboard.component.css`
- `frontend/oficina-web/public/assets/branding/av-car-favicon.png`
- `frontend/oficina-web/public/assets/branding/av-car-logo-horizontal.png`

## Observação técnica

A configuração de assets do Angular usa a pasta `public`. Por isso, os arquivos foram colocados em `frontend/oficina-web/public/assets/branding` e são referenciados no código como `assets/branding/nome-do-arquivo.png`.

## Validação recomendada

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair\frontend\oficina-web
npm.cmd install
npm.cmd start
```

Após abrir o navegador, utilizar `CTRL + F5` para evitar cache do favicon e dos estilos antigos.
