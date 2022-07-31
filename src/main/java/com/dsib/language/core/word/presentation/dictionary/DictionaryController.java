package com.dsib.language.core.word.presentation.dictionary;

import com.dsib.language.core.word.domain.dictionary.DictionaryService;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Dictionary rest controller.
 *
 * Provide endpoint to upload dictionary files
 */
@RestController
@RequestMapping(value = "/api/v1/dictionary")
@SwaggerDefinition(
        info = @Info(
                title = "Dictionary",
                description = "Dictionary management endpoint",
                version = "1.0.0"
        )
)
public class DictionaryController {

        private final DictionaryService dictionaryService;

        public DictionaryController(DictionaryService dictionaryService) {
                this.dictionaryService = dictionaryService;
        }

        @PostMapping("/file/upload")
        public ResponseEntity<String> uploadFile(@RequestParam("dictionary") MultipartFile dictionary) {
                String message;
                try {
                        message = "Uploaded the file successfully: " + dictionary.getOriginalFilename();
                        dictionaryService.updateDictionary(dictionary.getInputStream());
                        return ResponseEntity.status(HttpStatus.OK).body(message);
                } catch (Exception e) {
                        message = "Could not upload the file: " + dictionary.getOriginalFilename() + "!";
                        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
                }
        }

        @PostMapping("/files/upload")
        public ResponseEntity<String> uploadFiles(@RequestParam("dictionaries") MultipartFile[] dictionaryFiles) {
                String message;
                try {
                        List<InputStream> dictionaries = new LinkedList<>();
                        List<String> fileNames = new LinkedList<>();
                        for (int i = 0; i < dictionaryFiles.length; ++i) {
                                dictionaries.add(dictionaryFiles[i].getInputStream());
                                fileNames.add(dictionaryFiles[i].getOriginalFilename());
                        }
                        dictionaryService.updateDictionary(dictionaries);
                        message = "Uploaded the files successfully: " + String.join(",", fileNames);
                        return ResponseEntity.status(HttpStatus.OK).body(message);
                } catch (IOException e) {
                        message = "Could not upload the files";
                        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
                }
        }

}
