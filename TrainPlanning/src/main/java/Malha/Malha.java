package Malha;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.Scanner;
import java.util.Vector;

import cenarios.Utils;
import dominio.PTP;
import dominio.Trecho;
import dominio.Trem;

public class Malha {

	public static boolean DEBUG = true;
	
	
	Graph G;
	String[] nomes;
	int[] capacidades;


	public int getCapacidade(int i) {
		return capacidades[i];
	}

	
	// Para cada estacao de 0 a n-1 temos uma lista de trens nela:
	private Vector<ArrayList<Trem>> ocupacaoEstacao;
		
	// Um vetor de ocupações de trechos ordenado por Hi:
	private Vector<Trecho> ocupacaoTrecho ;
	
	
	// Custos dos caminhos minimos entre as estações:
	private int[][] Costs;
	
	// Predecessores dos vértices nos caminhos mínimos.  Se Pi[i][j] = k então a aresta (k,j) está no caminho mínimo de i a j. 
	private int[][] Pi;

	// Numero de vertices e arestas na malha:
	private int n;
	private int m;

	
	
	public Malha(String arquivoMalha, String arquivoEstacoes) throws IOException {

		G = GraphLoader.loadFromFile(arquivoMalha);
		//G.print();
		
		n = G.getN();
		m = G.getM();
			
		ShortestPaths sp = new ShortestPaths();
		sp.floyd(G);
		Costs = sp.getCosts();
		Pi = sp.getPi();

		// Carrega dados das estacoes do arquivo:
		Scanner s = new Scanner(new BufferedReader(new FileReader(arquivoEstacoes)));

		nomes = new String[n];
		capacidades = new int[n];

		int i = 0;
		while (s.hasNext()) {
			i = (int) s.nextInt();
			nomes[i] = s.next();
		System.out.println(nomes[i]);
			capacidades[i] = (int) s.nextInt();
		}
		s.close();

		// Cada estacao comeca com uma lista vazia de trens:
		ocupacaoEstacao = new Vector<ArrayList<Trem>>(n);
		for (i=0; i<n; i++) {
			ocupacaoEstacao.add(i, new ArrayList<Trem>());
		}
		
		// Os trechos começam vazios:
		ocupacaoTrecho = new Vector<Trecho>();
	}

	
	
	public void entrarEstacao(Trem t, int estacao) {
		// System.out.println("entrar ... "+ t.getNome() + "  ..."+ estacao);
		ocupacaoEstacao.get(estacao).add(t);
	}
	
	

	public void sairEstacao(Trem t, int estacao) {

		//System.out.println("saindo ... de "+ t.getNome() + "  ..." + estacao);
		ArrayList<Trem> L = ocupacaoEstacao.get(estacao);

		Iterator<Trem> itr = L.iterator();


		while (itr.hasNext()) {
			Trem x = itr.next();
			if (x == t) {
				itr.remove();
				break;
			}
		}
	}
	
	
	public void finalizartrem(Trem t) {

		for (int i=0; i<ocupacaoEstacao.size(); ++i) {
			ArrayList<Trem> T = ocupacaoEstacao.get(i);
			
			for (int j=0; j<T.size(); j++) {
				Trem x = T.get(j);
				if (x == t) {
					T.remove(t);
					break;
				}
			}
		}
	}
	
	
	public void imprimeOcupacaoTrechos() {
		
		for (int i = 0; i < ocupacaoTrecho.size(); ++i) {
			Trecho trecho = ocupacaoTrecho.get(i);
			System.out.printf("%10s (%d,%d) de %s até %s\n", trecho.getT().getNome(), trecho.getE1(), trecho.getE2(), trecho.getHi(), trecho.getHf());	
		}	
	}

	
	
	public void imprimeOcupacaoEstacoes() {
		
		for (int i=0; i<ocupacaoEstacao.size(); ++i) {
			ArrayList<Trem> T = ocupacaoEstacao.get(i);
			
			for (int j=0; j<T.size(); j++) {
				Trem x = T.get(j);
				System.out.printf("estação %d trem %d %10s atual %d prox %d\n",i,j,x.getNome(),x.getPosicao(),x.getProxima());
			}
		}
	}

	/*
	Adiciona uma ocupação de trecho, mantendo-as ordenadas por hf.
	*/
	public void entrarTrecho(Trem t1, int e1, int e2, LocalDateTime hi, LocalDateTime hf) {
	
		Trecho t = new Trecho(e1, e2, hi, hf, t1);
		ocupacaoTrecho.add(null);
		
		int i = ocupacaoTrecho.size()-1;
		for ( ; i>0; i--) {
			
			LocalDateTime h = ocupacaoTrecho.get(i-1).getHf();
			
			if (h.compareTo(hf) > 0) {
				ocupacaoTrecho.set(i,ocupacaoTrecho.get(i-1));
			}
			else {
				break;
			}
		}
		ocupacaoTrecho.set(i,t);
	}



	public void removerUltimoTrecho(Trem t1) {
	
		for (int i=ocupacaoTrecho.size()-1; i>=0; --i) 
			if (ocupacaoTrecho.get(i).getT()==t1) {
				ocupacaoTrecho.removeElementAt(i);
				break;
			}
	
	}



	public Trem tremTrecho(int e1, int e2, LocalDateTime hi, LocalDateTime hf) {

		for (int i=ocupacaoTrecho.size()-1; i>=0; i--) {
			
			Trecho t = ocupacaoTrecho.get(i);
			
			if (((t.getE1() == e1 && t.getE2() == e2) ||
				(t.getE1() == e2 && t.getE2() == e1))
				&&
				Utils.intersecao(t.getHi(), t.getHf(), hi, hf))
							
				return t.getT();	
		}
		return null;
	}
	
	
	
	public Trecho trechoEstaOcupado(int e1, int e2, LocalDateTime hi, LocalDateTime hf) {

		int i = ocupacaoTrecho.size()-1;

		while (i>=0) {
			Trecho t = ocupacaoTrecho.get(i);
			
			// Como as ocupações estão ordenadas por Hf, pode parar se trecho.Hf < hi:
			if (t.getHf().compareTo(hi) < 0) 
				break;
			
			if (((t.getE1() == e1 && t.getE2() == e2) || (t.getE1() == e2 && t.getE2() == e1)) &&
				Utils.intersecao(t.getHi(), t.getHf(), hi, hf)) {
				return t;
			}			
			i--;
		}
		
		return null;
	}
		
	
	
	public boolean temCapacidade(int estacao) {

		return ocupacaoEstacao.get(estacao).size() < capacidades[estacao];
	}

	
	
	public int espacoDisponivelEstacao(int estacao) {

		return capacidades[estacao] - ocupacaoEstacao.get(estacao).size();
	}

	
	
	public int distancia(int inicial, int terminal) {
		return Costs[inicial][terminal];
	}


	
	public int nextStation(int current, int destination) {

		if (Pi[current][destination] == -1)
			return -1;

		int k = destination;
		int next = destination;

		while (k != current && k != -1) {
			next = k;
			k = Pi[current][k];
		}

		return next;
	}

	
	
	
	public ArrayList<Integer> neighborStations(int station) {
		return G.getNeighbors(station);
	}

	
	
	/*
	 * Os dois trens estão fora de alguma estação e no mesmo trecho: 
	 *   ColisaoFrontal, Afastamento, t1
	 *
	 */
	public StatusTrens statusDePosicaoEntreDoisTrens(Trem t1, Trem t2) {

		PTP ptp1 = t1.getPTP();
		PTP ptp2 = t2.getPTP();

		// Os trens estao fora de uma estacao:
		if (ptp1.getDistancia() > 0 && ptp2.getDistancia() > 0) {

			// Os trens estão no mesmo trecho e na mesma direcao:
			if (ptp1.getPosicao() == ptp2.getPosicao() && ptp1.getProxima() == ptp2.getProxima()) {

				int d = distancia(ptp1.getPosicao(),ptp1.getProxima());
				int tprox1 = (int) ((d-ptp1.getDistancia())/t1.getVelocidade());
				int tprox2 = (int) ((d-ptp2.getDistancia())/t2.getVelocidade());

				LocalDateTime h1 = ptp1.getHorario().plusMinutes(tprox1);
				LocalDateTime h2 = ptp2.getHorario().plusMinutes(tprox2);

				if (ptp1.getDistancia() < ptp2.getDistancia()) 
					if (h1.compareTo(h2) <= 0) 
						return StatusTrens.T1atropelaT2;
					else 
						return StatusTrens.T1andaAtrasDeT2;

				if (ptp1.getDistancia() > ptp2.getDistancia())
					if (h1.compareTo(h2) >= 0) 
						return StatusTrens.T2atropelaT1;
					else 
						return StatusTrens.T2andaAtrasDeT1;
			}
		 
		// Os trens estão no mesmo trecho e em direcao contraria:
		else if (ptp1.getProxima() == ptp2.getPosicao() && ptp2.getProxima() == ptp1.getPosicao()) {

			// Distancia do trem 2 até a proxima estacao:
			double d2prox = Math.abs((distancia(ptp2.getPosicao(), ptp2.getProxima())) - ptp2.getDistancia());

			if (ptp1.getDistancia() <= d2prox) {
				return StatusTrens.ColisaoFrontal;
			} 
			else {
				return StatusTrens.Afastamento;
			}
		}
	} 
		
		// Os dois trens estao em alguma estacao:
		else if (ptp1.getDistancia() == 0 && ptp2.getDistancia() == 0) {
			if (ptp1.getPosicao() == ptp2.getPosicao()) {
				return StatusTrens.MesmaEstacao;
			}
			else {
				ArrayList<Integer> E = neighborStations(ptp1.getPosicao());
			
				for (int i=0; i<E.size(); i++) 
					if (E.get(i) == ptp2.getPosicao())
						return StatusTrens.EstacoesAdjacentes;
			}
		}

		return StatusTrens.Outros;
	}

	
	
	
	
	public ArrayList<Trem> getOcupacao(int estacao) {
		
		if (!ocupacaoEstacao.get(estacao).isEmpty())
			return ocupacaoEstacao.get(estacao);
		else
			return null;
	}

	


	/*
	 * Testa se uma estação e está no caminho entre as estações i e j (inclusive).
	 */
	
	public boolean estacaoEstaNoTrecho(int e, int i, int j) {

		int k = j;

		while (k != -1) {

			
			//System.out.printf("-->%d \n", k);
			
			if (k == e) 
				return true;

			k = Pi[i][k];
		}

		return false;
	}

	
	public boolean estacoesAdjacentes(int e1, int e2) {
		
		ArrayList<Integer> E = G.getNeighbors(e1);
		
		for (int i=0; i<E.size(); i++) 
			if (E.get(i) == e2)
				return true;
		
		return false;		
	}


	/*
	 * True se t1 entrou na estação e antes de t2. 
	 */

	public boolean chegouAntes(Trem t1, Trem t2, int e) {

		ArrayList<Trem> T = ocupacaoEstacao.get(e);

		for (Trem t : T) {
			if (t == t1)
				return true;
			if (t == t2)
				return false;
		}
		
		return false;
	}
	
		
}
	






//public Trem trensTrecho(int e1, int e2,LocalDateTime hi, LocalDateTime hf) {
//
//		LocalDateTime y1 = hi;
//		LocalDateTime y2 = hf;
//	for (int i = 0; i < ocupacaoTrecho.size(); ++i) {
//		
//		LocalDateTime x1 = ocupacaoTrecho.get(i).getHi();
//		LocalDateTime x2 = ocupacaoTrecho.get(i).getHf();
//
//		if ((ocupacaoTrecho!=null)&&
//			((ocupacaoTrecho.get(i)).getE1() == e1 && ( ocupacaoTrecho.get(i)).getE2() == e2) ||
//			(ocupacaoTrecho.get(i)).getE1() == e2 && ( ocupacaoTrecho.get(i)).getE2() == e1 
//			&& (
//			(x1.compareTo(y1) <= 0 && y1.compareTo(x2) <= 0) ||
//		(x1.compareTo(y2) <= 0 && y2.compareTo(x2) <= 0) ||
//			(y1.compareTo(x1) <= 0 && y2.compareTo(x2) >= 0))
//					
////			( ocupacaoTrecho.get(i).getHi().compareTo(hi)>=0 &&
////					ocupacaoTrecho.get(i).getHi().compareTo(hf)<=0 ) ||  // hi<H1<hf
////			(ocupacaoTrecho.get(i).getHf().compareTo(hi)>=0 &&        //hi<H2<hf
////			ocupacaoTrecho.get(i).getHf().compareTo(hf)<=0 ))
//		
//			
//						)
//			return ocupacaoTrecho.get(i).getT();	
//	}
//	return null;
//}
