package cards;

/**
 *
 * @author George
 */
public class Card {
    private String suit;
    private int val;
    
    Card(String suit, int val){
        this.suit = suit;
        this.val = val;
    }

    public String getSuit() {
        return suit;
    }

    public int getVal() {
        return val;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public void setVal(int val) {
        this.val = val;
    }
    
    @Override
    public String toString(){
        int val = this.val;
        String string;
        switch (val){
            default: string = this.val+" of "+this.getSuit();
                break;
            case 11: string= "Jack of "+this.getSuit();
                break;
            case 12: string= "Queen of "+this.getSuit();
                break;
            case 13: string= "King of "+this.getSuit();
                break;
            case 14: string= "Ace of "+this.getSuit();
                break;
        }
        return string;
    }
    
    
}
