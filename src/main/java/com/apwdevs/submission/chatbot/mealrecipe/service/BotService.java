package com.apwdevs.submission.chatbot.mealrecipe.service;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.profile.UserProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class BotService {

    @Autowired
    private LineMessagingClient lineMessagingClient;

    public UserProfileResponse getProfile(String targetId){
        try {

            return lineMessagingClient.getProfile(targetId).get();
        } catch (InterruptedException | ExecutionException e){
            throw new RuntimeException(e);
        }
    }

    public void replyMessage(ReplyMessage message){
        try {
            lineMessagingClient.replyMessage(message).get();
        } catch (InterruptedException | ExecutionException e){
            throw new RuntimeException(e);
        }
    }

    public void replyMessage(String replyToken, Message message){
        ReplyMessage replyMessage = new ReplyMessage(replyToken, message);
        replyMessage(replyMessage);
    }

    public void replyListMessage(String replyToken, List<Message> messages){
        ReplyMessage replyMessage = new ReplyMessage(replyToken, messages);
        replyMessage(replyMessage);
    }
}
