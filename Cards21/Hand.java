package Cards21;

/**
 *
 * @author George
 */
import java.util.*;
public class Hand {
    //Array list to store the hand cards.
    private List<Card> handCards = new ArrayList<>();
    
    public List<Card> getHandCards() {
        return handCards;
    }

    public void setHandCards(List<Card> hand) {
        handCards = hand;
    }
    
    public Hand(Card[] cards){
        handCards.addAll(Arrays.asList(cards));
            
    }
    
    public void addCard(Card card){
        handCards.add(card);
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        Iterator ite = this.handCards.iterator();
        while (ite.hasNext())
            sb.append(ite.next().toString()+"\n");
        sb.deleteCharAt(sb.length()-1); //Delete final newline character.
        return sb.toString();
    }
    
    // Method to work out the possible combined values of the aces in 
    // the hand, bearing in mind that ace can be counted as high or low.
    public static int[] aceValues(int aceNum) {
        int[] values = new int[2];
        switch (aceNum) {
            default:
                values[0] = 0;
                values[0] = 0;
                break;
            case 1:
                values[0] = 1;
                values[1] = 11;
                break;
            case 2:
                values[0] = 2;
                values[1] = 12;
                break;
            case 3:
                values[0] = 3;
                values[1] = 13;
                break;
            case 4:
                values[0] = 4;
                values[1] = 14;
                break;
        }
        return values;

    }
    
    // Method to work out the hand value. It takes into account
    // the number of aces and chooses whether their values are high or low
    // in a way which maximises the hand value.
    public int Handvalue() {
        int sum = 0;
        int aceNum = 0;
        for (Card card : handCards) {
            if (card.getVal() >= 10 && card.getVal() != 14) {
                sum += 10;
            } else if (card.getVal() == 14) {
                aceNum += 1;
            } else {
                sum += card.getVal();
            }
        }
        int[] values = aceValues(aceNum);
        int handSum1 = sum+values[0];
        int handSum2 = sum+values[1];
        if (handSum1<22 && handSum2<22)
            sum = Math.max(handSum1, handSum2);
        else if (handSum1>21 && handSum2<22)
            sum = handSum2;
        else if (handSum1<22 && handSum2>21)
            sum = handSum1;
        else 
            sum = handSum1;
        return sum;
    }
}
