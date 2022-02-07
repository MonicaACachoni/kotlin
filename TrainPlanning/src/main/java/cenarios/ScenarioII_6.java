package cenarios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Vector;

import Malha.Malha;
import dominio.PTP;
import dominio.Trecho;
import dominio.Trem;


// Neste cenário o trem incumbente está na última estação antes do destino 
// final no trajeto.
// Se não houver capacidade na estação ou se o trem incumbente entrar em 
// conflito com algum trem na direção contrária então o trem incumbente deve 
// esperar onde está.


public class ScenarioII_6 {

	public static boolean realizar(Malha malha, Vector<Trem> trens, Trem t1) {

		PTP ptp1 = t1.getPTP();
		int posicao = ptp1.getPosicao();
		int proxima = ptp1.getProxima();
		int terminal = t1.getDestino();

		if (proxima == terminal) {

			System.out.println("\n> [II.6] Final");

			int vagas = malha.espacoDisponivelEstacao(terminal);

			LocalDateTime hprox = ptp1.getHorario().plusMinutes(t1.tempoAteEstacao(proxima, malha));
			Trecho ocupacao = malha.trechoEstaOcupado(posicao, proxima, ptp1.getHorario(), hprox);

			if (vagas > 0 && ocupacao == null) {

				// Testa se o trem está na estação de origem ou se o último PTP foi ficar parado:
				if ((t1.getPercursoDoTrem().get(0).getPosicao() == posicao) ||
						(t1.getPercursoDoTrem().size() > 1 && t1.getPrevPTP().getPosicao() == posicao)) {	
					malha.sairEstacao(t1,posicao);
					malha.entrarTrecho(t1, posicao,	proxima, ptp1.getHorario(), hprox);			
				}
				else {
					malha.entrarTrecho(t1, posicao,	proxima, ptp1.getHorario(), hprox);
				}

				t1.addPTP(new PTP(proxima, 0, -1, hprox));

				//				System.out.printf("incumbente %10s v %2d curr %d %s next %s\n",t1.getNome(),t1.getVelocidade(),ptp1.getPosicao(),ptp1.getHorario(),-1);
				//				System.out.printf("movimento para %2d\n",proxima);

			} 
			else {  

				LocalDateTime hora = null;

				if (vagas == 0) {
					
					//
					 ArrayList<Trem> ocupacaoProxima = malha.getOcupacao(proxima);
					
					// Pega o horário do trem com menor horario na próxima estação:
					Trem tx = ocupacaoProxima.get(0);

					for (int i = 1; i < ocupacaoProxima.size(); i++) {
						Trem ti = trens.get(i);

						if (ti.getPosicao() == proxima) {
							LocalDateTime hi = ti.getPTP().getHorario();
							LocalDateTime hx = tx.getPTP().getHorario();

							if (hi.compareTo(hx) < 0) { 
								tx = ti;
							}
						}
					}

					hora = tx.getPTP().getHorario();
				}

				if (ocupacao != null) {

					Trem tx = ocupacao.getT();
					LocalDateTime h = tx.getHorario();

					if (hora != null) { 
						if (h.compareTo(hora) > 0)
							hora = h;
					}
					else
						hora = h;
				}


				// Testa se o trem está na estação de origem ou se o último PTP foi ficar parado:
				if ((t1.getPercursoDoTrem().get(0).getPosicao() == posicao) ||
						(t1.getPercursoDoTrem().size() > 1 && t1.getPrevPTP().getPosicao() == posicao)) {	
					;
				}
				else {
					malha.entrarEstacao(t1,posicao);
				}

			if(hora==null)	
				hora =hprox;
				
				t1.addPTP(new PTP(posicao, 0, ptp1.getProxima(), hora));
				System.out.printf("incumbente %10s v %2d curr %d %s next %s\n",t1.getNome(),t1.getVelocidade(),ptp1.getPosicao(),ptp1.getHorario(),-1);
				System.out.printf("parado em %2d até %s\n",posicao,hora);
			}

			return true;
		}
		return false;
	}
}
