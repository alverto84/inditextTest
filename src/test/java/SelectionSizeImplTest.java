import static org.junit.jupiter.api.Assertions.assertEquals;

import inditext.model.ProdutcSize;
import inditext.model.StockEntry;
import inditext.service.SelectionSizeImpl;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SelectionSizeImplTest {

  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
  private List<ProdutcSize> produtcSize;
  private List<StockEntry> stockEntry;

  @BeforeEach
  public void setUp() {
    System.setOut(new PrintStream(outputStreamCaptor));
    loadData();
  }

  public void loadData() {
    produtcSize = List.of(
        ProdutcSize.builder().id(1).syzeSystem(123).build(),
        ProdutcSize.builder().id(2).syzeSystem(321).build(),
        ProdutcSize.builder().id(3).syzeSystem(213).build(),
        ProdutcSize.builder().id(4).syzeSystem(456).build(),
        ProdutcSize.builder().id(5).syzeSystem(564).build(),
        ProdutcSize.builder().id(6).syzeSystem(654).build(),
        ProdutcSize.builder().id(7).syzeSystem(789).build(),
        ProdutcSize.builder().id(8).syzeSystem(999).build(),
        ProdutcSize.builder().id(9).syzeSystem(987).build());

    stockEntry = List.of(
        StockEntry.builder().sizeId(1).quantity(5.0).build(),
        StockEntry.builder().sizeId(2).quantity(8.0).build(),
        StockEntry.builder().sizeId(3).quantity(12.0).build(),
        StockEntry.builder().sizeId(4).quantity(null).build(),
        StockEntry.builder().sizeId(5).quantity(3.0).build(),
        StockEntry.builder().sizeId(6).quantity(15.0).build(),
        StockEntry.builder().sizeId(7).quantity(15.5).build(),
        StockEntry.builder().sizeId(9).quantity(-5.0).build());
  }

  @Test
  public void selectionSizeTest() {
    SelectionSizeImpl.builder().build().sizeSelection(produtcSize, stockEntry);
    assertEquals("3,6,7", outputStreamCaptor.toString().trim());
  }
}
