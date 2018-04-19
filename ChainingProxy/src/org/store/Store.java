package org.store;

import java.util.List;

import com.tcs.vfw.util.connection.SessionStore;

public class Store
{
	private SessionStore sessionStore;
	
	private List<Object> mailList;

	public SessionStore getSessionStore()
	{
		return sessionStore;
	}

	public void setSessionStore(SessionStore sessionStore)
	{
		this.sessionStore = sessionStore;
	}

	public List<Object> getMailList()
	{
		return mailList;
	}

	public void setMailList(List<Object> mailList)
	{
		this.mailList = mailList;
	}
	
	private Boolean isException;

	public Boolean getIsException()
	{
		return isException;
	}

	public void setIsException(Boolean isException)
	{
		this.isException = isException;
	}

}
