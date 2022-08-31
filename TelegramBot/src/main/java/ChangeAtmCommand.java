import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;


public class ChangeAtmCommand extends BotCommand {
    private Update update;
    private SendMessage message;

    public ChangeAtmCommand(Update update) {
        super("changeatm", "choose another ATM");
        this.update = update;
    }

    public SendMessage newRequest() {
        if (update.getMessage().getText().equals("/changeatm")) {
            String write = "Введите id нового ATM";
            String chatId = String.valueOf(update.getMessage().getChatId());
            message = new SendMessage();
            message.setChatId(chatId);
            message.setText(write);
            return message;
        }
        return null;
    }

}
