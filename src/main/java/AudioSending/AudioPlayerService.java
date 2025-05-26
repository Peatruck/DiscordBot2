package AudioSending;

import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AudioPlayerService {
    private final Map<String, String> messageToAudioMap = new HashMap<>();

    public AudioPlayerService() {
        messageToAudioMap.put("!ping", "teamspeakHeyWakeUp.wav");
        messageToAudioMap.put("!bye", "bye.wav");
    }

    public void playAudioForMessage(MessageReceivedEvent event) throws Exception {
        joinPopulatedChannel(event);
        String audioFile = messageToAudioMap.get(event.getMessage().getContentRaw());
        if (audioFile == null) return;

        InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(audioFile);
        if (resourceStream == null) {
            System.err.println("Audio file not found: " + audioFile);
            return;
        }

        Path tempInput = Files.createTempFile("audio_input", ".wav");
        Files.copy(resourceStream, tempInput, StandardCopyOption.REPLACE_EXISTING);
        resourceStream.close();


        Path tempOutput = Files.createTempFile("audio_output", ".pcm");
        boolean converted = AudioConverterForDiscord.convertToDiscordPcm(tempInput.toString(), tempOutput.toString());

        if (converted) {
            RawPcmSendHandler handler = new RawPcmSendHandler(tempOutput.toString());
            event.getGuild().getAudioManager().setSendingHandler(handler);


        new Thread(() -> {
            while (!handler.isDone()) {
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            }
            event.getGuild().getAudioManager().closeAudioConnection();
            System.out.println("Disconnected after playback.");
        }).start();
        } else {
            System.err.println("Audio conversion failed.");
        }

        tempInput.toFile().deleteOnExit();
        tempOutput.toFile().deleteOnExit();
    }

    public void joinPopulatedChannel(MessageReceivedEvent event) {
        Guild guild = event.getGuild();
        var member = event.getMember();
        if (member == null) return;
        var voiceState = member.getVoiceState();
        if (voiceState == null) return;
        var channel = voiceState.getChannel();
        if (channel instanceof VoiceChannel) {
            guild.getAudioManager().openAudioConnection(channel);
            System.out.println("Joined voice channel: " + channel.getName());
        }
    }

    public void disconnectFromVoiceChannel(Guild guild) {
        guild.getAudioManager().closeAudioConnection();
    }
}

