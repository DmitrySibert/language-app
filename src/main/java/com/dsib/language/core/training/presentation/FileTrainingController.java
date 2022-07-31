package com.dsib.language.core.training.presentation;

import com.dsib.language.core.training.application.file.FileTraining;
import com.dsib.language.core.training.application.file.FileTrainingProvider;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * File training rest controller.
 */
@RestController
@RequestMapping(value = "/api/v1/training")
@SwaggerDefinition(
        info = @Info(
                title = "File training",
                description = "File training management endpoint",
                version = "1.0.0"
        )
)
public class FileTrainingController {

    private FileTrainingProvider fileTrainingProvider;

    public FileTrainingController(FileTrainingProvider fileTrainingProvider) {
        this.fileTrainingProvider = fileTrainingProvider;
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> getTraining(@RequestParam List<String> tags)
            throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {

        FileTraining training = fileTrainingProvider.getSimpleTrainingByTags(tags);
        InputStreamResource inputStreamResource = new InputStreamResource(training.getFile());

        return ResponseEntity.ok()
                .headers(createFileHeaders(training.getName() + "." + training.getFileExtension()))
                .contentLength(training.getFileSize())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(inputStreamResource);
    }

    private HttpHeaders createFileHeaders(String filename) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        httpHeaders.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        httpHeaders.add(HttpHeaders.PRAGMA, "no-cache");
        httpHeaders.add(HttpHeaders.EXPIRES, "0");

        return httpHeaders;
    }
}
