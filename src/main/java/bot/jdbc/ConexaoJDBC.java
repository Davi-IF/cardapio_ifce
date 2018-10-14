package bot.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoJDBC {
	
private static ConexaoJDBC conexao;
	
	public static ConexaoJDBC getIntance() {
		if(conexao == null) {
			conexao = new ConexaoJDBC();
		}
		return conexao;
	}
	
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		
		String url = "jdbc:mysql://localhost:3306/cardapio_ifce?autoReconnect=true&useSSL=false";
		
		return DriverManager.getConnection(url,"root","Davi1708");
	}

}
