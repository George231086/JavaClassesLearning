package cards;

import java.util.List;
/**
 *
 * @author George
 */
public class Player {
    private Hand hand;
    private boolean stuck;
    private boolean bust;
    private final String name;

    public boolean getBust() {
        return bust;
    }

    public void setBust(boolean bust) {
        this.bust = bust;
    }

    public boolean getStuck() {
        return stuck;
    }

    public void setStuck(boolean stuck) {
        this.stuck = stuck;
    }
    
    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }
    
    public String getName(){
        return name;
    }
    
    public Player(String name){
        this.name = name;
    }
    
    public void printLastCard(){
        List<Card> tmp = hand.getHandCards();
        System.out.println(tmp.get(tmp.size() - 1));
    }
    
    public boolean goneBust(){
        return getHand().Handvalue()>21;
    }
}
