package org.jdbc;

import org.store.ConnectionProvider;
import org.store.Schema;

public class JDBCSessionUtil
{

	public static JDBCSession getGlobalSchemaSession() throws Exception
	{
		return JDBCSessionBuilder.instance().build(ConnectionProvider.getGlobalRawConnection());
	}
	
	public static JDBCSession getO2OGlobalSchemaSession() throws Exception
	{
		return JDBCSessionBuilder.instance().build(ConnectionProvider.getO2OGlobalRawConnection());
	}

	public static JDBCSession getCustomerSchemaSession(Long appId, Long orgId) throws Exception
	{
		return JDBCSessionBuilder.instance().build(ConnectionProvider.getCustomerSchemaRawConnection(appId, orgId));
	}

	public static JDBCSession getSession(final Schema schema, final Long appId, Long orgId) throws Exception
	{
		JDBCSession session = null;

		switch (schema)
		{
		case GLOBAL:
			session = JDBCSessionUtil.getGlobalSchemaSession();
			break;
		case O2OGLOBAL:
			session = JDBCSessionUtil.getO2OGlobalSchemaSession();
			break;
		case CUSTOMER_SCHEMA:
			session = JDBCSessionUtil.getCustomerSchemaSession(appId, orgId);
			break;
		default:
		}

		return session;
	}
}
