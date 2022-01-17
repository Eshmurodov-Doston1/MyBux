package com.example.domain.language;
public enum Language {
    UZ("uz"),
    RU("ru"),
    EN("en") ;

    private String value;


    Language(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


}

