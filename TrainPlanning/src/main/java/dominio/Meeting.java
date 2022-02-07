package dominio;



public class Meeting {

	Trem t1;
	Trem t2;
	int estacao;
	int duracao;

	public Meeting(Trem t1, Trem t2, int estacao, int duracao) {
		super();
		this.t1 = t1;
		this.t2 = t2;
		this.estacao = estacao;
		this.duracao = duracao;
	}

	public int getDuracao() {
		return duracao;
	}

	public void setDuracao(int duracao) {
		this.duracao = duracao;
	}

	public Trem getT1() {
		return t1;
	}

	public void setT1(Trem t1) {
		this.t1 = t1;
	}

	public Trem getT2() {
		return t2;
	}

	public void setT2(Trem t2) {
		this.t2 = t2;
	}

	public int getEstacao() {
		return estacao;
	}

	public void setEstacao(int estacao) {
		this.estacao = estacao;
	}
}
