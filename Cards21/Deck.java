package Cards21;
import java.util.*;
/**
 *
 * @author George
 */


public class Deck {
    private LinkedList<Card> cards = new LinkedList<>();
    private static String[] suits = {"Hearts","Diamonds","Spades","Clubs"};
    
    public LinkedList<Card> getCards() {
        return cards;
    }

    public void setCards(LinkedList<Card> cards) {
        this.cards = cards;
    }
    
    public Deck(){
        for (String suit : suits){
            for (int i=2; i < 15; i++){
                cards.add(new Card(suit,i));
            }
        }
    }
    
    public Card dealCard(){
        return this.cards.poll();
    }
    
    public Hand getHand(){
        Card[] cards = new Card[2];
        cards[0] = this.cards.poll();
        cards[1] = this.cards.poll();
        return new Hand(cards);
    }
    
    
    
}

