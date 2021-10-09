package inditext;

import inditext.model.ProdutcSize;
import inditext.model.StockEntry;
import inditext.service.SelectionSizeImpl;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShowByConsole {
    public static void main(String[] args) {

        BasicConfigurator.configure();
        final var logger = Logger.getLogger(ShowByConsole.class);

        List<ProdutcSize> produtcSize = List.of(
                ProdutcSize.builder().id(1).syzeSystem(123).build(),
                ProdutcSize.builder().id(2).syzeSystem(321).build(),
                ProdutcSize.builder().id(3).syzeSystem(213).build(),
                ProdutcSize.builder().id(4).syzeSystem(456).build(),
                ProdutcSize.builder().id(5).syzeSystem(564).build(),
                ProdutcSize.builder().id(6).syzeSystem(654).build(),
                ProdutcSize.builder().id(7).syzeSystem(789).build(),
                ProdutcSize.builder().id(8).syzeSystem(999).build(),
                ProdutcSize.builder().id(9).syzeSystem(987).build());

        List<StockEntry> stockEntry = List.of(
                StockEntry.builder().sizeId(1).quantity(5).build(),
                StockEntry.builder().sizeId(2).quantity(8).build(),
                StockEntry.builder().sizeId(3).quantity(12).build(),
                StockEntry.builder().sizeId(4).quantity(null).build(),
                StockEntry.builder().sizeId(5).quantity(3).build(),
                StockEntry.builder().sizeId(6).quantity(15).build(),
                StockEntry.builder().sizeId(7).quantity(16).build(),
                StockEntry.builder().sizeId(9).quantity(-5).build());

        SelectionSizeImpl.builder().build().sizeSelection(produtcSize, stockEntry);

        var numELements = 1000000;
        var range = 100000;
        logger.info("Number of elements: " + numELements + " Range: " + range);

        List<ProdutcSize> produtcSize2 = new ArrayList<>();
        List<StockEntry> stockEntry2 = new ArrayList<>();
        var generadorAleatorios = new Random();
        for (var i = 0; i < numELements; i++) {
            produtcSize2.add(ProdutcSize.builder().id(i).syzeSystem(1 + generadorAleatorios.nextInt(range)).build());
            stockEntry2.add(StockEntry.builder().sizeId(i).quantity(1 + generadorAleatorios.nextInt(range)).build());
        }
        SelectionSizeImpl.builder().build().sizeSelection(produtcSize2, stockEntry2);
    }
}
