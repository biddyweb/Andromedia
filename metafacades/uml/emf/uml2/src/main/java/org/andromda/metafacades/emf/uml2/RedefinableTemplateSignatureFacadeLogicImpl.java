// license-header java merge-point
//
// Generated by: MetafacadeLogicImpl.vsl in andromda-meta-cartridge.
package org.andromda.metafacades.emf.uml2;

import org.eclipse.uml2.RedefinableTemplateSignature;

/**
 *
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.RedefinableTemplateSignatureFacade.
 *
 * @see org.andromda.metafacades.uml.RedefinableTemplateSignatureFacade
 */
public class RedefinableTemplateSignatureFacadeLogicImpl
    extends RedefinableTemplateSignatureFacadeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * Public constructor for RedefinableTemplateSignatureFacadeLogicImpl
     * @param metaObject
     * @param context
     * @see org.andromda.metafacades.uml.RedefinableTemplateSignatureFacade
     */
    public RedefinableTemplateSignatureFacadeLogicImpl (RedefinableTemplateSignature metaObject, String context)
    {
        super(metaObject, context);
    }
    /**
     *
     * @return shieldedElement(redefinableTemplateSignature.getRedefinitionContexts().get(0))
     * @see org.andromda.metafacades.uml.RedefinableTemplateSignatureFacade#getClassifier()
     */
    protected Object handleGetClassifier()
    {
        final RedefinableTemplateSignature redefinableTemplateSignature = (RedefinableTemplateSignature)metaObject;
        if(redefinableTemplateSignature.getRedefinitionContexts().isEmpty())
        {
            return null;
        }
        return shieldedElement(redefinableTemplateSignature.getRedefinitionContexts().get(0));
    }

}