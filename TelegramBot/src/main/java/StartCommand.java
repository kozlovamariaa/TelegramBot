import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;


public class StartCommand extends BotCommand {
    private Update update;
    private SendMessage message;

    public StartCommand(Update update) {
        super("start", "Привет! Я знаю сколько $ находится в банкоматах Тинькофф. Пришли Id банкомата и я скажу, сколько $ ты можешь там снять.");
        this.update = update;
    }

    public SendMessage sendMessage(){
        String answer = "Привет! Я знаю сколько рублей находится в банкоматах Тинькофф. Пришли свою геолокацию или Id банкомата и я скажу какие банкоматы есть поблизости, сколько $ ты можешь снять.";
        String chatId = String.valueOf(update.getMessage().getChatId());
        message = new SendMessage();
        message.setChatId(chatId);
        message.setText(answer);
        return message;
    }
}
