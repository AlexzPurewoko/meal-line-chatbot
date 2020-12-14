package com.apwdevs.submission.chatbot.mealrecipe.handlers;

import com.apwdevs.submission.chatbot.mealrecipe.model.LineEvents;
import com.apwdevs.submission.chatbot.mealrecipe.model.LineMemberJoinedEvents;
import com.apwdevs.submission.chatbot.mealrecipe.model.ListsMember;
import com.apwdevs.submission.chatbot.mealrecipe.model.MemberJoinedEvents;
import com.apwdevs.submission.chatbot.mealrecipe.service.BotService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.UserSource;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.objectmapper.ModelObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class GreetEventHandlers {

    @Autowired
    private BotService botService;

    public void handleMemberJoinsGreet(String evPayload){
        try{
            ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();
            LineMemberJoinedEvents memberJoinedEvents = objectMapper.readValue(evPayload, LineMemberJoinedEvents.class);
            memberJoinedEvents.getEvents().forEach(event -> {
                ListsMember memberJoined= event.getMemberJoined();
                final StringBuffer members = new StringBuffer();
                memberJoined.getMembers().forEach(member -> {
                    members.append(botService.getProfile(member.getUserId()));
                    members.append(", ");
                });
                members.replace(members.length() - 2, members.length(), "!");

                StringBuffer messages = new StringBuffer()
                        .append("Hallo ").append(members.toString())
                        .append("\nSelamat datang di ").append((event.getSource() instanceof GroupSource) ? "Group!": "Room ini!")
                        .append("\nPerkenalkan, aku adalah Meal Master, sebuah bot yang akan membantu kamu untuk mencari resep makanan.")
                        .append("Aku harap dapat membantu kamu yah! Silakan tambahkan aku sebagai teman jika ingin tahu secara private resep makanan yang ingin kamu cari.")
                        .append("\n Untuk detail penggunaan, kamu bisa langsung akses dengan menggunakan perintah: ")
                        .append("\n\t/meal help");
                TextMessage txtMessage = new TextMessage(messages.toString());
                botService.replyMessage(event.getReplyToken(), txtMessage);

            });
        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public void joinGroupEventGreet(String replyToken, String groupName){
        StringBuilder message = new StringBuilder()
                .append("Hallo Semuanya!\n")
                .append("Terimakasih yah telah ditambahkan di group ").append(groupName).append('!')
                .append("\n\nSemoga aku bisa menyesuaikan dengan grup ini hehe.")
                .append("\nOiya kenalan dulu nih, aku adalah Meal Master, sebuah bot yang akan membantu kamu untuk mencari resep makanan.")
                .append("Aku harap dapat membantu kamu yah! Silakan tambahkan aku sebagai teman jika ingin tahu secara private resep makanan yang ingin kamu cari.")
                .append("\n Untuk detail penggunaan, kamu bisa langsung akses dengan menggunakan perintah: ")
                .append("\n\t/meal help");
        TextMessage textMessage = new TextMessage(message.toString());
        botService.replyMessage(replyToken, textMessage);
    }

    public void joinRoomEventGreet(String replyToken, String roomName){
        StringBuilder message = new StringBuilder()
                .append("Hallo Semuanya!\n")
                .append("Terimakasih yah telah ditambahkan di room ").append(roomName).append('!')
                .append("\n\nSemoga aku bisa menyesuaikan dengan room ini hehe.")
                .append("\nOiya kenalan dulu nih, aku adalah Meal Master, sebuah bot yang akan membantu kamu untuk mencari resep makanan.")
                .append("Aku harap dapat membantu kamu yah! Silakan tambahkan aku sebagai teman jika ingin tahu secara private resep makanan yang ingin kamu cari.")
                .append("\n Untuk detail penggunaan, kamu bisa langsung akses dengan menggunakan perintah: ")
                .append("\n\t/meal help");
        TextMessage textMessage = new TextMessage(message.toString());
        botService.replyMessage(replyToken, textMessage);
    }

    public void hasInvitedToChatGreet(String replyToken, String uName){
        StringBuilder message = new StringBuilder()
                .append("Hallo ").append(uName).append("!")
                .append("Terimakasih yah udah nambahin aku jadi temenmu hehe :) ")
                .append("\nOiya kenalan dulu nih, aku adalah Meal Master, sebuah bot yang akan membantu kamu untuk mencari resep makanan.")
                .append("Aku harap dapat membantu kamu yah!")
                .append("\n Untuk detail penggunaan, kamu bisa langsung akses dengan menggunakan perintah: ")
                .append("\n\t/meal help");
        TextMessage textMessage = new TextMessage(message.toString());
        botService.replyMessage(replyToken, textMessage);
    }
}
