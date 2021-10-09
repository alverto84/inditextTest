package inditext.service;

import inditext.model.ProdutcSize;
import inditext.model.StockEntry;
import lombok.Builder;
import org.apache.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Builder
public class SelectionSizeImpl implements SelectionSize {

    static final Logger logger = Logger.getLogger(SelectionSizeImpl.class);
    private static final int ZERO = 0;

    //O(N + N*log(N) + N + N*log(N) + N) = 2N*log(N) +3N -> O(N*log(N))
    public void sizeSelection(List<ProdutcSize> produtcsSize, List<StockEntry> stockEntries) {

        var start = Instant.now();

        //O(N) Transform the stockEntries to HashMap for having O(1)-O(log(N)) access to the stock quantity by productIdSize
        Map<Integer, Integer> stockEntriesMap = processStockEntries(stockEntries);

        //This map is used for store the max stock productId for a uniqueSize
        //Key: uniqueSize, Value: StockEntry(productId, quantity)
        HashMap<Integer, StockEntry> uniqueSizeMaxStock = new HashMap<>();

        //Iterate over each product and update the uniqueSizeMaxStock storing the max stock productId for a uniqueSize
        //O(N*log(N))
        produtcsSize.forEach(ps -> updateMaxUniqueSize(sumDigits(ps.getSyzeSystem()), ps.getId(), stockEntriesMap.get(ps.getId()), uniqueSizeMaxStock));

        //Generate the productId list from the uniqueSize with more stock
        // O(N)
        int[] maxStockPid = uniqueSizeMaxStock.entrySet().stream().map(Map.Entry::getValue).map(StockEntry::getSizeId).mapToInt(i -> i).toArray();

        //Parallel quicksort for sorting
        //O(N*log(N)
        Arrays.parallelSort(maxStockPid);

        //Print the productId sorted list by console
        //O(N)
        logger.info(Arrays.stream(maxStockPid).mapToObj(String::valueOf).collect(Collectors.joining(",")));

        var finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        logger.info("Elapsed time: " + timeElapsed + "ms");
    }

    private void updateMaxUniqueSize(int uniqueSize, Integer cPid, Integer currentQuantity, HashMap<Integer, StockEntry> uniqueSizeMaxStock) {
        Optional.ofNullable(currentQuantity).ifPresent(cQuantity -> Optional.of(cQuantity).map(c -> uniqueSizeMaxStock.get(uniqueSize))
                .ifPresentOrElse(maxStockEntry -> {
                            Optional.of(maxStockEntry).map(StockEntry::getQuantity)
                                    .filter(maxStockQuantity -> cQuantity > maxStockQuantity)
                                    .ifPresent(i -> uniqueSizeMaxStock.put(uniqueSize, StockEntry.builder().sizeId(cPid).quantity(cQuantity).build()));
                        }
                        , () -> uniqueSizeMaxStock.put(uniqueSize, StockEntry.builder().sizeId(cPid).quantity(cQuantity).build())));
    }

    //O(N)
    private Map<Integer, Integer> processStockEntries(List<StockEntry> stockEntries) {
        return Optional.ofNullable(stockEntries).orElse(Collections.emptyList())
                .stream()
                .filter(this::stockBiggerThanZero)
                .collect(Collectors.toMap(StockEntry::getSizeId, StockEntry::getQuantity));
    }

    //O(1)
    private boolean stockBiggerThanZero(StockEntry stockEntry) {
        return Objects.nonNull(stockEntry.getQuantity()) && stockEntry.getQuantity() > ZERO;
    }

    //O(log(N))
    int sumDigits(int n) {
        return n == 0 ? 0 : n % 10 + sumDigits(n / 10);
    }
}

