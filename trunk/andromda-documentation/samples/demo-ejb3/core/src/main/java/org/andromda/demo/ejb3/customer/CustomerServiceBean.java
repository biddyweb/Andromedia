// license-header java merge-point
//
// Generated by SessionBeanImpl.vsl in andromda-ejb3-cartridge on 08/06/2014 10:56:22.
// Modify as necessary. If deleted it will be regenerated.
//
package org.andromda.demo.ejb3.customer;

import java.util.Collection;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 * @see CustomerServiceBase
 *
 * Remember to manually configure the local business interface this bean implements if originally you only
 * defined the remote business interface.  However, this change is automatically reflected in the ejb-jar.xml.
 *
 * Do not specify the javax.ejb.Stateless annotation
 * Instead, the session bean is defined in the ejb-jar.xml descriptor.
 * @javax.ejb.Stateless
 */
@WebService(endpointInterface = "org.andromda.demo.ejb3.customer.CustomerServiceWSInterface", serviceName = "CustomerService")
@Stateless
public class CustomerServiceBean
    extends CustomerServiceBase
    implements CustomerServiceRemote
{
    // --------------- Constructors ---------------

    /**
     * Default constructor extending base class default constructor
     */
    public CustomerServiceBean()
    {
        super();
    }

    // -------- Business Methods Impl --------------

    /**
     * @see CustomerServiceBase#addCustomer(Customer)
     */
    @Override
    protected void handleAddCustomer(Customer customer)
        throws CustomerDaoException
    {
        getCustomerDao().create(customer);
    }

    /**
     * @see CustomerServiceBase#getAllCustomers()
     */
    @Override
    protected Customer[] handleGetAllCustomers()
        throws CustomerDaoException
    {
        Collection<?> customers = getCustomerDao().loadAll();
        return customers.toArray(new Customer[customers.size()]);
    }

    // -------- Lifecycle Callback Implementation --------------
}
