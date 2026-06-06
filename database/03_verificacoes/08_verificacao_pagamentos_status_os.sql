-- Verificação auxiliar da Etapa 18
-- Objetivo: conferir o status atual das Ordens de Serviço antes do registro de pagamento.
-- Este script não altera dados.

SELECT
    os.id_ordem_servico,
    os.numero_os,
    os.valor_total,
    s.nome_status AS status_atual,
    hs.data_status
FROM ordem_servico os
JOIN historico_status_ordem hs
    ON hs.id_ordem_servico = os.id_ordem_servico
JOIN status_ordem_servico s
    ON s.id_status_ordem_servico = hs.id_status_ordem_servico
WHERE os.ativo = TRUE
  AND hs.ativo = TRUE
  AND hs.data_status = (
      SELECT MAX(hs2.data_status)
      FROM historico_status_ordem hs2
      WHERE hs2.id_ordem_servico = os.id_ordem_servico
        AND hs2.ativo = TRUE
  )
ORDER BY os.id_ordem_servico;

-- Para registrar pagamento, o status_atual esperado é: PAGAMENTO.
