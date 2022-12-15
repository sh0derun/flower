package org.lang.flower;

import org.lang.flower.ast.AstFunctionArgument;
import org.lang.flower.ast.AstFunctionCall;

public class Parser {

    Lexer lexer;
    Token currentToken;

    public Parser(String code) {
        lexer = new Lexer(code);
    }

    public AstFunctionCall parseFunctionCall() {
        Token functionName = expectFunctionName();
        AstFunctionCall ast = new AstFunctionCall(functionName.value);

        expectLeftParent();

        Token token = lexer.getNextToken();
        assert !TokenType.COLON.equals(token.type) : "argument expected, found '" + TokenType.COLON.getValue()+"'";
        assert TokenType.STRING_LITERAL.equals(token.type) || TokenType.NUMBER.equals(token.type) : "unsupported argument type";
        AstFunctionArgument<Object> argument = null;

        while (token.type != TokenType.RIGHTPAR) {
            if(argument == null) {
                argument = new AstFunctionArgument<>();
                buildArgument(token, argument);
            } else {
                AstFunctionArgument<Object> ptr = argument;
                while (ptr.next != null) {
                    ptr = ptr.next;
                }
                AstFunctionArgument<Object> newArgument = new AstFunctionArgument<>();
                ptr.next = newArgument;
                buildArgument(token, newArgument);
            }

            token = lexer.getNextToken();

            if(token.type == TokenType.COLON){
                token = lexer.getNextToken();
                assert token.type != TokenType.RIGHTPAR : "argument expected, found '" + TokenType.RIGHTPAR.getValue()+"'";
            }
        }

        ast.argument = argument;
        return ast;
    }

    private void buildArgument(Token token, AstFunctionArgument<Object> argument) {
        if (token.type == TokenType.STRING_LITERAL) {
            argument.type = AstFunctionArgument.ArgumentType.STRING_LITERAL;
        } else if (token.type == TokenType.NUMBER) {
            argument.type = AstFunctionArgument.ArgumentType.NUMBER;
        }
        argument.value = token.value;
    }

    private Token expectFunctionName() {
        Token token = lexer.getNextToken();
        assert TokenType.ID.equals(token.type) || TokenType.PRINT.equals(token.type) : "function name expected, found '" + token.type +"'";
        return token;
    }

    private Token expectLeftParent() {
        Token token = lexer.getNextToken();
        assert TokenType.LEFTPAR.equals(token.type) : "left parenthesis " + TokenType.LEFTPAR.getValue() + " expected, found '" + token.type+"'";
        return token;
    }

    private Token expectRightParent() {
        Token token = lexer.getNextToken();
        assert TokenType.RIGHTPAR.equals(token.type) : "right parenthesis  " + TokenType.RIGHTPAR.getValue() + " expected, found '" + token.type+"'";
        return token;
    }

    private Token expectColon() {
        Token token = lexer.getNextToken();
        assert TokenType.COLON.equals(token.type) : "colon  " + TokenType.COLON.getValue() + " expected, found '" + token.type+"'";
        return token;
    }

}
