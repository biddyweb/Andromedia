package org.andromda.core.uml14;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.Operation;


/**
 *  Description of the Class
 *
 *@author    amowers
 */
public class ClassifierProxy
	extends ModelElementProxy
	implements UMLClassifier
{
	/**
	 *  Description of the Method
	 *
	 *@param  classifier    Description of the Parameter
	 *@param  scriptHelper  Description of the Parameter
	 *@return               Description of the Return Value
	 */
	public static Classifier newInstance(
		UMLScriptHelper scriptHelper,
		Classifier classifier)
	{
		Class[] interfaces = {
			UMLClassifier.class,
			Classifier.class
		};
		
		return (Classifier)java.lang.reflect.Proxy.newProxyInstance(
			classifier.getClass().getClassLoader(),
			interfaces,
			new ClassifierProxy(classifier, scriptHelper));
	}


	
	protected ClassifierProxy(
		Classifier classifier,
		UMLScriptHelper scriptHelper)
	{
		super(classifier,scriptHelper);
	}



	/**
	 *  Gets the attributes attribute of the ClassifierProxy object
	 *
	 *@return    The attributes value
	 */
	public Collection getAttributes()
	{
		Collection attributes = scriptHelper.getAttributes(modelElement);
		Collection attributeProxies = new Vector();
		
		for (Iterator i = attributes.iterator(); i.hasNext(); )
		{
			attributeProxies.add(
				AttributeProxy.newInstance(scriptHelper,(Attribute)i.next()) 
				);
		}
		
		return attributeProxies;
	}


	/**
	 *  Gets the dependencies attribute of the ClassifierProxy object
	 *
	 *@return    The dependencies value
	 */
	public Collection getDependencies()
	{
		Collection dependencies = scriptHelper.getDependencies(modelElement);
		Collection dependencyProxies = new Vector();
		
		for (Iterator i = dependencies.iterator(); i.hasNext(); )
		{
			dependencyProxies.add(
				DependencyProxy.newInstance(scriptHelper,(Dependency)i.next())
			);
		}
		
		return dependencyProxies;
	}


	/**
	 *  Gets the associationLinks attribute of the ClassifierProxy object
	 *
	 *@return    The associationLinks value
	 */
	public Collection getAssociationLinks()
	{
		return scriptHelper.getAssociationEnds(modelElement);
	}


	/**
	 *  Gets the package attribute of the ClassifierProxy object
	 *
	 *@return    The package value
	 */
	public Object getPackage()
	{
		return this.modelElement;
	}

	public Collection getOperations()
	{
		Collection operations = scriptHelper.getOperations(modelElement);
		Collection operationProxies = new Vector();


		for (Iterator i = operations.iterator(); i.hasNext(); )
		{
			Object proxy = 
				OperationProxy.newInstance(
					scriptHelper, 
					(Operation) (i.next() )
				);
				
			operationProxies.add(proxy);

		}
		
		
		return operationProxies;
	}
	
}

