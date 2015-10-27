package fattoria.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import fattoria.dao.eccezioni.DAOConnessioneException;
import fattoria.dao.eccezioni.DAOException;
import fattoria.entity.Cliente;

public final class DAOCliente extends DAO<Cliente> {
	/*public static void main(String[] args) {
		DAOCliente dao;
		try {
			dao = new DAOCliente();
			Cliente c = new Cliente (0, "Nome", "Cognome", "Città");
//			dao.insert(c);
//			System.out.println("Inserimento: " + c);
			c = new Cliente("Nuovo nome", "Nuovo cognome", "Nuova città");
			System.out.println("Modifica: " + dao.update(c));
			System.out.println("Lista: " + dao.select());
			//System.out.println("Eliminazione: " + dao.delete(pk));
			System.out.println("Clienti senza acquisti: " + dao.clientiNoAcquisti());
			String citta = "Quartu";
			System.out.println("Clienti di " + citta + ": " + dao.clientiDiCitta(citta));
			String clausola = "ca";
			System.out.println("Clienti LIKE " + clausola + ": " + dao.selectLike(clausola));
		} catch (DAOException e) {
			System.out.println(e.getMessage());
		}
	}*/

	public DAOCliente() throws DAOConnessioneException {
		super();
	}

	public int insert(Cliente cliente) throws DAOException {
		String sql = "INSERT INTO clienti ( nome, cognome, recapito"
				+ (cliente.getDatareg() == null ? "" : ", datareg")
				+ ", citta ) VALUES ( ?, ?, ?"
				+ (cliente.getDatareg() == null ? "" : ", ?")
				+ ", ? )";

		try(PreparedStatement pst = con.prepareStatement(sql, new String[] { "codcli" })) {//, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			this.pst = pst;
			int i = 1;
			pst.setString(i++, cliente.getNome());
			pst.setString(i++, cliente.getCognome());
			pst.setString(i++, cliente.getRecapito());
			if(cliente.getDatareg() != null)
				pst.setTimestamp(i++, cliente.getDatareg());
			pst.setString(i++, cliente.getCitta());

			return insertInto(true);
		} catch(SQLException e) {
			throw new DAOException("Errore nella Insert: " + e.getMessage());
		}
	}

	public ArrayList<Cliente > select() throws DAOException {
		ArrayList<Cliente > r = new ArrayList<>();

		String sql = "SELECT codcli, nome, cognome, " +
				"recapito, datareg, citta FROM clienti " +
				"ORDER BY codcli";

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

/*	public ArrayList<Cliente > selectLike(String criterio) throws DAOException {
		ArrayList<Cliente > r = new ArrayList<>();

		String sql = "SELECT codcli, nome, cognome, " +
				"recapito, datareg, citta FROM clienti " +
				"WHERE upper(nome) LIKE ? OR upper(cognome) LIKE ? " +
				"OR upper(recapito) LIKE ? OR upper(citta) LIKE ?";

		try {
			pst = con.prepareStatement(sql);

			criterio = criterio.toUpperCase() + "%";

			for(int i = 1; i < 5; i++)
				pst.setString(i, criterio);

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

	public ArrayList<Cliente > clientiNoAcquisti() throws DAOException {
		ArrayList<Cliente > r = new ArrayList<>();

		String sql = 
				"select * " +
						"from clienti " +
						"where codcli not in ( " +
						"	select codcli " +
						"	from acquisti " +
						") " +
						"ORDER BY nome, cognome, citta";

		try {
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();

			while(rs.next())
				r.add(componiEntity());

			if(r.isEmpty())
				throw new DAOException("Avviso: la clientiNoAcquisti ha restituito un risultato vuoto");

			return r;
		} catch(SQLException e) {
			throw new DAOException("Errore nella clientiNoAcquisti: " + e.getMessage());
		} finally {
			chiudiStatement();
		}
	}

	public ArrayList<Cliente > clientiDiCitta(String citta) throws DAOException {
		ArrayList<Cliente > r = new ArrayList<>();

		String sql = 
				"select * " +
						"from clienti " +
						"where citta = ? " +
						"ORDER BY nome, cognome";

		try {
			pst = con.prepareStatement(sql);

			pst.setString(1, citta);

			rs = pst.executeQuery();

			while(rs.next())
				r.add(componiEntity());

			if(r.isEmpty())
				throw new DAOException("Avviso: la clientiDiCitta ha restituito un risultato vuoto");

			return r;
		} catch(SQLException e) {
			throw new DAOException("Errore nella clientiDiCitta: " + e.getMessage());
		} finally {
			chiudiStatement();
		}
	}*/

	@Override
	Cliente componiEntity() throws DAOException {
		try {
			int codcli = rs.getInt("codcli");
			String nome = rs.getString("nome");
			String cognome = rs.getString("cognome");
			String recapito = rs.getString("recapito");
			Timestamp datareg = rs.getTimestamp("datareg");
			String citta = rs.getString("citta");

			return new Cliente (codcli, nome, cognome, recapito, datareg, citta);
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}
	}

	public Cliente selectByPK(int codcli) throws DAOException {
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
			Cliente r = selectByPK(cliente.getCodice());

			pst = con.prepareStatement(sql);//, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

			int i = 1;
			pst.setString(i++, cliente.getNome());
			pst.setString(i++, cliente.getCognome());
			pst.setString(i++, cliente.getRecapito());
			if(cliente.getDatareg() != null)
				pst.setTimestamp(i++, cliente.getDatareg());
			pst.setString(i++, cliente.getCitta());
			pst.setInt(i++, cliente.getCodice());

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

	public Cliente delete(Object codcli) throws DAOException {
		String sql = "DELETE FROM clienti WHERE codcli = ?";

		try {
			Cliente r = selectByPK((int) codcli);

			pst = con.prepareStatement(sql);

			pst.setInt(1, (int) codcli);

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

	public ArrayList<String> columnNames() {
		return new ArrayList<String>(Arrays.asList(new String[] {
				"Codcli", "Nome", "Cognome", "Recapito", "Datareg", "Citta"}));
	}

	@Override
	String getNomeTabella() {
		return "clienti";
	}

	@Override
	String getOrdinamento() {
		return "codcli";
	}
}
