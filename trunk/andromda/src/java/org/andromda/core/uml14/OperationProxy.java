package org.andromda.core.uml14;

import java.lang.reflect.Method;

import java.util.Collection;


import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.datatypes.VisibilityKind;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;

/**
 *  Description of the Class
 *
 *@author    amowers
 */
public class OperationProxy
	extends ModelElementProxy
	implements UMLOperation
{
	private UMLScriptHelper scriptHelper;
	private Operation operation;
	
	private static Method getVisibilityMethod;
	private static VisibilityKind PRIVATE = 
		new JavaVisibility("private");
	private static VisibilityKind PUBLIC =
		new JavaVisibility("public");
	private static VisibilityKind PROTECTED =
		new JavaVisibility("protected");
	private static VisibilityKind PACKAGE =
		new JavaVisibility("");
	
	
	/**
	 *  Description of the Method
	 *
	 *@param  operation    Description of the Parameter
	 *@param  scriptHelper  Description of the Parameter
	 *@return               Description of the Return Value
	 */
	public static Operation newInstance(
		UMLScriptHelper scriptHelper,
		Operation operation)
	{
		Class[] interfaces = new Class[]
			{
			UMLOperation.class,
			Operation.class
			};

		return (Operation)java.lang.reflect.Proxy.newProxyInstance(
			operation.getClass().getClassLoader(),
			interfaces,
			new OperationProxy(operation, scriptHelper));
	}


	
	private OperationProxy(
		Operation operation,
		UMLScriptHelper scriptHelper)
	{
		super(operation,scriptHelper);
	}


	public Object getId()
	{
		return this.operation;
	}

	public VisibilityKind getVisibility()
	{
		VisibilityKind visibility;
		
		visibility = operation.getVisibility();
		if (VisibilityKindEnum.VK_PRIVATE.equals(visibility))
		{
			return PRIVATE;
		} 
		else if (VisibilityKindEnum.VK_PROTECTED.equals(visibility))
		{
			return PROTECTED;
		}
		else if (VisibilityKindEnum.VK_PUBLIC.equals(visibility))
		{
			return PUBLIC;
		}
		
		return PACKAGE;		
	}
	
}

