package com.khang.backendecommerce.newstruc.domain.csv.dto;

public record ExportResult(
        Long executionId,
        String fileName,
        String status
) {
}
