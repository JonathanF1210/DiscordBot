package my.group;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;

public class DiscordBot {

    private final ShardManager shardManager;

    public DiscordBot() throws LoginException{
        String token = "MTEwMjMwNjk0NDI1NTAwNDc1Mw.GOwFo7.PN4gQotbUh7s1n0QMaeh29SEoX_QynBHGwL1Io";
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("Valorant-Top Fragging with classic only"));
        builder.build();
        shardManager = builder.build();
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public static void main(String[] args) {
        try{
            DiscordBot bot = new DiscordBot();
        } catch (LoginException e){
            System.out.println("Error: provided bot token is invalid!");
        }
    }
}