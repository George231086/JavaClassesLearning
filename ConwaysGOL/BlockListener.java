package ConwaysGOL;

/**
 *
 * @author George
 */
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BlockListener extends MouseAdapter {
    private final BlockBoard blockBoard;
    
    // We pass the blockBoard to the blockListener.
    public BlockListener(BlockBoard blockBoard){
        this.blockBoard=blockBoard;
    }
    
    // When the mouse is clicked we determine which block the pointer lies inside
    // then set that block alive if the board is enabled. The board is then repainted.
    @Override
    public void mouseClicked(MouseEvent e) {
        float blockWidth = blockBoard.getWidth() / blockBoard.getBlockNo();
        float blockHeight = blockBoard.getHeight() / blockBoard.getBlockNo();
        int iIndex = (int) (e.getX() / blockWidth);
        int jIndex = (int) (e.getY() / blockHeight);
        if (blockBoard.isBoardEnabled() && iIndex>0 && iIndex<blockBoard.getBlockNo()-1
                && jIndex>0 && jIndex<blockBoard.getBlockNo()-1) {
            if (!blockBoard.isAlive(iIndex, jIndex)) {
                blockBoard.setBlockAlive(iIndex, jIndex);
            } else {
                blockBoard.setBlockDead(iIndex, jIndex);
            }
            blockBoard.repaint();
        }
    }
}
