package com.example.batchprocessing.processor;

import com.example.batchprocessing.model.TablesWithDeleteColumn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;


@Slf4j
@Component
public class DeleteFieldsProcessor implements ItemProcessor<TablesWithDeleteColumn, TablesWithDeleteColumn> {

    @Autowired
    @Qualifier("admJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public TablesWithDeleteColumn process(TablesWithDeleteColumn tablesWithDeleteColumn) throws Exception {


        // Realizando a contagem
        String countQuery = "select count(1) from " + tablesWithDeleteColumn.getTableName() + " where deleted = true";
        int count = jdbcTemplate.queryForObject(countQuery, (rs, rowNum) -> rs.getInt(1));
        log.info("Tabela : " + tablesWithDeleteColumn.getTableName() + " - Total de registros : " + count);

        if (count > 0){

            // Realizando o delete
            String deleteQuery = "DELETE from " + tablesWithDeleteColumn.getTableName() + " where deleted = true";
            int deleted = jdbcTemplate.update(deleteQuery);

            if (deleted == 0){
                log.error("Erro ao deletar os registros da tabela : " + tablesWithDeleteColumn.getTableName());
            }

            log.info("Registros deletados!");
        }


        return tablesWithDeleteColumn;
    }
}
