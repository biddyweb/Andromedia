package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ServiceFacade;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController
 */
public class StrutsControllerLogicImpl
        extends StrutsControllerLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsController
{
    // ---------------- constructor -------------------------------

    public StrutsControllerLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsController ...

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController#getFullPath()()
     */
    public java.lang.String handleGetFullPath()
    {
        return '/' + getFullyQualifiedName().replace('.', '/');
    }

    // ------------- relations ------------------

    protected Collection handleGetServices()
    {
        final Collection servicesList = new LinkedList();
        final Collection dependencies = getDependencies();
        for (Iterator iterator = dependencies.iterator(); iterator.hasNext();)
        {
            DependencyFacade dependency = (DependencyFacade) iterator.next();
            ModelElementFacade target = dependency.getTargetElement();
            if (target instanceof ServiceFacade)
                servicesList.add(target);
        }
        return servicesList;
    }

    protected Object handleGetUseCase()
    {
        final Collection useCases = getModel().getAllUseCases();
        for (Iterator iterator = useCases.iterator(); iterator.hasNext();)
        {
            StrutsUseCase strutsUseCase = (StrutsUseCase) iterator.next();
            if (this.equals(strutsUseCase.getController()))
                return strutsUseCase;
        }
        return null;
    }
}
