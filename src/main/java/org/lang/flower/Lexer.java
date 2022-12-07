package org.lang.flower;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Lexer {

    String code;
    int cursor;
    char current;

    Lexer(String code){
        this.code = code;
        cursor = 0;
        current = this.code.charAt(cursor);
    }

    void advance(){
        cursor++;
        if(cursor < code.length()) {
            current = code.charAt(cursor);
        }
    }

    void checkSpace(){
        while(code.charAt(cursor) == ' ')
            advance();
    }

    Token getNextToken(){
        while(cursor < code.length()){
            if(current == ' ')
                checkSpace();
            if(Pattern.matches("^[A-Za-z0-9]$", ""+current)){
                return buildToken(c -> Pattern.matches("^[A-Za-z0-9]$", ""+c), TokenType.ID);
            }
            if(current == '"'){
                advance();
                Token string = buildToken(c -> c != '"', TokenType.STRING_LITERAL);
                advance();
                return string;
            }
            switch(current){
                case '=':{
                    advance();
                    return new Token(TokenType.EQU, "=");
                }
                case '(':{
                    advance();
                    return new Token(TokenType.LEFTPAR, "(");
                }
                case ')':{
                    advance();
                    return new Token(TokenType.RIGHTPAR, ")");
                }
            }
        }
        return null;
    }

    private Token buildToken(Predicate<Character> p, TokenType type) {
        Token token = new Token();
        token.type = type;
        while(p.test(current) && cursor < code.length()){
            token.value += current;
            advance();
        }
        return token;
    }

}
