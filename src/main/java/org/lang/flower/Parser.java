package org.lang.flower;

import org.lang.flower.ast.*;

public class Parser {

    Lexer lexer;
    Token currentToken;

    public Parser(String code) {
        lexer = new Lexer(code);
    }

    public AstAssignment parseAssignment() {
        expectLet();
        Token identifier = expectIdentifier();
        expectEqu();
        Token literalValue = expectLiteralValue();
        Expression.ExpressionType expressionType = null;
        if (TokenType.NUMBER.equals(literalValue.type)) {
            expressionType = Expression.ExpressionType.NUMBER_LITERAL;
        } else if (TokenType.STRING_LITERAL.equals(literalValue.type)) {
            expressionType = Expression.ExpressionType.STRING_LITERAL;
        } else {
            assert false : "Unreachable";
        }
        AstAssignment assignment = new AstAssignment(identifier.value, expressionType, literalValue.value);
        return assignment;
    }

    public AstFunctionDefinition<Object> parseFunctionDefinition() {
        Token returnType = expectReturnType();
        return null;
    }

    private Token expectReturnType() {
        Token token = lexer.getNextToken();
        System.out.println(AstReturnType.valueOf(token.value));
        return token;
    }

    public AstFunctionCall<Object> parseFunctionCall() {
        Token functionName = expectIdentifier();
        AstFunctionCall<Object> ast = new AstFunctionCall(functionName.value);

        expectLeftParent();

        Token token = lexer.getNextToken();
        assert !TokenType.COLON.equals(token.type) : "argument expected, found '" + TokenType.COLON.getValue() + "'";
        assert TokenType.STRING_LITERAL.equals(token.type) || TokenType.NUMBER.equals(token.type) : "unsupported argument type";
        AstFunctionArgument<Object> argument = null;

        while (token.type != TokenType.RIGHTPAR) {
            if (argument == null) {
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

            if (token.type == TokenType.COLON) {
                token = lexer.getNextToken();
                assert token.type != TokenType.RIGHTPAR : "argument expected, found '" + TokenType.RIGHTPAR.getValue() + "'";
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

    private Token expectIdentifier() {
        Token token = lexer.getNextToken();
        assert TokenType.ID.equals(token.type) || TokenType.PRINT.equals(token.type) : "identifier expected, found '" + token.value + "'";
        return token;
    }

    private Token expectLeftParent() {
        Token token = lexer.getNextToken();
        assert TokenType.LEFTPAR.equals(token.type) : "left parenthesis " + TokenType.LEFTPAR.getValue() + " expected, found '" + token.value + "'";
        return token;
    }

    private Token expectRightParent() {
        Token token = lexer.getNextToken();
        assert TokenType.RIGHTPAR.equals(token.type) : "right parenthesis  " + TokenType.RIGHTPAR.getValue() + " expected, found '" + token.value + "'";
        return token;
    }

    private Token expectColon() {
        Token token = lexer.getNextToken();
        assert TokenType.COLON.equals(token.type) : "colon  " + TokenType.COLON.getValue() + " expected, found '" + token.value + "'";
        return token;
    }

    private Token expectLiteralValue() {
        Token token = lexer.getNextToken();
        assert TokenType.NUMBER.equals(token.type) || TokenType.STRING_LITERAL.equals(token.type) : "'=' expected, found '" + token.value;
        return token;
    }

    private void expectEqu() {
        Token token = lexer.getNextToken();
        assert TokenType.EQU.equals(token.type) : "'=' expected, found '" + token.value;
    }

    private void expectLet() {
        Token token = lexer.getNextToken();
        assert TokenType.LET.equals(token.type) : "let expected, found '" + token.value;
    }

    public void parse() {
        //TODO : cross reference between function definition and the call
        Token token = lexer.getNextToken();
        switch(AstReturnType.getValueFromName(token.value)){
            case VOID:
                //implement rest of types and treat them as function definitions

        }
    }
}
