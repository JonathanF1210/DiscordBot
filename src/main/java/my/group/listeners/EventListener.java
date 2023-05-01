package my.group.listeners;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.html.Option;
import java.util.ArrayList;
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

    // slash commands

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equalsIgnoreCase("welcome")) {
            String userTag = event.getUser().getAsTag();
            event.reply("Welcome to the server, ** " + userTag + "**!").setEphemeral(true).queue();
        }else if (command.equalsIgnoreCase("roles")){
            event.deferReply().queue();
            String response = "";
            for (Role role : event.getGuild().getRoles()){
                response += role.getAsMention() + "\n";
            }
            event.getHook().sendMessage(response).setEphemeral(true).queue();
        }
        else if (command.equalsIgnoreCase("say")){
            OptionMapping messageOption = event.getOption("message");
            String message = messageOption.getAsString();

            MessageChannel channel;
            OptionMapping channelOption = event.getOption("channel");
            if (channelOption != null) {
                channel = channelOption.getAsChannel().asStandardGuildMessageChannel();
            } else {
                channel = event.getChannel();
            }

            channel.sendMessage(message).queue();
            event.reply("Your message was sent!").setEphemeral(true).queue();
        }
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("welcome", "Get welcomed by the bot."));
        commandData.add(Commands.slash("roles", "Display all roles on the server"));

        // Command: /say <message> [channel]
        OptionData option1 = new OptionData(OptionType.STRING, "message", "The message you want the bot to say", true);
        OptionData option2 = new OptionData(OptionType.CHANNEL, "channel", "The channel you want to send this message in").setChannelTypes(ChannelType.TEXT, ChannelType.NEWS, ChannelType.GUILD_PUBLIC_THREAD);

        commandData.add(Commands.slash("say", "Make the bot say a message.").addOptions(option1, option2));
        commandData.add(Commands.slash("emote", "Express your emotions through text.").addOptions());

        event.getGuild().updateCommands().addCommands(commandData).queue();


    }


}
