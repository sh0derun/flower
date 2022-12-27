package org.lang.flower.ast;

public class Expression {

    public enum ExpressionType {
        NUMBER_LITERAL,
        STRING_LITERAL,
        BINARY_OPERATION
    }

    public ExpressionType type;
    public String expressionLiteral;

    public Expression(ExpressionType type, String expressionLiteral){
        this.type = type;
        this.expressionLiteral = expressionLiteral;
    }

    @Override
    public String toString() {
        return "Expression{" +
                "type=" + type +
                ", expressionLiteral='" + expressionLiteral + '\'' +
                '}';
    }
}
