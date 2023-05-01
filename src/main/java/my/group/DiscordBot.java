package my.group;

import io.github.cdimascio.dotenv.Dotenv;
import my.group.listeners.EventListener;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Arrays;

public class DiscordBot {
    private final Dotenv config;
    private final ShardManager shardManager;
    private final GatewayIntent[] gatewayIntents;

    public DiscordBot() throws LoginException{
        config = Dotenv.configure().load();
        String token = config.get("TOKEN");
        gatewayIntents = new GatewayIntent[]{GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_EMOJIS_AND_STICKERS, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.SCHEDULED_EVENTS};

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token, Arrays.asList(gatewayIntents));
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("Valorant-Top Fragging with classic only"));
        builder.build();
        shardManager = builder.build();

        // Register listeners
        shardManager.addEventListener(new EventListener());
    }

    public Dotenv getConfig(){
        return config;
    }
    public ShardManager getShardManager() {
        return shardManager;
    }

    public static void main(String[] args) {
        try{
            DiscordBot bot = new DiscordBot();
        } catch (LoginException e){
            System.out.println("Error: Provided bot token is invalid!");
        }
    }
}