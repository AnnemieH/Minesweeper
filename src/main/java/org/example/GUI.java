package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Arrays;

public class GUI
{
    private JFrame frame = new JFrame ("Minesweeper");
    private JFrame gameEndFrame = new JFrame("");
    private Board board;
    private static JLabel minesRemaining = new JLabel("0");
    // Keep track of the slice we're showing
    private int[] currSlice;
    private JPanel boardPanel;
    private JPanel[] slicePanels;
    private JPanel[] createBoards(Board board)
    {
        // Create an array of panels whose length is equal to the product of all but the first two dimensions
        JPanel[] boardPanels = new JPanel[board.boundingProd()/(board.getBoundingBox()[0] * board.getBoundingBox()[1])];
        int[] dimensions = Arrays.copyOf(board.getBoundingBox(), board.getBoundingBox().length);


        // Create the grid of the same dimensions as the first two dimensions of the board
        // Set every boardpanel to have this layout
        for( int k = 0;
             k < boardPanels.length;
             ++k )
        {
            boardPanels[k] = new JPanel();
            boardPanels[k].setLayout(new GridLayout(dimensions[0], dimensions[1]));

            // Populate grid with buttons
            for (int j = 0;
                 j < dimensions[1];
                 ++j)
            {
                for (int i = 0;
                     i < dimensions[0];
                     ++i)
                {
                    // Remember to add k * boundingBox[0] * boundingBox[1] to the total index of this tile
                    int currIndex = k * board.getBoundingBox()[0] * board.getBoundingBox()[1]
                            + j * board.getBoundingBox()[0] + i;
                    //Tile currTile = board.getTileAt(new int[]{i, j});
                    Tile currTile = board.getTileAt(board.indexToCoords(currIndex));
                    boardPanels[k].add(currTile.tileButton);
                    boardPanels[k].setVisible(true);
                }
            }
        }
        return boardPanels;
    }

    public void endGame(Boolean isWon)
    {
        gameEndFrame = new JFrame ();
        JLabel message = new JLabel();
        // Check whether the game is won or lost and alter message depending
        if ( isWon )
        {
            gameEndFrame.setTitle("So fetch");
            message.setText("Lukas will be delivering your victory post-it at 5P.M. ... maybe.");
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

    public static void updateMinesRemaining ( int minesLeft )
    {
        minesRemaining.setText(Integer.toString(minesLeft));
    }

    private int coordsToIndex (int[] coords )
    {
        // If coords is empty, return 0
        if ( coords.length == 0 )
        {
            return 0;
        }

        int[] boundingBox = board.getBoundingBox();

        // If coords[0] is out of bounds, return -1
        if ( coords[0] < 0 || coords[0] >= boundingBox[0] )
        {
            return -1;
        }
        // Remove the first element from coords for recursion and store in strippedCoords
        int[] strippedCoords = new int[coords.length - 1];

        for ( int i : strippedCoords )
        {
            strippedCoords[i] = coords[i + 1];
        }

        // Remove the first element from BoundingBox for recursion
        int[] strippedBoundingBox = new int[boundingBox.length - 1];
        for ( int i : strippedBoundingBox )
        {
            strippedBoundingBox[i] = boundingBox[i + 1];
        }

        // Return the appropriate index value, making sure that if it's negative, we set it to -1
        return Math.max(coords[0] + (boundingBox[0] * coordsToIndex(strippedCoords, strippedBoundingBox)), -1);
    }
    private int coordsToIndex(int[] coords, int[] remBoundingBox)
    {
        // If coords is empty, return 0
        if ( coords.length == 0 )
        {
            return 0;
        }

        // If coords[0] is out of bounds, return -1
        if ( coords[0] < 0 || coords[0] >= remBoundingBox[0] )
        {
            return -1;
        }
        // Remove the first element from coords for recursion and store in strippedCoords
        int[] strippedCoords = new int[coords.length - 1];

        for ( int i : strippedCoords )
        {
            strippedCoords[i] = coords[i + 1];
        }

        // Remove the first element from remBoundingBox for recursion
        int[] strippedBoundingBox = new int[remBoundingBox.length - 1];
        for ( int i : strippedBoundingBox )
        {
            strippedBoundingBox[i] = remBoundingBox[i + 1];
        }

        // Return the appropriate index value, making sure that if it's negative, we set it to -1
        return Math.max(coords[0] + (remBoundingBox[0] * coordsToIndex(strippedCoords)), -1);
    }

    // Change the currently-viewed slice
    private void updateSlice()
    {
/*        // Clear boardPanel
        boardPanel.removeAll();
        // Add in current slice to boardPanel
        boardPanel.add(slicePanels[coordsToIndex(currSlice)]);*/
        Object currSliceLayout = boardPanel.getLayout();

        if ( currSliceLayout instanceof CardLayout )
        {
            ((CardLayout) currSliceLayout).show(boardPanel, Integer.toString(coordsToIndex(currSlice)));
            boardPanel.setLayout((CardLayout) currSliceLayout);

        }
    }

    // Add buttons to change the viewed map slice along various dimension axes
    private JPanel dimensionSelector ()
    {
        JPanel dimButtons = new JPanel();
        GroupLayout dimLayout = new GroupLayout(dimButtons);
        GroupLayout.SequentialGroup dimPanelSeqGroup = dimLayout.createSequentialGroup();
        GroupLayout.ParallelGroup dimPanelParGroup = dimLayout.createParallelGroup(GroupLayout.Alignment.CENTER);

        // Instantiäte currSlice
        currSlice = new int[board.getBoardDimensions() - 2];
        Arrays.fill(currSlice, 0);

        // For dimensions higher than 2, add selector buttons to the panel
        for ( int i = 2;
              i < board.getBoardDimensions();
              ++i )
        {
            // Label the button
            String labelString = "Z" + (i - 1);
            JLabel dimLabel = new JLabel(labelString);
            JButton leftButton = new JButton("<");
            JButton rightButton = new JButton(">");
            JLabel currDimSlice = new JLabel("0");

            // Add actionListeners to the buttons
            leftButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    // Subtract one from the appropriate dimension
                    int dimNumber = Integer.parseInt(labelString.substring(1));
                    int newSliceInt = currSlice[dimNumber - 1];
                    --newSliceInt;

                    // if the new value is less than 0, set it to wrap around
                    if ( newSliceInt < 0 )
                    {
                        newSliceInt += board.getBoundingBox()[dimNumber + 1];
                        newSliceInt %= board.getBoundingBox()[dimNumber + 1];
                    }

                    currSlice[dimNumber - 1] = newSliceInt;
                    currDimSlice.setText(Integer.toString(newSliceInt));

                    // Update the current slice
                    updateSlice();
                }
            });
            rightButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    // Subtract one from the appropriate dimension
                    int dimNumber = Integer.parseInt(labelString.substring(1));
                    int newSliceInt = currSlice[dimNumber - 1];
                    ++newSliceInt;

                    // if the new value is too big, set it to wrap around
                    if ( newSliceInt >= board.getBoundingBox()[dimNumber + 1] )
                    {
                        newSliceInt %= board.getBoundingBox()[dimNumber + 1];
                    }


                    currSlice[dimNumber - 1] = newSliceInt;
                    currDimSlice.setText(Integer.toString(newSliceInt));

                    // Update the current slice
                    updateSlice();
                }
            });

            // Align the buttons within themselves
            GroupLayout.ParallelGroup dimParGroup = dimLayout.createParallelGroup(GroupLayout.Alignment.CENTER);
            dimParGroup.addComponent(leftButton);
            dimParGroup.addComponent(currDimSlice);
            dimParGroup.addComponent(rightButton);
            GroupLayout.SequentialGroup dimSeqGroup = dimLayout.createSequentialGroup();
            dimSeqGroup.addComponent(leftButton);
            dimSeqGroup.addComponent(currDimSlice);
            dimSeqGroup.addComponent(rightButton);

            // Add buttons and label to panel
            dimPanelSeqGroup.addComponent(dimLabel);
            dimPanelSeqGroup.addGroup(dimParGroup);
            dimPanelParGroup.addComponent(dimLabel);
            dimPanelParGroup.addGroup(dimSeqGroup);

            dimLayout.setHorizontalGroup(dimPanelParGroup);
            dimLayout.setVerticalGroup(dimPanelSeqGroup);
        }

        dimButtons.setLayout(dimLayout);

        return dimButtons;
    }


    // CONSTRUCTORS
    public GUI(Board board)
    {
        this.board = board;

        frame = new JFrame ("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Add mines remaining label
        minesRemaining.setPreferredSize(new Dimension (100, 30 ));
        minesRemaining.setHorizontalAlignment(SwingConstants.CENTER);

        // Create a panel to store the boards
        slicePanels = createBoards(board);
        boardPanel = new JPanel();
        /*GridLayout boardLayout = new GridLayout();
        boardPanel.setLayout(boardLayout);
        boardPanel.setVisible(true);

        // Add slices to boardPanel
        for ( JPanel slice : slicePanels )
        {
            boardPanel.add(slice);
            slice.setVisible(false);
        }

        // Set the first slice to be visible
        slicePanels[0].setVisible(true);*/


        CardLayout boardLayout = new CardLayout();
        boardPanel.setLayout(boardLayout);

        // Add each slice of the board to boardPanel
        for ( int i = 0;
              i < slicePanels.length;
              ++i )
        {
            String name = Integer.toString(i);
            slicePanels[i].setName(name);
            boardPanel.add(slicePanels[i], name);
            boardLayout.addLayoutComponent(slicePanels[i], name);
        }

        // Make sure everything is visible
        /*for ( Component c : boardPanel.getComponents())
        {
            c.setVisible(true);
        }*/
        boardPanel.setVisible(true);

        JPanel dimPanel = dimensionSelector();
        JPanel gamePanel = new JPanel();

        GroupLayout gameLayout = new GroupLayout(gamePanel);
        gameLayout.setHorizontalGroup(gameLayout.createSequentialGroup()
                                              .addGroup(gameLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                              .addComponent(minesRemaining)
                                              .addComponent(boardPanel))
                                              .addComponent(dimPanel)
                                     );
        gameLayout.setVerticalGroup(gameLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                            .addGroup(gameLayout.createSequentialGroup()
                                            .addComponent(minesRemaining)
                                            .addComponent(boardPanel))
                                            .addComponent(dimPanel)
                                   );
        gamePanel.setLayout(gameLayout);
        frame.add(gamePanel);
        frame.setSize(new Dimension(800, 800));
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
