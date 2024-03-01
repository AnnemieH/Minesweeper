package org.example;

import java.util.*;

public class Board
{
    // Keep track of whether the game has started
    private boolean gameStarted = false;
    private Tile[] contents = new Tile[10];
    // Keep track of how many mines remaining the player thinks there is
    private int minesToFind = 0;
    private int totalMines = 0;
    // How many tiles can the player click on without exploding
    private int remainingSafeTiles = 0;

    // dimensions stores x and y sizes
    private int[] boundingBox = new int[2];

    // Store the shape of a board with 'x' for an active tile and 'o' (or anything else) for an inactive tile
    private LinkedList<LinkedList<String>> boardShape;

    private int countActiveTiles ()
    {
        int inactive = 0;

        // Iterate through boardshape and count the number of non-active tiles
        for ( int i = 0;
              i < boundingBox[0];
              ++i )
        {
            for ( int j = 0;
                  j < boundingBox[1];
                  ++j )
            {
                if ( !boardShape.get(i).get(j).equals("x"))
                {
                    ++inactive;
                }
            }
        }

        return contents.length - inactive;
    }

    // Given a pair of coördinates, return the index they correspond to in this board.
    // If coördinates are not present on the board, return -1
    private int coordsToIndex(int[] coords)
    {
        // Check if coördinates are out-of-bounds
        if (coords[0] < 0 || coords[0] >= boundingBox[0] || coords[1] < 0 || coords[1] >= boundingBox[1])
        {
            return -1;
        }
        // The first 0, 1, ..., x-1 tiles will be with y = 0, the next x, x+1, ..., 2x - 1 will be y = 2 etc.
        return coords[1] * boundingBox[0] + coords[0];
    }

    // Given an index, return the coördinates they correspond to in this board, otherwise return [-1, -1]
    private int[] indexToCoords(int index)
    {
        if ( index < 0 || index > contents.length)
        {
            return new int[]{-1, -1};
        }
        // The first 0, 1, ..., x-1 tiles will be with y = 0, the next x, x+1, ..., 2x - 1 will be y = 2 etc.
        int x = index % (boundingBox[0]);
        int y = index / (boundingBox[0]);
        return new int[]{x, y};
    }

    // If a tile has been revealed, update remainingSafeTiles and maybe win
    public void safeTileRevealed()
    {
        // A turn has been taken, so ensure gameStarted is set to true
        gameStarted = true;
        --remainingSafeTiles;
        if ( remainingSafeTiles <= 0 )
        {
            Main.endGame(true);
        }
    }

    // Mine has been hit
    public void boom(Tile bombLoc)
    {
        // Check to see if this is the first turn.
        // If it is, regenerate the board until the tile is not a bomb
        // Otherwise, end the game

        if ( gameStarted )
        {
            Main.endGame(false);
        }
        else
        {
            // Click the button again to fix the GUI
            bombLoc.reset();
            redoBoard();
            // Make sure that the game still counts this as not having started
            gameStarted = false;
            bombLoc.revealTile();
        }

    }

    // Chain reaction of tiles not adjacent to a mine
    public void chainReact (Tile tile)
    {
        // For each neighbour of the tile reveal it
        for ( Tile neighbour : getNeighbours( tile.getCoords() ) )
        {
            neighbour.revealTile();
        }
    }

    // A mine has been flagged
    public void mineFlagged ()
    {
        --minesToFind;
        Main.mineTotalUpdate(minesToFind);
    }

    // A mine has been unflagged
    public void mineUnFlagged()
    {
        ++minesToFind;
        Main.mineTotalUpdate(minesToFind);
    }

    // GETTERS
    public Tile getTileAt(int[] coords)
    {
        return contents[coordsToIndex(coords)];
    }
    public int[] getBoundingBox()
    {
        return boundingBox;
    }

    public int size()
    {
        return contents.length;
    }

    // Initialise remaining tiles
    private void initRemainingSafeTiles ()
    {
        int safeTiles = 0;

        // Count up active tiles
        for ( Tile tile : contents )
        {
            if ( tile.isActive() )
            {
                ++safeTiles;
            }
        }

        // Subtract all the mines and set this equal to remainingSafeTiles
        remainingSafeTiles = safeTiles - totalMines;
    }

    // Sum two arrays pairwise until one runs out of length
    private int[] pairwiseSum(int[] array1, int[] array2)
    {
        int[] finalArray = new int[Math.min(array1.length, array2.length)];
        for (int i = 0;
             i < Math.min(array1.length, array2.length);
             i++)
        {
            finalArray[i] = array1[i] + array2[i];
        }

        return finalArray;
    }
    // Given coordinates, return its 8 neighbours
    public Tile[] getNeighbours(int[] coords)
    {
        int[] xPeturbation = new int[]{1, 0};
        int[] xNegPeturbation = new int[]{-1, 0};
        int[] yPeturbation = new int[]{0, 1};
        int[] yNegPeturbation = new int[]{0, -1};

        ArrayList<Tile> neighbourList = new ArrayList<Tile>();

        int[] indexArray = new int[8];
        indexArray[0] = coordsToIndex(pairwiseSum(coords, xPeturbation));
        indexArray[1] = coordsToIndex(pairwiseSum(coords, xNegPeturbation));
        indexArray[2] = coordsToIndex(pairwiseSum(coords, yPeturbation));
        indexArray[3] = coordsToIndex(pairwiseSum(coords, yNegPeturbation));
        indexArray[4] = coordsToIndex(pairwiseSum(pairwiseSum(coords, xPeturbation), yPeturbation));
        indexArray[5] = coordsToIndex(pairwiseSum(pairwiseSum(coords, xNegPeturbation), yPeturbation));
        indexArray[6] = coordsToIndex(pairwiseSum(pairwiseSum(coords, xPeturbation), yNegPeturbation));
        indexArray[7] = coordsToIndex(pairwiseSum(pairwiseSum(coords, xNegPeturbation), yNegPeturbation));

        int arrayLength = 0;
        // Count the indices in bounds
        for ( int index : indexArray )
        {

            if ( index >= 0 && index < contents.length && isInBounds(indexToCoords(index)) )
            {
                ++arrayLength;
            }
        }

        // Create an array of size arrayLength
        Tile[] neighbours = new Tile[arrayLength];

        // Add the tiles at non-negative in-bounds indices to this array
        int neighboursSize = 0;
        for ( int index : indexArray )
        {
            if ( index >= 0 && isInBounds(indexToCoords(index))  )
            {
                Tile possNeighbour = getTileAt(indexToCoords(index));
                neighbours[neighboursSize] = possNeighbour;
                ++neighboursSize;

            }
        }

        return neighbours;
    }

    // Is given coördinates in bounds of the map
    private Boolean isInBounds(int[] coords)
    {
        return (boardShape.get(coords[1])).get(coords[0]).equals("x");
    }

    // Initialise the board with tiles
    private void initBoard()
    {
        // Keep an array of all the mines
        int[][] mineList = new int[minesToFind][2];

        // Uniformly at random allocate the mines
        Random rand = new Random();

        // Fill the board with tiles
        contents = new Tile[boundingBox[0] * boundingBox[1]];

        // Recalculate totalMines now that everything's initialised
        totalMines = Math.min(totalMines, countActiveTiles() - 1);
        minesToFind = totalMines;

        for ( int i = 0;
              i < contents.length;
              ++i )
        {
            contents[i] = new Tile(this, indexToCoords(i));
        }

        mineGen:
        for (int i = 0;
             i < totalMines;
             i++)
        {
            // If we're trying to add more mines than spaces on the board, abort this loop so there is at least one
            // unmined space
            if ( i >= countActiveTiles() - 1)
            {
                totalMines = i - 1;
                break;
            }
            int mineX = rand.nextInt(boundingBox[0]);
            int mineY = rand.nextInt(boundingBox[1]);


            // Store these coördinates in an array
            int[] mineLocation = {mineX, mineY};

            // Check to see if we've already mined this spot
            // If not, then repeat this loops
            for( int[] mine : mineList)
            {
                if (mineLocation == mine)
                {
                    --i;
                    continue mineGen;
                }
            }

            // Check to see if we're in-bounds.
            // If not, then repeat this loops
            if ( !isInBounds(mineLocation))
            {
                --i;
                continue;
            }
            mineList[i] = mineLocation;
        }
        // Set the chosen tiles to be mines
        for( int[] mine : mineList )
        {
            int index = coordsToIndex(mine);
            if ( index == -1 )
            {
                System.out.println("Error in board construction");
            }

            contents[index].setMine();
        }

        // Assign remaining tiles
        for (int i = 0;
             i < contents.length;
             ++i )
        {
            Tile currMine = contents[i];
            currMine.setCoords(indexToCoords(i));
            // Count adjacent mines
            int adjMines = 0;
            // Get the tiles neighbours and iterate through them

            Tile[] neighbours = getNeighbours(indexToCoords(i));


            // For each neighbour that is a mine, increment adjMines
            for ( int j = 0;
                  j < neighbours.length;
                  ++j )
            {
                if ( neighbours[j].getContents() == -1 )
                {
                    ++adjMines;
                }
            }

            // Set mine.contents to be adjMines unless it is a mine
            if ( currMine.getContents() != -1 )
            {
                currMine.setContents(adjMines);
            }

            // If this index is out-of-bounds, ignore it
            if ( !isInBounds(indexToCoords(i)))
            {
                getTileAt(indexToCoords(i)).deActivate();
            }
        }
        // Initialise tiles to find
        initRemainingSafeTiles();
        gameStarted = false;
    }
    // Reallocate the mines to cheat
    private void redoBoard()
    {
        // Keep an array of all the mines
        int[][] mineList = new int[minesToFind][2];

        // Set the contents of all tiles to be 0
        for ( Tile t : contents )
        {
            t.reset();
        }

        // Uniformly at random allocate the mines
        Random rand = new Random();

        mineGen:
        for (int i = 0;
             i < totalMines;
             i++)
        {
            // If we're trying to add more mines than spaces on the board, abort this loop so there is at least one
            // unmined space
            if ( i >= countActiveTiles() - 1)
            {
                totalMines = i - 1;
                break;
            }

            int mineX = rand.nextInt(boundingBox[0]);
            int mineY = rand.nextInt(boundingBox[1]);


            // Store these coördinates in an array
            int[] mineLocation = {mineX, mineY};

            // Check to see if we've already mined this spot
            // If not, then repeat this loops
            for( int[] mine : mineList)
            {
                if (mineLocation == mine)
                {
                    --i;
                    continue mineGen;
                }
            }

            // Check to see if we're in-bounds.
            // If not, then repeat this loops
            if ( !isInBounds(mineLocation))
            {
                --i;
                continue;
            }
            mineList[i] = mineLocation;
        }

        // Set the chosen tiles to be mines
        for( int[] mine : mineList )
        {
            int index = coordsToIndex(mine);
            if ( index == -1 )
            {
                System.out.println("Error in board construction");
            }

            contents[index].setMine();
        }

        // Assign remaining tiles
        for (int i = 0;
             i < contents.length;
             ++i )
        {
            Tile currMine = contents[i];
            currMine.setCoords(indexToCoords(i));
            // Count adjacent mines
            int adjMines = 0;
            // Get the tiles neighbours and iterate through them

            Tile[] neighbours = getNeighbours(indexToCoords(i));


            // For each neighbour that is a mine, increment adjMines
            for ( int j = 0;
                  j < neighbours.length;
                  ++j )
            {
                if ( neighbours[j].getContents() == -1 )
                {
                    ++adjMines;
                }
            }

            // Set mine.contents to be adjMines unless it is a mine
            if ( currMine.getContents() != -1 )
            {
                currMine.setContents(adjMines);
            }

            // If this index is out-of-bounds, ignore it
            if ( !isInBounds(indexToCoords(i)))
            {
                getTileAt(indexToCoords(i)).deActivate();
            }
        }

        // Initialise number of tiles to find
        initRemainingSafeTiles();
        gameStarted = false;
    }

    // CONSTRUCTORS
    public Board(int x, int y, int totalMines)
    {
        // Set dimensions to be x and y
        boundingBox[0] = x;
        boundingBox[1] = y;

        this.totalMines = totalMines;

        // Set minesToFind to be totalMines
        minesToFind = totalMines;
        Main.mineTotalUpdate(minesToFind);

        // Create a default boardShape
        for ( int i = 0;
              i < boundingBox[0];
              ++i )
        {
            // For each column in the current row, an 'x' to mark an available space
            LinkedList<String> currRow = new LinkedList<>();
            for ( int j = 0;
                  j < boundingBox[1];
                  ++j )
            {
                currRow.add("x");
            }

            // Add currRow to our board
            boardShape.add(currRow);
        }

        initBoard();
    }

    public Board( LinkedList<LinkedList<String>> boardShape, int totalMines)
    {
        minesToFind = totalMines;
        Main.mineTotalUpdate(minesToFind);
        this.boardShape = boardShape;
        this.totalMines = totalMines;

        // Find the size of the board
        int maxX = 0;
        int maxY = boardShape.size();
        for( List<String> line : boardShape )
        {
            if ( line.size() > maxX )
            {
                maxX = line.size();
            }
        }

        // Values are off-by-one so fix and set dimensions to be this
        boundingBox[0] = maxX - 1;
        boundingBox[1] = maxY - 1;

        initBoard();
    }
}
