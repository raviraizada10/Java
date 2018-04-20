package org.proxy;

import java.lang.reflect.Method;

import org.store.Store;

public class MailProxy<T> implements MethodInterceptor
{
	@Override
	public Object interceptBefore(Object proxy, Method method, Object[] params, Object realtarget) throws Exception
	{
		System.out.println("Hi From Mail API Before intercept");
		return "Hi From Mail API Before intercept";
		
	}

	@Override
	public void interceptAfter(Object proxy, Method method, Object[] params, Object realtarget, Store store, Object retObject, Object interceptBeforeReturnObject) throws Exception
	{
		System.out.println("Hi From Mail API after intercept");
		
	}
}
