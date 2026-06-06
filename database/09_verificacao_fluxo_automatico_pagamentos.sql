-- Verificação da Etapa 19: fluxo automático de pagamentos.
-- Objetivo: apoiar a conferência manual no pgAdmin após salvar pagamento pelo frontend.

SELECT
    os.id_ordem_servico,
    os.numero_os,
    os.valor_total,
    sos.nome_status AS status_atual,
    hso.data_status
FROM ordem_servico os
JOIN historico_status_ordem hso ON hso.id_ordem_servico = os.id_ordem_servico
JOIN status_ordem_servico sos ON sos.id_status_ordem_servico = hso.id_status_ordem_servico
WHERE hso.ativo = TRUE
  AND os.ativo = TRUE
  AND hso.data_status = (
      SELECT MAX(h2.data_status)
      FROM historico_status_ordem h2
      WHERE h2.id_ordem_servico = os.id_ordem_servico
        AND h2.ativo = TRUE
  )
ORDER BY os.id_ordem_servico;

SELECT
    p.id_pagamento,
    p.id_ordem_servico,
    p.forma_pagamento,
    p.status_pagamento,
    p.valor_pago,
    p.data_pagamento
FROM pagamento p
WHERE p.ativo = TRUE
ORDER BY p.id_pagamento DESC;
