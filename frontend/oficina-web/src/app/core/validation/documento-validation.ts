/**
 * Validações de documentos brasileiros usadas no Angular.
 *
 * A validação no frontend melhora a experiência do usuário, mas a regra também
 * existe no backend. Assim, CPF/CNPJ inválidos são bloqueados tanto na tela
 * quanto na API REST.
 */
export function somenteDigitos(valor: string | null | undefined): string {
  return (valor ?? '').replace(/\D/g, '');
}

export function cpfValido(valor: string | null | undefined): boolean {
  const cpf = somenteDigitos(valor);
  if (cpf.length !== 11 || todosDigitosIguais(cpf)) {
    return false;
  }

  const primeiroDigito = calcularDigitoCpf(cpf, 9, 10);
  const segundoDigito = calcularDigitoCpf(cpf, 10, 11);

  return Number(cpf.charAt(9)) === primeiroDigito && Number(cpf.charAt(10)) === segundoDigito;
}

export function cnpjValido(valor: string | null | undefined): boolean {
  const cnpj = somenteDigitos(valor);
  if (cnpj.length !== 14 || todosDigitosIguais(cnpj)) {
    return false;
  }

  const primeiroDigito = calcularDigitoCnpj(cnpj, 12, [5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2]);
  const segundoDigito = calcularDigitoCnpj(cnpj, 13, [6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2]);

  return Number(cnpj.charAt(12)) === primeiroDigito && Number(cnpj.charAt(13)) === segundoDigito;
}

export function formatarCpf(valor: string | null | undefined): string {
  const cpf = somenteDigitos(valor).slice(0, 11);
  return cpf
    .replace(/^(\d{3})(\d)/, '$1.$2')
    .replace(/^(\d{3})\.(\d{3})(\d)/, '$1.$2.$3')
    .replace(/^(\d{3})\.(\d{3})\.(\d{3})(\d)/, '$1.$2.$3-$4');
}

export function formatarCnpj(valor: string | null | undefined): string {
  const cnpj = somenteDigitos(valor).slice(0, 14);
  return cnpj
    .replace(/^(\d{2})(\d)/, '$1.$2')
    .replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3')
    .replace(/^(\d{2})\.(\d{3})\.(\d{3})(\d)/, '$1.$2.$3/$4')
    .replace(/^(\d{2})\.(\d{3})\.(\d{3})\/(\d{4})(\d)/, '$1.$2.$3/$4-$5');
}

function calcularDigitoCpf(cpf: string, quantidadeDigitos: number, pesoInicial: number): number {
  let soma = 0;
  for (let i = 0; i < quantidadeDigitos; i++) {
    soma += Number(cpf.charAt(i)) * (pesoInicial - i);
  }
  const resto = soma % 11;
  return resto < 2 ? 0 : 11 - resto;
}

function calcularDigitoCnpj(cnpj: string, quantidadeDigitos: number, pesos: number[]): number {
  let soma = 0;
  for (let i = 0; i < quantidadeDigitos; i++) {
    soma += Number(cnpj.charAt(i)) * pesos[i];
  }
  const resto = soma % 11;
  return resto < 2 ? 0 : 11 - resto;
}

function todosDigitosIguais(valor: string): boolean {
  return valor.split('').every(digito => digito === valor.charAt(0));
}
