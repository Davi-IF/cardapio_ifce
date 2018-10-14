package bot;

import java.sql.SQLException;
import java.util.Scanner;

import bot.jdbc.ConexaoJDBC;

public class TestaConexao {

	public static void main(String[] args) {
		
		ConexaoJDBC bd = new ConexaoJDBC();
		
		try {
			System.out.println(bd.getConnection());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Scanner sc = new Scanner(System.in);
		
		int a = sc.nextInt();

	}

}
