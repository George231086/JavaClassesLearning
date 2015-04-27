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
// Basic example demonstrating Java WatchService. 
// Based on https://docs.oracle.com/javase/tutorial/essential/io/notification.html
public class WatchServiceExample {

    public static void main(String args[]) {
        if (args.length != 1) {
            System.out.println("Requires single Directory name as argument.");
            System.exit(-1);
        }
        // Get directory to watch from commandline arg.
        String directoryToWatch = args[0];

        // Get objects which implement the Path and WatchService interface respectively. Register the
        // watchservice to the path, watch for files created, modified or deleted.
        Path path = Paths.get(directoryToWatch);
        WatchService watchService = null;
        try {
            watchService = path.getFileSystem().newWatchService();
            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Infinite loop waiting for events.
        while (true) {
            WatchKey key = null;
            try {
                // Receive a watchkey if one is queued, otherwise wait.
                key = watchService.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Return events, check their nature with a switch and act accordingly. 
            for (WatchEvent<?> event : key.pollEvents()) {
                switch (event.kind().name()) {
                    case "OVERFLOW":
                        System.out.println("Events lost");
                        break;
                    case "ENTRY_MODIFY":
                        System.out.println("File " + event.context() + " changed.");
                        break;
                    case "ENTRY_CREATE":
                        System.out.println("File " + event.context() + " created.");
                        break;
                    case "ENTRY_DELETE":
                        System.out.println("File " + event.context() + " deleted.");
                        break;
                }

            }
            // Put key back into ready state. reset method returns a boolean. False
            // indicates that the key is invalid. Either the key has been cancelled,
            // the directory is inaccessible or the watch service is closed. Break out
            // of infinite loop if this is the case.
            boolean valid = key.reset();
            if (!valid) {
                break;
            }

        }
    }
}
