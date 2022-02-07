package Malha;

import java.io.IOException;

class Main {
	
	
	
	
	public static void main(String[] args) throws IOException {

		//Malha M = new Malha(args[0]);
//		int n = M.getN();
//
//		
//		for (int s = 0; s < n; s++) {
//			  for (int t = 0; t < n; t++) {
//				  System.out.printf("next no caminho de %d para %d: %d\n", s,t,M.nextStation(s, t));
//				  
//			  }
//		}
	}
	
	
	public static void main2(String[] args) {

		Graph G = null;

		try {
			G = GraphLoader.loadFromFile(args[0]);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		
		ShortestPaths sp = new ShortestPaths();

		sp.floyd(G);

		int Costs[][] = sp.getCosts();
		int Pi[][] = sp.getPi();

		int n = G.getN();

		
		for (int s = 0; s < n; s++) {
		  for (int t = 0; t < n; t++) {

			System.out.printf("path %d %d with cost %d: ", s, t, Costs[s][t]);

			int k = t;

			while (k != s && k != -1) {
				System.out.printf("%d ", Pi[s][k]);
				k = Pi[s][k];
			}
			System.out.printf("\n");
		}
	}
	}
	
	
/*
	ShortestPaths sp = new ShortestPaths();

		int s = 1;

		ShortestPaths.dijkstra(G, s);

		int costs[] = sp.getCosts();
		int pi[] = sp.getPi();

		int n = G.getN();

		for (int t = 0; t < n; t++) {

			System.out.printf("path %d %d with cost %d: ", s, t, costs[t]);

			int k = t;

			while (k != -1) {
				System.out.printf("%d ", pi[k]);
				k = pi[k];
			}
			System.out.printf("\n");
		}
	}
*/
}
