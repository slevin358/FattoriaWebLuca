package fattoria.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import fattoria.business.Sesso;
import fattoria.dao.eccezioni.DAOConnessioneException;
import fattoria.dao.eccezioni.DAOException;
import fattoria.entity.Libretto;

public final class DAOLibretto extends DAO<Libretto> {

	public DAOLibretto() throws DAOConnessioneException {
		super();
	}

	public int insert(Libretto animale) throws DAOException {
		String sql = "INSERT INTO libretto ( nome, datanas, sesso, vaccino, specie ) VALUES ( ?, ?, ?, ?, ? )";

		try(PreparedStatement pst = con.prepareStatement(sql, new String[] { "codice" })) {//, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			this.pst = pst;
			int i = 1;
			pst.setString(i++, animale.getNome());
			pst.setDate(i++, animale.getDatanas());
			pst.setObject(i++, animale.getSesso() == null ? null : animale.getSesso().toString());
			pst.setInt(i++, animale.isVaccino() ? 1 : 0);
			pst.setObject(i++, animale.getSpecie() == null ? null : animale.getSpecie());
			return insertInto(true);
		} catch(SQLException e) {
			throw new DAOException("Errore nella Insert: " + e.getMessage());
		}
	}

	public ArrayList<Libretto> select() throws DAOException {
		ArrayList<Libretto> r = new ArrayList<>();

		String sql = "SELECT codice, nome, datanas, " +
				"sesso, vaccino, specie FROM libretto " +
				"ORDER BY codice";

		try(PreparedStatement pst = con.prepareStatement(sql)) {//, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			this.pst = pst;
			rs = pst.executeQuery();

			while(rs.next())
				r.add(componiEntity());

			if(r.isEmpty())
				throw new DAOException("Avviso: la Select ha restituito un risultato vuoto");

			return r;
		} catch(SQLException e) {
			throw new DAOException("Errore nella Select ALL: " + e.getMessage());
		}
	}

	@Override
	Libretto componiEntity() throws DAOException {
		try {
			int codice = rs.getInt("codice");
			String nome = rs.getString("nome");
			Date datanas = rs.getDate("datanas");
			String sesso = rs.getString("sesso");
			int vaccino = rs.getInt("vaccino");
			String specie = rs.getString("specie");

			return new Libretto(codice, nome, datanas, sesso == null ? null : Sesso.valueOf(sesso), vaccino == 1, specie);
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}
	}

	public Libretto selectByPK(int codice) throws DAOException {
		String sql = "SELECT codice, nome, datanas, sesso, " +
				"vaccino, specie FROM libretto WHERE codice = ?";

		try(PreparedStatement pst = con.prepareStatement(sql)) {//, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			this.pst = pst;

			pst.setInt(1, codice);

			rs = pst.executeQuery();

			if(rs.next())
				return componiEntity();
			else
				throw new DAOException("Avviso: la Select PK ha restituito un risultato vuoto");
		} catch(SQLException e) {
			throw new DAOException("Errore nella Select: " + e.getMessage());
		}
	}

	public Libretto update(Libretto animale) throws DAOException {
		String sql = "UPDATE libretto SET nome = ?, datanas = ?, "
				+ "sesso = ?, vaccino = ?, specie = ? WHERE codice = ?";

		try(PreparedStatement pst = con.prepareStatement(sql)) {//, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			this.pst = pst;

			Libretto r = selectByPK(animale.getCodice());

			pst.setString(1, animale.getNome());
			pst.setDate(2, animale.getDatanas());
			pst.setObject(3, animale.getSesso());
			pst.setInt(4, animale.isVaccino() ? 1 : 0);
			pst.setObject(5, animale.getSpecie());
			pst.setInt(6, animale.getCodice());

			int n = pst.executeUpdate();

			if(n > 0)
				return r;
			else
				throw new DAOException("Errore: la Update non ha modificato alcun valore");
		} catch(SQLException e) {
			throw new DAOException("Errore nella Insert: " + e.getMessage());
		}
	}

	public Libretto delete(Object codice) throws DAOException {
		String sql = "DELETE FROM libretto WHERE codice = ?";

		try(PreparedStatement pst = con.prepareStatement(sql)) {//, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			this.pst = pst;

			Libretto r = selectByPK((int) codice);

			pst.setInt(1, (int) codice);

			int n = pst.executeUpdate();

			if(n > 0)
				return r;
			else
				throw new DAOException("Errore: la Delete non ha eliminato alcun valore");
		} catch(SQLException e) {
			throw new DAOException("Errore nella Select: " + e.getMessage());
		}
	}

	public ArrayList<String> columnNames() {
		return new ArrayList<String>(Arrays.asList(new String[] {
				"Codice", "Nome", "Datanas", "Sesso", "Vaccino", "Specie"}));
	}

	@Override
	String getNomeTabella() {
		return "libretto";
	}

	@Override
	String getOrdinamento() {
		return "codice";
	}
}
