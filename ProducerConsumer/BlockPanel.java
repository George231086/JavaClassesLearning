import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

/**
 *
 * @author George
 */
public class BlockPanel extends JPanel {
    private final Rectangle2D.Float[][] blocks;
    private final boolean[][] liveBlocks;
    private final int blockNo;

    public BlockPanel(int blockNo) {
        super();
        this.blockNo=blockNo;
        blocks = new Rectangle2D.Float[blockNo][blockNo];
        liveBlocks = new boolean[blockNo][blockNo];
    }

    public void createBlocks(float blockWidth, float blockHeight) {
        for (int i = 0; i < blockNo; i++) {
            for (int j = 0; j < blockNo; j++) {
                blocks[i][j] = new Rectangle2D.Float(i * blockWidth, j * blockHeight,
                                                     blockWidth, blockHeight);
            }
        }
    }

    public int getBlockNo() {
        return blockNo;
    }

    public void setBlockAlive(int x, int y) {
        liveBlocks[x][y]=true;
    }

    public void setBlockDead(int x, int y) {
        liveBlocks[x][y]=false;
    }

    public void reset() {

        for (int i = 0; i < blockNo; i++) {
            for (int j = 0; j < blockNo; j++) {
                setBlockDead(i, j);
            }
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics comp) {
        super.paintComponent(comp);
        Graphics2D comp2D = (Graphics2D) comp;
        float blockWidth = this.getSize().width / blockNo;
        float blockHeight = this.getSize().height /blockNo;
        this.createBlocks(blockWidth,blockHeight);
        for (int i=0; i<blockNo; i++) {
            for (int j=0; j<blockNo; j++) {
                if (liveBlocks[i][j]) {
                    comp2D.setColor(Color.GREEN);
                    comp2D.fill(blocks[i][j]);

                }

                // Redraw rectangles so border between them can be seen.
                comp2D.setColor(Color.BLACK);
                comp2D.draw(blocks[i][j]);
            }
        }
    }
}
