package org.lang.flower;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    String code;
    int cursor;
    char current;
    int line, column;
    Token peekedToken;

    Lexer(String code) {
        this.code = code;
        cursor = 0;
        line = 1;
        column = 1;
        if(this.code.isEmpty()){
            current = '\0';
        } else{
            current = this.code.charAt(cursor);
        }
    }

    void advance() {
        if (cursor < code.length()) {
            if(current == '\n'){
                line++;
                column = 1;
            } else{
                column++;
            }
            cursor++;
            current = cursor < code.length() ? code.charAt(cursor) : '\0';
        }
    }

    char peekNext(){
        if(cursor+1 < code.length()){
            return code.charAt(cursor+1);
        }
        return '\0';
    }

    Token peekNextToken(){
        if(peekedToken == null){
            int cursorTmp = cursor;
            int lineTmp = line;
            int columnTmp = column;
            char currentTmp = current;

            peekedToken = getNextToken();

            cursor = cursorTmp;
            line = lineTmp;
            column = columnTmp;
            current = currentTmp;
        }

        return peekedToken;
    }

    void advance(int step) {
        if (cursor < code.length()) {
            if(current == '\n'){
                line++;
                column = 1;
            } else{
                column+=step;
            }
            cursor+=step;
            current = cursor < code.length() ? code.charAt(cursor) : '\0';
        }
    }

    void checkSpace() {
        while (cursor < code.length() && (code.charAt(cursor) == ' ' || code.charAt(cursor) == '\n' || code.charAt(cursor) == '\t' || code.charAt(cursor) == '\r'))
            advance();
    }

    Token getNextToken() {
        checkSpace();

        if(peekedToken != null){
            Token token = peekedToken;
            peekedToken = null;
            advance(token.value.length());
            checkSpace();
            return token;
        }

        while (cursor < code.length()) {
            if (current == ' ' || current == '\n'|| code.charAt(cursor) == '\t' || code.charAt(cursor) == '\r')
                checkSpace();
            if (Character.isDigit(current)) {
                return buildTokenFromPattern(TokenType.NUMBER, "^[0-9]+(\\.[0-9]+)?");
            }
            if (Character.isAlphabetic(current)) {
                return buildToken(c -> Pattern.matches("^[A-Za-z0-9]$", "" + c));
            }
            if (current == '"') {
                return buildTokenFromPattern(TokenType.STRING_LITERAL, "\\\"[^\"\\\\\\\\]*\\\"");
            }
            switch (current) {
                case '+': {
                    return buildToken(TokenType.PLUS, "+");
                }
                case '-': {
                    return buildToken(TokenType.MINUS, "-");
                }
                case '/': {
                    return buildToken(TokenType.DIVIDE, "/");
                }
                case '*': {
                    return buildToken(TokenType.MULIPLY, "*");
                }
                case '%': {
                    return buildToken(TokenType.MODULOS, "%");
                }
                case '=': {
                    if(peekNext() == '='){
                        advance();
                        return buildToken(TokenType.EQUALS, "==");
                    }
                    return buildToken(TokenType.ASSING, "=");
                }
                case '!': {
                    if(peekNext() == '='){
                        advance();
                        return buildToken(TokenType.NOT_EQUALS, "!=");
                    }
                    return buildToken(TokenType.NOT, "!");
                }
                case '>': {
                    if(peekNext() == '='){
                        advance();
                        return buildToken(TokenType.GREATER_OR_EQUALS, ">=");
                    }
                    return buildToken(TokenType.GREATER, ">");
                }
                case '<': {
                    if(peekNext() == '='){
                        advance();
                        return buildToken(TokenType.LESS_OR_EQUALS, "<=");
                    }
                    return buildToken(TokenType.LESS, "<");
                }
                case '(': {
                    return buildToken(TokenType.LEFTPAR, "(");
                }
                case ')': {
                    return buildToken(TokenType.RIGHTPAR, ")");
                }
                case ',': {
                    return buildToken(TokenType.COMMA, ",");
                }
                case '{': {
                    return buildToken(TokenType.LEFT_CURLY_BRACE, "{");
                }
                case '}': {
                    return buildToken(TokenType.RIGHT_CURLY_BRACE, "}");
                }
                default:
                    assert false : "unexpected token, found : " + current;
            }
        }
        return buildToken(TokenType.EOF, "");
//        return null;
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
            if(TokenType.STRING_LITERAL.equals(type)){
                assert false : "illegal string literal ending, found : "+res;
            } else if(TokenType.NUMBER.equals(type)) {
                assert false : "illegal number representation, found : "+res;
            } else {
                assert false : "Unreachable token, found : "+res;
            }
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
                case VOID:
                    token.type = TokenType.VOID;
                    break;
                case INT:
                    token.type = TokenType.INT;
                    break;
                case BOOLEAN:
                    token.type = TokenType.BOOLEAN;
                    break;
                case STRING:
                    token.type = TokenType.STRING;
                    break;
                case CHARACTER:
                    token.type = TokenType.CHARACTER;
                    break;
                case LEFTPAR:
                case RIGHTPAR:
                case ASSING:
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