// Ved Pradhan
// 4-21-20
// SeismoWorld5.java
// This program is the final game project of this year. It is a game that
// teaches the user about the equations and graphs of circles and using them
// to find the epicenter of earthquakes. Incorrect answers will prompt for a
// mini quiz that will test the user's graphing and equation writing skills.

// This program is a compilation of all of the concepts we have learned this
// year. This includes graphics, components, Listeners and Handlers, File I/O,
// arrays, conditionals, loops, algorithm design, etc.

// TESTING:

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.Timer;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.PrintWriter;
import java.io.FileWriter;

public class SeismoWorld5
{

  public SeismoWorld5()
  {
  }

  public static void main(String[] args)
  {
    SeismoWorld5 gameRunner = new SeismoWorld5();
    gameRunner.runIt();
  }

  // This method creates the window (JFrame) and adds the panel that
  // contains the rest of the game. Then, it makes it visible.
  public void runIt()
  {
    JFrame frame = new JFrame("Seismo World");
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(10, 10);
		frame.setResizable(false);
		GamePanelsHolder5 panelHolder = new GamePanelsHolder5();
		frame.add(panelHolder);
		frame.setVisible(true);
  }
}

class GamePanelsHolder5 extends JPanel
{
  private CardLayout listOfCards; // This CardLayout holds the start screen,
                                  // instructions screen, and game screen.]
  private boolean paused; // Is the game paused?
  private GamePanel5 gameScreen; // The panel where the game is played

  // sets up the card layout and adds the big three JPanels to it.
  public GamePanelsHolder5()
  {
    paused = true;

    listOfCards = new CardLayout();

    setLayout(listOfCards);

    StartPanel5 startScreen = new StartPanel5(this, listOfCards);
    InstructionsPanel5 instructionsScreen = new InstructionsPanel5(this,
      listOfCards);
    gameScreen = new GamePanel5(this, listOfCards);

    add(startScreen, "Start Panel");
    add(instructionsScreen, "Instructions Panel");
    add(gameScreen, "Game Panel");

    listOfCards.show(this, "Start Panel");
  }

  // this class shows the start screen when the game opens
  class StartPanel5 extends JPanel
  {
    private GamePanelsHolder5 parent1; // contains the parent so we can
                                      // switch cards
    private CardLayout list1; // again, so we can switch cards

    // Sets up the components in the start screen
    public StartPanel5(GamePanelsHolder5 parent1In, CardLayout list1In)
    {
      parent1 = parent1In;
      list1 = list1In;

      setLayout(new FlowLayout(FlowLayout.CENTER, 600, 30));

      Font bigFont = new Font("Dialog", Font.BOLD, 100);
      Font smallFont = new Font("Dialog", Font.PLAIN, 40);

      // Game Name Label
      JLabel gameName = new JLabel("Seismo World");
      gameName.setFont(bigFont);
      gameName.setForeground(Color.WHITE);

      StartScreenButtonHandler5 ssbh = new StartScreenButtonHandler5();

      // Navigation Buttons
      JButton playButton = new JButton("Begin Game");
      playButton.setFont(smallFont);
      playButton.addActionListener(ssbh);
      playButton.setPreferredSize(new Dimension(300, 50));

      JButton instructionsButton = new JButton("Instructions");
      instructionsButton.setFont(smallFont);
      instructionsButton.addActionListener(ssbh);
      instructionsButton.setPreferredSize(new Dimension(300, 50));

      JButton quitButton = new JButton("Quit");
      quitButton.setFont(smallFont);
      quitButton.addActionListener(ssbh);
      quitButton.setPreferredSize(new Dimension(300, 50));

      add(gameName);
      add(playButton);
      add(instructionsButton);
      add(quitButton);

      setBackground(Color.BLUE);
    }

    // draws the design in the background of the start screen
    public void paintComponent(Graphics g)
    {
      super.paintComponent(g);
      Color brown = new Color(102, 51, 0);
      g.setColor(brown);
      g.fillRect(0, 400, getWidth(), 200); // Draws the underground dirt
      int[] xValsFault = {500, 375, 400, 300, 425, 400};
      int[] yValsFault = {400, 450, 500, 550, 500, 450};
      g.setColor(Color.BLACK);
      g.fillPolygon(xValsFault, yValsFault, 6); // Draws the fault in the ground
      g.setColor(Color.GRAY);

      // Draws towers with alternating heights.
      for (int towerCount = 0; towerCount < getWidth(); towerCount += 50)
      {
        int towerHeight = 0;
        if ((towerCount / 50) % 2 == 0)
          towerHeight = 200;
        else
          towerHeight = 150;
        g.fillRect(towerCount + 10, 400 - towerHeight, 30, towerHeight);
      }
    }

    // Handler class for the three buttons in the start screen
    class StartScreenButtonHandler5 implements ActionListener
    {
      // Handles the button clicks for the three buttons.
      public void actionPerformed(ActionEvent evt)
      {
        String startScreenCommand = new String("");
        startScreenCommand = evt.getActionCommand();
        if (startScreenCommand.equals("Begin Game"))
        {
          paused = true;
          gameScreen = new GamePanel5(parent1, list1);
          list1.show(parent1, "Game Panel");
        }
        else if (startScreenCommand.equals("Instructions"))
          list1.show(parent1, "Instructions Panel");
        else if (startScreenCommand.equals("Quit"))
          System.exit(1);
      }
    }
  }

  // this class shows the instructions screen when called upon
  class InstructionsPanel5 extends JPanel
  {
    private GamePanelsHolder5 parent2; // contains the parent so we can
                                      // switch cards
    private CardLayout list2; // again, so we can switch cards
    private String inFileName; // the name of the instructions File
    private Scanner inScanner; // the Scanner to read the file.
    private JTextArea instructionsArea; // contains instructions. Needs to be
                                // field var so other methods can access it

    // Sets up the components in the instructions screen
    public InstructionsPanel5(GamePanelsHolder5 parent2In, CardLayout list2In)
    {
      parent2 = parent2In;
      list2 = list2In;

      setLayout(new BorderLayout());

      // Area of text where instructions are displayed
      Font textAreaFont = new Font("Dialog", Font.PLAIN, 25);
      instructionsArea = new JTextArea();
      instructionsArea.setLineWrap(true);
      instructionsArea.setWrapStyleWord(true);
      instructionsArea.setFont(textAreaFont);
      instructionsArea.setEditable(false);
      instructionsArea.setMargin(new Insets(10, 10, 10, 10));

      inFileName = new String("Instructions5.txt");
      inScanner = null;
      openFile();
      readFile();

      // Allows for user to scroll through instructions with scroll bar
      JScrollPane scrollPane = new JScrollPane(instructionsArea,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      scrollPane.getVerticalScrollBar().setPreferredSize(
        new Dimension(30, getHeight())); // Resizes the scrollbar to make
                                        // it bigger

      Font buttonFont = new Font("Dialog", Font.PLAIN, 40);
      BackButtonHandler5 bbh = new BackButtonHandler5();

      // Buttons to go back home or to game.
      JButton backButton = new JButton("Back to Home");
      backButton.setFont(buttonFont);
      backButton.addActionListener(bbh);
      backButton.setPreferredSize(new Dimension(getWidth(), 50));

      JButton backToGameButton = new JButton("Go to Game");
      backToGameButton.setFont(buttonFont);
      backToGameButton.addActionListener(bbh);
      backToGameButton.setPreferredSize(new Dimension(getWidth(), 50));

      add(backButton, BorderLayout.NORTH);
      add(backToGameButton, BorderLayout.SOUTH);
      add(scrollPane, BorderLayout.CENTER);
    }

    // Opens the instructions file so it can be scanned
    public void openFile()
    {
      File inFile = new File(inFileName);
      try
      {
        inScanner = new Scanner(inFile);
      }
      catch (FileNotFoundException e)
      {
        System.err.println("Sorry. " + inFileName + " could not be "
          + "found or accessed.");
        System.exit(2);
      }
    }

    // Reads the contents of the file and puts it into the text area
    public void readFile()
    {
      while (inScanner.hasNext())
      {
        String nextLineOfInstructions = new String("");
        nextLineOfInstructions = inScanner.nextLine();
        instructionsArea.append(nextLineOfInstructions + "\n");
      }
    }

    // Handles the back button to go to the home page.
    class BackButtonHandler5 implements ActionListener
    {
      // Goes to home page once button is clicked
      public void actionPerformed(ActionEvent evt)
      {
        String commandComingIn = new String("");
        commandComingIn = evt.getActionCommand();
        if (commandComingIn.equals("Back to Home"))
        {
          list2.show(parent2, "Start Panel");
        }
        else if (commandComingIn.equals("Go to Game"))
        {
          list2.show(parent2, "Game Panel");
        }
      }
    }
  }

 // this class shows the game panel where the game is played.
  class GamePanel5 extends JPanel
  {
    private GamePanelsHolder5 parent3; // contains the parent so we can
                                      // switch cards
    private CardLayout list3; // again, so we can switch cards
    private StatisticsPanel5 statisticsPanel; // information about the game
    private JTextArea alertArea; // alerts of earthquakes and such
    private DistrictInfoPanel5 districtInfoPanel; // info about a district
    private MapPanel5 mapPanel; // Shows the map of the game
    private JButton pauseButton; // So it can be paused in the handler
    private boolean quizMode; // Is the quiz on?
    private Timer gameTimer; // The timer to show the time
    private double secondsElapsed; // The real life seconds passed
    private int population; // The number of people left in the world
    private int money; // Amount of money player has.
    private JLabel amountOfMoney; // label to show the money
    private int epicenterX; // Coordinates for the foreshock epicenter
    private int epicenterY; // (x, y) in MATH FORM!

    // Sets up the JPanels in the game screen
    public GamePanel5(GamePanelsHolder5 parent3In, CardLayout list3In)
    {
      requestFocusInWindow();

      parent3 = parent3In;
      list3 = list3In;

      quizMode = false;

      GameTimerHandler5 gth = new GameTimerHandler5();
      gameTimer = new Timer(100, gth);
      secondsElapsed = 0.0;

      population = 720720;
      money = 60000;

      epicenterX = -1;
      epicenterY = -1;

      setLayout(new BorderLayout());

      createGamePlayPanel();
      createGameInfoPanel();
      createGameActionPanel();
      createDistrictInfoPanel();
      createMapPanel();

      add(districtInfoPanel, BorderLayout.EAST);
      add(mapPanel, BorderLayout.CENTER);
    }

    // To handle the game Timer
    class GameTimerHandler5 implements ActionListener
    {
      // handles the game timer functions
      public void actionPerformed(ActionEvent evt)
      {

        if (population == 0)
        {
          paused = true;
          gameTimer.stop();
          pauseButton.setText("Pause");
          mapPanel.endgame();
        }

        secondsElapsed += 0.1;
        statisticsPanel.repaint();
        districtInfoPanel.repaint();
        mapPanel.repaint();
        if ((int)(secondsElapsed % 90) == 5 && secondsElapsed % 90 < 5.1)
        {
          alertArea.append("New Foreshock Recorded!\n");

          epicenterX = (int)(Math.random() * mapPanel.getWidth());
          epicenterY = (int)(Math.random() * mapPanel.getHeight());
        }
        else if ((int)(secondsElapsed % 90) == 65 &&
          secondsElapsed % 90 < 65.1)
        {

          int actualRow = -1;
          int actualColumn = -1;
          int rowNum = -1;
          char columnChar = '?';
          int quakeDeaths = -1;

          actualRow = 3 - (int)(4.0 * epicenterY / (double)
            mapPanel.getHeight()); // The 3 - is to convert from math to java

          actualColumn = (int)(4.0 * epicenterX / (double)
            mapPanel.getWidth());

          rowNum = actualRow + 1;

          if (actualColumn == 0)
            columnChar = 'A';
          else if (actualColumn == 1)
            columnChar = 'B';
          else if (actualColumn == 2)
            columnChar = 'C';
          else if (actualColumn == 3)
            columnChar = 'D';

          quakeDeaths = districtInfoPanel.getDeathsAndDestroy(actualRow,
            actualColumn);

          population -= quakeDeaths;

          if (population == 0)
          {
            paused = true;
            pauseButton.setText("Pause");
            mapPanel.endgame();
          }
          else if (quakeDeaths == 0)
          {
            money += 10000;
            amountOfMoney.setText("Money: $" + money);
          }
          else if (quakeDeaths > 0)
          {
            mapPanel.enterQuizMode();
          }

          alertArea.append("Earthquake in sector " + rowNum + columnChar
            + ". Deaths: " + quakeDeaths + "\n");

          districtInfoPanel.repopulate();
          epicenterX = -1;
          epicenterY = -1;
        }
      }
    }

    // creates the game play panel for navigation
    public void createGamePlayPanel()
    {
      JPanel gamePlayPanel = new JPanel();
      gamePlayPanel.setBackground(Color.GRAY);
      gamePlayPanel.setPreferredSize(new Dimension(getWidth(), 100));
      gamePlayPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

      // Game Name label
      Font gameNameLabelFont = new Font("Dialog", Font.BOLD, 35);
      JLabel gameNameLabel = new JLabel("Seismo World");
      gameNameLabel.setForeground(Color.BLACK);
      gameNameLabel.setFont(gameNameLabelFont);

      GamePlayPanelButtonHandler5 gppbh = new GamePlayPanelButtonHandler5();
      Font navigationButtonFont = new Font("Dialog", Font.PLAIN, 20);

      // Navigation buttons
      pauseButton = new JButton("Start");
      pauseButton.setFont(navigationButtonFont);
      pauseButton.addActionListener(gppbh);
      pauseButton.setPreferredSize(new Dimension(150, 80));

      JButton goToInstructionsButton = new JButton("Instructions");
      goToInstructionsButton.setFont(navigationButtonFont);
      goToInstructionsButton.addActionListener(gppbh);
      goToInstructionsButton.setPreferredSize(new Dimension(150, 80));

      JButton goToHomeButton = new JButton("Exit Game");
      goToHomeButton.setFont(navigationButtonFont);
      goToHomeButton.addActionListener(gppbh);
      goToHomeButton.setPreferredSize(new Dimension(150, 80));

      gamePlayPanel.add(gameNameLabel);
      gamePlayPanel.add(pauseButton);
      gamePlayPanel.add(goToInstructionsButton);
      gamePlayPanel.add(goToHomeButton);

      add(gamePlayPanel, BorderLayout.NORTH);
    }

    // Handles buttons for game play panel
    class GamePlayPanelButtonHandler5 implements ActionListener
    {
      // called when these buttons are clicked for navigation
      public void actionPerformed(ActionEvent evt)
      {
        String navigationCommand = new String("");
        navigationCommand = evt.getActionCommand();
        if (navigationCommand.equals("Pause"))
        {
          paused = true;
          gameTimer.stop();
          pauseButton.setText("Resume");
        }
        else if (navigationCommand.equals("Resume") ||
          navigationCommand.equals("Start"))
        {
          paused = false;
          gameTimer.start();
          pauseButton.setText("Pause");
        }
        else if (navigationCommand.equals("Instructions"))
        {
          paused = true;
          gameTimer.stop();
          pauseButton.setText("Resume");
          list3.show(parent3, "Instructions Panel");
        }
        else if (navigationCommand.equals("Exit Game"))
        {
          paused = true;
          list3.show(parent3, "Start Panel");
        }
      }
    }

    // creates the game info panel for game data
    public void createGameInfoPanel()
    {
      JPanel gameInfoPanel = new JPanel();
      gameInfoPanel.setBackground(Color.GRAY);
      gameInfoPanel.setPreferredSize(new Dimension(getWidth(), 100));
      gameInfoPanel.setLayout(new GridLayout(1, 2, 10, 10));

      // shows the game data such as days, deaths, etc.
      statisticsPanel = new StatisticsPanel5();
      statisticsPanel.setBackground(Color.LIGHT_GRAY);

      // shows new alerts such as new earthquakes / foreshocks
      Font alertFont = new Font("Dialog", Font.PLAIN, 15);
      alertArea = new JTextArea();
      alertArea.setLineWrap(true);
      alertArea.setWrapStyleWord(true);
      alertArea.setFont(alertFont);
      alertArea.setEditable(false);
      alertArea.append("ALERTS:\n");
      alertArea.setBackground(Color.LIGHT_GRAY);
      alertArea.setMargin(new Insets(5, 5, 5, 5));

      // Allows for user to scroll through alerts with scroll bar
      JScrollPane alertScrollPane = new JScrollPane(alertArea,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      alertScrollPane.getVerticalScrollBar().setPreferredSize(
        new Dimension(10, getHeight())); // Resizes the scrollbar to make
                                        // it smaller

      gameInfoPanel.add(statisticsPanel);
      gameInfoPanel.add(alertScrollPane);

      add(gameInfoPanel, BorderLayout.SOUTH);
    }

    // Class for the statistics panel for game info
    class StatisticsPanel5 extends JPanel
    {
      public StatisticsPanel5()
      {

      }

      public void paintComponent(Graphics g)
      {
        super.paintComponent(g);
        Font statisticsFont = new Font("Dialog", Font.PLAIN, 20);
        g.setFont(statisticsFont);
        g.setColor(Color.BLACK);

        g.drawString("Deaths: " + (720720 - population), 40, 30); // shows
                                                    // the number of deaths
        int greenLength = -1;
        greenLength = (int)(300 * population / 720720.0);
        g.setColor(Color.GREEN);
        g.fillRect(40, 40, greenLength, 20); // A bar showing how many
                                            // people are left
        g.setColor(Color.RED);
        g.fillRect(40 + greenLength, 40, 300 - greenLength, 20);// A bar
                                        // showing how many people are dead

        int dayNumber = -1;
        int minutesDisplay = -1;
        double secondsNum = -1.0;
        String secondsDisplay = new String("");

        dayNumber = (int)(secondsElapsed / 90);
        minutesDisplay = (int)(secondsElapsed / 60);
        secondsNum = (int)((secondsElapsed % 60) * 10) / 10.0;
        secondsDisplay = "" + secondsNum;

        if (secondsDisplay.length() == 3)
          secondsDisplay = "0" + secondsDisplay;

        g.setColor(Color.BLACK);
        g.drawString("Day: " + dayNumber + " (" + minutesDisplay + ":" +
          secondsDisplay + ")", 40, 90); // shows what day we are on
                            // and how much real life time has passed
      }
    }

    // creates the game play panel for user actions
    public void createGameActionPanel()
    {
      JPanel gameActionPanel = new JPanel();
      gameActionPanel.setBackground(Color.GRAY);
      gameActionPanel.setPreferredSize(new Dimension(200, getHeight()));

      CardLayout gameActionCards = new CardLayout();
      gameActionPanel.setLayout(gameActionCards);

      // Jpanel for the draw circle panel for game action
      JPanel drawCirclePanel = new JPanel();
      drawCirclePanel.setBackground(Color.GRAY);
      drawCirclePanel.setLayout(new FlowLayout());

      // Menu to navigate in card layout
      JMenuBar navigationMenuBar = createNavigationMenu(gameActionCards,
        gameActionPanel);
      drawCirclePanel.add(navigationMenuBar);

      // Checkboxes to see which circles should show up
      JCheckBox circle1Box = new JCheckBox("Circle 1:");
      circle1Box.setSelected(false);
      circle1Box.setPreferredSize(new Dimension(200, 30));
      circle1Box.setBackground(Color.RED);

      JCheckBox circle2Box = new JCheckBox("Circle 2:");
      circle2Box.setSelected(false);
      circle2Box.setPreferredSize(new Dimension(200, 30));
      circle2Box.setBackground(Color.CYAN);

      JCheckBox circle3Box = new JCheckBox("Circle 3:");
      circle3Box.setSelected(false);
      circle3Box.setPreferredSize(new Dimension(200, 30));
      circle3Box.setBackground(Color.YELLOW);

      JCheckBox circle4Box = new JCheckBox("Circle 4:");
      circle4Box.setSelected(false);
      circle4Box.setPreferredSize(new Dimension(200, 30));
      circle4Box.setBackground(Color.WHITE);

      // Text areas for the user to enter the equation
      JTextField textField1 = new JTextField("");
      textField1.setPreferredSize(new Dimension(200, 30));

      JTextField textField2 = new JTextField("");
      textField2.setPreferredSize(new Dimension(200, 30));

      JTextField textField3 = new JTextField("");
      textField3.setPreferredSize(new Dimension(200, 30));

      JTextField textField4 = new JTextField("");
      textField4.setPreferredSize(new Dimension(200, 30));

      CircleCheckBoxHandler5 ccbh = new CircleCheckBoxHandler5(
        circle1Box, circle2Box, circle3Box, circle4Box,
        textField1, textField2, textField3, textField4
      );

      circle1Box.addActionListener(ccbh);
      circle2Box.addActionListener(ccbh);
      circle3Box.addActionListener(ccbh);
      circle4Box.addActionListener(ccbh);

      drawCirclePanel.add(circle1Box);
      drawCirclePanel.add(textField1);
      drawCirclePanel.add(circle2Box);
      drawCirclePanel.add(textField2);
      drawCirclePanel.add(circle3Box);
      drawCirclePanel.add(textField3);
      drawCirclePanel.add(circle4Box);
      drawCirclePanel.add(textField4);

      // Jpanel for the shop panel for game action
      JPanel shopPanel = new JPanel();
      shopPanel.setBackground(Color.GRAY);
      shopPanel.setLayout(new FlowLayout());

      // Menu will be same as above
      JMenuBar navigationMenuBar2 = createNavigationMenu(gameActionCards,
        gameActionPanel);
      shopPanel.add(navigationMenuBar2);

      // Label to show how much money you have
      Font shoppingFont = new Font("Dialog", Font.PLAIN, 15);

      amountOfMoney = new JLabel("Money: $" + money);
      amountOfMoney.setForeground(Color.WHITE);
      amountOfMoney.setFont(shoppingFont);
      amountOfMoney.setPreferredSize(new Dimension(200, 100));
      amountOfMoney.setHorizontalAlignment(JLabel.CENTER);
      shopPanel.add(amountOfMoney);

      // Buttons to buy products. These buttons use a Grid Layout so it
      // can place multiple lines of text in one button using JLabels

      // Button to buy a new Seismology Center
      JButton buyCenterButton = new JButton("");
      buyCenterButton.setPreferredSize(new Dimension(200, 100));
      buyCenterButton.setLayout(new GridLayout(4, 1));
      ShopPanelButtonHandler5 spbh1 = new ShopPanelButtonHandler5(
        "New center");
      buyCenterButton.addActionListener(spbh1);
      buyCenterButton.add(new JLabel()); // Works as a padding.

      // Top Label
      JLabel buyCenterButtonLabel1 = new JLabel("Buy a Seismology");
      buyCenterButtonLabel1.setFont(shoppingFont);
      buyCenterButtonLabel1.setHorizontalAlignment(JLabel.CENTER);
      buyCenterButton.add(buyCenterButtonLabel1);

      // Bottom Label
      JLabel buyCenterButtonLabel2 = new JLabel("Center: $20000");
      buyCenterButtonLabel2.setFont(shoppingFont);
      buyCenterButtonLabel2.setHorizontalAlignment(JLabel.CENTER);
      buyCenterButton.add(buyCenterButtonLabel2);

      // Putting it together
      buyCenterButton.add(new JLabel()); // Works as a padding.
      shopPanel.add(buyCenterButton);

      // Button to repair a sector
      JButton repairSectorButton = new JButton("");
      repairSectorButton.setPreferredSize(new Dimension(200, 100));
      repairSectorButton.setLayout(new GridLayout(4, 1));
      ShopPanelButtonHandler5 spbh2 = new ShopPanelButtonHandler5(
        "Repair sector");
      repairSectorButton.addActionListener(spbh2);
      repairSectorButton.add(new JLabel()); // Works as a padding.

      // Top Label
      JLabel repairSectorButtonLabel1 = new JLabel("Repair a Sector:");
      repairSectorButtonLabel1.setFont(shoppingFont);
      repairSectorButtonLabel1.setHorizontalAlignment(JLabel.CENTER);
      repairSectorButton.add(repairSectorButtonLabel1);

      // Bottom Label
      JLabel repairSectorButtonLabel2 = new JLabel("$30000");
      repairSectorButtonLabel2.setFont(shoppingFont);
      repairSectorButtonLabel2.setHorizontalAlignment(JLabel.CENTER);
      repairSectorButton.add(repairSectorButtonLabel2);

      // Putting it together
      repairSectorButton.add(new JLabel()); // Works as a padding.
      shopPanel.add(repairSectorButton);

      // Putting EVERYTHING together
      gameActionPanel.add(drawCirclePanel, "Draw Circles");
      gameActionPanel.add(shopPanel, "Shop");
      gameActionCards.show(gameActionPanel, "Draw Circles");

      add(gameActionPanel, BorderLayout.WEST);
    }

    // creates the menu to navigate between the two panels above
    public JMenuBar createNavigationMenu(CardLayout innerCardLayout,
      JPanel cardLayoutPanel)
    {
      JMenuBar navMenuBar = new JMenuBar();
			JMenu navMenu = new JMenu("Click to switch panels.");
			JMenuItem toDCP = new JMenuItem("Draw Circles");
			JMenuItem toSP = new JMenuItem("Shop");
			NavigationMenuHandler5 nmh = new NavigationMenuHandler5(innerCardLayout,
        cardLayoutPanel);
			toDCP.addActionListener(nmh);
	    toSP.addActionListener(nmh);
			navMenu.add(toDCP);
			navMenu.add(toSP);
			navMenuBar.add(navMenu);
      navMenuBar.setPreferredSize(new Dimension(200, 40));
			return navMenuBar;
    }

    // handles the navigation Menu
    class NavigationMenuHandler5 implements ActionListener
    {
      private CardLayout cardLayoutForNav; // The card layout for Switch
      private JPanel panelWithCards; // Game Action Panel for the switch

      public NavigationMenuHandler5(CardLayout cardLayoutIn,
        JPanel panelWithCardsIn)
      {
        cardLayoutForNav = cardLayoutIn;
        panelWithCards = panelWithCardsIn;
      }

      // if new navigation is clicked
      public void actionPerformed(ActionEvent evt)
      {
        String whereToGo = new String("");
        whereToGo = evt.getActionCommand();
        cardLayoutForNav.show(panelWithCards, whereToGo);
      }
    }

    // Handles the check check box
    class CircleCheckBoxHandler5 implements ActionListener
    {
      private JCheckBox circle1BoxH; // Check box for circle 1
      private JCheckBox circle2BoxH; // Check box for circle 2
      private JCheckBox circle3BoxH; // Check box for circle 3
      private JCheckBox circle4BoxH; // Check box for circle 4
      private JTextField textField1H; // Text field for circle 1
      private JTextField textField2H; // Text field for circle 2
      private JTextField textField3H; // Text field for circle 3
      private JTextField textField4H; // Text field for circle 4

      // Initializes field variables
      public CircleCheckBoxHandler5(JCheckBox circle1BoxIn, JCheckBox
        circle2BoxIn, JCheckBox circle3BoxIn, JCheckBox circle4BoxIn,
        JTextField textField1In, JTextField textField2In, JTextField
        textField3In, JTextField textField4In)
      {
        circle1BoxH = circle1BoxIn;
        circle2BoxH = circle2BoxIn;
        circle3BoxH = circle3BoxIn;
        circle4BoxH = circle4BoxIn;
        textField1H = textField1In;
        textField2H = textField2In;
        textField3H = textField3In;
        textField4H = textField4In;
      }

      // changes boolean array if check box is clicked
      public void actionPerformed(ActionEvent evt)
      {
        if (circle1BoxH.isSelected())
        {
          String equation1 = new String("");
          String answer1 = new String("");
          equation1 = textField1H.getText();
          answer1 = checkEquation(equation1, 0);
          if (!answer1.equals("perfect"))
          {
            textField1H.setText(answer1);
            circle1BoxH.setSelected(false);
            mapPanel.uncheck(0);
          }
        }
        else
          mapPanel.uncheck(0);
        if (circle2BoxH.isSelected())
        {
          String equation2 = new String("");
          String answer2 = new String("");
          equation2 = textField2H.getText();
          answer2 = checkEquation(equation2, 1);
          if (!answer2.equals("perfect"))
          {
            textField2H.setText(answer2);
            circle2BoxH.setSelected(false);
            mapPanel.uncheck(1);
          }
        }
        else
          mapPanel.uncheck(1);
        if (circle3BoxH.isSelected())
        {
          String equation3 = new String("");
          String answer3 = new String("");
          equation3 = textField3H.getText();
          answer3 = checkEquation(equation3, 2);
          if (!answer3.equals("perfect"))
          {
            textField3H.setText(answer3);
            circle3BoxH.setSelected(false);
            mapPanel.uncheck(2);
          }
        }
        else
          mapPanel.uncheck(2);
        if (circle4BoxH.isSelected())
        {
          String equation4 = new String("");
          String answer4 = new String("");
          equation4 = textField4H.getText();
          answer4 = checkEquation(equation4, 3);
          if (!answer4.equals("perfect"))
          {
            textField4H.setText(answer4);
            circle4BoxH.setSelected(false);
            mapPanel.uncheck(3);
          }
        }
        else
          mapPanel.uncheck(3);

        mapPanel.repaint();
      }

      // A LOOOONG algorithm to check if the equation follows the
      // format: (x-num)^2+(y-num)^2=num
      public String checkEquation(String equationIn, int indexOfCircle)
      {
        // Local variables.
        String reply = new String("");
        int indexOfFirstParen = -1;
        String argument1 = new String("");
        int circleXCoord = -1;
        int indexOfSecondParen = -1;
        String argument2 = new String("");
        int circleYCoord = -1;
        int circleRadiusSquared = -1;

        // Checks for first parentheses
        if (!equationIn.startsWith("(x-"))
          reply = "must start with (x-num)^2";
        else
        {
          equationIn = equationIn.substring(3);
          indexOfFirstParen = equationIn.indexOf(')');
          if (indexOfFirstParen == -1)
            reply = "missing closing parentheses";
          else
          {
            argument1 = equationIn.substring(0, indexOfFirstParen);

            //  Checks if the argument is an int (no decimals required)
            try
            {
              circleXCoord = Integer.parseInt(argument1);

              equationIn = equationIn.substring(indexOfFirstParen + 1);
              // Checks if quantity is being squared
              if (!equationIn.startsWith("^2"))
                reply = "square the quantity";
              else
              {
                equationIn = equationIn.substring(2);
                // Checks for second parentheses
                if (!equationIn.startsWith("+(y-"))
                  reply = "must have +(y-num)^2";
                else
                {
                  equationIn = equationIn.substring(4);
                  indexOfSecondParen = equationIn.indexOf(')');
                  if (indexOfSecondParen == -1)
                    reply = "missing closing parentheses";
                  else
                  {
                    argument2 = equationIn.substring(0, indexOfSecondParen);

                    // Checks if argument is an int
                    try
                    {
                      circleYCoord = Integer.parseInt(argument2);
                      circleYCoord = mapPanel.getHeight() - circleYCoord;
                      // Converts from math to java

                      equationIn = equationIn.substring(indexOfSecondParen + 1);
                      // Checks if quantity is squared
                      if (!equationIn.startsWith("^2"))
                        reply = "square the quantity";
                      else
                      {
                        equationIn = equationIn.substring(2);
                        // Checks if the equation is an equation (hehe)
                        if (!equationIn.startsWith("="))
                          reply = "equal sign misplaced";
                        else
                        {
                          equationIn = equationIn.substring(1);

                          // Checks if there is a constant to the right
                          try
                          {
                            circleRadiusSquared = Integer.parseInt(equationIn);

                            // YAY!! Everything is perfect!
                            reply = "perfect";
                          }
                          catch (NumberFormatException e)
                          {
                            reply = "isn't set equal to a number";
                          }
                        }
                      }
                    }
                    catch (NumberFormatException e)
                    {
                      reply = "illegal y argument";
                    }
                  }
                }
              }
            }
            catch (NumberFormatException e)
            {
              reply = "illegal x argument";
            }
          }
        }

        // Fill in the values of the array
        if (reply.equals("perfect"))
        {
          mapPanel.updateCircles(indexOfCircle, circleXCoord, circleYCoord,
            circleRadiusSquared);
        }

        // Return error message
        return reply;
      }
    }

    // handles the buttons in Shop panel
    class ShopPanelButtonHandler5 implements ActionListener
    {
      private String shopCommand; // To tell what button was clicked

      public ShopPanelButtonHandler5(String shopCommandIn)
      {
        shopCommand = shopCommandIn;
      }
      // is performed when button is clicked
      public void actionPerformed(ActionEvent evt)
      {
        if (shopCommand.equals("New center"))
        {
          if (money < 20000)
            amountOfMoney.setText("Not enough money: $" + money);
          else
          {
            money -= 20000;
            amountOfMoney.setText("Money: $" + money);
            districtInfoPanel.enterNewCenterMode();
          }
        }
        else if (shopCommand.equals("Repair sector"))
        {
          if (money < 30000)
            amountOfMoney.setText("Not enough money: $" + money);
          else
          {
            money -= 30000;
            amountOfMoney.setText("Money: $" + money);
            districtInfoPanel.enterRepairSectorMode();
          }
        }
      }
    }

    // creates the district info panel for district data
    public void createDistrictInfoPanel()
    {
      districtInfoPanel = new DistrictInfoPanel5();
      districtInfoPanel.setBackground(Color.GRAY);
      districtInfoPanel.setPreferredSize(new Dimension(200, getHeight()));
      districtInfoPanel.setLayout(new BorderLayout());
    }

    // Class for the district info panel
    class DistrictInfoPanel5 extends JPanel
    {
      private boolean startMode; // In the start, there is a placeholder text.
      private boolean newCenterMode; // If your bought something from the shop
      private boolean repairSectorMode;   // you need to place it in a sector
      private boolean invalidSelection; // Can the shop action be performed?
      private int selectedSectorColumn; // These two identify which sector the
      private int selectedSectorRow; // user clicked on.
      private Sector5[][] sectors; // double array contains 16 Sector objects
      private JButton evacuateButton; // the button to evacuate the sector

      // Initializes field variables and creates evacuate button
      public DistrictInfoPanel5()
      {
        startMode = true;
        newCenterMode = false;
        repairSectorMode = false;
        invalidSelection = false;
        setBackground(Color.GRAY);

        sectors = new Sector5[][]
        {
          new Sector5[]{new Sector5(625), new Sector5(625), new Sector5(625),
            new Sector5(625)},
          new Sector5[]{new Sector5(625), new Sector5(625), new Sector5(625),
            new Sector5(625)},
          new Sector5[]{new Sector5(625), new Sector5(625), new Sector5(625),
            new Sector5(625)},
          new Sector5[]{new Sector5(625), new Sector5(625), new Sector5(625),
            new Sector5(625)}
        };

        // Evacuate button
        evacuateButton = new JButton("EVACUATE");
        evacuateButton.setPreferredSize(new Dimension(getWidth(), 100));
        evacuateButton.setBackground(Color.RED);
        evacuateButton.setForeground(Color.WHITE);
        EvacuateButtonHandler5 ebh = new EvacuateButtonHandler5();
        evacuateButton.addActionListener(ebh);
        Font evacuateFont = new Font("Dialog", Font.PLAIN, 30);
        evacuateButton.setFont(evacuateFont);

        setLayout(new BorderLayout());
      }

      // Draws the info on the panel.
      public void paintComponent(Graphics g)
      {
        super.paintComponent(g);
        Font districtInfoFont = new Font("Dialog", Font.PLAIN, 15);
        g.setFont(districtInfoFont);
        g.setColor(Color.WHITE);
        if (startMode)
        {
          g.drawString("Click a sector to ", 10, 30);
          g.drawString("view its info.", 10, 50);
        }
        else if (newCenterMode || repairSectorMode)
        {
          g.drawString("Click a sector to ", 10, 30);
          g.drawString("place the item.", 10, 50);
        }
        else if (invalidSelection)
        {
          g.drawString("Your item cannot be ", 10, 30);
          g.drawString("place here. Money will ", 10, 50);
          g.drawString("be restored to you.", 10, 70);
        }
        else
        {
          int rowNumber = -1;
          char columnLetter = '?';
          Sector5 selectedSector = null;
          int selectedSectorPopulation = -1;
          String isHabitable = new String("");
          String centerCoordinates = new String("");
          String seismologyCenterExists = new String("");
          int distanceFromTheEpicenter = -1;

          rowNumber = selectedSectorRow + 1;

          if (selectedSectorColumn == 0)
            columnLetter = 'A';
          else if (selectedSectorColumn == 1)
            columnLetter = 'B';
          else if (selectedSectorColumn == 2)
            columnLetter = 'C';
          else if (selectedSectorColumn == 3)
            columnLetter = 'D';

          selectedSector = sectors[selectedSectorRow][selectedSectorColumn];

          selectedSectorPopulation = selectedSector.getPopulation();

          if (selectedSector.getDestroyed())
            isHabitable = "No";
          else
            isHabitable = "Yes";

          centerCoordinates = selectedSector.getCenterCoordinates();

          if (!centerCoordinates.equals("()"))
          {
            seismologyCenterExists = "Has Seismology Center";
            distanceFromTheEpicenter = selectedSector.getDistance(
              epicenterX, epicenterY);
          }
          else
            seismologyCenterExists = "No Seismology Center";

          g.drawString("Sector: " + rowNumber + columnLetter, 10, 30);
          g.drawString("Population: " + selectedSectorPopulation, 10, 60);
          g.drawString("Habitable: " + isHabitable, 10, 90);
          g.drawLine(0, 100, getWidth(), 100);
          g.drawString(seismologyCenterExists, 10, 130);
          if (seismologyCenterExists.equals("Has Seismology Center"))
          {
            g.drawString("Coordinates: " + centerCoordinates, 10, 160);
            if (epicenterX != -1 || epicenterY != -1)
            {
              g.drawString("Distance From Epicenter: ", 10, 190);
              g.drawString("" + distanceFromTheEpicenter, 10, 220);
            }
            else
              g.drawString("Waiting for foreshock... ", 10, 190);
          }
        }
      }

      // start newCenterMode and undo other modes
      public void enterNewCenterMode()
      {
        newCenterMode = true;
        repairSectorMode = false;
        startMode = false;
        invalidSelection = false;

        removeAll();
        repaint();
      }

      // start repairSectorMode and undo other modes
      public void enterRepairSectorMode()
      {
        repairSectorMode = true;
        newCenterMode = false;
        startMode = false;
        invalidSelection = false;

        removeAll();
        repaint();
      }

      // undo all modes and return to basic view state.
      public void enterNormalMode()
      {
        repairSectorMode = false;
        newCenterMode = false;
        startMode = false;
        invalidSelection = false;

        add(evacuateButton, BorderLayout.SOUTH);
        repaint();
      }

      // receives click info from map panel and decides what to do.
      public void receiveColumnRowInfo(int clickColumn, int clickRow)
      {
        selectedSectorColumn = clickColumn;
        selectedSectorRow = clickRow;
        Sector5 selectedSectorForInfo = sectors[clickRow][clickColumn];

        if (startMode)
          enterNormalMode();
        else if (newCenterMode)
        {
          // Check for validity then create it!
          String oldCenterCoordinates = new String("");

          oldCenterCoordinates = selectedSectorForInfo.getCenterCoordinates();
          if (oldCenterCoordinates.equals("()")) // No center yet, you can build
          {
            selectedSectorForInfo.newCenter(selectedSectorColumn,
              selectedSectorRow, mapPanel.getWidth(), mapPanel.getHeight());
            enterNormalMode();
          }
          else // Center already exists in this sector
          {
            invalidSelection = true;
            money += 20000;
            amountOfMoney.setText("Money: $" + money);
          }
        }
        else if (repairSectorMode)
        {
          // Check for validity then repair it!
          boolean hasBeenDestroyed = false;

          hasBeenDestroyed = selectedSectorForInfo.getDestroyed();
          if (hasBeenDestroyed) // It's desroyed, you can repair it.
          {
            selectedSectorForInfo.restore();
            enterNormalMode();
          }
          else // It's habitable, you can't repair it.
          {
            invalidSelection = true;
            money += 30000;
            amountOfMoney.setText("Money: $" + money);
          }
        }
        else if (invalidSelection)
          enterNormalMode();

        repopulate();
        repaint();
      }

      // Calculates the populations after deaths and repairs.
      public void repopulate()
      {
        int unEvacuatedSectorCount = 0;
        int peoplePerSector = -1;

        for (int rowCount1 = 0; rowCount1 < 4; rowCount1++)
        {
          for (int columnCount1 = 0; columnCount1 < 4; columnCount1++)
          {
            if (!sectors[rowCount1][columnCount1].getEvacuated())
              unEvacuatedSectorCount++;
          }
        }

        peoplePerSector = (int)(population / (double) unEvacuatedSectorCount);

        for (int rowCount2 = 0; rowCount2 < 4; rowCount2++)
        {
          for (int columnCount2 = 0; columnCount2 < 4; columnCount2++)
          {
            if (!sectors[rowCount2][columnCount2].getEvacuated())
              sectors[rowCount2][columnCount2].setPopulation(peoplePerSector);
          }
        }
      }

      // destroy the sector from the quake and return the number of deaths
      public int getDeathsAndDestroy(int rowIn, int columnIn)
      {
        Sector5 destroyedSector = sectors[rowIn][columnIn];
        int deathsFromQuake = -1;
        deathsFromQuake = destroyedSector.getPopulation();
        destroyedSector.destroy();
        return deathsFromQuake;
      }

      // Handles the evacuate button
      class EvacuateButtonHandler5 implements ActionListener
      {
        // evacuates the appropriate sector, then repopulates.
        public void actionPerformed(ActionEvent evt)
        {
          sectors[selectedSectorRow][selectedSectorColumn].evacuate();
          repopulate();
          repaint();
        }
      }
    }

    // creates the map panel for displaying the map
    public void createMapPanel()
    {
      mapPanel = new MapPanel5();
      mapPanel.setBackground(Color.WHITE);
      mapPanel.setLayout(null);
    }

    // Class for the map panel
    class MapPanel5 extends JPanel
    {
      private int[] centerXVals; // The center x value for the circles
      private int[] centerYVals; // The center y value for the circles
      private int[] circleRadii; // The radii for the circles
      private Color[] circleColors; // The color for each circle
      private boolean[] circleChecked; // Is the checkbox checked?
      private int selectedMapSectorColumn; // Again, to identify which sector
      private int selectedMapSectorRow; // was selected
      private Image mapImage; // Image for the map.
      private String mapImageName; // File name of the image.
      private PrintWriter toFeedback; // writes to the file to store feedback.
      private String feedbackFileName; // name of the feedback file
      private JTextArea feedbackArea; // area to submit feedback
      private String[] quizAnswers; // The answers to the quiz.

      // Initializes arrays
      public MapPanel5()
      {
        centerXVals = new int[]{-1, -1, -1, -1};
        centerYVals = new int[]{-1, -1, -1, -1};
        circleRadii = new int[]{-1, -1, -1, -1};
        circleColors = new Color[]{Color.RED, Color.CYAN, Color.YELLOW,
          Color.WHITE};
        circleChecked = new boolean[]{false, false, false, false};

        selectedMapSectorColumn = -1;
        selectedMapSectorRow = -1;

        mapImageName = new String("Map5.jpg");
        getMyImage();

        feedbackFileName = new String("Feedback5.txt");
        getMyFeedbackFile();

        quizAnswers = new String[]{new String(""), new String(""),
          new String(""), new String("")};

        MapPanelMouseHandler5 mpmh = new MapPanelMouseHandler5();
        addMouseListener(mpmh);

        setLayout(null);
      }

      // Method to either draw the map or the quiz.
      public void paintComponent(Graphics g)
      {
        super.paintComponent(g);

        if (!quizMode && population != 0)
        {
          // image
          g.drawImage(mapImage, 0, 0, getWidth(), getHeight(), this);

          g.setColor(Color.WHITE);
          // The lines to divide the map into sectors
          g.drawLine(0, (int)(getHeight() / 4.0), getWidth(),
            (int)(getHeight() / 4.0));
          g.drawLine(0, (int)(getHeight() / 2.0), getWidth(),
            (int)(getHeight() / 2.0));
          g.drawLine(0, (int)(3 * getHeight() / 4.0), getWidth(),
            (int)(3 * getHeight() / 4.0));

          g.drawLine((int)(getWidth() / 4.0), 0, (int)(getWidth() / 4.0),
            getHeight());
          g.drawLine((int)(getWidth() / 2.0), 0, (int)(getWidth() / 2.0),
            getHeight());
          g.drawLine((int)(3 * getWidth() / 4.0), 0,
            (int)(3 * getWidth() / 4.0), getHeight());

          // To draw the selected sector
          if (selectedMapSectorColumn != -1 || selectedMapSectorRow != -1)
          {
            g.setColor(Color.YELLOW);
            g.fillRect((int)(selectedMapSectorColumn * getWidth() / 4.0),
              (int)(selectedMapSectorRow * getHeight() / 4.0),
              (int)(getWidth() / 4.0), (int)(getHeight() / 4.0));
          }

          for (int circleArraysCount = 0; circleArraysCount < 4;
            circleArraysCount++)
          {
            if (circleChecked[circleArraysCount])
            {
              g.setColor(circleColors[circleArraysCount]);

              int topLeftX = -1;
              int topLeftY = -1;
              int rectWidth = -1;
              int rectHeight = -1;

              topLeftX = centerXVals[circleArraysCount] -
                circleRadii[circleArraysCount];
              topLeftY = centerYVals[circleArraysCount] -
                circleRadii[circleArraysCount];
              rectWidth = 2 * circleRadii[circleArraysCount];
              rectHeight = 2 * circleRadii[circleArraysCount];

              g.drawOval(topLeftX, topLeftY, rectWidth, rectHeight); // Draws
                                              // the circle in given rectangle.
            }
          }
        }
        else if (quizMode && population != 0)
        {
          drawCoordinatePlane(g);
          drawCircleAndGetAnswers(g);
        }
      }

      // Draws the cartesian coordinate plane for the quiz
      public void drawCoordinatePlane(Graphics g)
      {
        // Axes
        g.setColor(Color.BLACK);
        g.drawLine(200, 175, 370, 175); // x axis
        g.drawLine(285, 90, 285, 260); // y axis

        // Horizontal Grid Lines
        g.setColor(Color.LIGHT_GRAY);
        for (int hGridLine = 100; hGridLine <= 250; hGridLine += 15)
        {
          if (hGridLine != 175) // Not an axis
            g.drawLine(210, hGridLine, 360, hGridLine);
        }

        // Vertical Grid Lines
        for (int vGridLine = 210; vGridLine <= 360; vGridLine += 15)
        {
          if (vGridLine != 285) // Not an axis
            g.drawLine(vGridLine, 100, vGridLine, 250);
        }

        // Labelling the axes:
        Font cartesianPlaneFont = new Font("Serif", Font.PLAIN, 15);
        g.setColor(Color.BLACK);
        g.setFont(cartesianPlaneFont);
        g.drawString("y", 287, 98);
        g.drawString("x", 362, 173);

        // Labelling the increments on the axes
        g.drawString("0", 275, 187); // Origin
        g.drawString("1", 295, 187); // Next 4 are for the x axis
        g.drawString("2", 310, 187);
        g.drawString("3", 325, 187);
        g.drawString("4", 340, 187);
        g.drawString("1", 275, 165); // Next 4 are for the y axis
        g.drawString("2", 275, 150);
        g.drawString("3", 275, 135);
        g.drawString("4", 275, 120);
      }

      // draws the circle and gets the answer
      public void drawCircleAndGetAnswers(Graphics g)
      {
        String quizEquation = new String("");
        int quizRadius = -1;
        int quizXCoord = -10;
        int quizYCoord = -10;
        int maxRadius = -1;

        // Step 1: Select a center point with x's and y's
        // between 4 and -4
        quizXCoord = (int)(Math.random() * 9 - 4);
        quizYCoord = (int)(Math.random() * 9 - 4);

        // Step 2: Calculate the maximum radius that will allow
        // the circle to still fit in the -5 to 5 plane
        maxRadius = 5 - Math.abs(quizXCoord); // Smallest distance from left
                                              // or right of the edge
        maxRadius = Math.min(maxRadius, 5 - Math.abs(quizYCoord));
        // Smallest distance from top or bottom of the edge
        // We take Math.min() so we can choose the smallest radius so it
        // will fit in both bounds.

        // Step 3: Choose a radius between 1 and maxRadius
        quizRadius = (int)(Math.random() * maxRadius + 1);

        // Step 4: Calculate the equation.
        quizEquation = "(x";

        if (quizXCoord > 0)
          quizEquation += "-" + quizXCoord;
        else if (quizXCoord < 0)
          quizEquation += "+" + Math.abs(quizXCoord);

        quizEquation += ")^2+(y";

        if (quizYCoord > 0)
          quizEquation += "-" + quizYCoord;
        else if (quizYCoord < 0)
          quizEquation += "+" + Math.abs(quizYCoord);

        quizEquation += ")^2=" + (int)(Math.pow(quizRadius, 2));

        // Step 5: Store all answers into an array
        quizAnswers[0] = quizEquation;
        quizAnswers[1] = "" + quizRadius;
        quizAnswers[2] = "" + quizXCoord;
        quizAnswers[3] = "" + quizYCoord;

        // Step 6: Draw it onto the coordinate plane
        int quizTopLeftX = -10;
        int quizTopLeftY = -10;
        int quizDiameter = -1;

        quizTopLeftX = 285 + (15 * (quizXCoord - quizRadius));
        quizTopLeftY = 175 - (15 * (quizYCoord + quizRadius));
        quizDiameter = 30 * quizRadius;

        g.setColor(Color.BLUE);
        g.drawOval(quizTopLeftX, quizTopLeftY, quizDiameter, quizDiameter);
      }

      // method to get the image from the file.
      public void getMyImage()
      {
        File mapImageFile = new File("Images\\" + mapImageName);
        try
        {
          mapImage = ImageIO.read(mapImageFile);
        }
        catch(IOException e)
      	{
      		System.err.println("\n\n" + mapImageName + " can't be found.\n\n");
      		System.exit(3);
      	}
      }

      // Method to get the file to submit feedback
      public void getMyFeedbackFile()
      {
        File feedbackFile = new File(feedbackFileName);
        try
        {
          toFeedback = new PrintWriter(new FileWriter(feedbackFile, true));
        }
        catch(IOException e)
      	{
      		System.err.println("\n\n" + feedbackFileName + " can't be found."
            + "\n\n");
      		System.exit(4);
      	}
      }

      // Method to update the circle arrays
      public void updateCircles(int indexOfCircleIn, int circleXCoordIn,
        int circleYCoordIn, int circleRadiusSquaredIn)
      {
        centerXVals[indexOfCircleIn] = circleXCoordIn;
        centerYVals[indexOfCircleIn] = circleYCoordIn;
        circleRadii[indexOfCircleIn] = (int)(Math.sqrt(circleRadiusSquaredIn));
        circleChecked[indexOfCircleIn] = true;
      }

      // Void to uncheck in the boolean method
      public void uncheck(int indexToBeUnchecked)
      {
        circleChecked[indexToBeUnchecked] = false;
      }

      // Creates the quiz screen when there are deaths from the quake
      public void enterQuizMode()
      {
        paused = true;
        gameTimer.stop();
        pauseButton.setText("Resume");

        JOptionPane.showMessageDialog(null, "You have failed to evacuate "
          + "the correct district.\n You're seniors have requested for you "
          + "to redo your training.", "Training Alert",
          JOptionPane.WARNING_MESSAGE);

        quizMode = true;
        repaint();

        Font quizFont = new Font("Dialog", Font.PLAIN, 20);

        // Enter the equation
        JTextField equationField = new JTextField("Enter equation "
          + "(no spaces, with ()s).");
        equationField.setBounds(0, 0, getWidth(), 50);
        equationField.setFont(quizFont);

        // Enter the radius
        JTextField radiusField = new JTextField("Enter the radius.");
        radiusField.setBounds(0, 70, (int)(getWidth() / 2.0), 50);
        radiusField.setFont(quizFont);

        // Enter the x coordinate for the center
        JTextField xCoordField = new JTextField("The x value "
          + "of center.");
        xCoordField.setBounds(0, 140, (int)(getWidth() / 2.0), 50);
        xCoordField.setFont(quizFont);

        // Enter the y coordinate for the center
        JTextField yCoordField = new JTextField("The y value "
          + "of center.");
        yCoordField.setBounds(0, 210, (int)(getWidth() / 2.0), 50);
        yCoordField.setFont(quizFont);

        JButton submitQuizButton = new JButton("Submit Quiz");
        submitQuizButton.setBounds(0, getHeight() - 50, getWidth(), 50);
        submitQuizButton.setFont(quizFont);

        SubmitQuizButtonHandler5 sqbh = new SubmitQuizButtonHandler5(
          equationField, radiusField, xCoordField, yCoordField);
        submitQuizButton.addActionListener(sqbh);

        add(equationField);
        add(radiusField);
        add(xCoordField);
        add(yCoordField);
        add(submitQuizButton);
      }

      // After you're done, you go back to the normal game
      public void exitQuizMode()
      {
        removeAll();

        paused = false;
        gameTimer.start();
        pauseButton.setText("Pause");
        quizMode = false;
        repaint();
      }

      // Handles the button that submits the quiz
      class SubmitQuizButtonHandler5 implements ActionListener
      {
        private JTextField fieldForEquation; // user enters the equation
        private JTextField fieldForRadius; // user enters the radius
        private JTextField fieldForXCoord; // user enters the x coord
        private JTextField fieldForYCoord; // user enters the y coord

        // Initializes field variables
        public SubmitQuizButtonHandler5(JTextField fieldForEquationIn,
          JTextField fieldForRadiusIn, JTextField fieldForXCoordIn,
          JTextField fieldForYCoordIn)
        {
          fieldForEquation = fieldForEquationIn;
          fieldForRadius = fieldForRadiusIn;
          fieldForXCoord = fieldForXCoordIn;
          fieldForYCoord = fieldForYCoordIn;
        }

        // if the submit button is clicked, check the answer
        public void actionPerformed(ActionEvent evt)
        {
          boolean allAreCorrect = true;
          String equationAnswer = new String("");
          String radiusAnswer = new String("");
          String xCoordAnswer = new String("");
          String yCoordAnswer = new String("");

          equationAnswer = fieldForEquation.getText();
          radiusAnswer = fieldForRadius.getText();
          xCoordAnswer = fieldForXCoord.getText();
          yCoordAnswer = fieldForYCoord.getText();

          if (!equationAnswer.equals(quizAnswers[0]))
            allAreCorrect = false;
          else if (!radiusAnswer.equals(quizAnswers[1]))
            allAreCorrect = false;
          else if (!xCoordAnswer.equals(quizAnswers[2]))
            allAreCorrect = false;
          else if (!yCoordAnswer.equals(quizAnswers[3]))
            allAreCorrect = false;
          else
            allAreCorrect = true;

          if (allAreCorrect)
          {
            JOptionPane.showMessageDialog(null, "You have gotten the "
              + "correct answer!\nClick OK to return to the game.",
              "Correct Answer", JOptionPane.PLAIN_MESSAGE);
            exitQuizMode();
          }
          else
          {
            JOptionPane.showMessageDialog(null, "Sorry, you gave an "
              + "incorrect answer.\nClick OK to try again.",
              "Wrong Answer", JOptionPane.ERROR_MESSAGE);
          }
        }
      }

      // Creates the final ending screen when population reaches 0
      public void endgame()
      {
        repaint();

        // label saying "Game Over"
        JLabel gameOverLabel = new JLabel("GAME OVER!");
        gameOverLabel.setBounds(50, 50, 300, 100);
        Font gameOverFont = new Font("Dialog", Font.BOLD, 40);
        gameOverLabel.setFont(gameOverFont);

        int daysSurvived = -1;
        daysSurvived = (int)(secondsElapsed / 90);

        // Label to show how many days you survived
        JLabel daysSurvivedLabel = new JLabel("You survived " + daysSurvived
          + " days.");
        daysSurvivedLabel.setBounds(50, 105, 300, 50);
        Font daysSurvivedFont = new Font("Dialog", Font.BOLD, 20);
        daysSurvivedLabel.setFont(daysSurvivedFont);

        // Text area to submit feedback
        Font feedbackFont = new Font("Dialog", Font.PLAIN, 15);
        feedbackArea = new JTextArea();
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        feedbackArea.setFont(feedbackFont);
        feedbackArea.setEditable(true);
        feedbackArea.append("Please enter feedback!\n");
        feedbackArea.setBackground(Color.LIGHT_GRAY);
        feedbackArea.setMargin(new Insets(5, 5, 5, 5));
        feedbackArea.setBounds(50, 200, 300, 75);

        // Button to submit feedback
        JButton feedbackButton = new JButton("Submit Feedback!");
        feedbackButton.setBounds(100, 285, 200, 30);
        Font feedbackButtonFont = new Font("Dialog", Font.PLAIN, 20);
        feedbackButton.setFont(feedbackButtonFont);
        FeedBackButtonHandler5 fbh = new FeedBackButtonHandler5();
        feedbackButton.addActionListener(fbh);

        add(gameOverLabel);
        add(daysSurvivedLabel);
        add(feedbackArea);
        add(feedbackButton);
      }

      // Class to handle the mouse
      class MapPanelMouseHandler5 implements MouseListener
      {
        // figures out which sector was clicked
        public void mousePressed(MouseEvent evt)
        {
          if (!quizMode && population != 0)
          {
            int mouseX = -1;
            int mouseY = -1;

            mouseX = evt.getX();
            mouseY = evt.getY();

            selectedMapSectorColumn = (int)(mouseX / (getWidth() / 4.0));
            selectedMapSectorRow = (int)(mouseY / (getHeight() / 4.0));

            districtInfoPanel.receiveColumnRowInfo(selectedMapSectorColumn,
              selectedMapSectorRow);

            repaint();
          }
        }

        public void mouseReleased(MouseEvent evt) {}
        public void mouseClicked(MouseEvent evt) {}
        public void mouseEntered(MouseEvent evt) {}
        public void mouseExited(MouseEvent evt) {}
      }

      // Class to handle button in ending screen
      class FeedBackButtonHandler5 implements ActionListener
      {
        // submits feedback to the file containing feedback
        public void actionPerformed(ActionEvent evt)
        {
          String feedback = new String("");
          feedback = feedbackArea.getText();
          toFeedback.println("\n\n" + feedback);
          toFeedback.close();
          list3.show(parent3, "Start Panel");
        }
      }
    }
  }
}
