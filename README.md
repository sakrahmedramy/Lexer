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

Requires JDK 17+. Gradle is not needed, the wrapper (`gradlew`) downloads it.

```bash
./gradlew run --args="<folder>"
```

Example, using the included `example/` folder:

```bash
./gradlew run --args="example"
```

On Windows, use `gradlew` (cmd) or `.\gradlew` (PowerShell) instead of `./gradlew`.

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

## Limitations

- **No decimal numbers:** `3.14` is tokenized as `NUMBER(3)`, `DOT`, `NUMBER(14)`. Suffixes (`10L`, `2.5f`), hex (`0xFF`) and `1_000` are not supported either.
- **No char literals:** `'a'` is not recognized. `'` is reported as an unknown character.
- **Only a subset of keywords:** `int`, `public`, `private`, `static`, `void`, `class`, `return`, `if`, `else`, `while`, `for`, `new`. Other Java keywords (`boolean`, `try`, `switch`, `null`, ...) come out as `IDENTIFIER`.
- **Partial operator coverage:** `*=`, `/=`, `%=`, `->`, `::`, `<<`, `>>`, `?` and `~` are not recognized as tokens.
- **Line by line:** a string literal cannot span multiple lines (an unclosed `"` is reported and closed at the end of the line). Text blocks (`"""`) are not supported.
- **One consumer thread:** I/O and tokenization are separated, but tokenization itself is not parallelized across files.

## Tests

The JUnit 5 tests are in `test/LexerTest.java`. Gradle downloads JUnit automatically.

```bash
./gradlew test
```
