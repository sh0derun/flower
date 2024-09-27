package org.lang.flower;

import java.util.Optional;

public enum TokenType{
    ID("id"), LET("let"), PRINT("print"), VOID("void"), INT("int"), BOOLEAN("bool"), STRING("str"), CHARACTER("char"), LEFTPAR("("),
    RIGHTPAR(")"), LEFT_CURLY_BRACE("{"), RIGHT_CURLY_BRACE("}"), ASSING("="), PLUS("+"), MINUS("-"), DIVIDE("/"), MULIPLY("*"),
    MODULOS("%"), STRING_LITERAL("STRING_LITERAL"), NUMBER("NUMBER"), COMMA(","),
    EQUALS("=="),NOT_EQUALS("!="),NOT("!"),
    GREATER(">"),GREATER_OR_EQUALS(">="),LESS("<"),LESS_OR_EQUALS("<="),
    EOF(""), NONE("NONE");

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
