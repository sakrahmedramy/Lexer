import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static void main (String[] args){
        BlockingQueue<String> linkingStack = new ArrayBlockingQueue<>(100);

        if(args.length == 0){
            System.out.println("Usage: java Main <folder>");
            return;
        }

        String projectPath = args[0];

        ReadFile reader = new ReadFile(linkingStack,projectPath);
        Parse parser = new Parse(linkingStack);

        Thread thread1 = new Thread(reader);
        Thread thread2 = new Thread(parser);

        thread1.start();
        thread2.start();
    }
}
