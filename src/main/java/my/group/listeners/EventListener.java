package my.group.listeners;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EventListener extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {

        User user = event.getUser();
        String emoji = event.getReaction().getEmoji().getAsReactionCode();
        String channelMention = event.getChannel().getAsMention();

        String message = user.getAsTag() + " reacted to a message with " + emoji + " in the " + channelMention + " channel!";
        event.getGuild().getDefaultChannel().asStandardGuildMessageChannel().sendMessage(message).queue();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
       String message = event.getMessage().getContentRaw();
       MessageChannel channel = event.getChannel();
       if (message.toLowerCase().contains("ping")){
           channel.sendMessage("Pong!").queue();
       } else if (message.toLowerCase().replaceAll(" ", "").contains("hellofirstbot")){
           channel.sendMessage("Hello " + event.getMember().getEffectiveName()).queue();
       }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        event.getGuild().getDefaultChannel().asStandardGuildMessageChannel().sendMessage( event.getMember().getEffectiveName() + " has joined the server. Welcome them and have fun!").queue();
    }

    @Override
    public void onUserUpdateOnlineStatus(UserUpdateOnlineStatusEvent event) {
        List<Member> members = event.getGuild().getMembers();
        int onlineMembers = 0;
        for (Member member : members){
            if(member.getOnlineStatus() == OnlineStatus.ONLINE){
                onlineMembers++;
            }
        }


        User user = event.getUser();
        String message = "**" + user.getAsTag() + "** updated their status to " + event.getNewOnlineStatus().getKey() + "!" +
                        "\nThere are now " + onlineMembers + " user(s) online!";
        event.getGuild().getDefaultChannel().asStandardGuildMessageChannel().sendMessage(message).queue();
    }
}
