package br.edu.ifrn.trabalhopi.bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;

import br.edu.ifrn.trabalhopi.arquitetura.ControladorGeral;
import br.edu.ifrn.trabalhopi.dao.Database;
import br.edu.ifrn.trabalhopi.dao.UsuarioDAO;
import br.edu.ifrn.trabalhopi.dominio.Usuario;



/** 
 * MBean que controla opera��es relacionadas � busca de usu�rios.
 * 
 * @author ArthurAmanda
 */
@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class BuscaUsuariosMBean extends ControladorGeral {
	
	/** Op��o que ser� exibida na busca de usu�rios para ordenar a busca aleatoriamente */
	private static final int OPCAO_ORDENAR_QUALQUER = 0;
	
	/** 
	 * Op��o que ser� exibida na busca de usu�rios para ordenar a busca pelo nome,
	 * em ordem crescente. 
	 * */
	private static final int OPCAO_ORDENAR_NOME_ASC = 1;
	
	/** 
	 * Op��o que ser� exibida na busca de usu�rios para ordenar a busca pelo nome,
	 * em ordem decrescente. 
	 * */
	private static final int OPCAO_ORDENAR_NOME_DESC = 2;

	/** Armazena as informa��es preenchidas na p�gina de busca de usu�rios. */
	private Usuario usuarioBusca;
	
	/** Armazena os usu�rios encontrados no banco de acordo com os par�metros de busca. */
	private List<Usuario> usuariosEncontrados;
	
	/** Permite o acesso ao banco. */
	private UsuarioDAO dao;
	
	/** Indica qual op��o de ordenamento foi selecionada pelo usu�rio. */
	private Integer opcaoOrdenar;
	
	/** Inicializa��o do MBean */
	@PostConstruct
	private void init() {
		usuarioBusca = new Usuario();
		usuariosEncontrados = new ArrayList<>();
		opcaoOrdenar = null;
		
		dao = new UsuarioDAO();
	}
	
	/** Entra na p�gina de busca de usu�rios */
	public String entrarBuscarUsuarios(){
		return "/portal/listagemUsuario.xhtml";
	}
	
	/** Realiza a busca de usu�rios no banco. */
	public String buscar(){
		dao = new UsuarioDAO();
		
		String ordenar = opcaoOrdenar == OPCAO_ORDENAR_NOME_ASC ? "u.nome ASC" : 
							opcaoOrdenar == OPCAO_ORDENAR_NOME_DESC ? "u.nome DESC" : null;
		
		usuariosEncontrados = dao.findUsuarioGeral(usuarioBusca.getEmail(),usuarioBusca.getNome(),
				usuarioBusca.getApelido(),
				ordenar);
			
		return null;
	}
	
	/** 
	 * Inativa um usu�rio do banco de dados. N�o o remove, apenas inativa, por�m
	 * tem o mesmo efeito, j� que ele n�o pode mais fazer login. � �til para quando
	 * o administrador n�o quer perder as informa��es do registro, por diversos motivos.
	 *  
	 * */
	public String removerUsuario(Usuario usuario) throws Exception{
		
		EntityManager em = Database.getInstance().getEntityManager();
		
		try {
			//Iniciando transa��o com o banco de dados
			em.getTransaction().begin();
			
			//se o usu�rio estiver ativo
			if (usuario.isAtivo()){
				//ent�o vamos inativ�-lo
				dao.atualizarCampo(usuario.getClass(), usuario.getId(), "ativo", false);
				
			} else {
				//nesse caso, n�o est� ativo, ent�o vamos reativ�-lo
				dao.atualizarCampo(usuario.getClass(), usuario.getId(), "ativo", true);
			}
			
			//Transa��o confirmada
			em.getTransaction().commit();
			
			dao.desanexarEntidade(usuario); //For�a o recarregamento da entidade, j� que alteramos os dados dela
			
			
		} catch (Exception e){
			e.printStackTrace();
			
			if (em.getTransaction().isActive())
				//Como ocorreu erro, a transa��o n�o ser� confirmada
				em.getTransaction().rollback();
		}
		
		return buscar();
	}
	
	
	public Usuario getUsuarioBusca() {
		return usuarioBusca;
	}

	public void setUsuarioBusca(Usuario usuarioBusca) {
		this.usuarioBusca = usuarioBusca;
	}

	public List<Usuario> getUsuariosEncontrados() {
		return usuariosEncontrados;
	}

	public void setUsuariosEncontrados(List<Usuario> usuariosEncontrados) {
		this.usuariosEncontrados = usuariosEncontrados;
	}

	public Integer getOpcaoOrdenar() {
		return opcaoOrdenar;
	}

	public void setOpcaoOrdenar(Integer opcaoOrdenar) {
		this.opcaoOrdenar = opcaoOrdenar;
	}

	public int getOpcaoOrdenarQualquer() {
		return OPCAO_ORDENAR_QUALQUER;
	}

	public int getOpcaoOrdenarNomeAsc() {
		return OPCAO_ORDENAR_NOME_ASC;
	}

	public int getOpcaoOrdenarNomeDesc() {
		return OPCAO_ORDENAR_NOME_DESC;
	}

}
	
