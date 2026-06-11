import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { routes } from './app.routes';
import { apiErrorInterceptor } from './core/interceptors/api-error.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: false, runCoalescing: false }),
    provideRouter(routes),
    /**
     * Função: Controla na tela a etapa provide http client.
     * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
     */
    provideHttpClient(withInterceptors([apiErrorInterceptor]))
  ]
};
