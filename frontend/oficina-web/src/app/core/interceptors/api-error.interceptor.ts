import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';

export const apiErrorInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      const mensagem = error.error?.message || error.message || 'Erro inesperado ao comunicar com a API.';
      return throwError(() => new Error(mensagem));
    })
  );
};
