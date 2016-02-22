package com.samsung.ssc.database.sql;


import java.util.List;

public interface SQLHelper {

    public String getTableCreateDDL(String tableName, List<SQLColumn> columns);
    
    public String getTableCreateDDL(String tableName, List<SQLColumn> columns,String[] primaryKeyColumns);

    public String getTableDropDDL(String tableName);

    public String getSQLTypeInteger();

    public String getSQLTypeString();
    
    public String getSQLTypeBoolean();
    
    public String getSQLTypeReal();
    
    public String getSQLTypeDateTime();


}
