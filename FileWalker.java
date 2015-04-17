/**
 *
 * @author George
 *
 * Program to copy all pdfs from the Downloads directory to a a directory
 * DownloadPDFs in the Documents directory. Hard coded but could easily be
 * modified and generalised.
 *
 */
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

// Extend SimpleFileVisitor and override methods we want, saving the hassle
// of implementing the whole FileVisitor interface.
public class FileWalker extends SimpleFileVisitor<Path> {

    private PathMatcher matcher;
    private String destination = "C:\\Users\\George\\Documents\\DownloadPDFs";

    @Override
    public FileVisitResult visitFile(Path pathInput, BasicFileAttributes fileAttributes) {
        matcher = FileSystems.getDefault().getPathMatcher("glob:*.pdf");
        Path nameOfFile = pathInput.getFileName();
        Path dest = Paths.get(destination);
        if (matcher.matches(nameOfFile)) {
            try {
                Files.copy(pathInput, dest.resolve(pathInput.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File name: " + pathInput.getFileName() + " copied successfully");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return FileVisitResult.CONTINUE;
    }

    /**
     * *
     *
     * @param args
     */
    public static void main(String args[]) {

        Path p = Paths.get("C:\\Users\\George\\Downloads");
        try {
            Files.walkFileTree(p, new FileWalker());
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }
}    

