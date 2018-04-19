package org.proxy;


public class ProxyFactory<T>
{/*
	@SuppressWarnings("unchecked")
	private T getProxyObject(Object inObject, String[] interceptors) throws Throwable
	{
		if (interceptors != null && interceptors.length > 0)
		{
			ProxyInvocationHandler invocationHandler = new ProxyInvocationHandler();
			Object[] interceptorObjects = getInterceptors(interceptors);
			invocationHandler.setRealTarget(inObject);
			invocationHandler.setMethodInterceptors(interceptorObjects);
			final Class<?>[] interfaces = inObject.getClass().getInterfaces();

			return (T) Proxy.newProxyInstance(inObject.getClass().getClassLoader(), new Class<?>[]
			{
				interfaces[0]
			}, invocationHandler);
		}
	}

	private static Object getInterceptor(String interceptors) throws Exception
	{
		return Class.forName(interceptors).newInstance();
		InterceptorFunction.PROXY
	}

	private static Object[] getInterceptors(String[] interceptors) throws Exception
	{
		Object[] objInterceptors = new Object[interceptors.length];
		for (int i = 0; i < interceptors.length; i++)
		{
			objInterceptors[i] = getInterceptor(interceptors[i]);
		}
		return objInterceptors;
	}

	private static Object getTargetObject(String className) throws Exception
	{
		return Class.forName(className).newInstance();
	}
*/}
