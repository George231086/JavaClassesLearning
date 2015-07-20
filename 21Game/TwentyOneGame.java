package cards;

import java.util.Collections;
import java.util.Scanner;

public class TwentyOneGame{
    private Deck deck;
    private Dealer dealer; 
    private Player[] players;
    private static int lineNum = 20;

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }
    
    // Constructor to creat deck, dealer and the players.
    public TwentyOneGame(int playerNo){
        deck = new Deck();
        dealer = new Dealer(deck);
        players = new Player[playerNo];
        for (int i = 0; i < playerNo; i++)
            players[i] = new Player("Player " + (i+1) );
    }
    
    // Shuffle the deck and deal the hands.
    public void startGame() {
        Collections.shuffle(deck.getCards());
        for (Player player : players) {
            dealer.dealHand(player);
            
        }
        
    }
    
    // Method for player to take a turn.
    public void takeTurn(Player player){
        
        System.out.println(player.getName() + ":\n" + player.getHand().toString());
        
        // Check whether player is bust or has stuck.
        if (player.getBust())
            System.out.println("Status: Bust.");
        if (player.getStuck())
            System.out.println("Status: Stuck.");
        boolean chosen = false;
        
        // Give the choice to stick or twist, act accordingly. Keep offering 
        //this choice until player is bust or has stuck.
        while (!chosen) {
            if (!player.getStuck() && !player.getBust()) {
                System.out.println("\nStick or Twist? s/t");
                Scanner input = new Scanner(System.in);
                String choice = input.next();
                if (choice.equals("t")) {
                    dealer.dealCard(player);
                    player.printLastCard();
                    chosen = true;
                    if (player.goneBust()) {
                        player.setBust(true);
                        System.out.println(player.getName() + " is bust!"+" Handvalue: "+player.getHand().Handvalue());
                    }

                } else if (choice.equals("s")) {
                    player.setStuck(true);
                    System.out.println(player.getName() + " has stuck!"+" Handvalue: "+player.getHand().Handvalue());
                    chosen = true;
                } else {
                    System.out.println("Need to choose s or t !");
                }

            } else {
                // For a multiple player game have continue option so that the players
                // status cannot be deduced by the other players due to a lack of a turn.
                System.out.println("Continue: c?");
                Scanner input = new Scanner(System.in);
                String choice = input.next();
                if (choice.equals("c")) {
                    chosen = true;
                }
            }
        }
    }
    
    // Method to create space on the screen so that a player cannot see the
    // previous players turn.
    public static void createSpace(int n) {
        for (int i = 0; i < n; i++) {
            System.out.println("\n");
        }
    }
    
    // Method to simulate a round of player turns.
    public void playRound() {
        Player[] gamePlayers = players;
        for (Player player : gamePlayers) {
            takeTurn(player);
            System.out.println("");
            try {
                // Add a slight delay to make it more playable,
                // allows person to view their hand for longer.
                Thread.sleep(3000);
                createSpace(lineNum);
                if (playersActive()) {
                    System.out.println("Change Player!");
                    // Delay gives time to allow players to swap.
                    Thread.sleep(3000);
                    createSpace(lineNum);
                }
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Method to check whether there are any players left that need to
    // take a turn.
    public boolean playersActive() {
        Player[] gamePlayers = players;
        for (Player player : gamePlayers) {
            if (!player.getStuck() && !player.getBust()) {
                return true;
            }
        }
        return false;
    }
    
    // Method to handle multiple players, keep playing rounds until
    // all players have either stuck or are bust.
    public void playPlayersGame(){
        startGame();
        while (playersActive())
            playRound();
        
    }
    
    // Method to control one player game, keep looping taking a turn until
    // player is bust or has stuck.
    public void playOnePlayerGame() {
        startGame();
        Player player = players[0];
        while (!player.getBust() && !player.getStuck()) {
            System.out.println("");
            takeTurn(player);
        }
    }
    
    // Create a default winning player with hand value 0. Loop through the game players comparing
    // their hand value to the winning player's hand then update the winning player
    // if its hand has been beaten. Only the hand value of the non bust players is counted. 
    // Keep track of whether there is a draw or all players are bust so that the winning player is
    // the default player.
    public String findWinner(){
        Player[] gamePlayers = players;
        Player winningPlayer = new Player("default");
        Card[] defaultCards = new Card[1];
        defaultCards[0] = new Card("none",0);
        winningPlayer.setHand(new Hand(defaultCards));
        boolean draw = false;
        for (Player player : gamePlayers){
            if (!player.getBust() && player.getHand().Handvalue() >
                    winningPlayer.getHand().Handvalue()){
                winningPlayer = player;
            } else if (!player.getBust() && player.getHand().Handvalue() ==
                    winningPlayer.getHand().Handvalue())
                draw = true;
        }
        if (winningPlayer.getHand().Handvalue() == 0)
            return "House wins! All players bust!";
        else if (draw)
            return "We have a draw!";
        return winningPlayer.getName()+" wins!";
    }
    
    // Print results. Add delays to increase tension.
    public void printResult() {
        System.out.println("The results are....");
        try {
            Thread.sleep(1000);

            for (Player player : players) {
                if (player.getBust()) {
                    System.out.println(player.getName() + " is bust!");
                } else {
                    System.out.println(player.getName() + " has " + player.getHand().Handvalue());
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(findWinner());
    }
    
    // Method to simulate dealers go. The dealer takes turns until
    // either he has a winning hand or is bust.
    public void dealersTurn() {
        System.out.println("\nDealers turn: ");
        dealer.dealHand(dealer);
        System.out.println(dealer.getHand().toString());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (!dealer.getBust() && dealer.getHand().Handvalue()
                <= players[0].getHand().Handvalue() && dealer.getHand().Handvalue() != 21) {
            dealer.dealCard(dealer);
            dealer.printLastCard();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (dealer.goneBust()) {
                dealer.setBust(true);
            }
        }
        if (dealer.getBust()) {
            System.out.println("Dealer is bust! " + players[0].getName() + " wins!");
        } else if (dealer.getHand().Handvalue() == players[0].getHand().Handvalue()) {
            System.out.println("Draw!");
        } else {
            System.out.println("Dealer wins!" + " Handvalue: " + dealer.getHand().Handvalue());
        }
    }
    
    public static void main(String[] args){
        if (args.length<1)
            System.out.println("Need arguments: [player Number] [Optional: Number of lines to split players, needs"
                    + "to be > 0] ");
        if (args.length == 2 && Integer.parseInt(args[1]) > 0 )
            lineNum = Integer.parseInt(args[1]);
        int playerNum = Integer.parseInt(args[0]);
        
        // Handle multiplayer game.
        if (playerNum > 1){
            TwentyOneGame bjg = new TwentyOneGame(playerNum);
            bjg.playPlayersGame();
            bjg.printResult();
        } 
        //Handle single player game.
        else if (playerNum == 1) {
            TwentyOneGame bjg = new TwentyOneGame(1);
            bjg.playOnePlayerGame();
            if (bjg.players[0].getStuck()) {
                bjg.dealersTurn();
            } else if (bjg.players[0].goneBust()) {
                System.out.println("Dealer Wins");
            }
        }
        
        
        
        

    }
}