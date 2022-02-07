package dominio;

public enum Sentido {
	
	Norte("Norte", 1),
	Sul("Sul", 2),
	Igual("Igual", 3),
	Contrario("Contrario", 4);

	public Integer sentido;

	private Sentido(String nome, Integer sentido) {
		this.sentido = sentido;
	}
}
