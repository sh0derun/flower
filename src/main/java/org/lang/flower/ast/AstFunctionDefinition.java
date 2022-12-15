package org.lang.flower.ast;

public class AstFunctionDefinition extends AstDeclaration {

    AstDefinitionBlock block;

    AstFunctionDefinition(String name){
        this.name = name;
        type = DeclarationType.FUNCTION;
    }

}
