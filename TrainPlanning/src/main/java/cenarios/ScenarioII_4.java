package cenarios;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Vector;

import Malha.Malha;
import dominio.PTP;
import dominio.Trem;


// Neste cenário um trem t está na estação k e está programado para esperar
// nessa estação.  Durante o período de espera a estação vai ficar sem
// capacidade.  A solução é reprogramar a espera de t na estação de onde ele
// veio, digamos k-1.  Se k-1 também ficar sem capacidade durante a permanência
// de t, a espera deve ser reprogramada para k-2 e assim por diante.


public class ScenarioII_4 {


	public static boolean realizar(Malha malha, Vector<Trem> trens) {

		for (int i = 0; i < trens.size(); i++) {

			Trem t = trens.get(i);

			// Verifica se o trem tem dois ou mais PTPs finais na mesma estação:
			ArrayList<PTP> C = t.getPercursoDoTrem();

			int k = C.size()-1;
			while (k > 0 && C.get(k).getPosicao() == C.get(k-1).getPosicao())
				k--;

			// O trem não ficou parado na estação:
			if (k == C.size()-1)
				continue;

			// O trem não tem estação anterior, i.e., ele começou seu trajeto na estação em que está:
			if (k == 0)
				continue;

			PTP fim = t.getPTP();
			PTP ini = C.get(k);

			int e = fim.getPosicao();

			if (maxTrens(trens, e, ini.getHorario(), fim.getHorario(), t) > malha.getCapacidade(e)) {

				// Calcula a espera do trem t na estação anterior:
				ArrayList<Trem> X = malha.getOcupacao(e);
				
				// Pega o menor PTP em X:
				int jmin = 0;

				if (X==null) {
					return false;
				}
				else {
					
					System.out.println("\n> [II.4] !!!!!!!!!!!!");
					System.out.printf("%s \n",X);
					
					//ADD -1 pra tamanho do vetor
					for (int j = 1; j < X.size()-1; j++) {
						LocalDateTime h = X.get(j).getPTP().getHorario();
						LocalDateTime hm = X.get(jmin).getPTP().getHorario();

						if (h.compareTo(hm) < 0) 
							jmin = j;
					}
					LocalDateTime h = trens.get(jmin).getPTP().getHorario();
					LocalDateTime atual = t.getPTP().getHorario();

					int delta = (int) h.until(atual,ChronoUnit.MINUTES);
					delta += (int) ini.getHorario().until(fim.getHorario(),ChronoUnit.MINUTES);
					delta = Math.abs(delta);
					System.out.println("delta: "+ Math.abs(delta));
					
					// Remove os PTPs de t para ele voltar para a estação anterior:
					while ((t.getPercursoDoTrem().size()>0)  &&
							t.getPercursoDoTrem().get(t.getPercursoDoTrem().size()-1).getPosicao() == e) {
						System.out.println("Removendo ultimo Trecho e PTP !!!");
						System.out.printf("%s  Remove trem %d %d %s \n",t.getNome(),t.getPosicao(), t.getProxima(),t.getHorario());
							malha.removerUltimoTrecho(t);
							t.removeUltimoPTP();
					}

					// Adiciona a espera a t:
					PTP u = t.getPTP();
					t.addPTP(new PTP(u.getPosicao(),0,u.getProxima(),u.getHorario().plusMinutes(delta)));
					if ((t.getPercursoDoTrem().get(0).getPosicao() == u.getPosicao()) ||
							(t.getPercursoDoTrem().size() > 1 && t.getPrevPTP().getPosicao() == u.getPosicao())) {	
						;
					}
					else {
						malha.entrarEstacao(t,u.getPosicao());
					}
			}
	
				return true;
			}
		}

		return false;
	}


	
	// Calcula o maior número de trens que ficaram em uma estação durante
	// um intervalo de tempo.
	private static int maxTrens(Vector<Trem> trens,
								int estacao, LocalDateTime inicio, LocalDateTime termino, Trem t) {


		// Constrói uma lista ordenada S que tem os pontos dos intervalos que têm
		// interseção com [inicio,termino] e uma lista C que tem os índices do
		// início e do fim de cada intervalo, inicialmente com [1,-1].  Cada novo
		// intervalo que tem interseção com [inicio,termino] adiciona dois pontos em
		// S e duas marcas correspondentes, t e -t.

		// Por exemplo:
		//  10,50  ->  10,20,30,50  ->  5,10,15,20,30,50
		//   1,-1  ->   1, 2,-2,-1  ->  3, 1,-3, 2,-2,-1

		// Depois é só encontrar o número máximo de números positivos sem o
		// correspondente negativo entre 1 e -1.

		ArrayList<LocalDateTime> S = new ArrayList<LocalDateTime>();
		S.add(inicio);
		S.add(termino);

		ArrayList<Integer> C = new ArrayList<Integer>();
		C.add(1);
		C.add(-1);
		int x = 2;	

		for (int j = 0; j < trens.size(); j++) {
			
			if (trens.get(j)==t)
				continue;

			ArrayList<PTP> caminho = trens.get(j).getPercursoDoTrem();

			// Percorre os PTPs do trem j:
			int k = caminho.size()-1; 
			int deltak = -1;
			
			while (k >= 0) {

				PTP p = caminho.get(k);
				LocalDateTime hi = p.getHorario();
				LocalDateTime hf;

				// O trem j não esteve na estação no intervalo [inicio,termino]:
				//if (hi.compareTo(inicio) < 0)
				//	break;

				// O trem j esteve na estação:
				if (p.getPosicao() == estacao) {

					int z = k;
					while (z-1 > 0 && caminho.get(z-1).getPosicao() == estacao) {
						z--;
						deltak += -1;
					}
						
					if (z == k) { 
						hi = p.getHorario();
						hf = p.getHorario();
					}
					else {
						hi = caminho.get(z).getHorario();
						hf = p.getHorario();
					}						

					// O trem j esteve na estacao no intervalo [inicio,termino]:
					if (Utils.intersecao(inicio,termino,hi,hf)) {

						if (hi.compareTo(S.get(0)) < 0) {
							S.add(0,hi);
							C.add(0,x);
						}
						else {
							for (int s = 0; s < S.size()-1; s++) {

								if (hi.compareTo(S.get(s)) > 0 && hi.compareTo(S.get(s+1)) < 0) {
									S.add(s+1,hi);
									C.add(s+1,x);
									break;
								}
							}
						}

						int n = S.size();
						if (hf.compareTo(S.get(n-1)) > 0) {
							S.add(n,hf);
							C.add(n,-x);
						}
						else {
							for (int s = 0; s < S.size()-1; s++) {

								if (hf.compareTo(S.get(s)) > 0 && hf.compareTo(S.get(s+1)) < 0) {
									S.add(s+1,hf);
									C.add(s+1,-x);
									break;
								}
							}
						}
					
						x++;
					}
				}
				else {
					deltak = -1;
				}
				
				k += deltak;
			}
		}


		boolean dentro = false;
		int s;
		int max = 0;
		int c = 0;

		  System.out.println("C ==== "+ C);
		  System.out.println("S ==== "+ S);
		for (s = 0; s < C.size(); s++) {

			int i = C.get(s);

			if (i == 1)
				dentro = true;

			if (i == -1)
				dentro = false;

			if (i > 0) {
				c++;

				if (dentro && c > max)
					max = c;
			}
			else
				c--;
		}
  System.out.println("max ==== "+ max);
		return max;
	}

}


