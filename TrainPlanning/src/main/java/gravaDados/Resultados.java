package gravaDados;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;

import dominio.Trem;
public class Resultados {
	
	public static void results(Vector<Trem> Finalizados) throws IOException
	{


//	    FileWriter arq = new FileWriter("C:\\Users\\monic\\OneDrive\\Área de Trabalho\\algoritmo\\TrainPlanning\\src\\main\\java\\gravaDados\\novo.txt");
//	    PrintWriter gravarArq = new PrintWriter(arq);
//	 
//	    gravarArq.printf(Finalizados.get(0).getNome());
//	    
//	 
//	    arq.close();
	    
	    
	    
	    int rownum = 0;
	    int cellnum = 0;
	    HSSFCell cell;
	    HSSFRow row;

	    
	 // Criando o arquivo e uma planilha chamada "Product"
	    HSSFWorkbook workbook = new HSSFWorkbook();
	    HSSFSheet sheet = workbook.createSheet("Product");
	  //Configurando estilos de células (Cores, alinhamento, formatação, etc..)
	    HSSFDataFormat numberFormat = workbook.createDataFormat();

	    CellStyle headerStyle = workbook.createCellStyle();
	    headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	    headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
	    headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
	    headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

	    CellStyle textStyle = workbook.createCellStyle();
	    textStyle.setAlignment(CellStyle.ALIGN_CENTER);
	    textStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

	    CellStyle numberStyle = workbook.createCellStyle();
	    numberStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    
	    CellStyle dateStyle = workbook.createCellStyle();
	    dateStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	    dateStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
	    dateStyle.setAlignment(CellStyle.ALIGN_CENTER);
	    dateStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

	    // Configurando Header
	    row = sheet.createRow(rownum);
	    cell = row.createCell(cellnum);
	    cell.setCellStyle(headerStyle);
	    cell.setCellValue("Nome");
	    rownum++; cellnum++;

	    cell = row.createCell(cellnum);
	    cell.setCellStyle(dateStyle);
	    cell.setCellValue("Hora");
	    cellnum++; 
	    
	    cell = row.createCell(cellnum);
	    cell.setCellStyle(headerStyle);
	    cell.setCellValue("Estação");
	    cellnum++;

	    

	    // Adicionando os dados dos produtos na planilha
	    for (int x=0; x<Finalizados.size();x++) {
	    row = sheet.createRow(rownum++);

		    cellnum = 0;
	
		   // cell = row.createCell(cellnum++);
		    cell.setCellStyle(textStyle);
		    for (int y=0;y<Finalizados.get(x).getPercursoDoTrem().size();y++)
		    {
		    	int posicao = Finalizados.get(x).getPercursoDoTrem().get(y).getPosicao();
		    	
		    	row = sheet.createRow(rownum++);
		 	    cellnum = 0;

		 	    // nome do trem
		 	    cell = row.createCell(cellnum++);
		 	    cell.setCellStyle(textStyle);
		 	    cell.setCellValue(Finalizados.get(x).getNome());
		 	    
		 	    //Hora do trem na posição
		 	    cell = row.createCell(cellnum++);    
		 	    cell.setCellStyle(dateStyle);
		 	    cell.setCellValue((Finalizados.get(x).getPercursoDoTrem().get(y).getHorario()).toString());
		
		 	    //posicao do trem
		 	    cell = row.createCell(cellnum++);
		 	    cell.setCellStyle(textStyle);
		 	    cell.setCellValue(posicao);

		 	    
		    }
		   

	    }

	    File path = new File("./src/main/java/gravaDados");
	    String classPath = path.getAbsolutePath();
	    String pathtoFile = path.toString() + "/" +  "results.xls"; 
	    FileOutputStream out = new FileOutputStream(new File(pathtoFile));
	    workbook.write(out);
	    out.close();

	 
  
	}
}
