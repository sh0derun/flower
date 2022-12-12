package org.lang.flower;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    String code;
    int cursor;
    char current;

    Lexer(String code) {
        this.code = code;
        cursor = 0;
        current = this.code.charAt(cursor);
    }

    void advance() {
        cursor++;
        if (cursor < code.length()) {
            current = code.charAt(cursor);
        }
    }

    void advance(int step) {
        cursor += step;
        if (cursor < code.length()) {
            current = code.charAt(cursor);
        }
    }

    void checkSpace() {
        while (code.charAt(cursor) == ' ')
            advance();
    }

    Token getNextToken() {
        while (cursor < code.length()) {
            if (current == ' ')
                checkSpace();
            if (Character.isDigit(current)) {
                return buildNumberToken();
            }
            if (Pattern.matches("^[A-Za-z0-9]$", "" + current)) {
                return buildToken(c -> Pattern.matches("^[A-Za-z0-9]$", "" + c), TokenType.ID);
            }
            if (current == '"') {
                advance();
                Token string = buildToken(c -> c != '"', TokenType.STRING_LITERAL);
                advance();
                return string;
            }
            switch (current) {
                case '+': {
                    return buildToken(TokenType.PLUS, "+");
                }
                case '-': {
                    return buildToken(TokenType.MINUS, "-");
                }
                case '=': {
                    return buildToken(TokenType.EQU, "=");
                }
                case '(': {
                    return buildToken(TokenType.LEFTPAR, "(");
                }
                case ')': {
                    return buildToken(TokenType.RIGHTPAR, ")");
                }
                default:
                    assert false : "unexpected token, found : " + current;
            }
        }
        return null;
    }

    private Token buildToken(TokenType type, String operator) {
        advance();
        return new Token(type, operator);
    }

    private Token buildNumberToken() {
        Token token = new Token();
        token.type = TokenType.NUMBER;
        String res = code.substring(cursor);
        Pattern pattern = Pattern.compile("[0-9]+(\\.[0-9]+)?");
        Matcher matcher = pattern.matcher(res);
        if (matcher.find() && cursor < code.length()) {
            String group = matcher.group();
            token.value += group;
            advance(group.length());
        }
        return token;
    }

    private Token buildToken(Predicate<Character> p, TokenType type) {
        StringBuilder builder = new StringBuilder();
        while (p.test(current) && cursor < code.length()) {
            builder.append(current);
            advance();
        }

        Token token = new Token(type, builder.toString());

        TokenType.fromValue(token.value).ifPresent(tp -> {
            switch (tp) {
                case LET:
                    token.type = TokenType.LET;
                    break;
                case PRINT:
                    token.type = TokenType.PRINT;
                    break;
                default:
                    System.out.println(tp);
            }
        });

        return token;
    }

}