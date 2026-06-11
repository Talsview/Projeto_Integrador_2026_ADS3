import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { TimeoutError, catchError, throwError } from 'rxjs';

/**
 * Interceptor global de comunicação com a API.
 *
 * A atualização das tabelas foi concentrada nos componentes e services, pois cada tela
 * possui dependências específicas. O interceptor fica responsável por normalizar erros
 * retornados pelo backend Spring Boot.
 */
export const apiErrorInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    catchError((error: HttpErrorResponse | TimeoutError) => {
      return throwError(() => normalizarErroApi(error));
    })
  );
};

function normalizarErroApi(error: HttpErrorResponse | TimeoutError): Error {
  if (error instanceof TimeoutError) {
    return new Error('API sem resposta.');
  }

  const body: any = error.error ?? {};
  const data: any = body.data ?? body.dados ?? {};
  const detalhes = data.details ?? data.detalhes ?? body.details ?? body.detalhes ?? [];

  if (Array.isArray(detalhes) && detalhes.length > 0) {
    const detalhe = detalhes.find(item => typeof item === 'string' && item.trim().length > 0);
    if (detalhe) {
      return new Error(detalhe);
    }
  }

  const mensagem = data.message
    || data.mensagem
    || data.title
    || data.titulo
    || body.message
    || body.mensagem
    || error.message
    || 'Erro inesperado ao comunicar com a API.';

  return new Error(mensagem);
}
