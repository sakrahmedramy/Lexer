import java.util.concurrent.BlockingQueue;

public class Parse implements Runnable {
    
    private BlockingQueue<String> linkingStack;

    public Parse(BlockingQueue<String> linkingStack) {
        this.linkingStack = linkingStack;
    }

    @Override
    public void run() {
        try {
            while (true) {
               
                String line = linkingStack.take(); 
               
                if (line.equals("STOP")) {
                    System.out.println("Gata! Nu mai sunt fișiere de parsat.");
                    break; 
                }

                System.out.println(line);
            
            }
        } catch (InterruptedException e) {
            System.out.println("Eroare în Parse: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}