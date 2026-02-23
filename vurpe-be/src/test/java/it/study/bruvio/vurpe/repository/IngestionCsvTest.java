package it.study.bruvio.vurpe.repository;

import it.study.bruvio.vurpe.dto.response.PayloadResponse;
import it.study.bruvio.vurpe.dto.response.Status;
import it.study.bruvio.vurpe.service.IngestionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class IngestionCsvTest {

    @Mock
    private FilesRepository repoFiles;
    @Mock
    private DataRecordRepository repoData;

    @InjectMocks
    private IngestionService ingestionService;

    @Test
    @DisplayName("Test Valid CSV")
    public void testShouldValidCsv() throws Exception {
        String csvContent = """
                            id;amount;category;date;description
                            ricevuta_amazon_7842;9039;Elettronica;2025-11-05 14:30:00;Acquisto cuffie wireless Sony WH-1000XM5
                            abbonamento_netflix_11;1339;Intrattenimento;2025-11-10 00:01:15;Rinnovo mensile abbonamento Netflix Premium
                            """;

        MockMultipartFile mockfile = createCsvFile(csvContent);

        PayloadResponse<String> response = ingestionService.uploadFile(mockfile);
        assertThat(response.status()).isEqualTo(Status.OK);
        assertThat(response.message()).isEqualTo("success ok");
    }



    private MockMultipartFile createCsvFile(String content){

        return new MockMultipartFile("file",
                "utenti-da-validare.csv",
                "text/csv",
                content.getBytes(StandardCharsets.UTF_8));
    }
}
