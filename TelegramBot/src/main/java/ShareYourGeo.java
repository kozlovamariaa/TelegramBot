
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.util.HashMap;


public class ShareYourGeo extends InlineKeyboardButton {
    private Double longitude;
    private Double latitude;
    private Update update;

    public ShareYourGeo(Double longitude, Double latitude,Update update){
        this.longitude = longitude;
        this.latitude = latitude;
        this.update = update;
    }
    public void print(){
        System.out.println("latitude " + latitude);
        System.out.println("longitude " + longitude);
    }

    public HashMap<String, String> checkIfThereATM() throws IOException {
        Parser parser = new Parser();
        HashMap<String, String> listOfAtms2 = parser.viewForList(latitude,longitude);
        return listOfAtms2;
    }
}
