package org.store;

import com.tcs.vfw.util.jdbc.JDBCSession;

public class SessionStore
{
	private JDBCSession o2oGlobalSession;
	private JDBCSession customerSession;

	public JDBCSession getO2OGlobalSession()
	{
		return this.o2oGlobalSession;
	}

	public void setO2OGlobalSession(JDBCSession o2oGlobalSession)
	{
		this.o2oGlobalSession = o2oGlobalSession;
	}

	public JDBCSession getCustomerSession()
	{
		return this.customerSession;
	}

	public void setCustomerSession(JDBCSession customerSession)
	{
		this.customerSession = customerSession;
	}

}
