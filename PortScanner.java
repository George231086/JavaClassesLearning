import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author George
 */
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

    @Override
    public void run() {
        scan();
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

    /**
     * *
     *
     * @param args Program to look for open ports on a given host. Usage:
     * [hostname\IP] [Starting port] [Finishing port] [Port range per thread] 
     * eg: localhost 1 2000 10 
     * The work is split between multiple threads, so that
     * the port range is scanned in parallel.
     */
    public static void main(String args[]) {

        if (args.length == 4) {
            try {
                InetAddress address = InetAddress.getByName(args[0]);
                PortScanner.threadedPortScan(args[0], Integer.parseInt(args[3]),
                        Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            } catch (NumberFormatException e1) {
                System.out.println("Arguments 2,3 and 4 must be integers!");
            } catch (UnknownHostException e2) {
                System.out.println("Hostname not known!");
            }

        } else {
            System.out.println("Usage: [hostname\\IP] [Starting port] [Finishing port] [Port range per thread]");
        }

    }

}
