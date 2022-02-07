package dominio;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.Vector;


public class AreaProibida {
	
	private int estacaoi; 
	private int estacaof; 
		
	LocalDateTime tempoi;
	LocalDateTime tempof;
		
		
	public AreaProibida(int ei, int ef, LocalDateTime ti, LocalDateTime tf) {
		this.estacaoi = ei;
		this.estacaof = ef;
		this.tempoi = ti;
		this.tempof = tf;
	}

	public int getEstacaoI() {
		return estacaoi;
	}

	public void setEstacaoI(int v) {
		this.estacaoi = v;
	}

	public int getEstacaoF() {
		return estacaof;
	}

	public void setEstacaoF(int v) {
		this.estacaof = v;
	}

	public LocalDateTime getTempoI() {
		return tempoi;
	}

	public void setTempoI(LocalDateTime t) {
		this.tempoi = t;
	}

	public LocalDateTime getTempoF() {
		return tempof;
	}

	public void setTempoF(LocalDateTime t) {
		this.tempof = t;
	}
	
	

	
	public static Vector<AreaProibida> loadFromFile(String arquivo) throws FileNotFoundException {

		Vector<AreaProibida> areas = new Vector<AreaProibida>();

		/*
		 * Formato do arquivo:
		 * estação-A estação-B hora-início-localDateTime hora-térmico-localDateTime
		 */
		
		Scanner s = new Scanner(new BufferedReader(new FileReader(arquivo)));

		while (s.hasNext()) {
			int e1 = s.nextInt();
			int e2 = s.nextInt();
			LocalDateTime h1 = LocalDateTime.parse(s.next());
			LocalDateTime h2 = LocalDateTime.parse(s.next());

			areas.add(new AreaProibida(e1, e2, h1, h2));
		}

		s.close();
		
		return(areas);
	}
	
}
