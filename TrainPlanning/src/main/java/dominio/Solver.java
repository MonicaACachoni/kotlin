package dominio;

public class Solver {

	private int qtosTrensChegam;
	private int sentido;
	
	@Override
	public String toString() {
		return "Solver [qtosTrensChegam=" + qtosTrensChegam + ", sentido=" + sentido + "]";
	}
	public int getQtosTrensChegam() {
		return qtosTrensChegam;
	}
	public void setQtosTrensChegam(int qtosTrensChegam) {
		this.qtosTrensChegam = qtosTrensChegam;
	}
	
	public int getSentido() {
		return sentido;
	}
	public void setSentido(int sentido) {
		this.sentido = sentido;
	}
	
}
