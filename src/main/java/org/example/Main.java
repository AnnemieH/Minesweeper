package org.example;

import java.io.FileNotFoundException;
import java.util.*;
import java.io.File;

public class Main
{
    private static Board board;
    private static File currMap;
    private static GUI ui;

    // Game has just begun
    public static void startGame()
    {
        // If game is already running, end it
        if ( board != null )
        {
            ui.close();
        }
        // Instantiäte variäbles
        //board = new Board(16, 16, 40);
        int currMines = board.getTotalMines();
        board = loadBoard(currMap, currMines);
        ui = new GUI(board);

    }
    // Start the game from a given map
    public static void startGame(File map, int mines)
    {
        currMap = map;
        // If game is already running, end it
        if ( board != null )
        {
            ui.close();
        }
        // Instantiäte variäbles
        //board = new Board(16, 16, 40);
        board = loadBoard(map, mines);
        ui = new GUI(board);

    }

    public static void startGame ( int[] boundingBox, int mines )
    {
        // If game is already running, end it
        if ( board != null )
        {
            ui.close();
        }
        // Instantiäte variäbles
        board = new Board(boundingBox, mines);
        ui = new GUI(board);
    }
    // Game is over
    public static void endGame(Boolean isWon)
    {
        ui.endGame( isWon );
    }

    // Tell the GUI if a bomb was flagged
    public static void mineTotalUpdate(int minesRemaining)
    {
        GUI.updateMinesRemaining(minesRemaining);
    }

    // Break csv lines apart
    private static LinkedList<String> breakCSVLine ( String line )
    {
        LinkedList<String> values = new LinkedList<String>();
        try (Scanner rowScanner = new Scanner(line))
        {
            rowScanner.useDelimiter(",");

            while ( rowScanner.hasNext() )
            {
                String s = rowScanner.next();
                values.add(s);
            }

        }

        return values;
    }

    // Load a board from a file
    public static Board loadBoard(File map, int mines)
    {
        // Read the csv into board shape, reserving the first line for data
        LinkedList<LinkedList<String>> boardShape = new LinkedList<LinkedList<String>>();
        LinkedList<String> dataLine = new LinkedList<>();
        try( Scanner scanner = new Scanner(map))
        {
            dataLine = breakCSVLine(scanner.nextLine());

            while( scanner.hasNextLine() )
            {
                boardShape.add(breakCSVLine(scanner.nextLine()));
            }
        }
        catch ( FileNotFoundException e )
        {
            System.out.println("File not found");
        }

        return new Board(boardShape, dataLine, mines);
    }

    // Quit the program
    public static void quit()
    {
        System.exit(0);
    }

    public static void main(String[] args)
    {
        MainMenu menu  = new MainMenu();
    }
}