import parser.PortfolioWriter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the PortfolioWriter class, which is responsible for writing
 * stock portfolio data to an XML file. This class tests the creation of the XML file,
 * the writing of stock data, and the correct closing and saving of the file.
 */
public class PortfolioWriterTest {
  private File test;
  private String writtenTestFile;

  /**
   * Rule to create a temporary folder for testing file operations.
   */
  @Rule
  public TemporaryFolder folder = new TemporaryFolder(new File("src/data/portfolios"));

  /**
   * Sets up the test environment before each test. Initializes the test file and the
   * expected path for the written XML file.
   *
   * @throws IOException if an I/O error occurs during file creation
   */
  @Before
  public void setUp() throws IOException {
    writtenTestFile = "src/data/portfolios/portfolioTest.xml";
    test = folder.newFile();
  }

  /**
   * Tests the PortfolioWriter by writing stock data to an XML file and verifying
   * that the file exists and has the correct length. This test ensures that the
   * PortfolioWriter correctly writes and saves the portfolio data.
   */
  @Test
  public void testPortfolioWriter() {
    try {
      PortfolioWriter writer = new PortfolioWriter("portfolioTest", "2024-06-06",
              folder.getRoot().getAbsolutePath());
      writer.writeStock("AAPL", 1);
      writer.writeStock("GOOG", 2);
      writer.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    assertTrue(Files.exists(Path.of(writtenTestFile)));
    assertTrue(Files.exists(Path.of(test.getAbsolutePath())));
    assertEquals(Path.of(writtenTestFile).toFile().length(), test.length());
  }
}
