package org.lang.flower.ast;

public abstract class AstStatement {
    protected String name;

    enum StatementType {
        FUNCTION_CALL
    }
    protected StatementType type;
}
