package org.lang.flower.ast;

public class AstAssignment extends AstStatement {

    public Expression expression;

    public AstAssignment(String name, Expression.ExpressionType expressionType, String expressionLiteral){
        this.name = name;
        this.type = StatementType.ASSIGNMENT;
        this.expression = new Expression(expressionType, expressionLiteral);
    }

    @Override
    public String toString() {
        return "AstAssignment{" +
                "expression=" + expression +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
