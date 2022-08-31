import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

public class StopCommand extends BotCommand {
    Update update;

    public StopCommand(Update update) {
        super("stopme", "stop bot");
        this.update = update;
    }


    public SendMessage stopBot(){
        if(update.getMessage().getText().equals("/stopme")){
            String write = "Телеграм бот остановлен";
            String chatId = String.valueOf(update.getMessage().getChatId());
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(write);
            System.out.println("Я сплю.");
            return message;
        }
        return null;
    }
}
