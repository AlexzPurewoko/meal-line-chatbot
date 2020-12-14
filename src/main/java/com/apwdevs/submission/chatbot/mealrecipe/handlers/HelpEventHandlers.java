package com.apwdevs.submission.chatbot.mealrecipe.handlers;

import com.apwdevs.submission.chatbot.mealrecipe.service.BotService;
import com.linecorp.bot.model.message.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelpEventHandlers {

    @Autowired
    private BotService botService;

    public void showHelp(String replyToken, String profileName, HelpType helpType, String optionalContent){
        switch (helpType){
            case SEARCH:
                replyMessages(
                        replyToken,
                        profileName,
                        String.format(
                                "Nampaknya, kamu ingin mencari sebuah resep makanan. Namun penggunaan perintahmu masih salah, sehingga kami tidak bisa melakukan permintaan kamu\n"+
                                "Maaf yah, seharusnya kamu mengetikkan:\n\n"+
                                "/meal search %s",
                                optionalContent
                        )
                );
                break;
            case CATEGORIES:
                replyMessages(
                        replyToken,
                        profileName,
                                "Nampaknya, kamu ingin melihat kategori dari resep makanan yang tersedia. Namun penggunaan perintahmu masih salah, sehingga kami tidak bisa melakukan permintaan kamu\n"+
                                "Maaf yah, seharusnya kamu mengetikkan:\n\n"+
                                "/meal all categories"
                );
                break;
            case RANDOM:
                replyMessages(
                        replyToken,
                        profileName,
                                "Nampaknya, kamu bingung mau masak apa dan ingin mencari hal baru yang random. Namun penggunaan perintahmu masih salah, sehingga kami tidak bisa melakukan permintaan kamu\n"+
                                "Maaf yah, seharusnya kamu mengetikkan:\n\n"+
                                "/meal random"
                );
                break;
            case LIST_FROM_CATEGORIES:
                replyMessages(
                        replyToken,
                        profileName,
                        String.format(
                                "Nampaknya, kamu penasaran nih dengan apa yang tersedia pada tiap kategori. Namun penggunaan perintahmu masih salah, sehingga kami tidak bisa melakukan permintaan kamu\n"+
                                "Maaf yah, seharusnya kamu mengetikkan:\n\n"+
                                "/meal from category %s",
                                optionalContent
                        )
                );
                break;
            case SHOW_HELP:
                replyMessages(
                        replyToken,
                        profileName,
                        "Untuk menampilkan menu Help, kamu bisa mengetikkan: \n\n"+
                                "/meal help"
                );
                break;
            case ALL: {
                StringBuilder messages = new StringBuilder();
                messages.append("Haii ").append(profileName).append("!\n");
                messages.append("Meal Master adalah sebuah bot yang digunakan untuk mencari resep tentang berbagai makanan luar negeri.\n");
                messages.append("Namun karena berasal dari luar negeri, semua konten yang dicari akan ditampilkan dalam bahasa inggris. Mohon maaf ya!\n");
                messages.append("Berikut adalah beberapa perintah yang bisa kamu gunakan: \n\n");
                messages.append("\t1. Mencari resep makanan: \n\t\t/meal search <nama resep>\n\n");
                messages.append("\t2. Kategori makanan: \n\t\t/meal all categories\n\n");
                messages.append("\t3. Resep random: \n\t\t/meal random\n\n");
                messages.append("\t4. Melihat makanan berdasarkan kategori: \n\t\t/meal from category <nama kategori>\n\n");
                messages.append("\t5. Menampilkan bantuan: \n\t\t/meal help\n\n");
                messages.append("Silakan Dicoba yah!");

                TextMessage m = new TextMessage(messages.toString());
                botService.replyMessage(replyToken, m);
            }
        }
    }

    private void replyMessages(String replyToken, String profileName, String msgBody){
        TextMessage message = new TextMessage(
                String.format(
                        "Hallo %s!\n\n%s\n\nSilakan coba diperbaiki lagi yah :)",
                        profileName,
                        msgBody
                )
        );

        botService.replyMessage(replyToken, message);
    }

    public enum HelpType {
        SEARCH,
        CATEGORIES,
        RANDOM,
        LIST_FROM_CATEGORIES,
        SHOW_HELP,
        ALL
    }
}
