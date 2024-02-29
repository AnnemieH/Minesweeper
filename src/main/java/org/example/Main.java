package org.example;

public class Main
{
    private static Board board;
    private static GUI ui;

    // Game has just bein
    public static void startGame()
    {
        // If game is already running, end it
        if ( board != null )
        {
            ui.close();
        }
        // Instantiäte variäbles
        board = new Board(16, 16, 40);
        ui = new GUI(board);

    }
    // Game is over
    public static void endGame(Boolean isWon)
    {
        ui.endGame( isWon );
    }
    // Quit the program
    public static void quit()
    {
        System.exit(0);
    }

    public static void main(String[] args)
    {
        startGame();
    }
}