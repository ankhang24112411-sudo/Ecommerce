package com.khang.backendecommerce.newstruc.domain.csv;


import com.khang.backendecommerce.newstruc.domain.csv.dto.ProductCsvDTO;
import com.khang.backendecommerce.newstruc.domain.csv.dto.ProductStockRow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductExportProcessor implements ItemProcessor<ProductStockRow, ProductCsvDTO> {

    @Override
    public ProductCsvDTO process(ProductStockRow item) {

        String status;
        if (item.stockQuantity() <= 0) {
            status = "OUT_OF_STOCK";
        } else if (item.stockQuantity() <= 10) {
            status = "LOW_STOCK";
        } else {
            status = "IN_STOCK";
        }

        log.info("Processing product: {}", item.name());
        return new ProductCsvDTO(item.productId(), item.name(), item.sku(), item.price(), item.stockQuantity(), status);
    }
}
