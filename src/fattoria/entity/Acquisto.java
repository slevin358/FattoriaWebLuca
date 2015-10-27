package fattoria.entity;

public class Acquisto {
	private Cliente cliente;
	private Libretto animale;
	private double prezzo;

	public Acquisto(Cliente cliente, Libretto animale, double prezzo) {
		this.cliente = cliente;
		this.animale = animale;
		this.prezzo = prezzo;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public Libretto getAnimale() {
		return animale;
	}

	public double getPrezzo() {
		return prezzo;
	}

	@Override
	public String toString() {
		return cliente + "" + animale
				+ "<td>" + prezzo + "</td>";
	}
	
	public int getCodice() {
		return animale.getCodice();
	}
	
	public String getDescr() {
		return animale.getCodice() + ") " + animale.getNome() + ", " + animale.getSpecie() + " di " + cliente.getNome();
	}
}
