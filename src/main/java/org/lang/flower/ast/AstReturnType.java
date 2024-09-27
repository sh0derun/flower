package org.lang.flower.ast;

public enum AstReturnType {
    VOID("void"), INT("int"), BOOLEAN("bool"), STRING("str"), CHARACTER("char");

    private String value;

    AstReturnType(String value){
        this.value = value;
    }

    public static AstReturnType getValueFromName(String name){
        AstReturnType returnType= null;
        for(AstReturnType astReturnType : values()){
            if(astReturnType.name().equalsIgnoreCase(name)){
                returnType = astReturnType;
                break;
            }
        }
        return returnType;
    }
}
