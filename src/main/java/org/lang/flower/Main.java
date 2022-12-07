package org.lang.flower;

public class Main {

    static Lexer lexer = new Lexer("let i = \"hello\"");

    public static void main(String[] args) {
        for(Token token = lexer.getNextToken(); token != null; token = lexer.getNextToken())
            System.out.println(token);
    }
}
