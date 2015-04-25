import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
/**
 *
 * @author George
 */
public class WatchServiceExample {
    
    
    public static void main(String args[]){
        String directoryToWatch = args[0]; 
        Path path = Paths.get(directoryToWatch);
        WatchService watchService = null;
        try{
            watchService = path.getFileSystem().newWatchService();
            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY,
            StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);
        } catch (IOException e){
            e.printStackTrace();
        }
        
        while (true){
            WatchKey key = null;
            try {
                key = watchService.take();
                
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            
            for (WatchEvent<?> event : key.pollEvents()){
               switch (event.kind().name()){
                   case "OVERFLOW":
                       System.out.println("Events lost");
                       break;
                   case "ENTRY_MODIFY":
                       System.out.println("File "+event.context()+ " changed.");
                       break;
                   case "ENTRY_CREATE":
                       System.out.println("File "+event.context()+" created.");
                       break;
                   case "ENTRY_DELETE":
                       System.out.println("File "+event.context()+" deleted.");
                       break;   
               }
            
            key.reset();
            }
            
        }
    }
}
