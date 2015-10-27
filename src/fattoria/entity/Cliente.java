package fattoria.entity;

import java.sql.Timestamp;

public class Cliente implements Comparable<Cliente> {
	private int codcli;
	private String nome, cognome, recapito;
	private Timestamp datareg;
	private String citta;

	public Cliente(int codice, String nome, String cognome, String recapito,
			Timestamp datareg, String citta) {
		this.codcli = codice;
		this.nome = (nome == null ? "" : nome);
		this.cognome = (cognome == null ? "" : cognome);
		this.recapito = recapito;
		this.datareg = datareg; // (datareg == null ? new
								// Timestamp(System.currentTimeMillis()) :
								// datareg);
		this.citta = citta;
	}
	
	public Cliente(String nome, String cognome, String recapito,
			Timestamp datareg, String citta) {
		this(0, nome, cognome, recapito, datareg, citta);
	}

	public Cliente(int codice, String nome, String cognome, String citta) {
		this(codice, nome, cognome, null, null, citta);
	}

	public Cliente(String nome, String cognome, String citta) {
		this(0, nome, cognome, null, null, citta);
	}

	public Cliente(int codice, String nome, String cognome, String recapito,
			String citta) {
		this(codice, nome, cognome, recapito, null, citta);
	}

	public Cliente(int codice, String nome, String cognome, Timestamp datareg,
			String citta) {
		this(codice, nome, cognome, null, datareg, citta);
	}

	public int getCodice() {
		return codcli;
	}

	public String getNome() {
		return nome;
	}

	public String getCognome() {
		return cognome;
	}

	public String getRecapito() {
		return recapito;
	}

	public Timestamp getDatareg() {
		return datareg;
	}

	public String getCitta() {
		return citta;
	}

	@Override
	public String toString() {
		return "<td>" + codcli + "</td><td>" + nome + "</td><td>"
				+ cognome + "</td><td>" + recapito + "</td><td>" + datareg
				+ "</td><td>" + citta + "</td>";
	}

	@Override
	public int compareTo(Cliente o) {
		if(o != null)
			return (citta + nome + cognome + datareg).compareTo(o.citta + o.nome + o.cognome + o.datareg);
		
		return -1;
	}
	
	public String getDescr() {
		return codcli + ") " + nome + " " + cognome;
	}
}
