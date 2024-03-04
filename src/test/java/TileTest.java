import org.junit.jupiter.api.*;
import org.example.*;

import java.util.Arrays;

public class TileTest
{
    @Test
    public void testTileInitialisationwithMouseListener()
    {
        Board board = new Board(new int[]{5, 5}, 10);
        int[] coords = new int[]{1,2};
        Tile newTile = new Tile(board, coords);

        Assertions.assertEquals(2, newTile.tileButton.getMouseListeners().length, "Button has listeners");
    }
}
