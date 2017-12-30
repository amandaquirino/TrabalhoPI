package br.edu.ifrn.trabalhopi.util;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import br.edu.ifrn.trabalhopi.dominio.Usuario;



/**
 * Classe com métodos úteis relativos a usuários do sistema.
 * @author ArthurAmanda
 */
public class UsuarioUtil {
	
	/** Obtém o usuário logado no sistema. */
	public static Usuario getUsuarioLogado(){
		if (FacesContext.getCurrentInstance() == null)
			return null;
		
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		return (Usuario) req.getSession().getAttribute("usuarioLogado");
	}

}
