package lambdaapi.dto;

public class ClientRequestDto {

	String nome;
	String email;
	String cpf;
	boolean criado;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public boolean isCriado() {
		return criado;
	}
	public void setCriado(boolean criado) {
		this.criado = criado;
	}
	
}
