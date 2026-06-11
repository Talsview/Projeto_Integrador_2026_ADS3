import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, map, switchMap, timeout } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse, PageResponse } from '../models/api-response.model';

/**
 * Serviço base para comunicação REST com o backend Spring Boot.
 *
 * Correção aplicada na Etapa 25:
 * - Toda consulta GET usa no-cache e parâmetro _t para evitar dados antigos no navegador.
 * - A extração de resposta aceita ApiResponse, PageResponse, arrays diretos e objetos diretos.
 * - A lista sempre é retornada com nova referência para forçar atualização visual nas tabelas.
 */
export abstract class BaseApiService<T> {
  protected readonly apiBaseUrl = environment.apiBaseUrl;
  protected readonly tempoLimiteMs = 10000;

  protected readonly noCacheHeaders = new HttpHeaders({
    'Cache-Control': 'no-cache',
    'Pragma': 'no-cache',
    'Expires': '0'
  });

  /**
   * Função: Recebe os serviços necessários para esta classe, como HttpClient, APIs ou dependências
   * de navegação.
   * Uso no sistema: permite que o Angular injete dependências sem criação manual dentro dos métodos.
   */
  protected constructor(
    protected readonly http: HttpClient,
    private readonly resourcePath: string
  ) {}

  /**
   * Função: Monta a chamada HTTP de consulta e normaliza a resposta recebida da API.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  listar(): Observable<T[]> {
    return this.http.get<ApiResponse<PageResponse<T> | T[]> | PageResponse<T> | T[]>(
      `${this.apiBaseUrl}/${this.resourcePath}`,
      this.opcoesSemCache()
    ).pipe(
      timeout(this.tempoLimiteMs),
      /**
       * Função: Executa a integração HTTP necessária para map.
       * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
       * componentes focados na tela.
       */
      map(response => this.extrairListaDeResposta<T>(response))
    );
  }


  /**
   * Função: Monta a chamada HTTP para listar ou reativar registros inativados no backend.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  listarInativos(): Observable<T[]> {
    return this.http.get<ApiResponse<PageResponse<T> | T[]> | PageResponse<T> | T[]>(
      `${this.apiBaseUrl}/${this.resourcePath}/inativos`,
      this.opcoesSemCache()
    ).pipe(
      timeout(this.tempoLimiteMs),
      map(response => this.extrairListaDeResposta<T>(response))
    );
  }

  /**
   * Função: Executa a integração HTTP necessária para ativar.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  ativar(id: number): Observable<T> {
    return this.http.patch<ApiResponse<T> | T>(`${this.apiBaseUrl}/${this.resourcePath}/${id}/ativar`, {})
      .pipe(
        timeout(this.tempoLimiteMs),
        map(response => this.extrairDados(response) as T)
      );
  }

  /**
   * Função: Monta a chamada HTTP de consulta e normaliza a resposta recebida da API.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  ativarEListar(id: number): Observable<T[]> {
    return this.ativar(id).pipe(switchMap(() => this.listar()));
  }

  /**
   * Função: Monta a chamada HTTP de consulta e normaliza a resposta recebida da API.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  buscarPorId(id: number): Observable<T> {
    return this.http.get<ApiResponse<T> | T>(
      `${this.apiBaseUrl}/${this.resourcePath}/${id}`,
      this.opcoesSemCache()
    ).pipe(
      timeout(this.tempoLimiteMs),
      /**
       * Função: Executa a integração HTTP necessária para map.
       * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
       * componentes focados na tela.
       */
      map(response => this.extrairDados(response) as T)
    );
  }

  /**
   * Função: Monta a chamada HTTP de consulta e normaliza a resposta recebida da API.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  pesquisar(termo: string): Observable<T[]> {
    return this.http.get<ApiResponse<PageResponse<T> | T[]> | PageResponse<T> | T[]>(
      `${this.apiBaseUrl}/${this.resourcePath}/pesquisar`,
      this.opcoesSemCache(new HttpParams().set('termo', termo ?? ''))
    ).pipe(
      timeout(this.tempoLimiteMs),
      /**
       * Função: Executa a integração HTTP necessária para map.
       * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
       * componentes focados na tela.
       */
      map(response => this.extrairListaDeResposta<T>(response))
    );
  }

  /**
   * Função: Envia ao backend os dados preenchidos na tela para gravação.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  criar(payload: Partial<T>): Observable<T> {
    return this.http.post<ApiResponse<T> | T>(`${this.apiBaseUrl}/${this.resourcePath}`, payload)
      .pipe(
        timeout(this.tempoLimiteMs),
        /**
         * Função: Executa a integração HTTP necessária para map.
         * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
         * componentes focados na tela.
         */
        map(response => this.extrairDados(response) as T)
      );
  }

  /**
   * Função: Envia ao backend a alteração de um registro já existente.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  atualizar(id: number, payload: Partial<T>): Observable<T> {
    return this.http.put<ApiResponse<T> | T>(`${this.apiBaseUrl}/${this.resourcePath}/${id}`, payload)
      .pipe(
        timeout(this.tempoLimiteMs),
        /**
         * Função: Executa a integração HTTP necessária para map.
         * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
         * componentes focados na tela.
         */
        map(response => this.extrairDados(response) as T)
      );
  }

  /**
   * Função: Solicita ao backend a inativação lógica do registro, preservando histórico.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  excluir(id: number): Observable<void> {
    return this.http.delete<ApiResponse<void> | void>(`${this.apiBaseUrl}/${this.resourcePath}/${id}`)
      .pipe(
        timeout(this.tempoLimiteMs),
        /**
         * Função: Executa a integração HTTP necessária para map.
         * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
         * componentes focados na tela.
         */
        map(() => undefined)
      );
  }

  /**
   * Função: Monta a chamada HTTP de consulta e normaliza a resposta recebida da API.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  salvarEListar(id: number | null | undefined, payload: Partial<T>): Observable<T[]> {
    const acao = id ? this.atualizar(id, payload) : this.criar(payload);
    return acao.pipe(switchMap(() => this.listar()));
  }

  /**
   * Função: Monta a chamada HTTP de consulta e normaliza a resposta recebida da API.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  excluirEListar(id: number): Observable<T[]> {
    return this.excluir(id).pipe(switchMap(() => this.listar()));
  }

  /**
   * Função: Monta a chamada HTTP de consulta e normaliza a resposta recebida da API.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  atualizarEListar(id: number, payload: Partial<T>): Observable<T[]> {
    return this.atualizar(id, payload).pipe(switchMap(() => this.listar()));
  }

  protected extrairDados<R>(response: ApiResponse<R> | R | null | undefined): R | null | undefined {
    if (response === null) {
      return null;
    }
    if (response === undefined) {
      return undefined;
    }

    const body: any = response;
    if (Object.prototype.hasOwnProperty.call(body, 'data')) {
      return body.data as R;
    }
    if (Object.prototype.hasOwnProperty.call(body, 'dados')) {
      return body.dados as R;
    }

    return response as unknown as R;
  }

  /**
   * Função: Executa a integração HTTP necessária para extrair mensagem.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  protected extrairMensagem(response: ApiResponse<unknown>): string {
    return response.message ?? response.mensagem ?? 'Operação concluída.';
  }

  /**
   * Função: Executa a integração HTTP necessária para parametros sem cache.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  protected parametrosSemCache(params?: HttpParams): HttpParams {
    return (params ?? new HttpParams()).set('_t', Date.now().toString());
  }

  /**
   * Função: Executa a integração HTTP necessária para opcoes sem cache.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  protected opcoesSemCache(params?: HttpParams): { headers: HttpHeaders; params: HttpParams } {
    return {
      headers: this.noCacheHeaders,
      params: this.parametrosSemCache(params)
    };
  }

  protected extrairListaDeResposta<R>(response: ApiResponse<PageResponse<R> | R[]> | PageResponse<R> | R[] | null | undefined): R[] {
    const dados = this.extrairDados(response as any) as PageResponse<R> | R[] | null | undefined;
    return this.extrairLista(dados);
  }

  protected extrairLista<R>(data: PageResponse<R> | R[] | null | undefined): R[] {
    if (!data) {
      return [];
    }
    if (Array.isArray(data)) {
      return [...data];
    }
    return [...(data.content ?? [])];
  }
}
