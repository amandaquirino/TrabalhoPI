package br.edu.ifrn.trabalhopi.bean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import br.edu.ifrn.trabalhopi.arquitetura.ControladorGeral;
import br.edu.ifrn.trabalhopi.dao.UsuarioDAO;
import br.edu.ifrn.trabalhopi.dominio.TipoUsuario;
import br.edu.ifrn.trabalhopi.dominio.Usuario;
import br.edu.ifrn.trabalhopi.dominio.ValidadorUtil;
import br.edu.ifrn.trabalhopi.util.CriptografiaUtils;



/**
 * MBean que controla o login no sistema. 
 * @author ArthurAmanda
 */
@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class LoginMBean extends ControladorGeral {
	
	/** Armazena os dados informados na tela de login. */
	private Usuario usuario;
	
	/** Armazena os dados iniciais de cadastro do usu�rio. */
	private Usuario usuarioCadastro;
	
	@PostConstruct
	private void init(){
		usuario = new Usuario();
		usuarioCadastro = new Usuario();
		usuarioCadastro.setTipoUsuario(TipoUsuario.COMUM);
	}
	
	/** Autentica o usu�rio e faz login no sistema. */
	public String autenticar(){
		if (!validarLogin()){
			return null;
		}
		
		try {
			UsuarioDAO dao = new UsuarioDAO();
			usuario = dao.findUsuarioByLoginSenha(usuario.getEmail(), CriptografiaUtils.criptografarMD5(usuario.getSenha()));
			
			if (!ValidadorUtil.isEmpty(usuario)){
				if (!usuario.isAtivo()){
					addMsgError("Este usu�rio foi desabilitado e n�o possui mais acesso ao sistema.");
				 	return null;
				}
			} else {
				init();
				addMsgError("Usu�rio/Senha incorretos.");
				return null;
			}
			
			//Salva o usu�rio permanentemente na mem�ria do sistema 
			getCurrentSession().setAttribute("usuarioLogado", usuario);
			return "/portal/index.xhtml";
			
		} catch (Exception e) {
			tratamentoErroPadrao(e);
			return null;
		} 
	}
	
	/** Verifica se os dados para login est�o corretos */
	public boolean validarLogin(){
		if (usuario == null || (ValidadorUtil.isEmpty(usuario.getEmail()) && 
				ValidadorUtil.isEmpty(usuario.getSenha()))){
			addMsgError("Usu�rio/senha n�o informados.");
			return false;
		}
		
		if (ValidadorUtil.isEmpty(usuario.getEmail())){
			addMsgError("Usu�rio: campo obrigat�rio n�o informado.");
			return false;
		}
		
		if (ValidadorUtil.isEmpty(usuario.getSenha())){
			addMsgError("Senha: campo obrigat�rio n�o informado.");
			return false;
		}
		
		return true;
	}
	
	/** Realiza logoff do sistema. */
	public String logoff(){
		getCurrentSession().invalidate();
		return "/portal/login.xhtml";
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuarioCadastro() {
		return usuarioCadastro;
	}

	public void setUsuarioCadastro(Usuario usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}

}
