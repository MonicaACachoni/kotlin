package dominio;

import java.time.LocalDateTime;

public class OcupacaoDoLoop {
	
	public OcupacaoDoLoop() {
		super();
	}

	Integer quantidadeDeTrensNoLoop=0;
	LocalDateTime horario;


	public Integer getOcupacaoDoLoop() {
		return quantidadeDeTrensNoLoop;
	}

	public void setOcupacaoDoLoop(Integer ocupacaoDoLoop) {
		this.quantidadeDeTrensNoLoop = ocupacaoDoLoop;
	}

	public void somarTremEmLoop() {
		this.quantidadeDeTrensNoLoop = quantidadeDeTrensNoLoop + 1;
	}

	public void subtrairTremEmLoop() {
		this.quantidadeDeTrensNoLoop = quantidadeDeTrensNoLoop-1;
	}
}
