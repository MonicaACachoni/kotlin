/**
 * 
 */
package dominio;

import java.util.Comparator;

/**
 * @author Monica
 *
 */
public class TremComparator implements Comparator<Trem> { 

	


//	static LocalDateTime thisISs;
//	static LocalDateTime thatISs;
//
//	        public int compare(TremLoop trem,TremLoop outroTrem)
//	        {
//	        	trem.getPercursoDoTrem().forEach(p-> {
//					
//					thisISs=p.getHorarioDoTremNaPosicao();
//				});
//				outroTrem.getPercursoDoTrem().forEach(p-> {
//				thatISs=p.getHorarioDoTremNaPosicao();
//				});
//				return thisISs.compareTo(thatISs);
//	        }
	
	static int thisISs;
	static int thatISs;

	        public int compare(Trem trem,Trem outroTrem) 	        {
	        	
	        	thisISs = trem.getPTP().getPosicao();
	        	thatISs = outroTrem.getPTP().getPosicao();
				
				return thisISs == thatISs ? 0 : thisISs<thatISs ? -1 : 1;
	        }
	
	    }

