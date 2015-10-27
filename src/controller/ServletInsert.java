package controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fattoria.business.Sesso;
import fattoria.dao.*;
import fattoria.dao.eccezioni.DAOException;
import fattoria.entity.*;

@WebServlet("/ServletInsert")
public class ServletInsert extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String risorsa = "errore.jsp";
		
		try {
			String tipo = request.getParameter("tipo");
			Object entity = null;
			DAO dao = null;
			
			if(tipo == null)
				tipo = "";
			
			switch(tipo) {
				case "animale":
					String nome = request.getParameter("nome");
					if(nome == null || nome.isEmpty())
						throw new IllegalArgumentException("Nome non può essere null o vuoto");
					Date datanas = request.getParameter("datanas") == null || request.getParameter("datanas").isEmpty() ? null : new Date(DateFormat.getDateInstance(DateFormat.SHORT).parse(request.getParameter("datanas")).getTime());
					Sesso sesso = request.getParameter("sesso") == null || request.getParameter("sesso").isEmpty() ? null : Sesso.valueOf(request.getParameter("sesso"));
					Boolean vaccino = request.getParameter("vaccino") == null ? false : request.getParameter("vaccino").equals("1");
					String specie = request.getParameter("specie");
					
					dao = new DAOLibretto();
					entity = new Libretto(nome, datanas, sesso, vaccino, specie);
					
					break;
				case "cliente":
					nome = request.getParameter("nome");
					if(nome == null || nome.isEmpty())
						throw new IllegalArgumentException("Nome non può essere null o vuoto");
					String cognome = request.getParameter("cognome");
					if(cognome == null || cognome.isEmpty())
						throw new IllegalArgumentException("Cognome non può essere null o vuoto");
					String recapito = request.getParameter("recapito") != null && request.getParameter("recapito").isEmpty() ? null : request.getParameter("recapito");
					Timestamp datareg = request.getParameter("datareg") == null || request.getParameter("datareg").isEmpty() ? null : new Timestamp(DateFormat.getDateInstance(DateFormat.SHORT).parse(request.getParameter("datareg")).getTime());
					String citta = request.getParameter("citta") != null && request.getParameter("citta").isEmpty() ? null : request.getParameter("citta");
					
					dao = new DAOCliente();
					entity = new Cliente(nome, cognome, recapito, datareg, citta);
					
					break;
				case "acquisto":
					int codcli = Integer.parseInt(request.getParameter("codcli"));
					int codice = Integer.parseInt(request.getParameter("codice"));
					double prezzo = Double.parseDouble(request.getParameter("prezzo"));
					
					dao = new DAOAcquisto();
					entity = new Acquisto(new DAOCliente().selectByPK(codcli), new DAOLibretto().selectByPK(codice), prezzo);
					
					break;
				default:
					request.setAttribute("errore", "Il parametro 'tipo' ha un valore non previsto");
			}
			
			dao.insert(entity);
			risorsa = "ServletSelect?destinazione=select.jsp";
		} catch (IllegalArgumentException | DAOException | ParseException e) {
			request.setAttribute("errore", e.getMessage());
		}
		
		getServletContext().getRequestDispatcher("/" + risorsa).forward(request, response);
	}

}
