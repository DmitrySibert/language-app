package com.dsib.language.core.word.presentation;

import com.dsib.language.core.word.domain.Word;
import com.dsib.language.core.word.application.WordService;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.dsib.language.core.word.presentation.Constants.X_USER_ID_HEADER;

/**
 * Words rest controller.
 * <p>
 * Provide endpoint to get words data
 */
@RestController
@RequestMapping(value = "/api/v1/word")
@SwaggerDefinition(
  info = @Info(
    title = "Word",
    description = "Words management endpoint",
    version = "1.0.0"
  )
)
public class WordController {

  private final WordService wordService;

  public WordController(WordService wordService) {
    this.wordService = wordService;
  }

  @GetMapping
  public ResponseEntity<List<Word>> getWords(
    @RequestHeader(value = X_USER_ID_HEADER) String userId,
    @RequestParam("origins") List<String> origins
  ) {
    return ResponseEntity.status(HttpStatus.OK).body(wordService.getByOrigin(origins, userId));
  }

  @GetMapping(value = "/tags")
  public List<String> getAllTags(@RequestHeader(value = X_USER_ID_HEADER) String userId) {
    return wordService.getAllTags(userId);
  }
}
