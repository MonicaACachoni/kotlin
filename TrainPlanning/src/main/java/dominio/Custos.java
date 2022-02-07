package dominio;

import java.util.Hashtable;

public class Custos {

	private  int custoDeParada;
	private  int custoDeRetrocesso;
	private  int custoDefault;
	private  Hashtable<Integer, Integer>custoPorEstacao =  new Hashtable<Integer, Integer>();

	public Custos(int custoDeParada, int custoRetrocesso, int custoDefault, Hashtable<Integer, Integer> custoPorEstacao) {
		super();
		this.custoDeParada = custoDeParada;
		this.custoDeRetrocesso = custoRetrocesso;
		this.custoDefault = custoDefault;
		this.custoPorEstacao = custoPorEstacao;
	}

	public int getCustoDeParada() {
		return custoDeParada;
	}

	public void setCustoDeParada(int custoDeParada) {
		this.custoDeParada = custoDeParada;
	}

	public int getCustoParado() {
		return custoDeRetrocesso;
	}

	public void setCustoParado(int custoParado) {
		this.custoDeRetrocesso = custoParado;
	}

	public int getCustoDefault() {
		return custoDefault;
	}

	public void setCustoDefault(int custoDefault) {
		this.custoDefault = custoDefault;
	}

	public Integer getCustoPorEstacao(int i) {
		return custoPorEstacao.get(i);
	}

	public void setCustoPorEstacao(Hashtable<Integer, Integer> custoPorEstacao) {
		this.custoPorEstacao = custoPorEstacao;
	}
	



	
	

}
