package Malha;

public class Edge {

	private int terminal;
	private int length;

	Edge(int terminal, int length) {
		this.terminal = terminal;
		this.length = length;
	}

	public int getTerminal() {
		return terminal;
	}

	public int getLength() {
		return length;
	}

}
