package cenarios;

import java.time.LocalDateTime;
import java.util.Vector;

import Algoritmo.Algoritmo5_1;

import Malha.Malha;
import Malha.StatusTrens;
import dominio.Custos;
import dominio.PTP;
import dominio.Trem;
import cenarios.Utils;
// Neste cenário dois trens estao entre duas estações e indo um em direção ao
// outro.  Para resolver, o trem com menor custo é retrocedido e o outro é avançado.  
// Os dois podem andar simultaneamente.

public class ScenarioI_2 {
//teste dropbox

	public static boolean realizar(Malha malha, Vector<Trem> trens, Trem t1) {
		
		if (trens.size() < 2)
			return false;

		for (int j = 1; j < trens.size(); j++) {

			Trem t2 = trens.get(j);

			if (t1 != t2 ) {

				if (malha.statusDePosicaoEntreDoisTrens(t1, t2) == StatusTrens.ColisaoFrontal)		
				{

					System.out.println("\n> [I.2] Colisao frontal");
					System.out.println("trens: " + trens.size());

					// Faz t1 ser o trem com menor custo de retrocesso:
					
					if (calculaCusto(t1,t2,malha) >calculaCusto(t2,t1,malha)) {
						Trem aux = t1;
						t1 = t2;
						t2 = aux;
					}

					PTP ptp1 = t1.getPTP();
					int pos = ptp1.getPosicao();
					
					
					
					
					// t1 é retrocedido:
					if (!malha.temCapacidade(pos)) {
						Algoritmo5_1.deadlock(t1);
					}
					int d = (int) ptp1.getDistancia();
					LocalDateTime t1prox = ptp1.getHorario().plusMinutes((int) d / t1.getVelocidade());

					malha.entrarTrecho(t1, t1.getPosicao(), t1.getProxima(), ptp1.getHorario(), t1prox);

					PTP p = new PTP(pos, 0, malha.nextStation(pos, t1.getDestino()), t1prox);
					t1.addPTP(p);
	
					malha.entrarEstacao(t1, pos);
					

					// t2 vai ser avançado e deve chegar depois de t1:
					PTP ptp2 = t2.getPTP();
					pos = ptp2.getProxima();

					if (!malha.temCapacidade(pos))
						Algoritmo5_1.deadlock(t2);
					
					LocalDateTime inicial =  ptp2.getHorario(); 					

					d = (int) (malha.distancia(ptp1.getPosicao(), ptp2.getPosicao()) - ptp2.getDistancia());
					LocalDateTime t2prox = ptp2.getHorario().plusMinutes((int) d / t2.getVelocidade());
									
					if (t2prox.compareTo(t1prox) < 0) {
						// t2 chegaria antes de t1.  Mantém t2 parado esperando t1:
						p = new PTP(ptp2.getPosicao(), ptp2.getDistancia(), ptp2.getProxima(), t1prox);
						t2.addPTP(p);
						t2prox = p.getHorario().plusMinutes((int) d / t2.getVelocidade());
					}

					malha.entrarTrecho(t2, t2.getPosicao(), t2.getProxima(), inicial, t2prox);

					p = new PTP(pos, 0, malha.nextStation(pos,t2.getDestino()), t2prox);
					t2.addPTP(p);

					malha.entrarEstacao(t2, pos);

					return true;
				}
				else if ((malha.statusDePosicaoEntreDoisTrens(t1, t2) == StatusTrens.T1andaAtrasDeT2)) {
					System.out.println("Tratar cenario da fase 1 nao previsto no paper");
					//Retroceder o trem de menor d
					if (t1.getPercursoDoTrem().get(0).getDistancia() < t2.getPercursoDoTrem().get(0).getDistancia()) {
						System.out.println("Retroceder trem t1: "+ t1.toString());
						PTP ptp1 = t1.getPTP();
						LocalDateTime tpos = ptp1.getHorario().plusMinutes((int) (ptp1.getDistancia()) / t1.getVelocidade());
						malha.entrarTrecho(t1, ptp1.getPosicao(), t1.getPTP().getProxima(), ptp1.getHorario(), tpos);
						t1.addPTP(new PTP(ptp1.getPosicao(), 0, ptp1.getProxima(), tpos));

					}
					else {
						System.out.println("Retroceder trem t2: "+ t2.toString());

						PTP ptp2 = t2.getPTP();
						LocalDateTime tpos = ptp2.getHorario().plusMinutes((int) (ptp2.getDistancia()) / t2.getVelocidade());
						malha.entrarTrecho(t2, ptp2.getPosicao(), t1.getPTP().getProxima(), ptp2.getHorario(), tpos);
						t1.addPTP(new PTP(ptp2.getPosicao(), 0, ptp2.getProxima(), tpos));
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
		Custos custos1 = t1.getCustosDoTrem();		
		
		//Custos para t2
		Custos custos2 = t2.getCustosDoTrem();		
		PTP ptp2 = t2.getPTP();
		int prox2 =ptp2.getProxima();
		int d2 = (int) ptp2.getDistancia();
		int d = malha.distancia(prox2, t2.getDestino());
		LocalDateTime tprox2 = (ptp2.getHorario().plusMinutes((int) (d-d2) / t1.getVelocidade()));
		
		// custo para parar o trem + custo do trem parado + custo do Trem 
		custoTotal = custos1.getCustoDeParada() +  tprox2.getMinute()*custos1.getCustoParado();

		return custoTotal;
		
	}
}
