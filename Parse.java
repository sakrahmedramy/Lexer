import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Parse implements Runnable {
    
    private BlockingQueue<String> linkingStack;
    private Lexer lexer;

    public Parse(BlockingQueue<String> linkingStack) {
        this.linkingStack = linkingStack;
        this.lexer = new Lexer();
    }

    @Override
    public void run() {
        int currentLineNumber = 1;
        try {
            while (true) {
               
                String line = linkingStack.take(); 
               
                if (line == ReadFile.STOP) {
                    System.out.println("Done");
                    break;
                }

                if (line == ReadFile.NEW_FILE) {
                    currentLineNumber = 1;
                    lexer.inMultiLineComment = false;
                    continue;
                }

                List<Token> tokensFromLine = lexer.tokenizeLine(line, currentLineNumber);

                for(Token t : tokensFromLine) {
                    System.out.println(t.toString());
                }

                currentLineNumber++;
            
            }
        } catch (InterruptedException e) {
            System.out.println("Errore in Parse: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}