package org.andromda.core.simpleuml;


import org.omg.uml.foundation.core.ModelElement;


/**
 * defines those methods missing from Dependency in the UML 1.4 schema that are 
 * needed by the UML2EJB based code generation scripts.
 * 
 * @author Anthony Mowers
 */
public interface UMLDependency
	extends UMLModelElement
{
	public Object getId();
    
	public ModelElement getTargetType();
}
