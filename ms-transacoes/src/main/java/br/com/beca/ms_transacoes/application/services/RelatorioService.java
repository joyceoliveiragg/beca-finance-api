package br.com.beca.ms_transacoes.application.services;

import br.com.beca.ms_transacoes.domain.entities.Transacao;
import br.com.beca.ms_transacoes.infra.persistence.TransacaoRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class RelatorioService {

    @Autowired
    private TransacaoRepository repository;

    public byte[] gerarExtratoPdf(Long usuarioId) {
        List<Transacao> transacoes = repository.findByUsuarioId(usuarioId);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph titulo = new Paragraph("Extrato de Transações", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);

            adicionarCabecalho(table, "Data");
            adicionarCabecalho(table, "Valor");
            adicionarCabecalho(table, "Moeda");

            for (Transacao t : transacoes) {
                table.addCell(t.getDataCriacao().toString());
                table.addCell("R$ " + t.getValor().toString());
                table.addCell(t.getMoeda());
            }

            document.add(table);
            document.close();

            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }

    private void adicionarCabecalho(PdfPTable table, String texto) {
        PdfPCell cell = new PdfPCell();
        cell.setPhrase(new Phrase(texto, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(java.awt.Color.LIGHT_GRAY);
        table.addCell(cell);
    }
}