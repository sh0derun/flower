package org.lang.flower;

import org.lang.flower.ast.AstFunctionCall;

import java.io.FileWriter;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        Parser parser = new Parser("print(\"hello hhh\")");
        AstFunctionCall<Object> ast = parser.parseFunctionCall();
        System.out.println(ast);

        String asm = Compiler.compileAstToAsm(ast);
        System.out.println(asm);

        String assemblyFileName = "code";



        try (FileWriter fileWriter = new FileWriter(assemblyFileName+".asm")) {
            fileWriter.append(asm);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Compiler.assembleAndLink(assemblyFileName);
    }
}