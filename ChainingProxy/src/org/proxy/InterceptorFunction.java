package org.proxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

/**
 * This is the Enum of all the interceptors present in the solution.
 * All the interceptors should be added to this Enum with their fully classified class name.
 * @author Ravi Raizada(1006792).
 *
 */
public enum InterceptorFunction
{
	PROXY("com.tcs.vfw.proxy.ConnectionProxy"), 
	MAIL("com.tcs.vfw.proxy.MailProxy");

	private static List<InterceptorFunction> allFunctions;
	private static List<InterceptorFunction> proxyFunction;

	private String className;

	static
	{
		allFunctions = new ArrayList<>(EnumSet.allOf(InterceptorFunction.class));
		proxyFunction = new ArrayList<InterceptorFunction>(Arrays.asList(InterceptorFunction.PROXY));
	}

	InterceptorFunction(String className)
	{
		this.className = className;
	}

	public String getClassName()
	{
		return this.className;
	}

	/**
	 * @return a list of all interceptors - should be used only for specific purpose only 
	 */
	public static List<InterceptorFunction> getAllFunctions()
	{
		return allFunctions;
	}

	/**
	 * @return a list of Connection Proxy interceptor 
	 */
	public static List<InterceptorFunction> getProxyFunction()
	{
		return proxyFunction;
	}
}
