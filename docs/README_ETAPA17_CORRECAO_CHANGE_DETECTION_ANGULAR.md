# Etapa 17 — Correção definitiva da atualização visual do Angular após retorno da API

## 1. Objetivo

Esta etapa corrige o comportamento identificado nas telas do frontend Angular em que a tabela e os botões não atualizavam imediatamente após uma operação com o backend.

O problema era percebido principalmente ao cadastrar um cliente: o backend salvava corretamente o registro, porém a tela só atualizava visualmente depois de outro clique, como pressionar o botão de pesquisa ou listagem.

## 2. Sintoma identificado

O comportamento observado foi:

```text
1. O usuário preenchia o formulário.
2. O usuário clicava em Salvar ou Processar.
3. O backend retornava a resposta.
4. O objeto era incluído ou atualizado na lista do componente.
5. A visualização do Angular permanecia antiga.
6. A tela só era redesenhada após outro clique.
```

Esse sintoma indica falha de atualização visual, não necessariamente erro de persistência no backend.

## 3. Causa técnica provável

As respostas HTTP estavam sendo processadas corretamente, porém algumas atualizações de estado podiam ocorrer fora do ciclo de detecção de mudanças do Angular.

Quando isso acontece, variáveis como:

```text
processando
salvando
carregando
consultando
clientes
veiculos
ordensServico
```

são alteradas no TypeScript, mas o HTML não é redesenhado imediatamente.

## 4. Solução aplicada

Foi aplicado um ajuste global no interceptor da API:

```text
api-error.interceptor.ts
```

Agora, todas as respostas HTTP, erros e eventos de conclusão são reenviados para dentro do `NgZone` do Angular.

Com isso, o Angular passa a executar automaticamente a detecção de mudanças após o retorno da API.

## 5. Arquivos alterados

```text
frontend/oficina-web/src/app/core/interceptors/api-error.interceptor.ts
frontend/oficina-web/src/app/app.config.ts
README.md
docs/README_ETAPA17_CORRECAO_CHANGE_DETECTION_ANGULAR.md
docs/adr/ADR-020-correcao-atualizacao-visual-angular.md
database/07_verificacao_correcao_atualizacao_visual.sql
```

## 6. Resultado esperado

Após a correção:

```text
1. O botão de ação não fica mais preso em Processando.
2. A tabela atualiza sem precisar clicar em outro botão.
3. As mensagens de sucesso e erro aparecem imediatamente.
4. A comunicação Angular/Backend fica mais estável visualmente.
5. O frontend passa a refletir o retorno real da API no momento correto.
```

## 7. Teste recomendado

1. Rodar o backend:

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair
mvn.cmd spring-boot:run
```

2. Rodar o frontend:

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair\frontend\oficina-web
npm.cmd install
npm.cmd run start:proxy
```

3. Acessar:

```text
http://localhost:4200/clientes
```

4. Cadastrar um cliente.

5. Verificar se a tabela é atualizada automaticamente.

## 8. Observação prática

Se o navegador ainda mostrar o comportamento antigo, é provável que o Angular esteja rodando uma versão anterior do bundle. Nesse caso, encerrar o processo com `CTRL + C`, iniciar novamente com `npm.cmd run start:proxy` e atualizar o navegador com `CTRL + F5`.
