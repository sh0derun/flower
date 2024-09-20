package org.lang.flower.ast;

public class AstFunctionCall <T> extends AstStatement {

    public AstFunctionDefinition<T> definition;

    public AstFunctionArgument<Object> argument;

    public AstFunctionCall(String name){
        this.name = name;
        type = StatementType.FUNCTION_CALL;
        definition = null;
    }

    @Override
    public String toString() {
        return "AstFunctionCall{" +
                "\n\tdefinition=" + definition +
                ", \n\targument=" + argument +
                ", \n\tname='" + name + '\'' +
                ", \n\ttype=" + type +
                "\n}";
    }
}
