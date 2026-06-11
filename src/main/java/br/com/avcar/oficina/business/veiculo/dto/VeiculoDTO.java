package br.com.avcar.oficina.business.veiculo.dto;

import br.com.avcar.oficina.core.dto.BaseDTO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilizado para cadastro, atualização e consulta detalhada de veículo.
 *
 * Para cadastro, os campos modeloId e proprietarioAtualId são obrigatórios.
 * Para transferência de proprietário, utilizar TransferenciaProprietarioDTO.
 */
@Getter
@Setter
public class VeiculoDTO extends BaseDTO {

    private Long modeloId;
    private Long marcaId;
    private String nomeMarca;
    private String nomeModelo;
    private String placa;
    private String chassi;
    private String cor;
    private Integer anoVeiculo;
    private Integer anoModelo;
    private Integer quilometragemAtual;
    private String observacao;
    private Long proprietarioAtualId;
    private String nomeProprietarioAtual;
    private LocalDate dataInicioPosse;
    private String observacaoPosse;
    /**
     * Função: Representa ou apoia os dados usados na operação array list.
     * Uso no sistema: organiza a transferência ou persistência de informações entre as camadas.
     */
    private List<HistoricoProprietarioDTO> historicoProprietarios = new ArrayList<>();
}
