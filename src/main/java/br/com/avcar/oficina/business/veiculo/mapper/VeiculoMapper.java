package br.com.avcar.oficina.business.veiculo.mapper;

import br.com.avcar.oficina.business.veiculo.dto.VeiculoDTO;
import br.com.avcar.oficina.business.veiculo.model.ModeloModel;
import br.com.avcar.oficina.business.veiculo.model.VeiculoModel;
import org.springframework.stereotype.Component;

/**
 * Mapper usado para montar e atualizar o Model de Veículo a partir do DTO de entrada.
 */
@Component
public class VeiculoMapper {

    /**
     * Função: Converte entidades do domínio em DTOs usados pela API e pelo frontend.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public VeiculoModel toModel(VeiculoDTO dto, ModeloModel modelo) {
        if (dto == null) {
            return null;
        }
        VeiculoModel model = new VeiculoModel();
        model.setModelo(modelo);
        atualizarCampos(model, dto, modelo);
        return model;
    }

    /**
     * Função: Copia para a entidade existente apenas os campos que podem ser alterados pelo usuário.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public void atualizarCampos(VeiculoModel model, VeiculoDTO dto, ModeloModel modelo) {
        model.setModelo(modelo);
        model.setPlaca(normalizePlaca(dto.getPlaca()));
        model.setChassi(normalizeNullable(dto.getChassi()));
        model.setCor(normalizeNullable(dto.getCor()));
        model.setAnoVeiculo(dto.getAnoVeiculo());
        model.setAnoModelo(dto.getAnoModelo());
        model.setQuilometragemAtual(dto.getQuilometragemAtual());
        model.setObservacao(dto.getObservacao());
    }

    /**
     * Função: Mapeia dados entre camadas durante a operação normalize placa.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public String normalizePlaca(String placa) {
        if (placa == null) {
            return null;
        }
        return placa.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
    }

    /**
     * Função: Mapeia dados entre camadas durante a operação normalize nullable.
     * Uso no sistema: evita que Controller e Service fiquem misturando regras de conversão de objetos.
     */
    public String normalizeNullable(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim().toUpperCase();
    }
}
