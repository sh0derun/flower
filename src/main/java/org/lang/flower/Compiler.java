package org.lang.flower;

import org.lang.flower.ast.AstFunctionCall;
import org.lang.flower.ast.AstStatement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Compiler {

    private static final String OS;
    private static final String ARCH;

    static {
        OS = System.getProperty("os.name");
        ARCH = System.getProperty("os.arch");
        System.out.println("----------------------");
        System.out.println(OS + " - " + ARCH);
        System.out.println("----------------------");
    }

    private Compiler() {
    }

    public static String compileAstToAsm(AstStatement statement) {
        StringBuilder asm = new StringBuilder("");
        if (statement != null) {
            if (OS.contains("Windows")) {
                if (ARCH.contains("amd64")) {
                    asm.append("bits 64\n");
                    StringBuilder externs = new StringBuilder("extern ");
                    List<String> externSymbols = new ArrayList<>(List.of("ExitProcess"));
                    StringBuilder textSection = new StringBuilder("\nsection .text\n");
                    StringBuilder dataSection = new StringBuilder("\nsection .data\n");
                    StringBuilder bssSection = new StringBuilder("\nsection .bss\n");

                    textSection.append("\tglobal main\n");
                    textSection.append("\tmain:\n");

                    switch (statement.type) {
                        case FUNCTION_CALL: {
                            AstFunctionCall<?> functionCall = (AstFunctionCall<?>) statement;
                            if ("print".equals(statement.name)) {
                                externSymbols.add("GetStdHandle");
                                textSection.append("\t\tmov rcx, -11\n");
                                textSection.append("\t\tcall GetStdHandle\n");
                                textSection.append("\t\t;-------------------------\n");
                                dataSection.append("\tmessage:  db  \"").append(functionCall.argument.value).append("\"").append("\n");
                                dataSection.append("\tmessageSize:\tequ\t$-message\n");
                                bssSection.append("\tptrwitten:\tresd\t1\n");
                                externSymbols.add("WriteConsoleA");

                                textSection.append("\t\tsub rsp, 32\n");

                                textSection.append("\t\tmov rcx, rax\n");
                                textSection.append("\t\tmov rdx, message\n");
                                textSection.append("\t\tmov r8, messageSize\n");
                                textSection.append("\t\tmov r9, ptrwitten\n");
                                textSection.append("\t\tmov qword [rsp+32], 0\n");
                                textSection.append("\t\tcall WriteConsoleA\n");

                                textSection.append("\t\tadd rsp, 32\n");
                            }
                        }
                    }
                    textSection.append("\t\t;-------------------------\n");
                    textSection.append("\t\tmov rcx, 0\n");
                    textSection.append("\t\tcall ExitProcess\n");

                    externs.append(String.join(",", externSymbols));
                    asm.append(externs);
                    asm.append(dataSection);
                    asm.append(bssSection);
                    asm.append(textSection);
                }
            }
        }
        return asm.toString();
    }

    public static void assembleAndLink(String assemblyFileName) {
        ProcessBuilder builder = new ProcessBuilder("cmd", "/c", "nasm", "-fwin64", "-o " + assemblyFileName + ".obj", assemblyFileName + ".asm");
        try {
            Process process = builder.inheritIO().start();
            process.waitFor();
            if (process.exitValue() == 0) {
                builder.command("cmd", "/c", "ld -o " + assemblyFileName + ".exe " + assemblyFileName + ".obj -LC:/Windows/System32 -lkernel32");
                process = builder.inheritIO().start();
                process.waitFor();
                if (process.exitValue() == 0) {
                    builder.command("cmd", "/c", assemblyFileName + ".exe");
                    process = builder.inheritIO().start();
                    process.waitFor();
                    System.exit(process.exitValue());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

}
