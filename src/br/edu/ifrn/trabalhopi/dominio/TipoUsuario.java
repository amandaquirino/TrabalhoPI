package br.edu.ifrn.trabalhopi.dominio;

/**
 * Enum (enumeração) que armazena os possíveis tipos de usuários do sistema.
 * 
 * @author ArthurAmanda
 */
public enum TipoUsuario {
	
	/* ATENÇÃO: ESSES NOMES NÃO PODEM SER MODIFICADOS. */
	
	/** Usuário comum. */
	COMUM,
	
	/** Administrador do sistema */
	ADMINISTRADOR;
	
	/** Obtém uma descrição do tipo de usuário. */
	public String toString() {
		if (this == TipoUsuario.ADMINISTRADOR){
			return "Administrador";
		} else if (this == TipoUsuario.COMUM){
			return "Comum";
		} else {
			return "Não identificado";
		}
	}
	
}
