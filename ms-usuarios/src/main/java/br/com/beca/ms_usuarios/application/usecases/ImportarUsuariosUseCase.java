package br.com.beca.ms_usuarios.application.usecases;

import br.com.beca.ms_usuarios.infra.persistence.entities.UsuarioEntity;
import br.com.beca.ms_usuarios.infra.persistence.UsuarioRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImportarUsuariosUseCase {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void executar(MultipartFile arquivo) {
        try (InputStream inputStream = arquivo.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            List<UsuarioEntity> novosUsuarios = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String nome = getCellValue(row.getCell(0));
                String email = getCellValue(row.getCell(1));
                String cpf = getCellValue(row.getCell(2));
                String senhaPlana = getCellValue(row.getCell(3));

                String senhaHash = passwordEncoder.encode(senhaPlana);

                UsuarioEntity usuarioEntity = new UsuarioEntity(null, nome, email, senhaHash, cpf);

                novosUsuarios.add(usuarioEntity);
            }

            repository.saveAll(novosUsuarios);
            System.out.println("Sucesso! " + novosUsuarios.size() + " usuários importados.");

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar arquivo Excel: " + e.getMessage());
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            default -> "";
        };
    }
}