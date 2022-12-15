package org.lang.flower;

import org.lang.flower.ast.AstFunctionCall;


public class Main {
    public static void main(String[] args) {
        Parser parser = new Parser("print(9,1,3,\"hello\")");
        AstFunctionCall ast = parser.parseFunctionCall();
        System.out.println(ast);
    }
}