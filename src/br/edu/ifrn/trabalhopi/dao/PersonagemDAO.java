package br.edu.ifrn.trabalhopi.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.edu.ifrn.trabalhopi.dominio.Personagem;
import br.edu.ifrn.trabalhopi.dominio.Usuario;
import br.edu.ifrn.trabalhopi.dominio.ValidadorUtil;



public class PersonagemDAO extends DAOGenerico {
	/**
	 * Método que lista todos os usuarios
	 */
	public List<Personagem> findAll(){
		EntityManager em = getEntityManager();
		String hql = "SELECT p  FROM Personagem p";
		Query q = em.createQuery(hql);
		List<Personagem> result = (List<Personagem>) q.getResultList();
		return result;
	}
	
	/**
	 * Método que permite listar usuários através da busca por
	 * diversos atributos.
	 */
	public List<Personagem> findPersonagemGeral(String nome, String raca){
		EntityManager em = getEntityManager();
		
		String hql = "SELECT p ";
		hql += " FROM Personagem p WHERE 1=1";
		
		if (ValidadorUtil.isNotEmpty(nome)){
			hql += " AND upper(p.nome) like :nome ";
		}
		if (ValidadorUtil.isNotEmpty(raca)){
			hql += " AND upper(p.raca) like :raca ";
		}
		
		Query q = em.createQuery(hql);
		
		if (ValidadorUtil.isNotEmpty(nome)){
			q.setParameter("nome", nome);
		}
		if (ValidadorUtil.isNotEmpty(raca)){
			q.setParameter("raca", "%" + raca + "%");
		}
		
		try {
			@SuppressWarnings("unchecked")
			List<Personagem> result = q.getResultList();
			
			return result;
		} catch (NoResultException e){
			return null;
		}
	}
	
}
