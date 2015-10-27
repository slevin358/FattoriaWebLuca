package fattoria.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fattoria.dao.eccezioni.DAOConnessioneException;
import fattoria.dao.eccezioni.DAOException;

public abstract class DAO<T> {
	protected static Connection con;
	protected PreparedStatement pst;
	protected ResultSet rs;

	protected DAO() throws DAOConnessioneException {
		if(con == null)
			con = ConnessioneSingleton.getIstanza().getCon();
	}
	
	public void chiudiCon() throws DAOException {
		try {
			con = null;
			ConnessioneSingleton.close();
			//System.out.println("\nDistruggo la connessione");
		} catch (SQLException e) {
			throw new DAOException("Errore durante la chiusura della connessione: " + e);
		}
	}
	
	public abstract ArrayList<T> select() throws DAOException;
	public abstract ArrayList<String> columnNames();
	public abstract int insert(T entity) throws DAOException;
	public abstract T delete(Object pk) throws DAOException;
	//public abstract ArrayList<T> selectByQuery(String[] columns, String query) throws DAOException;
	//public abstract ArrayList<T> selectByQuery(String query) throws DAOException;
	public abstract T selectByPK(int pk) throws DAOException;
	abstract String getNomeTabella();
	abstract T componiEntity() throws DAOException;
	abstract String getOrdinamento();

	public ArrayList<T> selectByQuery(String[] columns, String query)
			throws DAOException {
		ArrayList<T> r = new ArrayList<>();
		String clausole = "";
		
		String sql = "SELECT * FROM " + getNomeTabella() + " "; 
		for(String column : columns){
			String clausola = "upper(" + column + ") LIKE '%" + query.toUpperCase() + "%' OR ";
			clausole += clausola;
		}
		sql += "WHERE " + clausole + " 1=0 " +
				"ORDER BY " + getOrdinamento();
		
		//System.out.println(sql);
		
		try(PreparedStatement pst = con.prepareStatement(sql)) {
			rs = pst.executeQuery();
			
			while(rs.next())
				r.add(componiEntity());
			
			if(r.isEmpty())
				throw new DAOException("Avviso: la SelectByQuery ha restituito un risultato vuoto");
		
			return r;
		} catch(SQLException e) {
			throw new DAOException("Errore nella SelectByQuery: " + e.getMessage());
		}
	}
	
	public ArrayList<T> selectByQuery(String query) throws DAOException {
		return selectByQuery((String[]) columnNames().toArray(), query);
	}
	
	public int insertInto(boolean returning) throws SQLException {
		int r = pst.executeUpdate();
		
		if(returning) {
			rs = pst.getGeneratedKeys();
			
			if(rs.next())
				r = rs.getInt(1);
		}
		
		return r;
	}

	@SuppressWarnings("rawtypes")
	public static DAO getInstance(String tipo) throws DAOConnessioneException {
		switch (tipo) {
			case "animale":
				return new DAOLibretto();
			case "cliente":
				return new DAOCliente();
			case "acquisto":
				return new DAOAcquisto();
			default:
				throw new IllegalArgumentException("Il parametro 'tipo' non è valido");
		}
	}
	
	protected void chiudiStatement() throws DAOException {
		try {
			pst.close();
		} catch (SQLException e) {
			throw new DAOException("Errore nella chiusura della Connessione: " + e.getMessage());
		}
	}
}
