    package NotWordle;

    import net.dv8tion.jda.api.entities.User;
    import java.util.Map;
    import java.util.concurrent.ConcurrentHashMap;

    public class OngoingGames {
        private static final Map<User, GameState> ongoingGames = new ConcurrentHashMap<>();

        public static void addGame(User user, GameState gameState) {
            ongoingGames.put(user, gameState);
        }

        public static GameState getGame(User user) {
            return ongoingGames.get(user);
        }

        public static void endGame(User user) {
            ongoingGames.remove(user);
        }
        //TODO implement a method to check if a user has an ongoing game
        //TODO implement a method to track games played, win rate, etc.
    }