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

    public void endGame(Boolean isWon)
    {
        JFrame frame = new JFrame ();
        JLabel message = new JLabel();
        Graphics graphics;
        // Check whether the game is won or lost and alter message depending
        if ( isWon )
        {
            frame.setTitle("So fetch");
            message.setText("Lukas will be delivering your victory cheque at 5P.M. ... maybe.");
        }
        else
        {
            frame.setTitle("It's okay babes, you tried");
            message.setText("Too bad, so sad.");
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);

        GridBagLayout endGame = new GridBagLayout();
        GridBagConstraints endGameConstraints = new GridBagConstraints();
        frame.setLayout(endGame);

        endGameConstraints.fill = GridBagConstraints.HORIZONTAL;

        // Set constraints for message
        message.setHorizontalAlignment(SwingConstants.CENTER);
        endGameConstraints.weightx = 0.9;
        endGameConstraints.gridx = 0;
        endGameConstraints.gridy = 0;
        endGameConstraints.gridwidth = 3;
        endGame.setConstraints(message, endGameConstraints);
        frame.add(message);

        String restartText = "Restart";
        JButton restartButton = new JButton(restartText);
        String quitText = "Quit";
        JButton quitButton = new JButton( quitText );

        // Set constraints for restartButton
        endGameConstraints.weightx = 0.5;
        endGameConstraints.gridx = 0;
        endGameConstraints.gridy = 1;
        endGameConstraints.gridwidth = 1;
        endGame.setConstraints(restartButton, endGameConstraints);
        //Rectangle2D bounds =  graphics.getFontMetrics().getStringBounds(restartText, graphics);
        // restartButton.setMaximumSize(bounds.getSize());
        frame.add(restartButton);

        // Set constraints for quitButton
        endGameConstraints.weightx = 0.5;
        endGameConstraints.gridx = 2;
        endGameConstraints.gridy = 1;
        endGameConstraints.gridwidth = 1;
        endGame.setConstraints(quitButton, endGameConstraints);
        frame.add(quitButton);


        frame.setVisible(true);
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
