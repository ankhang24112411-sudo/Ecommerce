package com.khang.backendecommerce.newstruc.csv2;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductExportRow {
    @ExcelProperty("Tên sản phẩm")
    private String name;

    @ExcelProperty("Giá")

    private BigDecimal price;


    @ExcelProperty("Số lượng")
    private Long stockQuantity;

    @ExcelProperty("Trạng thái")
    private String stockStatus;
}
