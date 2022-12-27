package org.lang.flower.ast;

public abstract class AstStatement {
    public String name;

    public enum StatementType {
        FUNCTION_CALL,
        ASSIGNMENT
    }
    public StatementType type;
}
