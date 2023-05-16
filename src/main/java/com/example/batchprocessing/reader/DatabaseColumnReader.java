package com.example.batchprocessing.reader;

import com.example.batchprocessing.model.TablesWithDeleteColumn;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.PostgresPagingQueryProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DatabaseColumnReader {
    private static final String GET_TABLES = "SELECT table_name, column_name, data_type FROM information_schema.columns WHERE table_schema = 'public' and column_name = 'deleted' and data_type = 'boolean'";

    @Bean
    public JdbcPagingItemReader<TablesWithDeleteColumn> getTablesReader(@Qualifier("admDataSource") DataSource dataSource){
        final JdbcPagingItemReader<TablesWithDeleteColumn> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        reader.setFetchSize(100);
        reader.setPageSize(100);
        reader.setRowMapper(rowMapper());
        reader.setQueryProvider(createQuery());

        return reader;
    }

    private PostgresPagingQueryProvider createQuery(){
        final Map<String, Order> sortKeys = new HashMap<>();

        sortKeys.put("table_name",Order.ASCENDING);

        final PostgresPagingQueryProvider queryProvider = new PostgresPagingQueryProvider();

        queryProvider.setSelectClause("*");
        queryProvider.setFromClause(getFromClause());
        queryProvider.setSortKeys(sortKeys);

        return queryProvider;
    }

    public String getFromClause(){
        return "( " + GET_TABLES + ")" + " AS RESULT_TABLE ";
    }


    private RowMapper<TablesWithDeleteColumn> rowMapper() {
        return new RowMapper<TablesWithDeleteColumn>() {

            @Override
            public TablesWithDeleteColumn mapRow(ResultSet rs, int rowNum) throws SQLException {

                TablesWithDeleteColumn obj = new TablesWithDeleteColumn();

                obj.setTableName(rs.getString("table_name"));
                obj.setColumnName(rs.getString("column_name"));

                obj.setDataType(rs.getString("data_type"));

                return obj;
            }

        };
    }
}
