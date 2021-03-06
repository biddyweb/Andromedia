package org.andromda.translation.ocl.testsuite;

/**
 * Any unchecked exception that will be thrown when an unexpected translator test exception occurs. Should be thrown
 * when a test is not configured correctly.
 */
public class TranslationTestProcessorException
        extends RuntimeException
{
    private static final long serialVersionUID = 34L;
    /**
     * Constructs an instance of TranslationTestProcessorException.
     *
     * @param th
     */
    public TranslationTestProcessorException(Throwable th)
    {
        super(th);
    }

    /**
     * Constructs an instance of TranslationTestProcessorException.
     *
     * @param msg
     */
    public TranslationTestProcessorException(String msg)
    {
        super(msg);
    }

    /**
     * Constructs an instance of TranslationTestProcessorException.
     *
     * @param msg
     * @param th
     */
    public TranslationTestProcessorException(String msg, Throwable th)
    {
        super(msg, th);
    }

}