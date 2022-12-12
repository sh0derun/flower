package org.lang.flower;

import java.util.Optional;

public enum TokenType{
    ID("id"),
    LET("let"),
    PRINT("print"),
    LEFTPAR("("),
    RIGHTPAR(")"),
    EQU("="),
    PLUS("+"),
    MINUS("-"),
    STRING_LITERAL("STRING_LITERAL"),
    NUMBER("");

    private String value;

    TokenType(String value){
        this.value = value;
    }

    public static Optional<TokenType> fromValue(String val){
        for (TokenType tokenType : TokenType.values()) {
            if(tokenType.value.equals(val))
                return Optional.of(tokenType);
        }
        return Optional.empty();
    }

    public String getValue(){
        return value;
    }

}
