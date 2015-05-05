import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author George
 */
public class Consumer implements Runnable {
    private final Drop drop;
    private final BlockPanel blockPanel;


    public Consumer(Drop drop, int blockNo) {
        this.drop=drop;
        JFrame frame = new JFrame("Consumer");
        blockPanel = new BlockPanel(blockNo);
        frame.add(blockPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize =java.awt.Toolkit.getDefaultToolkit().getScreenSize();;
        frame.setBounds(screenSize.width/2,screenSize.height/4, 300, 300);
        frame.setVisible(true);
    }

    public void run() {
        while (true) {
            takeAndUseBlock();
            if (drop.isClosedForBusiness() && drop.isEmpty())
                break;
        }
    }

    public void takeAndUseBlock() {
        int[] blockCoords = drop.take();
        blockPanel.setBlockAlive(blockCoords[0],blockCoords[1]);
        blockPanel.repaint();
    }





