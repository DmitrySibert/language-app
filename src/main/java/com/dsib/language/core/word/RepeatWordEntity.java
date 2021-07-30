package com.dsib.language.core.word;

import org.springframework.data.annotation.Id;

public class RepeatWordEntity {

    @Id
    private String origin;

    public RepeatWordEntity(String origin) {
        this.origin = origin;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
