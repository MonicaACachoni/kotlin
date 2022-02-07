package cenarios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import Malha.Malha;
import dominio.Custos;
import dominio.PTP;
import dominio.Trecho;
import dominio.Trem;

// Este é o cenário de conflito entre grupos de trens.

public class ScenarioII_2 {

	public static void resolver(Malha malha, ArrayList<Trem> N, ArrayList<Trem> S) {
		//int ek, int ek1) {

		int custoN = 0;
		int custoS = 0;
		int tempoN=0, tempoS=0;
	
		Trem.iniciaSimulacao(N);
		Trem.iniciaSimulacao(S);
				
		
		//// Todos os trens em N passam sem parar.

		// A última estação em S:
		int fim = S.get(S.size()-1).getPosicao();
		System.out.println("fim: " + fim);

		
		// Simula cada trem de N até fim, ocupando os trechos.  Isso vai 
		// fazer com que os trens em S fiquem parados:
		for (int i=0; i<N.size(); i++) {
			
			Trem tn = N.get(i);
			LocalDateTime hpos = tn.getHorario();
			int pos = tn.getPosicao();
			
			while (pos != fim && pos != -1) {
				
				int prox = malha.nextStation(pos, fim);
				LocalDateTime hprox = hpos.plusMinutes(tn.tempoAteEstacao(prox, malha));
				
				System.out.println("--->n " + tn.getNome() + " pos " + pos + " prox " + prox);
				
				// Se o trecho estiver ocupado, tn deve esperar.
				Trecho t = malha.trechoEstaOcupado(pos, prox, hpos, hprox);
				while (t != null) {
					hpos = t.getHf();
					hprox = hpos.plusMinutes(tn.tempoAteEstacao(prox, malha));
					
					PTP ptp = new PTP(pos,0,prox,hpos);
					tn.addPTP(ptp);
					tempoS += tn.tempoAteEstacao(prox, malha);
					
					t = malha.trechoEstaOcupado(pos, prox, hpos, hprox);
				}
				
				PTP ptp = new PTP(prox,0,malha.nextStation(prox,tn.getDestino()),hprox);
				tn.addPTP(ptp);
				malha.entrarTrecho(tn, pos, prox, hpos, hprox);
				
				pos = prox;
				hpos = hprox;
			}
				
		}
	
		// Faz a conta do custo com norte passando.
		for (int j = 0; j < S.size(); j++) {
			// atualiza PTP

			Trem t = S.get(j);
			PTP ptp = t.getPTP();
			//Custos para t
			Custos custos = t.getCustosDoTrem();	

			custoS += tempoS * t.getCustosDoTrem(ptp.getPosicao());

			

		}
		
		
		
		
		Trem.terminaSimulacao(S,malha,fim);
		Trem.terminaSimulacao(N,malha,fim);
		
		
		Trem.iniciaSimulacao(N);
		Trem.iniciaSimulacao(S);

		//// Todos os trens em S passam sem parar.

		// A última estação em N:
		fim = N.get(N.size()-1).getPosicao();
		System.out.println("fim: " + fim);

		// Simula cada trem de S até fim, ocupando os trechos.  Isso vai 
		// fazer com que os trens em N fiquem parados:
		for (int i=0; i<S.size(); i++) {

			Trem ts = S.get(i);
			LocalDateTime hpos = ts.getHorario();
			int pos = ts.getPosicao();

			while (pos != fim && pos != -1) {

				int prox = malha.nextStation(pos, fim);
				LocalDateTime hprox = hpos.plusMinutes(ts.tempoAteEstacao(prox, malha));

				System.out.println("--->s pos " + pos + " prox " + prox);

				// Se o trecho estiver ocupado, tn deve esperar.
				Trecho t = malha.trechoEstaOcupado(pos, prox, hpos, hprox);
				while (t != null) {
					hpos = t.getHf();
					hprox = hpos.plusMinutes(ts.tempoAteEstacao(prox, malha));

					PTP ptp = new PTP(pos,0,prox,hpos);
					ts.addPTP(ptp);
					t = malha.trechoEstaOcupado(pos, prox, hpos, hprox);
					tempoN += ts.tempoAteEstacao(prox, malha);

				}

				PTP ptp = new PTP(prox,0,malha.nextStation(prox,ts.getDestino()),hprox);
				ts.addPTP(ptp);
				malha.entrarTrecho(ts, pos, prox, hpos, hprox);

				pos = prox;
				hpos = hprox;
			}
		}	


		// Faz a conta do custo com norte passando.
//				
		for (int j = 0; j < N.size(); j++) {
			// atualiza PTP

			Trem t = N.get(j);
			PTP ptp = t.getPTP();
			//Custos para t
			Custos custos = t.getCustosDoTrem();	

			custoN += tempoN * t.getCustosDoTrem(ptp.getPosicao());

			

		}
		Trem.terminaSimulacao(S,malha,fim);
		Trem.terminaSimulacao(N,malha,fim);
		
		//====================================================================================================================
		//====================================================================================================================
		//====================================================================================================================
		System.out.println("\n>Resolve os dois grupos");

		System.out.println(" S = "+ custoS + " N = " + custoN);


		if (custoN <= custoS) {

			//// Todos os trens em N passam sem parar.

			// A última estação em S:
			fim = S.get(S.size()-1).getPosicao();
			System.out.println("fim: " + fim);

			// Movimenta cada trem de N até fim, ocupando os trechos.  Isso vai 
			// fazer com que os trens em S fiquem parados:
			for (int i=0; i<N.size(); i++) {

				Trem tn = N.get(i);
				LocalDateTime hpos = tn.getHorario();
				int pos = tn.getPosicao();

				while (pos != fim && pos != -1) {

					int prox = malha.nextStation(pos, fim);
					LocalDateTime hprox = hpos.plusMinutes(tn.tempoAteEstacao(prox, malha));

					System.out.println("--->n " + tn.getNome() + " pos " + pos + " prox " + prox);

					// Se o trecho estiver ocupado, tn deve esperar.
					Trecho t = malha.trechoEstaOcupado(pos, prox, hpos, hprox);
					while (t != null) {
						hpos = t.getHf();
						hprox = hpos.plusMinutes(tn.tempoAteEstacao(prox, malha));

						PTP ptp = new PTP(pos,0,prox,hpos);
						tn.addPTP(ptp);

						t = malha.trechoEstaOcupado(pos, prox, hpos, hprox);
					}

					PTP ptp = new PTP(prox,0,malha.nextStation(prox,tn.getDestino()),hprox);
					tn.addPTP(ptp);
					malha.sairEstacao(tn, pos);
					malha.entrarTrecho(tn, pos, prox, hpos, hprox);

					pos = prox;
					hpos = hprox;
				}
			}	
		}
		else {
			//// Todos os trens em S passam sem parar.

			// A última estação em N:
			fim = N.get(N.size()-1).getPosicao();
			System.out.println("fim: " + fim);

			// Movimenta cada trem de S até fim, ocupando os trechos.  Isso vai 
			// fazer com que os trens em N fiquem parados:
			for (int i=0; i<S.size(); i++) {

				Trem ts = S.get(i);
				LocalDateTime hpos = ts.getHorario();
				int pos = ts.getPosicao();

				while (pos != fim && pos != -1) {

					int prox = malha.nextStation(pos, fim);
					LocalDateTime hprox = hpos.plusMinutes(ts.tempoAteEstacao(prox, malha));

					System.out.println("--->s pos " + pos + " prox " + prox);

					// Se o trecho estiver ocupado, tn deve esperar.
					Trecho t = malha.trechoEstaOcupado(pos, prox, hpos, hprox);
					while (t != null) {
						hpos = t.getHf();
						hprox = hpos.plusMinutes(ts.tempoAteEstacao(prox, malha));

						PTP ptp = new PTP(pos,0,prox,hpos);
						ts.addPTP(ptp);
						t = malha.trechoEstaOcupado(pos, prox, hpos, hprox);
					}

					PTP ptp = new PTP(prox,0,malha.nextStation(prox,ts.getDestino()),hprox);
					ts.addPTP(ptp);
					malha.sairEstacao(ts, pos);
					malha.entrarTrecho(ts, pos, prox, hpos, hprox);

					pos = prox;
					hpos = hprox;
				}
			}	

		}
	}



	/*
	 *  Retorna o trem com maior horário de chegada na próxima estação.
	 */
	public static LocalDateTime maiorHorarioChegada(Malha malha, ArrayList<Trem> T) {

		Trem tmax = T.get(0);
		PTP ptpmax = tmax.getPTP();
		LocalDateTime hmax = ptpmax.getHorario().plusMinutes(tmax.tempoAteEstacao(ptpmax.getProxima(), malha));

		for (int i = 1; i < T.size(); i++) {

			Trem t = T.get(i);
			PTP ptp = t.getPTP();
			LocalDateTime h = ptp.getHorario().plusMinutes(t.tempoAteEstacao(ptp.getProxima(), malha));

			if (h.compareTo(hmax) > 0) {
				tmax = t;
				hmax = h;
			}
		}

		return hmax;
	}



	/*	
		Dada uma lista C de trens que estão na mesma estação O indo para a
		masma estação E, testa se algum trem de C entra em conflito com um
		trem que sai de E e vai para O.

		Devolve null se não há conflito ou o trem com menor horário no ptp que entra em conflito.
	 */
	public static Trem verificaConflitos(Malha M, Vector<Trem> T, ArrayList<Trem> C) {

		//Collections.sort(C);

		for (int i = 0; i < C.size(); i++) {

			Trem tc = C.get(i);
			int posicaoc = tc.getPosicao();
			int proximac = tc.getProxima();

			for (int j = 0; j < T.size(); j++) {

				if (i == j)
					continue;

				Trem tj = T.get(j);

				if (proximac == tj.getPosicao() && posicaoc == tj.getProxima()) {

					LocalDateTime hc = tc.getHorario();
					LocalDateTime ht = tj.getHorario();

					if (Utils.intersecao(hc, hc.plusMinutes(tc.tempoAteEstacao(proximac, M)),
							ht, ht.plusMinutes(tj.tempoAteEstacao(tj.getProxima(), M)))) {
						return tj;
					}
				}
			}
		}

		return null;
	}


	// O trecho que vai ter o conflito analisado é e1-e2.  O incumbente vai chegar em e1.
	// Constrói uma lista com todos os trens que vão chegar na estação e2 que não
	// venham da estação e1 no intervalo [hi,hf[.

	public static ArrayList<Trem> 
	trensOpostos(Malha M, Vector<Trem> T, int e1, int e2, LocalDateTime hi, LocalDateTime hf) {

		ArrayList<Trem> C = new ArrayList<Trem>();

		for (int i = 0; i < T.size(); i++) {

			Trem t = T.get(i);
			PTP ptp = t.getPTP();

			int proxima = ptp.getProxima();

			if (proxima == -1)
				continue;

			int penultima = t.getPenultimaPosicao();	


			if (proxima == e2 && ptp.getPosicao()!=e1) { 
				// e1= estacao posicao incumbente 
				//penultima 
				//e1 :posicao atual
				//e2 :pr
				// [hi:00:00:00, hf:12:00:00] compara o intervalo com h
				LocalDateTime h = ptp.getHorario().plusMinutes(t.tempoAteEstacao(proxima, M));
				if (h.compareTo(hi) >= 0 && h.compareTo(hf) <= 0) {
					C.add(t);
					System.out.println("C=====>  "+C);}
			}
		}

		return C;
	}

	public static ArrayList<Trem> 
	trensMsmSentido(Malha M, Vector<Trem> T, int e1, int e2, LocalDateTime hi, LocalDateTime hf) {

		ArrayList<Trem> C = new ArrayList<Trem>();

		for (int i = 0; i < T.size(); i++) {

			Trem t = T.get(i);
			PTP ptp = t.getPTP();

			int proxima = ptp.getProxima();

			if (proxima == -1)
				continue;

			int posicao = t.getPosicao();


			if (proxima == e2 && posicao==e1) { 

				LocalDateTime h = ptp.getHorario().plusMinutes(t.tempoAteEstacao(proxima, M));
				if (h.compareTo(hi) >= 0 && h.compareTo(hf) <= 0) {
					C.add(t);
					System.out.println("D=====>  "+C);}
			}
		}

		return C;
	}


	public static void atualiza(ArrayList<Trem> lista, Trem tultimo, Malha M) {

		for (int j = 0; j < lista.size(); j++) {
			// atualiza PTP
			Trem t = lista.get(j);
			PTP ptp = t.getPTP();

			LocalDateTime h = ptp.getHorario().plusMinutes(tultimo.tempoAteEstacao(ptp.getPosicao(), M));

			int p1 = ptp.getProxima(); // k + 1
			int p2 = M.nextStation(p1, t.getDestino()); // k 
			Trecho trensnoTrecho = M.trechoEstaOcupado(p1,	p2, ptp.getHorario(), h);
			if (trensnoTrecho == null) {

				// Testa se o trem está na estação de origem ou se o último PTP foi ficar parado:
				if ((t.getPercursoDoTrem().get(0).getPosicao() == p1) ||
						(t.getPercursoDoTrem().size() > 1 && t.getPrevPTP().getPosicao() == p1)) {	

					M.entrarTrecho(t, ptp.getPosicao(),	p1, ptp.getHorario(), h);			

				}
				else {
					M.entrarEstacao(t, ptp.getPosicao());
				}

				PTP p = new PTP(ptp.getPosicao(), 0, ptp.getProxima(), h);
				t.addPTP(p);
				M.entrarTrecho(t, ptp.getPosicao(),	p1, ptp.getHorario(), h);		
			}
		}
	}




	//	public static int calculaCusto(ArrayList<Trem> lista, Trem tultimo, Malha M) {
	//
	//		int tempoTotal=0;
	//
	//		for (int j = 0; j < lista.size(); j++) {
	//			// atualiza PTP
	//			Trem t = lista.get(j);
	//			PTP ptp = t.getPTP();
	//
	//			tempoTotal+= tultimo.tempoAteEstacao(ptp.getProxima(), M);
	//
	//		}
	//		return tempoTotal;
	//	}
	//	
	//	public static int calculaCusto(Trem t1, int tempo) {
	//		int custoTotal = 0;
	//		// custoParada custoManterParadoNaEstaçãoPorMinuto default duplas(Estacao,Custo)
	//		
	//		//Custos para t1
	//		Custos custos = t1.getCustosDoTrem();		
	//		
	//		
	//		// custo para parar o trem + custo do trem parado + custo do Trem 
	//		custoTotal = t1.getCusto()+tempo*custos.getCustoParado();
	//
	//		return custoTotal;
	//		
	//	}
	
	
	public static int novoCusto(ArrayList<Trem> lista, Trem ultimo, Malha m) {

		int custoTotal=0;
		int tultimo;
		PTP ptp;
		int tempoParado=0;

		// Custo total de uma lista S ou N
		// tempo que ultimo trem do outro lado leva para atravessar (tultimo)
		ptp = ultimo.getPTP();
		tultimo= ultimo.tempoAteEstacao(ptp.getProxima(), m);

		// para cada trem da lista calcular o tempo ate a proxima estaçao e somar no custo do proximo
		for (int j = 0; j < lista.size(); j++) {
			// atualiza PTP

			Trem t = lista.get(j);
			ptp = t.getPTP();
			//Custos para t
			Custos custos = t.getCustosDoTrem();	

			custoTotal += tultimo * t.getCustosDoTrem(ptp.getPosicao());

			tultimo += t.tempoAteEstacao(ptp.getProxima(), m);

		}

		return custoTotal;

	}
}


//		public static ArrayList<Trem> trensOpostos(int atual1, int proxima1, LocalDateTime ha_t1, LocalDateTime hc_t1_k,
//				Vector<Trem> T, Trem t1, Malha M) {
//
//			// incumbente
//			//          PTP ptp1 = t1.getPTP();
//			//          int atual1 = ptp1.getPosicao();
//			//          int proxima1 = ptp1.getProxima();
//			//          LocalDateTime ha_t1 = ptp1.getHorario(); //horario de t1
//			//          LocalDateTime hc_t1_k = ha_t1.plusMinutes(t1.tempoAteEstacao(proxima1, M)); //horario que t1 chega em k+1
//
//			ArrayList<Trem> conflpot = new ArrayList<>();
//
//			for (int i = 0; i < T.size(); i++) {
//				Trem t = T.get(i);
//				PTP ptp = t.getPTP();
//				int atual = ptp.getPosicao();
//				int proxima = ptp.getProxima();
//
//				LocalDateTime hoposto = ptp.getHorario();
//				LocalDateTime hopostok = hoposto.plusMinutes(t.tempoAteEstacao(proxima, M));// trem que chega em k+1 no
//				// intervalo de t1 ir de k para
//				// k+1
//
//				if (t.equals(t1))
//					continue;
//
//				if ((hopostok.compareTo(ha_t1) <= 1) && (hopostok.compareTo(hc_t1_k) >= 1)) {
//					if ((proxima == proxima1) && (atual != atual1)) {
//						System.out.println("adding ... ");
//						conflpot.add(t);
//					}
//
//				}
//
//			}
//			return conflpot;
//		}

