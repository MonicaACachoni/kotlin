package Malha;

public enum StatusTrens {
	
	ColisaoFrontal("Mesmo Trecho, Direcões Contrarias, Colisão Frontal", 1),
	Afastamento("Mesmo Trecho, Direcões Contrarias, Afastamento", 2),
	T1atropelaT2("Mesmo Trecho, Mesma Direcao, T1 atropela T2",3),
	T1andaAtrasDeT2("Mesmo Trecho, Mesma Direcao, T1 anda atras de T2",4),
	T2atropelaT1("Mesmo Trecho, Mesma Direcao, T2 atropela T1",5),
	T2andaAtrasDeT1("Mesmo Trecho, Mesma Direcao, T2 anda atras de T1",6),
	EstacoesAdjacentes("Estacoes Adjacentes", 7),
	MesmaEstacao("Mesma Estacao", 8),
	Outros("Outra configuracao", 9); 

	public int status;
	
	private StatusTrens(String nome, Integer status) {
		this.status = status;
	}
	
}
