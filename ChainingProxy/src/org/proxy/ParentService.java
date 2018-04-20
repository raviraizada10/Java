package org.proxy;

import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @author Ravi Raizada(1006792)
 * @category - Parent Service- Should be extended by all the services
 * @param <T>
 */
public class ParentService<T>
{
	@SuppressWarnings("unchecked")
	public T getProxyInstance(List<InterceptorFunction> interceptors)
	{
		try
		{
			if (!interceptors.contains(InterceptorFunction.PROXY))
			{
				interceptors.add(0, InterceptorFunction.PROXY);
			}
			else if(interceptors.indexOf(InterceptorFunction.PROXY)!=0){
				interceptors.remove(InterceptorFunction.PROXY);
				interceptors.add(0, InterceptorFunction.PROXY);
			}
			
			if (interceptors != null && interceptors.size() > 0)
			{
				ProxyInvocationHandler invocationHandler = new ProxyInvocationHandler();
				Object[] interceptorObjects = getInterceptors(interceptors);
				invocationHandler.setRealTarget(this);
				invocationHandler.setMethodInterceptors(interceptorObjects);

				final List<Class<?>> interfaces = ClassUtils.getAllInterfaces(this.getClass());

				return (T) Proxy.newProxyInstance(ParentService.class.getClassLoader(), new Class<?>[]
				{
					interfaces.get(0)
				}, invocationHandler);
			}
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private static Object getInterceptor(String interceptors) throws Exception
	{
		return Class.forName(interceptors).newInstance();
	}

	private static Object[] getInterceptors(List<InterceptorFunction> interceptors) throws Exception
	{
		Object[] objInterceptors = new Object[interceptors.size()];
		for (int i = 0; i < interceptors.size(); i++)
		{
			objInterceptors[i] = getInterceptor(interceptors.get(i).getClassName());
		}
		return objInterceptors;
	}
}
