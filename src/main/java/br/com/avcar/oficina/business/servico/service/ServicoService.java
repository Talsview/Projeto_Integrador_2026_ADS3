package br.com.avcar.oficina.business.servico.service;

import br.com.avcar.oficina.business.servico.dto.ServicoDTO;
import br.com.avcar.oficina.business.servico.enums.TipoServico;
import br.com.avcar.oficina.business.servico.mapper.ServicoMapper;
import br.com.avcar.oficina.business.servico.model.ServicoInternoModel;
import br.com.avcar.oficina.business.servico.model.ServicoModel;
import br.com.avcar.oficina.business.servico.model.ServicoTerceirizadoModel;
import br.com.avcar.oficina.business.servico.repository.IServicoInternoRepository;
import br.com.avcar.oficina.business.servico.repository.IServicoRepository;
import br.com.avcar.oficina.business.servico.repository.IServicoTerceirizadoRepository;
import br.com.avcar.oficina.business.servico.validation.ServicoValidation;
import br.com.avcar.oficina.core.exception.BusinessException;
import br.com.avcar.oficina.core.exception.RuleValidationException;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsável pelas regras de negócio de Serviço.
 *
 * Regra de negócio implementada: todo Serviço deve ser classificado como
 * Interno ou Terceirizado, de forma exclusiva e total. Serviços terceirizados
 * permanecem sob responsabilidade da oficina perante o cliente.
 */
@Service
public class ServicoService {

    private final IServicoRepository servicoRepository;
    private final IServicoInternoRepository servicoInternoRepository;
    private final IServicoTerceirizadoRepository servicoTerceirizadoRepository;
    private final ServicoValidation validation;
    private final ServicoMapper mapper;

    public ServicoService(IServicoRepository servicoRepository,
                          IServicoInternoRepository servicoInternoRepository,
                          IServicoTerceirizadoRepository servicoTerceirizadoRepository,
                          ServicoValidation validation,
                          ServicoMapper mapper) {
        this.servicoRepository = servicoRepository;
        this.servicoInternoRepository = servicoInternoRepository;
        this.servicoTerceirizadoRepository = servicoTerceirizadoRepository;
        this.validation = validation;
        this.mapper = mapper;
    }

    @Transactional
    public ServicoDTO cadastrar(ServicoDTO dto) {
        validation.validateInsert(dto);

        ServicoModel servico = mapper.toServicoModel(dto);
        ServicoModel saved = servicoRepository.save(servico);

        salvarEspecializacao(saved, dto);
        return buscar(saved.getId());
    }

    @Transactional
    public ServicoDTO atualizar(Long id, ServicoDTO dto) {
        validation.validateUpdate(id, dto);

        ServicoModel servico = buscarModelAtivo(id);
        mapper.atualizarServicoModel(servico, dto);
        ServicoModel saved = servicoRepository.save(servico);

        atualizarEspecializacao(saved, dto);
        return buscar(saved.getId());
    }

    @Transactional(readOnly = true)
    public ServicoDTO buscar(Long id) {
        validation.validateId(id);
        ServicoModel servico = buscarModelAtivo(id);
        return montarDto(servico);
    }

    @Transactional(readOnly = true)
    public Page<ServicoDTO> listar(Pageable pageable) {
        return servicoRepository.findAllByAtivoTrue(pageable).map(this::montarDto);
    }

    @Transactional(readOnly = true)
    public Page<ServicoDTO> listarPorTipo(TipoServico tipoServico, Pageable pageable) {
        if (tipoServico == null) {
            throw new RuleValidationException("O tipo do serviço é obrigatório para a consulta.");
        }

        if (tipoServico == TipoServico.INTERNO) {
            return servicoRepository.findServicosInternosAtivos(pageable).map(this::montarDto);
        }
        return servicoRepository.findServicosTerceirizadosAtivos(pageable).map(this::montarDto);
    }

    @Transactional(readOnly = true)
    public Page<ServicoDTO> pesquisar(String termo, Pageable pageable) {
        if (termo == null || termo.isBlank()) {
            return listar(pageable);
        }
        return servicoRepository.search(termo.trim(), pageable).map(this::montarDto);
    }

    @Transactional
    public void inativar(Long id) {
        validation.validateId(id);
        ServicoModel servico = buscarModelAtivo(id);
        servico.setAtivo(Boolean.FALSE);
        servicoRepository.save(servico);

        servicoInternoRepository.findByIdAndAtivoTrue(id).ifPresent(interno -> {
            interno.setAtivo(Boolean.FALSE);
            servicoInternoRepository.save(interno);
        });

        servicoTerceirizadoRepository.findByIdAndAtivoTrue(id).ifPresent(terceirizado -> {
            terceirizado.setAtivo(Boolean.FALSE);
            servicoTerceirizadoRepository.save(terceirizado);
        });
    }

    public ServicoModel buscarModelAtivo(Long id) {
        return servicoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Serviço não encontrado ou inativo."));
    }

    private void salvarEspecializacao(ServicoModel servico, ServicoDTO dto) {
        if (dto.getTipoServico() == TipoServico.INTERNO) {
            servicoInternoRepository.save(mapper.toServicoInternoModel(servico, dto));
            return;
        }
        servicoTerceirizadoRepository.save(mapper.toServicoTerceirizadoModel(servico, dto));
    }

    private void atualizarEspecializacao(ServicoModel servico, ServicoDTO dto) {
        if (dto.getTipoServico() == TipoServico.INTERNO) {
            inativarEspecializacaoTerceirizada(servico.getId());
            ServicoInternoModel interno = servicoInternoRepository.findById(servico.getId())
                    .orElseGet(ServicoInternoModel::new);
            mapper.atualizarServicoInternoModel(interno, servico, dto);
            servicoInternoRepository.save(interno);
            return;
        }

        inativarEspecializacaoInterna(servico.getId());
        ServicoTerceirizadoModel terceirizado = servicoTerceirizadoRepository.findById(servico.getId())
                .orElseGet(ServicoTerceirizadoModel::new);
        mapper.atualizarServicoTerceirizadoModel(terceirizado, servico, dto);
        servicoTerceirizadoRepository.save(terceirizado);
    }

    private void inativarEspecializacaoInterna(Long idServico) {
        servicoInternoRepository.findByIdAndAtivoTrue(idServico).ifPresent(interno -> {
            interno.setAtivo(Boolean.FALSE);
            servicoInternoRepository.save(interno);
        });
    }

    private void inativarEspecializacaoTerceirizada(Long idServico) {
        servicoTerceirizadoRepository.findByIdAndAtivoTrue(idServico).ifPresent(terceirizado -> {
            terceirizado.setAtivo(Boolean.FALSE);
            servicoTerceirizadoRepository.save(terceirizado);
        });
    }

    private ServicoDTO montarDto(ServicoModel servico) {
        Optional<ServicoInternoModel> interno = servicoInternoRepository.findByIdAndAtivoTrue(servico.getId());
        Optional<ServicoTerceirizadoModel> terceirizado = servicoTerceirizadoRepository.findByIdAndAtivoTrue(servico.getId());
        return mapper.toDto(servico, interno.orElse(null), terceirizado.orElse(null));
    }
}
