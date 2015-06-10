import java.awt.Dimension;
import java.util.Random;
import javax.swing.JFrame;
import java.awt.Toolkit;

/**
 *
 * @author George
 */
public class Producer implements Runnable {
    private final Drop drop;
    private final BlockPanel blockPanel;
    private final Random random = new Random();
    private final boolean[][] blocksProduced;


    public Producer(Drop drop, int blockNo) {
        this.drop=drop;
        JFrame frame = new JFrame("Producer");
        blockPanel = new BlockPanel(blockNo);
        blocksProduced = new boolean[blockNo][blockNo];
        frame.add(blockPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds(screenSize.width/4,screenSize.height/4, 300, 300);
        frame.setVisible(true);
    }

    @Override
    public void run() {
        while (true) {
            produceAndPutBlock();
            if (drop.isClosedForBusiness()) {
                break;
            }

        }
    }

    public void produceAndPutBlock() {
        int[] blockCoords = blockToLive();
        int xpos = blockCoords[0];
        int ypos = blockCoords[1];
        blockPanel.setBlockAlive(xpos, ypos);
        blocksProduced[xpos][ypos] = true;
        if (producedAllBlocks()) {
            drop.closeDown();
        }
        blockPanel.repaint();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        drop.put(xpos, ypos);
        blockPanel.reset();
    }

    public int[] blockToLive() {
        int blockNo = blockPanel.getBlockNo();
        int[] xycoords = {random.nextInt(blockNo), random.nextInt(blockNo)};
        return xycoords;
    }

    public boolean producedAllBlocks() {
        int liveNo = 0;
        int blockNo = blockPanel.getBlockNo();
        for (int i = 0; i < blockNo; i++) {
            for (int j = 0; j < blockNo; j++) {
                if (blocksProduced[i][j]) {
                    liveNo++;
                }
            }
        }
        return (liveNo == blockNo * blockNo);
    }





}
