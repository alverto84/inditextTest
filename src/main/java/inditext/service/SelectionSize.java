package inditext.service;

import inditext.model.ProdutcSize;
import inditext.model.StockEntry;
import java.util.List;

public interface SelectionSize {

  void sizeSelection(List<ProdutcSize> produtcsSize, List<StockEntry> stockEntries);
}


