package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class MainMenu
{
    private JFrame menuFrame = new JFrame();

    // Menu buttons
    private JButton newGameButton = new JButton("New Game");
    private JButton quitGameButton = new JButton("Quit Game");

    // Get the menu ready
    private void prepareMenu()
    {
        menuFrame.setSize(200, 600);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);

        panel.setBorder(new EmptyBorder(new Insets (100, 75, 100, 75)));

        newGameButton.addActionListener(newGameListener);
        panel.add(newGameButton);

        Dimension min = new Dimension(10, 10);
        Dimension pref = new Dimension(20, 20);
        Dimension max = new Dimension(50, 50);
        panel.add(new Box.Filler(min, pref, max));

        quitGameButton.addActionListener(quitGameListener);
        panel.add(quitGameButton);

        menuFrame.add(panel);
        menuFrame.pack();
        menuFrame.setVisible(true);

    }

    // CONSTRUCTOR
    public MainMenu ()
    {
        prepareMenu();
    }

    // LISTENERS
    ActionListener newGameListener = new ActionListener()
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            // Stop the button quitting the app
            menuFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            Main.startGame();
            menuFrame.dispatchEvent(new WindowEvent(menuFrame, WindowEvent.WINDOW_CLOSING));
        }
    };

    ActionListener quitGameListener = new ActionListener()
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Main.quit();
        }
    };
}
