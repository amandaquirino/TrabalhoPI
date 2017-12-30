package br.edu.ifrn.trabalhopi.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.edu.ifrn.trabalhopi.dominio.Usuario;
import br.edu.ifrn.trabalhopi.dominio.ValidadorUtil;

/**
 * DAO (Data Access Object - Objeto de Acesso a Dados) com 
 * métodos relativos à entidade {@link Usuario}.
 * @author ArthurAmanda
 */
public class UsuarioDAO extends DAOGenerico {

	/** Encontra um usuário a partir de seu login e senha. */
	public Usuario findUsuarioByLoginSenha(String login, String senha){
		EntityManager em = getEntityManager();
		
		String hql = "SELECT usuario ";
		hql += " FROM Usuario usuario WHERE usuario.email = :login and usuario.senha = :senha ";
		
		Query q = em.createQuery(hql);
		q.setParameter("login", login);
		q.setParameter("senha", senha);
		
		try {
			Usuario usuario = (Usuario) q.getSingleResult();
			return usuario;
		} catch (NoResultException e){
			return null;
		}
	}
	
	/**
	 * Método que permite listar usuários através da busca por
	 * diversos atributos.
	 */
	public List<Usuario> findUsuarioGeral(String email, String nome, String apelido, 
			String ordenarPor){
		EntityManager em = getEntityManager();
		
		String hql = "SELECT u ";
		hql += " FROM Usuario u WHERE 1=1";
		
		if (ValidadorUtil.isNotEmpty(email)){
			hql += " AND u.email = :email ";
		}
		if (ValidadorUtil.isNotEmpty(nome)){
			hql += " AND upper(u.nome) like :nome ";
		}
		if (ValidadorUtil.isNotEmpty(apelido)){
			hql += " AND upper(u.apelido) like :apelido ";
		}
		if (ValidadorUtil.isNotEmpty(ordenarPor)){
			hql += " ORDER BY :ordenarPor ";
		}
		
		Query q = em.createQuery(hql);
		
		if (ValidadorUtil.isNotEmpty(email)){
			q.setParameter("email", email);
		}
		if (ValidadorUtil.isNotEmpty(nome)){
			q.setParameter("nome", "%" + nome.toUpperCase() + "%");
		}
		if (ValidadorUtil.isNotEmpty(apelido)){
			q.setParameter("apelido", "%" + apelido.toUpperCase() + "%");
		}
		if (ValidadorUtil.isNotEmpty(ordenarPor)){
			q.setParameter("ordenarPor", ordenarPor);
		}
		
		try {
			@SuppressWarnings("unchecked")
			List<Usuario> result = q.getResultList();
			
			return result;
		} catch (NoResultException e){
			return null;
		}
	}
	
}
