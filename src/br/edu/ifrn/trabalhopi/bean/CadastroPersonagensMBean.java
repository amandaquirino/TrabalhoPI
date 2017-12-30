package br.edu.ifrn.trabalhopi.bean;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import br.edu.ifrn.trabalhopi.arquitetura.ControladorGeral;
import br.edu.ifrn.trabalhopi.dao.DAOGenerico;
import br.edu.ifrn.trabalhopi.dao.Database;
import br.edu.ifrn.trabalhopi.dao.PersonagemDAO;
import br.edu.ifrn.trabalhopi.dominio.Arquivo;
import br.edu.ifrn.trabalhopi.dominio.Personagem;
import br.edu.ifrn.trabalhopi.dominio.Usuario;
import br.edu.ifrn.trabalhopi.dominio.ValidadorUtil;
import br.edu.ifrn.trabalhopi.util.CriptografiaUtils;


	@SuppressWarnings("serial")
	@ManagedBean
	@SessionScoped
	public class CadastroPersonagensMBean extends ControladorGeral {

		/** Indica se � cadastro (true) ou edi��o (false) */
		private boolean cadastro;
		
		/** Armazena os dados do usu�rio que ser� cadastrado */
		private Personagem personagem;
		
		/** Permite o acesso ao banco. */
		private DAOGenerico dao;
		
		/** Tamanho m�ximo para upload, em bytes. */
		private final Integer TAM_MAXIMO_ARQUIVO = 2097152;
		
		/** Formatos permitidos para o envio da foto do usu�rio. */
		private final String[] FORMATOS_PERMITIDOS = {"png", "jpg", "jpeg"};
		
		private void init() {
			personagem = new Personagem();
			
			dao = new PersonagemDAO();

			cadastro = false;
		}
		
		/** Entra na tela de cadastro de usu�rios. */
		public String entrarCadastroPesonagens(){
			init();
			cadastro = true;
			return "/portal/cadastroDePersonagens.xhtml";
		}
		
		/** Entra na tela de edi��o de usu�rios. */
		public String entrarEdicaoPersonagens(Personagem personagem){
			init();
			this.personagem = personagem; //o usu�rio a ser editado ser� o recebido pelo par�metro
			cadastro = false;
			return "/portal/cadastroDePersonagens.xhtml";
		}
		
		/** Cadastra ou edita um usu�rio no banco. */
		public String salvar() throws InstantiationException,
				IllegalAccessException {
			
			DAOGenerico dao = new DAOGenerico();
			boolean erro = false; //Indica se houve erro na valida��o dos dados do usu�rio
			
			//Realizando valida��es
			
			cadastro = personagem.getId() == 0 ? true : false; //Se for cadastro, true; se for edi��o, false
			
			if (cadastro){	
				//Cadastro
				if (ValidadorUtil.isNotEmpty(dao.buscarPelaColuna("nome", personagem.getNome(), Personagem.class))){
					addMsgError("J� existe um personagem com o nome informado.");
					erro = true;
				}
			} else {
				//Edi��o
				if (ValidadorUtil.isNotEmpty(personagem.getNome())){
					
					addMsgError("Informe o nome do personagem.");
					erro = true;
				
				}
			}
			
			//Se houve algum erro no processo de valida��o dos dados, impede o avan�o do cadastro
			if (erro)
				return null;
			
			try {
				boolean validou = validarImagemCadastro();
				
				if (!validou){
					return null;
				}
				
				//Se o usu�rio anexou foto
				if (personagem.getArquivo() != null && ValidadorUtil.isNotEmpty(personagem.getArquivo().getFileName())
						&& personagem.getArquivo().getSize() != -1){
					
					//Cria nova entidade arquivo
					Arquivo arq = new Arquivo();
					arq.setNome(personagem.getArquivo().getFileName());
					arq.setBytes(personagem.getArquivo().getContents());
					
					//Cadastrando a foto no banco
					
					EntityManager em = Database.getInstance().getEntityManager();
					
					try {
						//Iniciando transa��o com o banco de dados
						em.getTransaction().begin();
						
						em.persist(arq);
						
						//Transa��o confirmada
						em.getTransaction().commit();
						
						
					} catch (Exception e){
						e.printStackTrace();
						
						if (em.getTransaction().isActive())
							//Como ocorreu erro, a transa��o n�o ser� confirmada
							em.getTransaction().rollback();
					}
					
					/*Informa o ID da foto do usu�rio, para que possa ser carregada posteriormente
					(como a imagem j� foi cadastrada, ela j� possui ID) */
					personagem.setIdFoto(arq.getId());
				}
				
				//Cadastrando/editando usu�rio
				EntityManager em = Database.getInstance().getEntityManager();
				
				try {
					//Iniciando transa��o com o banco de dados
					em.getTransaction().begin();
					
					if (personagem.getId() == 0){
						em.persist(personagem); //Cadastro
					} else {
						em.merge(personagem); //Edi��o
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
				
				return posCadastro();
				
			} catch (Exception e) {
				tratamentoErroPadrao(e);
				return null;
			} 
		}
		
		/** Verifica se a imagem anexada do usu�rio est� de acordo com o esperado */
		private boolean validarImagemCadastro(){
			if (personagem.getArquivo() != null && ValidadorUtil.isNotEmpty(personagem.getArquivo().getFileName())
					&& personagem.getArquivo().getSize() != -1){
				//Verificando extens�o
				
				String[] nomes = personagem.getArquivo().getFileName().split("\\.");
				String extensao = nomes[nomes.length - 1].toLowerCase();
				
				List<String> formatos = Arrays.asList(FORMATOS_PERMITIDOS);
				
				if (!formatos.contains(extensao)){
					String msg = "O formato de arquivo que voc� anexou n�o � suportado. Por favor, ";
					msg += "envie em um dos seguintes formatos: png ou jpg.";
					
					addMsgError(msg);
					return false;
				} 
				
				//Verificando tamanho arquivo
				if (personagem.getArquivo().getSize() > TAM_MAXIMO_ARQUIVO) {
					addMsgError("O tamanho m�ximo para upload de arquivo � de 2 MB.");
					return false;
				}
			}
			
			return true;
		}
		
		protected String posCadastro() {
			return "/portal/index.xhtml";
		}

		public Personagem getPersonagem() {
			return personagem;
		}

		public void setPersonagem(Personagem personagem) {
			this.personagem = personagem;
		}
		
		
	
}
