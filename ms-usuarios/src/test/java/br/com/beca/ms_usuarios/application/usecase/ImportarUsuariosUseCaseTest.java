package br.com.beca.ms_usuarios.application.usecase;

import br.com.beca.ms_usuarios.application.usecases.ImportarUsuariosUseCase;
import br.com.beca.ms_usuarios.infra.persistence.UsuarioRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImportarUsuariosUseCaseTest {

    @InjectMocks
    private ImportarUsuariosUseCase useCase;

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void deveImportarUsuariosComSucesso() throws Exception {
        MultipartFile file = criarExcelValido();

        when(passwordEncoder.encode(any())).thenReturn("senhaCriptografada");

        var response = useCase.executar(file);

        assertEquals(2, response.importados());
        assertEquals(0, response.erros());
        verify(repository, times(2)).save(any());
    }

    @Test
    void deveContarErroQuandoLinhaInvalida() throws Exception {
        MultipartFile file = criarExcelComLinhaInvalida();

        when(passwordEncoder.encode(any())).thenReturn("senha");

        var response = useCase.executar(file);

        assertEquals(1, response.importados());
        assertEquals(1, response.erros());
    }

    @Test
    void deveFalharQuandoArquivoNaoEhExcel() {
        MultipartFile file = new MockMultipartFile(
                "file",
                "usuarios.txt",
                "text/plain",
                "qualquer".getBytes()
        );

        assertThrows(IllegalArgumentException.class,
                () -> useCase.executar(file));
    }

    // -------- helpers --------

    private MultipartFile criarExcelValido() throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        sheet.createRow(0);

        Row r1 = sheet.createRow(1);
        r1.createCell(0).setCellValue("Ana");
        r1.createCell(1).setCellValue("ana@email.com");
        r1.createCell(2).setCellValue("12345678901");
        r1.createCell(3).setCellValue("123");

        Row r2 = sheet.createRow(2);
        r2.createCell(0).setCellValue("João");
        r2.createCell(1).setCellValue("joao@email.com");
        r2.createCell(2).setCellValue("98765432100");
        r2.createCell(3).setCellValue("456");

        return toMultipart(workbook);
    }

    private MultipartFile criarExcelComLinhaInvalida() throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        sheet.createRow(0);

        Row r1 = sheet.createRow(1);
        r1.createCell(0).setCellValue("Ana");
        r1.createCell(1).setCellValue("ana@email.com");
        r1.createCell(2).setCellValue("12345678901");
        r1.createCell(3).setCellValue("123");

        Row r2 = sheet.createRow(2);
        r2.createCell(0).setCellValue(""); // inválido

        return toMultipart(workbook);
    }

    private MultipartFile toMultipart(Workbook workbook) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return new MockMultipartFile(
                "file",
                "usuarios.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                out.toByteArray()
        );
    }
}
