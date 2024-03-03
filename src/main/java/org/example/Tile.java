package org.example;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.border.Border;

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

    // Reset this button
    public void reset()
    {
        contents = 0;
        tileButton.setText(null);
        tileButton.setBackground(Color.gray);
        //tileButton.setSelected(false);
        isRevealed = false;
    }

    private int[] coords = new int[2];
    public JToggleButton tileButton = new JToggleButton();

    private void textColour()
    {
        switch (contents)
        {
            case 0:
                tileButton.setBackground(Color.GRAY);
            case 1:
                tileButton.setBackground(Color.MAGENTA);
            case 2:
                tileButton.setBackground(Color.BLUE);
            case 3:
                tileButton.setBackground(Color.RED);
            case 4:
                tileButton.setBackground(Color.PINK);
            case 5:
                tileButton.setBackground(Color.CYAN);
            case 6:
                tileButton.setBackground(Color.YELLOW);
            case 7:
                tileButton.setBackground(Color.GREEN);
            case 8:
                tileButton.setBackground(Color.ORANGE);
            default:
                tileButton.setBackground(Color.BLACK);
        }
    }

    public void revealTile()
    {
        // If the tile has already been revealed or is inactive, do not reveal it
        if ( isRevealed == true)
        {
            return;
        }
        else if ( isActive == false )
        {
            return;
        }
        isRevealed = true;
        tileButton.setBackground(Color.white);
        tileButton.setText(Integer.toString(contents));
        tileButton.doClick(10);
        //tileButton.setEnabled(true);
        // Check if it is a bomb and, if so, send a signal that the game is over
        if ( contents == -1 )
        {
            board.boom(this);
        }
        else if ( contents == 0 )
        {
            // Report to board that a safe tile was clicked
            board.safeTileRevealed();
            // Explode out tiles
            board.chainReact(this);
        }
        else if ( contents >= 1 )
        {
            // Report to board that a safe tile was clicked
            board.safeTileRevealed();
        }

        //textColour();
        // Repress the button before disabling it to fix graphics
        tileButton.doClick(1);
        tileButton.setEnabled(false);
    }

    public void deActivate()
    {
        isActive = false;
        tileButton.setText(null);
        tileButton.setEnabled(false);
        tileButton.setBackground(Color.BLACK);
        isRevealed = true;
    }

    public void click()
    {
        tileButton.doClick(1);
    }

    // Change whether the tile is flagged
    public void changeFlag()
    {
        isFlagged = !isFlagged;
        if( isFlagged )
        {
            tileButton.setIcon(flagIcon);
            board.mineFlagged();
        }
        else
        {
            tileButton.setIcon(null);
            board.mineUnFlagged();
        }
    }

    // SETTERS
    public void setMine()
    {
        setContents(-1);
    }

    public void setContents(int contents)
    {
        if(isActive == true)
        {
            this.contents = contents;
        }
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

    public boolean isActive()
    {
        return isActive;
    }

    // CONSTRUCTORS
    public Tile(Board board, int[] coords)
    {
        this.board = board;
        this.coords = new int[2];
        this.coords = coords;

        tileButton.setFont(new Font("SansSerif", Font.PLAIN, 11));
        Border tileBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
        tileButton.setBorder(tileBorder);
        tileButton.setBackground(Color.gray);
        tileButton.setVisible(true);

        //tileButton.setBackground(backgroundColour);
        //tileButton.setForeground(foregroundColour);
        // Add action listener to button
        tileButton.addMouseListener(mouseClick);
    }

    MouseListener mouseClick = new MouseListener()
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

                Object caller = e.getSource();
                if( caller instanceof JToggleButton )
                {
                    ((JToggleButton) caller).setSelected(false);
                }
            }
            else if ( SwingUtilities.isRightMouseButton(e) )
            {
                changeFlag();
            }
        }

        @Override
        public void mousePressed(MouseEvent e)
        {

        }

        @Override
        public void mouseReleased(MouseEvent e)
        {

        }

        @Override
        public void mouseEntered(MouseEvent e)
        {

        }

        @Override
        public void mouseExited(MouseEvent e)
        {

        }
    };
}
