Feature: Criar um arquivo de trens
  Eu desejo criar um arquivo .txt para meus testes

@trem
Scenario Outline: Criando arquivos de trens
    Given que eu tenho o trem		"<name>"
    When Quando eu solicitar o  "<estacaoOrigem>"
   	And selecionar a  "<proxima>","<distancia>","<estacaoDestino>",  "<horario>", "<velocidade>", "<custo>"
    Then um arquivo com o formato de entrada deverá ser criado

Examples:      
    	| name    | estacaoOrigem  | proxima  | distancia | estacaoDestino | horario             | velocidade | custo |
      | amarelo | 1              | 2        |			30		|     30         |  2019-06-15T10:44:00| 2					| 12 	  |
      | verde   | 1              | 2        |			30		|     30         |  2019-06-15T10:44:00| 2					| 12 	  |

#amarelo 1 2 30 30 2019-06-15T10:44:00 2 12
#nome origem proximo distancia destino horario velocidade custo

@trem
Scenario Outline: Criando arquivos de trens
    Given que eu tenho o trem		"<name>"
    When Quando eu solicitar o  "<estacaoOrigem>"
   	And selecionar a  "<proxima>","<distancia>","<estacaoDestino>",  "<horario>", "<velocidade>", "<custo>"
    Then um arquivo com o formato de entrada deverá ser criado

Examples:      
    	| name    | estacaoOrigem  | proxima  | distancia | estacaoDestino | horario             | velocidade | custo |
      | amarelo | 1              | 2        |			30		|     30         |  2019-06-15T10:44:00| 2					| 12 	  |
      | verde   | 1              | 2        |			30		|     30         |  2019-06-15T10:44:00| 2					| 12 	  |

