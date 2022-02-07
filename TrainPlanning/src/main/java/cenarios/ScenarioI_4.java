package cenarios;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Vector;

import Algoritmo.Algoritmo5_1;
import Malha.Malha;
import Malha.StatusTrens;
import dominio.PTP;
import dominio.Trem;

// O trem incumbente está fora de uma estação.  Se ele não estiver envolvido em
// conflitos e se a próxima estação tiver capacidade então o incumbente é movido
// para a próxima estação.

public class ScenarioI_4 {

	public static boolean realizar(Malha malha, Vector<Trem> trens, Trem t1, Vector<Trem> finalizados) {

		if (trens.size() < 2)
			return false;

		PTP ptp1 = t1.getPTP();

		for (int j = 0; j < trens.size(); j++) {

			Trem t2 = trens.get(j);

			if (t2 == t1)
				continue;

			PTP ptp2 = t2.getPTP();

			// t1 atropelaria um trem t2 no mesmo trecho e na mesma direção. t1 deve esperar.
			if (malha.statusDePosicaoEntreDoisTrens(t1, t2) == StatusTrens.T1atropelaT2) {

				System.out.println("\n> [I.4] movimento simples");
				System.out.println("Atropelamento");

				int d = malha.distancia(ptp1.getPosicao(), ptp1.getProxima());

				LocalDateTime tprox1 = ptp1.getHorario().plusMinutes((int) (d - ptp1.getDistancia()) / t1.getVelocidade());
				LocalDateTime tprox2 = ptp2.getHorario().plusMinutes((int) (d - ptp2.getDistancia()) / t2.getVelocidade());

				int delta = (int) tprox1.until(tprox2, ChronoUnit.MINUTES) + 1;

				LocalDateTime tprox = ptp1.getHorario().plusMinutes(delta);

				malha.entrarTrecho(t1, ptp1.getPosicao(), t1.getPTP().getProxima(), ptp1.getHorario(), tprox1);

				PTP p = new PTP(ptp1.getPosicao(), ptp1.getDistancia(), ptp1.getProxima(), tprox);
				t1.addPTP(p);

				return true;
			}

			// Não há capacidade na próxima estação de t1:
			if (!malha.temCapacidade(ptp1.getProxima())) {

				System.out.println("\n> [I.4] movimento simples");
				System.out.println("Sem capacidade");

				if (!malha.temCapacidade(ptp1.getPosicao())) {
					Algoritmo5_1.deadlock(t1);
				} else {
					// Retrocede o trem:
					LocalDateTime tpos = ptp1.getHorario().plusMinutes((int) (ptp1.getDistancia()) / t1.getVelocidade());
					malha.entrarTrecho(t1, ptp1.getPosicao(), t1.getPTP().getProxima(), ptp1.getHorario(), tpos);
					t1.addPTP(new PTP(ptp1.getPosicao(), 0, ptp1.getProxima(), tpos));
				}

				return true;
			}

			System.out.println("\n> [I.4] movimento simples");

			// Não há impedimento da trajetoria de outro trem ou de capacidade, t1 se move
			// para a estação:
			int prox = ptp1.getProxima();
			int destino = t1.getDestino();

			int d = (int) (malha.distancia(ptp1.getPosicao(), prox) - ptp1.getDistancia());
			LocalDateTime horario = ptp1.getHorario().plusMinutes(d / t1.getVelocidade());

			malha.entrarTrecho(t1, ptp1.getPosicao(), prox, ptp1.getHorario(), horario);
			PTP p = new PTP(prox, 0, malha.nextStation(prox, destino), horario);
			t1.addPTP(p);

			if (prox == destino) {
				finalizados.add(t1);
				trens.remove(t1);
			}

			malha.entrarEstacao(t1, prox);

			return true;
		}

		return false;
	}
}
