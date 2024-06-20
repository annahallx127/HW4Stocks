package parser;

import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;

/**
 * A class that writes a stock portfolio to an XML file. This class implements
 * the StockWriter interface and provides functionality to write stock information
 * to a temporary XML file and then transform and save it to the specified directory
 * and file name. The XML file will have a root element named after the portfolio, with each stock
 * entry as a child element containing the stock symbol and the number of shares.
 * Example of the generated XML structure:
 * <pre>
 * {@code
 * <portfolio date="YYYY-MM-DD">
 *     <stock index="0">
 *         <symbol>STOCK_SYMBOL</symbol>
 *         <shares>NUMBER_OF_SHARES</shares>
 *     </stock>
 *     ...
 * </portfolio>
 * }
 * </pre>
 */
public class PortfolioWriter implements StockWriter {
  private StreamResult result;
  private XMLOutputFactory factory = XMLOutputFactory.newFactory();
  private XMLStreamWriter tempWriter;
  private int counter = 0;
  private File tmp = File.createTempFile("xml", null);
  private FileWriter stream = new FileWriter(tmp);

  /**
   * Constructs a PortfolioWriter instance to write portfolio data to an XML file.
   *
   * @param name      the name of the portfolio file (without extension)
   * @param date      the date associated with the portfolio
   * @param directory the directory where the portfolio file will be saved
   * @throws IOException if an I/O error occurs during file creation
   */
  public PortfolioWriter(String name, String date, String directory) throws IOException {
    try {
      if (directory.isEmpty()) {
        directory = System.getProperty("java.io.tmpdir");
      }
      if (!directory.endsWith("/")) {
        directory += "/";
      }
      if (!name.endsWith(".xml")) {
        name += ".xml";
      }
      FileWriter streamWriter = new FileWriter(Path.of(directory, name).toString());
      result = new StreamResult(streamWriter);
      tempWriter = factory.createXMLStreamWriter(stream);
      tempWriter.writeStartDocument();
      tempWriter.writeStartElement("portfolio");
      tempWriter.writeAttribute("date", date);
    } catch (XMLStreamException e) {
      e.printStackTrace(System.err);
    }
  }

  /**
   * Writes a stock entry to the temporary XML file.
   *
   * @param symbol the stock symbol
   * @param shares the number of shares of the stock
   */
  @Override
  public void writeStock(String symbol, double shares) {
    try {
      tempWriter.writeStartElement("stock");
      tempWriter.writeAttribute("index", String.valueOf(counter));
      tempWriter.writeStartElement("symbol");
      tempWriter.writeCharacters(symbol);
      tempWriter.writeEndElement();
      tempWriter.writeStartElement("shares");
      tempWriter.writeCharacters(String.valueOf(shares));
      tempWriter.writeEndElement();
      tempWriter.writeEndElement();
      counter++;
    } catch (XMLStreamException e) {
      e.printStackTrace(System.err);
    }
  }

  /**
   * Closes the temporary XML writer, finalizes the document, and transforms it into the final XML
   * format saved to the specified file. Ensures the XML output is indented for readability.
   */
  @Override
  public void close() {
    try {
      tempWriter.writeEndElement();
      tempWriter.writeEndDocument();
      tempWriter.flush();
      tempWriter.close();
      Transformer transformer = SAXTransformerFactory.newDefaultInstance().newTransformer();
      SAXSource sax = new SAXSource(new InputSource(new FileReader(tmp)));
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      transformer.setOutputProperty("{https://xml.apache.org/xslt}indent-amount", "4");
      transformer.transform(sax, result);
    } catch (TransformerException | IOException | IllegalArgumentException | XMLStreamException e) {
      e.printStackTrace(System.err);
    }
  }
}
