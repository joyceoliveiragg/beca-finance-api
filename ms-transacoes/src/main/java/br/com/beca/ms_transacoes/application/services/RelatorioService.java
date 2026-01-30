package br.com.beca.ms_transacoes.application.services;

import br.com.beca.ms_transacoes.infra.persistence.TransacaoRepository;
import br.com.beca.ms_transacoes.infra.persistence.entities.TransacaoEntity;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class RelatorioService {

    @Autowired
    private TransacaoRepository repository;

    public byte[] gerarRelatorioExcel(Long usuarioId, String periodo) {

        LocalDate hoje = LocalDate.now();
        LocalDateTime inicio;
        LocalDateTime fim;

        if ("diario".equalsIgnoreCase(periodo)) {
            inicio = hoje.atStartOfDay();
            fim = hoje.atTime(23, 59, 59);
        } else if ("mensal".equalsIgnoreCase(periodo)) {
            inicio = hoje.withDayOfMonth(1).atStartOfDay();
            fim = hoje.withDayOfMonth(hoje.lengthOfMonth()).atTime(23, 59, 59);
        } else {
            throw new IllegalArgumentException("Período inválido. Use diario ou mensal.");
        }

        List<TransacaoEntity> transacoes =
                repository.buscarPorPeriodo(usuarioId, inicio, fim);

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Relatório");

            int rowIdx = 0;

            Row header = sheet.createRow(rowIdx++);
            header.createCell(0).setCellValue("Data");
            header.createCell(1).setCellValue("Tipo");
            header.createCell(2).setCellValue("Valor");
            header.createCell(3).setCellValue("Categoria");
            header.createCell(4).setCellValue("Status");

            BigDecimal totalPeriodo = BigDecimal.ZERO;

            for (TransacaoEntity t : transacoes) {

                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(t.getDataCriacao().toLocalDate().toString());
                row.createCell(1).setCellValue("SAIDA");
                row.createCell(2).setCellValue(t.getValor().doubleValue());
                row.createCell(3).setCellValue(t.getCategoria() != null ? t.getCategoria().toString() : "OUTROS");
                row.createCell(4).setCellValue(t.getStatus() != null ? t.getStatus().name() : "");

                totalPeriodo = totalPeriodo.add(t.getValor());
            }

            Row totalRow = sheet.createRow(rowIdx);
            totalRow.createCell(1).setCellValue("TOTAL DO PERÍODO");
            totalRow.createCell(2).setCellValue(totalPeriodo.doubleValue());

            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório Excel", e);
        }

    }
}
