package tester;
import  java.lang.Object;

import java.awt.Color;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.TemporalField;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.Minute;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import Malha.Malha;
import dominio.PTP;
import dominio.Trem;

/**
 * A simple demonstration application showing how to create a line chart using data from an
 * {@link XYDataset}.
 *
 */
public class trainPlot extends ApplicationFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	TimeSeries series;
	Trem tremN1;
	XYSeriesCollection dataset = null;
private LocalDateTime DateUtil;
	
	
	public trainPlot(final String title,List<Trem> trensAPlanejar, Malha m) {
		super(title);
		
		
			
			XYDataset dataset = createDataset(trensAPlanejar,m);
			final JFreeChart chart = createChart(dataset);
			final ChartPanel chartPanel = new ChartPanel(chart);
			chartPanel.setPreferredSize(new java.awt.Dimension(1600, 1100));
			setContentPane(chartPanel);
		
			
			
			
	}
	
	
	   private Class<? extends RegularTimePeriod> timePeriodClass = Hour.class;
	 
	   private static final TimeZone GMT = TimeZone.getTimeZone("GMT-3");
	 
	   public RegularTimePeriod convertToDateViaSqlTimestamp(LocalDateTime period) {
	    Date date = java.sql.Timestamp.valueOf(period );
	    return RegularTimePeriod.createInstance(timePeriodClass, date , GMT);
	}
    /** 
     * Creates a sample dataset.
     * @param trensAPlanejar 
     * 
     * @return a sample dataset.
     */
    private XYDataset createDataset(List<Trem> trensAPlanejar, Malha m) {
    	TimeSeriesCollection dataset = new TimeSeriesCollection();

    	int listaTrens=trensAPlanejar.size();
  
		for (int i=0; i < listaTrens ; i++) {
			
			tremN1 = trensAPlanejar.get(i);
			final TimeSeries serie = new TimeSeries(tremN1, "Domain", "Range", Hour.class);
			
			for (PTP p : tremN1.getPercursoDoTrem()) {
				// horario do trem na posicao , getpercurso
				
				LocalDateTime period =  p.getHorario();

				RegularTimePeriod period1 = convertToDateViaSqlTimestamp(period);
				Number value = p.getPosicao();

				
				 long timeStamp = period.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
				//   timeSeries.add(new Millisecond(new Date(timeStamp), value));
				 
				serie.addOrUpdate(period1, value);
			};

			dataset.addSeries(serie);
			}

        return dataset;
         
    }
    



	/**
     * Creates a chart.
     * 
     * @param dataset  the data for the chart.
     * 
     * @return a chart.
     */
    private JFreeChart createChart(final XYDataset dataset) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
            "Train Scheduling",      // chart title
            "Hour",                      // x axis label
            "Positon",                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
        );
        
        

        chart.setBackgroundPaint(Color.white);

//        final StandardLegend legend = (StandardLegend) chart.getLegend();
  //      legend.setDisplaySeriesShapes(true);
        
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
    //    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.
                
        return chart;
        
    }

    
  
}
