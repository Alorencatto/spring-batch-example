package com.example.batchprocessing.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TablesWithDeleteColumn {

    private String tableName;

    private String columnName;

    private String dataType;
}
