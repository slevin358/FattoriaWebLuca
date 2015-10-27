package fattoria.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import fattoria.dao.eccezioni.DAOConnessioneException;

final class ConnessioneSingleton {
	private static ConnessioneSingleton istanza;
	private Connection con;
	
	private ConnessioneSingleton() throws DAOConnessioneException {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/orcl", "scott", "tiger");
			//System.out.println("\nCreo la connessione " + con);
		} catch (ClassNotFoundException | SQLException e) {
			throw new DAOConnessioneException("Errore nella connessione: " + e.getMessage());
		}
	}
	
	static ConnessioneSingleton getIstanza() throws DAOConnessioneException {
		if(istanza == null)
			istanza = new ConnessioneSingleton();
		
		return istanza;
	}

	Connection getCon() {
		return con;
	}
	
	static void close() throws SQLException {
		istanza.getCon().close();
		
		istanza = null;
	}
}
