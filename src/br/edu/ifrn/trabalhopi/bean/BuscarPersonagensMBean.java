package br.edu.ifrn.trabalhopi.bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.edu.ifrn.trabalhopi.arquitetura.ControladorGeral;
import br.edu.ifrn.trabalhopi.dao.PersonagemDAO;
import br.edu.ifrn.trabalhopi.dao.UsuarioDAO;
import br.edu.ifrn.trabalhopi.dominio.Personagem;
import br.edu.ifrn.trabalhopi.dominio.Usuario;


@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class BuscarPersonagensMBean extends ControladorGeral {
	
	
		
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
		private Personagem personagemBusca;
		
		/** Armazena os usuários encontrados no banco de acordo com os parâmetros de busca. */
		private List<Personagem> personagensEncontrados;
		
		/** Permite o acesso ao banco. */
		private PersonagemDAO dao;
		
		/** Indica qual opção de ordenamento foi selecionada pelo usuário. */
		private Integer opcaoOrdenar;
		
		/** Inicialização do MBean */
		@PostConstruct
		private void init() {
			personagemBusca = new Personagem();
			personagensEncontrados = new ArrayList<>();
			opcaoOrdenar = null;
			
			dao = new PersonagemDAO();
		}
		
		/** Entra na página de busca de personagens */
		public String entrarBuscarPersonagens(){
			return "/portal/listagemPersonagem.xhtml";
		}
		
		/** Realiza a busca de personagens no banco. */
		public List<Personagem> getBuscar(){
			dao = new PersonagemDAO();
			
			
			personagensEncontrados = dao.findPersonagemGeral(null,null);
			System.out.println(personagensEncontrados.get(0).getNome());
			return personagensEncontrados;
		}
		
		/** Realiza a busca de personagens no banco. */
		public String buscarPersonagens(){
			dao = new PersonagemDAO();
			
			
			personagensEncontrados = dao.findPersonagemGeral(personagemBusca.getNome(),
			personagemBusca.getRaca());
				
			return null;
		}
		
		
		public Personagem getPersonagemBusca() {
			return personagemBusca;
		}

		public void setPersonagemBusca(Personagem personagemBusca) {
			this.personagemBusca = personagemBusca;
		}

		public List<Personagem> getPersonagensEncontrados() {
			return personagensEncontrados;
		}

		public void setPersonagensEncontrados(List<Personagem> personagensEncontrados) {
			this.personagensEncontrados = personagensEncontrados;
		}


	}

