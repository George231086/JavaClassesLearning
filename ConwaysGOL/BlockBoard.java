package ConwaysGOL;

import java.awt.Color;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

/**
 *
 * @author George
 */
public class BlockBoard extends JPanel {

    private Rectangle2D.Float[][] blocks;
    private boolean[][] liveBlocks;
    private boolean boardEnabled = true;
    private int blockNo ;
    
    
    public void setBoardEnabled(boolean boardEnabled) {
        this.boardEnabled = boardEnabled;
    }

    public void setBlockNo(int blockNo) {
        this.blockNo = blockNo;
    }

    public boolean isBoardEnabled() {
        return boardEnabled;
    }

    public int getBlockNo() {
        return blockNo;
    }
    
    
    public Rectangle2D.Float[][] getBlocks() {
        return blocks;
    }

    public boolean[][] getLiveBlocks() {
        return liveBlocks;
    }

    public void setBlocks(Rectangle2D.Float[][] blocks) {
        this.blocks = blocks;
    }

    public void setLiveBlocks(boolean[][] liveBlocks) {
        this.liveBlocks = liveBlocks;
    }
    
    // Check if all blocks are dead
    public boolean allBlocksDead() {
        for (int i = 0; i < blockNo; i++) {
            for (int j = 0; j < blockNo; j++) {
                if (liveBlocks[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    // Constructor to create blockboard and add a blocklistener,
    // this is an object derived from a MouseAdapter. We override
    // the mouse clicked method to set blocks alive.
    public BlockBoard(int blockNo) {
        super();
        this.blockNo=blockNo;
        blocks = new Rectangle2D.Float[blockNo][blockNo];
        liveBlocks = new boolean[blockNo][blockNo];
        BlockListener blistener = new BlockListener(this);
        this.addMouseListener(blistener);
    }
    
    public void setBlockAlive(int x, int y){
        liveBlocks[x][y]=true;
    }
    
    public void setBlockDead(int x, int y){
        liveBlocks[x][y]=false;
    }
    
    public boolean isAlive(int x,int y){
        return liveBlocks[x][y];
    }
    
    // Fill the board with blocks
    public void createBlocks(float blockWidth, float blockHeight) {
        for (int i = 0; i < blockNo; i++) {
            for (int j = 0; j < blockNo; j++) {
                blocks[i][j] = new Rectangle2D.Float(i * blockWidth, j * blockHeight,
                        blockWidth, blockHeight);
            }
        }
    }
    
    // Calculate how many neighbours are alive.
    public int liveNeighbourNo(int i, int j) {
        int count = 0;
        if (i > 0 && i < blockNo - 1 && j > 0 && j < blockNo - 1) {
            if (liveBlocks[i][j + 1]) {
                count++;
            }
            if (liveBlocks[i + 1][j + 1]) {
                count++;
            }
            if (liveBlocks[i + 1][j]) {
                count++;
            }
            if (liveBlocks[i + 1][j - 1]) {
                count++;
            }
            if (liveBlocks[i][j - 1]) {
                count++;
            }
            if (liveBlocks[i - 1][j - 1]) {
                count++;
            }
            if (liveBlocks[i - 1][j]) {
                count++;
            }
            if (liveBlocks[i - 1][j + 1]) {
                count++;
            }
        }
        return count;
    }
    
    // Determine whether a block should live based on the number of
    // live neighbours.
    public boolean shouldLive(int i, int j) {
        boolean answer = false;
        int liveNeighbours = liveNeighbourNo(i, j);
        if (liveBlocks[i][j]) {
            answer = liveNeighbours == 2 || liveNeighbours == 3;
        } else {
            if (liveNeighbours == 3 && i > 0 && i < blockNo - 1 && j > 0 && j < blockNo - 1 ) {
                answer = true;
            }
        }
        return answer;
    }
    
    // Updates the board giving the new states of the blocks, then repaints the panel.
    public void updateBoard() {
       
            boolean[][] newBlockStatus = new boolean[blockNo][blockNo];
            for (int i = 0; i < blockNo; i++) {
                for (int j = 0; j < blockNo; j++) {
                    newBlockStatus[i][j] = shouldLive(i, j);

                }

            }
            liveBlocks = newBlockStatus;
            repaint();
        
    }
    
    // Set all the status of all blocks to dead and repaint. Enable the board
    // so that new blocks can be set alive.
    public void reset() {

        for (int i = 0; i < blockNo; i++) {
            for (int j = 0; j < blockNo; j++) {
                setBlockDead(i, j);
            }
        }
        
        boardEnabled = true;
        repaint();
    }
    
    // Randomly set around half of the blocks alive and repaint.
    public void randomSeed() {
        Random r = new Random();

        for (int i = 1; i < blockNo ; i++) {
            for (int j = 1; j < blockNo ; j++) {
                if (!liveBlocks[i][j] && r.nextInt(2) == 1) {
                    liveBlocks[i][j] = true;
                }
            }
        }
        repaint();
    }
    
    // Draw the blocks, paint them green if alive, leave them the background panel colour if dead.
    // The border blocks are painted red, they are fixed as dead..
    @Override
    public void paintComponent(Graphics comp) {
        super.paintComponent(comp);
        Graphics2D comp2D = (Graphics2D) comp;
        float blockWidth = this.getSize().width / blockNo;
        float blockHeight = this.getSize().height /blockNo;
        this.createBlocks(blockWidth,blockHeight);
        for (int i=0;i<blockNo;i++) {
            for (int j=0;j<blockNo;j++) {
                if (liveBlocks[i][j]){
                    comp2D.setColor(Color.GREEN);
                    comp2D.fill(blocks[i][j]);
                    
                }
                if (i == 0 || i == blockNo - 1 || j == 0 || j == blockNo - 1){
                    comp2D.setColor(Color.RED);
                    comp2D.fill(blocks[i][j]);
                }
                // Redraw rectangles so border between them can be seen.
                comp2D.setColor(Color.BLACK);
                comp2D.draw(blocks[i][j]);
            }
        }
    }

}
