package org.lang.flower.ast;

import java.util.List;

public class AstFunctionDefinition<T> extends AstDeclaration {

    AstReturnType returnType;
    String name;
    List<AstFunctionParameter> parameters;
    List<AstStatement> body;

    AstFunctionDefinition(String name){
        this.name = name;
        type = DeclarationType.FUNCTION;
    }

    public AstFunctionDefinition(AstReturnType returnType, String name, List<AstFunctionParameter> parameters, List<AstStatement> body) {
        this.returnType = returnType;
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public String toString() {
        return "AstFunctionDefinition{" +
                "returnType=" + returnType +
                ", name='" + name + '\'' +
                ", parameters=" + parameters +
                ", body=" + body +
                '}';
    }
}
