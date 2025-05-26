package NotWordle;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import static NotWordle.WordleConstants.WORDLE_WELCOME_MESSAGE;

public class WordleAgent {
    public static final Set<String> WORDS = new HashSet<>();


    static {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(WordleAgent.class.getResourceAsStream("/words.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                WORDS.add(line.trim().toLowerCase());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load words.txt", e);
        }
    }

    public void wordleServiceStart(MessageReceivedEvent event) {
        User author = event.getAuthor();
        String randomWord = WORDS.stream()
                .skip((int) (WORDS.size() * Math.random()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No words available"));
        OngoingGames.addGame(author, new GameState(randomWord, "", 0));
        event.getChannel().sendMessage(WORDLE_WELCOME_MESSAGE).queue();
    }



public void wordleServiceOngoing(MessageReceivedEvent event) {
    User author = event.getAuthor();
    String messageContent = event.getMessage().getContentRaw().trim();

    String[] parts = messageContent.split("\\s+", 2);
    if (parts.length < 2) {
        event.getChannel().sendMessage("Usage: !Guess <your_word>").queue();
        return;
    }
    String guess = parts[1].toLowerCase();

    GameState game = OngoingGames.getGame(author);
    if (game == null) {
        event.getChannel().sendMessage("No ongoing game. Start with !Wordle").queue();
        return;
    }

    if (game.getAttempts() >= 6) {
        event.getChannel().sendMessage("Maximum number of guesses reached! The word was: " + game.getRandomWord()).queue();
        OngoingGames.endGame(author);
        return;
    }

    if (guess.length() != game.getRandomWord().length()) {
        event.getChannel().sendMessage("Guess must be " + game.getRandomWord().length() + " letters.").queue();
        return;
    }
    if (!WordleAgent.WORDS.contains(guess)) {
        event.getChannel().sendMessage("Not a valid word.").queue();
        return;
    }

    game.setCurrentGuess(guess);
    game.incrementAttempts();

    if (guess.equals(game.getRandomWord())) {
        event.getChannel().sendMessage("Congratulations! You guessed the word: " + game.getRandomWord()).queue();
        OngoingGames.endGame(author);
    } else if (game.getAttempts() >= 6) {
        event.getChannel().sendMessage("No more guesses! The word was: " + game.getRandomWord()).queue();
        OngoingGames.endGame(author);
    } else {
        //TODO fix yellow squares appearing in incorrect positions
        StringBuilder feedback = new StringBuilder();
        String target = game.getRandomWord();
        for (int i = 0; i < guess.length(); i++) {
            if (guess.charAt(i) == target.charAt(i)) {
                feedback.append("\uD83D\uDFE9"); // Green square
            } else if (target.contains(String.valueOf(guess.charAt(i)))) {
                feedback.append("\uD83D\uDFE8"); // Yellow square
            } else {
                feedback.append("\u2B1B"); // Black square
            }
        }
        event.getChannel().sendMessage("Feedback: " + feedback + " (" + game.getAttempts() + "/6 guesses)").queue();
    }
}
}