export const environment = {
  production: false,
  /**
   * Em desenvolvimento, a aplicação Angular deve chamar a API por meio do proxy.
   * O arquivo proxy.conf.json redireciona /api para http://localhost:9081/api.
   */
  apiBaseUrl: '/api',
  swaggerUrl: 'http://localhost:9081/swagger-ui.html'
};
