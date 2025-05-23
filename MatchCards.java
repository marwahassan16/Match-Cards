import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;

public class MatchCards {
    class Card {
        String cardName;
        ImageIcon cardImageIcon;

        Card(String cardName, ImageIcon cardImageIcon) {
            this.cardName = cardName;
            this.cardImageIcon = cardImageIcon;
        }
    }

     String[] cardList = {
     "crab","fast","karin","parasite","patrick","pearl",
     "pufferfish","sandy","spongpop","squid"
    };

  int rows = 5;
  int columns = 4;
  int cardWidth = 150;
  int cardHeight = 150;

    ArrayList<Card> cardSet;
    ImageIcon cardBackImageIcon;
    ImageIcon winImageIcon;
    ImageIcon loseImageIcon;

    int boardWidth = columns * cardWidth;
    int boardHeight = rows * cardHeight;

    JFrame frame = new JFrame("Match Cards");
    JLabel textLabel = new JLabel();
    JLabel timerLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel restartGamePanel = new JPanel();
    JButton restartButton = new JButton();

    int errorCount = 0;
    int timeLeft = 120;
    Timer hideCardTimer;
    Timer countdownTimer;
    boolean gameReady = false;
    JButton card1Selected;
    JButton card2Selected;
    ArrayList<JButton> board;

    void setupCards() {
        cardSet = new ArrayList<>();
        for (String cardName : cardList) {
            Image cardImg = new ImageIcon(getClass().getResource("/img/" + cardName + ".jpg")).getImage();
            ImageIcon cardImageIcon = new ImageIcon(cardImg.getScaledInstance(cardWidth, cardHeight, Image.SCALE_SMOOTH));
            cardSet.add(new Card(cardName, cardImageIcon));
        }
        cardSet.addAll(new ArrayList<>(cardSet));

        Image cardBackImg = new ImageIcon(getClass().getResource("/img/back.jpg")).getImage();
        cardBackImageIcon = new ImageIcon(cardBackImg.getScaledInstance(cardWidth, cardHeight, Image.SCALE_SMOOTH));
    }

    void shuffleCards() {
        Collections.shuffle(cardSet);
    }
    void startNewGame() {
        gameReady = false;
        restartButton.setEnabled(false);
        card1Selected = null;
        card2Selected = null;
        shuffleCards();

        for (int i = 0; i < board.size(); i++) {
            board.get(i).setIcon(cardSet.get(i).cardImageIcon);
            board.get(i).setEnabled(true);
        }

        errorCount = 0;
        textLabel.setText("Errors: " + errorCount);

        timeLeft = 120;
        timerLabel.setText("Time: " + timeLeft + "s");
        countdownTimer.restart();

        hideCardTimer.start();
    }

    void checkWinCondition() {
        boolean allMatched = true;
        for (JButton tile : board) {
            if (tile.getIcon() == cardBackImageIcon) {
                allMatched = false;
                break;
            }
        }
        if (allMatched) {
            countdownTimer.stop();
            gameReady = false;
            showEndScreen(winImageIcon);
        }
    }

void showEndScreen(ImageIcon image) {
    countdownTimer.stop();
    hideCardTimer.stop();

    
    Image scaledImg = image.getImage().getScaledInstance(boardWidth, boardHeight, Image.SCALE_SMOOTH);
    ImageIcon scaledIcon = new ImageIcon(scaledImg);

    
    JLabel backgroundLabel = new JLabel(scaledIcon);
    backgroundLabel.setLayout(new BorderLayout()); 

    
    JButton endRestartButton = new JButton("New GAME");
    endRestartButton.setFont(new Font("Algerian", Font.BOLD, 25));
    endRestartButton.setPreferredSize(new Dimension(boardWidth / 2, 40));
    endRestartButton.setFocusable(false);


    endRestartButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.getContentPane().removeAll();
            frame.add(textPanel, BorderLayout.NORTH);
            frame.add(boardPanel, BorderLayout.CENTER);
            frame.add(restartGamePanel, BorderLayout.SOUTH);
            frame.revalidate();
            frame.repaint();
            startNewGame();
        }
    });

  
    JPanel buttonPanel = new JPanel();
    buttonPanel.setOpaque(false);
    buttonPanel.add(endRestartButton);


    backgroundLabel.add(buttonPanel, BorderLayout.SOUTH);

    
    frame.getContentPane().removeAll();
    frame.getContentPane().add(backgroundLabel);
    frame.revalidate();
    frame.repaint();
}

    void hideCards() {
        if (gameReady && card1Selected != null && card2Selected != null) {
            card1Selected.setIcon(cardBackImageIcon);
            card2Selected.setIcon(cardBackImageIcon);
            card1Selected = null;
            card2Selected = null;
        } else {
            for (int i = 0; i < board.size(); i++) {
                board.get(i).setIcon(cardBackImageIcon);
            }
            gameReady = true;
            restartButton.setEnabled(true);
        }
    }

    MatchCards() {
        setupCards();
        shuffleCards();

        frame.setLayout(new BorderLayout());
        frame.setSize(boardWidth, boardHeight + 60);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       
        JPanel introPanel = new JPanel(new BorderLayout());
        ImageIcon introImageIcon = new ImageIcon(new ImageIcon(
            getClass().getResource("/img/startscreen.jpg"))
            .getImage().getScaledInstance(boardWidth, boardHeight, Image.SCALE_SMOOTH));
        JLabel introLabel = new JLabel(introImageIcon);
        introLabel.setLayout(new BorderLayout());

        JButton startGameButton = new JButton("Start Game");
        startGameButton.setFont(new Font("Algerian", Font.BOLD, 25));
        startGameButton.setPreferredSize(new Dimension(boardWidth / 2, 40));
        startGameButton.setFocusable(false);
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(introPanel);
                frame.add(textPanel, BorderLayout.NORTH);
                frame.add(boardPanel, BorderLayout.CENTER);
                frame.add(restartGamePanel, BorderLayout.SOUTH);
                frame.revalidate();
                frame.repaint();
                startNewGame();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(startGameButton);
        introLabel.add(buttonPanel, BorderLayout.SOUTH);
        introPanel.add(introLabel, BorderLayout.CENTER);
        frame.add(introPanel);

        
        textLabel.setFont(new Font("Algerian", Font.PLAIN, 20));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Errors: " + errorCount);

        timerLabel.setFont(new Font("Algerian", Font.PLAIN, 20));
        timerLabel.setHorizontalAlignment(JLabel.CENTER);
        timerLabel.setText("Time: " + timeLeft + "s");

        textPanel.setLayout(new GridLayout(1, 2));
        textPanel.setPreferredSize(new Dimension(boardWidth, 30));
        textPanel.add(textLabel);
        textPanel.add(timerLabel);

        board = new ArrayList<>();
        boardPanel.setLayout(new GridLayout(rows, columns));

        for (int i = 0; i < cardSet.size(); i++) {
            JButton tile = new JButton();
            tile.setPreferredSize(new Dimension(cardWidth, cardHeight));
            tile.setOpaque(true);
            tile.setIcon(cardSet.get(i).cardImageIcon);
            tile.setFocusable(false);
            tile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!gameReady) return;

                    JButton tile = (JButton) e.getSource();
                    if (tile.getIcon() == cardBackImageIcon) {
                        if (card1Selected == null) {
                            card1Selected = tile;
                            int index = board.indexOf(card1Selected);
                            card1Selected.setIcon(cardSet.get(index).cardImageIcon);
                        } else if (card2Selected == null) {
                            card2Selected = tile;
                            int index = board.indexOf(card2Selected);
                            card2Selected.setIcon(cardSet.get(index).cardImageIcon);

                            if (card1Selected.getIcon() != card2Selected.getIcon()) {
                                errorCount++;
                                textLabel.setText("Errors: " + errorCount);
                                hideCardTimer.start();
                            } else {
                                card1Selected = null;
                                card2Selected = null;
                                checkWinCondition();
                            }
                        }
                    }
                }
            });
            board.add(tile);
            boardPanel.add(tile);
        }

        restartButton.setFont(new Font("Algerian", Font.PLAIN, 20));
        restartButton.setText("Restart Game");
        restartButton.setPreferredSize(new Dimension(boardWidth / 2, 30));
        restartButton.setFocusable(false);
        restartButton.setEnabled(false);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewGame();
            }
        });

        restartGamePanel.add(restartButton);

        frame.pack();
        frame.setVisible(true);

        hideCardTimer = new Timer(1000, e -> hideCards());
        hideCardTimer.setRepeats(false);
        hideCardTimer.start();

        countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                timerLabel.setText("Time: " + timeLeft + "s");
                if (timeLeft <= 0) {
                    countdownTimer.stop();
                    gameReady = false;
                    for (JButton tile : board) tile.setEnabled(false);
                    showEndScreen(loseImageIcon);
                }
            }
        });

        winImageIcon = new ImageIcon(getClass().getResource("/img/win.jpg"));
        loseImageIcon = new ImageIcon(getClass().getResource("/img/lose.jpg"));
    }


    public static void main(String[] args) {
        new MatchCards();
    }
}