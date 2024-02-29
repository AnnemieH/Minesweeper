package org.example;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

public class Tile
{
    private boolean isRevealed = false;
    private boolean isActive = true;
    // Has the user flagged this tile as beïng a mine?
    private boolean isFlagged = false;
    Icon flagIcon = new ImageIcon("src/main/resources/exclamation.png");
    Board board;

    // Both background and text colours should be black at the start
    Color backgroundColour = Color.BLACK;
    Color foregroundColour = Color.BLACK;

    // -1 if a mine
    // otherwise contents equals the number of mines adjacent to the tile
    private int contents = 0;

    private int[] coords = new int[2];
    public JToggleButton tileButton = new JToggleButton();

    public void revealTile()
    {
        if ( isRevealed == true )
        {
            return;
        }
        // Comment
        isRevealed = true;
        tileButton.setText(Integer.toString(contents));
        tileButton.doClick(10);
        //tileButton.setEnabled(true);
        // Check if it is a bomb and, if so, send a signal that the game is over
        if ( contents == -1 )
        {
            Main.endGame(false);
           // board.endGame(false);
        }
        else if ( contents == 0 )
        {
            // Explode out tiles
            board.chainReact(this);
        }



        tileButton.setEnabled(false);
    }


    // Change whether the tile is flagged
    public void changeFlag()
    {
        isFlagged = !isFlagged;
        if( isFlagged )
        {
            tileButton.setIcon(flagIcon);
        }
        else
        {
            tileButton.setIcon(null);
        }
    }

    // SETTERS
    public void setMine()
    {
        setContents(-1);
    }

    public void setContents(int contents)
    {
        this.contents = contents;
        //tileButton.setText(Integer.toString(this.contents));
    }

    public void setCoords ( int[] coordinates )
    {
        coords[0] = coordinates[0];
        coords[1] = coordinates[1];
    }


    // GETTERS

    public int getContents()
    {
        return contents;
    }
    public int[] getCoords()
    {
        return coords;
    }

    // CONSTRUCTORS
    public Tile(Board board, int[] coords)
    {
        this.board = board;
        this.coords = new int[2];
        this.coords = coords;

        //tileButton.setBackground(backgroundColour);
        //tileButton.setForeground(foregroundColour);
        // Add action listener to button
        tileButton.addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                // If tile has already been revealed do nothing
                if ( isRevealed )
                {
                    return;
                }

                // Differentiäte between left and right mouse clicks
                if ( SwingUtilities.isLeftMouseButton(e) )
                {
                    revealTile();
                }
                else if ( SwingUtilities.isRightMouseButton(e) )
                {
                    changeFlag();
                }
            }

            @Override
            public void mouseReleased( MouseEvent e )
            {

            }

            @Override
            public void mousePressed( MouseEvent e )
            {

            }

            @Override
            public void mouseEntered( MouseEvent e )
            {

            }

            @Override
            public void mouseExited( MouseEvent e )
            {

            }
        });
    }
}
