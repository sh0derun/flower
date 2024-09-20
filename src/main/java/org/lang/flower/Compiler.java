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

    private Compiler() {}

    public static String compileAstToAsm(AstStatement statement) {
        StringBuilder asm = new StringBuilder();
        if (statement != null && OS.contains("Windows") && ARCH.contains("amd64")) {
            asm.append("bits 64\n");
            asm.append("default rel\n"); // ensures proper addressing modes
            asm.append("extern ExitProcess, GetStdHandle, WriteConsoleA\n\n");

            StringBuilder dataSection = new StringBuilder("section .data\n");
            StringBuilder bssSection = new StringBuilder("section .bss\n");
            StringBuilder textSection = new StringBuilder("section .text\n");

            textSection.append("global main\n\n");
            textSection.append("main:\n");
            textSection.append("    push rbp\n"); // proper function prologue
            textSection.append("    mov rbp, rsp\n");
            textSection.append("    sub rsp, 64  ; Allocate shadow space and align stack\n\n"); // ensure 16-byte stack alignment

            if (statement.type == AstStatement.StatementType.FUNCTION_CALL) {
                AstFunctionCall<?> functionCall = (AstFunctionCall<?>) statement;
                if ("print".equals(functionCall.name)) {
                    String message = functionCall.argument.value.toString();
                    dataSection.append("    message db \"")
                                .append(message)
                                .append("\", 0\n"); // adding null terminator to the string
                    dataSection.append("    messageLen equ $ - message - 1\n\n"); // adjusted size calculation to account for null terminator
                    bssSection.append("    written resq 1\n\n"); // changed to resq (8 bytes) instead of resd (4 bytes)

                    textSection.append("    ; Get handle to stdout\n");
                    textSection.append("    mov rcx, -11\n");
                    textSection.append("    call GetStdHandle\n");
                    textSection.append("    mov rbx, rax  ; Save handle in rbx\n\n");

                    textSection.append("    ; Call WriteConsoleA\n");
                    textSection.append("    mov rcx, rbx  ; hConsoleOutput\n");
                    textSection.append("    lea rdx, [message]  ; lpBuffer\n"); // use lea for effective address loading
                    textSection.append("    mov r8, messageLen  ; nNumberOfCharsToWrite\n");
                    textSection.append("    lea r9, [written]  ; lpNumberOfCharsWritten\n"); // use lea for effective address loading
                    textSection.append("    mov qword [rsp+32], 0  ; lpReserved must be NULL\n");
                    textSection.append("    call WriteConsoleA\n\n");
                }
            }

            textSection.append("    ; Exit program\n");
            textSection.append("    xor ecx, ecx  ; exit code 0\n"); // simplified exit process call
            textSection.append("    call ExitProcess\n\n");

            //[TODO] (Safety measures in case ExitProcess doesn't terminate) to be removed once code is stable
            textSection.append("    ; We shouldn't reach here, but just in case:\n");
            textSection.append("    xor eax, eax\n");
            textSection.append("    leave\n");
            textSection.append("    ret\n");

            asm.append(dataSection);
            asm.append(bssSection);
            asm.append(textSection);
        }
        return asm.toString();
    }

    public static void assembleAndLink(String assemblyFileName) {
        try {
            // Assemble
            ProcessBuilder assembler = new ProcessBuilder("nasm", "-f win64", "-o", assemblyFileName + ".obj", assemblyFileName + ".asm");
            Process process = assembler.inheritIO().start();
            if (process.waitFor() != 0) {
                throw new RuntimeException("Assembly failed");
            }

            // Link
            ProcessBuilder linker = new ProcessBuilder("gcc", "-o", assemblyFileName+".exe", assemblyFileName+".obj", "-lkernel32", "-luser32");
            process = linker.inheritIO().start();
            if (process.waitFor() != 0) {
                throw new RuntimeException("Linking failed");
            }

            System.out.println("Executable created: " + assemblyFileName + ".exe");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            System.exit(1);
        }
    }

}
