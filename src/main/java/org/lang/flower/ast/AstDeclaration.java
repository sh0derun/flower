package org.lang.flower.ast;

public abstract class AstDeclaration {

    String name;

    enum DeclarationType {
        FUNCTION
    }
    DeclarationType type;

}
