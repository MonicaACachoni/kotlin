package cenarios;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Vector;

import Malha.Malha;
import dominio.PTP;
import dominio.Trem;


// Neste cenário temos dois trens em direções contrárias t1=(k,h1) e t2=(k,h2),
// t2 chegou de k+1, a estação para onde t1 vai, e os PTPs são tais que h2>h1.
// Ou seja, se t1 é o incumbente, ele vai ser programado para colidir com
// t2, que não chegou ainda.  A solução é manter t1 na estação até a chegada de t2.


public class ScenarioII_3 {

	public static boolean realizar(Malha m, Vector<Trem> trens) {

		for (int i = 0; i < trens.size(); i++) {

			Trem t1 = trens.get(i);
			PTP ptp1 = t1.getPTP();
			LocalDateTime h1 = ptp1.getHorario();

			for (int j = 0; j < trens.size(); j++) {

				if (i == j)
					continue;

				Trem t2 = trens.get(j);
				PTP ptp2 = t2.getPTP();
				PTP prev2 = t2.getPrevPTP();
				LocalDateTime h2 = ptp2.getHorario();

				if (ptp1.getPosicao() == ptp2.getPosicao() &&
					prev2 != null &&
					ptp1.getProxima() == prev2.getPosicao() &&
					h1.compareTo(h2) < 0) {

					long delta = h1.until(h2,ChronoUnit.MINUTES);
					h1 = h1.plusMinutes(delta);
					

					Trem trensnoTrecho = m.tremTrecho(ptp1.getPosicao(),	ptp1.getProxima(), ptp1.getHorario(), h1);
					if (trensnoTrecho == null) {

						// Testa se o trem está na estação de origem ou se o último PTP foi ficar parado:
						if ((t1.getPercursoDoTrem().get(0).getPosicao() == ptp1.getProxima()) ||
							(t1.getPercursoDoTrem().size() > 1 && t1.getPrevPTP().getPosicao() == ptp1.getProxima())) {	

							m.sairEstacao(t1,ptp1.getPosicao());
							m.entrarTrecho(t1, ptp1.getPosicao(),	ptp1.getProxima(), ptp1.getHorario(), h1);			

						}
						else {
							m.entrarTrecho(t1, ptp1.getPosicao(),	ptp1.getProxima(), ptp1.getHorario(), h1);
						}
					}
					t1.addPTP(new PTP(ptp1.getPosicao(), 0, ptp1.getProxima(), h1));
					
					return true;
				}
			}
		}
		return false;
	}
}
