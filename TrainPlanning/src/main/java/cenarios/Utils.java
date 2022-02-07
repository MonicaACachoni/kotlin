package cenarios;

import java.time.LocalDateTime;
import java.util.Vector;

import Malha.Malha;
import dominio.Custos;
import dominio.PTP;
import dominio.Trem;

public class Utils {

	
	// Verifica se os intervalos [x1,x2] e [y1,y2] têm interseção, supondo x1<x2 e y1<y2.
	public static boolean intersecao(LocalDateTime x1, LocalDateTime x2, LocalDateTime y1, LocalDateTime y2) {

		if ((x1.compareTo(y1) <= 0 && y1.compareTo(x2) < 0) ||
			(y1.compareTo(x1) <= 0 && x1.compareTo(y2) < 0)) 
			return true;
		else
			return false;
	}	
	

		public static boolean trechoOcupado( Trem t1,int p1, int prox1, LocalDateTime tp1, LocalDateTime tprox1, Vector<Trem> trens, Malha malha) {

			
			for (int i = 0; i < trens.size(); i++) {

				Trem t = trens.get(i);
				PTP ptp = t.getPTP();

				if (t==t1) return false;
				int prox = ptp.getProxima();
				LocalDateTime h = ptp.getHorario().plusMinutes(t.tempoAteEstacao(ptp.getProxima(), malha));
					
				if (p1==ptp.getPosicao() &&
						(prox1==prox) && 
						(tprox1.compareTo(h)<=0) &&
						(tp1.compareTo(ptp.getHorario())>=0))
					return true;
			}
			
				return false;
		}
//		public static int calculaCusto(Trem t1, Trem t2) {
//			int custoTotal = 0;
//			// custoParada custoManterParadoNaEstaçãoPorMinuto default duplas(Estacao,Custo)
//			Custos custos = t.getCustosDoTrem();		
//			
//			PTP ptp = t.getPTP();
//			int prox =ptp.getProxima();
//			
//			int d = (int) ptp.getDistancia();
//			LocalDateTime tprox = ptp.getHorario().plusMinutes((int) d / t.getVelocidade());
//			
//			custoTotal = tprox.getMinute()*custos.getCustoDeParada()+ custos.getCustoParado()+t.getCustosDoTrem(prox)+t.getCusto();
//			
//			return custoTotal;
//			
//		}
	
}
