package it.study.bruvio.vurpe.service;

import it.study.bruvio.vurpe.dto.response.PayloadResponse;
import it.study.bruvio.vurpe.entity.DataRecord;
import it.study.bruvio.vurpe.entity.FileStatusEnum;
import it.study.bruvio.vurpe.entity.Files;
import it.study.bruvio.vurpe.repository.DataRecordRepository;
import it.study.bruvio.vurpe.repository.FilesRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class IngestionService {
    private final FilesRepository repoFiles;
    private final DataRecordRepository repoData;

    @Transactional(rollbackFor = Exception.class)
    public PayloadResponse<String> uploadFile(MultipartFile file) throws Exception {

        if (file.isEmpty()) {
            return PayloadResponse.error("files is empty", " ");
        }
        String nameFile = file.getOriginalFilename();
        if (!Objects.equals(FilenameUtils.getExtension(nameFile), "csv")) {
            return PayloadResponse.error("extension error", " ");
        }
        if (!validateCsvHeader(file)) {
            return PayloadResponse.error("header error", " ");
        }
        UUID fileId = insertRows(file).getId();
        if (!repoFiles.existsById(fileId)) {
            return PayloadResponse.error("row error", " ");
        }

        return PayloadResponse.success(fileId.toString(), "success ok");
    }

    private boolean validateCsvHeader(MultipartFile file) throws IOException {
        List<String> expectedColumn = new ArrayList<>(List.of("id", "amount", "category", "date", "description"));

        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            String header = reader.readLine();
            if (header == null) {
                throw new IllegalArgumentException("Header not found");
            }

            String[] headerColumns = header.split(";");
            Set<String> setHeaderColumns = new HashSet<>(List.of(headerColumns));

            if (setHeaderColumns.size() != expectedColumn.size()) {
                throw new IllegalArgumentException("Number of columns is different");

            }

            for (String el : setHeaderColumns) {
                if (!expectedColumn.contains(el)) {
                    throw new IllegalArgumentException("missing column: " + el);
                }

            }
        }
        return true;
    }

    private Files insertRows(MultipartFile file) throws Exception {
        Files f = new Files();
        int count = 1;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            f.setOriginalName(file.getOriginalFilename());
            f.setFileSize(file.getSize());
            f.setStatus(FileStatusEnum.UPLOADING);
            repoFiles.saveAndFlush(f);

            String row;
            boolean isHeader = true;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            List<DataRecord> dtList = new ArrayList<>();
            while ((row = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                if (dataSplitter(row).length == 0) {
                    continue;
                }
                try {
                    DataRecord dt = getDataRecord(row, f, formatter);
                    dtList.add(dt);
                } catch (Exception e) {
                    throw new Exception("errore riga: " + count + " colonna:", e);
                }
                count++;
            }

            repoData.saveAll(dtList);

        } catch (Exception e) {
            throw new Exception("errore riga: " + count, e);
        }
        f.setStatus(FileStatusEnum.UPLOADED);
        return repoFiles.save(f);
    }

    private @NotNull DataRecord getDataRecord(String row, Files f, DateTimeFormatter formatter) throws Exception {
        String[] data = dataSplitter(row);
        if (data.length < 5) {
            throw new Exception("Numero di colonne insufficiente");
        }
        return new DataRecord(
                f.getId(), // file_id
                data[0], // original_id
                new BigDecimal(data[1].trim()), // amount
                data[2], // category
                LocalDateTime.parse(data[3].trim(), formatter), // date
                data[4]// description
        );
    }

    private String[] dataSplitter(String row) {
        return row.split(";");
    }
}
