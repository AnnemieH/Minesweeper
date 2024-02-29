package org.example;
import javax.swing.*;
import java.awt.*;
import java.applet.Applet;

public class GUI
{
    private JFrame frame = new JFrame ("Minesweeper");
    private void createBoard(Board board)
    {
        int[] dimensions = new int[]{board.getDimensions()[0], board.getDimensions()[1]};
        // Create the grid of the same dimensions as the board
        frame.setLayout(new GridLayout(dimensions[0], dimensions[1]));

        // Populate grid with buttons
        for (int i = 0;
             i < dimensions[0];
             ++i)
        {
            for (int j = 0;
                 j < dimensions[1];
                 ++j)
            {
                Tile currTile = board.getTileAt(new int[]{i, j});
                frame.add(currTile.tileButton);
                frame.setVisible(true);
            }
        }
    }

    public GUI(Board board)
    {
        frame = new JFrame ("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setVisible(true);

        createBoard(board);
    }
}
