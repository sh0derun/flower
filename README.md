# Flower

An attempt to create a programming language

## Prerequisite

- nasm assembler [https://www.nasm.us/](https://www.nasm.us/)  
- gcc from mingw are needed to be able to build [https://www.mingw-w64.org/](https://www.mingw-w64.org/)

## Prerequisite

### Compiling and Running Java Project

#### 1. Compile the Java files

- First, create a list of all Java source files and then compile them:

```bash
# On Unix-like systems (Linux, macOS):
find src/main/java -name "*.java" > sources.txt

# On Windows (Command Prompt):
dir /s /B src\main\java\*.java > sources.txt

# On Windows (PowerShell):
Get-ChildItem -Path src\main\java -Filter *.java -Recurse | ForEach-Object { $_.FullName } > sources.txt

# Compile all files listed in sources.txt
javac -d out @sources.txt

# Step 3: Run the compiled program with assertions enabled
java -ea -cp .\out org.lang.flower.Main code.flower
```

- The first command creates a `sources.txt` file containing paths to all Java source files.
- `javac -d out @sources.txt` compiles all listed files, outputting to the `out` directory.
- `java -ea -cp .\out org.lang.flower.Main code.flower` runs the program:
    - `-ea`: Enables assertions because lexer and the parser are based on assertions
    - `-cp .\out`: Sets the classpath to the `out` directory
    - `org.lang.flower.Main`: The fully qualified name of the main class
    - `code.flower`: An argument that represents the file code passed to the main method

## Status

Now the language is capable just to print literal string or number, and it supports only windows