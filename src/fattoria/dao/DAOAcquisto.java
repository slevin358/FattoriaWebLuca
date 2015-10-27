package fattoria.dao;

import java.sql.*;
import java.util.ArrayList;

import fattoria.dao.eccezioni.*;
import fattoria.entity.*;
import fattoria.business.Sesso;

public final class DAOAcquisto extends DAO<Acquisto> {
	public DAOAcquisto() throws DAOConnessioneException {
		super();
	}
	
	public ArrayList<Acquisto> select() throws DAOException {
		ArrayList<Acquisto> r = new ArrayList<>();
		
		String sql = "SELECT codcli, codice, prezzo FROM acquisti " +
					"ORDER BY codcli, codice";

		try {
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();
			
			while(rs.next())
				r.add(componiEntity());
			
			if(r.isEmpty())
				throw new DAOException("Avviso: la Select ha restituito un risultato vuoto");
		
			return r;
		} catch(SQLException e) {
			throw new DAOException("Errore nella Select ALL: " + e.getMessage());
		} finally {
			chiudiStatement();
		}
	}
	
	public ArrayList<Acquisto> clientiConAcquisti(String citta) throws DAOException {
		ArrayList<Acquisto> r = new ArrayList<>();
		
		citta = citta.toLowerCase();
		
		String sql = 
				"select c.codcli, c.nome, cognome, recapito, datareg, citta, " +
				"prezzo, l.codice, l.nome, l.datanas, l.sesso, l.vaccino, l.specie " +
				"from clienti c " +
				"left join acquisti a " +
				"on c.codcli = a.codcli " +
				"left join libretto l " +
				"on a.codice = l.codice " +
				"where lower(citta) = ? " +
				"order by 2, 3";

		try {
			pst = con.prepareStatement(sql);
			
			pst.setString(1, citta);
			
			rs = pst.executeQuery();
			
			while(rs.next())
				r.add(componiEntityFull());
			
			if(r.isEmpty())
				throw new DAOException("Avviso: la Select ha restituito un risultato vuoto");
		
			return r;
		} catch(SQLException e) {
			throw new DAOException("Errore nella Select ALL: " + e.getMessage());
		} finally {
			chiudiStatement();
		}
	}
	
	@Override
	Acquisto componiEntity() throws DAOException {
		try {
			int codcli = rs.getInt("codcli");
			int codice = rs.getInt("codice");
			double prezzo = rs.getDouble("prezzo");
			
			return new Acquisto(new DAOCliente().selectByPK(codcli), new DAOLibretto().selectByPK(codice), prezzo);
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}
	}
	
	private Acquisto componiEntityFull() throws SQLException, DAOException {
		int codcli = rs.getInt(1);
		String nomeC = rs.getString(2);
		String cognome = rs.getString(3);
		String recapito = rs.getString(4);
		Timestamp datareg = rs.getTimestamp(5);
		String citta = rs.getString(6);
		
		double prezzo = rs.getDouble(7);
		
		int codice = rs.getInt(8);
		String nomeL = rs.getString(9);
		Date datanas = rs.getDate(10);
		String sesso = rs.getString(11);
		int vaccino = rs.getInt(12);
		String specie = rs.getString(13);
		
		Libretto animale = null;
		
		if(rs.getObject(8) != null)
			animale = new Libretto(codice, nomeL, datanas, sesso == null ? null : Sesso.valueOf(sesso), vaccino == 1, specie);
		
		return new Acquisto(new Cliente (codcli, nomeC, cognome, recapito, datareg, citta), 
				animale, 
				prezzo);
	}

	@Override
	public ArrayList<String> columnNames() {
		ArrayList<String> als = new ArrayList<>();
		
		try {
			als.addAll(new DAOCliente().columnNames());
			als.addAll(new DAOLibretto().columnNames());
			als.add("Prezzo");
		} catch (DAOConnessioneException e) {
			e.printStackTrace();
		}
		
		return als;
	}

	@Override
	public int insert(Acquisto acquisto) throws DAOException {
		String sql = "INSERT INTO acquisti VALUES ( ?, ?, ? )";

		try(PreparedStatement pst = con.prepareStatement(sql)) {//, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			this.pst = pst;
			int i = 1;
			pst.setInt(i++, acquisto.getCliente().getCodice());
			pst.setInt(i++, acquisto.getAnimale().getCodice());
			pst.setDouble(i++, acquisto.getPrezzo());
			return insertInto(false);
		} catch(SQLException e) {
			throw new DAOException("Errore nella Insert: " + e.getMessage());
		}
	}

	@Override
	public Acquisto delete(Object pk) throws DAOException {
		String sql = "DELETE FROM acquisti WHERE codice = ?";

		try {
			Acquisto r = selectByPK((int) pk);
			
			pst = con.prepareStatement(sql);
			
			pst.setInt(1, (int) pk);
			
			int n = pst.executeUpdate();
			
			if(n > 0)
				return r;
			else
				throw new DAOException("Errore: la Delete non ha eliminato alcun valore");
		} catch(SQLException e) {
			throw new DAOException("Errore nella Select: " + e.getMessage());
		} finally {
			chiudiStatement();
		}
	}

	/*public Cliente select(int codcli) throws DAOException {
		String sql = "SELECT codcli, nome, cognome, recapito, " +
					"datareg, citta FROM clienti WHERE codcli = ?";

		try {
			pst = con.prepareStatement(sql);
			
			pst.setInt(1, codcli);
			
			rs = pst.executeQuery();
			
			if(rs.next())
				return componiEntity();
			else
				throw new DAOException("Avviso: la Select PK ha restituito un risultato vuoto");
		} catch(SQLException e) {
			throw new DAOException("Errore nella Select: " + e.getMessage());
		} finally {
			chiudiStatement();
		}
	}
	
	public Cliente  update(Cliente cliente) throws DAOException {
		String sql = "UPDATE clienti SET nome = ?, cognome = ?, recapito = ?"
				+ (cliente.getDatareg() == null ? "" : ", datareg = ?")
				+ ", citta = ? WHERE codcli = ?";

		try {
			Cliente r = select(cliente.getCodcli());
			
			pst = con.prepareStatement(sql);//, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			
			int i = 1;
			pst.setString(i++, cliente.getNome());
			pst.setString(i++, cliente.getCognome());
			pst.setString(i++, cliente.getRecapito());
			if(cliente.getDatareg() != null)
				pst.setTimestamp(i++, cliente.getDatareg());
			pst.setString(i++, cliente.getCitta());
			pst.setInt(i++, cliente.getCodcli());
			
			int n = pst.executeUpdate();
		
			if(n > 0)
				return r;
			else
				throw new DAOException("Errore: la Update non ha modificato alcun valore");
		} catch(SQLException e) {
			throw new DAOException("Errore nella Update: " + e.getMessage());
		} finally {
			chiudiStatement();
		}
	}
	
	public Cliente delete(int codcli) throws DAOException {
		String sql = "DELETE FROM clienti WHERE codcli = ?";

		try {
			Cliente r = select(codcli);
			
			pst = con.prepareStatement(sql);
			
			pst.setInt(1, codcli);
			
			int n = pst.executeUpdate();
			
			if(n > 0)
				return r;
			else
				throw new DAOException("Errore: la Delete non ha eliminato alcun valore");
		} catch(SQLException e) {
			throw new DAOException("Errore nella Select: " + e.getMessage());
		} finally {
			chiudiStatement();
		}
	}*/

	public Acquisto selectByPK(int codice) throws DAOException {
		String sql = "SELECT codcli, codice, prezzo FROM acquisti WHERE codice = ?";

		try {
			pst = con.prepareStatement(sql);
			
			pst.setInt(1, codice);
			
			rs = pst.executeQuery();
			
			if(rs.next())
				return componiEntity();
			else
				throw new DAOException("Avviso: la Select PK ha restituito un risultato vuoto");
		} catch(SQLException e) {
			throw new DAOException("Errore nella Select: " + e.getMessage());
		} finally {
			chiudiStatement();
		}
	}
	
	@Override
	String getNomeTabella() {
		return "acquisti";
	}
	
	@Override
	String getOrdinamento() {
		return "codice, codcli";
	}

}
