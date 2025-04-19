package de.t0bx.permifyvelocity.language;

import lombok.Getter;

@Getter
public enum Language {
    ENGLISH("en"),
    GERMAN("de");

    private final String code;

    Language(final String code) {
        this.code = code;
    }

    public static Language fromCode(final String code) {
        for (final Language language : values()) {
            if (language.code.equals(code)) {
                return language;
            }
        }
        return null;
    }
}
