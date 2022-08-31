import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Buttons extends InlineKeyboardMarkup {
    private Update update;

    public Buttons(Update update){
        this.update = update;
    }

    public InlineKeyboardMarkup createButtons(HashMap<String,String > mapWithAddresses){
        ArrayList<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        for(Map.Entry<String, String> entry: mapWithAddresses.entrySet()){
            String value = entry.getValue();
            String key = entry.getKey();
            List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(value);
            button.setCallbackData(key);
            keyboardButtonsRow1.add(button);
            rowList.add(keyboardButtonsRow1);
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
