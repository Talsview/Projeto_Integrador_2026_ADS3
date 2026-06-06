# Etapa 15 — Ampliação do Frontend Angular com Telas Operacionais

## 1. Objetivo

Esta etapa amplia a camada **View em Angular** para que o sistema possua telas suficientes para apresentação e uso operacional inicial da oficina mecânica AV CAR AUTO CENTER.

O foco desta etapa é disponibilizar telas para os principais módulos já existentes no backend Spring Boot REST, mantendo a comunicação por meio dos endpoints publicados no Swagger.

## 2. Telas implementadas ou ampliadas

Foram criadas ou atualizadas as seguintes telas:

```text
Painel operacional
Clientes
Funções
Colaboradores
Marcas e Modelos
Veículos
Serviços
Empresas Terceirizadas
Peças e Fornecedores
Ordens de Serviço
Itens da OS
Pagamentos
Garantias
Estrutura de Dados I
Padrões de Projeto
```

## 3. Serviços Angular criados

Foram criados serviços HTTP para consumir a API REST:

```text
FuncaoApiService
ColaboradorApiService
MarcaApiService
ModeloApiService
ServicoApiService
EmpresaTerceirizadaApiService
FornecedorApiService
PecaApiService
ItemServicoApiService
ItemPecaApiService
PagamentoApiService
GarantiaApiService
```

Esses serviços usam o `BaseApiService` sempre que possível, preservando uma estrutura uniforme para listagem, pesquisa, cadastro, atualização e exclusão lógica.

## 4. Models TypeScript adicionados

Foram adicionados models para representar os DTOs do backend:

```text
pessoa.model.ts
servico.model.ts
peca.model.ts
pagamento.model.ts
garantia.model.ts
ordem-servico.model.ts atualizado
```

## 5. Regras de negócio refletidas na interface

A interface passa a evidenciar as principais regras de domínio:

```text
Mecânico, atendente, secretária e demais cargos são registros de Funcao.
Colaborador pode possuir uma ou mais funções.
Veículo possui marca, modelo e proprietário atual.
Serviço pode ser interno ou terceirizado.
Peça usada em OS deve estar vinculada a peça cadastrada e fornecedor identificado.
Todo ItemServico exige colaborador responsável.
Pagamentos são vinculados à OrdemServico.
Garantias de peça e serviço são consultadas após a geração pelo backend.
```

## 6. Observação técnica

As telas foram desenvolvidas para rodar no VS Code com Angular em modo de desenvolvimento, usando:

```powershell
npm.cmd run start:proxy
```

O proxy continua redirecionando chamadas `/api` para:

```text
http://localhost:9081
```

## 7. Comandos de execução

Backend:

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair
mvn.cmd spring-boot:run
```

Frontend:

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair\frontend\oficina-web
npm.cmd install
npm.cmd run start:proxy
```

Acessar:

```text
http://localhost:4200
```

## 8. Próxima etapa recomendada

A próxima etapa deve ser uma rodada de testes no VS Code, verificando:

```text
Se todas as rotas abrem corretamente.
Se as listas carregam com backend e banco ativos.
Se os cadastros retornam mensagens de sucesso ou erro de validação.
Se os endpoints do Swagger correspondem às chamadas feitas pelo Angular.
```
