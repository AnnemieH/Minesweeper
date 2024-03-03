package org.example;

import java.util.*;

public class Board
{
    // Keep track of whether the game has started
    private boolean gameStarted = false;
    // How many dimensions are we playing in
    int boardDimensions = 2;
    private Tile[] contents = new Tile[10];
    // Keep track of how many mines remaining the player thinks there is
    private int minesToFind = 0;
    private int totalMines = 0;
    // How many tiles can the player click on without exploding
    private int remainingSafeTiles = 0;

    // store teh bounding box of our map
    private int[] boundingBox = new int[2];

    // Store the shape of a board with 'x' for an active tile and 'o' (or anything else) for an inactive tile
    private LinkedList<LinkedList<String>> boardShape;
    private String[] boardForm;

    private int countActiveTiles ()
    {
        int active = 0;

        // Iterate through boardshape and count the number of active tiles
        for ( String tile : boardForm )
        {
            if ( tile.equals("x") )
            {
                ++active;
            }
        }


        return active;
    }

    // Given an array of coördinates, return the index they correspond to in this board.
    // If coördinates are not present on the board, return -1
    public int coordsToIndex(int[] coords)
    {
        // If coords is empty, return 0
        if ( coords.length == 0 )
        {
            return 0;
        }

        // If coords[0] is out of bounds, return -1
        if ( coords[0] < 0 || coords[0] >= boundingBox[0] )
        {
                return -1;
        }
        // Remove the first element from coords for recursion and store in strippedCoords
        int[] strippedCoords = new int[coords.length - 1];

        for (  int i = 0;
               i < strippedCoords.length;
               ++i )
        {
            strippedCoords[i] = coords[i + 1];
        }

        // Remove the first element from BoundingBox for recursion
        int[] strippedBoundingBox = new int[boundingBox.length - 1];
        for (  int i = 0;
               i < strippedBoundingBox.length;
               ++i )
        {
            strippedBoundingBox[i] = boundingBox[i + 1];
        }

        // Return the appropriate index value, making sure that if it's negative, we set it to -1
        return Math.max(coords[0] + (boundingBox[0] * coordsToIndex(strippedCoords, strippedBoundingBox)), -1);
/*
        // Check if coördinates are out-of-bounds
        if (coords[0] < 0 || coords[0] >= boundingBox[0] || coords[1] < 0 || coords[1] >= boundingBox[1])
        {
            return -1;
        }
        // The first 0, 1, ..., x-1 tiles will be with y = 0, the next x, x+1, ..., 2x - 1 will be y = 2 etc.
        return coords[1] * boundingBox[0] + coords[0];*/
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

        for ( int i = 0;
              i < strippedCoords.length;
              ++i )
        {
            strippedCoords[i] = coords[i + 1];
        }

        // Remove the first element from remBoundingBox for recursion
        int[] strippedBoundingBox = new int[remBoundingBox.length - 1];
        for ( int i = 0;
              i < strippedBoundingBox.length;
              ++i )
        {
                strippedBoundingBox[i] = remBoundingBox[i + 1];
        }

        // Return the appropriate index value, making sure that if it's negative, we set it to -1
        return Math.max(coords[0] + (remBoundingBox[0] * coordsToIndex(strippedCoords)), -1);
    }

    // Given an index, return the coördinates they correspond to in this board, otherwise return [-1, -1]
    public int[] indexToCoords(int index)
     {

        // Create an array of size boardDimensions to store our coordinates in
        int[] coords = new int[boardDimensions];

        // If index is illegal, return a -1 array
        if ( index < 0 || index >= contents.length)
        {
            for ( int i : coords )
            {
                coords[i] = -1;
            }
            return coords;
        }

        int strippedIndex = index;
        for ( int i = 0;
              i < coords.length;
              ++i )
        {
            coords[i] = strippedIndex % boundingBox[i];
            strippedIndex /= boundingBox[i];
        }

        return coords;
        /*if ( index < 0 || index > contents.length)
        {
            return new int[]{-1, -1};
        }
        // The first 0, 1, ..., x-1 tiles will be with y = 0, the next x, x+1, ..., 2x - 1 will be y = 2 etc.
        int x = index % (boundingBox[0]);
        int y = index / (boundingBox[0]);
        return new int[]{x, y};*/
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
    public int boundingProd()
    {
        int temp = 1;
        for ( int bound : boundingBox )
        {
            temp *= bound;
        }

        return temp;
    }

    public int getBoardDimensions()
    {
        return boardDimensions;
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

    private int pow (int number, int exponent)
    {
        int temp = 1;
        for ( int i = 0;
              i < exponent;
              ++i )
        {
            temp *= number;
        }

        return temp;
    }

    // Generate an array of perturbations
    private int[][] generatePerturbations(int[][] perturb, int dim)
    {
        // If dim == 0, we're done
        if ( dim == 0 )
        {
            return perturb;
        }

        int[][] newPerturb = new int[pow(3,boardDimensions - dim + 1)][boardDimensions];

        // For each perturbation already found, take it and add +1, -1 and 0 in the next dimension
        for ( int i = 0;
               i < perturb.length;
               ++i )
        {
            int[] currPerturb = perturb[i];
            currPerturb[boardDimensions - dim] = 1;
            System.arraycopy(currPerturb, 0, newPerturb[3 * i], 0, currPerturb.length);
            currPerturb[boardDimensions - dim] = -1;
            System.arraycopy(currPerturb, 0, newPerturb[(3 * i) + 1], 0, currPerturb.length);
            currPerturb[boardDimensions - dim] = 0;
            System.arraycopy(currPerturb, 0, newPerturb[(3 * i) + 2], 0, currPerturb.length);
        }

        return generatePerturbations(newPerturb, dim - 1);
    }

    private int[][] generatePerturbations()
    {
        int[][] newPerturb = new int[1][boardDimensions];

        // Generate all the unitary perturbations plus the 0 permutation
        int[][] fullPerturb = generatePerturbations(newPerturb, boardDimensions);

        // Return the unitary permutations with 0 stripped out
        return Arrays.copyOfRange(fullPerturb, 0, fullPerturb.length - 1);
    }
    // Given coordinates, return its 8 neighbours
    public Tile[] getNeighbours(int[] coords)
    {
        int[][] perturbations = generatePerturbations();

        ArrayList<Tile> neighbourList = new ArrayList<Tile>();

        int[] indexArray = new int[pow(3, boardDimensions) - 1];
        for ( int i = 0;
              i < indexArray.length;
              ++i )
        {
            indexArray[i] = coordsToIndex(pairwiseSum(coords, perturbations[i]));
        }

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
        return (boardForm[coordsToIndex(coords)]).equals("x");
    }

    // Initialise the board with tiles
    private void initBoard()
    {
        // Keep an array of all the mines
        int[][] mineList = new int[minesToFind][2];

        // Uniformly at random allocate the mines
        Random rand = new Random();

        int size = 1;

        // Fill the board with tiles
        for ( int i = 0;
              i < boardDimensions;
              ++i )
        {
            size *= boundingBox[i];
        }
        contents = new Tile[size];

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

            int[] mineLocation = new int[boardDimensions];

            for ( int j = 0;
                  j < boardDimensions;
                  ++j )
            {
                mineLocation[j] = rand.nextInt(boundingBox[j]);
            }

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
            Tile currTile = contents[i];
            currTile.setCoords(indexToCoords(i));
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
            if ( currTile.getContents() != -1 )
            {
                currTile.setContents(adjMines);
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

            int[] mineLocation = new int[boardDimensions];

            for ( int j = 0;
                  j < boardDimensions;
                  ++j )
            {
                mineLocation[j] = rand.nextInt(boundingBox[j]);
            }

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

    // Create a board filled with available spaces of the correct dimension and bounds
    private String[] genDefaultBoard()
    {
        int length = 1;
        // Generate an array of size equal to the product of boundingBox
        for ( int dim : boundingBox )
        {
            length *= dim;
        }

        // return an array of size length and fill it with 'x'
        String[] defaultBoard = new String[length];
        Arrays.fill(defaultBoard, "x");
        return defaultBoard;
    }

    // CONSTRUCTORS
    public Board(int[] dimParam, int totalMines)
    {
        // Set dimensions to be x and y
        boundingBox = dimParam;
        boardDimensions = dimParam.length;

        this.totalMines = totalMines;

        // Set minesToFind to be totalMines
        minesToFind = totalMines;
        Main.mineTotalUpdate(minesToFind);

        boardForm = genDefaultBoard();

        initBoard();
    }

    public Board( LinkedList<LinkedList<String>> boardShape, LinkedList<String> dataLine, int totalMines)
    {
        minesToFind = totalMines;
        Main.mineTotalUpdate(minesToFind);
        this.totalMines = totalMines;

        // dataLine contains boundingBox so set that
        for ( int i = 0;
              i < dataLine.size();
              ++i )
        {
            boundingBox[i] = Integer.parseInt(dataLine.get(i));
        }

        // Unpack boardShape and turn into an array
        String[] boardArray = new String[boundingProd()];
        for ( int i = 0;
              i < boardShape.size();
              ++i )
        {
            String[] tempArray = new String[boardShape.size()];
            boardShape.get(i).toArray(tempArray);
            System.arraycopy(tempArray,0, boardArray, i * boundingBox[0], tempArray.length);
        }

        boardForm = boardArray;

        initBoard();
    }
}
