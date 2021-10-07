package inditext.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class StockEntry {

  Integer sizeId;
  Double quantity;

}
