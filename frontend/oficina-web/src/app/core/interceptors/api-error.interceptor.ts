import { ApplicationRef, NgZone, inject } from '@angular/core';
import { HttpErrorResponse, HttpEvent, HttpInterceptorFn } from '@angular/common/http';
import { Observable, TimeoutError } from 'rxjs';

/**
 * Interceptor global de comunicação com a API.
 *
 * Além de padronizar as mensagens de erro, este interceptor garante que as
 * respostas HTTP retornem para dentro do NgZone do Angular e força uma
 * atualização visual após cada resposta. Essa correção evita o problema em que
 * a tela só exibia os dados depois de o usuário clicar em outro botão ou campo.
 */
export const apiErrorInterceptor: HttpInterceptorFn = (req, next) => {
  const zone = inject(NgZone);
  const appRef = inject(ApplicationRef);

  const atualizarInterface = () => {
    window.setTimeout(() => {
      zone.run(() => appRef.tick());
    }, 0);
  };

  return new Observable<HttpEvent<unknown>>((observer) => {
    const subscription = next(req).subscribe({
      next: (event) => {
        zone.run(() => {
          observer.next(event);
          atualizarInterface();
        });
      },
      error: (error: HttpErrorResponse | TimeoutError) => {
        zone.run(() => {
          observer.error(normalizarErroApi(error));
          atualizarInterface();
        });
      },
      complete: () => {
        zone.run(() => {
          observer.complete();
          atualizarInterface();
        });
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
