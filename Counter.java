/**
 *
 * @author George
 * 
 * A modification of the counter class from Oracles thread interference tutorial page
 * http://docs.oracle.com/javase/tutorial/essential/concurrency/interfere.html.
 * We implement the interface Runnable twice with anonymous classes, pass these
 * into threads and have one repeatedly increment the count instance variable whilst
 * the other decrements it. They both try to alter count the same number of
 * times so if there isn't any interference the final value for count will be 0.
 * 
 * 
 * 
 */

public class Counter {
    //count variable the threads will alter.
    private int count=0;
    
    //Implementations of Runnable that we'll fill our threads with.
    //They'll try to alter count 1000 times.
    
    private Runnable incrementer = new Runnable(){
        @Override
        public void run(){
            for (int i=0;i<1000;i++){
                increment();
            }
        }
    };
    
    private Runnable decrementer = new Runnable(){
        @Override
        public void run(){
            for (int i=0;i<1000;i++){
                decrement();
            }
        }
    };
    
    public void increment(){
        count++;
    }
    
    public void decrement(){
        count--;
    }
    
    //Create threads, start them, wait till they've both finished to
    //return count.
    
    public int startThreadsAndGetResult(){
        Thread i = new Thread(incrementer);
        Thread d = new Thread(decrementer);
        i.start();
        d.start();
        try {
            i.join();
            d.join();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
        return count;
    }
    
    
    
    //Run startThreadsAndGetResults 100 times, report the number of runs that return
    //the wrong count, ie the number of times thread interference has occurred. To eliminate
    //the interference we could use the synchronize key word when defining increment and
    //decrement.
    
    public static void main(String args[]){
        int incorrectCountNum = 0;
        for (int i = 0; i < 100; i++) {
            Counter c = new Counter();
            if (c.startThreadsAndGetResult() != 0) {
                incorrectCountNum++;
            }
        }
        System.out.println(incorrectCountNum);
    }
        
}
