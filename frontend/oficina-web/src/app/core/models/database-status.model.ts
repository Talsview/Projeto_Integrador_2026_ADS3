export interface DatabaseStatus {
  available: boolean;
  mensagem: string;
  tempoRespostaMs?: number;
  dataHoraVerificacao?: string;
  dataHoraUltimaMudancaStatus?: string;
  verificacoesRealizadas?: number;
  falhasConsecutivas?: number;
  ultimoErro?: string;
  cacheUtilizado?: boolean;
}
