package bot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class App {

	public static void main(String[] args) {
		ApiContextInitializer.init();
		
		TelegramBotsApi bot = new TelegramBotsApi();
		
		try {
			bot.registerBot(new Cardapio_bot());
		}catch(TelegramApiRequestException e) {
			System.out.println(e);
		}

	}

}
