package com.apwdevs.submission.chatbot.mealrecipe.controller;

import com.apwdevs.submission.chatbot.mealrecipe.handlers.GreetEventHandlers;
import com.apwdevs.submission.chatbot.mealrecipe.handlers.HelpEventHandlers;
import com.apwdevs.submission.chatbot.mealrecipe.handlers.MessageCommandHandlers;
import com.apwdevs.submission.chatbot.mealrecipe.model.LineEvents;
import com.apwdevs.submission.chatbot.mealrecipe.model.MemberJoinedEvents;
import com.apwdevs.submission.chatbot.mealrecipe.service.BotService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.model.event.*;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.event.source.UserSource;
import com.linecorp.bot.model.objectmapper.ModelObjectMapper;
import com.linecorp.bot.model.profile.UserProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sound.sampled.LineEvent;
import java.io.IOException;
import java.lang.reflect.Member;
import java.util.List;

@RestController
public class BotController {
    @Autowired
    @Qualifier("lineSignatureValidator")
    private LineSignatureValidator lValidator;

    @Autowired
    private BotService botService;

    @Autowired
    private GreetEventHandlers greetMsgHandler;

    @Autowired
    private MessageCommandHandlers msgCommands;

    @Autowired
    private HelpEventHandlers helpHandler;

    private String bodyPayloads;

    @RequestMapping(value = "/app-req", method = RequestMethod.POST)
    public ResponseEntity<String> fromInternalChats(
            @RequestHeader("X-Line-Signature") String sig,
            @RequestBody String evPayload
    ){
        try {
            if(lValidator.validateSignature(evPayload.getBytes(), sig)){
                throw new IllegalAccessException("Invalid Signature Validation");
            }

            ObjectMapper oMapper = ModelObjectMapper.createNewObjectMapper();
            LineEvents evModel = oMapper.readValue(evPayload, LineEvents.class);

            bodyPayloads = evPayload;
            evModel.getEvents().forEach(this::handleInternalChatsEvent);
            return new ResponseEntity<>("Events has been processed successfully", HttpStatus.OK);
        } catch (IllegalAccessException e) {
            return new ResponseEntity<>("Bad Access! Please access form Line instead!", HttpStatus.BAD_REQUEST);
        } catch (IOException e){
            return  new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private void handleInternalChatsEvent(Event event)  {
        if(event instanceof JoinEvent){
            JoinEvent e = (JoinEvent) event;
            String replyToken = e.getReplyToken();

            Source targetJoin = e.getSource();
            if(targetJoin instanceof RoomSource){
                greetMsgHandler.joinRoomEventGreet(replyToken, getDisplayName(((RoomSource) targetJoin).getRoomId()));
            } else if (targetJoin instanceof GroupSource){
                greetMsgHandler.joinGroupEventGreet(replyToken, getDisplayName(((GroupSource) targetJoin).getGroupId()));
            }
        } else if (event instanceof FollowEvent){
            greetMsgHandler.hasInvitedToChatGreet(((FollowEvent) event).getReplyToken(), getDisplayName(event.getSource().getUserId()));
        } else if(event instanceof UnknownEvent && ((UnknownEvent) event).getType().equals("memberJoined")){
            greetMsgHandler.handleMemberJoinsGreet(bodyPayloads);
        } else if(event instanceof MessageEvent) {
            MessageEvent<?> evMessage = (MessageEvent<?>) event;
            String replyToken = evMessage.getReplyToken();
            String profileSourceName = getDisplayName(evMessage.getSource().getUserId());
            if(evMessage.getMessage() instanceof TextMessageContent){
                handleMsgCommands(replyToken, profileSourceName, ((TextMessageContent) evMessage.getMessage()).getText());
            }
        }
    }

    private void handleMsgCommands(String replyToken, String profileName, String messageCommandContent){
        if(messageCommandContent.regionMatches(true, 0, "/meal", 0, 5)){
            // if match with commands "/meal search <name>"
            if(messageCommandContent.matches("(?i)\\/meal\\s*search\\s*")){
                msgCommands.searchMealEvent(
                        replyToken,
                        profileName,
                        messageCommandContent.replaceFirst("(?i)\\/meal\\s*search\\s*", "")
                );
            }

            // if match with commands "/meal all categories"
            else if (messageCommandContent.matches("(?i)\\/meal\\s*all\\s*categories\\s*")){
                msgCommands.displayAllCategories(
                        replyToken,
                        profileName
                );
            }

            // if match with commands "/meal random"
            else if(messageCommandContent.matches("(?i)\\/meal\\s*random\\s*")){
                msgCommands.randomMeal(
                        replyToken,
                        profileName
                );
            }

            // if match with commands "/meal from category"
            else if(messageCommandContent.matches("(?i)\\/meal\\s*from\\s*category\\s*")){
                msgCommands.filterMealByCategories(
                        replyToken,
                        profileName,
                        messageCommandContent.replaceFirst("(?i)\\/meal\\s*from\\s*category\\s*", "")
                );
            }

            // if match with commands "/meal help"
            else if(messageCommandContent.matches("(?i)\\/meal\\s*help\\s*")){
                helpHandler.showHelp(replyToken, profileName, HelpEventHandlers.HelpType.ALL, "");
            }

            // correct this command with help features
            else {
                showHelpMessageCorrection(replyToken, profileName, messageCommandContent);
            }
        } else if(messageCommandContent.substring(0, 5).matches("\\/(m|M)+(\\w)+")){
            helpHandler.showHelp(replyToken, profileName, HelpEventHandlers.HelpType.ALL, "");
        }
    }

    private void showHelpMessageCorrection(String replyToken, String profileName, String messageCommandContent) {
        if(messageCommandContent.matches("(?i)\\/meal\\s*s(e)?(a)?(r)?(c)?(h)?\\s*")){
            helpHandler.showHelp(
                    replyToken,
                    profileName,
                    HelpEventHandlers.HelpType.SEARCH,
                    messageCommandContent.replaceFirst("(?i)\\/meal\\s*s(e)?(a)?(r)?(c)?(h)?\\s*", "")
            );
        }

        else if(messageCommandContent.matches("(?i)\\/meal\\s*a(l)?(l)\\s?(c)?(a)?(t)?(e)?(g)?(o)?(r)?(i)?(e)?(s)?\\s*")){
            helpHandler.showHelp(
                    replyToken,
                    profileName,
                    HelpEventHandlers.HelpType.CATEGORIES,
                    messageCommandContent.replaceFirst("(?i)\\/meal\\s*a(l)?(l)\\s?(c)?(a)?(t)?(e)?(g)?(o)?(r)?(i)?(e)?(s)?\\s*", "")
            );
        }

        else if(messageCommandContent.matches("(?i)\\/meal\\s*r(a)?(n)?(d)?(o)?(m)?\\s*")){
            helpHandler.showHelp(
                    replyToken,
                    profileName,
                    HelpEventHandlers.HelpType.RANDOM,
                    messageCommandContent.replaceFirst("(?i)\\/meal\\s*r(a)?(n)?(d)?(o)?(m)?\\s*", "")
            );
        }

        else if(messageCommandContent.matches("(?i)\\/meal\\s*f(r)?(o)?(m)?\\s+(c)?(a)?(t)?(e)?(g)?(o)?(r)?(y)?\\s*")){
            helpHandler.showHelp(
                    replyToken,
                    profileName,
                    HelpEventHandlers.HelpType.LIST_FROM_CATEGORIES,
                    messageCommandContent.replaceFirst("(?i)\\/meal\\s*f(r)?(o)?(m)?\\s+(c)?(a)?(t)?(e)?(g)?(o)?(r)?(y)?\\s*", "")
            );
        }

        else if(messageCommandContent.matches("(?i)\\/meal\\s*h(e)?(l)?(p)?\\s*")){
            helpHandler.showHelp(
                    replyToken,
                    profileName,
                    HelpEventHandlers.HelpType.SHOW_HELP,
                    messageCommandContent.replaceFirst("(?i)\\/meal\\s*h(e)?(l)?(p)?\\s*", "")
            );
        }

        else {
            helpHandler.showHelp(
                    replyToken,
                    profileName,
                    HelpEventHandlers.HelpType.ALL,
                    ""
            );
        }
    }

    private String getDisplayName(String ids){
        UserProfileResponse profileResponse = botService.getProfile(ids);
        return profileResponse.getDisplayName();
    }
}
