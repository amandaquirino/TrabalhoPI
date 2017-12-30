package br.edu.ifrn.trabalhopi.dominio;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;





@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class Usuario {
	
	@Id
    @GeneratedValue  
	@Column(name="id_usuario", nullable = false)
	private int id;
	
	//Personagem associada ao usuário do sistema.
	@ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name = "id_personagem", referencedColumnName = "id_personagem", nullable=false, updatable=true)
	private Personagem personagem;
	
	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false)
	private String senha;
	
	@Column(nullable = false)
	private String apelido;
	
	/** Tipo do usuário. */
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private TipoUsuario tipoUsuario;
	
	@Column(nullable = false)
	private int idade;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private int telefone;
	
	/** Utilizado para remoção lógica. */
	@Column(nullable = false)
	private boolean ativo = true;
	
	/* Personagem associado ao usuário do sistema. 
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.PERSIST)
	@JoinColumn(name = "id_personagem", referencedColumnName = "id_personagem", nullable=false)
	private Personagem personagem;
	*/
	
	/** Confirmação da nova senha. */
	@Transient
	private String novaSenhaConfirmacao;
	

	/** Cria um código que identifica unicamente um usuário. Serve para descobrir
	 * se um usuário é igual a outro ou não. */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/** Identifica se um usuário é igual a outro ou não. */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TipoUsuario getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(TipoUsuario tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	/*public Personagem getPersonagem() {
		return personagem;
	}

	public void setPersonagem(Personagem personagem) {
		this.personagem = personagem;
	}*/

	public String getNovaSenhaConfirmacao() {
		return novaSenhaConfirmacao;
	}

	public void setNovaSenhaConfirmacao(String novaSenhaConfirmacao) {
		this.novaSenhaConfirmacao = novaSenhaConfirmacao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getApelido() {
		return apelido;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}

	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getTelefone() {
		return telefone;
	}

	public void setTelefone(int telefone) {
		this.telefone = telefone;
	}
	
	public boolean isAdministrador(){
		return tipoUsuario != null && tipoUsuario == TipoUsuario.ADMINISTRADOR;
	}
	
	public boolean isUsuarioComum(){
		return tipoUsuario != null && tipoUsuario == TipoUsuario.COMUM;
	}
	
	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Personagem getPersonagem() {
		if(personagem == null){
			personagem = new Personagem();
		}
		return personagem;
	}

	public void setPersonagem(Personagem personagem) {
		this.personagem = personagem;
	}
	
/*
	@Override
	public String toString() {
		return "Pessoa [nome=" + nome + ", senha=" + senha + ", apelido=" + apelido + ", idade=" + idade + ", email="
				+ email + ", telefone=" + telefone + ", personagem=" + personagem + "]"; 
	} */

	
	
	
	
	
	
}
