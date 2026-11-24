public enum TokenType {
    // Keywords
    INT, CLASS, PUBLIC, PRIVATE, STATIC, VOID, RETURN, IF, ELSE, WHILE, FOR, NEW,

    // Identifiers and Literals
    IDENTIFIER, 
    NUMBER, 
    STRING_LITERAL,

    // Arithmetic and Assignment Operators
    ASSIGN,       // =
    PLUS,         // +
    MINUS,        // -
    STAR,         // *
    SLASH,        // /
    MODULO,       // %
    INCREMENT,    // ++
    DECREMENT,    // --
    PLUS_ASSIGN,  // +=
    MINUS_ASSIGN, // -=

    // Logical and Relational Operators
    EQUAL,        // ==
    NOT_EQUAL,    // !=
    LESS,         // <
    LESS_EQUAL,   // <=
    GREATER,      // >
    GREATER_EQUAL,// >=
    BANG,         // !
    AND,          // &&
    OR,           // ||
    AMPERSAND,    // &
    PIPE,         // |
    CARET,        // ^

    // Punctuation and Delimiters
    SEMICOLON,    // ;
    DOT,          // .
    COMMA,        // ,
    COLON,        // :
    
    LPAREN,       // (
    RPAREN,       // )
    LBRACE,       // {
    RBRACE,       // }
    LBRACKET,     // [
    RBRACKET,     // ]

    // Special text characters
    BACKSLASH,    // \

    // End of file/stream
    EOF
}