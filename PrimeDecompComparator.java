import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author George
 *
 * Class to demonstrate the Comparator interface. We create a Comparator that
 * can be used to sort integers according to the number of elements in their
 * prime decomposition.
 *
 */
public class PrimeDecompComparator implements Comparator<Integer> {

    /**
     *
     * @param x
     * @param y
     *
     * Compare two integers by the number of primes in their prime
     * decomposition. E.g 11<9 as 11 is prime and 9=3*3.
     *
     */
    @Override
    public int compare(Integer x, Integer y) {
        //Here we compute the whole prime decomposition
        //of x and y. This in general may not be necessary.
        //As this class is mainly to demonstrate the comparator
        //interface, we won't worry about optimizations.

        List<Integer> xlist = computePrimeDecomp(x);
        List<Integer> ylist = computePrimeDecomp(y);
        int diff = xlist.size() - ylist.size();
        if (diff < 0) {
            return -1;
        } else if (diff == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     *
     * @param x number to find prime decomposition of.
     * @return
     *
     * Computes the prime decomposition of a given integer. It returns a list of
     * the elements in the decomposition, including repeats. E.g applying the
     * method to 11 and 9 will return [11] and [3,3] respectively.
     *
     *
     */
    public List<Integer> computePrimeDecomp(int x) {
        //int originalInt = x;
        List<Integer> primeFactors = new ArrayList<>();
        for (int i = 2; i <= x; i++) {
            if (x % i == 0) {
                primeFactors.add(i);
                x /= i;
                i = 1;
            }
        }
        /*int check = 1;
         for (int primeFactor : primeFactors){
         check*=primeFactor;
         }
         System.out.println(originalInt-check);*/
        return primeFactors;
    }

    public static void main(String[] args) {
        int n = 10000;
        List<Integer> numbers = new ArrayList(n);
        for (int i = 2; i < n; i++) {
            numbers.add(i);
        }
        PrimeDecompComparator p = new PrimeDecompComparator();
        Collections.sort(numbers, p);

        //Can use functional foreach from Java 8.
        numbers.stream().forEach((number) -> {

            List<Integer> factors = p.computePrimeDecomp(number);
            Collections.sort(factors);
            System.out.println("Number:\t" + number + "\tPrime decomp:\t" + factors);
        });

    }

}
