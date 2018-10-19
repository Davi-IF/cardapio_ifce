package bot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.InputMismatchException;
import java.util.List;

import org.glassfish.jersey.process.internal.RequestScope.Instance;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.vdurmont.emoji.EmojiParser;

public class Cardapio_bot extends TelegramLongPollingBot{
	
	StringBuilder almoco = new StringBuilder();
	StringBuilder janta = new StringBuilder();
	List<String> list = new ArrayList<String>();
	
	public String getBotUsername() {		
		return "cardapio_ifce_bot";
	}

	public void onUpdateReceived(Update update) {
		
		
		
		if(update.getMessage().getText().equals("/cardapio")) {
			
			getCardapio(update);
			
		}else if(update.getMessage().getText().equals("/feedback")) {
			
			SendMessage sm = new SendMessage()
					.setChatId(update.getMessage().getChatId())
					.setText("Por favor, escolha sua refeição");
			
			try {
				List<String> almoco = getAlmoco(update);
				
				ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
			    List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
			    
			    KeyboardRow row;
			  
			    for(int i = 0; i < almoco.size(); i++) {
			    	row = new KeyboardRow();
			    	row.add(almoco.get(i));
			    	keyboard.add(row);
			    }
			    keyboardMarkup.setKeyboard(keyboard);
			    sm.setReplyMarkup(keyboardMarkup);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				execute(sm);
			}catch(TelegramApiException e) {
				e.printStackTrace();
			}
		}else if(list.contains(update.getMessage().getText())) {
			SendMessage sm = new SendMessage()
					.setChatId(update.getMessage().getChatId())
					.setText("Por favor, nos dê sua avaliação a respeito do prato.");
			
			ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
		    List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
		    KeyboardRow row = new KeyboardRow();
		    
		    row.add(EmojiParser.parseToUnicode(":star:"));
		    row.add(EmojiParser.parseToUnicode(":star: :star:"));
		    
		    keyboard.add(row);
		    
		    row = new KeyboardRow();
		    
		    row.add(EmojiParser.parseToUnicode(":star: :star: :star:"));
		    row.add(EmojiParser.parseToUnicode(":star: :star: :star: :star:"));
		    
		    keyboard.add(row);
		    
		    row = new KeyboardRow();
		    
		    row.add(EmojiParser.parseToUnicode(":star: :star: :star: :star: :star:"));
		    
		    keyboard.add(row);
		    
		    keyboardMarkup.setKeyboard(keyboard);
		    sm.setReplyMarkup(keyboardMarkup);
		    
		    try {
				execute(sm);
			} catch (TelegramApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    list.clear();
		}else if(update.getMessage().getText().contains(EmojiParser.parseToUnicode(":star:"))) {
			SendMessage sm = new SendMessage();
			
			String[] avaliacao = update.getMessage().getText().split(" ");
			
			sm.setChatId(update.getMessage().getChatId());
			sm.setText("Avaliação armazenada com sucesso.\n\nMuito obrigado! "+EmojiParser.parseToUnicode(":+1:"));
			
			ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove();
			sm.setReplyMarkup(keyboardMarkup);
			try {
			    execute(sm);
			} catch (TelegramApiException e) {
			    e.printStackTrace();
			}
		}
	}
	@Override
	public String getBotToken() {	
		return "";
	}
	
	private void clearKeyboard(Update update){
	    SendMessage msg = new SendMessage()
                .setChatId(update.getMessage().getChatId());
	    
		
	}
	
	private void getCardapio(Update update) {
		StringBuilder almoco = new StringBuilder();
		StringBuilder janta = new StringBuilder();
		SendMessage sm = new SendMessage();
		
		String refeicaoA = "", refeicaoJ = "";
		
		GregorianCalendar calendar = new GregorianCalendar();
		Integer diaInteiro = new Integer(calendar.get(GregorianCalendar.DAY_OF_MONTH));
		
		
		
		String diaAtual;
		if(diaInteiro < 10) {
			diaAtual = "0"+diaInteiro.toString(); 
		}else {
			diaAtual = diaInteiro.toString();
		}
		
		String cabeca = "";
		int indice = 0;
		
		try {
			Document ifce = Jsoup.connect
					("https://ifce.edu.br/sobral/campus-sobral/cardario-restaurante-academico")
					.timeout(60000).validateTLSCertificates(false).get();
			
			Elements dias = ifce.select("table.plain").select("tr").first().select("th");
			
			String[] dia = new String[2];
			
			for(int i = 1; i < dias.size(); i ++) {
				 dia = dias.get(i).select("p").get(1).text().split("/");
				 if(diaAtual.equals(dia[0])) {
					 indice = i;
					 cabeca = "Cardápio do dia: "+dias.get(i).select("p").get(1).text();
					 break;
				 }	 
			}
			
			refeicaoA = ifce.select("table.plain")
					.select("tr").get(1).select("td").first().text() 
					+"\n";
			
			refeicaoJ = ifce.select("table.plain")
					.select("tr").get(2).select("td").first().text() 
					+"\n";
			
			Elements opcoesA = ifce.select("table.plain")
					.select("tr").get(1).select("td").get(indice).select("p");
			
			Elements opcoesJ = ifce.select("table.plain")
					.select("tr").get(2).select("td").get(indice).select("p");
			
			for(int i = 0; i < opcoesA.size(); i++) {
				almoco.append(" "+opcoesA.get(i).text()+"\n\n");
			}
			
			for(int i = 0; i < opcoesJ.size(); i++) {
				janta.append(" "+opcoesJ.get(i).text()+"\n\n");
			}
			
			if(opcoesA.isEmpty()) {
				almoco.append("Almoço não informado pelo o Campus. \n");
			}
			
			if(opcoesJ.isEmpty()) {
				janta.append("Janta não informado pelo o Campus. \n");
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		sm.setChatId(update.getMessage().getChatId());
		sm.setText(cabeca+"\n\n"+refeicaoA+"\n"+almoco+"\n"+refeicaoJ+"\n"+janta);
		
		try {
			execute(sm);
		}catch(TelegramApiException e) {
			e.printStackTrace();
		}
	}
	
	private List<String> getAlmoco(Update update) throws IOException {
		SendMessage sm = new SendMessage();
		
		Document ifce = Jsoup.connect
				("https://ifce.edu.br/sobral/campus-sobral/cardario-restaurante-academico")
				.timeout(60000).validateTLSCertificates(false).get();
		
		int indice = 0;
		Elements dias = ifce.select("table.plain").select("tr").first().select("th");
		String[] dia = new String[2];
		
		GregorianCalendar calendar = new GregorianCalendar();
		Integer diaInteiro = new Integer(calendar.get(GregorianCalendar.DAY_OF_MONTH));
		
		if(calendar.get(GregorianCalendar.DAY_OF_WEEK) == 7 ||
				calendar.get(GregorianCalendar.DAY_OF_WEEK) == 1) {
			sm.setChatId(update.getMessage().getChatId());
			sm.setText("Hoje não tem almoco e janta");
			
			try {
				execute(sm);
			} catch (TelegramApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		String diaAtual;
		if(diaInteiro < 10) {
			diaAtual = "0"+diaInteiro.toString(); 
		}else {
			diaAtual = diaInteiro.toString();
		}
		for(int i = 1; i < dias.size(); i ++) {
			 dia = dias.get(i).select("p").get(1).text().split("/");
			 if(diaAtual.equals(dia[0])) {
				 indice = i;
				 break;
			 }	 
		}
		
		Elements opcoesA = ifce.select("table.plain")
				.select("tr").get(1).select("td").get(indice).select("p");
		
		for(int i = 0; i < opcoesA.size(); i++) {
			
			if(!opcoesA.get(i).text().isEmpty())
				list.add(opcoesA.get(i).text());
		}
		
		return list;
	}

}
