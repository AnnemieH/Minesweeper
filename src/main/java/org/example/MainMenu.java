package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class MainMenu
{
    private JFrame menuFrame = new JFrame();
    private JPanel mainPanel = new JPanel();
    // Menu buttons
    private JButton newGameButton = new JButton("New Game");
    private JButton quitGameButton = new JButton("Quit Game");
    // Menu fields
    private JTextField mineField;
    private JTextField dimensionField;
    private JTextField lengthField;

    // Constants
    private int textWidth = 100;
    private int textHeight = 20;

    // Find all game maps and store them in an ArrayList
    private ArrayList <JButton> findMaps()
    {
        ArrayList <JButton> maps = new ArrayList<JButton>();
        File[] loadedMaps = new File("src/main/resources/Maps").listFiles();

        // For every file we've just found, create a button that, on press, will start the game with that map
        for( File map : loadedMaps )
        {
            JButton loadMap = new JButton();
            loadMap.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    menuFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    Main.startGame(map);
                    menuFrame.dispatchEvent(new WindowEvent(menuFrame, WindowEvent.WINDOW_CLOSING));
                }
            });

            // Name the button
            String mapName = map.getName();
            mapName = mapName.substring(0, mapName.length() - 4);
            loadMap.setText(mapName);
            maps.add(loadMap);
        }

        return maps;
    }

    // Get the menu ready
    private void prepareMainMenu()
    {
        menuFrame.setSize(200, 600);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BoxLayout layout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
        mainPanel.setLayout(layout);

        mainPanel.setBorder(new EmptyBorder(new Insets (100, 75, 100, 75)));

        newGameButton.addActionListener(newGameListener);
        mainPanel.add(newGameButton);

        Dimension min = new Dimension(10, 10);
        Dimension pref = new Dimension(20, 20);
        Dimension max = new Dimension(50, 50);
        mainPanel.add(new Box.Filler(min, pref, max));

        quitGameButton.addActionListener(quitGameListener);
        mainPanel.add(quitGameButton);

        menuFrame.add(mainPanel);
        menuFrame.pack();
        menuFrame.setVisible(true);

    }

    private void prepareNewGameMenu()
    {
        // Define the graphical elements we want
        JPanel newGamePanel = new JPanel();

        // mineField should contain positive integers
        mineField = new JTextField();
        mineField.addKeyListener(checkPositive);
        mineField.setPreferredSize(getTextDim());
        JLabel mineLabel = new JLabel("Total mines: ");

        // dimensionField should contain positive integers
        dimensionField = new JTextField();
        dimensionField.addKeyListener(checkPositive);
        dimensionField.setPreferredSize(getTextDim());
        JLabel dimensionLabel = new JLabel("D:");

        JButton beginButton = new JButton ( "Start game" );
        beginButton.addActionListener(startGameListener);

        // Define the layout
        // Layout of basic parameters.
        // e.g. number of mines, dimension
        JPanel paramPanel = new JPanel();
        GroupLayout paramLayout = new GroupLayout(paramPanel);

        paramLayout.setHorizontalGroup(paramLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                 .addGroup(paramLayout
                                                                   .createSequentialGroup()
                                                                   .addComponent(mineLabel)
                                                                   .addComponent(mineField)
                                                          )
                                                 .addGroup(paramLayout
                                                                   .createSequentialGroup()
                                                                   .addComponent(dimensionLabel)
                                                                   .addComponent(dimensionField)
                                                          )
                                        );
        paramLayout.setVerticalGroup(paramLayout.createSequentialGroup()
                                               .addGroup(
                                                       paramLayout.createParallelGroup()
                                                               .addComponent(mineLabel)
                                                               .addComponent(mineField)
                                                        )
                                               .addGroup(
                                                       paramLayout.createParallelGroup()
                                                               .addComponent(dimensionLabel)
                                                               .addComponent(dimensionField)
                                                        )
                                      );

        paramPanel.setLayout(paramLayout);
        paramPanel.setVisible(true);

        // Layout of the bounding box menu
        JPanel boundingBoxPanel = new JPanel();
        JLabel isSquareLabel = new JLabel("Square?");
        JCheckBox isSquare = new JCheckBox();
        JLabel lengthLabel = new JLabel("Length");
        lengthField = new JTextField();
        lengthField.addKeyListener(checkPositive);
        lengthField.setPreferredSize(getTextDim());
        GroupLayout boundingBoxMenuLayout = new GroupLayout(boundingBoxPanel);
        boundingBoxMenuLayout.setHorizontalGroup(boundingBoxMenuLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                         .addGroup(boundingBoxMenuLayout
                                                                           .createSequentialGroup()
                                                                           .addComponent(isSquareLabel)
                                                                           .addComponent(isSquare)
                                                                  )
                                                         .addGroup(boundingBoxMenuLayout
                                                                           .createSequentialGroup()
                                                                           .addComponent(lengthLabel)
                                                                           .addComponent(lengthField)
                                                                  )
                                                );
        boundingBoxMenuLayout.setVerticalGroup(boundingBoxMenuLayout.createSequentialGroup()
                                                       .addGroup(boundingBoxMenuLayout
                                                                         .createParallelGroup()
                                                                         .addComponent(isSquareLabel)
                                                                         .addComponent(isSquare)
                                                                )
                                                       .addGroup(boundingBoxMenuLayout
                                                                         .createParallelGroup()
                                                                         .addComponent(lengthLabel)
                                                                         .addComponent(lengthField)
                                                                )
                                                );
        boundingBoxPanel.setLayout(boundingBoxMenuLayout);

        // Layout of the load game menu
        JPanel loadPanel = new JPanel();
        JLabel loadLabel = new JLabel("Maps Available:");
        GroupLayout loadPanelMenuLayout = new GroupLayout(loadPanel);
        GroupLayout.SequentialGroup loadPanelSeqGroup = loadPanelMenuLayout.createSequentialGroup();
        GroupLayout.ParallelGroup loadPanelParGroup = loadPanelMenuLayout.createParallelGroup(GroupLayout.Alignment.LEADING);

        loadPanelSeqGroup.addComponent(loadLabel);
        loadPanelParGroup.addComponent(loadLabel);

        for ( JButton map : findMaps())
        {
            loadPanelSeqGroup.addComponent(map);
            loadPanelParGroup.addComponent(map);
        }
        loadPanelMenuLayout.setHorizontalGroup(loadPanelParGroup);
        loadPanelMenuLayout.setVerticalGroup(loadPanelSeqGroup);
        loadPanel.setLayout(loadPanelMenuLayout);



        // Layout of the larger options menu
        JPanel optionsPanel = new JPanel();
        GroupLayout optionsPanelMenuLayout = new GroupLayout(optionsPanel);
        optionsPanelMenuLayout.setHorizontalGroup(optionsPanelMenuLayout.createSequentialGroup()
                                                          .addComponent(paramPanel)
                                                          .addComponent(boundingBoxPanel)
                                                          .addComponent(loadPanel)
                                                 );
        optionsPanelMenuLayout.setVerticalGroup(optionsPanelMenuLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(paramPanel)
                                                        .addComponent(boundingBoxPanel)
                                                        .addComponent(loadPanel)
                                               );
        optionsPanel.setLayout(optionsPanelMenuLayout);

        // Layout of the global menu
        GroupLayout newGameMenuGroup = new GroupLayout(newGamePanel);
        newGameMenuGroup.setHorizontalGroup(newGameMenuGroup.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                    .addComponent(optionsPanel)
                                                    .addComponent(beginButton)
                                           );
        newGameMenuGroup.setVerticalGroup(newGameMenuGroup.createSequentialGroup()
                                                  .addComponent(optionsPanel)
                                                  .addComponent(beginButton)
                                         );
        newGamePanel.setLayout(newGameMenuGroup);

        menuFrame.add(newGamePanel);
        menuFrame.pack();
    }

    // GETTERS


    public int getTextWidth()
    {
        return textWidth;
    }

    public int getTextHeight()
    {
        return textHeight;
    }

    public Dimension getTextDim()
    {
        return new Dimension(getTextWidth(), getTextHeight());
    }

    // CONSTRUCTOR
    public MainMenu ()
    {
        prepareMainMenu();
    }

    // LISTENERS
    ActionListener newGameListener = new ActionListener()
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            // Set main menu invisible and prepare newGameMenu
            mainPanel.setVisible(false);
            prepareNewGameMenu();
            menuFrame.setTitle("New Game, Who Dis?");
        }
    };
    ActionListener startGameListener = new ActionListener()
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            // Stop the button quitting the app
            menuFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            int[] boundingBox = new int[Integer.parseInt(dimensionField.getText())];

            for ( int i = 0;
                  i < Integer.parseInt(dimensionField.getText());
                  ++i )
            {
                boundingBox[i] = Integer.parseInt(lengthField.getText());
            }

            Main.startGame( boundingBox, Integer.parseInt(mineField.getText()) );
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

    KeyListener checkPositive = new KeyListener()
    {
        @Override
        public void keyTyped(KeyEvent e)
        {
        }

        @Override
        public void keyPressed(KeyEvent e)
        {

        }

        @Override
        public void keyReleased(KeyEvent e)
        {

            Object caller = e.getSource();
            if ( caller instanceof JTextField )
            {
                // If the string is null, don't worry about it
                String fieldString = ((JTextField) caller).getText();
                if (fieldString.equals("") )
                {
                    return;
                }
                // Check to see entered text is non-positive. If it is, set it to 1
                try
                {
                    int fieldVal = Integer.parseInt(fieldString);
                    if (fieldVal <= 0)
                    {
                        fieldVal = 1;
                        ((JTextField) caller).setText(Integer.toString(fieldVal));
                    }
                }
                catch (Exception ex)
                {
                    // If something messes up, set to default.
                    ((JTextField) caller).setText("1");
                }
            }
        }
    };
}
