package org.andromda.core.metafacade;

/**
 * Fake metafacade number 3 (just used for testing the MetafacadeMappings).
 *
 * @author Chad Brandon
 */
public class Metafacade3Impl
    extends Metafacade2Impl
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public Metafacade3Impl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }
}
