package br.edu.ifrn.trabalhopi.bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import br.edu.ifrn.trabalhopi.arquitetura.ControladorGeral;
import br.edu.ifrn.trabalhopi.dao.DAOGenerico;
import br.edu.ifrn.trabalhopi.dao.Database;
import br.edu.ifrn.trabalhopi.dao.PersonagemDAO;
import br.edu.ifrn.trabalhopi.dao.UsuarioDAO;
import br.edu.ifrn.trabalhopi.dominio.Personagem;
import br.edu.ifrn.trabalhopi.dominio.Usuario;
import br.edu.ifrn.trabalhopi.dominio.ValidadorUtil;
import br.edu.ifrn.trabalhopi.util.CriptografiaUtils;





/** 
 * MBean que controla opera��es relacionadas � cria��o/edi��o de usu�rios.<br/>
 * 
 * @author ArthurAmanda
 */
@ManagedBean
@SessionScoped
public class CadastroUsuariosMBean extends ControladorGeral {
	
	/** Indica se � cadastro (true) ou edi��o (false) */
	private boolean cadastro;
	
	/** Armazena os dados do usu�rio que ser� cadastrado */
	private Usuario usuario;
	
	private Personagem personagemBusca;
	
	
	
	/** Permite o acesso ao banco. */
	private DAOGenerico dao;
	
	private List<Personagem> fotosPersonagens;
	
	@PostConstruct
	public void init() {
		usuario = new Usuario();
		fotosPersonagens = new ArrayList<>();
		personagemBusca = new Personagem();
		
		dao = new UsuarioDAO();
		

		cadastro = false;
	}
	
	public Usuario buscar(){
		usuario = (Usuario) dao.buscarPelaColunaUnico("email", usuario.getEmail(), Usuario.class);
		return usuario;
	}
	
	/** Realiza a busca de personagens no banco. */
	public List<Personagem> buscarFotosPersonagens(){
		dao = new PersonagemDAO();
		fotosPersonagens = ((PersonagemDAO) dao).findAll();
			
		return fotosPersonagens;
	}
	
	
	/** Entra na tela de cadastro de usu�rios. */
	public String entrarCadastroUsuarios(){
		init();
		return "/portal/telaDeEdicaoOuCadastroPeloAdministrador";
	}
	
	/** Entra na tela de cadastro de usu�rios. */
	public String cadastroPeloUsuario(){
		init();
		return "/publico/telaDeEdicaoOuCadastroPeloUsuario.xhtml";
	}
	
	
	/** Entra na tela de edi��o de usu�rios. */
	public String entrarEdicaoUsuarios(Usuario usuario){
		init();
		this.usuario = usuario; //o usu�rio a ser editado ser� o recebido pelo par�metro
		cadastro = false;
		return "/portal/telaDeEdicaoOuCadastroPeloAdministrador.xhtml";
	}
	
	public String edicaoPeloUsuario(Usuario usuario){
		init();
		this.usuario = usuario; //o usu�rio a ser editado ser� o recebido pelo par�metro
		cadastro = false;
		return "/portal/telaDeEdicaoOuCadastroPeloUsuario.xhtml";
	}
	
	
	//Cadastrando/editando usu�rio
	/** Cadastra ou edita um usu�rio no banco. */
	public String salvar() throws InstantiationException,
			IllegalAccessException {
		System.out.println("chamou1");	
		DAOGenerico dao = new DAOGenerico();
		boolean erro = false; //Indica se houve erro na valida��o dos dados do usu�rio
		
		//Realizando valida��es
		
				cadastro = usuario.getId() == 0 ? true : false; //Se for cadastro, true; se for edi��o, false
				
				if (cadastro){	
					System.out.println("chamou2");	
					//Cadastro
					if (ValidadorUtil.isNotEmpty(dao.buscarPelaColuna("email", usuario.getEmail(), Usuario.class))){
						addMsgError("J� existe um usu�rio com o email informado.");
						erro = true;
					}
					if (!usuario.getSenha().equals(usuario.getNovaSenhaConfirmacao())){
						addMsgError("A senha informada n�o confere com sua confirma��o.");
						erro = true;
					}
				} else {
					System.out.println("chamou3");	
					//Edi��o
					if (ValidadorUtil.isNotEmpty(usuario.getSenha()) && ValidadorUtil.isEmpty(usuario.getNovaSenhaConfirmacao())
							|| (ValidadorUtil.isEmpty(usuario.getSenha()) && ValidadorUtil.isNotEmpty(usuario.getNovaSenhaConfirmacao()))){
						
						addMsgError("Informe a senha e sua confirma��o.");
						erro = true;
					
					} else if (ValidadorUtil.isNotEmpty(usuario.getSenha()) && ValidadorUtil.isNotEmpty(usuario.getNovaSenhaConfirmacao()) 
							&& !usuario.getSenha().equals(usuario.getNovaSenhaConfirmacao())){
						
						addMsgError("A senha informada n�o confere com sua confirma��o.");
						erro = true;
					}
				}
				if (!ValidadorUtil.validateEmail(usuario.getEmail())){
					addMsgError("O email informado � inv�lido.");
					erro = true;
				}
				System.out.println("chamou4");	
				
				//Se houve algum erro no processo de valida��o dos dados, impede o avan�o do cadastro
				if (erro)
					return null;
				
				if (!cadastro && ValidadorUtil.isEmpty(usuario.getSenha())){
					//Se for edi��o, s� deve modificar a senha caso o usu�rio tenha digitado alguma coisa
					//no campo de senha, ou seja, caso a senha esteja vazia, ela n�o deve ser modificada (deve
					//permanecer a mesma do banco).
					
					dao.desanexarEntidade(usuario); //retira o usu�rio da mem�ria do hibernate para evitar erros
					
					//Busca novamente o usu�rio no banco
					Usuario usuarioBanco = dao.encontrarPeloID(usuario.getId(), Usuario.class);
					//Como ele n�o digitou nada na senha, ela deve permanecer a mesma do banco
					usuario.setSenha(usuarioBanco.getSenha()); //A senha do banco j� est� criptografada 
					
				} else {
					//Nos demais casos (cadastro ou edi��o com mudan�a de senha), a senha n�o est� criptografada,
					//ent�o devemos criptograf�-la
					usuario.setSenha(CriptografiaUtils.criptografarMD5(usuario.getSenha()));
				}
				System.out.println("chamou5");				
	EntityManager em = Database.getInstance().getEntityManager();
	usuario.setPersonagem(em.find(Personagem.class, usuario.getPersonagem().getId()));
	System.out.println(usuario.getPersonagem().getId());
	
	try {
	
		//Iniciando transa��o com o banco de dados
		em.getTransaction().begin();
		
		if (usuario.getId() == 0){
			em.persist(usuario); //Cadastro
		} else {
			em.merge(usuario); //Edi��o
		}
		
		//Transa��o confirmada
		em.getTransaction().commit();
		
		
	} catch (Exception e){
		e.printStackTrace();
		
		if (em.getTransaction().isActive())
			//Como ocorreu erro, a transa��o n�o ser� confirmada
			em.getTransaction().rollback();
	}
	
	addMsgInfo((cadastro ? "Cadastro realizado" : "Altera��o realizada") + " com sucesso!");
	
	dao.desanexarEntidade(getUsuarioLogado());
	Usuario usuarioLogado = (Usuario) dao.buscarPelaColunaUnico("id", getUsuarioLogado().getId(), Usuario.class);
	getCurrentSession().setAttribute("usuarioLogado", usuarioLogado);
	
	return posCadastro();
	}
	
	
	protected String posCadastro() {
		return "/portal/index.xhtml";
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Personagem getPersonagemBusca() {
		return personagemBusca;
	}

	public void setPersonagemBusca(Personagem personagemBusca) {
		this.personagemBusca = personagemBusca;
	}

	public List<Personagem> getFotosPersonagens() {
		return fotosPersonagens;
	}

	public void setFotosPersonagens(List<Personagem> fotosPersonagens) {
		this.fotosPersonagens = fotosPersonagens;
	}
	
	

}
	
