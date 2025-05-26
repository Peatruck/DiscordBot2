package ListenerService;

import NotWordle.WordleAgent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class WordleEventListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (event.getMessage().getContentRaw().equalsIgnoreCase("!Wordle")) {
            WordleAgent wordleAgent = new WordleAgent();
            wordleAgent.wordleServiceStart(event);
        }
        if (event.getMessage().getContentRaw().contains("!Guess")) {
            WordleAgent wordleAgent = new WordleAgent();
            wordleAgent.wordleServiceOngoing(event);
        }
    }
}