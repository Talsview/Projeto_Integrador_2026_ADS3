# Relatório de Auditoria e Otimização de Código

## Escopo analisado

- Arquivos analisados: 373
- Componentes Angular: 15
- Services Java: 21
- Scripts SQL: 15

## Arquivos por extensão

- `.css`: 3
- `.html`: 16
- `.java`: 219
- `.json`: 6
- `.md`: 63
- `.properties`: 1
- `.sql`: 15
- `.ts`: 50

## Linhas repetidas candidatas à refatoração

- 67x `@Transactional(readOnly = true)`
- 55x `import org.springframework.data.domain.Page;`
- 54x `import org.springframework.data.domain.Pageable;`
- 52x `ativo BOOLEAN NOT NULL DEFAULT TRUE,`
- 52x `data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,`
- 40x `import org.springframework.stereotype.Component;`
- 40x `data_hora_atualizacao TIMESTAMP,`
- 33x `import br.com.avcar.oficina.core.model.BaseModel;`
- 31x `validation.validateId(id);`
- 30x `import br.com.avcar.oficina.core.dto.BaseDTO;`
- 29x `import java.math.BigDecimal;`
- 27x `import jakarta.persistence.Column;`
- 27x `import br.com.avcar.oficina.core.repository.IGenericRepository;`
- 26x `import jakarta.persistence.Entity;`
- 26x `import jakarta.persistence.Id;`
- 26x `import jakarta.persistence.Table;`
- 26x `import org.springframework.stereotype.Repository;`
- 23x `import java.time.LocalDateTime;`
- 23x `error: error => { this.erro = error.message; this.atualizarTela(); }`
- 22x `import br.com.avcar.oficina.core.response.ApiResponse;`

## Componentes que usam atualização visual explícita

- `frontend/oficina-web/src/app/pages/clientes/clientes.component.ts`
- `frontend/oficina-web/src/app/pages/colaboradores/colaboradores.component.ts`
- `frontend/oficina-web/src/app/pages/empresas-terceirizadas/empresas-terceirizadas.component.ts`
- `frontend/oficina-web/src/app/pages/estrutura-dados/estrutura-dados.component.ts`
- `frontend/oficina-web/src/app/pages/funcoes/funcoes.component.ts`
- `frontend/oficina-web/src/app/pages/garantias/garantias.component.ts`
- `frontend/oficina-web/src/app/pages/itens-os/itens-os.component.ts`
- `frontend/oficina-web/src/app/pages/marcas-modelos/marcas-modelos.component.ts`
- `frontend/oficina-web/src/app/pages/ordens-servico/ordens-servico.component.ts`
- `frontend/oficina-web/src/app/pages/pagamentos/pagamentos.component.ts`
- `frontend/oficina-web/src/app/pages/pecas-fornecedores/pecas-fornecedores.component.ts`
- `frontend/oficina-web/src/app/pages/servicos/servicos.component.ts`
- `frontend/oficina-web/src/app/pages/veiculos/veiculos.component.ts`

## Recomendações

- Manter o menu do Angular orientado por dados, evitando links repetidos no HTML.
- Manter o `BaseApiService` como ponto único para GET sem cache, extração de `ApiResponse` e atualização após salvar/excluir.
- Evitar recriar telas técnicas no menu operacional; padrões de projeto devem ficar na documentação e no código comentado.
- Preservar scripts SQL separados por finalidade: schema, seed, verificações e completo opcional.