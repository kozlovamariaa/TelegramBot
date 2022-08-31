import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;



public class MyTelegramBot extends TelegramLongPollingBot  {
    private final String BOT_TOKEN = "secret string";
    private static final String BOT_NAME = "YourMoneySuperBot";
    private Timer timer;
    private Location location;
    private String chatId;
    private InlineKeyboardMarkup inlineKeyboardMarkup;
    private SendMessage message1;
    private Integer messageId;


    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String request = update.getMessage().getText();
            chatId = String.valueOf(update.getMessage().getChatId());
            if (request.equals("/start")) {
                if(timer != null){
                    timer.stop();
                }
                StartCommand start = new StartCommand(update);
                SendMessage message = start.sendMessage();
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            else if(request.equals("/changeatm")) {
                timer.stop();
                ChangeAtmCommand change = new ChangeAtmCommand(update);
                SendMessage message = change.newRequest();
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            else if(request.equals("/stopme")) {
                timer.stop();
                StopCommand stop = new StopCommand(update);
                SendMessage message = stop.stopBot();
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            else {
                ActionListener taskPerformer = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        AtmMoneyMessage atmMoneyMessage = new AtmMoneyMessage(update,request,chatId);
                        message1 = atmMoneyMessage.sendMoneyToUser();
                        try {
                            execute(message1);
                        } catch (TelegramApiException c) {
                            c.printStackTrace();
                        }

                    }
                };
                timer = new Timer(5000,taskPerformer);
                timer.restart();
            }
        }

        //удаление клавиатуры
        if(update.hasCallbackQuery()){
            if(inlineKeyboardMarkup != null){
                DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId);
                try {
                    execute(deleteMessage);
                }catch(TelegramApiException tae) {
                    throw new RuntimeException(tae);
                }
            }
            String request1 = update.getCallbackQuery().getData();
            ActionListener taskPerformer = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    AtmMoneyMessage atmMoneyMessage = new AtmMoneyMessage(update,request1,chatId);
                    message1 = atmMoneyMessage.sendMoneyToUser();
                    try {
                        execute(message1);
                    } catch (TelegramApiException c) {
                        c.printStackTrace();
                    }
                }
            };
            timer = new Timer(5000,taskPerformer);
            timer.setInitialDelay(1);
            timer.restart();
        }

        if(update.hasMessage() && update.getMessage().hasLocation()){
            if(timer != null){
                timer.stop();
            }
            String chatId = String.valueOf(update.getMessage().getChatId());
            location = update.getMessage().getLocation();
            ShareYourGeo geo = new ShareYourGeo(location.getLongitude(),location.getLatitude(), update);
            geo.print();
            try {
                HashMap<String, String > map = geo.checkIfThereATM();
                Buttons buttons = new Buttons(update);
                inlineKeyboardMarkup = buttons.createButtons(map);
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("Банкоматы поблизости: ");
                message.setReplyMarkup(inlineKeyboardMarkup);
                try {
                    Message result = execute(message);
                    messageId = result.getMessageId();
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}


