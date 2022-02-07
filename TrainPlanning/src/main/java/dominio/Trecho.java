package dominio;

import java.time.LocalDateTime;


public class Trecho {
	
	int e1;
	int e2;
	LocalDateTime hi;
	LocalDateTime hf;
	Trem t;
	
	public Trecho(int e1, int e2,LocalDateTime hi,LocalDateTime hf, Trem t) {
		this.e1 = e1;
		this.e2 = e2;
		this.hi = hi;
		this.hf = hf;
		this.t = t;
	}

	public LocalDateTime getHi() {
		return hi;
	}

	public LocalDateTime getHf() {
		return hf;
	}

	public int getE1() {
		return e1;
	}

	public int getE2() {
		return e2;
	}

	public Trem getT() {
		return t;
	}
}
