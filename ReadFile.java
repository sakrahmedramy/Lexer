import java.io.File;                  
import java.io.FileNotFoundException; 
import java.util.ArrayDeque;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue; 

public class ReadFile implements Runnable {
    // Compared with == so they are never confused with a "STOP" line from a file
    public static final String STOP = new String("STOP");
    public static final String NEW_FILE = new String("NEW_FILE");

    private BlockingQueue<String> queue;
    private String pathOfProject;

    public ReadFile(BlockingQueue<String> queue, String pathOfProject) {
        this.queue = queue;
        this.pathOfProject = pathOfProject;
    }

    @Override
    public void run(){

        ArrayDeque<File> foldersToExplore = new ArrayDeque<>();
        File root = new File(pathOfProject);

        if (root.exists() && root.isDirectory()) {
            foldersToExplore.add(root);
        }

        try { while(!foldersToExplore.isEmpty()){
            File currentFolder = foldersToExplore.poll();
            File[] folderContent = currentFolder.listFiles();
            
            if(folderContent != null){
                for(File folderOrFile : folderContent){
                    if(folderOrFile.isDirectory()){
                        foldersToExplore.add(folderOrFile);
                    } else if(folderOrFile.isFile()){
                        String fileName = folderOrFile.getName();
                            if (fileName.endsWith(".java") || fileName.endsWith(".txt")) {
                                readFile(folderOrFile);
                        }
                    }   
                }
            }
        }
        queue.put(STOP);
            
            } catch (InterruptedException e) {
                System.out.println("Thread error: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }

private void readFile(File file) throws InterruptedException {
        try (Scanner myReader = new Scanner(file)) {
            queue.put(NEW_FILE);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                
                queue.put(line); 
            }
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't read the: " + file.getName());
        }
    }
}
