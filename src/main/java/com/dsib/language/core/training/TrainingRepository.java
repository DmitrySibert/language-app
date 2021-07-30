package com.dsib.language.core.training;

import java.util.Optional;

public interface TrainingRepository {
    Optional<Training> find(String id);
    Training create(Training training);
    Training update(Training training);
}
