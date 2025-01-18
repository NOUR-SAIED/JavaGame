package controller;

import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import model.LetterGenerator;
import model.GameScore;
import view.GameView;

public class GameController{
	private Timer timer; // Chronomètre Swing
	private int timeRemaining; // Temps restant en secondes
	


    private GameView gameView;

    public GameController() {
        // Obtenez les données du modèle
        Map<String, Object> gameData = LetterGenerator.generateGameSetup(8);
        GameScore gameScore = new GameScore();
        List<Character> letters = (List<Character>) gameData.get("letters");
        List<Integer> wordLengths = (List<Integer>) gameData.get("wordLengths");

        // Initialisez la vue avec les lettres et les longueurs de mots
        gameView = new GameView(letters, wordLengths,gameScore);
          
        startTimer();


        // Ajoutez des écouteurs pour les interactions utilisateur
        addListeners();
    }

    private void addListeners() {
        // Écouteurs pour les boutons de lettres
        for (JButton button : gameView.getLetterButtons()) {
            button.addActionListener(e -> {
                String letter = button.getText();
              
            });
        }
    }
    private void startTimer() {
        timeRemaining = 100; // 5 minutes par exemple

        timer = new Timer(1000, e -> {
            timeRemaining--;
            gameView.updateTimer(timeRemaining); // Met à jour l'affichage dans la vue

            if (timeRemaining <= 0) {
                timer.stop();
                gameOver(); // Méthode à appeler quand le temps est écoulé
            }
        });
        timer.start();
    }
    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }
    public void gameOver() {
        // Stop the timer and disable all components
        stopTimer();
        gameView.disableComponents(gameView);

        
        int finalScore = gameView.getGameScore().getScore(); // Fetch the final score
        String message = "<html>"
                + "<div style='text-align: center; font-family: Arial, sans-serif;'>"
                + "<h1 style='color: #FF6347;'>Game Over</h1>"
                + "<p style='font-size: 16px;'>Your final score is:</p>"
                + "<h2 style='color: #4CAF50; font-size: 36px; margin-top: 10px;'>" + finalScore + "</h2>"
                + "<p style='font-size: 14px; margin-top: 20px;'>Would you like to restart?</p>"
                + "</div>"
                + "</html>";
        int option = JOptionPane.showOptionDialog(
            gameView,
            message,
            "Game Over",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            new String[] { "Restart", "Exit" },
            "Restart"
        );

        if (option == JOptionPane.YES_OPTION) {
            // Restart the game
            restartGame();
        } else {
            // Exit the application
            System.exit(0);
        }
    }

    private void restartGame() {
        // Close the current game window and start a new instance
        gameView.dispose();
        SwingUtilities.invokeLater(GameController::new);
    }



    public static void main(String[] args) {
        // Exécutez l'application sur le thread de l'interface utilisateur Swing
        SwingUtilities.invokeLater(GameController::new);
    }
}