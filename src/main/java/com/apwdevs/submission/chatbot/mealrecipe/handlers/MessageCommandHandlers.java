package com.apwdevs.submission.chatbot.mealrecipe.handlers;

import com.apwdevs.submission.chatbot.mealrecipe.service.BotService;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageCommandHandlers {

    @Autowired
    private BotService botService;

    // sementara text terlebih dahulu untuk percobaan
    public void searchMealEvent(String replyToken, String profileName, String mealName){
        testMessages(
                replyToken,
                String.format(
                        "Hai %s!\nNampaknya kamu sedang mencari resep dari %s. Tenang saja, fitur ini akan tersedia kemudian :)\n%s",
                        profileName,
                        mealName,
                        "/meal search "+mealName
                )
        );
    }

    public void displayAllCategories(String replyToken, String profileName){
        testMessages(
                replyToken,
                String.format(
                        "Hai %s!\nNampaknya kamu ingin melihat category yang tersedia. Tenang saja, fitur ini akan tersedia kemudian :)\n%s",
                        profileName,
                        "/meal all categories"
                )
        );
    }

    public void randomMeal(String replyToken, String profileName){
        testMessages(
                replyToken,
                String.format(
                        "Hai %s!\nNampaknya kamu ingin melihat resep makanan secara random. Tenang saja, fitur ini akan tersedia kemudian :)\n%s",
                        profileName,
                        "/meal random"
                )
        );
    }

    public void filterMealByCategories(String replyToken, String profileName, String categoryName){
        testMessages(
                replyToken,
                String.format(
                        "Hai %s!\nNampaknya kamu sedang mencari semua resep dari kategori %s. Tenang saja, fitur ini akan tersedia kemudian :)\n%s",
                        profileName,
                        categoryName,
                        "/meal from category "+categoryName
                )
        );
    }

    private void testMessages(String replyToken, String msgBody){
        TextMessage textMessageContent = new TextMessage(msgBody);
        botService.replyMessage(replyToken, textMessageContent);
    }
}
