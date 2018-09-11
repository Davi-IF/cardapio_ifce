package bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Cardapio_bot extends TelegramLongPollingBot{

	public String getBotUsername() {
		
		return "cardapio_ifce_bot";
	}

	public void onUpdateReceived(Update update) {
		SendMessage sm = new SendMessage();
		
		sm.setChatId(update.getMessage().getChatId());
		sm.setText("Só 4 bolachas wafer com suco de manga :'(");
		
		try {
			execute(sm);
		}catch(TelegramApiException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getBotToken() {
		
		return "653270451:AAEz3acFfFhrAbf8H4-ws69HsknhKJ2qiZ4";
	}

}
