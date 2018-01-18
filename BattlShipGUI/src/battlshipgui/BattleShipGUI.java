/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlshipgui;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.swing.border.*;
/**
 *
 * @author Jason
 */
//Jason Jiang & Tanmy Weng
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.swing.border.*;

public class BattleShipGUI {

    //Global Variables
    private static JButton[][] cpuBoard, playerBoard;
    private static JLabel col, col2, row, row2, blank, blank2, blankSpot, playerMessage, cpuMessage;
    private static JPanel topPanel, promptPanel, playerPanel, cpuPanel, playerButtonPanel, cpuButtonPanel, middlePanel, promptPlayerPanel, promptCpuPanel;
    public static String fileName;
    public static String ships[] = {"Battleship", "Aircraft Carrier", "Submarine", "Destroyer", "Patrol Boat"};
    public static int playerShips[] = {4, 5, 3, 3, 2};
    public static int cpuShips[] = {4, 5, 3, 3, 2};
    public static int hitsC, hitsP, attackP, attackC = 0;
    public static String ship[] = {"B", "C", "S", "D", "P"};
    public static String abc = "ABCDEFGHIJ";
    private static JFrame newFrames;

    //Reads either the player or cpu file
    public static void loadFile(JButton[][] board, String file) throws IOException {
        BufferedReader inputStream = null;

        try {
            inputStream = new BufferedReader(new FileReader(file));
            String lineRead = inputStream.readLine();
            int i = 0;

            while (lineRead != null) {
                String splitted[] = lineRead.split(" "); //Store every position into an array
                for (int j = 0; j < 10; j++) {
                    board[i][j].setActionCommand(splitted[j]);	//Store the first 10 into first line
                    board[i][j].setText(splitted[j]);
                }
                i++; //Switch line
                lineRead = inputStream.readLine();
            }
        } catch (FileNotFoundException exception) {
            System.out.println("Error opening file");
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    //When a button is pressed:
    private static class ButtonFunction implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            String eventName = event.getActionCommand();
            JButton button = (JButton) event.getSource();

            ImageIcon hit = new ImageIcon("H.png");
            ImageIcon miss = new ImageIcon("M.png");

            attackP++;
            if (eventName.equals("*")) {
                //Player Miss
                button.setIcon(miss);
                button.setDisabledIcon(miss);
                button.setEnabled(false);
                playerMessage.setText("You missed, sir!");
            } else {
                //Player Hit
                hitsP++;
                button.setIcon(hit);
                button.setDisabledIcon(hit);
                playerMessage.setText("Direct hit, nice shot Sir!");
                button.setEnabled(false);
                for (int i = 0; i < 5; i++) {
                    if (eventName.equals(ship[i])) {
                        cpuShips[i]--;
                        if (cpuShips[i] == 0) {
                            playerMessage.setText("Direct hit, nice shot Sir! You have destroyed a " + ships[i]);
                            cpuShips[i]++;
                        }
                    }
                }
            }
            //If Player has 17 hits, player Wins
            if (hitsP == 17) {
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        cpuBoard[i][j].setEnabled(false);
                    }
                }
                gameStats(hitsC, attackC, hitsP, attackP); // Stops the game when player sank all of the ships
            } else {
                cpuMove(playerBoard);
            }
        }
    }

    //CPU Attacks
    public static void cpuMove(JButton player[][]) {

        Random cpu = new Random();
        ImageIcon hit = new ImageIcon("H.png");
        ImageIcon miss = new ImageIcon("M.png");
        int row = cpu.nextInt(10);
        int col = cpu.nextInt(10);
        String check = player[row][col].getText();

        while (check.equals("H") || check.equals("M")) {
            row = cpu.nextInt(10);
            col = cpu.nextInt(10);
            check = player[row][col].getText();
        }

        attackC++;
        if (check.equals("*")) {
            //CPU Miss
            player[row][col].setText("M");
            player[row][col].setIcon(miss);
            player[row][col].setDisabledIcon(miss);
            cpuMessage.setText("CPU has attacked " + abc.charAt(row) + col + " and Missed!");
        } else {
            //CPU Hit
            hitsC++;
            player[row][col].setText("H");
            player[row][col].setIcon(hit);
            player[row][col].setDisabledIcon(hit);
            cpuMessage.setText("Cpu has attacked " + abc.charAt(row) + col + " and hit!");
            for (int i = 0; i < 5; i++) {
                if (check.equals(ship[i])) {
                    playerShips[i]--;
                    if (playerShips[i] == 0) {
                        cpuMessage.setText("Cpu has attacked" + abc.charAt(row) + col + " and destroyed a " + ships[i]);
                        playerShips[i]++;
                    }
                }
            }
        }
        //Checks if CPU has 17 hits, if so, the CPU wins
        if (hitsC == 17) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    cpuBoard[i][j].setEnabled(false);
                }
            }
            gameStats(hitsC, attackC, hitsP, attackP);
        }
    }

    //End statistics popup menu
    public static void gameStats(int cpuHit, int cpuAttack, int playerHit, int playerAttack) {
        JLabel winner = new JLabel();
        if (cpuHit == 17) {
            newFrames = new JFrame("CPU Won!");
            winner.setText("CPU Won!");
        } else {
            newFrames = new JFrame("Player Won!");
            winner.setText("Player Won!");
        }
        JPanel winners = new JPanel();
        JPanel cpuStatsPanel = new JPanel();
        JPanel playerStatsPanel = new JPanel();
        JLabel cHitLabel = new JLabel("Hits: " + cpuHit);
        JLabel cMissLabel = new JLabel("Misses: " + (cpuAttack - cpuHit));
        JLabel cAttackLabel = new JLabel("Attacks " + (cpuAttack));
        JLabel cAccuracyLabel = new JLabel("Accuracy " + ((double) cpuHit / (double) cpuAttack * 100) + "%");
        JLabel pHitLabel = new JLabel("Hits: " + playerHit);
        JLabel pMissLabel = new JLabel("Misses: " + (playerAttack - playerHit));
        JLabel pAttackLabel = new JLabel("Attacks " + (playerAttack));
        JLabel pAccuracyLabel = new JLabel("Accuracy " + ((double) playerHit / (double) playerAttack * 100) + "%");
        playerStatsPanel.setLayout(new GridLayout(4, 1, 3, 3));
        cpuStatsPanel.setLayout(new GridLayout(4, 1, 3, 3));

        Border border = BorderFactory.createLineBorder(Color.black, 3, true);
        winners.setBorder(BorderFactory.createTitledBorder(border, "WINNER!", TitledBorder.CENTER, TitledBorder.TOP));
        cpuStatsPanel.setBorder(BorderFactory.createTitledBorder(border, "CPU Stats", TitledBorder.LEFT, TitledBorder.TOP));
        playerStatsPanel.setBorder(BorderFactory.createTitledBorder(border, "Player Stats", TitledBorder.LEFT, TitledBorder.TOP));
        cpuStatsPanel.add(cHitLabel);
        cpuStatsPanel.add(cMissLabel);
        cpuStatsPanel.add(cAttackLabel);
        cpuStatsPanel.add(cAccuracyLabel);
        winners.add(winner);
        playerStatsPanel.add(pHitLabel);
        playerStatsPanel.add(pMissLabel);
        playerStatsPanel.add(pAttackLabel);
        playerStatsPanel.add(pAccuracyLabel);
        newFrames.setLayout(new GridLayout(3, 1, 3, 3));
        newFrames.add(winners);
        newFrames.add(playerStatsPanel);
        newFrames.add(cpuStatsPanel);
        newFrames.setVisible(true);
        newFrames.setResizable(true);
        newFrames.pack();
    }

    //Main Method
    public static void main(String[] args) throws IOException {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JFrame frame = new JFrame("Battleship GUI");
        topPanel = new JPanel();
        playerPanel = new JPanel();
        cpuPanel = new JPanel();
        playerButtonPanel = new JPanel();
        cpuButtonPanel = new JPanel();
        promptPanel = new JPanel();
        middlePanel = new JPanel();
        promptPlayerPanel = new JPanel();
        promptCpuPanel = new JPanel();

        col = new JLabel();
        row2 = new JLabel();
        row = new JLabel();
        col2 = new JLabel();
        blank = new JLabel();
        blank2 = new JLabel();
        blankSpot = new JLabel();
        playerMessage = new JLabel();
        cpuMessage = new JLabel();

        playerMessage.setText("Please open the PLATER.txt file!");
        cpuMessage.setText("Please open the CPU.txt file!");

        blank2.setIcon(new ImageIcon("blank2.png"));
        blank.setIcon(new ImageIcon("blank.png"));
        col.setIcon(new ImageIcon("cols.png"));
        row.setIcon(new ImageIcon("rows.png"));
        col2.setIcon(new ImageIcon("cols.png"));
        row2.setIcon(new ImageIcon("rows.png"));
        blankSpot.setIcon(new ImageIcon("blankSpot.png"));
        playerBoard = new JButton[10][10];
        cpuBoard = new JButton[10][10];

        //code Starts
        menuBar.add(menu);
        //Choose a file
        JMenuItem menuOpen = new JMenuItem(new AbstractAction("Open") {

            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                int response = fc.showOpenDialog(null);
                if (response == JFileChooser.APPROVE_OPTION) {
                    try {
                        fileName = fc.getSelectedFile().toString(); //Gets File Name
                        //Loads CPU.txt
                        if (fileName.contains("CPU.txt")) {
                            cpuMessage.setText("Successfully load Cpu file");
                            loadFile(cpuBoard, fileName);
                            String playerCheck = playerBoard[1][1].getText();
                            for (int i = 0; i < 10; i++) {
                                for (int j = 0; j < 10; j++) {
                                    cpuBoard[i][j].setText("*");
                                    if (!playerCheck.equals("")) {
                                        cpuBoard[i][j].setEnabled(true);
                                    }
                                }
                            }
                            //Loads PLAYER.txt 
                        } else if (fileName.contains("PLAYER.txt")) {
                            playerMessage.setText("Successfully load player file");
                            loadFile(playerBoard, fileName);
                            String cpuCheck = cpuBoard[1][1].getText();
                            for (int i = 0; i < 10; i++) {
                                for (int j = 0; j < 10; j++) {
                                    if (!cpuCheck.equals("")) {				// check if there is anything in other file
                                        cpuBoard[i][j].setEnabled(true);
                                    }
                                }
                            }
                            //Player picked an incorrect file
                        } else {
                            JFrame newFrame = new JFrame("ERROR!");
                            JPanel newPanel = new JPanel();
                            JLabel newLabel = new JLabel("Invalid file, file has to be PLAYER.txt or CPU.txt");
                            newPanel.setPreferredSize(new Dimension(300, 50));
                            newPanel.add(newLabel);
                            newFrame.setVisible(true);
                            newFrame.setResizable(false);
                            newFrame.setSize(700, 700);
                            newFrame.setContentPane(newPanel);
                            newFrame.pack();
                        }
                    } catch (IOException ex) {
                    }
                }
            }
        });
        menuOpen.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.Event.CTRL_MASK));
        menu.add(menuOpen);

        //JMenuItem button that restarts the game
        JMenuItem menuRestart = new JMenuItem(new AbstractAction("Restart Game") {

            public void actionPerformed(ActionEvent e) {
                //Resets all the values to its intial amount and clears the board
                hitsC = 0;
                hitsP = 0;
                attackP = 0;
                attackC = 0;
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        playerBoard[i][j].setText("");
                        cpuBoard[i][j].setText("");
                        playerBoard[i][j].setIcon(null);
                        cpuBoard[i][j].setIcon(null);
                        playerBoard[i][j].setEnabled(false);
                        cpuBoard[i][j].setEnabled(false);
                    }
                    playerMessage.setText("Please open the PLATER.txt file!");
                    cpuMessage.setText("Please open the CPU.txt file!");
                }
            }
        });
        menuRestart.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.Event.CTRL_MASK));
        menu.add(menuRestart);

        //JMenuItem that closes the program
        JMenuItem menuExit = new JMenuItem(new AbstractAction("Exit") {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menuExit.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.Event.CTRL_MASK));
        menu.add(menuExit);

        //Setting all the button details
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                playerBoard[i][j] = new JButton();
                cpuBoard[i][j] = new JButton();
                playerBoard[i][j].setText("");
                cpuBoard[i][j].setText("");
                playerBoard[i][j].setPreferredSize(new Dimension(50, 50));
                cpuBoard[i][j].setPreferredSize(new Dimension(50, 50));
                cpuBoard[i][j].setBackground(Color.BLACK);
                cpuBoard[i][j].setEnabled(false);// Make the button unclickable until user load files
                playerBoard[i][j].setEnabled(false);
                playerBoard[i][j].addActionListener(new ButtonFunction());
                cpuBoard[i][j].addActionListener(new ButtonFunction());
            }
        }

        cpuPanel.setBorder(BorderFactory.createLineBorder(Color.black, 3, true));
        cpuPanel.add(row);
        playerPanel.add(row2);
        playerPanel.setBorder(BorderFactory.createLineBorder(Color.black, 3, true));
        cpuButtonPanel.setLayout(new GridLayout(10, 10));
        playerButtonPanel.setLayout(new GridLayout(10, 10));

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                cpuButtonPanel.add(cpuBoard[i][j]);
                playerButtonPanel.add(playerBoard[i][j]);
            }
        }

        Border border = BorderFactory.createLineBorder(Color.black, 3, true);
        playerPanel.add(playerButtonPanel);
        cpuPanel.add(cpuButtonPanel);
        promptPlayerPanel.setBorder(BorderFactory.createTitledBorder(border, "Player Messages", TitledBorder.LEFT, TitledBorder.TOP));
        promptCpuPanel.setBorder(BorderFactory.createTitledBorder(border, "CPU Messages", TitledBorder.LEFT, TitledBorder.TOP));
        promptPlayerPanel.add(playerMessage);
        promptCpuPanel.add(cpuMessage);
        promptPanel.setLayout(new GridLayout(2, 1, 3, 3));
        promptPanel.add(promptPlayerPanel);
        promptPanel.add(promptCpuPanel);
        topPanel.add(blank2);
        topPanel.add(col);
        topPanel.add(blank);
        topPanel.add(col2);
        topPanel.setBackground(Color.WHITE);
        middlePanel.add(playerPanel);
        middlePanel.add(cpuPanel);
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(middlePanel, BorderLayout.CENTER);
        frame.add(promptPanel, BorderLayout.SOUTH);
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
