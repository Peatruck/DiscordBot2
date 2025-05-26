package ListenerService;

import AudioSending.AudioPlayerService;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventListener extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent event) {
    if (event.getAuthor().isBot() ||
    ValidCommands.audioCommands.stream().noneMatch(cmd -> event.getMessage().getContentRaw().contains(cmd))) {
            return;
        }
        AudioPlayerService audioPlayerService = new AudioPlayerService();
        System.out.println("Received message: " + event.getMessage().getContentRaw() + " from " + event.getAuthor().getName());
        try {
            audioPlayerService.playAudioForMessage(event);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}



