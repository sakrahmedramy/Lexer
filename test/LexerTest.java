import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class LexerTest {

    @Test
    public void keywordsAndIdentifiers(){
        Lexer lexer = new Lexer();
        List<Token> tokens = lexer.tokenizeLine("public int count_1 = 42;", 1);

        assertEquals(TokenType.PUBLIC, tokens.get(0).type);
        assertEquals(TokenType.INT, tokens.get(1).type);
        assertEquals(TokenType.IDENTIFIER, tokens.get(2).type);
        assertEquals("count_1", tokens.get(2).value);
        assertEquals(TokenType.ASSIGN, tokens.get(3).type);
        assertEquals(TokenType.NUMBER, tokens.get(4).type);
        assertEquals("42", tokens.get(4).value);
        assertEquals(TokenType.SEMICOLON, tokens.get(5).type);
        assertEquals(6, tokens.size());
    }

    @Test
    public void compositeOperators(){
        Lexer lexer = new Lexer();
        List<Token> tokens = lexer.tokenizeLine("== != <= >= && || ++ -- += -= < > =", 1);

        TokenType[] expected = {
            TokenType.EQUAL, TokenType.NOT_EQUAL, TokenType.LESS_EQUAL, TokenType.GREATER_EQUAL,
            TokenType.AND, TokenType.OR, TokenType.INCREMENT, TokenType.DECREMENT,
            TokenType.PLUS_ASSIGN, TokenType.MINUS_ASSIGN, TokenType.LESS, TokenType.GREATER,
            TokenType.ASSIGN
        };

        assertEquals(expected.length, tokens.size());
        for(int i = 0; i < expected.length; i++){
            assertEquals(expected[i], tokens.get(i).type);
        }
    }

    @Test
    public void multiLineCommentAcrossLines(){
        Lexer lexer = new Lexer();

        List<Token> line1 = lexer.tokenizeLine("int a; /* start", 1);
        List<Token> line2 = lexer.tokenizeLine("int ignored;", 2);
        List<Token> line3 = lexer.tokenizeLine("end */ int b;", 3);

        assertEquals(3, line1.size());
        assertTrue(line2.isEmpty());
        assertEquals(3, line3.size());
        assertEquals("b", line3.get(1).value);
        assertEquals(3, line3.get(1).line);
    }

    @Test
    public void stringLiteralAndLineComment(){
        Lexer lexer = new Lexer();
        List<Token> tokens = lexer.tokenizeLine("s = \"a \\\" b\"; // comment", 1);

        assertEquals(4, tokens.size());
        assertEquals(TokenType.STRING_LITERAL, tokens.get(2).type);
        assertEquals("\"a \\\" b\"", tokens.get(2).value);
        assertEquals(TokenType.SEMICOLON, tokens.get(3).type);
    }
}
