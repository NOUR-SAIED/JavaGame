package model;

public class GameScore {
    private int tempsRestant;
    private int tentativesRestantes;
    private int scoresPossibilites;
    private int score;
    private int remainingWords;  // Field to store the number of remaining words


    public GameScore() {
    	this.tentativesRestantes=2;
	
	}

	public int calculateScore(int tempsRestant, int tentativesRestantes, int scoresPossibilites) {
        int score = (20 * tempsRestant) + (30 * tentativesRestantes)+(50*scoresPossibilites);
        
        return score;
    }

	public int getScoresPossibilites() {
		return scoresPossibilites;
	}

	public void setScoresPossibilites(int scoresPossibilites) {
		this.scoresPossibilites = scoresPossibilites;
	}

	public int getTempsRestant() {
		return tempsRestant;
	}

	public void setTempsRestant(int tempsRestant) {
		this.tempsRestant = tempsRestant;
	}

	public int getTentativesRestantes() {
		return tentativesRestantes;
	}

	public void setTentativesRestantes(int tentativesRestantes) {
		this.tentativesRestantes = tentativesRestantes;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public int getRemainingWords() {
        return remainingWords;  // Return the number of remaining words
    }
	
	 public void setRemainingWords(int remainingWords) {
	        this.remainingWords = remainingWords;  // Set the number of remaining words
	    }
	 
	// Optionally, you could also add logic to decrement the remaining words directly
	    public void decrementRemainingWords() {
	        if (remainingWords > 0) {
	            remainingWords--;  // Decrease the remaining words by 1
	        }
	    }
}
