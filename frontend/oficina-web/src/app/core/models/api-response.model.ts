export interface ApiResponse<T> {
  success?: boolean;
  sucesso?: boolean;
  status?: number;
  message?: string;
  mensagem?: string;
  data?: T;
  dados?: T;
  timestamp?: string;
}

export interface ErrorResponse {
  status: number;
  error: string;
  message: string;
  timestamp: string;
  path?: string;
  details?: string[];
}

export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}
