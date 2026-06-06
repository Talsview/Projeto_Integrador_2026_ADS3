import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse, PageResponse } from '../models/api-response.model';

export abstract class BaseApiService<T> {
  protected readonly apiBaseUrl = environment.apiBaseUrl;

  protected constructor(
    protected readonly http: HttpClient,
    private readonly resourcePath: string
  ) {}

  listar(): Observable<T[]> {
    return this.http.get<ApiResponse<PageResponse<T> | T[]>>(`${this.apiBaseUrl}/${this.resourcePath}`)
      .pipe(map(response => this.extrairLista(response.data)));
  }

  buscarPorId(id: number): Observable<T> {
    return this.http.get<ApiResponse<T>>(`${this.apiBaseUrl}/${this.resourcePath}/${id}`)
      .pipe(map(response => response.data));
  }

  pesquisar(termo: string): Observable<T[]> {
    const params = new HttpParams().set('termo', termo ?? '');
    return this.http.get<ApiResponse<PageResponse<T> | T[]>>(`${this.apiBaseUrl}/${this.resourcePath}/pesquisar`, { params })
      .pipe(map(response => this.extrairLista(response.data)));
  }

  criar(payload: Partial<T>): Observable<T> {
    return this.http.post<ApiResponse<T>>(`${this.apiBaseUrl}/${this.resourcePath}`, payload)
      .pipe(map(response => response.data));
  }

  atualizar(id: number, payload: Partial<T>): Observable<T> {
    return this.http.put<ApiResponse<T>>(`${this.apiBaseUrl}/${this.resourcePath}/${id}`, payload)
      .pipe(map(response => response.data));
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<ApiResponse<void>>(`${this.apiBaseUrl}/${this.resourcePath}/${id}`)
      .pipe(map(() => undefined));
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
