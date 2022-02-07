package Malha;

import java.util.ArrayList;
import java.util.List;

class Graph {

	private int n;
	private int m;

	private Vertex[] vtcs;



	/********************************************************************************
     Build an instance of a graph. 

     @param n The number of vertices to be inserted (later) in the graph.

     @param m The number of pairs of vertices to be connected (later) in the
     graph, counted as follows:

     If edges (u,v) and (v,u) are both going to be inserted, add 1 to m. 
     If edge (u,v) is going to be inserted (but not (v,u)), also add 1 to m.
     For parallel edges use the same rule as long as its length differentiate
     them.
	 ********************************************************************************/
	public Graph(int n) {

		vtcs = new Vertex[n];

		for (int i=0; i<n; i++)
			vtcs[i] = new Vertex();

		this.n = n;
		this.m = 0;

	}



	/********************************************************************************
     Add edge uv and its attributes to the graph, for vertex indices u and v.
     Return the index of the new edge.  Loops are skipped in which case the
     return is -1.
	 ********************************************************************************/
	void addEdge(int u, int v, int length) {

		// Skips loops:
		if (u == v)
			return;

		vtcs[u].Neighbors.add(new Edge(v,length));
		vtcs[v].Neighbors.add(new Edge(u,length));
		m++;
	}



	public int getN() {
		return n;
	}



	public int getM() {
		return m;
	}



	public ArrayList<Integer> getNeighbors(int u) {

		ArrayList<Integer> N = new ArrayList<Integer>();
		
		for (int index=0; index < this.getVertex(u).Neighbors.size() ; index++) {
			Edge uv = getVertex(u).Neighbors.get(index);
			int v = uv.getTerminal();
			N.add(v);
		}

		return N;
	}



	/********************************************************************************
     Return the vertex whose index is u.
	 ********************************************************************************/
	public Vertex getVertex(int u) {
		return vtcs[u];
	}


	/********************************************************************************
	 ********************************************************************************/
	public void print() {

		System.out.printf("Vertices %d\n",n);
		System.out.printf("Edges %d\n\n",m);

		for (int u=0; u<n; u++) {
			System.out.printf("Vertice %d: ",u);

			for (int index=0; index < this.getVertex(u).Neighbors.size() ; index++) {

				Edge uv = getVertex(u).Neighbors.get(index);
				int v = uv.getTerminal();

				System.out.printf("[(%d,%d) %d]  ",u,v,uv.getLength());
			}
			System.out.printf("\n");
		}
	}


}

