package bo;

import java.io.Serializable;

public class MyBusinessObject implements BusinessObjectInterface, Serializable
{

	@Override
	public String doExecute(String in) throws Exception
	{
		System.out.println("Here in MyBusinessObject doExecute: input :" + in);
		throw new Exception();
	}

}
