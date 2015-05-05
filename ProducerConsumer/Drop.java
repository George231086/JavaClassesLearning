import java.util.Arrays;

/**
 *
 * @author George
 */
public class Drop {
    private final int[] data = new int[2];
    private boolean empty=true;
    private boolean closedForBusiness = false;

    public synchronized int[] take() {
        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        int[] dataTaken = Arrays.copyOf(data,data.length);
        Arrays.fill(data,0);
        empty = true;
        notifyAll();
        return dataTaken;

    }

    public synchronized void put(int xpos, int ypos) {
        while (!empty) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        data[0] = xpos;
        data[1] = ypos;
        empty = false;
        notifyAll();
    }

    public void closeDown() {
        closedForBusiness = true;
    }

    public boolean isClosedForBusiness() {
        return closedForBusiness;
    }

    public boolean isEmpty() {
        return empty;
    }

    public static void main(String args[]) throws Exception {
        Drop d = new Drop();
        Producer p = new Producer(d,10);
        Consumer c = new Consumer(d,10);
        Thread t1 = new Thread(p);
        Thread t2 = new Thread(c);
        t1.start();
        t2.start();
    }
}
