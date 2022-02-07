package cenarios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Vector;

import Malha.Malha;
import dominio.Meeting;
import dominio.PTP;
import dominio.Trem;

// Planned Meet (conhecida como Tie nos meios ferroviários)
// Dois trens devem se encontrar por algum motivo. 
// O trem que chega primeiro à estação planejada espera a chegada do outro.

public class ScenarioII_5 {

	public static boolean realizar(Malha malha, Vector<Trem> trens) {

		for (int i = 0; i < trens.size(); i++) {

			Trem t1 = trens.get(i);
			PTP ptp1 = t1.getPTP();

			// Checa se há alguma parada programada para o trem na estação ende ele está:
			if (t1.getTie().size() > 0) {

				ArrayList<Meeting> ties = t1.getTie();

				for (int j = 0; j < ties.size(); j++) {

					Meeting tie = ties.get(j);
					int pos1 = t1.getPTP().getPosicao();

					if (tie.getEstacao() == pos1) {
						Trem t2 = tie.getT1() == t1 ? tie.getT2() : tie.getT1();

						if (t2.getPosicao() != pos1) {
							// Se o outro trem não chegou, espera até o horário que ele chegaria se fosse direto:

							PTP ptp2 = t2.getPTP();
							LocalDateTime h = ptp2.getHorario().plusMinutes((t2.tempoAteEstacao(pos1, malha)));
							PTP p = new PTP(ptp1.getPosicao(), 0, ptp1.getProxima(), h);
						
							if ((t1.getPercursoDoTrem().get(0).getPosicao() == ptp2.getPosicao()) ||
									(t1.getPercursoDoTrem().size() > 1 && t1.getPrevPTP().getPosicao() == ptp2.getPosicao())) {	
								;
							}
							else {
								malha.entrarEstacao(t1,ptp1.getPosicao());
							}
							
							
							t1.addPTP(p);					
						
						}
						else {
							// Se o outro trem chegou, adiciona a espera nos dois trens igual à duração:

							PTP ptp = t1.getPTP();
							LocalDateTime h = ptp1.getHorario().plusMinutes(tie.getDuracao());
							t1.addPTP(new PTP(ptp1.getPosicao(), 0, ptp1.getProxima(), h));
							malha.entrarTrecho(t1, ptp.getPosicao(),
									ptp.getProxima(),
									ptp.getHorario(),
									h);
							ptp = t2.getPTP();
							h = ptp1.getHorario().plusMinutes(tie.getDuracao());
							
							if ((t2.getPercursoDoTrem().get(0).getPosicao() == ptp1.getPosicao()) ||
									(t2.getPercursoDoTrem().size() > 1 && t1.getPrevPTP().getPosicao() == ptp1.getPosicao())) {	
								;
							}
							else {
								malha.entrarEstacao(t2,ptp1.getPosicao());
							}
							
							
							t2.addPTP(new PTP(ptp.getPosicao(), 0, ptp.getProxima(), h));
						
						}
						return true;
					}
				}
			}
		}
		return false;
	}
}
