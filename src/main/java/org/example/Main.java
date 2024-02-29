package org.example;

public class Main
{
    private static Board board;
    private static GUI ui;

    // Game is over
    public static void endGame(Boolean isWon)
    {
        ui.endGame( isWon );
    }

    public static void main(String[] args)
    {
        // Instantiäte variäbles
        board = new Board(16, 16, 40);
        ui = new GUI(board);
    }
}