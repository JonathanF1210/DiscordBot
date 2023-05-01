package my.group;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;

public class DiscordBot {
    private final Dotenv config;
    private final ShardManager shardManager;

    public DiscordBot() throws LoginException{
        config = Dotenv.configure().load();
        String token = config.get("TOKEN");

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("Valorant-Top Fragging with classic only"));
        builder.build();
        shardManager = builder.build();
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