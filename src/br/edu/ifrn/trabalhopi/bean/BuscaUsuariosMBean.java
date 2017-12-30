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
 * MBean que controla operações relacionadas à busca de usuários.
 * 
 * @author ArthurAmanda
 */
@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class BuscaUsuariosMBean extends ControladorGeral {
	
	/** Opção que será exibida na busca de usuários para ordenar a busca aleatoriamente */
	private static final int OPCAO_ORDENAR_QUALQUER = 0;
	
	/** 
	 * Opção que será exibida na busca de usuários para ordenar a busca pelo nome,
	 * em ordem crescente. 
	 * */
	private static final int OPCAO_ORDENAR_NOME_ASC = 1;
	
	/** 
	 * Opção que será exibida na busca de usuários para ordenar a busca pelo nome,
	 * em ordem decrescente. 
	 * */
	private static final int OPCAO_ORDENAR_NOME_DESC = 2;

	/** Armazena as informações preenchidas na página de busca de usuários. */
	private Usuario usuarioBusca;
	
	/** Armazena os usuários encontrados no banco de acordo com os parâmetros de busca. */
	private List<Usuario> usuariosEncontrados;
	
	/** Permite o acesso ao banco. */
	private UsuarioDAO dao;
	
	/** Indica qual opção de ordenamento foi selecionada pelo usuário. */
	private Integer opcaoOrdenar;
	
	/** Inicialização do MBean */
	@PostConstruct
	private void init() {
		usuarioBusca = new Usuario();
		usuariosEncontrados = new ArrayList<>();
		opcaoOrdenar = null;
		
		dao = new UsuarioDAO();
	}
	
	/** Entra na página de busca de usuários */
	public String entrarBuscarUsuarios(){
		return "/portal/listagemUsuario.xhtml";
	}
	
	/** Realiza a busca de usuários no banco. */
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
	 * Inativa um usuário do banco de dados. Não o remove, apenas inativa, porém
	 * tem o mesmo efeito, já que ele não pode mais fazer login. É útil para quando
	 * o administrador não quer perder as informações do registro, por diversos motivos.
	 *  
	 * */
	public String removerUsuario(Usuario usuario) throws Exception{
		
		EntityManager em = Database.getInstance().getEntityManager();
		
		try {
			//Iniciando transação com o banco de dados
			em.getTransaction().begin();
			
			//se o usuário estiver ativo
			if (usuario.isAtivo()){
				//então vamos inativá-lo
				dao.atualizarCampo(usuario.getClass(), usuario.getId(), "ativo", false);
				
			} else {
				//nesse caso, não está ativo, então vamos reativá-lo
				dao.atualizarCampo(usuario.getClass(), usuario.getId(), "ativo", true);
			}
			
			//Transação confirmada
			em.getTransaction().commit();
			
			dao.desanexarEntidade(usuario); //Força o recarregamento da entidade, já que alteramos os dados dela
			
			
		} catch (Exception e){
			e.printStackTrace();
			
			if (em.getTransaction().isActive())
				//Como ocorreu erro, a transação não será confirmada
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
	
