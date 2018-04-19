package org.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public interface JDBCSession
{
	void commitTransaction() throws SQLException;

	void rollbackTransaction() throws SQLException;

	void stopSession() throws SQLException;

	Connection getConnection() throws SQLException;

	Query prepareQuery(String sql) throws SQLException;

	InsertQuery prepareInsertQuery() throws SQLException;

	UpdateQuery prepareUpdateQuery() throws SQLException;

	SelectQuery prepareSelectQuery() throws SQLException;
}
