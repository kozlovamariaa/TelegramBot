import org.checkerframework.checker.units.qual.A;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;


public class AtmMoneyMessage extends BotCommand {
    private Update update;
    private String request;
    private String chatId;
    private SendMessage message;

    public AtmMoneyMessage(Update update, String request, String chatId) {
        this.update = update;
        this.request = request;
        this.chatId = chatId;
    }


    public SendMessage sendMoneyToUser() {
        message = new SendMessage();
        Parser parser = new Parser();
        try {
            String answer = parser.view(request);
            message.setChatId(chatId);
            if (!answer.equals("")) {
                message.setText("В этом банкомате на данный момент " + answer);
            }
            else{
                message.setText(" Sorry, choose another ATM or location");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

}