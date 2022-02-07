package dominio;

import java.time.LocalDateTime;

public class PTP implements Comparable<PTP> {

	int posicao;       // estacao referencia da posição do trem.
	double distancia;  // distancia do trem até a estação em posicao.  Se zero, está na estação.
	int proxima;       // proxima estaçao no caminho do trem.
	LocalDateTime horario;
	
	
	public PTP(int posicao, double distancia, int proxima, LocalDateTime horario) {
		super();
		this.posicao = posicao;
		this.distancia = distancia;
		this.proxima = proxima;
		this.horario = horario;
	}
	

	public double getDistancia() {
		return distancia;
	}


	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}

	public int getProxima() {
		return proxima;
	}

	public void setProxima(int proxima) {
		this.proxima = proxima;
	}

	public int getPosicao() {
		return posicao;
	}
	
	public LocalDateTime getHorario() {
		return horario;
	}
	public void sethorario(LocalDateTime horario) {
		this.horario = horario;
	}
	
//	public void setPTP(Trem t,int posicao, int distancia, int proxima, LocalDateTime horario) {
//		PTP p = new PTP(posicao,distancia,proxima,horario,"");
//		t.setPTP(p);
//	}
//	
	@Override
	public String toString() {
		return  posicao + "   " + horario + " ";
	}
	
	//@Override
	public int compareTo(PTP that) {
		return this.getHorario().compareTo(that.getHorario());
	}
	 
}
