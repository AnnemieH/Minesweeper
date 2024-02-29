package org.example;

public class Main
{
    public static void main(String[] args)
    {
        // Instantiäte variäbles
        Board board = new Board(16, 16, 40);
        GUI ui = new GUI(board);
    }
}