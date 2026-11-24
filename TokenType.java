public enum TokenType {
    // Cuvinte cheie (Keywords)
    INT, CLASS, PUBLIC, PRIVATE, STATIC, VOID, RETURN, IF, ELSE, WHILE, FOR, NEW,

    // Identificatori și Literali
    IDENTIFIER, 
    NUMBER, 
    STRING_LITERAL,

    // Operatori Aritmetici și de Atribuire
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

    // Operatori Logici și Relaționali
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

    // Punctuație și Delimitatori
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

    // Caractere speciale pentru text
    BACKSLASH,    // \

    // Sfârșitul fișierului/stream-ului
    EOF
}