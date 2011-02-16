// license-header java merge-point
//
// Attention: Generated code! Do not modify by hand!!
// Generated by: DaoDefaultException.vsl in andromda-java-cartridge.
//
package org.andromda.howto2.rental;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * 
 */
public class PersonDaoException
    extends java.lang.Exception
{
    private static final long serialVersionUID = 1880853378843554448L;

    /**
     * The default constructor.
     */
    public PersonDaoException()
    {}

    /**
     * Constructs a new instance of PersonDaoException
     *
     * @param throwable the parent Throwable
     */
    public PersonDaoException(Throwable throwable)
    {
        super(findRootCause(throwable));
    }

    /**
     * Constructs a new instance of PersonDaoException
     *
     * @param message the throwable message.
     */
    public PersonDaoException(String message)
    {
        super(message);
    }

    /**
     * Constructs a new instance of PersonDaoException
     *
     * @param message the throwable message.
     * @param throwable the parent of this Throwable.
     */
    public PersonDaoException(String message, Throwable throwable)
    {
        super(message, findRootCause(throwable));
    }

    /**
     * Finds the root cause of the parent exception
     * by traveling up the exception tree
     */
    private static Throwable findRootCause(Throwable th)
    {
        if (th != null)
        {
            // Lets reflectively get any JMX or EJB exception causes.
            try
            {
                Throwable targetException = null;
                // java.lang.reflect.InvocationTargetException
                // or javax.management.ReflectionException
                String exceptionProperty = "targetException";
                if (PropertyUtils.isReadable(th, exceptionProperty))
                {
                    targetException = (Throwable)PropertyUtils.getProperty(th, exceptionProperty);
                }
                else
                {
                    exceptionProperty = "causedByException";
                    //javax.ejb.EJBException
                    if (PropertyUtils.isReadable(th, exceptionProperty))
                    {
                        targetException = (Throwable)PropertyUtils.getProperty(th, exceptionProperty);
                    }
                }
                if (targetException != null)
                {
                    th = targetException;
                }
            }
            catch (Exception ex)
            {
                // just print the exception and continue
                ex.printStackTrace();
            }

            if (th.getCause() != null)
            {
                th = th.getCause();
                th = findRootCause(th);
            }
        }
        return th;
    }

}
