package br.edu.ifrn.trabalhopi.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

/**
 * Esta classe deve ser implementada por todos os DAOs do sistema.
 * Ela contém métodos muito úteis para acesso ao banco de dados,
 * incluindo métodos prontos para acesso a informações de quaisquer
 * tabelas.
 *  
 * @author ArthurAmanda
 */
public class DAOGenerico {
	
	/** Obtém o EntityManager para poder fazer operações no banco. */
	public EntityManager getEntityManager() {
		return Database.getInstance().getEntityManager();
	}
	
	/** 
	 * Este método pode ser chamado quando for preciso atualizar (update) um registro
	 * no banco de dados. Funciona para qualquer entidade.
	 * */
	public void update(Object c){
		getEntityManager().merge(c);
	}
	
	/** Obtém qualquer entidade através do ID dela. */
	public <T> T encontrarPeloID(int id, Class<T> classe){
		EntityManager em = getEntityManager();
		T c = em.find(classe, id);
		return c;
	}
	
	/** Obtém todos os registros de uma tabela no banco de dados. Basta informar o .class da entidade. */
	public <T> List<T> buscarTodos(Class<T> classe){
		EntityManager em = getEntityManager();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(classe);
		TypedQuery<T> typedQuery = em.createQuery(query.select(query.from(classe)));
		List<T> c = typedQuery.getResultList();
		return c;
	}
	
	/** Retorna todos os registros de uma entidade que possuam um valor exato de coluna. */
	@SuppressWarnings("unchecked")
	public <T> List<T> buscarPelaColuna(String coluna, Object valor, Class<T> classe) {
		String tabela = classe.getSimpleName();
		String jpql = "from "+tabela+ " where "+coluna+" = :valor";
		EntityManager em = getEntityManager();
		Query q = em.createQuery(jpql);
		q.setParameter("valor", valor);
		List<T> retorno = q.getResultList();
		return retorno;
	}
	
	/** Retorna todos os registros de uma entidade que possuam um valor exato de coluna. */
	@SuppressWarnings("unchecked")
	public <T> Object buscarPelaColunaUnico(String coluna, Object valor, Class<T> classe) {
		String tabela = classe.getSimpleName();
		String jpql = "from "+tabela+ " where "+coluna+" = :valor";
		EntityManager em = getEntityManager();
		Query q = em.createQuery(jpql);
		q.setParameter("valor", valor);
		Object retorno = q.getSingleResult();
		return retorno;
	}
	
	/** Atualiza uma coluna de uma tabela de qualquer entidade. */
	public <T> void atualizarCampo(Class<T> classe, int id, String coluna, Object valor) {
		String tabela = classe.getSimpleName();
		String jpql = "update "+tabela+ " set " + coluna + " = :valor where id = :id ";
		EntityManager em = getEntityManager();
		Query q = em.createQuery(jpql);
		q.setParameter("valor", valor);
		q.setParameter("id", id);
		q.executeUpdate();
	}

	/** 
	 * Limpa o cache do Hibernate. Força o recarregamento de todas as entidades, quando necessário.
	 * Todas as alterações não salvas serão perdidas. */
	public void limparCache() {
		getEntityManager().clear();	
	}
	
	/** Limpa uma determinada entidade da memória do Hibernate. */
	public void desanexarEntidade(Object p) {
		getEntityManager().detach(p);
	}
	
	/** Atualiza as informações de um determinado registro (objeto) de entidade.
	 * Sincroniza com o banco. */
	public void atualizarEntidade(Object p) {
		getEntityManager().refresh(p);
	}
	
	/** Realiza um update através de um SQL simples. */
	public void update(String sql) {
		Session session = (Session) Database.getInstance().getEntityManager().getDelegate();
		SQLQuery q = session.createSQLQuery(sql);
		q.executeUpdate();
	}

}