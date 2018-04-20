package org.proxy;
import java.lang.reflect.Method;

import org.store.Store;

public interface MethodInterceptor
{
	Object interceptBefore(Object proxy, Method method, Object[] params, Object realtarget) throws Exception;

	void interceptAfter(Object proxy, Method method, Object[] params, Object realtarget, Store store, Object retObject, Object interceptBeforeReturnObject) throws Exception;
}
