package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fattoria.dao.DAO;
import fattoria.dao.eccezioni.DAOException;

@WebServlet("/ServletDelete")
public class ServletDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String risorsa = "errore.jsp";
		
		try {
			String tipo = request.getParameter("tipo");

			if(tipo == null)
				tipo = "";
			
			String codice = request.getParameter("codice");
			
			if(codice == null || codice.isEmpty())
				throw new IllegalArgumentException("Codice non può essere null o vuoto");
			
			DAO.getInstance(tipo).delete(Integer.parseInt(codice));
			risorsa = "ServletSelect?destinazione=select.jsp";
		} catch (IllegalArgumentException | DAOException e) {
			request.setAttribute("errore", e.getMessage());
		}
		
		getServletContext().getRequestDispatcher("/" + risorsa).forward(request, response);
	}

}
