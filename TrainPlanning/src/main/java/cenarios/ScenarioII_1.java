package cenarios;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Vector;

import Malha.Malha;
import dominio.Custos;
import dominio.PTP;
import dominio.Trem;

// Neste cenário há dois trens na estação k indo para k+1 tais que t1 sai antes de t2 mas t2 atropela t1.

// A menos que a diferença de tempo seja muito pequena ou que o custo de manter t1 na estação k 
// seja altíssimo, t1 deve deixar t2 passar.  

// A proposta no artigo (subrotina 4.1) é só deixar t2 passar.


public class ScenarioII_1 {


	public static boolean realizar(Malha malha, Vector<Trem> trens) {

		for (int i = 0; i < trens.size(); i++) {

			Trem t1 = trens.get(i);
			PTP ptp1 = t1.getPTP();

			if (ptp1.getProxima() == -1)
				continue;

			for (int j = i+1; j < trens.size(); j++) {

				if (i == j) 
					continue;

				Trem t2 = trens.get(j);				
				PTP ptp2 = t2.getPTP();

				if (ptp2.getProxima() == -1)
					continue;

				if (ptp1.getPosicao() == ptp2.getPosicao() &&
					ptp1.getProxima() == ptp2.getProxima()) {

					LocalDateTime hcorr1 = ptp1.getHorario();
					LocalDateTime hprox1 = hcorr1.plusMinutes(t1.tempoAteEstacao(ptp1.getProxima(), malha));

					LocalDateTime hcorr2 = ptp2.getHorario();
					LocalDateTime hprox2 = hcorr2.plusMinutes(t2.tempoAteEstacao(ptp2.getProxima(), malha));
					
					// Se t2 sai junto com ou depois de t1 mas chegaria antes, t1 fica parado para t2 passar:
					int custo1 = calculaCusto(t1, t2, malha);
					int custo2 = calculaCusto(t2, t1, malha);
					if (hcorr2.compareTo(hcorr1) >= 0 && hprox2.compareTo(hprox1) < 0 && custo1<custo2) {
												
						int posicao = ptp1.getPosicao();
						// Testa se o trem está na estação de origem ou se o último PTP foi ficar parado:
						if ((t1.getPercursoDoTrem().get(0).getPosicao() == posicao) ||
							(t1.getPercursoDoTrem().size() > 1 && t1.getPrevPTP().getPosicao() == posicao)) {
							;
						}
						else {
							malha.entrarEstacao(t1,posicao);	
						}
						
						System.out.println("\n> [II.1] Ultrapassagem");
						System.out.printf("->%s\n",t1.getNome());
						System.out.printf("->%s\n",t2.getNome());
						System.out.printf("%s parado em %2d até %s\n",t1.getNome(),posicao,hprox2);
						
						t1.addPTP(new PTP(posicao, 0, ptp1.getProxima(), hprox2));
						return true;
					}
					
					// Se t1 sai junto com ou depois de t2 mas chegaria antes, t2 fica parado para t1 passar:
					else if (hcorr1.compareTo(hcorr2) >= 0 && hprox1.compareTo(hprox2) < 0) {
						
						int posicao = ptp2.getPosicao();
						// Testa se o trem está na estação de origem ou se o último PTP foi ficar parado:
						if ((t2.getPercursoDoTrem().get(0).getPosicao() == posicao) ||
							(t2.getPercursoDoTrem().size() > 1 && t2.getPrevPTP().getPosicao() == posicao)) {
								;
						}
						else {
							malha.entrarEstacao(t2,posicao);	
						}

						System.out.println("\n> [II.1] Ultrapassagem");
						System.out.printf("->%s\n",t1.getNome());
						System.out.printf("->%s\n",t2.getNome());
						System.out.printf("%s parado em %2d até %s\n",t2.getNome(),posicao,hprox1);

						t2.addPTP(new PTP(posicao, 0, ptp2.getProxima(), hprox1));
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public static int calculaCusto(Trem t1, Trem t2, Malha malha) {
		int custoTotal = 0;
		// custoParada custoManterParadoNaEstaçãoPorMinuto default duplas(Estacao,Custo)
		
		//Custos para t1
		Custos custos = t1.getCustosDoTrem();		
		
		//Custos para t2
		PTP ptp2 = t2.getPTP();
		int prox2 =ptp2.getProxima();
		int d = malha.distancia(prox2, t2.getDestino());
		
		LocalDateTime tprox2 = (ptp2.getHorario().plusMinutes((int) (d) / t1.getVelocidade()));
		
		// custo para parar o trem + custo do trem parado + custo do Trem 
		custoTotal = tprox2.getMinute()*custos.getCustoParado()+ t1.getCustosDoTrem(t1.getPosicao());

		return custoTotal;
		
	}
}
