package bot.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import bot.dto.FeedbackDTO;
import bot.jdbc.ConexaoJDBC;

public class FeedbackDAO {
	
	public void insert(FeedbackDTO feed) throws ClassNotFoundException, SQLException {
		
		Connection connection = ConexaoJDBC.getIntance().getConnection();
		
		String sql = "INSERT INTO feedback(avalicao) VALUES(?)";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		
		statement.setInt(1,feed.getAvaliacao());
		
		statement.execute();
		connection.close();
	}

}
