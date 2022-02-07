package testes;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CriarArquivoTrens {
	
	private String nome;
	int posicao; // estacao referencia de saida do trem
	double distancia;  // distancia que o trem se encontra de posicao
	int proxima; // proxima estaçao do trem
	private LocalDateTime horaInicial;
	int destino; // posicao y sobre a linha
	int velocidade;
	int custo;
	StringBuilder name = new StringBuilder();

	
	public void gravarTxt(String nome) throws IOException {
		

	
	 FileWriter arq = new FileWriter("C:\\Users\\monic\\Dropbox\\Codigo\\trens.txt",true);
	 PrintWriter gravarArq = new PrintWriter(arq);
	 gravarArq.append(nome);

	 	
	 arq.flush();  
	 arq.close();
	}

	@Given("^que eu tenho o trem		\"([^\"]*)\"$")
	public void que_eu_tenho_o_trem(String arg1) throws Throwable {
		name.append(arg1);
		name.append(" ");
	}

	@When("^Quando eu solicitar o  \"([^\"]*)\"$")
	public void quando_eu_solicitar_o(String arg1) throws Throwable {
		name.append(arg1);
		name.append(" ");
	}

	@When("^selecionar a  \"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",  \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
	public void selecionar_a(String arg1, String arg2, String arg3, String arg4, String arg5, String arg6) throws Throwable {
		name.append(arg1);
		name.append(" ");
		name.append(arg2);
		name.append(" ");
		name.append(arg3);
		name.append(" ");
		name.append(arg4);
		name.append(" ");
		name.append(arg5);
		name.append(" ");
		name.append(arg6);
		name.append("\n");
	}

	@Then("^um arquivo com o formato de entrada deverá ser criado$")
	public void um_arquivo_com_o_formato_de_entrada_deverá_ser_criado() throws Throwable {
		gravarTxt(name.toString());
		System.out.print("Gravar arquivo trens");
	}


		
}
