package dominio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Malha.Malha;

public class Trem implements Comparable<Trem> {

	// public static int headway = 60;

	private String nome;
	private int destino; 
	private Custos custosDoTrem;
	private int velocidade;
	private int rank; // uma variável para garantir que um trem sempre vai ter sua chance de ser incumbente.
	private PTP ptp;
	

	private ArrayList<PTP> percursoDoTrem = new ArrayList<PTP>();
	private int tamPercurso;
	
	
	public void setPercursoDoTrem(ArrayList<PTP> percursoDoTrem) {
		this.percursoDoTrem = percursoDoTrem;
	}


	private ArrayList<Meeting> tie = new ArrayList<Meeting>();

	
	public static void iniciaSimulacao(ArrayList<Trem> s) {
	
		for (int i = 0; i < s.size(); i++) {
			Trem t = s.get(i);
			t.iniciaSimulacao();
		}
		
	}
	

	private void iniciaSimulacao() {
		this.tamPercurso = percursoDoTrem.size();
	}


	public static void terminaSimulacao(ArrayList<Trem> T, Malha malha, int fim) {
		
		for (int i = 0; i < T.size(); i++) {
			Trem t = T.get(i);
			malha.removerUltimoTrecho(t);
			malha.removerUltimoTrecho(t);
			t.terminaSimulacao();
			
			
		}
		
	}

	


	private void terminaSimulacao() {
		
		int r = percursoDoTrem.size() - this.tamPercurso;
		
		for (int i=0; i<r; i++) {
			//percursoDoTrem.remove(percursoDoTrem.size()-1);
			removeUltimoPTP();
		}
			

	}


	public static Vector<Trem> loadFromFile(String arquivo, Malha M, String custos) throws IOException {

		Vector<Trem> T = new Vector<Trem>();

		/*
		 * Formato do arquivo de trens: um trem por linha, com os seguintes campos
		 * separados por espaço (8 campos):
		 *
		 * nome 
		 * trecho-origem-estacao-A 
		 * trecho-origem-estacao-B 
		 * distancia-estacao-A
		 * estacao-destino 
		 * horario-de-saida-localDateTime 
		 * velocidade-metro-por-minuto
		 * custo-por-unidade-de-tempo-parado
		 * 
		 * Assume-se que o trem *sempre* se move na direção estacao-A -> estacao-B.
		 * 
		 * Toda linha que começa com # não será considerada.
		 */

		//		Trem x = new Trem("a",1,1,1,1,null,1,1);
		//		Trem z = new Trem("z",1,1,1,1,null,1,1);
		//		
		//		M.entrarTrecho(x, 1, 2, LocalDateTime.parse("2020-01-01T12:00:00"), LocalDateTime.parse("2020-01-01T12:00:00"));
		//		M.entrarTrecho(x, 1, 2, LocalDateTime.parse("2020-01-01T01:00:00"), LocalDateTime.parse("2020-01-01T01:00:00"));
		//		M.entrarTrecho(x, 1, 2, LocalDateTime.parse("2020-01-01T06:00:00"), LocalDateTime.parse("2020-01-01T06:00:00"));
		//		M.entrarTrecho(z, 1, 2, LocalDateTime.parse("2020-01-01T06:00:00"), LocalDateTime.parse("2020-01-01T06:00:00"));
		//		M.entrarTrecho(z, 1, 2, LocalDateTime.parse("2020-01-01T00:59:00"), LocalDateTime.parse("2020-01-01T00:59:00"));
		//		
		//		M.imprimeOcupacaoTrechos();
		//		
		//		System.exit(0);


		Scanner s = new Scanner(new BufferedReader(new FileReader(arquivo)));

		while (s.hasNext()) {

			String nome = s.next();

			//Carrega o custo dos trens
			Custos cT = Trem.loadCostsFromFile(custos,nome);

			Trem t = new Trem(nome, (int) s.nextInt(), (int) s.nextInt(), (int) s.nextInt(), (int) s.nextInt(),
					LocalDateTime.parse(s.next()), (int) s.nextInt(), (int) s.nextInt(),cT);

			T.add(t);
			PTP ptp = t.getPTP();

			if (ptp.getDistancia() == 0)
				M.entrarEstacao(t, ptp.getPosicao());

			//else {
			//	LocalDateTime h = p.getHorario().plusMinutes(t.tempoAteEstacao(t.getPTP().getProxima(),M));
			//	M.entrarTrecho(t, p.getPosicao(), p.getProxima(), p.getHorario(), h);
			//}
		}

		s.close();

		return T;
	}

	public static Custos loadCostsFromFile(String arquivo, String nome) throws IOException {

		// nomeTrem custoParaParar custoRetrocessoPorDistancia custoManterParadoNaEstaçãoPorMinuto custoManterParadoEspecificoPorEstaçaPorMinuto-duplas(Estacao,Custo)
		
		Custos custosDoTrem ;
		int iii=0;
		int custoDeParada = 0;
		int custoParado = 0;
		int custoDefault = 0;
		Hashtable<Integer,Integer> custoPorEstacao = new Hashtable<Integer,Integer>();

		File s = new File(arquivo);
		Scanner sc = new Scanner(s);
		String linha;
		while (sc.hasNextLine()) {
			linha = sc.nextLine();		   
			String[] tokens = linha.split(" ");
			System.out.println(iii);
			iii++;
			if (tokens[0].equals(nome)) {
				System.out.println(iii);
				iii++;
				custoDeParada= Integer.parseInt(tokens[1]);
				custoParado= Integer.parseInt(tokens[2]);
				custoDefault= Integer.parseInt(tokens[3]);
				for (int i=4; i<tokens.length; i++) {
					System.out.println(tokens[i]);
					String[] custoPrEst = tokens[i].split(",");
					for (int x=0; x<custoPrEst.length; x++) {
						custoPorEstacao.put(Integer.parseInt(custoPrEst[x]), Integer.parseInt(custoPrEst[x+1]));
						x++;
					}
				}
			}}
		custosDoTrem = new Custos(custoDeParada, custoParado, custoDefault, custoPorEstacao);

		sc.close();
		return custosDoTrem;
	}


	public Trem(String nome, int inicioTrecho, int fimTrecho, int distanciaInicioTrecho, 
			int destino, LocalDateTime saida, int velocidade, int custo, Custos custosDoTrem) {

		this.nome = nome;
		this.destino = destino;
		this.velocidade = velocidade;
		this.custosDoTrem=custosDoTrem;
		this.rank = 0;

		ptp = new PTP(inicioTrecho, distanciaInicioTrecho, fimTrecho, saida);
		addPTP(ptp);
	}

	public int compareTo(Trem that) {
		return this.getPTP().getHorario().compareTo(that.getPTP().getHorario());
	}


	public int getDestino() {
		return destino;
	}

	public String getNome() {
		return nome;
	}

	public ArrayList<PTP> getPercursoDoTrem() {
		return percursoDoTrem;
	}

	public int getPosicao() {
		return ptp.getPosicao();
	}


	public int getPenultimaPosicao() {

		int i = percursoDoTrem.size() - 1;
		int p = percursoDoTrem.get(i).getPosicao();
		i--;

		while (i >= 0) { 
			int pp = percursoDoTrem.get(i).getPosicao();
			if (pp != p) 
				return pp;
			i--;
		}

		return -1;
	}


	public int getProxima() {
		return ptp.getProxima();
	}

	public LocalDateTime getHorario() {
		return ptp.getHorario();
	}


	public double getDistancia() {
		return ptp.getDistancia();
	}


	public PTP getPrevPTP() {

		int s = percursoDoTrem.size();
		return s >= 2 ? percursoDoTrem.get(s-2) : null;
	}


	public PTP getPTP() {
		return ptp;
	}


	public ArrayList<Meeting> getTie() {
		return tie;
	}


	public int getVelocidade() {
		return velocidade;
	}




	public void addPTP(PTP p) {
		percursoDoTrem.add(p);
		ptp = p;
		//System.out.println("PTP ADICIONADO = "+this.nome + " pos: " + p.posicao + " dis: " + p.distancia + " prx: " + p.proxima + " hor: " + p.horario);
	}



	public void setTie(int i, Meeting t) {
		tie.add(i, t);
	}



	public int tempoAteEstacao(int destino, Malha M) {

		// if (destino == -1)
		// return 0;

		PTP p = ptp;

		int estacao = p.getPosicao();
		double distancia = p.getDistancia();

		int d = M.distancia(estacao, destino);

		return Math.abs((int) ((d - distancia) / velocidade));
	}


	public void removeUltimoPTP() {
		percursoDoTrem.remove(percursoDoTrem.size()-1);
		ptp=percursoDoTrem.get(percursoDoTrem.size()-1);
	}



	@Override
	public String toString() {

		//		for (int j = 0; j < getPercursoDoTrem().size(); j++) {
		//              System.out.println("PTP no. " + j + " Nome: " + nome + " Percurso= " + getPercursoDoTrem().get(j).posicao
		//                              + " distancia= " + getPercursoDoTrem().get(j).distancia + " proxima= "
		//                              + getPercursoDoTrem().get(j).proxima + " horario= " + getPercursoDoTrem().get(j).horario + "\n");
		//		}

		System.out.println(nome + " " + getPosicao());

		return nome;

	}


	public void increaseRank() {
		rank++;
	}

	public int getRank() {
		return rank;
	}

	public Integer getCustosDoTrem(int estacao) {
		if(custosDoTrem.getCustoPorEstacao(estacao)==null)
			return custosDoTrem.getCustoDefault();
		else
			return custosDoTrem.getCustoPorEstacao(estacao);
	}

	
	public Custos getCustosDoTrem() {
		return custosDoTrem;
	} 
	
	
	public void setCustosDoTrem(Custos custosDoTrem) {
		this.custosDoTrem = custosDoTrem;
	}



}
