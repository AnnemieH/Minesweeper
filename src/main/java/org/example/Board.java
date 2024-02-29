package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;

public class Board
{
    private Tile[] contents = new Tile[10];
    private int minesToFind = 0;

    // dimensions stores x and y sizes
    private int[] dimensions = new int[2];
    private Boolean[] boardShape;

    // Given a pair of coördinates, return the index they correspond to in this board.
    // If coördinates are not present on the board, return -1
    private int coordsToIndex(int[] coords)
    {
        // Check if coördinatees are out-of-bounds
        if (coords[0] < 0 || coords[0] >= dimensions[0] || coords[1] < 0 || coords[1] >= dimensions[1])
        {
            return -1;
        }
        // The first 0, 1, ..., x-1 tiles will be with y = 0, the next x, x+1, ..., 2x - 1 will be y = 2 etc.
        return coords[1] * dimensions[0] + coords[0];
    }

    // Given an index, return the coördinates they correspond to in this board, otherwise return [-1, -1]
    private int[] indexToCoords(int index)
    {
        if ( index < 0 || index > contents.length)
        {
            return new int[]{-1, -1};
        }
        // The first 0, 1, ..., x-1 tiles will be with y = 0, the next x, x+1, ..., 2x - 1 will be y = 2 etc.
        int x = index % (dimensions[0]);
        int y = index / (dimensions[0]);
        return new int[]{x, y};
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

    // GETTERS
    public Tile getTileAt(int[] coords)
    {
        return contents[coordsToIndex(coords)];
    }
    public int[] getDimensions()
    {
        return dimensions;
    }

    public int size()
    {
        return contents.length;
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
        // Count the non-negative indices
        for ( int index : indexArray )
        {
            if ( index != -1 )
            {
                ++arrayLength;
            }
        }

        // Create an array of size arrayLength
        Tile[] neighbours = new Tile[arrayLength];

        // Add the tiles at non-negative indices to this array
        int neighboursSize = 0;
        for ( int index : indexArray )
        {
            if ( index != -1 )
            {
                neighbours[neighboursSize] = getTileAt(indexToCoords(index));
                ++neighboursSize;
            }
        }

        return neighbours;
    }
    // CONSTRUCTORS
    public Board(int x, int y, int totalMines)
    {
        // Set dimensions to be x and y
        dimensions[0] = x;
        dimensions[1] = y;

        // Set minesToFind to be totalMines
        minesToFind = totalMines;

        // Keep an array of all the mines
        int[][] mineList = new int[minesToFind][2];

        // Uniformly at random allocate the mines
        Random rand = new Random();

        mineGen:
        for (int i = 0;
             i < totalMines;
             i++)
        {
            int mineX = rand.nextInt(dimensions[0]);
            int mineY = rand.nextInt(dimensions[1]);

            // Store these coördinates in an array
            int[] mineLocation = {mineX, mineY};

            // Check to see if we've already mined this spot
            for( int[] mine : mineList)
            {
                if (mineLocation == mine)
                {
                    --i;
                    continue mineGen;
                }
            }
            mineList[i] = mineLocation;
        }

        // Fill the board with tiles
        contents = new Tile[dimensions[0] * dimensions[1]];
        for ( int i = 0;
              i < contents.length;
              ++i )
        {
            contents[i] = new Tile(this, indexToCoords(i));
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
            for ( Tile t : neighbours )
            {
                if ( t.getContents() == -1 )
                {
                    ++adjMines;
                }
            }

            // Set mine.contents to be adjMines unless it is a mine
            if ( currMine.getContents() != -1 )
            {
                currMine.setContents(adjMines);
            }
        }
    }
}
