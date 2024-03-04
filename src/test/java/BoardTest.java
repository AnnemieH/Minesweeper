import org.junit.jupiter.api.*;
import org.example.*;

import java.io.File;

public class BoardTest
{
    @Test
    public void BoardInitialisation()
    {
        Board board = new Board(new int[]{11, 12}, 40);

        String[] form = board.getBoardForm();
        int emptySpaces = 0;
        int nullSpaces = 0;

        for (String f : form)
        {
            if (f.equals(""))
            {
                ++emptySpaces;
            }
            else if (f.equals(null))
            {
                ++nullSpaces;
            }
        }

        Assertions.assertEquals(0, emptySpaces, "There are more than 0 empty spaces");
        Assertions.assertEquals(0, nullSpaces, "There are more than 0 empty spaces");
        Assertions.assertEquals(40, board.getTotalMines(), "Oh no, mines are wrong");
        Assertions.assertEquals(11, board.getBoundingBox()[0], "Whoops, too much x dimension");
        Assertions.assertEquals(12, board.getBoundingBox()[1], "Whoops, too much y dimension");
    }
    @Test
    public void BoardInitialisationfromMap()
    {
        File loadedMap = new File("src/main/resources/Maps/loss.csv");
        Board board = Main.loadBoard(loadedMap, 40);

        String[] form = board.getBoardForm();
        int emptySpaces = 0;
        int nullSpaces = 0;

        for (String f : form)
        {
            if (f.equals(""))
            {
                ++emptySpaces;
            }
            else if (f.equals(null))
            {
                ++nullSpaces;
            }
        }

        Assertions.assertEquals(0, emptySpaces, "There are more than 0 empty spaces");
        Assertions.assertEquals(0, nullSpaces, "There are more than 0 empty spaces");
        Assertions.assertEquals(40, board.getTotalMines(), "Oh no, mines are wrong");
        Assertions.assertEquals(11, board.getBoundingBox()[0], "Whoops, too much x dimension");
        Assertions.assertEquals(11, board.getBoundingBox()[1], "Whoops, too much y dimension");
    }
}
