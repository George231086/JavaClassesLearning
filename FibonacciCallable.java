import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author George
 */
// Class to demonstrate Callable interface. call method returns the nth Fibonacci
// number, where n is an instance variable set in the constructor of the FibonacciCallable
// object.
public class FibonacciCallable implements Callable<BigInteger> {

    private final int n;

    public FibonacciCallable(int n) {
        this.n = n;
    }

    public BigInteger fibonacci(int n) {
        BigInteger oldNum = BigInteger.ONE;
        BigInteger newNum = BigInteger.ONE;
        BigInteger temp;

        for (int i = 1; i < n - 1; i++) {
            temp = oldNum.add(newNum);
            oldNum = newNum;
            newNum = temp;
        }
        return newNum;
    }

    @Override
    public BigInteger call() {
        return fibonacci(n);
    }

    /**
     * @param args Program prints the first n Fibonacci numbers. Usage: [n] ,
     * where n is the number of Fibonacci numbers to print.
     */
    public static void main(String args[]) {
        if (args.length == 1) {
            try {

                int n = Integer.parseInt(args[0]);
                ExecutorService threadPool = Executors.newFixedThreadPool(10);
                List<Future<BigInteger>> futures = new ArrayList<>();

                // Submit a FibonacciCallable object for each i up to n to the thread pool and
                // add the resulting future to the futures list.
                for (int i = 1; i <= n; i++) {
                    futures.add(threadPool.submit(new FibonacciCallable(i)));
                }

                // get is called on the futures in the order of increasing n. This blocks until
                // computation is complete, forcing the results to be printed in the correct Fibonacci
                // sequence order.
                for (int i = 1; i <= futures.size(); i++) {
                    System.out.printf("Fibonacci Number %d: %s \n", i, futures.get(i - 1).get());
                }
                threadPool.shutdown();
            } catch (NumberFormatException e1) {
                System.out.println("Requires integer value > 0 !");
            } catch (InterruptedException | ExecutionException e2) {
                System.out.println("Error");

            }
        } else {
            System.out.println("Usage: [integer n] - prints first n Fibonacci numbers");
        }
    }

}
