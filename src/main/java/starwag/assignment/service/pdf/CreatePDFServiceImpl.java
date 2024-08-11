package starwag.assignment.service.pdf;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import starwag.assignment.dto.SectionDTO;
import starwag.assignment.service.SectionProcessingService;

@Service
public class CreatePDFServiceImpl implements CreatePDFService {

  final
  SectionProcessingService sectionProcessingService;

  private static final Log LOG = LogFactory.getLog(CreatePDFServiceImpl.class);

  public CreatePDFServiceImpl(SectionProcessingService sectionProcessingService) {
    this.sectionProcessingService = sectionProcessingService;
  }

  @Override
  public byte[] generatePDF() {
    try (ByteArrayOutputStream byteArrayOutputStreamOfChart = new ByteArrayOutputStream();
         ByteArrayOutputStream baosOfPDF = new ByteArrayOutputStream()) {

      // Generate chart image
      BufferedImage chartImage = getChart().createBufferedImage(700, 500);
      ImageIO.write(chartImage, "png", byteArrayOutputStreamOfChart);

      // Create PDF document
      Document document = new Document();
      PdfWriter.getInstance(document, baosOfPDF);
      document.open();

      // Add chart image to PDF
      Image chartImageElement = Image.getInstance(byteArrayOutputStreamOfChart.toByteArray());
      chartImageElement.scaleToFit(500, 300);
      document.add(chartImageElement);
      document.close();

      return baosOfPDF.toByteArray();

    } catch (IOException | DocumentException e) {
      LOG.error("Exception while generating PDF: ", e);
      throw new RuntimeException("Failed to generate PDF", e);
    }
  }

  private JFreeChart getChart() {
    DefaultPieDataset defaultCategoryDataset = new DefaultPieDataset();
    List<SectionDTO> sectionList = sectionProcessingService.getAllSections();
    sectionList.forEach(s -> defaultCategoryDataset.setValue(s.getName(), s.getGeologicalClasses().size()));

    JFreeChart chart = ChartFactory.createPieChart(
        "Classes per Section",
        defaultCategoryDataset,
        true,
        true,
        false
    );

    PiePlot plot = (PiePlot) chart.getPlot();
    plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
        "{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0.00%")));

    return chart;
  }
}
