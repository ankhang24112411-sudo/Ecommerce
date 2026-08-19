package com.khang.backendecommerce.newstruc.csv2;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.khang.backendecommerce.newstruc.entity.ProductEntity;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportExcelService {
    private final ProductExportMapper productExportMapper;

    public void exportData(HttpServletResponse response) throws IOException {
        List<ProductEntity> products = productExportMapper.selectProductForExport(10);
        String fileName = "product.xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        ExcelWriterBuilder excelWriterBuilder = EasyExcelFactory.write(response.getOutputStream(), ProductEntity.class);
    }
}
