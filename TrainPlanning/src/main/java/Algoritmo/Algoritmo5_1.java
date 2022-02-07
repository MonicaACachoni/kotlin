package Algoritmo;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Vector;

import org.jfree.ui.RefineryUtilities;

import Malha.Malha;
import cenarios.ScenarioII_1;
import cenarios.ScenarioII_2;
import cenarios.ScenarioII_4;
import cenarios.ScenarioII_5;
import cenarios.ScenarioII_6;
import cenarios.ScenarioI_2;
import cenarios.ScenarioI_3;
import cenarios.ScenarioI_4;
import dominio.AreaProibida;
import dominio.PTP;
import dominio.Trecho;
import dominio.Trem;
import gravaDados.Resultados;	

public class Algoritmo5_1 {

	public static void deadlock(Trem t) {

		System.out.println("Deadlock\n");
		System.out.println(t.getNome() + "\n");
		System.exit(1);
	}

	// arquivo-de-malha arquivo-de-estacoes arquivo-de-trens arquivo-de-encontros
	// C:\Users\Monica\Dropbox\workspaceMonica\TrainPlanning\src\main\java\entradasDoSistema\malha1.txt
	// C:\Users\Monica\Dropbox\workspaceMonica\TrainPlanning\src\main\java\entradasDoSistema\estacoes1.txt
	// C:\Users\Monica\Dropbox\workspaceMonica\TrainPlanning\src\main\java\entradasDoSistema\trem.txt
	// C:\Users\Monica\Dropbox\workspaceMonica\TrainPlanning\src\main\java\entradasDoSistema\meeting1.txt

	// 1
	// C:\Users\Monica\Dropbox\workspaceMonica\TrainPlanning\src\main\java\entradasDoSistema\malha1.txt
	// 2
	// C:\Users\Monica\Dropbox\workspaceMonica\TrainPlanning\src\main\java\entradasDoSistema\estacoes1.txt
	// 3
	// C:\Users\Monica\Dropbox\workspaceMonica\TrainPlanning\src\main\java\entradasDoSistema\trem.txt
	// 4
	// C:\Users\Monica\Dropbox\workspaceMonica\TrainPlanning\src\main\java\entradasDoSistema\meeting1.txt
	// 5
	// C:\Users\Monica\Dropbox\workspaceMonica\TrainPlanning\src\main\java\entradasDoSistema\areasProibidas.txt

	/*
	 *
	 * # Trem azul vai da estacao 5 para a 12, se move a 32 km-por-hora: azul 5 5 0
	 * 12 32 2018-06-15T09:44:05
	 *
	 * # Trem verde esta entre as estacoes 6 e 7 a 2349 unidade de distÃ¢ncia da 6 e
	 * vai para a 19 a 27 unidades de velocidade: verde 6 7 2349 19 27
	 * 2018-06-15T09:44:00
	 *
	 * meeting: nome-do-trem nome-do-trem estacao duracao-em-minutos
	 *
	 * meeting: azul verde 15 45
	 *
	 * 2007-12-03T10:15:30
	 */

	// arquivo-de-malha arquivo-de-estacoes arquivo-de-trens arquivo-de-encontros
	// C:\Users\Monica\Dropbox\workspaceMonica\TrainPlanning\src\main\java\entradasDoSistema\malha1.txt
	// C:\Users\Monica\Dropbox\workspaceMonica\TrainPlanning\src\main\java\entradasDoSistema\estacoes1.txt
	// C:\Users\Monica\Dropbox\workspaceMonica\TrainPlanning\src\main\java\entradasDoSistema\trem.txt
	// C:\Users\Monica\Dropbox\workspaceMonica\TrainPlanning\src\main\java\entradasDoSistema\meeting1.txt

	// 1
	// C:\Users\Monica\Dropbox\workspaceMonica\TrainPlanning\src\main\java\entradasDoSistema\malha1.txt
	// 2
	// C:\Users\Monica\Dropbox\workspaceMonica\TrainPlanning\src\main\java\entradasDoSistema\estacoes1.txt
	// 3
	// C:\Users\Monica\Dropbox\workspaceMonica\TrainPlanning\src\main\java\entradasDoSistema\trem.txt
	// 4
	// C:\Users\Monica\Dropbox\workspaceMonica\TrainPlanning\src\main\java\entradasDoSistema\meeting1.txt
	// 5
	// C:\Users\Monica\Dropbox\workspaceMonica\TrainPlanning\src\main\java\entradasDoSistema\areasProibidas.txt

	public static void main(String[] args) throws IOException {

		// Carrega a malha:
		// Malha M = new Malha(args[0], args[1]);

		long startTime, endTimeN;

		startTime = medeTempo.getCpuTime();

		File path = new File("./src/main/java/entradasDoSistema");
		String classPath = path.getAbsolutePath();
		// classPath.replaceAll("/", "\\");

		String tipo = args[0];

		// Carrega a malha:
		String malha = path.toString() + "/" + tipo + "-malha.txt"; // classPath + "\\" + tipo + "-malha.txt";
		String estacao = path.toString() + "/" + tipo + "-estacoes.txt"; // classPath + "\\" + tipo + "-estacoes.txt";
	
		// Carrega custos dos trens
		String custos = path.toString() + "/" + tipo + "-custos.txt"; // classPath + "\\" + tipo + "-custos.txt";

		Malha M = new Malha(malha, estacao);


		/*
		 * // Carrega os encontros planejados:
		 *
		 * s = new Scanner(new BufferedReader(new FileReader(args[3])));
		 *
		 * while (s.hasNext()) { String nome1 = s.next(); String nome2 = s.next(); Trem
		 * t1 = null, t2 = null, t;
		 *
		 * for (int i = 0; i < T.size(); i++) { t = T.get(i); if
		 * (nome1.equals(t.getNome())) { t1 = t; } if (nome2.equals(t.getNome())) { t2 =
		 * t; } if (t1 != null && t2 != null) { Meeting tie = new Meeting(t1, t2,
		 * s.nextInt(), LocalDateTime.parse(s.next()), LocalDateTime.parse(s.next()));
		 * t1.setTie(t1.getTie().size(), tie); t1 = null; t2 = null; } }
		 *
		 * }
		 *
		 * s.close();
		 */

		// Carrega as áreas proibidas:
		String areaP = path.toString() + "/" + "areasProibidas.txt"; // classPath + "\\" + "areasProibidas.txt";
		Vector<AreaProibida> areasProibidas = AreaProibida.loadFromFile(areaP);

		// Carrega os trens:
		String trensS = path.toString() + "/" + tipo + "-trens.txt"; // classPath + "\\" + tipo + "-trens.txt";
		Vector<Trem> T = Trem.loadFromFile(trensS, M,custos);


		// Trens finalizados ficam em outra lista:
		Vector<Trem> finalizados = new Vector<Trem>();

		//// ALGORITMO 5.1
		// Fase I - Enquanto todos os trens não estiverem em estações

		// cenario de area proibida nao pode checar apenas o proximo trecho, ele precisa checar toda vez antes de andar se o trecho é proibido, 
		// por isso sera adicionado tbm ao final do algoritmo
		//		ScenarioI_1.realizar(M, T, areasProibidas); 

		Trem min = maisAntigoForaEstacao(T);

		while (min != null) {

			if (ScenarioI_2.realizar(M, T, min))
				;
			else if (ScenarioI_3.realizar(M, T))
				;
			else
				ScenarioI_4.realizar(M, T, min, finalizados);

			min = maisAntigoForaEstacao(T);
		}


		System.out.println("\n-------------Fim da Fase I-------------------------\n");


//			for (int i=T.size()-1; i>=0; i--) {
//				Trem ti = T.get(i);
//				T.remove(ti);
//				finalizados.add(ti);
//			}


		// Fase II - Enquanto todos os trens nao estiverem no destino

		Trem incumbente = null;
		
		while (T.size() != 0) {

			boolean flag = true;

			System.out.printf("\n");
			System.out.printf("-------iteração------------------------------------------\n");
			System.out.printf("%d trens\n",T.size());

			for (int i = 0; i < T.size(); i++) {
				Trem x = T.get(i);
				System.out.printf("trem %d %10s atual %d prox %d hora %s\n",i,x.getNome(),x.getPosicao(),x.getProxima(),x.getHorario());
			}

			System.out.printf("\n");			
			System.out.printf("Ocupação de estações\n");
			M.imprimeOcupacaoEstacoes();

			System.out.printf("\n");			
			System.out.printf("Ocupação de trechos\n");
			M.imprimeOcupacaoTrechos();


			System.out.println("");

			while (flag) {

				if (flag = ScenarioII_1.realizar(M, T)) 
					;
			//	else if (flag = ScenarioII_3.realizar(M,T))
				//	;	
			else if (flag = ScenarioII_4.realizar(M, T))
					;
				else 
					flag = ScenarioII_5.realizar(M, T);

			}	

			// Encontra o trem incumbente:
			Trem ti;
			
			if (incumbente != null)
				ti = incumbente;
			else 
				ti = incumbente(M,T);
			
			Trem tiSalvo = ti;
			
			
			System.out.println("Incumbente " + ti.getNome() + 
					" pos " + ti.getPosicao() + " prox " + ti.getProxima());

			System.out.println(">Verifica requisitos para [II.2]");
			
			flag = false;
/*
			// Cenário II.2.
			// Repete o Algoritmo 4.1 enquanto houver um trem incumbente:
			while (true) {

				//// Algoritmo 4.1
				// Encontra grupos de trens em conflito ou um novo incumbente:
				ArrayList<Trem> N = new ArrayList<Trem>();
				ArrayList<Trem> S = new ArrayList<Trem>();
				ArrayList<Trem> aux = new ArrayList<Trem>();

				int ek = ti.getPosicao();  // estação k
				if (ek == -1)
					break;

				int ek1 = ti.getProxima(); // estação k+1 ou k-1
				if (ek1 == -1) 
					break;

				//N.add(ti);

				// Se houver outros trens em ek que chegariam em ek1 durante o 
				// tempo de trajeto de ti, eles devem ser colocados em N:
								
				
				LocalDateTime hi = ti.getHorario();

				LocalDateTime tmk = hi;
				LocalDateTime tfk = hi; 
				LocalDateTime tmk1 = hi;
				LocalDateTime tfk1 = hi.plusMinutes(ti.tempoAteEstacao(ek1, M));

				// Repete enquanto trens forem adicionados a S e N sem encontrar conflito:
				while (true) {

					if (tmk1.compareTo(tfk1) == 0)
					{
						ti=null;
						break;
					}
					// Encontra os trens que vão chegar em k+/-1:
					aux = ScenarioII_2.trensOpostos(M,T,ek,ek1,tmk1,tfk1);

					// Não há trens para adicionar em S:
					if (aux.size() == 0) {
						ti = null;
						break;
					}

					// Há trens para adicionar em S:
					Trem conflito = ScenarioII_2.verificaConflitos(M, T, aux);
					
					// Há algum trem em conflito:
					if (conflito != null) {
						ti = conflito;			
						break;
					}

					tmk = tfk;
					tfk = ScenarioII_2.maiorHorarioChegada(M, aux);

					S.addAll(aux);


					//// Encontra os trens que vão chegar em k:
					aux = ScenarioII_2.trensMsmSentido(M,T,ek,ek1,tmk,tfk);

					// Não há trens para adicionar em N:
					if (aux.size() == 0) {
						ti = null;
						break;
					}

					// Há trens para adicionar em N:
					conflito = ScenarioII_2.verificaConflitos(M, T, aux);

					if (conflito != null) {
						ti = conflito;
						break;
					}

					tmk1 = tfk1;
					tfk1 = ScenarioII_2.maiorHorarioChegada(M, aux);

					N.addAll(aux);
				
				}
				// fim do Algoritmo 4.1

				// O algoritmo 4.1 encontrou algum trem em conflito, esse é o incumbente agora.
				if (ti != null)  {
					System.out.println(">Encontrou conflito, o novo incumbente é " + ti.getNome());
					incumbente = ti;
					break;
				}

				// O algoritmo 4.1 encontrou dois grupos S e N, resolve os conflitos e termina:
				if (S.size() > 0 && N.size() > 0) {
					System.out.println("\n> [II.2] Conflitos multiplos");
					System.out.println(">Encontrou dois grupos " + S.size() + " " + N.size());
						
//					System.out.printf("Grupo S\n");
//					for (int i=0; i<S.size(); i++) {
//						Trem z = S.get(i);
//						System.out.printf("  %s\n",z.getNome());
//					}
//					
//					System.out.printf("Grupo N\n");
//					for (int i=0; i<N.size(); i++) {
//						Trem z = N.get(i);
//						System.out.printf("  %s\n",z.getNome());
//					}					

					ScenarioII_2.resolver(M, N, S);
					flag = true;
					incumbente = null;
					break;
				}

				// O algoritmo 4.1 não encontrou grupos em conflito:
				System.out.println(">Não encontrou grupos");
				incumbente = null;
				break;
			}*/
			// fim do Cenário II.2	


			// Se não executou o Cenário II-2:
			if (flag == false) {

				ti = tiSalvo;

				if (ScenarioII_6.realizar(M, T, ti)) {
					//M.sairTrecho(ti);
				}

				else if (ti.getPTP().getProxima() == -1) {

					//System.out.println("\n> [II.Y] Remoção do sistema");
					//System.out.printf("incumbente %10s v %2d curr %d %s next %s\n",ti.getNome(),ti.getVelocidade(),ti.getPrevPTP().getPosicao(),ti.getPTP().getHorario(),-1);

				//	M.sairTrecho(ti);
					M.finalizartrem(ti);
					T.remove(ti);
					finalizados.add(ti);
				}

				else {

					//System.out.println("\n> [II.X] Movimento simples");

					// tem que checar se area nao é proibida antes de ir pra proxima
					//ScenarioI_1.realizar(M, T, areasProibidas); 

					// O incumbente se move para a próxima estação:
					PTP ptp = ti.getPTP();
					int posicao = ptp.getPosicao();
					int proxima = ptp.getProxima();
					int depois = M.nextStation(proxima, ti.getDestino()); 
					LocalDateTime hprox = ptp.getHorario().plusMinutes(ti.tempoAteEstacao(proxima, M));

					LocalDateTime hora = null;
					Trecho trecho = M.trechoEstaOcupado(posicao, proxima, ptp.getHorario(), hprox);

					//			System.out.printf("incumbente %10s est %d %s prox %d %s\n",ti.getNome(),ptp.getPosicao(),ptp.getHorario(),ptp.getProxima(),hprox);


					// Testa se o trem está na estação de origem ou se o último PTP foi ficar parado:
					if (ti.getPercursoDoTrem().size() == 1 ||
							(ti.getPercursoDoTrem().size() > 1 && ti.getPrevPTP().getPosicao() == posicao && ti.getPrevPTP().getDistancia()==0))
					{
						;
					}
					else
					{
						M.entrarEstacao(ti,ti.getPosicao());
					}
					
					if (trecho == null) {
						// Testa se o trem está na estação de origem ou se o último PTP foi ficar parado:
						if (ti.getPercursoDoTrem().size() == 1 ||
								(ti.getPercursoDoTrem().size() > 1 && ti.getPrevPTP().getPosicao() == posicao && ti.getPrevPTP().getDistancia()==0)) {
							M.sairEstacao(ti,posicao);
							M.entrarTrecho(ti, posicao,	proxima, ptp.getHorario(), hprox);							
						//M.entrarEstacao(ti,proxima);
						}
						else {
							M.entrarTrecho(ti, posicao,	proxima, ptp.getHorario(), hprox);
							M.sairEstacao(ti,posicao);
							//M.entrarEstacao(ti,proxima);
						}

						ti.addPTP(new PTP(proxima, 0, depois, hprox));
									System.out.printf("%s se move para %d\n",ti.getNome(),proxima);
					}
					else  {
						// Testa se o trem está na estação de origem ou se o último PTP foi ficar parado:
//						if (ti.getPercursoDoTrem().get(0).getPosicao() == posicao ||
//								(ti.getPercursoDoTrem().size() > 1 && ti.getPrevPTP().getPosicao() == posicao && ti.getPrevPTP().getDistancia()==0)) {
//							//				System.out.println("Oi3");
//							;
//						}
//						else {
//							System.out.println("Oi4");
//						
//							M.entrarEstacao(ti,posicao);
//						}

						hora = trecho.getHf().plusMinutes(1);

						// Se dois trens estão em uma estação com o mesmo tempo e vão cruzar, 
						// eles podem entrar em loop porque um ocupa o trecho do outro.  Adicionamos
						// um segundo para quebrar o empate:
						if (hora.compareTo(ti.getHorario()) == 0) {
							hora = hora.plusMinutes(1);
						}

						ti.addPTP(new PTP(posicao, 0, proxima, hora));

								System.out.printf("%s parado em %2d até %s\n",ti.getNome(),posicao,hora);
					}
				}
			}
		}


		endTimeN = medeTempo.getCpuTime();

		// Salva os resultados no txt
		Resultados.results(finalizados);
		//System.out.println(finalizados);

		/*
		final trainPlot demo = new trainPlot("Train Scheduling by Monica", finalizados, M);
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
		 */



		// // ==================================
		final Graph demo1 = new Graph("Train Planning", 1200, 900, 
				"Horário", "Posição", "Planning", "graph", finalizados, M);
		demo1.pack();
		RefineryUtilities.centerFrameOnScreen(demo1);
		demo1.setVisible(true);

		// ==================================
		// Gson gson = new Gson();
		//
		// // converte objetos Java para JSON e retorna JSON como String
		// String json = gson.toJson(Finalizados);

		//
		// try {
		// // Escreve Json convertido em arquivo chamado "file.json"
		// FileWriter writer = new FileWriter(
		// "C:/Users/Monica/Dropbox/workspaceMonica/TrainPlanning/src/main/java/Algoritmo/train.json");
		// writer.write(json);
		// writer.close();
		//
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		//
		// RunPython.runP();

		// main
	}



	private static Trem incumbente(Malha malha, Vector<Trem> T) {

		Trem tmin = T.get(0);
		LocalDateTime hmin = tmin.getHorario();

		for (int i = 1; i < T.size(); i++) {
			Trem ti = T.get(i);
			LocalDateTime hi = ti.getHorario();

			if (hi.compareTo(hmin) < 0) {
				tmin = ti;
				hmin = hi;
			}
			else if (hi.compareTo(hmin) == 0) {				
				if ((ti.getPosicao() == tmin.getPosicao() && malha.chegouAntes(ti,tmin,ti.getPosicao())) ||
				    (ti.getPosicao() != tmin.getPosicao() && ti.getRank() > tmin.getRank())) {
					tmin = ti;
					hmin = hi;
				}
			}
		}

		// Aumenta o rank dos trens com mesmo horário que o incumbente:
		for (int i = 0; i < T.size(); i++) {

			Trem ti = T.get(i);
			LocalDateTime hi = ti.getHorario();

			if (ti != tmin && hi.compareTo(hmin) == 0) {
				ti.increaseRank();
			}
		}

		return tmin;
	}




	// Retorna o trem fora de uma estação com menor horário no PTP ou
	// null se todos estão em alguma estação.
	private static Trem maisAntigoForaEstacao(Vector<Trem> trens) {

		Trem tmin = null;
		LocalDateTime hmin = null;

		for (int i = 0; i < trens.size(); i++) {

			Trem t = trens.get(i);
			PTP ptp = t.getPTP();
			LocalDateTime h = ptp.getHorario();

			if (ptp.getDistancia() != 0) {
				if (tmin == null || h.compareTo(hmin) < 0) {
					tmin = t;
					hmin = tmin.getPTP().getHorario();
				}
			}
		}

		return tmin;
	}
}
