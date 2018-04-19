package org.store;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionProvider
{
	public static final Connection getGlobalRawConnection() throws Exception
	{
		try
		{
			final Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/global", "root", "root");
			c.setAutoCommit(false);
			return c;
		}
		catch (final Exception e)
		{
			throw new Exception(e);
		}
	}
	
	public static final Connection getO2OGlobalRawConnection() throws Exception
	{
		try
		{
			final Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/one2one_global", "root", "root");
			c.setAutoCommit(false);
			return c;
		}
		catch (final Exception e)
		{
			throw new Exception(e);
		}
	}

	public static final Connection getCustomerSchemaRawConnection(Long appId, final Long orgId) throws Exception
	{
		try
		{
			final Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/one2one", "root", "root");
			c.setAutoCommit(false);
			return c;
		}
		catch (final Exception e)
		{
			throw e;
		}
	}
	
	public static void main(String[] args)
	{
		try
		{
			ConnectionProvider.getGlobalRawConnection();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
