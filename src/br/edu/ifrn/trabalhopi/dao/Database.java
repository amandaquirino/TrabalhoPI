package br.edu.ifrn.trabalhopi.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;

/**
 * Classe que controla a comunicação com o banco de dados, 
 * inclusive o gerenciamento de entidades (EntityManager).
 * Esta classe é um singleton, ou seja, só pode existir
 * uma ocorrência (objeto) dela em todo o sistema.
 * 
 * @author ArthurAmanda
 */
public class Database {

	private static Database singleton = new Database();
	private static EntityManager em;

	private Database() {
		criarEM();
	}
	
	private void criarEM(){
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("Trabalho3Bi"); //nome que está no arquivo persistence.xml
		em = emf.createEntityManager();
	}

	public static Database getInstance() {
		return singleton;
	}

	public EntityManager getEntityManager() {
		if (!em.isOpen()){
			criarEM();
		}
		
		return em;
	}
	
	public Session getSession(){
		return (Session) em.getDelegate();
	}

}

