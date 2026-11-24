import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {
    boolean inMultiLineComment = false;
    private static final Map<String,TokenType> keywords = new HashMap<>();

    static {
        keywords.put("int", TokenType.INT);
        keywords.put("public", TokenType.PUBLIC);
        keywords.put("private", TokenType.PRIVATE);
        keywords.put("static", TokenType.STATIC);
        keywords.put("void", TokenType.VOID);
        keywords.put("class", TokenType.CLASS);
        keywords.put("return", TokenType.RETURN);
        keywords.put("if", TokenType.IF);
        keywords.put("else", TokenType.ELSE);
        keywords.put("while", TokenType.WHILE);
        keywords.put("for", TokenType.FOR);
        keywords.put("new", TokenType.NEW);
        }

    public List<Token> tokenizeLine(String line, int lineNumber){

        List<Token> tokens = new ArrayList<>();
        int i = 0;

        while(i < line.length()){
            char c = line.charAt(i);
            char next = ' ';
            if(i + 1 < line.length()){
                next = line.charAt(i + 1);
            }

            // Multi-line comment
            if(inMultiLineComment){
                if(c == '*' && next == '/'){
                    inMultiLineComment = false;
                    i += 2;
                } else {
                    i++;
                }
                continue;
            }

            if(Character.isWhitespace(c)){
                i++;
                continue;
            }

            if (Character.isLetter(c) || c == '_') {
                StringBuilder word = new StringBuilder();
                while (i < line.length() && (Character.isLetterOrDigit(line.charAt(i)) || line.charAt(i) == '_')) {
                    word.append(line.charAt(i));
                    i++;
                }

                String text = word.toString();

                TokenType type = keywords.getOrDefault(text, TokenType.IDENTIFIER);
                tokens.add(new Token(type, text, lineNumber));

                continue;
            }
            if (Character.isDigit(c)) {
                StringBuilder num = new StringBuilder();
                while (i < line.length() && Character.isDigit(line.charAt(i))) {
                    num.append(line.charAt(i));
                    i++;
                }
                tokens.add(new Token(TokenType.NUMBER, num.toString(), lineNumber));
                continue;
            }
            if (c == '"') {
                StringBuilder str = new StringBuilder();
                str.append(c);
                i++;
                while (i < line.length() && line.charAt(i) != '"') {
                    if (line.charAt(i) == '\\' && i + 1 < line.length()) {
                        str.append(line.charAt(i));
                        i++;
                    }
                    str.append(line.charAt(i));
                    i++;
                }
                if (i < line.length()) {
                    str.append('"');
                    i++;
                } else {
                    System.out.println("Unterminated string at line " + lineNumber);
                }
                tokens.add(new Token(TokenType.STRING_LITERAL, str.toString(), lineNumber));
                continue;
            }

           switch (c) {
                // Punctuation and Delimiters
                case ';': tokens.add(new Token(TokenType.SEMICOLON, ";", lineNumber)); break;
                case '.': tokens.add(new Token(TokenType.DOT, ".", lineNumber)); break;
                case ',': tokens.add(new Token(TokenType.COMMA, ",", lineNumber)); break;
                case ':': tokens.add(new Token(TokenType.COLON, ":", lineNumber)); break;
                case '(': tokens.add(new Token(TokenType.LPAREN, "(", lineNumber)); break;
                case ')': tokens.add(new Token(TokenType.RPAREN, ")", lineNumber)); break;
                case '{': tokens.add(new Token(TokenType.LBRACE, "{", lineNumber)); break;
                case '}': tokens.add(new Token(TokenType.RBRACE, "}", lineNumber)); break;
                case '[': tokens.add(new Token(TokenType.LBRACKET, "[", lineNumber)); break;
                case ']': tokens.add(new Token(TokenType.RBRACKET, "]", lineNumber)); break;

                // Arithmetic and Assignment Operators
                case '=':
                    if(next == '='){
                        tokens.add(new Token(TokenType.EQUAL, "==", lineNumber));
                        i++;
                    } else {
                        tokens.add(new Token(TokenType.ASSIGN, "=", lineNumber));
                    }
                    break;
                case '+':
                    if(next == '+'){
                        tokens.add(new Token(TokenType.INCREMENT, "++", lineNumber));
                        i++;
                    } else if(next == '='){
                        tokens.add(new Token(TokenType.PLUS_ASSIGN, "+=", lineNumber));
                        i++;
                    } else {
                        tokens.add(new Token(TokenType.PLUS, "+", lineNumber));
                    }
                    break;
                case '-':
                    if(next == '-'){
                        tokens.add(new Token(TokenType.DECREMENT, "--", lineNumber));
                        i++;
                    } else if(next == '='){
                        tokens.add(new Token(TokenType.MINUS_ASSIGN, "-=", lineNumber));
                        i++;
                    } else {
                        tokens.add(new Token(TokenType.MINUS, "-", lineNumber));
                    }
                    break;
                case '*': tokens.add(new Token(TokenType.STAR, "*", lineNumber)); break;
                case '/':
                    if(next == '/'){
                        return tokens;
                    } else if(next == '*'){
                        inMultiLineComment = true;
                        i++;
                    } else {
                        tokens.add(new Token(TokenType.SLASH, "/", lineNumber));
                    }
                    break;
                case '%': tokens.add(new Token(TokenType.MODULO, "%", lineNumber)); break;

                // Logical and Relational Operators
                case '<':
                    if(next == '='){
                        tokens.add(new Token(TokenType.LESS_EQUAL, "<=", lineNumber));
                        i++;
                    } else {
                        tokens.add(new Token(TokenType.LESS, "<", lineNumber));
                    }
                    break;
                case '>':
                    if(next == '='){
                        tokens.add(new Token(TokenType.GREATER_EQUAL, ">=", lineNumber));
                        i++;
                    } else {
                        tokens.add(new Token(TokenType.GREATER, ">", lineNumber));
                    }
                    break;
                case '!':
                    if(next == '='){
                        tokens.add(new Token(TokenType.NOT_EQUAL, "!=", lineNumber));
                        i++;
                    } else {
                        tokens.add(new Token(TokenType.BANG, "!", lineNumber));
                    }
                    break;
                case '&':
                    if(next == '&'){
                        tokens.add(new Token(TokenType.AND, "&&", lineNumber));
                        i++;
                    } else {
                        tokens.add(new Token(TokenType.AMPERSAND, "&", lineNumber));
                    }
                    break;
                case '|':
                    if(next == '|'){
                        tokens.add(new Token(TokenType.OR, "||", lineNumber));
                        i++;
                    } else {
                        tokens.add(new Token(TokenType.PIPE, "|", lineNumber));
                    }
                    break;
                case '^': tokens.add(new Token(TokenType.CARET, "^", lineNumber)); break;

                // Special text characters
                case '\\': tokens.add(new Token(TokenType.BACKSLASH, "\\", lineNumber)); break;

                default:
                    System.out.println("Unknown character '" + c + "' at line " + lineNumber);
            }
          i++;
        }
        return tokens;
    }
}
