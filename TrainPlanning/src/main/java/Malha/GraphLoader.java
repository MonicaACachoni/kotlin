package Malha;

import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;
import java.util.Locale;
import java.io.IOException;

public class GraphLoader {

	/*
	 * Loads a graph from a file, collapsing vertices bufferRadius apart. A maximum
	 * of maxRouteEndpoints source and destination points will be allowed during
	 * routing. The time in seconds spent running TSP is tspRunBy.
	 * 
	 * gridDim is the dimension of the preprocessing grid. If it is greater than
	 * zero, run preprocessing steps to speed up routes construction.
	 * 
	 * If fileout is not null, serializes the graph what allows using reload();
	 */

	public static Graph loadFromFile(String filein) throws IOException {

		Locale.setDefault(Locale.US);
		Scanner s = new Scanner(new BufferedReader(new FileReader(filein)));

		int len; // distancia entre duas estacoes (u,v)
		int u, v; // vertices

		int nvertices = s.nextInt(); // quantidade de vertices
		Graph g = new Graph(nvertices);

		while (s.hasNext()) {
			u = (int) s.nextDouble();
			v = (int) s.nextDouble();
			len = (int) (s.nextDouble());

			//malha.novoTrecho(u, v);

			g.addEdge(u, v, len);
		}

		s.close();

		return g;
	}
}
