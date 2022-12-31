package org.lang.flower;

import org.lang.flower.ast.AstFunctionCall;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("\nflower: fatal error: no input file");
        }

        String inputFile = args[0];
        Optional<String> code = Optional.empty();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile))) {
            code = bufferedReader.lines().reduce((s, s2) -> s +"\n"+ s2);
        } catch (IOException e) {
            throw new IllegalArgumentException("\nflower: error: " + inputFile + ": No such file\nflower: fatal error: no input file");
        }

        Parser parser = new Parser(code.orElseThrow());
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