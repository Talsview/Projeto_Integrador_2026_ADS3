package br.com.avcar.oficina.business.notafiscal.controller;

import br.com.avcar.oficina.business.notafiscal.service.NotaFiscalPdfService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsável pela geração de documentos fiscais/recibos em PDF.
 */
@RestController
@RequestMapping("/api/notas-fiscais")
public class NotaFiscalController {

    private final NotaFiscalPdfService notaFiscalPdfService;

    public NotaFiscalController(NotaFiscalPdfService notaFiscalPdfService) {
        this.notaFiscalPdfService = notaFiscalPdfService;
    }

    @GetMapping(value = "/ordens-servico/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> gerarNotaFiscalOrdemServico(@PathVariable Long id) {
        byte[] pdf = notaFiscalPdfService.gerarNotaFiscalOrdemServico(id);
        String nomeArquivo = notaFiscalPdfService.montarNomeArquivo(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(nomeArquivo).build().toString())
                .body(pdf);
    }
}
