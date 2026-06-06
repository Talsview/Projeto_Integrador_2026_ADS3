# Etapa 5 — Módulo Serviço, Serviço Interno, Serviço Terceirizado e Empresa Terceirizada

## 1. Objetivo da etapa

Esta etapa implementa a base de serviços da oficina mecânica AV CAR AUTO CENTER, respeitando o MER e o modelo lógico do projeto. O módulo foi criado para permitir o cadastro de serviços executados pela própria oficina e serviços terceirizados por empresas externas.

A implementação prepara o sistema para a próxima etapa, na qual a Ordem de Serviço utilizará os serviços cadastrados por meio da entidade associativa `ItemServico`.

---

## 2. Entidades implementadas

```text
Servico
ServicoInterno
ServicoTerceirizado
EmpresaTerceirizada
```

### 2.1 Servico

Representa o cadastro geral de serviços oferecidos pela oficina. Possui dados comuns aos dois tipos de serviço, como nome, descrição, prazo de garantia e valor base.

### 2.2 ServicoInterno

Representa a especialização de `Servico` para trabalhos executados diretamente por colaboradores da oficina.

### 2.3 ServicoTerceirizado

Representa a especialização de `Servico` para trabalhos encaminhados a uma empresa externa. Mesmo nesse caso, a oficina permanece responsável perante o cliente.

### 2.4 EmpresaTerceirizada

Representa a empresa externa que poderá executar serviços terceirizados vinculados futuramente ao `ItemServico` pela entidade `ExecucaoServicoTerceirizado`.

---

## 3. Regra de generalização/especialização aplicada

```text
Servico especializa em ServicoInterno e ServicoTerceirizado.
Tipo: exclusiva e total (xt).
Leitura: todo Serviço é Interno ou Terceirizado, nunca ambos.
```

Essa regra foi implementada fisicamente com uma tabela base (`servico`) e uma tabela para cada especialização (`servico_interno` e `servico_terceirizado`).

---

## 4. Regras de negócio atendidas

```text
RN16 — Todo Servico é Interno ou Terceirizado.
RN17 — Serviço terceirizado gera ExecucaoServicoTerceirizado em etapa posterior da OS.
RN18 — EmpresaTerceirizada pode executar várias terceirizações.
RN19 — A oficina permanece responsável pelo serviço terceirizado.
RN25 — ItemServico gera GarantiaServico em etapa posterior da OS.
RN27 — Garantia do serviço varia conforme o tipo de serviço.
```

---

## 5. Arquivos criados

### DTOs

```text
src/main/java/br/com/avcar/oficina/business/servico/dto/ServicoDTO.java
src/main/java/br/com/avcar/oficina/business/servico/dto/EmpresaTerceirizadaDTO.java
```

### Enum

```text
src/main/java/br/com/avcar/oficina/business/servico/enums/TipoServico.java
```

### Models

```text
src/main/java/br/com/avcar/oficina/business/servico/model/ServicoModel.java
src/main/java/br/com/avcar/oficina/business/servico/model/ServicoInternoModel.java
src/main/java/br/com/avcar/oficina/business/servico/model/ServicoTerceirizadoModel.java
src/main/java/br/com/avcar/oficina/business/servico/model/EmpresaTerceirizadaModel.java
```

### Repositories

```text
src/main/java/br/com/avcar/oficina/business/servico/repository/IServicoRepository.java
src/main/java/br/com/avcar/oficina/business/servico/repository/IServicoInternoRepository.java
src/main/java/br/com/avcar/oficina/business/servico/repository/IServicoTerceirizadoRepository.java
src/main/java/br/com/avcar/oficina/business/servico/repository/IEmpresaTerceirizadaRepository.java
```

### Mappers

```text
src/main/java/br/com/avcar/oficina/business/servico/mapper/ServicoMapper.java
src/main/java/br/com/avcar/oficina/business/servico/mapper/EmpresaTerceirizadaMapper.java
```

### Validations

```text
src/main/java/br/com/avcar/oficina/business/servico/validation/ServicoValidation.java
src/main/java/br/com/avcar/oficina/business/servico/validation/EmpresaTerceirizadaValidation.java
```

### Services

```text
src/main/java/br/com/avcar/oficina/business/servico/service/ServicoService.java
src/main/java/br/com/avcar/oficina/business/servico/service/EmpresaTerceirizadaService.java
```

### Controllers

```text
src/main/java/br/com/avcar/oficina/business/servico/controller/ServicoController.java
src/main/java/br/com/avcar/oficina/business/servico/controller/EmpresaTerceirizadaController.java
```

---

## 6. Endpoints implementados

### Serviço

```text
POST   /api/servicos
PUT    /api/servicos/{id}
GET    /api/servicos/{id}
GET    /api/servicos
GET    /api/servicos/tipo/{tipoServico}
GET    /api/servicos/pesquisar?termo=valor
DELETE /api/servicos/{id}
```

Valores aceitos para `{tipoServico}`:

```text
INTERNO
TERCEIRIZADO
```

### Empresa Terceirizada

```text
POST   /api/empresas-terceirizadas
PUT    /api/empresas-terceirizadas/{id}
GET    /api/empresas-terceirizadas/{id}
GET    /api/empresas-terceirizadas
GET    /api/empresas-terceirizadas/pesquisar?termo=valor
DELETE /api/empresas-terceirizadas/{id}
```

---

## 7. Ajustes no banco de dados

Foram atualizados os scripts:

```text
database/01_create_schema.sql
database/02_seed_inicial.sql
```

No script físico foi corrigida a duplicidade da coluna `id_empresa_terceirizada` na tabela `execucao_servico_terceirizado`.

Também foram incluídos dados iniciais de serviços internos, serviços terceirizados e empresas terceirizadas para facilitar testes via Swagger e Angular.

---

## 8. Observação acadêmica

Esta etapa preserva a rastreabilidade necessária para a Ordem de Serviço. O serviço cadastrado ainda não representa execução em uma OS; ele é apenas o catálogo. A execução real ocorrerá posteriormente por meio de `ItemServico`, entidade associativa responsável por vincular `OrdemServico`, `Servico` e `Colaborador`.

Essa separação evita duplicidade de serviços e permite que a oficina saiba:

```text
1. Qual serviço foi cadastrado.
2. Se o serviço é interno ou terceirizado.
3. Qual garantia padrão o serviço possui.
4. Qual empresa externa poderá executar serviços terceirizados futuramente.
5. Qual colaborador será responsável pelo serviço quando a OS for implementada.
```
