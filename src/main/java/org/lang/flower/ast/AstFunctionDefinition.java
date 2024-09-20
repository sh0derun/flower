package org.lang.flower.ast;

public class AstFunctionDefinition<T> extends AstDeclaration {

    AstDefinitionBlock<T> block;

    AstFunctionDefinition(String name){
        this.name = name;
        type = DeclarationType.FUNCTION;
    }

}
