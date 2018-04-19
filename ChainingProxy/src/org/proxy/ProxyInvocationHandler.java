package org.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.store.Store;

/**
 * @author Ravi Raizada(1006792)
 * This is the invocation handler for all the method interceptors created in the solution.
 */

public class ProxyInvocationHandler implements InvocationHandler
{
	private Object realtarget = null;

	public void setRealTarget(Object realtarget_)
	{
		this.realtarget = realtarget_;
	}

	Object[] methodInterceptors = null;

	public void setMethodInterceptors(Object[] methodInterceptors_)
	{
		this.methodInterceptors = methodInterceptors_;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		try
		{
			Object[] retInterceptBefore = null;

			if (methodInterceptors != null && methodInterceptors.length > 0)
			{
				retInterceptBefore = new Object[methodInterceptors.length];
				for (int i = 0; i < methodInterceptors.length; i++)
				{
					if (methodInterceptors[i] != null)
					{
						retInterceptBefore[i] = ((MethodInterceptor) methodInterceptors[i]).interceptBefore(proxy, method, args, realtarget);
					}
				}
			}
			Object retObject = method.invoke(realtarget, args);
			if (methodInterceptors != null)
			{
				for (int i = 0; i < methodInterceptors.length; i++)
				{
					if (methodInterceptors[i] != null)
					{
						((MethodInterceptor) methodInterceptors[i]).interceptAfter(proxy, method, args, realtarget, (Store) args[0], retObject, retInterceptBefore[i]);
					}
				}
			}
			return retObject;
		}
		catch (InvocationTargetException e)
		{
			throw e.getTargetException();
		}
		catch (Exception e)
		{
			throw e;
		}
	}
}
