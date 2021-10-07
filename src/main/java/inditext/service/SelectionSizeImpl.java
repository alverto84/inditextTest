package inditext.service;

import inditext.model.ProdutcSize;
import inditext.model.StockEntry;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public class SelectionSizeImpl implements SelectionSize {

  private final int ZERO = 0;

  //O(N + N*log(N) + N + N*log(N) + N) = 2N*log(N) +3N -> O(N*log(N))
  public void sizeSelection(List<ProdutcSize> produtcsSize, List<StockEntry> stockEntries) {

    HashMap<Integer, StockEntry> sizeMaxStock = new HashMap();
    //O(N) Transform the stockEntries to HashMap for having O(1)-O(log(N)) access to the stock quantity by productIdSize
    Map<Integer, Double> stockEntryMap = Optional.ofNullable(stockEntries).orElse(Collections.emptyList())
        .stream()
        .filter(s -> Objects.nonNull(s.getQuantity()) && s.getQuantity() > ZERO)
        .collect(Collectors.toMap(StockEntry::getSizeId, StockEntry::getQuantity));

    //O(N*log(N))
    for (ProdutcSize ps : produtcsSize) {
      Integer currentPid = ps.getId();
      Integer size = ps.getSyzeSystem();
      //O(log(N)) sum digits for identify the same size
      int sum = sumDigits(size);
      Double currentQuantity = stockEntryMap.get(currentPid);
      if (Objects.nonNull(currentQuantity)) {
        StockEntry maxStockEntry = sizeMaxStock.get(sum);
        if (Objects.nonNull(maxStockEntry)) {
          Double quantityOld = maxStockEntry.getQuantity();
          if (currentQuantity > quantityOld) {
            //For a concrete size update the max quantity for a productSizeId
            sizeMaxStock.put(sum, StockEntry.builder().sizeId(currentPid).quantity(currentQuantity).build());
          }
        } else {
          sizeMaxStock.put(sum, new StockEntry(currentPid, currentQuantity));
        }
      }
    }
    //Generate result list O(N)
    int[] maxStockPid =
        sizeMaxStock.entrySet().stream().map(e -> e.getValue()).map(e -> e.getSizeId()).mapToInt(i -> i).toArray();
    //O(N*log(N) parallel quicksort
    Arrays.parallelSort(maxStockPid);
    //O(N)
    System.out.println(Arrays.stream(maxStockPid).mapToObj(String::valueOf).collect(Collectors.joining(",")));
  }

  //O(log(N))
  int sumDigits(int n) {
    return n == 0 ? 0 : n % 10 + sumDigits(n / 10);
  }
}

