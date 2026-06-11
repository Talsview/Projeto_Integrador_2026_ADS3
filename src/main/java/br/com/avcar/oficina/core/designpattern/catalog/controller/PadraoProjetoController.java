package br.com.avcar.oficina.core.designpattern.catalog.controller;

import br.com.avcar.oficina.core.designpattern.catalog.dto.PadraoProjetoDTO;
import br.com.avcar.oficina.core.response.ApiResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller de apoio acadêmico para evidenciar, pelo Swagger, onde os seis
 * padrões de projeto exigidos pela disciplina foram aplicados.
 */
@RestController
@RequestMapping("/api/padroes-projeto")
public class PadraoProjetoController {

    @GetMapping
    /**
     * Função: Recebe filtros de consulta de módulo, delega a busca ao serviço e devolve os dados no
     * formato da API.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    public ResponseEntity<ApiResponse<List<PadraoProjetoDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.success("Padrões de projeto aplicados no sistema.", montarCatalogo()));
    }

    /**
     * Função: Atende a rota HTTP responsável por montar catalogo e repassa a regra ao serviço
     * correspondente.
     * Uso no sistema: mantém a camada Controller limitada à entrada e saída da API, sem concentrar
     * regra de negócio.
     */
    private List<PadraoProjetoDTO> montarCatalogo() {
        return List.of(
                new PadraoProjetoDTO(
                        "Singleton",
                        "Monitoramento local da conexão com o PostgreSQL.",
                        "DatabaseConnectionSingleton",
                        "Centraliza o diagnóstico do banco local, armazena o último estado verificado, controla falhas consecutivas e evita conexões repetidas em curto intervalo.",
                        "Tela Configurações e endpoint /api/database/status exibem tempo de resposta, quantidade de verificações, falhas consecutivas e último erro sanitizado."),
                new PadraoProjetoDTO(
                        "Factory Method",
                        "Cadastro de cliente Pessoa Física e Pessoa Jurídica.",
                        "ClienteCadastroFactory / ClienteFactoryMethod",
                        "Seleciona a fábrica correta para criar cliente PF ou PJ, respeitando a especialização exclusiva e total.",
                        "Ao cadastrar cliente, o sistema cria a estrutura correta de Pessoa, Cliente e especialização PF/PJ sem duplicar regras na service."),
                new PadraoProjetoDTO(
                        "Adapter",
                        "Montagem da resposta de veículo para o Angular.",
                        "VeiculoResponseAdapter",
                        "Adapta entidades internas de Veículo, Modelo, Marca e Histórico de Proprietário para DTOs de resposta.",
                        "A tela de Veículos recebe proprietário atual, marca, modelo e histórico em formato adequado à interface."),
                new PadraoProjetoDTO(
                        "Iterator",
                        "Percurso da Fila de Atendimento e da Lista Linear de busca.",
                        "OficinaIterator / FilaAtendimentoIterator / ListaLinearIterator",
                        "Permite percorrer estruturas lineares próprias sem expor sua implementação interna.",
                        "A tela Fila de Atendimento percorre a fila de OS em execução e a pesquisa linear sem usar diretamente os nós internos da estrutura."),
                new PadraoProjetoDTO(
                        "Template Method",
                        "Ordenação manual de Ordens de Serviço.",
                        "OrdenadorTemplate",
                        "Define o esqueleto do algoritmo de ordenação e permite variar o critério por data, valor ou prioridade.",
                        "A Fila de Atendimento reutiliza o mesmo algoritmo base para ordenar por data, valor e prioridade."),
                new PadraoProjetoDTO(
                        "Decorator",
                        "Notificação interna com auditoria operacional persistente.",
                        "NotificadorAuditoriaDecorator",
                        "Adiciona auditoria persistente à notificação sem alterar o componente base responsável pelo envio.",
                        "Mudanças de status da OS geram registros na tabela notificacao_auditoria e podem ser consultadas por /api/notificacoes/auditoria."));
    }
}
