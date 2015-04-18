/**
 *
 * @author George
 *
 * Program to find files of a given extension, in a given directory and copy them
 * to a target directory.
 *
 */
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

// Extend SimpleFileVisitor and override methods we want, saving the hassle
// of implementing the whole FileVisitor interface.
public class FileFindAndCopy extends SimpleFileVisitor<Path> {

    private PathMatcher matcher;
    private final String destinationDirectory;
    private final String fileExtension;
    
    public FileFindAndCopy(String fileExtension, String destinationDirectory ){
        this.destinationDirectory = destinationDirectory;
        this.fileExtension = fileExtension;
        
    }
    
    // Method called when file is visited. 
    @Override
    public FileVisitResult visitFile(Path pathInput, BasicFileAttributes fileAttributes) {
        
        // Create matcher for given extension.
        matcher = FileSystems.getDefault().getPathMatcher("glob:*."+fileExtension);
        
        Path nameOfFile = pathInput.getFileName();
        Path dest = Paths.get(destinationDirectory);
        
        // Create directory if it doesn't exist.
        if (dest.toFile().mkdir())
            System.out.println("Directory created!");
        
        // Copy file if it matches file extension.
        if (matcher.matches(nameOfFile)) {
            try {
                Files.copy(pathInput, dest.resolve(pathInput.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File name: " + pathInput.getFileName() + " copied successfully");

            } catch (IOException e) {
                System.out.println("Could not copy "+pathInput.getFileName());
            }
        }
        return FileVisitResult.CONTINUE;
    }

    /**
     * *
     *
     * @param args Extension, sourceDirectory, destinationDirectory
     */
    public static void main(String args[]) {
        if (args.length != 3) {
            System.out.println("Needs 3 arguments: Extension (eg txt), sourceDirectory,"
                    + " destinationDirectory ");
            System.exit(-1);
        } else {
            if (Files.isDirectory(Paths.get(args[1]), LinkOption.NOFOLLOW_LINKS)) {

                try {
                    Files.walkFileTree(Paths.get(args[1]), new FileFindAndCopy(args[0], args[2]));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("walkFileTree problem!");
                }
            } else {
                System.out.println("argument sourceDirectory needs to be a directory!");
                System.exit(-1);
            }
        }
    }
}
