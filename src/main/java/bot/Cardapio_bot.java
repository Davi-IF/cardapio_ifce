package bot;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.InputMismatchException;

import org.glassfish.jersey.process.internal.RequestScope.Instance;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.vdurmont.emoji.EmojiParser;

public class Cardapio_bot extends TelegramLongPollingBot{

	public String getBotUsername() {
		
		return "cardapio_ifce_bot";
	}

	public void onUpdateReceived(Update update) {
		
		if(update.getMessage().getText().equals("/cardapio")) {
			SendMessage sm = new SendMessage();
			
			String refeicaoA = "", refeicaoJ = "";
			StringBuilder almoco = new StringBuilder();
			StringBuilder janta = new StringBuilder();
			GregorianCalendar calendar = new GregorianCalendar();
			Integer diaInteiro = new Integer(calendar.get(GregorianCalendar.DAY_OF_MONTH));
			
			if(calendar.get(GregorianCalendar.DAY_OF_WEEK) == 7) {
				diaInteiro--;
			}else if(calendar.get(GregorianCalendar.DAY_OF_WEEK) == 1) {
				diaInteiro -= 2;
				
			}
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
				
				String[] dia = new String[31];
				
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
		}else if(update.getMessage().getText().equals("/feedback")) {
			SendMessage sm = new SendMessage();			
			sm.setChatId(update.getMessage().getChatId());
			sm.setText("Por favor, digite um numero de 1 a 5");
			
			try {
				execute(sm);
			}catch(TelegramApiException e) {
				e.printStackTrace();
			}
			
		}
	}
	@Override
	public String getBotToken() {
		
		return "";
	}

}
