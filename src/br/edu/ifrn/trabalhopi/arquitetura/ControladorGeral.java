package br.edu.ifrn.trabalhopi.arquitetura;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.edu.ifrn.trabalhopi.auxiliares.MailHelper;
import br.edu.ifrn.trabalhopi.dominio.Usuario;
import br.edu.ifrn.trabalhopi.util.UsuarioUtil;




/**
 * Classe que implementa métodos comuns e úteis aos MBeans. Portanto,
 * deve ser estendido por eles.
 *  
 * @author ArthurAmanda
 */
@SuppressWarnings("serial")
public class ControladorGeral implements Serializable {
	
	/** Adiciona uma mensagem de informação a ser exibida para o usuário. */
	protected void addMsgInfo(String msg){
		FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
		FacesContext.getCurrentInstance().addMessage(null, fm);
	}
	
	/** Adiciona uma mensagem de aviso a ser exibida para o usuário. */
	protected void addMsgWarning(String msg){
		FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, null);
		FacesContext.getCurrentInstance().addMessage(null, fm);
	}
	
	/** Adiciona uma mensagem de erro a ser exibida para o usuário. */
	protected void addMsgError(String msg){
		FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
		FacesContext.getCurrentInstance().addMessage(null, fm);
	}
	
	/**
	 * Tratamento padrão de exceções em geral do sistema. Imprime o erro no console,
	 * envia o erro por email para a administração do sistema e adiciona uma mensagem
	 * de erro padrão a ser exibida ao usuário.
	 */
	protected void tratamentoErroPadrao(Exception e){
		e.printStackTrace();
		notificarErro(e);
		addMsgError("Ocorreu um erro ao realizar a operação. Por favor, entre em contato com a "
				+ "administração do sistema, ou tente novamente mais tarde.");
	}
	
	/** Notifica a administração do sistema sobre a ocorrência de um erro. */
	protected void notificarErro(Exception e){
		try {
			MailHelper.enviarEmail(MailHelper.EMAIL_SISTEMA, "Erro", getStackTrace(e));
		} catch (AddressException e1) {
			e1.printStackTrace();
		} catch (MessagingException e1) {
			e1.printStackTrace();
		}
	}
	
	/** Transforma uma exceção em String, para torná-la legível a humanos. */
	private static String getStackTrace(Throwable t){
		String msg = t.toString() + "<br/>";
		
		if (t.getStackTrace() != null){
			for (StackTraceElement s : t.getStackTrace()){
				msg += s.toString() + "<br/>";
			}
		}
		
		if (t.getCause() != null){
			msg += "Caused by: " + t.getCause().toString() + "<br/>";
			
			if (t.getCause().getStackTrace() != null){
				for (StackTraceElement s : t.getCause().getStackTrace()){
					msg += s.toString() + "<br/>";
				}
			}
		}
		
		
		return msg;
	}
	
	/**
	 * Possibilita o acesso ao HttpServletRequest, ou seja, à requisição de acesso
	 * à página atual.
	 */
	public HttpServletRequest getCurrentRequest() {
		return (HttpServletRequest) getExternalContext().getRequest();
	}

	/**
	 * Possibilita o acesso ao HttpServletResponse, ou seja, à resposta a ser enviada
	 * à requisição de acesso à página atual.
	 */
	public HttpServletResponse getCurrentResponse() {
		return (HttpServletResponse) getExternalContext().getResponse();
	}

	/**
	 * Possibilita o acesso ao HttpSession, ou seja, à sessão do sistema,
	 * local da memória onde ficam armazenadas as informações atuais.
	 */
	public HttpSession getCurrentSession() {
		return getCurrentRequest().getSession(true);
	}
	
	/**
	 * Acessa o external context do JavaServer Faces
	 **/
	private ExternalContext getExternalContext() {
		return FacesContext.getCurrentInstance().getExternalContext();
	}
	
	/** Retorna o usuário logado no sistema. */
	public Usuario getUsuarioLogado() {
		return UsuarioUtil.getUsuarioLogado();
	}
	
	/** Obtém um parâmetro que tenha sido enviado através da URL de uma página. */
	public String getParameter(String param) {
		return getCurrentRequest().getParameter(param);
	}
	
	/** Obtém um parâmetro (número inteiro) que tenha sido enviado através da URL de uma página. */
	public Integer getParameterInt(String param) {
		return Integer.parseInt(getParameter(param));
	}
	
	public Integer getParameterInt(String param, int padrao) {
		String valor = getParameter(param);
		return valor != null ? Integer.parseInt(valor) : padrao;
	}
	
}
