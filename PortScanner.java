/**
 *
 * @author George
 */
import java.net.Socket;
import java.io.IOException;

// Basic Portscanner object. Made it runnable so that the range of values to be
// scanned can be split between multiple threads and scanned in parallel.
public class PortScanner implements Runnable {

    private final String hostname;
    private int start = 1;
    private int finish = 10;

    public PortScanner(String hostname, int start, int finish) {
        this.hostname = hostname;
        this.start = start;
        this.finish = finish;
    }

    // Method to detect if port is open. Based on the assumption that if
    // we can connect to it, it is open, if not, it is not. In general
    // there may be other reasons why we cannot connect.
    public boolean portOpen(int portNumber) {
        try (Socket socket = new Socket(hostname, portNumber)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    // Scan between start and finish values given as instance 
    // variables in the constructor of the portScanner object.
    public void scan() {
        int portNumber = start;
        while (portNumber <= finish) {
            if (portOpen(portNumber)) {
                System.out.printf("Port %s is open \n", portNumber);
            } else {

            }
            portNumber++;
        }
    }
    
    // Get portScanner to start scanning.
    @Override
    public void run() {
        scan();
    }

    // Static method to split the work of scanning across multiple threads
    // in parallel. 
    public static void threadedPortScan(String hostname, int scanRangePerThread, int start, int finish) {
        // Divide the work, create and start the threads. The number of ports each thread will scan
        // is given as a parameter.
        int range = finish - start;
        int numberOfThreads = range / scanRangePerThread;
        for (int i = 0; i < numberOfThreads; i++) {
            int startPosForThread = start + i * scanRangePerThread;
            int finishPosForThread;
            if (i != numberOfThreads - 1) {
                finishPosForThread = startPosForThread + scanRangePerThread - 1;
            } else {
                finishPosForThread = startPosForThread + scanRangePerThread + range % numberOfThreads;
            }
            new Thread(new PortScanner(hostname, startPosForThread,
                    finishPosForThread)).start();
        }
    }

    public static void main(String args[]) {
        // Check which of the ports in the range 1 to 2000 are open on the localhost.
        // Share the work between multiple threads, each thread except the last checking 3 ports. 
        PortScanner.threadedPortScan("localhost", 3, 1, 2000);

    }

}
