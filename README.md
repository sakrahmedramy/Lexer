# Multithreaded Java Lexer

A lexical analyzer written from scratch in Java. It walks a folder, reads every `.java` / `.txt` file and turns the source code into typed tokens (`TokenType` enum).

## How it works

- **Producer-Consumer:** `ReadFile` (producer thread) does the file I/O and puts lines into an `ArrayBlockingQueue`. `Parse` (consumer thread) takes them out and tokenizes them with `Lexer`.
- **Poison pill:** when every file has been read, `ReadFile` puts `STOP` in the queue and `Parse` stops. Before each file it also sends `NEW_FILE`, so line numbers restart at 1 and the comment state is reset. Both signals are compared with `==`, so a source line containing the text `STOP` can't stop the program.
- **Keywords:** looked up in a `HashMap` (O(1)).
- **Operators:** a `switch` on a primitive `char`. One character of lookahead handles composite operators: `==` `!=` `<=` `>=` `&&` `||` `++` `--` `+=` `-=`.
- **Comments:** `//` skips the rest of the line. `/* ... */` can span multiple lines (`inMultiLineComment` keeps the state between lines).
- **String literals:** `"..."` becomes a single `STRING_LITERAL` token. `\"` does not close the string.

## Running

Requires JDK 17+.

```bash
javac -d out *.java
java -cp out Main <folder>
```

Example, using the included `example/` folder:

```bash
java -cp out Main example
```

`example/Hello.java`:

```java
public class Hello {
    /* multi-line
       comment */
    public static void main(String[] args) {
        int x = 10;
        if (x >= 5 && x != 7) {
            x++; // increment
        }
    }
}
```

Output (shortened):

```
[PUBLIC : 'public' (line 1)]
[CLASS : 'class' (line 1)]
[IDENTIFIER : 'Hello' (line 1)]
[LBRACE : '{' (line 1)]
[PUBLIC : 'public' (line 4)]
[STATIC : 'static' (line 4)]
[VOID : 'void' (line 4)]
[IDENTIFIER : 'main' (line 4)]
...
[INT : 'int' (line 5)]
[IDENTIFIER : 'x' (line 5)]
[ASSIGN : '=' (line 5)]
[NUMBER : '10' (line 5)]
[SEMICOLON : ';' (line 5)]
[IF : 'if' (line 6)]
[LPAREN : '(' (line 6)]
[IDENTIFIER : 'x' (line 6)]
[GREATER_EQUAL : '>=' (line 6)]
[NUMBER : '5' (line 6)]
[AND : '&&' (line 6)]
[IDENTIFIER : 'x' (line 6)]
[NOT_EQUAL : '!=' (line 6)]
[NUMBER : '7' (line 6)]
[RPAREN : ')' (line 6)]
[LBRACE : '{' (line 6)]
[IDENTIFIER : 'x' (line 7)]
[INCREMENT : '++' (line 7)]
[SEMICOLON : ';' (line 7)]
[RBRACE : '}' (line 8)]
[RBRACE : '}' (line 9)]
[RBRACE : '}' (line 10)]
Done
```

Lines 2-3 (the multiline comment) and the `// increment` comment produce no tokens.

## Tests

The JUnit 5 tests are in `test/LexerTest.java`. The JUnit jar is in `lib/`.

```bash
javac -d out *.java
javac -d out -cp "out;lib/junit-platform-console-standalone-1.11.4.jar" test/LexerTest.java
java -jar lib/junit-platform-console-standalone-1.11.4.jar execute -cp out --select-class LexerTest
```

On Linux/macOS, use `:` instead of `;` in the classpath.
