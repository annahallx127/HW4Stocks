import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import parser.PortfolioWriter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PortfolioWriterTest {
  PortfolioWriter writer;
  File test;
  String pathToWrite;

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Before
  public void setUp() throws IOException {
    pathToWrite = "src/data/portfolios/portfolioTest.xml";
    test = folder.newFile("portfolioTest.xml");
  }

  @Test
  public void testPortfolioWriter() {
    writer = new PortfolioWriter("portfolioTest", "2024-06-06", test.getAbsolutePath());
    writer.writeStock( "AAPL", 1);
    writer.writeStock("GOOG", 2);
    writer.close();
    // TODO: write portfolioTest.xml for assert
    assertTrue(Files.exists(Path.of(pathToWrite)));
    assertEquals(test.length(), Path.of(pathToWrite).toFile().length());
  }
}