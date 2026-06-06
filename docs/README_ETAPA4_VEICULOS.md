# Etapa 4 — Módulo Marca, Modelo, Veículo e Histórico de Proprietário

## 1. Objetivo da etapa

Esta etapa implementa o núcleo de veículos da oficina mecânica AV CAR AUTO CENTER, permitindo cadastrar marcas, modelos, veículos e registrar o histórico de proprietários.

A implementação mantém a regra central do MER: o veículo não possui um cliente fixo como atributo direto. A relação entre Cliente e Veículo é controlada pela entidade associativa `HistoricoProprietario`, pois um veículo pode ter vários proprietários ao longo do tempo.

## 2. Entidades implementadas

```text
Marca
Modelo
Veiculo
HistoricoProprietario
```

## 3. Relacionamentos atendidos

```text
Marca (1,1) -------- possui -------- (0,n) Modelo.
Modelo (1,1) -------- classifica -------- (0,n) Veiculo.
Cliente (1,1) -------- possui -------- (0,n) HistoricoProprietario.
Veiculo (1,1) -------- possui -------- (1,n) HistoricoProprietario.
```

## 4. Regras de negócio atendidas

```text
RV01 — Veículo deve estar vinculado a um Modelo.
RV02 — Modelo deve estar vinculado a uma Marca.
RV03 — Todo Veículo deve possuir ao menos um HistoricoProprietario.
RV04 — Um Cliente pode possuir nenhum ou vários veículos ao longo do tempo.
RV05 — Um Veículo pode ter vários proprietários ao longo do tempo.
RV06 — Apenas um histórico de proprietário deve estar marcado como proprietário atual para o mesmo veículo.
RV07 — Ao transferir veículo para outro cliente, o histórico anterior recebe data_fim_posse e proprietario_atual = false.
RV08 — O novo histórico recebe proprietario_atual = true.
RV09 — A placa deve ser única para veículo ativo.
RV10 — O cadastro preserva marca, modelo, ano de fabricação e ano do modelo.
```

## 5. Endpoints criados

### 5.1 Marca

```text
POST   /api/marcas
PUT    /api/marcas/{id}
GET    /api/marcas/{id}
GET    /api/marcas
GET    /api/marcas/pesquisar?termo=valor
DELETE /api/marcas/{id}
```

### 5.2 Modelo

```text
POST   /api/modelos
PUT    /api/modelos/{id}
GET    /api/modelos/{id}
GET    /api/modelos
GET    /api/modelos/marca/{marcaId}
GET    /api/modelos/pesquisar?termo=valor
DELETE /api/modelos/{id}
```

### 5.3 Veículo

```text
POST   /api/veiculos
PUT    /api/veiculos/{id}
PATCH  /api/veiculos/{id}/transferir-proprietario
GET    /api/veiculos/{id}
GET    /api/veiculos
GET    /api/veiculos/pesquisar?termo=valor
DELETE /api/veiculos/{id}
```

## 6. Exemplo de cadastro de marca

```json
{
  "nomeMarca": "GM"
}
```

## 7. Exemplo de cadastro de modelo

```json
{
  "marcaId": 1,
  "nomeModelo": "Cobalt"
}
```

## 8. Exemplo de cadastro de veículo

```json
{
  "modeloId": 1,
  "placa": "PQX-1354",
  "chassi": "",
  "cor": "Prata",
  "anoVeiculo": 2016,
  "anoModelo": 2016,
  "quilometragemAtual": 100525,
  "observacao": "Veículo cadastrado a partir de OS real analisada.",
  "proprietarioAtualId": 1,
  "dataInicioPosse": "2024-12-03",
  "observacaoPosse": "Histórico inicial de propriedade."
}
```

## 9. Exemplo de transferência de proprietário

```json
{
  "novoClienteId": 2,
  "dataInicioPosse": "2026-06-06",
  "observacao": "Transferência registrada pela oficina."
}
```

## 10. Padrão de projeto aplicado na etapa

```text
Adapter → VeiculoResponseAdapter
```

O `VeiculoResponseAdapter` adapta a estrutura interna formada por `VeiculoModel`, `ModeloModel`, `MarcaModel` e `HistoricoProprietarioModel` para respostas consolidadas usadas pelo Angular.

Esse uso é coerente porque o domínio guarda o proprietário em uma entidade histórica, enquanto a interface precisa receber os dados de forma resumida e compreensível, com marca, modelo, placa, proprietário atual e histórico de proprietários.

## 11. Arquivos principais criados

```text
business/veiculo/model/MarcaModel.java
business/veiculo/model/ModeloModel.java
business/veiculo/model/VeiculoModel.java
business/veiculo/model/HistoricoProprietarioModel.java

business/veiculo/dto/MarcaDTO.java
business/veiculo/dto/ModeloDTO.java
business/veiculo/dto/VeiculoDTO.java
business/veiculo/dto/VeiculoResumoDTO.java
business/veiculo/dto/HistoricoProprietarioDTO.java
business/veiculo/dto/TransferenciaProprietarioDTO.java

business/veiculo/repository/IMarcaRepository.java
business/veiculo/repository/IModeloRepository.java
business/veiculo/repository/IVeiculoRepository.java
business/veiculo/repository/IHistoricoProprietarioRepository.java

business/veiculo/mapper/MarcaMapper.java
business/veiculo/mapper/ModeloMapper.java
business/veiculo/mapper/VeiculoMapper.java
business/veiculo/mapper/HistoricoProprietarioMapper.java

business/veiculo/adapter/VeiculoResponseAdapter.java

business/veiculo/validation/MarcaValidation.java
business/veiculo/validation/ModeloValidation.java
business/veiculo/validation/VeiculoValidation.java

business/veiculo/service/MarcaService.java
business/veiculo/service/ModeloService.java
business/veiculo/service/VeiculoService.java

business/veiculo/controller/MarcaController.java
business/veiculo/controller/ModeloController.java
business/veiculo/controller/VeiculoController.java
```

## 12. Observação acadêmica

Esta etapa reforça a rastreabilidade entre cliente e veículo. O histórico de propriedade permite consultar quem é o proprietário atual e quem foram os proprietários anteriores, sem apagar dados antigos quando o veículo troca de dono.
