package fattoria.entity;

import java.sql.Date;

import fattoria.business.Sesso;

public class Libretto {
	private int codice;
	private String nome;
	private Date datanas;
	private Character sesso;
	private boolean vaccino;
	private String specie;

	public Libretto(int codice, String nome, Date datanas, Sesso sesso,
			boolean vaccino, String specie) {/*
											 * throws Exception { if(nome ==
											 * null) throw new Exception(
											 * "Non puoi assegnare NULL all'attributo non nullo 'nome'"
											 * );
											 */

		this.codice = codice;
		this.nome = (nome == null ? "" : nome);
		this.datanas = datanas;
		this.sesso = (sesso == null ? null : (sesso.name().charAt(0)));
		this.vaccino = vaccino;
		this.specie = specie;
	}
	
	public Libretto(String nome, Date datanas, Sesso sesso,
			boolean vaccino, String specie) {
		this(0, nome, datanas, sesso, vaccino, specie);
	}

	public Libretto(int codice, String nome, Date datanas, Sesso sesso) {
		this(codice, nome, datanas, sesso, false, null);
	}

	public Libretto(String nome, Date datanas, Sesso sesso) {
		this(0, nome, datanas, sesso, false, null);
	}

	public Libretto(int codice, String nome, Date datanas, Sesso sesso,
			boolean vaccino) {
		this(codice, nome, datanas, sesso, vaccino, null);
	}

	public Libretto(int codice, String nome, Date datanas, Sesso sesso,
			String specie) {
		this(codice, nome, datanas, sesso, false, specie);
	}

	public int getCodice() {
		return codice;
	}

	public String getNome() {
		return nome;
	}

	public Date getDatanas() {
		return datanas;
	}

	public Character getSesso() {
		return sesso;
	}

	public boolean isVaccino() {
		return vaccino;
	}

	public String getSpecie() {
		return specie;
	}
	
	public String getDescr() {
		return codice + ") " + nome + " (" + specie + ")";
	}

	@Override
	public String toString() {
		return "<td>" + codice + "</td><td>" + nome + "</td><td>"
				+ datanas + "</td><td>" + sesso + "</td><td>" + vaccino
				+ "</td><td>" + specie + "</td>";
	}

}
