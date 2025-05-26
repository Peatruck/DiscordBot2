
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.Properties;

public class PeatruckBot {
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        try (var in = PeatruckBot.class.getClassLoader().getResourceAsStream("botTokens")) {
            props.load(in);
        }
        String BOT_TOKEN = props.getProperty("BOT_TOKEN").trim();
        JDA api = net.dv8tion.jda.api.JDABuilder.createDefault(BOT_TOKEN, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_VOICE_STATES).build();
        api.awaitReady();
        api.addEventListener(new ListenerService.EventListener());
        api.addEventListener(new ListenerService.WordleEventListener());
    }
}
