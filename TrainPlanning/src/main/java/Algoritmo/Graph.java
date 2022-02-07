package Algoritmo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import Malha.Malha;
import dominio.PTP;
import dominio.Trem;

class Graph extends ApplicationFrame {

	//private static final long serialVersionUID = 1L;

	private Trem tremN1;

	public Graph(final String windowTitle, int width, int height, String xTitle, String yTitle, String headerTitle,
			String graphTitle, List<Trem> T, Malha M) {
		
		super(windowTitle);

		TimeSeriesCollection dataset = createDataset(T, M);

		final JFreeChart chart = ChartFactory.createTimeSeriesChart(headerTitle, // set title
				xTitle, // set x title
				yTitle, // set y title
				dataset, false, false, false);
		
		XYLineAndShapeRenderer r0 = new XYLineAndShapeRenderer();
		
		
		for (int i=0; i<T.size(); i++) {

			Trem t1 = T.get(i);
			
			if (t1.getNome().equals("Vermelho")) {
				r0.setSeriesPaint(i, Color.RED);
			}
			else if (t1.getNome().equals("Azul")) {
				r0.setSeriesPaint(i, Color.BLUE);
			}
			else if (t1.getNome().equals("Verde")) {
				r0.setSeriesPaint(i, new Color(0x00, 0x80, 0x00));
			}
			else if (t1.getNome().equals("Amarelo")) {
				r0.setSeriesPaint(i, Color.YELLOW);
			}
			else if (t1.getNome().equals("Laranja")) {
				r0.setSeriesPaint(i, Color.ORANGE);
			}
			else if (t1.getNome().equals("Preto")) {
				r0.setSeriesPaint(i, Color.BLACK);
			}
			else if (t1.getNome().equals("Roxo")) {
				r0.setSeriesPaint(i, new Color(0x99,0x33,0x99));
			}
			else if (t1.getNome().equals("Rosa")) {
				r0.setSeriesPaint(i, Color.PINK);
			}
			else if (t1.getNome().equals("Branco")) {
				r0.setSeriesPaint(i, Color.LIGHT_GRAY);
			}
		
		}
			
			
		final XYPlot plot = chart.getXYPlot();
		plot.setRenderer(0,r0);

		
		ValueAxis axis = plot.getDomainAxis();
		// axis.setFixedAutoRange(60000.0);

		axis = plot.getRangeAxis();
		axis.setAutoRange(true);

		final ChartPanel chartPanel = new ChartPanel(chart);
		final JPanel content = new JPanel(new BorderLayout());
		content.add(chartPanel);

		chartPanel.setPreferredSize(new java.awt.Dimension(width, height));

		
		setContentPane(content);
	}

//    public void addPointA(double y) {
//        this.seriesA.add(new Millisecond(), y);
//    }
//
//    public void addPointB(double y) {
//        this.seriesB.add(new Millisecond(), y);
//    }

	private TimeSeriesCollection createDataset(List<Trem> trensAPlanejar, Malha M) {

		TimeSeriesCollection dataset = new TimeSeriesCollection();

		int listaTrens = trensAPlanejar.size();

		for (int i = 0; i < listaTrens; i++) {

			tremN1 = trensAPlanejar.get(i);
			TimeSeries serie = new TimeSeries(tremN1, "Posicao", "Range", Hour.class);

			for (PTP p : tremN1.getPercursoDoTrem()) {
				LocalDateTime horario = p.getHorario();
				Second second = new Second(Date.from(horario.atZone(ZoneId.systemDefault()).toInstant()));

				Number value;
				
				if (p.getDistancia() == 0)
					value = p.getPosicao();
				else {
					if (p.getPosicao() > p.getProxima())
						value = p.getPosicao() - (p.getDistancia()/M.distancia(p.getPosicao(), p.getProxima()));
					else
						value = p.getPosicao() + (p.getDistancia()/M.distancia(p.getPosicao(), p.getProxima()));
					
				}
				
				
				serie.addOrUpdate(second, value);
			};

			
			dataset.addSeries(serie);
		}

		return dataset;

	}
}