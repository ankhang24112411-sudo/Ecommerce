package com.khang.backendecommerce.newstruc.domain.csv;

import com.khang.backendecommerce.newstruc.domain.csv.dto.ExportResult;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ProductExportJobService {
    private final Job exportProductJob;
    private final JobOperator jobOperator;

    public ExportResult export() throws Exception {

        Files.createDirectories(Paths.get("exports"));

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        String fileName = "exports/products_" + timestamp + ".csv";

        var parameters = new JobParametersBuilder().addString("output.file.name", fileName).addLong("run.id", System.currentTimeMillis()).toJobParameters();

        var execution = jobOperator.start(exportProductJob, parameters);

        return new ExportResult(execution.getId(), fileName, execution.getStatus().name());
    }
}
