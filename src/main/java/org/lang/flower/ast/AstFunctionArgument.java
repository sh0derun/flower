package org.lang.flower.ast;

public class AstFunctionArgument <T> {

    public T value;

    public enum ArgumentType {
        NUMBER, STRING_LITERAL
    } public ArgumentType type;

    public AstFunctionArgument<T> next = null;

    @Override
    public String toString() {
        return "AstFunctionArgument{" +
                "value=" + value +
                ", type=" + type +
                ", next=" + next +
                '}';
    }
}
