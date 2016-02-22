package com.samsung.ssc.database.sql;

public final class SQLColumn {

	private String columnName;
	private String columnType;
	private boolean primaryKey = false;
	private boolean isNotNull = false;
	private boolean isUnique = false;

	private SQLColumn(String columnName, String columnType, boolean primaryKey,
			boolean isNotNull, boolean isUnique) {

		this.columnName = columnName;
		this.columnType = columnType;
		this.primaryKey = primaryKey;
		this.isNotNull = isNotNull;
		this.isUnique = isUnique;

	}

	public String getColumnName() {
		return columnName;
	}

	public String getColumnType() {
		return columnType;
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public boolean isNotNull() {
		return isNotNull;
	}

	public boolean isUnique() {
		return isUnique;
	}

	public static class SQLColumnBuilder {

		private String builderColumnName;
		private String builderColumnType;
		private boolean builderIsPrimaryKey = false;
		private boolean builderIsNotNull = false;
		private boolean builderIsUnique = false;

		public SQLColumnBuilder(final String columnName, final String columnType) {

			this.builderColumnName = columnName;
			this.builderColumnType = columnType;

		}

		public SQLColumnBuilder isPrimaryKey(boolean isPrimaryKey) {
			builderIsPrimaryKey = isPrimaryKey;
			return this;
		}

		public SQLColumnBuilder isNotNull(boolean isNotNull) {
			builderIsNotNull = isNotNull;
			return this;
		}

		public SQLColumnBuilder isUnique(boolean isUnique) {
			builderIsUnique = isUnique;
			return this;
		}

		public SQLColumn createSQLColumn() {
			return new SQLColumn(builderColumnName, builderColumnType,
					builderIsPrimaryKey, builderIsNotNull, builderIsUnique);
		}
	}

}
