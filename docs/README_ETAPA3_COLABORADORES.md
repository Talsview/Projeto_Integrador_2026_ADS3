# Etapa 3 — Módulo Colaborador, Função e ColaboradorFunção

## Objetivo

Implementar o módulo responsável pelo cadastro de colaboradores da oficina mecânica AV CAR AUTO CENTER, mantendo fidelidade ao MER e ao modelo lógico validados.

Esta etapa complementa a generalização/especialização de `Pessoa`, pois uma mesma pessoa pode ser cliente, colaborador ou ambos.

## Entidades implementadas

```text
Colaborador
Funcao
ColaboradorFuncao
```

## Regra de modelagem respeitada

```text
Pessoa especializa em Cliente e Colaborador.
Tipo: compartilhada e total (ct).
Leitura: toda Pessoa é Cliente, Colaborador ou ambos.
```

```text
Colaborador (1,1) -------- possui -------- (1,n) ColaboradorFuncao.
ColaboradorFuncao (0,n) -------- é atribuída em -------- (1,1) Funcao.
```

## Observação importante sobre Mecânico

Não foi criada entidade `Mecanico`.

Conforme a regra de negócio consolidada, mecânico, atendente, secretária, faxineiro, estoquista e gerente devem ser registros da tabela `funcao`. O vínculo entre colaborador e função ocorre por meio de `ColaboradorFuncao`, permitindo que um colaborador possua uma ou mais funções.

## Camadas criadas

```text
business/pessoa/controller/ColaboradorController.java
business/pessoa/controller/FuncaoController.java
business/pessoa/dto/ColaboradorDTO.java
business/pessoa/dto/ColaboradorResumoDTO.java
business/pessoa/dto/ColaboradorFuncaoDTO.java
business/pessoa/dto/FuncaoDTO.java
business/pessoa/mapper/ColaboradorMapper.java
business/pessoa/mapper/FuncaoMapper.java
business/pessoa/model/ColaboradorModel.java
business/pessoa/model/ColaboradorFuncaoModel.java
business/pessoa/model/FuncaoModel.java
business/pessoa/repository/IColaboradorRepository.java
business/pessoa/repository/IColaboradorFuncaoRepository.java
business/pessoa/repository/IFuncaoRepository.java
business/pessoa/service/ColaboradorService.java
business/pessoa/service/FuncaoService.java
business/pessoa/validation/ColaboradorValidation.java
business/pessoa/validation/FuncaoValidation.java
```

## Endpoints de Função

```text
POST   /api/funcoes
PUT    /api/funcoes/{id}
GET    /api/funcoes/{id}
GET    /api/funcoes
GET    /api/funcoes/pesquisar?termo=valor
DELETE /api/funcoes/{id}
```

## Endpoints de Colaborador

```text
POST   /api/colaboradores
PUT    /api/colaboradores/{id}
GET    /api/colaboradores/{id}
GET    /api/colaboradores
GET    /api/colaboradores/pesquisar?termo=valor
DELETE /api/colaboradores/{id}
```

## Exemplo de JSON para cadastro de função

```json
{
  "nomeFuncao": "Mecânico",
  "descricao": "Colaborador responsável por executar serviços técnicos nos veículos."
}
```

## Exemplo de JSON para cadastro de colaborador

```json
{
  "nome": "Almir Pinto da Silva",
  "telefone": "(62) 99999-0000",
  "email": "almir@avcar.com.br",
  "endereco": "Goiânia-GO",
  "dataAdmissao": "2026-06-01",
  "statusColaborador": "ATIVO",
  "funcoesIds": [1]
}
```

Também é possível reaproveitar uma Pessoa já cadastrada informando `pessoaId`:

```json
{
  "pessoaId": 10,
  "nome": "Pessoa já cadastrada",
  "telefone": "(62) 98888-0000",
  "email": "pessoa@exemplo.com",
  "endereco": "Goiânia-GO",
  "dataAdmissao": "2026-06-01",
  "statusColaborador": "ATIVO",
  "funcoesIds": [1, 2]
}
```

## Regras implementadas

```text
RN01 — Toda pessoa cadastrada deve ser Cliente, Colaborador ou ambos.
RN11 — Colaborador pode ter múltiplas funções.
RN14 — Colaborador pode possuir uma ou mais funções.
RN15 — Funções como mecânico e atendente ficam em Funcao.
```

## Observações de implementação

- O identificador padrão permanece `Long`.
- A exclusão de colaborador é lógica.
- Ao inativar um colaborador, suas funções ativas também são encerradas com `dataFim`.
- A entidade `Pessoa` não é inativada automaticamente ao inativar o colaborador, pois a mesma pessoa pode continuar sendo cliente.
- `ColaboradorFuncao` preserva histórico das funções exercidas pelo colaborador.
- A API está preparada para consumo pelo Angular.
