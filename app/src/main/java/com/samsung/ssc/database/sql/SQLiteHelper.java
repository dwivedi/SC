package com.samsung.ssc.database.sql;

import java.util.List;

public final class SQLiteHelper implements SQLHelper {

	@Override
	public String getTableCreateDDL(String tableName, List<SQLColumn> columns) {
		StringBuilder builder = new StringBuilder();

		builder.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append("(");

		int size = columns.size();
		for (int i = 0; i < size; i++) {
			
			SQLColumn column = columns.get(i);

			builder.append(column.getColumnName()).append(" ")
					.append(column.getColumnType());

			if (column.isPrimaryKey()) {
				builder.append(" PRIMARY KEY ");
			}

			if (column.isNotNull()) {
				builder.append(" NOT NULL ");
			}

			if (column.isUnique()) {
				builder.append(" UNIQUE ");
			}

			if (i < size - 1) {
				builder.append(",");
			}
			
			
		}

		builder.append(")");

		return builder.toString();
	}

	@Override
	public String getTableDropDDL(String tableName) {
		StringBuilder builder = new StringBuilder();
		builder.append("DROP TABLE IF EXISTS ").append(tableName);
		return builder.toString();
	}

	@Override
	public String getSQLTypeInteger() {
		return "INTEGER";
	}

	@Override
	public String getSQLTypeString() {
		return "TEXT";
	}

	@Override
	public String getSQLTypeBoolean() {

		return "BOOLEAN";
	}

	@Override
	public String getSQLTypeReal() {

		return "REAL";
	}
	@Override
	public String getSQLTypeDateTime() {
		// TODO Auto-generated method stub
		return "DATETIME";
	}

	@Override
	public String getTableCreateDDL(String tableName, List<SQLColumn> columns,
			String[] primaryKeyColumns) {
		StringBuilder builder = new StringBuilder();

		builder.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append("(");

		int size = columns.size();
		for (int i = 0; i < size; i++) {
			
			SQLColumn column = columns.get(i);

			builder.append(column.getColumnName()).append(" ")
					.append(column.getColumnType());

		 
			if (column.isNotNull()) {
				builder.append(" NOT NULL ");
			}

			if (column.isUnique()) {
				builder.append(" UNIQUE ");
			}

			if (i < size - 1) {
				builder.append(",");
			}
			 
			
		}
		
		builder.append(", PRIMARY KEY (");
		
		int primaryKeyColumnCount = primaryKeyColumns.length;
		
		for (int i = 0; i < primaryKeyColumnCount; i++) {
			String column = primaryKeyColumns[i];
			builder.append(column);
			if (i < primaryKeyColumnCount - 1) {
				builder.append(",");
			}
			 
		}
		

		builder.append("))");

		return builder.toString();
	}

}
