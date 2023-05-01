package my.group;

import io.github.cdimascio.dotenv.Dotenv;
import my.group.listeners.EventListener;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

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
        gatewayIntents = new GatewayIntent[]{GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.MESSAGE_CONTENT,
                            GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_EMOJIS_AND_STICKERS, GatewayIntent.GUILD_VOICE_STATES,
                            GatewayIntent.SCHEDULED_EVENTS, GatewayIntent.GUILD_PRESENCES};

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token, Arrays.asList(gatewayIntents));
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("Valorant-Top Fragging with classic only"));
        builder.build();
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.enableCache(CacheFlag.ONLINE_STATUS);
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