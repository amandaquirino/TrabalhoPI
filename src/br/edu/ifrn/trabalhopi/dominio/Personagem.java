package br.edu.ifrn.trabalhopi.dominio;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.primefaces.model.UploadedFile;

import br.edu.ifrn.trabalhopi.util.CriptografiaUtils;
import br.edu.ifrn.trabalhopi.dominio.Usuario;


@Entity
public class Personagem {

	@Id
	@GeneratedValue
	@Column(name="id_personagem", nullable = false)
	private int id;
	
	
	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false)
	private String raca;
	
	@Column(nullable = false)
	private String descricao;
	
	@Column(name="id_foto")
	private Integer idFoto;
	
	 
	/* Atributo não persisitido que armazena uma foto que o usuário deseja
	 para seu perfil. */
	 
	@Transient
	private UploadedFile arquivo;
	
	//Obtém a URL através da qual a foto do usuário pode ser carregada.
	public String getUrlFotoPersonagem(){
		return "/verArquivo?"
				+ "idArquivo=" + getIdFoto() //id do arquivo
				+"&key=" + CriptografiaUtils.criptografarMD5(String.valueOf(getIdFoto())) //chave criptografada para acesso à imagem 
				+ "&salvar=false"; 
	}
	
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getRaca() {
		return raca;
	}

	public void setRaca(String raca) {
		this.raca = raca;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public UploadedFile getArquivo() {
		return arquivo;
	}

	public void setArquivo(UploadedFile arquivo) {
		this.arquivo = arquivo;
	}

	public Integer getIdFoto() {
		return idFoto;
	}

	public void setIdFoto(Integer idFoto) {
		this.idFoto = idFoto;
	}
	
	
}


