package org.lang.flower;

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
                return buildTokenFromPattern(TokenType.NUMBER, "[0-9]+(\\.[0-9]+)?");
            }
            if (Character.isAlphabetic(current)) {
                return buildToken(c -> Pattern.matches("^[A-Za-z0-9]$", "" + c));
            }
            if (current == '"') {
                return buildTokenFromPattern(TokenType.STRING_LITERAL, "\"[A-Za-z0-9 ]*\"");
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
                case ',': {
                    return buildToken(TokenType.COLON, ",");
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

    private Token buildTokenFromPattern(TokenType type, String regex) {
        Token token = new Token();
        token.type = type;
        String res = code.substring(cursor);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(res);
        if (matcher.find() && cursor < code.length()) {
            String group = matcher.group();
            token.value += TokenType.STRING_LITERAL.equals(type) ? group.substring(1, group.length()-1) : group;
            advance(group.length());
        } else {
            assert !TokenType.STRING_LITERAL.equals(type) : "illegal string literal ending";
        }
        return token;
    }

    private Token buildToken(Predicate<Character> p) {
        StringBuilder builder = new StringBuilder();
        while (p.test(current) && cursor < code.length()) {
            builder.append(current);
            advance();
        }

        Token token = new Token(TokenType.ID, builder.toString());

        TokenType.fromValue(token.value).ifPresent(tp -> {
            switch (tp) {
                case ID:
                    break;
                case LET:
                    token.type = TokenType.LET;
                    break;
                case PRINT:
                    token.type = TokenType.PRINT;
                    break;
                case LEFTPAR:
                case RIGHTPAR:
                case EQU:
                case PLUS:
                case MINUS:
                case STRING_LITERAL:
                case NUMBER:
                    break;
                default:
                    System.out.println(tp);
            }
        });

        return token;
    }

}