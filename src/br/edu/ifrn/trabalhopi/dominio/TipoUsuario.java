package br.edu.ifrn.trabalhopi.dominio;

/**
 * Enum (enumera��o) que armazena os poss�veis tipos de usu�rios do sistema.
 * 
 * @author ArthurAmanda
 */
public enum TipoUsuario {
	
	/* ATEN��O: ESSES NOMES N�O PODEM SER MODIFICADOS. */
	
	/** Usu�rio comum. */
	COMUM,
	
	/** Administrador do sistema */
	ADMINISTRADOR;
	
	/** Obt�m uma descri��o do tipo de usu�rio. */
	public String toString() {
		if (this == TipoUsuario.ADMINISTRADOR){
			return "Administrador";
		} else if (this == TipoUsuario.COMUM){
			return "Comum";
		} else {
			return "N�o identificado";
		}
	}
	
}
