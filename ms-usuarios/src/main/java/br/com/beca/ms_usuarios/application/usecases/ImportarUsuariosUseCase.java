package br.com.beca.ms_usuarios.application.usecases;

import br.com.beca.ms_usuarios.domain.entities.Usuario;
import br.com.beca.ms_usuarios.infra.persistence.UsuarioRepository;
import br.com.beca.ms_usuarios.infra.persistence.entities.UsuarioEntity;
import br.com.beca.ms_usuarios.application.dto.ResultadoImportacaoResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class ImportarUsuariosUseCase {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResultadoImportacaoResponse executar(MultipartFile arquivo) {

        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException("Arquivo não pode ser vazio");
        }

        if (!arquivo.getOriginalFilename().endsWith(".xlsx")) {
            throw new IllegalArgumentException("Arquivo inválido. Envie um arquivo .xlsx");
        }

        int sucesso = 0;
        int erro = 0;

        try (InputStream inputStream = arquivo.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                try {
                    Row row = sheet.getRow(i);
                    if (row == null) {
                        erro++;
                        continue;
                    }

                    String nome = getCellValue(row.getCell(0));
                    String email = getCellValue(row.getCell(1));
                    String cpf = getCellValue(row.getCell(2));
                    String senhaPlana = getCellValue(row.getCell(3));

                    if (nome.isBlank() || email.isBlank() || cpf.isBlank() || senhaPlana.isBlank()) {
                        throw new IllegalArgumentException("Dados obrigatórios ausentes");
                    }

                    Usuario usuario = new Usuario(
                            null,
                            nome,
                            email,
                            passwordEncoder.encode(senhaPlana),
                            cpf
                    );

                    repository.save(new UsuarioEntity(usuario));
                    sucesso++;

                } catch (Exception e) {
                    erro++;
                }
            }

            return new ResultadoImportacaoResponse(sucesso, erro);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar arquivo Excel", e);
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                cell.setCellType(CellType.STRING);
                yield cell.getStringCellValue().trim();
            }
            default -> "";
        };
    }

}
