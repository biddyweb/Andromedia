// license-header java merge-point
//
// Generated by: SessionBeanImpl.vsl in andromda-ejb3-cartridge.
//
package org.andromda.demo.ejb3.rental;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSender;
import javax.jms.QueueSession;

/**
 * @see org.andromda.demo.ejb3.rental.RentalServiceBean
 */
/**
 * Do not specify the javax.ejb.Stateless annotation
 * Instead, define the session bean in the ejb-jar.xml descriptor
 * @javax.ejb.Stateless
 */
/**
 * Uncomment to enable webservices for RentalServiceBean
 *@javax.jws.WebService(endpointInterface = "org.andromda.demo.ejb3.rental.RentalServiceWSInterface")
 */
public class RentalServiceBean
    extends org.andromda.demo.ejb3.rental.RentalServiceBase
{
    // --------------- Constructors ---------------

    public RentalServiceBean()
    {
        super();
    }

    // -------- Business Methods Impl --------------

    /**
     * @see org.andromda.demo.ejb3.rental.RentalServiceBase#addCar(org.andromda.demo.ejb3.rental.RentalCar)
     */
    protected void handleAddCar(org.andromda.demo.ejb3.rental.RentalCar car)
        throws Exception
    {
        //TODO: put your implementation here.
        throw new java.lang.UnsupportedOperationException("org.andromda.demo.ejb3.rental.RentalServiceBean.handleAddCar(org.andromda.demo.ejb3.rental.RentalCar car) Not implemented!");
    }

    /**
     * @see org.andromda.demo.ejb3.rental.RentalServiceBase#getAllCars()
     */
    protected java.util.List handleGetAllCars()
        throws Exception
    {
        //TODO: put your implementation here.
        // Dummy return value, just that the file compiles
        return null;
    }

    /**
     * @see org.andromda.demo.ejb3.rental.RentalServiceBase#getCustomersByName(java.lang.String)
     */
    protected java.util.List handleGetCustomersByName(java.lang.String name)
        throws Exception
    {
        //TODO: put your implementation here.
        // Dummy return value, just that the file compiles
        return null;
    }

    /**
     * @see org.andromda.demo.ejb3.rental.RentalServiceBase#processRental(org.andromda.demo.ejb3.rental.RentalCar, int)
     */
    protected void handleProcessRental(org.andromda.demo.ejb3.rental.RentalCar car, int leasePeriod)
        throws Exception
    {
        QueueConnection connection = null;
        QueueSession session = null;
        QueueSender sender = null;

        try
        {
            connection = queueFactory.createQueueConnection();
            session = connection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            sender = session.createSender((Queue)paymentProcessor);

            ObjectMessage message = session.createObjectMessage(car);
            sender.send(message);
        }
        catch (JMSException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            if (sender != null) {
                try
                {
                    sender.close();
                }
                catch (JMSException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (session != null) {
                try
                {
                    session.close();
                }
                catch (JMSException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try
                {
                    connection.close();
                }
                catch (JMSException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }


    // -------- Lifecycle Callback Impl --------------

}