package com.khang.backendecommerce.newstruc.domain.csv;

import com.khang.backendecommerce.newstruc.domain.csv.dto.ProductCsvDTO;
import com.khang.backendecommerce.newstruc.domain.csv.dto.ProductStockRow;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.database.JdbcPagingItemReader;
import org.springframework.batch.infrastructure.item.database.Order;
import org.springframework.batch.infrastructure.item.database.PagingQueryProvider;
import org.springframework.batch.infrastructure.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.infrastructure.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.infrastructure.support.DatabaseType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.core.repository.JobRepository;

import javax.sql.DataSource;
import java.util.Collections;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class ExportProductConfig {
    private final ProductExportProcessor productExportProcessor;
    private final JobRepository repository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;

    @Bean
    public SqlPagingQueryProviderFactoryBean productQueryProvider() {

        var provider = new SqlPagingQueryProviderFactoryBean();
        provider.setSelectClause("""
                select product_id, name,  sku, price,  stock_quantity
                """);
        provider.setFromClause("""
                from (select p.id AS product_id, p.name, p.sku,  p.price,
                        coalesce(
                            sum(i.quantity - COALESCE(i.reserved_quantity, 0)),0) as stock_quantity
                    from tbl_product p
                    left join tbl_inventory i
                    on i.product_id = p.id
                    group by p.id,p.name,p.sku,p.price ) product_stock
                """);
        provider.setDataSource(dataSource);
        provider.setDatabaseType(DatabaseType.POSTGRES.name());

        provider.setSortKeys(Collections.singletonMap("product_id", Order.ASCENDING));
        return provider;
    }

    @Bean
    public JdbcPagingItemReader<ProductStockRow> productReader(PagingQueryProvider productQueryProvider) throws Exception {
        return new JdbcPagingItemReaderBuilder<ProductStockRow>()
                .name("product stock reader")
                .dataSource(dataSource)
                .queryProvider(productQueryProvider)
                .rowMapper(new DataClassRowMapper<>(ProductStockRow.class))
                .pageSize(100)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<ProductCsvDTO> productCsvWriter(@Value("#{jobParameters['output.file.name']}") String outputFile) {

        return new FlatFileItemWriterBuilder<ProductCsvDTO>()
                .name("product csv writer")
                .resource(new FileSystemResource(outputFile))
                .headerCallback(writer -> writer.write("productId;name;sku;price;stockQuantity;inventoryStatus"))
                .delimited()
                .delimiter(";")
                .sourceType(ProductCsvDTO.class)
                .names("productId", "name", "sku", "price", "stockQuantity", "inventoryStatus")
                .shouldDeleteIfEmpty(true)
                .append(false)
                .build();
    }

    @Bean
    public Step exportProductStep(JdbcPagingItemReader<ProductStockRow> productReader, FlatFileItemWriter<ProductCsvDTO> productCsvWriter) {

        return new StepBuilder("exportProductStep", repository)
                .<ProductStockRow, ProductCsvDTO>chunk(100)
                .transactionManager(transactionManager)
                .reader(productReader)
                .processor(productExportProcessor)
                .writer(productCsvWriter)
                .build();
    }

    @Bean
    public Job exportProductJob(Step exportProductStep) {

        return new JobBuilder("exportProductJob", repository)
                .start(exportProductStep)
                .build();
    }
}
