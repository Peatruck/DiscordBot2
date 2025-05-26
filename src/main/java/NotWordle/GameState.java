package NotWordle;

public class GameState {
    private final String randomWord;
    private String currentGuess;
    private int attempts;

    public GameState(String randomWord, String currentGuess, int attempts) {
        this.randomWord = randomWord;
        this.currentGuess = currentGuess;
        this.attempts = attempts;
    }

    public String getRandomWord() {
        return randomWord;
    }

    public String getCurrentGuess() {
        return currentGuess;
    }

    public void setCurrentGuess(String currentGuess) {
        this.currentGuess = currentGuess;
    }

    public int getAttempts() {
        return attempts;
    }

    public void incrementAttempts() {
        this.attempts++;
    }
}