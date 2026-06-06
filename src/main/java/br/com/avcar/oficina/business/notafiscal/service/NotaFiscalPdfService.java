package br.com.avcar.oficina.business.notafiscal.service;

import br.com.avcar.oficina.business.ordemservico.model.ItemServicoModel;
import br.com.avcar.oficina.business.ordemservico.model.OrdemServicoModel;
import br.com.avcar.oficina.business.ordemservico.model.HistoricoStatusOrdemModel;
import br.com.avcar.oficina.business.ordemservico.repository.IHistoricoStatusOrdemRepository;
import br.com.avcar.oficina.business.ordemservico.repository.IItemServicoRepository;
import br.com.avcar.oficina.business.ordemservico.repository.IOrdemServicoRepository;
import br.com.avcar.oficina.business.pagamento.enums.StatusPagamento;
import br.com.avcar.oficina.business.pagamento.model.PagamentoModel;
import br.com.avcar.oficina.business.pagamento.repository.IPagamentoRepository;
import br.com.avcar.oficina.business.peca.model.ItemPecaModel;
import br.com.avcar.oficina.business.peca.repository.IItemPecaRepository;
import br.com.avcar.oficina.business.pessoa.model.PessoaFisicaModel;
import br.com.avcar.oficina.business.pessoa.model.PessoaJuridicaModel;
import br.com.avcar.oficina.business.pessoa.repository.IPessoaFisicaRepository;
import br.com.avcar.oficina.business.pessoa.repository.IPessoaJuridicaRepository;
import br.com.avcar.oficina.core.exception.BusinessException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Geração de documento fiscal interno em PDF para Ordem de Serviço.
 *
 * Observação acadêmica: este documento segue o layout das OS fornecidas pela
 * oficina, porém não substitui emissão fiscal eletrônica autorizada por prefeitura
 * ou SEFAZ. Ele é um comprovante/nota simplificada para o sistema local.
 */
@Service
public class NotaFiscalPdfService {

    private static final String EMPRESA_NOME = "AV CAR AUTO CENTER";
    private static final String EMPRESA_ENDERECO = "Avenida Universitária, QD G LT 06, Setor Leste Universitário, Goiânia-GO";
    private static final String EMPRESA_REFERENCIA = "DEPOIS DO SUPERMERCADO MARIANA";
    private static final String EMPRESA_TELEFONE = "62 3300-0006";
    private static final String EMPRESA_EMAIL = "avcarautocenter@gmail.com";
    private static final Locale LOCALE_BR = new Locale("pt", "BR");
    private static final DateTimeFormatter DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Color AZUL_OFICINA = new Color(11, 46, 107);
    private static final Color LARANJA_OFICINA = new Color(244, 81, 30);
    private static final Color CINZA_CABECALHO = new Color(224, 224, 224);
    private static final Color CINZA_CLARO = new Color(245, 247, 250);
    private static final Color BORDA = new Color(80, 80, 80);

    private final IOrdemServicoRepository ordemServicoRepository;
    private final IItemServicoRepository itemServicoRepository;
    private final IItemPecaRepository itemPecaRepository;
    private final IPagamentoRepository pagamentoRepository;
    private final IHistoricoStatusOrdemRepository historicoStatusRepository;
    private final IPessoaFisicaRepository pessoaFisicaRepository;
    private final IPessoaJuridicaRepository pessoaJuridicaRepository;

    public NotaFiscalPdfService(IOrdemServicoRepository ordemServicoRepository,
                                IItemServicoRepository itemServicoRepository,
                                IItemPecaRepository itemPecaRepository,
                                IPagamentoRepository pagamentoRepository,
                                IHistoricoStatusOrdemRepository historicoStatusRepository,
                                IPessoaFisicaRepository pessoaFisicaRepository,
                                IPessoaJuridicaRepository pessoaJuridicaRepository) {
        this.ordemServicoRepository = ordemServicoRepository;
        this.itemServicoRepository = itemServicoRepository;
        this.itemPecaRepository = itemPecaRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.historicoStatusRepository = historicoStatusRepository;
        this.pessoaFisicaRepository = pessoaFisicaRepository;
        this.pessoaJuridicaRepository = pessoaJuridicaRepository;
    }

    @Transactional(readOnly = true)
    public byte[] gerarNotaFiscalOrdemServico(Long idOrdemServico) {
        if (idOrdemServico == null) {
            throw new BusinessException("O identificador da Ordem de Serviço é obrigatório para gerar a nota fiscal.");
        }

        OrdemServicoModel ordem = ordemServicoRepository.findByIdAndAtivoTrue(idOrdemServico)
                .orElseThrow(() -> new BusinessException("Ordem de Serviço não encontrada ou inativa."));

        List<ItemServicoModel> servicos = itemServicoRepository.findByOrdemServicoIdAndAtivoTrue(ordem.getId());
        List<ItemPecaModel> pecas = itemPecaRepository.findByIdOrdemServicoAndAtivoTrue(ordem.getId());
        List<PagamentoModel> pagamentos = pagamentoRepository.findByOrdemServicoIdAndAtivoTrue(ordem.getId());
        HistoricoStatusOrdemModel statusAtual = buscarStatusAtual(ordem.getId()).orElse(null);

        recalcularEmMemoria(ordem, servicos, pecas);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 26, 26, 24, 24);
            PdfWriter.getInstance(document, outputStream);
            document.open();

            adicionarCabecalho(document, ordem);
            adicionarDadosCliente(document, ordem);
            adicionarDadosVeiculo(document, ordem, statusAtual);
            adicionarSecaoItens(document, pecas, servicos);
            adicionarTotais(document, servicos, pecas, pagamentos, ordem);
            adicionarObservacoes(document, ordem);
            adicionarAssinaturas(document, ordem);

            document.close();
            return outputStream.toByteArray();
        } catch (DocumentException | IOException ex) {
            throw new BusinessException("Não foi possível gerar o PDF da nota fiscal da Ordem de Serviço.");
        }
    }

    public String montarNomeArquivo(Long idOrdemServico) {
        OrdemServicoModel ordem = ordemServicoRepository.findByIdAndAtivoTrue(idOrdemServico)
                .orElseThrow(() -> new BusinessException("Ordem de Serviço não encontrada ou inativa."));
        String numero = texto(ordem.getNumeroOs()).replaceAll("[^A-Za-z0-9_-]", "-");
        if (numero.isBlank()) {
            numero = String.valueOf(ordem.getId());
        }
        return "nota-fiscal-os-" + numero + ".pdf";
    }

    private void adicionarCabecalho(Document document, OrdemServicoModel ordem) throws DocumentException, IOException {
        PdfPTable cabecalho = new PdfPTable(new float[]{1.5f, 3.8f, 1.8f});
        cabecalho.setWidthPercentage(100);

        PdfPCell logoCell = semBorda();
        Optional<Image> logo = carregarLogo();
        if (logo.isPresent()) {
            Image imagem = logo.get();
            imagem.scaleToFit(150, 58);
            logoCell.addElement(imagem);
        } else {
            Paragraph marca = new Paragraph("AV CAR", fonteTitulo(18, AZUL_OFICINA));
            marca.setAlignment(Element.ALIGN_LEFT);
            logoCell.addElement(marca);
        }
        cabecalho.addCell(logoCell);

        PdfPCell empresaCell = semBorda();
        empresaCell.addElement(paragrafo(EMPRESA_NOME, fonteTitulo(12, AZUL_OFICINA), Element.ALIGN_CENTER));
        empresaCell.addElement(paragrafo(EMPRESA_ENDERECO, fonteNormal(8), Element.ALIGN_CENTER));
        empresaCell.addElement(paragrafo(EMPRESA_REFERENCIA, fonteNormal(8), Element.ALIGN_CENTER));
        empresaCell.addElement(paragrafo(EMPRESA_TELEFONE + " | " + EMPRESA_EMAIL, fonteNormal(8), Element.ALIGN_CENTER));
        cabecalho.addCell(empresaCell);

        PdfPCell emissaoCell = semBorda();
        emissaoCell.addElement(paragrafo("Goiânia-GO", fonteNegrito(8), Element.ALIGN_RIGHT));
        emissaoCell.addElement(paragrafo(formatarDataHora(LocalDateTime.now()), fonteNormal(8), Element.ALIGN_RIGHT));
        emissaoCell.addElement(paragrafo("Série interna: OS", fonteNormal(8), Element.ALIGN_RIGHT));
        cabecalho.addCell(emissaoCell);
        document.add(cabecalho);

        Paragraph titulo = new Paragraph("NOTA FISCAL / COMPROVANTE DA ORDEM DE SERVIÇO Nº " + texto(ordem.getNumeroOs()), fonteTitulo(12, Color.WHITE));
        titulo.setAlignment(Element.ALIGN_CENTER);
        PdfPTable faixa = new PdfPTable(1);
        faixa.setWidthPercentage(100);
        PdfPCell faixaCell = celula(titulo, AZUL_OFICINA, Element.ALIGN_CENTER);
        faixaCell.setPadding(7);
        faixa.addCell(faixaCell);
        faixa.setSpacingBefore(6);
        faixa.setSpacingAfter(6);
        document.add(faixa);
    }

    private void adicionarDadosCliente(Document document, OrdemServicoModel ordem) throws DocumentException {
        PdfPTable tabela = new PdfPTable(new float[]{1.2f, 4.2f, 1.2f, 2.4f});
        tabela.setWidthPercentage(100);
        adicionarTituloSecao(tabela, "Cliente");
        String nome = ordem.getCliente() != null && ordem.getCliente().getPessoa() != null
                ? ordem.getCliente().getPessoa().getNome()
                : "";
        String documento = documentoCliente(ordem);
        String telefone = ordem.getCliente() != null && ordem.getCliente().getPessoa() != null
                ? ordem.getCliente().getPessoa().getTelefone()
                : "";
        String email = ordem.getCliente() != null && ordem.getCliente().getPessoa() != null
                ? ordem.getCliente().getPessoa().getEmail()
                : "";
        String endereco = ordem.getCliente() != null && ordem.getCliente().getPessoa() != null
                ? ordem.getCliente().getPessoa().getEndereco()
                : "";

        tabela.addCell(rotulo("Cliente"));
        tabela.addCell(valor(nome));
        tabela.addCell(rotulo(documento.startsWith("CNPJ") ? "CNPJ" : "CPF"));
        tabela.addCell(valor(documento.replace("CPF: ", "").replace("CNPJ: ", "")));
        tabela.addCell(rotulo("Endereço"));
        PdfPCell enderecoCell = valor(endereco);
        enderecoCell.setColspan(3);
        tabela.addCell(enderecoCell);
        tabela.addCell(rotulo("Fones"));
        tabela.addCell(valor(telefone));
        tabela.addCell(rotulo("E-mail"));
        tabela.addCell(valor(email));
        tabela.setSpacingAfter(4);
        document.add(tabela);
    }

    private void adicionarDadosVeiculo(Document document, OrdemServicoModel ordem, HistoricoStatusOrdemModel statusAtual) throws DocumentException {
        PdfPTable tabela = new PdfPTable(new float[]{1.2f, 2.5f, 1.2f, 2.2f, 1.1f, 1.7f});
        tabela.setWidthPercentage(100);
        adicionarTituloSecao(tabela, "Veículo");

        String marca = "";
        String modelo = "";
        if (ordem.getVeiculo() != null && ordem.getVeiculo().getModelo() != null) {
            modelo = ordem.getVeiculo().getModelo().getNomeModelo();
            if (ordem.getVeiculo().getModelo().getMarca() != null) {
                marca = ordem.getVeiculo().getModelo().getMarca().getNomeMarca();
            }
        }

        tabela.addCell(rotulo("Veículo/Ano"));
        tabela.addCell(valor((modelo + "/" + valorInteiro(ordem.getVeiculo() == null ? null : ordem.getVeiculo().getAnoModelo())).trim()));
        tabela.addCell(rotulo("Entrada"));
        tabela.addCell(valor(formatarData(ordem.getDataAbertura())));
        tabela.addCell(rotulo("Status"));
        tabela.addCell(valor(statusAtual == null ? "" : statusAtual.getStatusOrdemServico().getNomeStatus()));
        tabela.addCell(rotulo("Marca"));
        tabela.addCell(valor(marca));
        tabela.addCell(rotulo("Cor"));
        tabela.addCell(valor(ordem.getVeiculo() == null ? "" : ordem.getVeiculo().getCor()));
        tabela.addCell(rotulo("Placa"));
        tabela.addCell(valor(ordem.getVeiculo() == null ? "" : ordem.getVeiculo().getPlaca()));
        tabela.addCell(rotulo("Chassi"));
        tabela.addCell(valor(ordem.getVeiculo() == null ? "" : ordem.getVeiculo().getChassi()));
        tabela.addCell(rotulo("KM"));
        PdfPCell km = valor(valorInteiro(ordem.getVeiculo() == null ? null : ordem.getVeiculo().getQuilometragemAtual()));
        km.setColspan(3);
        tabela.addCell(km);
        tabela.setSpacingAfter(4);
        document.add(tabela);
    }

    private void adicionarSecaoItens(Document document, List<ItemPecaModel> pecas, List<ItemServicoModel> servicos) throws DocumentException {
        PdfPTable titulo = new PdfPTable(1);
        titulo.setWidthPercentage(100);
        adicionarTituloSecao(titulo, "Valores da Nota Fiscal");
        titulo.setSpacingAfter(0);
        document.add(titulo);

        PdfPTable tabelaPecas = new PdfPTable(new float[]{5.2f, 1f, 1.4f, 1.4f});
        tabelaPecas.setWidthPercentage(100);
        tabelaPecas.addCell(cabecalhoTabela("Peças/Produtos"));
        tabelaPecas.addCell(cabecalhoTabela("Qtd"));
        tabelaPecas.addCell(cabecalhoTabela("Vl Un"));
        tabelaPecas.addCell(cabecalhoTabela("Total"));
        if (pecas.isEmpty()) {
            PdfPCell vazio = valor("Nenhuma peça/produto vinculada à Ordem de Serviço.");
            vazio.setColspan(4);
            tabelaPecas.addCell(vazio);
        } else {
            for (ItemPecaModel item : pecas) {
                tabelaPecas.addCell(valor(descricaoPeca(item)));
                tabelaPecas.addCell(valorCentralizado(numero(item.getQuantidade())));
                tabelaPecas.addCell(valorDireita(moeda(item.getValorUnitario())));
                tabelaPecas.addCell(valorDireita(moeda(item.getValorTotal())));
            }
        }
        document.add(tabelaPecas);

        PdfPTable tabelaServicos = new PdfPTable(new float[]{3.5f, 2f, 1.2f, 1.2f, 1f, 1.3f});
        tabelaServicos.setWidthPercentage(100);
        tabelaServicos.addCell(cabecalhoTabela("Serviços executados"));
        tabelaServicos.addCell(cabecalhoTabela("Responsável"));
        tabelaServicos.addCell(cabecalhoTabela("Início"));
        tabelaServicos.addCell(cabecalhoTabela("Fim"));
        tabelaServicos.addCell(cabecalhoTabela("Qtd"));
        tabelaServicos.addCell(cabecalhoTabela("Total"));
        if (servicos.isEmpty()) {
            PdfPCell vazio = valor("Nenhum serviço vinculado à Ordem de Serviço.");
            vazio.setColspan(6);
            tabelaServicos.addCell(vazio);
        } else {
            for (ItemServicoModel item : servicos) {
                tabelaServicos.addCell(valor(descricaoServico(item)));
                tabelaServicos.addCell(valor(responsavel(item)));
                tabelaServicos.addCell(valorCentralizado(hora(item.getDataInicio())));
                tabelaServicos.addCell(valorCentralizado(hora(item.getDataFim())));
                tabelaServicos.addCell(valorCentralizado(numero(item.getQuantidade())));
                tabelaServicos.addCell(valorDireita(moeda(item.getValorTotal())));
            }
        }
        tabelaServicos.setSpacingAfter(4);
        document.add(tabelaServicos);
    }

    private void adicionarTotais(Document document,
                                 List<ItemServicoModel> servicos,
                                 List<ItemPecaModel> pecas,
                                 List<PagamentoModel> pagamentos,
                                 OrdemServicoModel ordem) throws DocumentException {
        BigDecimal totalServicos = somarServicos(servicos);
        BigDecimal totalPecas = somarPecas(pecas);
        BigDecimal totalPago = pagamentos.stream()
                .filter(p -> StatusPagamento.PAGO.equals(p.getStatusPagamento()))
                .map(PagamentoModel::getValorPago)
                .filter(v -> v != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal total = totalServicos.add(totalPecas);
        BigDecimal pendente = total.subtract(totalPago);
        if (pendente.compareTo(BigDecimal.ZERO) < 0) {
            pendente = BigDecimal.ZERO;
        }

        PdfPTable tabela = new PdfPTable(new float[]{5f, 2f});
        tabela.setWidthPercentage(46);
        tabela.setHorizontalAlignment(Element.ALIGN_RIGHT);
        PdfPCell titulo = cabecalhoTabela("Totais");
        titulo.setColspan(2);
        tabela.addCell(titulo);
        tabela.addCell(valor("Mão de obra/Serviços"));
        tabela.addCell(valorDireita(moeda(totalServicos)));
        tabela.addCell(valor("Peças/Produtos"));
        tabela.addCell(valorDireita(moeda(totalPecas)));
        tabela.addCell(valor("Deslocamento"));
        tabela.addCell(valorDireita(moeda(BigDecimal.ZERO)));
        tabela.addCell(valor("Serviço Guincho"));
        tabela.addCell(valorDireita(moeda(BigDecimal.ZERO)));
        tabela.addCell(valor("Outros"));
        tabela.addCell(valorDireita(moeda(BigDecimal.ZERO)));
        tabela.addCell(valorNegrito("TOTAL"));
        tabela.addCell(valorDireitaNegrito(moeda(total)));
        tabela.addCell(valor("Valor pago"));
        tabela.addCell(valorDireita(moeda(totalPago)));
        tabela.addCell(valor("Saldo pendente"));
        tabela.addCell(valorDireita(moeda(pendente)));
        tabela.setSpacingAfter(8);
        document.add(tabela);

        if (!pagamentos.isEmpty()) {
            PdfPTable tabelaPagamentos = new PdfPTable(new float[]{2f, 2f, 2f, 2f});
            tabelaPagamentos.setWidthPercentage(100);
            adicionarTituloSecao(tabelaPagamentos, "Pagamentos registrados");
            tabelaPagamentos.addCell(cabecalhoTabela("Forma"));
            tabelaPagamentos.addCell(cabecalhoTabela("Data"));
            tabelaPagamentos.addCell(cabecalhoTabela("Status"));
            tabelaPagamentos.addCell(cabecalhoTabela("Valor"));
            for (PagamentoModel pagamento : pagamentos) {
                tabelaPagamentos.addCell(valor(pagamento.getFormaPagamento() == null ? "" : pagamento.getFormaPagamento().name()));
                tabelaPagamentos.addCell(valor(formatarDataHora(pagamento.getDataPagamento())));
                tabelaPagamentos.addCell(valor(pagamento.getStatusPagamento() == null ? "" : pagamento.getStatusPagamento().name()));
                tabelaPagamentos.addCell(valorDireita(moeda(pagamento.getValorPago())));
            }
            tabelaPagamentos.setSpacingAfter(6);
            document.add(tabelaPagamentos);
        }
    }

    private void adicionarObservacoes(Document document, OrdemServicoModel ordem) throws DocumentException {
        PdfPTable tabela = new PdfPTable(1);
        tabela.setWidthPercentage(100);
        adicionarTituloSecao(tabela, "Observações");
        String observacao = texto(ordem.getObservacao());
        if (observacao.isBlank()) {
            observacao = "Documento gerado pelo sistema local da AV CAR AUTO CENTER com base na Ordem de Serviço cadastrada.";
        }
        tabela.addCell(valor(observacao));
        tabela.addCell(valor("Garantias de peças e serviços devem respeitar os prazos cadastrados no sistema. A garantia das peças inicia após a finalização da Ordem de Serviço."));
        tabela.addCell(valor("Documento fiscal interno/simplificado. Para fins legais específicos, validar a necessidade de emissão fiscal eletrônica municipal ou estadual."));
        tabela.setSpacingAfter(16);
        document.add(tabela);
    }

    private void adicionarAssinaturas(Document document, OrdemServicoModel ordem) throws DocumentException {
        PdfPTable tabela = new PdfPTable(new float[]{1f, 1f});
        tabela.setWidthPercentage(100);
        tabela.setSpacingBefore(18);
        tabela.addCell(assinatura(texto(ordem.getCliente() != null && ordem.getCliente().getPessoa() != null ? ordem.getCliente().getPessoa().getNome() : "CLIENTE")));
        tabela.addCell(assinatura(EMPRESA_NOME));
        document.add(tabela);
    }

    private Optional<HistoricoStatusOrdemModel> buscarStatusAtual(Long idOrdemServico) {
        List<HistoricoStatusOrdemModel> historico = historicoStatusRepository.findHistoricoFluxoDesc(idOrdemServico);
        return historico.isEmpty() ? Optional.empty() : Optional.of(historico.get(0));
    }

    private void recalcularEmMemoria(OrdemServicoModel ordem, List<ItemServicoModel> servicos, List<ItemPecaModel> pecas) {
        ordem.setValorTotal(somarServicos(servicos).add(somarPecas(pecas)));
    }

    private BigDecimal somarServicos(List<ItemServicoModel> servicos) {
        return servicos.stream()
                .map(ItemServicoModel::getValorTotal)
                .filter(v -> v != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal somarPecas(List<ItemPecaModel> pecas) {
        return pecas.stream()
                .map(ItemPecaModel::getValorTotal)
                .filter(v -> v != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String documentoCliente(OrdemServicoModel ordem) {
        if (ordem.getCliente() == null || ordem.getCliente().getId() == null) {
            return "";
        }
        Optional<PessoaFisicaModel> pf = pessoaFisicaRepository.findByIdAndAtivoTrue(ordem.getCliente().getId());
        if (pf.isPresent()) {
            return "CPF: " + texto(pf.get().getCpf());
        }
        Optional<PessoaJuridicaModel> pj = pessoaJuridicaRepository.findByIdAndAtivoTrue(ordem.getCliente().getId());
        return pj.map(pessoaJuridicaModel -> "CNPJ: " + texto(pessoaJuridicaModel.getCnpj())).orElse("");
    }

    private Optional<Image> carregarLogo() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/assets/branding/av-car-logo-horizontal-480.png");
        if (!resource.exists()) {
            return Optional.empty();
        }
        try (InputStream inputStream = resource.getInputStream()) {
            return Optional.of(Image.getInstance(inputStream.readAllBytes()));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    private void adicionarTituloSecao(PdfPTable tabela, String titulo) {
        PdfPCell cell = cabecalhoTabela(titulo);
        cell.setColspan(tabela.getNumberOfColumns());
        tabela.addCell(cell);
    }

    private PdfPCell cabecalhoTabela(String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto(texto), fonteNegrito(8)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(CINZA_CABECALHO);
        cell.setBorderColor(BORDA);
        cell.setPadding(4);
        return cell;
    }

    private PdfPCell rotulo(String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto(texto), fonteNegrito(8)));
        cell.setBackgroundColor(CINZA_CLARO);
        cell.setBorderColor(BORDA);
        cell.setPadding(3);
        return cell;
    }

    private PdfPCell valor(String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto(texto), fonteNormal(8)));
        cell.setBorderColor(BORDA);
        cell.setPadding(3);
        return cell;
    }

    private PdfPCell valorNegrito(String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto(texto), fonteNegrito(8)));
        cell.setBorderColor(BORDA);
        cell.setPadding(3);
        return cell;
    }

    private PdfPCell valorCentralizado(String texto) {
        PdfPCell cell = valor(texto);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private PdfPCell valorDireita(String texto) {
        PdfPCell cell = valor(texto);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }

    private PdfPCell valorDireitaNegrito(String texto) {
        PdfPCell cell = valorNegrito(texto);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }

    private PdfPCell assinatura(String texto) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingTop(16);
        Paragraph linha = new Paragraph("________________________________________", fonteNormal(9));
        linha.setAlignment(Element.ALIGN_CENTER);
        Paragraph nome = new Paragraph(texto(texto), fonteNegrito(8));
        nome.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(linha);
        cell.addElement(nome);
        return cell;
    }

    private PdfPCell semBorda() {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2);
        return cell;
    }

    private PdfPCell celula(Paragraph paragraph, Color background, int alinhamento) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(background);
        cell.setHorizontalAlignment(alinhamento);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderColor(background);
        cell.addElement(paragraph);
        return cell;
    }

    private Paragraph paragrafo(String texto, Font fonte, int alinhamento) {
        Paragraph paragraph = new Paragraph(texto(texto), fonte);
        paragraph.setAlignment(alinhamento);
        paragraph.setLeading(10f);
        return paragraph;
    }

    private String descricaoPeca(ItemPecaModel item) {
        if (item == null || item.getPeca() == null) {
            return "";
        }
        String codigo = texto(item.getPeca().getCodigoNacional());
        String nome = texto(item.getPeca().getNomePeca());
        String marca = texto(item.getPeca().getMarcaPeca());
        String fornecedor = item.getFornecedor() == null ? "" : texto(item.getFornecedor().getNomeFornecedor());
        StringBuilder builder = new StringBuilder();
        if (!codigo.isBlank()) {
            builder.append(codigo).append(" - ");
        }
        builder.append(nome);
        if (!marca.isBlank()) {
            builder.append(" - ").append(marca);
        }
        if (!fornecedor.isBlank()) {
            builder.append(" | Forn.: ").append(fornecedor);
        }
        return builder.toString();
    }

    private String descricaoServico(ItemServicoModel item) {
        if (item == null || item.getServico() == null) {
            return "";
        }
        String descricao = texto(item.getDescricaoExecucao());
        return descricao.isBlank() ? texto(item.getServico().getNomeServico()) : descricao;
    }

    private String responsavel(ItemServicoModel item) {
        if (item == null || item.getColaborador() == null || item.getColaborador().getPessoa() == null) {
            return "";
        }
        return texto(item.getColaborador().getPessoa().getNome());
    }

    private String formatarDataHora(LocalDateTime data) {
        return data == null ? "" : data.format(DATA_HORA);
    }

    private String formatarData(LocalDateTime data) {
        return data == null ? "" : data.format(DATA);
    }

    private String hora(LocalDateTime data) {
        return data == null ? "" : data.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private String valorInteiro(Integer valor) {
        return valor == null ? "" : valor.toString();
    }

    private String numero(BigDecimal valor) {
        return valor == null ? "" : valor.stripTrailingZeros().toPlainString().replace('.', ',');
    }

    private String moeda(BigDecimal valor) {
        return NumberFormat.getCurrencyInstance(LOCALE_BR).format(valor == null ? BigDecimal.ZERO : valor);
    }

    private String texto(String value) {
        return value == null ? "" : value.trim();
    }

    private Font fonteNormal(int tamanho) {
        return FontFactory.getFont(FontFactory.HELVETICA, tamanho, Font.NORMAL, Color.BLACK);
    }

    private Font fonteNegrito(int tamanho) {
        return FontFactory.getFont(FontFactory.HELVETICA, tamanho, Font.BOLD, Color.BLACK);
    }

    private Font fonteTitulo(int tamanho, Color cor) {
        return FontFactory.getFont(FontFactory.HELVETICA, tamanho, Font.BOLD, cor);
    }
}
