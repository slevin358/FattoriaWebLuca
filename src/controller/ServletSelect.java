package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fattoria.dao.DAO;
import fattoria.dao.eccezioni.DAOException;

@WebServlet("/ServletSelect")
public class ServletSelect extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String risorsa = "errore.jsp";

		try {
			creaListe(request, "tipo");
			
			risorsa = request.getParameter("destinazione");

			if(risorsa == null) {
				risorsa = "errore.jsp";
				throw new DAOException("Il parametro 'destinazione' non può essere null");
			}
		} catch (IllegalArgumentException | DAOException e) {
			request.setAttribute("errore", e.getMessage());
		}
		
		getServletContext().getRequestDispatcher("/" + risorsa).forward(request, response);
	}

	@SuppressWarnings("rawtypes")
	private void creaListe(HttpServletRequest request, String param) throws DAOException {
		String[] params = request.getParameterValues(param);
		boolean multi = params.length > 1;
		
		for(String tipo : params) {
			DAO dao = DAO.getInstance(tipo);
	
			request.setAttribute((multi ? ("elenco_" + tipo) : "elenco"), dao.select());
			request.setAttribute((multi ? ("colonne_" + tipo) : "colonne"), dao.columnNames());
		}
	}
}
