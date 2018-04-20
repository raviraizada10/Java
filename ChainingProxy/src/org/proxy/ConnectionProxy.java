package org.proxy;

import java.lang.reflect.Method;

import org.store.Schema;
import org.store.SessionStore;
import org.store.Store;

/**
 * Implementation of MethodInterceptor interface. This implementation provides connection and session management.  
 * @param <T>
 * @author Ravi Raizada (1006792)
 */
public class ConnectionProxy<T> implements MethodInterceptor
{
	@Override
	public Object interceptBefore(Object proxy, Method method, Object[] params, Object realtarget) throws Exception
	{
		Long orgId = null;
		Long appId = null; 
		Store store = null;
		SessionStore sessionStore = null;
		boolean passedSession = false;

		final ServiceMethod methodAnnotation = method.getAnnotation(ServiceMethod.class);

		if (methodAnnotation == null)
		{
			throw new Exception("*** THE METHOD : " + method.getName() + " DOES NOT HAVE SERVICEMETHOD ANNOTATION ");
		}

		if ((params == null) || (params.length == 0))
		{
			throw new Exception("*** THE METHOD : " + method.getName() + " WITH SERVICEMETHOD ANNOATATION HAS NO FORMAL PARAMETERS ***");
		}

		BaseVO baseVO = null;

		passedSession = (params[0] != null);

		if (!passedSession)
		{
			if (params.length > 1)
			{
				if ((params[1] != null))
				{
					if (!(params[1] instanceof BaseVO))
					{
						throw new IllegalArgumentException("*** SECOND PARAMETER IS NOT AN INSTANCE OF BaseVO ***");
					}
				}

				baseVO = (BaseVO) params[1];

				if (baseVO != null)
				{
					orgId = baseVO.getOrgId();
					appId = baseVO.getAppId();

					if (baseVO.getOrgId() == null)
					{
						throw new IllegalArgumentException("*** OrgId IS NULL, PLEASE SET OrgId ***");
					}

					if (baseVO.getAppId() == null)
					{
						throw new IllegalArgumentException("*** AppId IS NULL, PLEASE SET AppId ***");
					}
				}
			}

			store = new Store();
			sessionStore = new SessionStore();
			sessionStore.setO2OGlobalSession(JDBCSessionUtil.getSession(Schema.O2OGLOBAL, appId, orgId));
			sessionStore.setCustomerSession(JDBCSessionUtil.getSession(Schema.CUSTOMER_SCHEMA, appId, orgId));
			store.setSessionStore(sessionStore);

			if ((((sessionStore.getO2OGlobalSession() == null) || (sessionStore.getCustomerSession() == null))))
			{
				throw new Exception("*** NO SUITABLE CONNECTION FOUND ***");
			}

			params[0] = store;
		}
		return baseVO;
	}

	@Override
	public void interceptAfter(Object proxy, Method method, Object[] params, Object realtarget, Store store, Object retObject, Object interceptBeforeReturnObject) throws Exception
	{
		Boolean passedSession = (params[0] != null);
		if ((store != null))
		{
			if (store.getIsException()!=null && store.getIsException())
			{
				if (!passedSession)
				{
					store.getSessionStore().getO2OGlobalSession().rollbackTransaction();
					store.getSessionStore().getCustomerSession().rollbackTransaction();
					store.getSessionStore().getO2OGlobalSession().stopSession();
					store.getSessionStore().getCustomerSession().stopSession();
				}
			}
			else
			{
				if (!passedSession)
				{
					store.getSessionStore().getO2OGlobalSession().commitTransaction();
					store.getSessionStore().getCustomerSession().commitTransaction();
					store.getSessionStore().getO2OGlobalSession().stopSession();
					store.getSessionStore().getCustomerSession().stopSession();
				}

			}
		}
	}
}
