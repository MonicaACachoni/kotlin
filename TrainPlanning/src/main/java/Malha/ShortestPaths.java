package Malha;

public class ShortestPaths {

	private static int[] costs;
	private static int[] pi;

	private int[][] Costs;
	private int[][] Pi;
	
	
	public void floyd(Graph G) {

		int n = G.getN();
		int i, j, k;

		Costs = new int[n][n];
		Pi = new int[n][n];

		for (i = 0; i < n; i++) {
			for (j = 0; j < n; j++) {
				Costs[i][j] = Integer.MAX_VALUE/2-1;
				Pi[i][j] = -1;
			}
			Costs[i][i] = 0;
		}

		for (i = 0; i < n; i++) {
			for (int index = 0; index < G.getVertex(i).Neighbors.size(); index++) {
				Edge ij = G.getVertex(i).Neighbors.get(index);
				j = ij.getTerminal();
				Costs[i][j] = ij.getLength();
				Pi[i][j] = i;
			}
		}

		/*
		for (i = 0; i < n; i++) {
			System.out.printf("%d\t", i);
			for (j = 0; j < n; j++) {
				System.out.printf("%s ", Costs[i][j] == Integer.MAX_VALUE/2-1 ? -1 : Costs[i][j]);
			}
			System.out.printf("\n");
		}
		*/

		for (k = 0; k < n; k++) {
			for (i = 0; i < n; i++) {
				for (j = 0; j < n; j++) {
					if (Costs[i][j] > Costs[i][k] + Costs[k][j]) {
						Costs[i][j] = Costs[i][k] + Costs[k][j];
						Pi[i][j] = Pi[k][j];
					}
				}
			}
		}
		
		/*
		for (i = 0; i < n; i++) {
			System.out.printf("%d\t", i);
			for (j = 0; j < n; j++) {
				System.out.printf("%s ", Costs[i][j] == Integer.MAX_VALUE/2-1 ? -1 : Costs[i][j]);
			}
			System.out.printf("\n");
		}
		
		System.out.printf("\n");
		for (i = 0; i < n; i++) {
			System.out.printf("%d\t", i);
			for (j = 0; j < n; j++) {
				System.out.printf("%s ", Pi[i][j]);
			}
			System.out.printf("\n");
		}
		*/
	}

	
	
	
	public void dijkstra(Graph g, int s) {

		// Marks:
		// 0 = not reached, -1 = finished, >0 = reached, holds sqrt to t.

		int n = g.getN();

		pi = new int[n];

		for (int i = 0; i < n; i++)
			pi[i] = -1;

		costs = new int[n];
		Heap h = new Heap(n);

		for (int i = 0; i < n; i++) {
			h.insert(i, Integer.MAX_VALUE);
			costs[i] = Integer.MAX_VALUE;
		}

		h.decreaseCost(s, 0);
		costs[s] = 0;

		while (h.size() > 0) {

			int u = h.deleteMin();

			System.out.printf("u=%d\n", u);

			for (int index = 0; index < g.getVertex(u).Neighbors.size(); index++) {

				Edge uv = g.getVertex(u).Neighbors.get(index);
				int v = uv.getTerminal();

				System.out.printf("  v=%d\n", v);

				if (h.exists(v)) {
					int cost = costs[u] + uv.getLength();

					System.out.printf("  cost=%d\n", cost);

					if (cost < costs[v]) {
						pi[v] = u;
						costs[v] = cost;
						h.decreaseCost(v, cost);
					}
				} else {
					System.out.printf("  nao existe\n");
				}
			}
		}
	}

	public int[][] getCosts() {
		return Costs;
	}

	public int[][] getPi() {
		return Pi;
	}

	public int[] getcosts() {
		return costs;
	}

	public int[] getpi() {
		return pi;
	}

}