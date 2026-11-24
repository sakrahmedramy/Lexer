import java.io.File;                  
import java.io.FileNotFoundException; 
import java.util.ArrayDeque;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue; 

public class ReadFile implements Runnable {
    // Se compară cu == ca să nu fie confundate cu o linie "STOP" din fișier
    public static final String STOP = new String("STOP");
    public static final String NEW_FILE = new String("NEW_FILE");

    private BlockingQueue<String> linkingStack;
    private String pathOfProject;

    public ReadFile(BlockingQueue<String> linkingStack, String pathOfProject) {
        this.linkingStack = linkingStack;
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
        linkingStack.put(STOP);
            
            } catch (InterruptedException e) {
                System.out.println("Thread error: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }

private void readFile(File file) throws InterruptedException {
        try (Scanner myReader = new Scanner(file)) {
            linkingStack.put(NEW_FILE);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                
                linkingStack.put(line); 
            }
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't read the: " + file.getName());
        }
    }
}
