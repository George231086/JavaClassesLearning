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

    
    public List<Integer> computePrimeDecomp(int x) {
        
        List<Integer> primeFactors = new ArrayList<>();
        for (int i = 2; i <= x; i++) {
            if (x % i == 0) {
                primeFactors.add(i);
                x /= i;
                i = 1;
            }
        }
        
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
