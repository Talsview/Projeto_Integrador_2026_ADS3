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
    public ResponseEntity<ApiResponse<List<PadraoProjetoDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.success("Padrões de projeto aplicados no sistema.", montarCatalogo()));
    }

    private List<PadraoProjetoDTO> montarCatalogo() {
        return List.of(
                new PadraoProjetoDTO(
                        "Singleton",
                        "Verificação local da conexão com o banco PostgreSQL.",
                        "DatabaseConnectionSingleton",
                        "Centraliza a checagem técnica da conexão local sem espalhar criação de conexão pelo sistema."),
                new PadraoProjetoDTO(
                        "Factory Method",
                        "Cadastro de cliente Pessoa Física e Pessoa Jurídica.",
                        "ClienteCadastroFactory / ClienteFactoryMethod",
                        "Seleciona a fábrica correta para criar cliente PF ou PJ, respeitando a especialização exclusiva e total."),
                new PadraoProjetoDTO(
                        "Adapter",
                        "Montagem da resposta de veículo para o Angular.",
                        "VeiculoResponseAdapter",
                        "Adapta entidades internas de Veículo, Modelo, Marca e Histórico de Proprietário para DTOs de resposta."),
                new PadraoProjetoDTO(
                        "Iterator",
                        "Percurso da Fila de Atendimento e da Lista Linear de busca.",
                        "OficinaIterator / FilaAtendimentoIterator / ListaLinearIterator",
                        "Permite percorrer estruturas lineares próprias sem expor sua implementação interna."),
                new PadraoProjetoDTO(
                        "Template Method",
                        "Ordenação manual de Ordens de Serviço.",
                        "OrdenadorTemplate",
                        "Define o esqueleto do algoritmo de ordenação e permite variar o critério por data, valor ou prioridade."),
                new PadraoProjetoDTO(
                        "Decorator",
                        "Notificação interna com auditoria operacional.",
                        "NotificadorAuditoriaDecorator",
                        "Adiciona auditoria à notificação sem alterar o componente base responsável pelo envio."));
    }
}
