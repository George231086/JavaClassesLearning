package ConwaysGOL;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.awt.BorderLayout;

/**
 *
 * @author George
 */
public class GameOfLife implements ActionListener, Runnable {

    private final JButton goButton;
    private final JButton resetButton;
    private final JButton randomButton;
    private final BlockBoard blockBoard;
    private ScheduledExecutorService scheduledExecutorService;
    
    // Constructor to set up gui, add the buttons and blockboard.
    public GameOfLife(int blockNo) {
        JFrame f = new JFrame("Conway's Game Of Life!");

        blockBoard = new BlockBoard(blockNo);
        JPanel buttonPane = new JPanel();
        JPanel mainPane = new JPanel();

        goButton = new JButton("Go!");
        resetButton = new JButton("Reset");
        randomButton = new JButton("Random seed");

        buttonPane.add(goButton);
        buttonPane.add(resetButton);
        buttonPane.add(randomButton);

        mainPane.setLayout(new BorderLayout());
        mainPane.add(blockBoard);
        mainPane.add(buttonPane, BorderLayout.SOUTH);

        f.add(mainPane);
        f.setSize(500, 500);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public void addActionListeners() {
        goButton.addActionListener(this);
        resetButton.addActionListener(this);
        randomButton.addActionListener(this);
    }
    
    // If go button is pressed we disable the board and start it updating.
    // If a scheduledExecutorService is in existence, we check whether it's
    // finished running. If it has and there are live blocks on the board, we set it running.
    // If there are no live blocks we enable the board so that new blocks can be chosen.
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == goButton) {
            blockBoard.setBoardEnabled(false);
            // These checks prevents a new instance of a ScheduledExecutorService
            // being created by rungame() whenever go is pressed.
            if (scheduledExecutorService == null) {
                runGame();
            } else {
                if (scheduledExecutorService.isTerminated()) {
                    if (!blockBoard.allBlocksDead()) {
                        runGame();

                    } else {
                        blockBoard.setBoardEnabled(true);
                    }
                }
            }
        } // Reset board and stop the executor service. 
        if (e.getSource() == resetButton) {
            blockBoard.reset();
            if (scheduledExecutorService != null) {
                scheduledExecutorService.shutdown();
            }
        } // Randomly seed board, allowed even if board is being updated.
        if (e.getSource() == randomButton) {
            blockBoard.randomSeed();
        }
    }
    
    // Update the board. If all blocks are dead then shut down the executor service
    // and enable the board so new blocks can be set live
    @Override
    public void run() {
        if (!blockBoard.allBlocksDead()) {
            blockBoard.updateBoard();
        } else {
            scheduledExecutorService.shutdown();
            blockBoard.setBoardEnabled(true);
            
        }
    }
    
    // The executor service will update the board every second.
    public void runGame() {
        scheduledExecutorService
                = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(
                this, 0, 1, TimeUnit.SECONDS);

    }

    
   
     
    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                int blockNo = Integer.parseInt(args[0]);
                new GameOfLife(blockNo).addActionListeners();
            } catch (NumberFormatException e) {
                System.out.println("If argument is given then it needs to be an integer! "
                        + "Default block number is 20");
            }
        } else {
            // If no command line argument is given default to 20 by 20
            new GameOfLife(20).addActionListeners();
        }
    }

}
