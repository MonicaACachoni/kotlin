package cenarios;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Vector;

import Malha.Malha;
import dominio.PTP;
import dominio.Trecho;
import dominio.Trem;


// Neste cenário quatro trens t1,t2,t3,t4 estão na seguinte situação:
//
//  (ek+2)  t2->  t1->  (ek+1)  (ek)  <-t3  <-t4  (ek-1)
//
// e os horários nos PTPs são iguais e as estações ek e ek+1 têm capacidade para 2 trens.
// 
// Para resolver os movimentos são intercalados e ao final t1 e t4 estão em k e
// t2 e t3 estão em k+1.


public class ScenarioI_3 {

	public static boolean realizar(Malha malha, Vector<Trem> trens) {

		if (trens.size() < 4)
			return false;
		
		// Constrói um vetor L de trechos e um vetor paralelo L com os respectivos trens:
		Vector<Trecho> T = new Vector<Trecho>();
		Vector<ArrayList<Trem>> L = new Vector<ArrayList<Trem>>();

		for (int i = 0; i < trens.size(); i++) {
			Trem t = trens.get(i);
			PTP ptp1 = t.getPTP();

			if (ptp1.getDistancia() > 0) {
				int e1 = ptp1.getPosicao();
				int e2 = ptp1.getProxima();

				if (e2 < e1) {
					int tmp = e1;
					e1 = e2;
					e2 = tmp;
				}

				// Se o trecho já tinha aparecido então só adiciona t:
				int k = 0;
				for ( ; k<T.size(); k++) {
					Trecho x = T.get(k);
					if (x.getE1() == e1 && x.getE2() == e2) {
						L.get(k).add(t);
						break;
					}
				}

				if (k == L.size()) {
					Trecho z = new Trecho(e1,e2,null,null,null);
					T.add(z);
					ArrayList<Trem> A = new ArrayList<Trem>();
					A.add(t);
					L.add(A);
				}
			}
		}

		// Remove de T e L os trechos que não têm exatamente dois trens na mesma direção com horário de saída igual:
		for (int k = T.size()-1; k >= 0; k--) {
			ArrayList<Trem> A = L.get(k);

			if (A.size() != 2) {
				T.remove(k);
				L.remove(k);
				break;				
			}

			Trem a = A.get(0);
			PTP ptpa = a.getPTP();

			Trem b = A.get(1);
			PTP ptpb = b.getPTP();

			if (ptpa.getPosicao() != ptpb.getPosicao() || ptpa.getHorario().compareTo(ptpb.getHorario()) != 0) {
				T.remove(k);
				L.remove(k);			
			}				
		}


		// Para cada trecho com dois trens em T verifica se há um outro trecho que satisfaz o cenário:
		for (int p = 0; p < T.size(); p++) {
			for (int q = p+1; q < T.size(); q++) {

				ArrayList<Trem> trensa = L.get(p);
				ArrayList<Trem> trensb = L.get(q);

				// Estações:
				int k = trensa.get(0).getPTP().getProxima();
				int l = trensb.get(0).getPTP().getProxima(); // l = k+1

				if (malha.estacoesAdjacentes(k,l) && malha.getCapacidade(k) == 2 && 
					malha.getCapacidade(l) == 2) {

					// Achou dois pares de trechos que satisfazem o cenário.
					// Pega os trens:

					// (ek+2)  t2->  t1->  (ek+1)  (ek)  <-t3  <-t4  (ek-1)
					Trem t1,t2,t3,t4;

					if (trensb.get(0).getPTP().getDistancia() > trensb.get(1).getPTP().getDistancia()) {
						t1 = trensb.get(0);
						t2 = trensb.get(1);
					}
					else {
						t1 = trensb.get(1);
						t2 = trensb.get(0);
					}

					if (trensa.get(0).getPTP().getDistancia() > trensa.get(1).getPTP().getDistancia()) {
						t3 = trensa.get(0);
						t4 = trensa.get(1);
					}
					else {
						t3 = trensa.get(1);
						t4 = trensa.get(0);
					}


					PTP ptp1 = t1.getPTP();
					PTP ptp2 = t2.getPTP();
					PTP ptp3 = t3.getPTP();
					PTP ptp4 = t4.getPTP();

					System.out.println("\n> [I.3] conflitos multiplos");
					System.out.println("trens: " + trens.size());
					
					System.out.printf("trem t1 %10s curr %d next %d\n",t1.getNome(),ptp1.getPosicao(),ptp1.getProxima());
					System.out.printf("trem t2 %10s curr %d next %d\n",t2.getNome(),ptp2.getPosicao(),ptp2.getProxima());
					System.out.printf("trem t3 %10s curr %d next %d\n",t3.getNome(),ptp3.getPosicao(),ptp3.getProxima());
					System.out.printf("trem t4 %10s curr %d next %d\n\n",t4.getNome(),ptp4.getPosicao(),ptp4.getProxima());
				
					
					// As quatro estações:
					//  N  (ek+2)  t2->  t1->  (ek+1)  (ek)  <-t3  <-t4  (ek-1)  S
					//       m                   l       k                 j
					
					int j = ptp3.getPosicao(); // j = k-1
					// k = k
					// l = k+1
					int m = ptp1.getPosicao(); // m = k+2;

					LocalDateTime hora, hora1l, hora1k, hora2, hora3, hora4, inicial;

					// t3 vai para k:
					hora3 = ptp3.getHorario().plusMinutes((t3.tempoAteEstacao(k, malha)));
					
					malha.entrarTrecho(t3, j, k, ptp3.getHorario(), hora3);
					
					ptp3 = new PTP(k,0,l,hora3);
					t3.addPTP(ptp3);
										
					
					// t4 vai para k. Se t4 atropela t3 ele deve esperar:
					inicial = ptp4.getHorario(); 					
					hora4 = inicial.plusMinutes((t4.tempoAteEstacao(k, malha)));
					
					if (hora4.compareTo(hora3) < 0) {
						ptp4 = new PTP(ptp4.getPosicao(), ptp4.getDistancia(), ptp4.getProxima(), hora3);
						t4.addPTP(ptp4);
						hora4 = ptp4.getHorario().plusMinutes((t4.tempoAteEstacao(k, malha)));
					}
					
					ptp4 = new PTP(k,0,l,hora4);
					t4.addPTP(ptp4);
					
					malha.entrarTrecho(t4, k, j, inicial, hora4);
					
					
					// t3 vai para k+1:
					hora3 = ptp3.getHorario().plusMinutes((t3.tempoAteEstacao(l, malha)));

					malha.entrarTrecho(t3, m, l, ptp3.getHorario(), hora3);
					
					ptp3 = new PTP(l,0,m,hora3);
					t3.addPTP(ptp3);
					
					
					// t1 vai para k+1:
					hora1l = ptp1.getHorario().plusMinutes((t1.tempoAteEstacao(l, malha)));
					
					malha.entrarTrecho(t1, l, k, ptp1.getHorario(), hora1l);
					
					ptp1 = new PTP(l,0,k,hora1l);
					t1.addPTP(ptp1);
					
					//malha.entrarEstacao(t1,l);

					// t1 espera em K+1 até t3 chegar em k+1:
					if (hora1l.compareTo(hora3) < 0) {
						ptp1 = new PTP(l,0,k,hora3);
						t1.addPTP(ptp1);
					}

					// t2 fica parado.

					//  (ek+2) -- (ek+1) -- (ek) -- (ek-1)
					//    m         l        k        j

					// t1 vai para k:
					hora1l = ptp1.getHorario();
					hora1k = hora1l.plusMinutes((t1.tempoAteEstacao(k, malha)));
					ptp1 = new PTP(k,0,j,hora1k);
					t1.addPTP(ptp1);

					malha.entrarTrecho(t1, l, k, hora1l, hora1k);
										
					// t2 vai para k+1, mas espera para chegar lá depois que t1 sair:
					inicial = ptp2.getHorario(); 
					hora2 = inicial.plusMinutes((t2.tempoAteEstacao(l, malha)));

					if (hora2.compareTo(hora1l) < 0) {
						long delta = hora2.until(hora1l,ChronoUnit.MINUTES);
						hora = ptp2.getHorario().plusMinutes(delta);
						ptp2 = new PTP(ptp2.getPosicao(),ptp2.getDistancia(),l,hora);
						t2.addPTP(ptp2);
						hora2 = ptp2.getHorario().plusMinutes((t2.tempoAteEstacao(l, malha)));
					}

					// t2 vai para k+1:
					ptp2 = new PTP(l,0,k,hora2);
					t2.addPTP(ptp2);
					
					// t2 ocupa o trecho (k+2,k+1):
					malha.entrarTrecho(t2, m, l, inicial, hora2);

					// t3 fica em k+1 até t2 chegar em k+1:
					ptp3 = new PTP(l,0,m,hora2);
					t3.addPTP(ptp3);
					
					// t4 fica em k até t1 chegar em k:
					ptp4 = new PTP(k,0,l,hora1k);
					t4.addPTP(ptp4);
					
					malha.entrarEstacao(t1,k);
					malha.entrarEstacao(t2,l);
					malha.entrarEstacao(t3,l);
					malha.entrarEstacao(t4,k);

					return true;
				}
			}
		}
		return false;
	}
}
