import { NgZone, inject } from '@angular/core';
import { HttpErrorResponse, HttpEvent, HttpInterceptorFn } from '@angular/common/http';
import { Observable, TimeoutError } from 'rxjs';

/**
 * Interceptor global de comunicação com a API.
 *
 * Além de padronizar as mensagens de erro, este interceptor força o retorno
 * das respostas HTTP para dentro do NgZone do Angular. Também prioriza os
 * detalhes de erro enviados pelo backend, evitando mensagens genéricas como
 * "Violação de Regra de Negócio" quando existe uma causa específica.
 */
export const apiErrorInterceptor: HttpInterceptorFn = (req, next) => {
  const zone = inject(NgZone);

  return new Observable<HttpEvent<unknown>>((observer) => {
    const subscription = next(req).subscribe({
      next: (event) => {
        zone.run(() => observer.next(event));
      },
      error: (error: HttpErrorResponse | TimeoutError) => {
        zone.run(() => observer.error(normalizarErroApi(error)));
      },
      complete: () => {
        zone.run(() => observer.complete());
      }
    });

    return () => subscription.unsubscribe();
  });
};

function normalizarErroApi(error: HttpErrorResponse | TimeoutError): Error {
  if (error instanceof TimeoutError) {
    return new Error('A API demorou para responder. Verifique se o backend e o PostgreSQL estão em execução.');
  }

  const body: any = error.error ?? {};
  const data: any = body.data ?? body.dados ?? {};

  const detalhes = data.details
    ?? data.detalhes
    ?? body.details
    ?? body.detalhes
    ?? [];

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
