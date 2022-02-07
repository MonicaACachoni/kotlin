package dominio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Estacao implements Comparable<Estacao>{
	
	//name
	private String nomeLoop = "";
	// Positions in the railway
	private BigDecimal loopPosition;
	private BigDecimal custoDoLoop;
	Integer capacidadeLoop;
	ArrayList <OcupacaoDoLoop> ocuparLoop = new ArrayList<OcupacaoDoLoop>();
	ArrayList<Solver>  solver = new ArrayList<Solver>();
	
	
	public static boolean temCapacidadeLivre(Estacao ocupar, LocalDateTime horario){
		
		if (!ocupar.ocuparLoop.isEmpty()&&
				ocupar.capacidadeLoop>0) {
			for (OcupacaoDoLoop c : ocupar.ocuparLoop) {
				
						if ((ocupar.getCapacidadeLoop())>=(c.quantidadeDeTrensNoLoop)&&
								(horario.compareTo(c.horario)==0)
								)
						{
						//	System.out.println("Trens no loop = "+ c.quantidadeDeTrensNoLoop);
							OcupacaoDoLoop adicionarOcupacao = new OcupacaoDoLoop();
							adicionarOcupacao.horario = horario;
							
							//se ainda tem capacidade no loop, adiciona o trem ao loop
							adicionarOcupacao.somarTremEmLoop();
														
							ocupar.ocuparLoop.add(adicionarOcupacao);
							return true;
						}
						else {
						//	System.out.println("Trens no loop = "+ c.quantidadeDeTrensNoLoop);
							// se  nao tem mais capacidade no loop responde que não cabe mais um
							return false;
						}
				}
		}
		else
		{
			OcupacaoDoLoop adicionarOcupacao = new OcupacaoDoLoop();
			adicionarOcupacao.horario = horario;
			//se ainda tem capacidade no loop, adiciona o trem ao loop
			adicionarOcupacao.somarTremEmLoop();
										
			ocupar.ocuparLoop.add(adicionarOcupacao);
			return true;
		}
		
		return false;
	}
	
	public Integer getCapacidadeLoop() {
		return capacidadeLoop;
	}

	public void setCapacidadeLoop(Integer capacidadeLoop) {
		this.capacidadeLoop = capacidadeLoop;
	}

	public BigDecimal getCustoDoLoop() {
		return custoDoLoop;
	}

	public void setCustoDoLoop(BigDecimal custoDoLoop) {
		this.custoDoLoop = custoDoLoop;
	}

	public static final BigDecimal CUSTO_POR_PARAR_O_TREM_NO_LOOP = BigDecimal.valueOf(5l);
	
	public Estacao(String nomeLoop, BigDecimal posicaoK, Integer capacidade) {
	//construtor
		this.nomeLoop=nomeLoop;
		this.loopPosition=posicaoK;
		this.custoDoLoop=CUSTO_POR_PARAR_O_TREM_NO_LOOP.multiply(loopPosition);
		this.capacidadeLoop = capacidade;
		
	}

	public String getNomeLoop() {
		return nomeLoop;
	}

	public void setNomeLoop(String nomeLoop) {
		this.nomeLoop = nomeLoop;
	}

	public BigDecimal getLoopPosition() {
		return loopPosition;
	}

	
	public void setLoopPosition(BigDecimal loopPosition) {
		this.loopPosition = loopPosition;
	}

	//@Override
	public int compareTo(Estacao that) {		
		return this.loopPosition.compareTo(that.loopPosition);
	}

	//@Override
	public String toString() {
		return "Loop [nomeLoop=" + nomeLoop + ", loopPosition=" + loopPosition + "]";
	}

	public List<Solver> getSolver() {
		return solver;
	}

	public void setSolver(ArrayList<Solver> solver) {
		this.solver = solver;
	}
	public void setSolver(int i, Solver solver) {
		this.solver.add(solver);
	}
	
	
	

}
