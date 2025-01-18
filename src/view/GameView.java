package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import model.GameScore;
import model.LetterGenerator;

public class GameView extends JFrame {
    private JPanel letterPanel;
    private JPanel timerPanel;
    private GameScore gameScore;
    private JPanel scorePanel;
    private JPanel headerPanel;  // Panneau pour le titre et le timer

    private JPanel[] wordPanels = new JPanel[3];
    private static JPanel selectedPanel = null;
    private JLabel scoreLabel;
    private int score = 0;
    private JTextField[][] allWordFields;
    private JTextField selectedField = null;
    private List<JButton> letterButtons = new ArrayList<>();
    private JLabel timerLabel; // Affichage du temps
    private Timer timer; // Chronomètre Swing
    private int timeRemaining; // Temps restant en secondes


    public GameView(List<Character> letters, List<Integer> wordLengths,GameScore gameScore) {
    	this.gameScore = gameScore;
        setTitle("Word Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(1500, 1000);
        allWordFields = new JTextField[wordLengths.size()][];
        createHeaderPanel(); 
        // Create sections before adding to frame
        createLetterSection(letters);
        createScoreSection();
       
        // Central Panel for Word Fields
        JPanel centerPanel = new JPanel();
        centerPanel.add(letterPanel);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createTitledBorder("Word Fields"));

        // Create word panels based on word lengths
        for (int i = 0; i < wordLengths.size(); i++) {
            wordPanels[i] = createWordPanel(wordLengths.get(i), i + 1);
            centerPanel.add(wordPanels[i]);
        }
        timerPanel=createTimerSection();
        // Add all panels to the frame
        //add(timerPanel, BorderLayout.EAST);
        headerPanel.add(timerPanel);
        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(scorePanel, BorderLayout.SOUTH);

        setVisible(true);
    }
  //score getter in gameview
    public GameScore getGameScore() {
        return gameScore;
    }
    private void createHeaderPanel() {
        headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));  // FlowLayout à gauche
        headerPanel.setBackground(new Color(255, 230, 204));

        // Ajouter l'icône du jeu
        JLabel iconLabel = new JLabel(new ImageIcon("path/to/your/icon.png"));  // Mettez ici le chemin de votre icône
        iconLabel.setPreferredSize(new Dimension(50, 50));  // Ajustez la taille de l'icône si nécessaire
        headerPanel.add(iconLabel);

        // Ajouter le titre du jeu
        JLabel titleLabel = new JLabel("Word Game");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(new Color(204, 102, 0));
        headerPanel.add(titleLabel);

        // Ajouter le timer à la droite du header
        headerPanel.add(Box.createHorizontalStrut(500));  // Espace entre le titre et le timer
    }
    

    private void createLetterSection(List<Character> letters) {
        letterPanel = new JPanel(new FlowLayout());
        letterPanel.setBorder(BorderFactory.createTitledBorder("Generated Letters"));
        letterPanel.setBackground(new Color(255, 230, 204));

        for (Character letter : letters) {
            JButton letterButton = new JButton(letter.toString());
            letterButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
            letterButton.setBackground(new Color(255, 153, 51));
            letterButton.setForeground(Color.WHITE);
            letterButton.setFocusPainted(false);
            letterButton.setPreferredSize(new Dimension(70, 70));
            letterButton.setBorder(BorderFactory.createLineBorder(new Color(204, 102, 0), 2));

            // Action for selecting letters
            letterButton.addActionListener(e -> {
                if (selectedField != null && selectedField.getText().isEmpty()) {
                    selectedField.setText(letter.toString());
                    letterButton.setBackground(Color.BLACK);
                    letterButton.setEnabled(false);
                      // Update after a letter is selected
                }
            });

            letterButtons.add(letterButton);
            letterPanel.add(letterButton);
        }
    }

    private JPanel createWordPanel(int length, int panelNumber) {
        JPanel wordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 30));
        wordPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "WORD " + panelNumber));
        wordPanel.setBackground(new Color(255, 247, 230));
               
        
        
        JButton deleteButton = new JButton("X");
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 24));
        deleteButton.setBackground(Color.RED);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setPreferredSize(new Dimension(50,50));
       
        wordPanel.add(deleteButton);
        

        JTextField[] wordFields = new JTextField[length];
        for (int j = 0; j < length; j++) {
            JTextField textField = new JTextField(1);
            textField.setFont(new Font("Segoe UI", Font.PLAIN, 24));
            textField.setHorizontalAlignment(JTextField.CENTER);
            textField.setPreferredSize(new Dimension(150, 100));
            textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 153, 51), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));

            // FocusListener to select the text field
            textField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {            	
                	 if (selectedField != null) {
                		 selectedField.setBorder(BorderFactory.createLineBorder(new Color(255, 153, 51),2));                     }	
                    selectedField = textField;
                    selectedField.setBorder(BorderFactory.createLineBorder(Color.BLACK,3));
                    
                    
                }
            });

            wordFields[j] = textField;
            wordPanel.add(textField);
        }

        allWordFields[panelNumber - 1] = wordFields;

        // Check button for validating the word
        JButton checkButton = new JButton("Check");
        checkButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        checkButton.setBackground(new Color(204, 102, 0));
        checkButton.setForeground(Color.WHITE);
        checkButton.setFocusPainted(false);
        checkButton.setPreferredSize(new Dimension(100, 40));
        checkButton.setBorder(BorderFactory.createLineBorder(new Color(204, 102, 0), 2));

        checkButton.addActionListener(e -> validateWord(wordFields));

        wordPanel.add(checkButton);

        // MouseListener to change the panel's background color and reset letter button colors
        wordPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Reset the color of the previously selected panel
                if (selectedPanel != null) {
                    selectedPanel.setBackground(new Color(255, 247, 230)); // Default color
                }
             	 if (selectedField != null) {
            		 selectedField.setBorder(BorderFactory.createLineBorder(new Color(255, 153, 51),2));                     }	

                // Update the selected panel to the clicked one
                selectedPanel = wordPanel;
                selectedPanel.setBackground(new Color(255, 153, 51)); // Color for selected panel

                // Re-enable and reset the color of the letter buttons
                updateLetterButtons();
            }
        });
        
             
        
      
        deleteButton.addActionListener(e -> {
            if (selectedField != null && Arrays.asList(wordFields).contains(selectedField)) {
                // Get the deleted letter
                String deletedLetter = selectedField.getText();
                selectedField.setText(""); // Clear the selected field

                // Re-enable the letter in the letter list
                if (!deletedLetter.isEmpty()) {
                    char letter = deletedLetter.charAt(0);
                    for (JButton letterButton : letterButtons) {
                        if (letterButton.getText().charAt(0) == letter) {
                            letterButton.setBackground(new Color(255, 153, 51)); // Restore button color
                            letterButton.setEnabled(true); // Enable the letter button
                            break;
                        }
                    }
                }
            }
        });

       
        return wordPanel;
    }
    
    private int correctWordCount = 0;  // Counter for correct words
    private void validateWord(JTextField[] wordFields) {
        StringBuilder word = new StringBuilder();
        for (JTextField field : wordFields) {
            word.append(field.getText());
        }

        boolean isCorrect = LetterGenerator.loadWordsByLength(word.length()).contains(word.toString().toUpperCase());
        for (JTextField field : wordFields) {
            field.setBackground(isCorrect ? new Color(102, 255, 102) : new Color(255, 102, 102)); // Green for correct, red for incorrect
        }

        if (isCorrect) {
            JOptionPane.showMessageDialog(this, "Correct! The word is valid.");
            updateScore(timeRemaining);  // Update score after correct word validation
            updateScoreLabel();  // Update the score label
            
            // Increase the correct word count
            correctWordCount++;
            
            // If 3 correct words are completed, show a congratulatory message
            if (correctWordCount == 3) {
                // Custom message when all 3 words are completed before time runs out
                int finalScore = this.getGameScore().getScore(); // Fetch the final score
                String message = "<html><div style='text-align: center;'>"
                        + "<h1 style='color: #4CAF50;'>Congratulations!</h1>"
                        + "<p style='font-size: 16px;'>You finished all 3 words before time was up!</p>"
                        + "<h2 style='color: #FF6347;'>Your final score is:</h2>"
                        + "<h2 style='color: #4CAF50;'>" + finalScore + "</h2>"
                        + "</div></html>";

                JOptionPane.showMessageDialog(this, message, "Well Done!", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            // Handle incorrect word validation
            int response = JOptionPane.showOptionDialog(this, 
                "Incorrect word. Try again or cancel.", 
                "Invalid Word", 
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.WARNING_MESSAGE, 
                null, 
                new String[] {"Try Again", "Cancel"}, 
                "Try Again");

            if (response == 0) { // If "Try Again" is selected
                int remainingTentative = gameScore.getTentativesRestantes();
                gameScore.setTentativesRestantes(remainingTentative - 1); 

                if (gameScore.getTentativesRestantes() <= 0) {
                    JOptionPane.showMessageDialog(this, "No more attempts left!");
                } else {
                    for (JTextField field : wordFields) {
                        field.setText("");  // Clear text in fields
                        field.setBackground(Color.WHITE);  // Reset background color
                    }
                    int newPossibilities = gameScore.getScoresPossibilites() + word.length();
                    gameScore.setScoresPossibilites(newPossibilities);
                    updateScore(timeRemaining);  // Update score after word validation attempt
                    updateScoreLabel();  // Update the score label
                }
            }
        }
    }


    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + gameScore.getScore()); // Update the label with the new score
    }
    private void updateScore(int remainingTime ) {
    	int tentatives=gameScore.getTentativesRestantes();
    	int possibilities=gameScore.getScoresPossibilites();
    	int score=gameScore.calculateScore(remainingTime, tentatives, possibilities);
    	gameScore.setScore(score);
    	
    }

    private void createScoreSection() {
        scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        scorePanel.setBorder(BorderFactory.createTitledBorder("Score"));
        scorePanel.setBackground(new Color(255, 230, 204));

        scoreLabel = new JLabel("Score: " + gameScore.getScore());
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        scoreLabel.setForeground(new Color(204, 102, 0));
        scorePanel.add(scoreLabel);
    }

    private void updateLetterButtons() {
        // Reset the background colors of all the letter buttons
        for (JButton button : letterButtons) {
            button.setBackground(new Color(255, 153, 51));
            button.setEnabled(true);
        }
    }

    private void updateWordFieldsColor() {
        // Reset all the word fields to their default background color
        for (JPanel wordPanel : wordPanels) {
            for (Component component : wordPanel.getComponents()) {
                if (component instanceof JTextField) {
                    JTextField textField = (JTextField) component;
                    textField.setBackground(Color.WHITE);
                }
            }
        }
    }

    public List<JButton> getLetterButtons() {
        return letterButtons;
    }
    
    
    
    
    /////////////////////////////////////////////
    
    
    private JPanel createTimerSection() {
        // Créer un panneau pour le timer avec un alignement à droite
        JPanel timerPanel = new JPanel(new BorderLayout());
        timerPanel.setOpaque(false); // Pour ne pas cacher le fond du panel principal
        
        // Créer l'icône du timer
        JLabel timerIcon = new JLabel(new ImageIcon("path_to_your_icon.png")); // Remplacer "path_to_your_icon.png" par le chemin de votre icône
        
        // Créer l'affichage du timer
        timerLabel = new JLabel(formatTime(timeRemaining));
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        timerLabel.setHorizontalAlignment(JLabel.LEFT); // Aligné à gauche par rapport à l'icône
        timerLabel.setForeground(Color.RED);

        // Ajouter l'icône et le timer dans le panneau
        timerPanel.add(timerIcon, BorderLayout.WEST); // Icône à gauche
        timerPanel.add(timerLabel, BorderLayout.CENTER); // Timer à droite de l'icône
        
        // Ajouter le timerPanel au scorePanel
        return timerPanel;
    }



    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }
    
    
	public void updateTimer(int timeRemaining) {
        this.timeRemaining = timeRemaining;
        timerLabel.setText(formatTime(timeRemaining));
    }

    
    public void disableComponents(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JButton || component instanceof JTextField) {
                component.setEnabled(false);
            } else if (component instanceof Container) {
                disableComponents((Container) component); // Appel récursif pour les sous-conteneurs
            }
        }
    }


    
    
      
    
}