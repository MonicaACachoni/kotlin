package cenarios;

import java.time.LocalDateTime;
import java.util.Vector;

import Malha.Malha;
import dominio.AreaProibida;
import dominio.PTP;
import dominio.Trem;


// Este cenário mantém os trens que vão entrar em áreas proibidas na estação em
// que estão.
// Também mantém parados trens que estejam dentro de uma área proibida; esse
// caso não faz parte do artigo explicitamente.


public class ScenarioI_1 {

	
	public static void realizar(Malha malha, Vector<Trem> trens, Vector<AreaProibida> proibidas) {

		System.out.println("\n> [I.1] Areas Proibidas");
		System.out.println("trens: " + trens.size());

		/*
		se executar esse cenario apenas 1x e a Area proibida aparece em uma estacao posterior nao ira funcionar, por isso sera chamado antes 
		do "mundo ideal" pra checar se a area proibida
		nao da tbm pra checar se esta fora da estacao, pois tem q ver se nao indo pra proxima tbm nao ira entrar em area proibida
		*/
				
		for (int i = 0; i < trens.size(); i++) {

			Trem t = trens.get(i);
			PTP ptp = t.getPTP();

		//	if (ptp.getDistancia() > 0) {    // tem ser mesmo pra qdo trem ta na estacao e quer ir pra proxima e eh proibida, naoó qdo esta em transito

				int prox = ptp.getProxima();

				for (int j = 0; j < proibidas.size(); j++) {

					AreaProibida p = proibidas.get(j);
					boolean dentro = malha.estacaoEstaNoTrecho(ptp.getPosicao(), p.getEstacaoI(), p.getEstacaoF());

					// Trem i está dentro da área ou vai entrar na área:
					if (dentro || (!dentro && (prox == p.getEstacaoI() || prox == p.getEstacaoF()))) {

						// Se a movimentação do trem tem interseção com o tempo de proibição, então
						// mantém o trem parado até o fim da proibição, senão não faz nada.

						LocalDateTime tprox = ptp.getHorario().plusMinutes((t.tempoAteEstacao(prox, malha)));

						if (p.getTempoI().compareTo(tprox) <= 0 && tprox.compareTo(p.getTempoF()) < 0) {
							PTP nptp = new PTP(ptp.getPosicao(), ptp.getDistancia(), prox, p.getTempoF());
							t.addPTP(nptp);
						}
					}
				}
			//}
		}
	}
}
