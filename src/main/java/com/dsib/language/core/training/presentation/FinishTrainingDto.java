package com.dsib.language.core.training.presentation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinishTrainingDto {
    private String id;
    private List<String> wordsForRepetition;
    private List<String> approvedWords;
}
