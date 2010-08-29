// license-header java merge-point
//
// Generated by: MetafacadeLogicImpl.vsl in andromda-meta-cartridge.
package org.andromda.cartridges.jsf.metafacades;

import org.andromda.cartridges.jsf.JSFProfile;
import org.andromda.metafacades.uml.StateVertexFacade;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.utils.StringUtilsHelper;

/**
 * 
 * MetafacadeLogic implementation for org.andromda.cartridges.jsf.metafacades.JSFExceptionHandler.
 *
 * @see org.andromda.cartridges.jsf.metafacades.JSFExceptionHandler
 */
public class JSFExceptionHandlerLogicImpl
    extends JSFExceptionHandlerLogic
{

    /**
     * Public constructor for JSFExceptionHandlerLogicImpl
     * @see org.andromda.cartridges.jsf.metafacades.JSFExceptionHandler
     */
    public JSFExceptionHandlerLogicImpl (Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * <p>
     * The key to use with this handler's message resource bundle that
     * will retrieve the error message template for this exception.
     * </p>
     * @see org.andromda.cartridges.jsf.metafacades.JSFExceptionHandler#getExceptionKey()
     */
    protected String handleGetExceptionKey()
    {
        final String type = getExceptionType();
        final int dotIndex = type.lastIndexOf('.');

        // the dot may not be the last character
        return StringUtilsHelper.toResourceMessageKey((dotIndex < type.length() - 1)
            ? type.substring(dotIndex + 1) : type);
    }

    /**
     * <p>
     * The module-relative URI to the resource that will complete the
     * request/response if this exception occurs.
     * </p>
     * @see org.andromda.cartridges.jsf.metafacades.JSFExceptionHandler#getExceptionPath()
     */
    protected String handleGetExceptionPath()
    {
        final StateVertexFacade target = getTarget();
        if (target instanceof JSFForward)
            return (target).getFullyQualifiedNamePath() + ".jsf";
        else if (target instanceof JSFFinalState)
            return ((JSFFinalState)target).getFullyQualifiedNamePath();
        else
            return "";
    }

    /**
     * <p>
     * Fully qualified Java class name of the exception type to
     * register with this handler.
     * </p>
     * @see org.andromda.cartridges.jsf.metafacades.JSFExceptionHandler#getExceptionType()
     */
    protected String handleGetExceptionType()
    {
        final Object value = this.findTaggedValue(JSFProfile.TAGGEDVALUE_EXCEPTION_TYPE);
        return value == null ? "" : value.toString();
    }

    /**
     * 
     * @see org.andromda.cartridges.jsf.metafacades.JSFExceptionHandler#getMessageKey()
     */
    protected String handleGetMessageKey()
    {
        final UseCaseFacade useCase = this.getUseCase();
        return useCase != null ? StringUtilsHelper.toResourceMessageKey(useCase.getName()) : null;
    }

}