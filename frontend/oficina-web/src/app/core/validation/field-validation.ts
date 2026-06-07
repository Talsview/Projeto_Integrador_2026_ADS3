/**
 * Validações reutilizáveis do Angular.
 *
 * O objetivo é bloquear erros simples antes de chamar a API REST. As mesmas
 * regras críticas também existem no backend, que continua sendo a camada de
 * segurança e integridade real do sistema.
 */
export function somenteDigitosCampo(valor: string | number | null | undefined): string {
  return String(valor ?? '').replace(/\D/g, '');
}

export function textoObrigatorio(valor: string | null | undefined): boolean {
  return Boolean((valor ?? '').trim());
}

export function emailValido(valor: string | null | undefined): boolean {
  const email = (valor ?? '').trim();
  if (!email) return true;
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

export function telefoneValido(valor: string | null | undefined): boolean {
  const texto = String(valor ?? '').trim();
  const digitos = somenteDigitosCampo(texto);
  // Campo vazio é permitido quando o formulário tratar telefone como opcional.
  // Porém, se o usuário digitou letras ou símbolos não permitidos, o campo deve ser inválido.
  if (!texto) return true;
  if (/[^0-9()\s-]/.test(texto)) return false;
  if (!digitos) return false;
  return digitos.length >= 10 && digitos.length <= 11;
}

export function formatarTelefone(valor: string | number | null | undefined): string {
  const digitos = somenteDigitosCampo(valor).slice(0, 11);

  if (!digitos) return '';
  if (digitos.length <= 2) return `(${digitos}`;
  if (digitos.length <= 6) return `(${digitos.slice(0, 2)}) ${digitos.slice(2)}`;

  if (digitos.length <= 10) {
    return `(${digitos.slice(0, 2)}) ${digitos.slice(2, 6)}-${digitos.slice(6)}`;
  }

  return `(${digitos.slice(0, 2)}) ${digitos.slice(2, 7)}-${digitos.slice(7)}`;
}

export function nomePessoaValido(valor: string | null | undefined): boolean {
  const texto = (valor ?? '').trim();
  return texto.length >= 3 && !/\d/.test(texto) && /^[\p{L} .,'ºª-]+$/u.test(texto);
}

export function textoCadastroValido(valor: string | null | undefined, obrigatorio = true): boolean {
  const texto = (valor ?? '').trim();
  if (!texto) return !obrigatorio;
  return texto.length >= 2 && !/^\d+$/.test(texto) && /^[\p{L}0-9 .,'ºª&/-]+$/u.test(texto);
}

export function dataFutura(valor: string | null | undefined): boolean {
  if (!valor) return false;
  const data = new Date(`${valor}T00:00:00`);
  const hoje = new Date();
  hoje.setHours(0, 0, 0, 0);
  return data.getTime() > hoje.getTime();
}

export function dataHoraFutura(valor: string | null | undefined): boolean {
  if (!valor) return false;
  return new Date(valor).getTime() > Date.now();
}

export function dataHoraAnterior(a: string | null | undefined, b: string | null | undefined): boolean {
  if (!a || !b) return false;
  return new Date(a).getTime() < new Date(b).getTime();
}

export function anoVeiculoValido(ano: number | string | null | undefined, obrigatorio = true): boolean {
  if (ano === null || ano === undefined || ano === '') return !obrigatorio;
  const valor = Number(ano);
  const limite = new Date().getFullYear() + 1;
  return Number.isInteger(valor) && valor >= 1900 && valor <= limite;
}

export function normalizarPlaca(valor: string | null | undefined): string {
  return String(valor ?? '').replace(/[^a-zA-Z0-9]/g, '').toUpperCase().slice(0, 7);
}

export function placaValida(valor: string | null | undefined): boolean {
  const placa = normalizarPlaca(valor);
  return /^[A-Z]{3}[0-9]{4}$/.test(placa) || /^[A-Z]{3}[0-9][A-Z][0-9]{2}$/.test(placa);
}

export function normalizarChassi(valor: string | null | undefined): string {
  return String(valor ?? '').replace(/[^a-zA-Z0-9]/g, '').toUpperCase().replace(/[IOQ]/g, '').slice(0, 17);
}

export function chassiValido(valor: string | null | undefined): boolean {
  const chassi = String(valor ?? '').replace(/[^a-zA-Z0-9]/g, '').toUpperCase();
  if (!chassi) return true;
  return /^[A-HJ-NPR-Z0-9]{17}$/.test(chassi);
}

export function numeroMaiorQueZero(valor: number | string | null | undefined): boolean {
  return Number(valor ?? 0) > 0;
}

export function numeroNaoNegativo(valor: number | string | null | undefined): boolean {
  return Number(valor ?? 0) >= 0;
}

export function limitarTexto(valor: string | null | undefined, tamanho: number): string {
  return String(valor ?? '').slice(0, tamanho);
}
