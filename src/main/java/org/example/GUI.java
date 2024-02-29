package org.example;
import javax.swing.*;
import java.awt.*;
import java.applet.Applet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class GUI
{
    private JFrame frame = new JFrame ("Minesweeper");
    private JFrame gameEndFrame = new JFrame("");
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
        gameEndFrame = new JFrame ();
        JLabel message = new JLabel();
        // Check whether the game is won or lost and alter message depending
        if ( isWon )
        {
            gameEndFrame.setTitle("So fetch");
            message.setText("Lukas will be delivering your victory cheque at 5P.M. ... maybe.");
        }
        else
        {
            gameEndFrame.setTitle("It's okay babes, you tried");
            message.setText("Too bad, so sad.");
        }

        gameEndFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameEndFrame.setSize(300, 300);

        GridBagLayout endGame = new GridBagLayout();
        GridBagConstraints endGameConstraints = new GridBagConstraints();
        gameEndFrame.setLayout(endGame);

        endGameConstraints.fill = GridBagConstraints.HORIZONTAL;

        // Set constraints for message
        message.setHorizontalAlignment(SwingConstants.CENTER);
        endGameConstraints.weightx = 0.9;
        endGameConstraints.gridx = 0;
        endGameConstraints.gridy = 0;
        endGameConstraints.gridwidth = 3;
        endGame.setConstraints(message, endGameConstraints);
        gameEndFrame.add(message);

        String restartText = "Restart";
        JButton restartButton = new JButton(restartText);
        String quitText = "Quit";
        JButton quitButton = new JButton( quitText );

        // Set constraints for restartButton
        endGameConstraints.weightx = 0.0;
        endGameConstraints.gridx = 0;
        endGameConstraints.gridy = 1;
        endGameConstraints.gridwidth = 1;
        endGame.setConstraints(restartButton, endGameConstraints);
        restartButton.setPreferredSize(new Dimension (100, 30 ));
        gameEndFrame.add(restartButton);

        // Set constraints for quitButton
        endGameConstraints.weightx = 0.0;
        endGameConstraints.gridx = 2;
        endGameConstraints.gridy = 1;
        endGameConstraints.gridwidth = 1;
        endGame.setConstraints(quitButton, endGameConstraints);
        quitButton.setPreferredSize(new Dimension (100, 30 ));
        gameEndFrame.add(quitButton);

        // Add a box between them as a separator
        Box spacer = new Box(BoxLayout.X_AXIS);
        endGameConstraints.weightx = 1.0;
        endGameConstraints.gridx = 1;
        endGameConstraints.gridy = 1;
        endGameConstraints.gridwidth = 1;
        endGame.setConstraints(spacer, endGameConstraints);
        gameEndFrame.add(spacer);

        // Add some action listeners to the buttons
        restartButton.addActionListener(restartListener);
        quitButton.addActionListener(quitListener);


        gameEndFrame.setVisible(true);
    }


    // CONSTRUCTORS
    public GUI(Board board)
    {
        frame = new JFrame ("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setVisible(true);

        createBoard(board);
    }

    // DESTRUCTORS
    // Close windows
    public void close()
    {
        close(frame);
        close(gameEndFrame);
    }
    public void close(JFrame frameToClose)
    {
        // Make sure that window is just closed, this isn't a programme quit
        frameToClose.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameToClose.dispatchEvent(new WindowEvent(frameToClose, WindowEvent.WINDOW_CLOSING));
    }


    // LISTENERS
    ActionListener restartListener = new ActionListener()
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Main.startGame();
        }
    };

    ActionListener quitListener = new ActionListener()
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Main.quit();
        }
    };
}
