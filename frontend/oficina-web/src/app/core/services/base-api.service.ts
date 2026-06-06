import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map, timeout } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse, PageResponse } from '../models/api-response.model';

export abstract class BaseApiService<T> {
  protected readonly apiBaseUrl = environment.apiBaseUrl;
  private readonly tempoLimiteMs = 10000;

  protected constructor(
    protected readonly http: HttpClient,
    private readonly resourcePath: string
  ) {}

  listar(): Observable<T[]> {
    return this.http.get<ApiResponse<PageResponse<T> | T[]>>(`${this.apiBaseUrl}/${this.resourcePath}`)
      .pipe(
        timeout(this.tempoLimiteMs),
        map(response => this.extrairLista(this.extrairDados(response)))
      );
  }

  buscarPorId(id: number): Observable<T> {
    return this.http.get<ApiResponse<T>>(`${this.apiBaseUrl}/${this.resourcePath}/${id}`)
      .pipe(
        timeout(this.tempoLimiteMs),
        map(response => this.extrairDados(response) as T)
      );
  }

  pesquisar(termo: string): Observable<T[]> {
    const params = new HttpParams().set('termo', termo ?? '');
    return this.http.get<ApiResponse<PageResponse<T> | T[]>>(`${this.apiBaseUrl}/${this.resourcePath}/pesquisar`, { params })
      .pipe(
        timeout(this.tempoLimiteMs),
        map(response => this.extrairLista(this.extrairDados(response)))
      );
  }

  criar(payload: Partial<T>): Observable<T> {
    return this.http.post<ApiResponse<T>>(`${this.apiBaseUrl}/${this.resourcePath}`, payload)
      .pipe(
        timeout(this.tempoLimiteMs),
        map(response => this.extrairDados(response) as T)
      );
  }

  atualizar(id: number, payload: Partial<T>): Observable<T> {
    return this.http.put<ApiResponse<T>>(`${this.apiBaseUrl}/${this.resourcePath}/${id}`, payload)
      .pipe(
        timeout(this.tempoLimiteMs),
        map(response => this.extrairDados(response) as T)
      );
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<ApiResponse<void>>(`${this.apiBaseUrl}/${this.resourcePath}/${id}`)
      .pipe(
        timeout(this.tempoLimiteMs),
        map(() => undefined)
      );
  }

  protected extrairDados<R>(response: ApiResponse<R>): R | null | undefined {
    return response.data ?? response.dados;
  }

  protected extrairMensagem(response: ApiResponse<unknown>): string {
    return response.message ?? response.mensagem ?? 'Operação executada com sucesso.';
  }

  private extrairLista(data: PageResponse<T> | T[] | null | undefined): T[] {
    if (!data) {
      return [];
    }
    if (Array.isArray(data)) {
      return data;
    }
    return data.content ?? [];
  }
}
