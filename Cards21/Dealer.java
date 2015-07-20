package Cards21;

/**
 *
 * @author George
 */
public class Dealer extends Player {
    private Deck deck;
    

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }
    
    public Dealer(Deck deck){
        super("Dealer");
        this.deck=deck;
    }
    
    public void dealHand(Player player){
         player.setHand(deck.getHand());
    }
    
    public void dealCard(Player player){
         player.getHand().addCard(deck.dealCard());
    }
    
}
