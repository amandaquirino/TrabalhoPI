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
	
	/** Armazena os dados iniciais de cadastro do usuário. */
	private Usuario usuarioCadastro;
	
	@PostConstruct
	private void init(){
		usuario = new Usuario();
		usuarioCadastro = new Usuario();
		usuarioCadastro.setTipoUsuario(TipoUsuario.COMUM);
	}
	
	/** Autentica o usuário e faz login no sistema. */
	public String autenticar(){
		if (!validarLogin()){
			return null;
		}
		
		try {
			UsuarioDAO dao = new UsuarioDAO();
			usuario = dao.findUsuarioByLoginSenha(usuario.getEmail(), CriptografiaUtils.criptografarMD5(usuario.getSenha()));
			
			if (!ValidadorUtil.isEmpty(usuario)){
				if (!usuario.isAtivo()){
					addMsgError("Este usuário foi desabilitado e não possui mais acesso ao sistema.");
				 	return null;
				}
			} else {
				init();
				addMsgError("Usuário/Senha incorretos.");
				return null;
			}
			
			//Salva o usuário permanentemente na memória do sistema 
			getCurrentSession().setAttribute("usuarioLogado", usuario);
			return "/portal/index.xhtml";
			
		} catch (Exception e) {
			tratamentoErroPadrao(e);
			return null;
		} 
	}
	
	/** Verifica se os dados para login estão corretos */
	public boolean validarLogin(){
		if (usuario == null || (ValidadorUtil.isEmpty(usuario.getEmail()) && 
				ValidadorUtil.isEmpty(usuario.getSenha()))){
			addMsgError("Usuário/senha não informados.");
			return false;
		}
		
		if (ValidadorUtil.isEmpty(usuario.getEmail())){
			addMsgError("Usuário: campo obrigatório não informado.");
			return false;
		}
		
		if (ValidadorUtil.isEmpty(usuario.getSenha())){
			addMsgError("Senha: campo obrigatório não informado.");
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
