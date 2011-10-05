// license-header java merge-point
//
// Generated by: SessionBeanImpl.vsl in andromda-ejb3-cartridge.
//
package org.andromda.demo.ejb3.user;

/**
 * @see org.andromda.demo.ejb3.user.UserManagerBean
 */
/**
 * Do not specify the javax.ejb.Stateless annotation
 * Instead, define the session bean in the ejb-jar.xml descriptor
 * @javax.ejb.Stateless
 */
/**
 * Uncomment to enable webservices for UserManagerBean
 *@javax.jws.WebService(endpointInterface = "org.andromda.demo.ejb3.user.UserManagerWSInterface")
 */
public class UserManagerBean
    extends org.andromda.demo.ejb3.user.UserManagerBase
{
    // --------------- Constructors ---------------

    public UserManagerBean()
    {
        super();
    }

    // -------- Business Methods Impl --------------

    /**
     * @see org.andromda.demo.ejb3.user.UserManagerBase#addUser(org.andromda.demo.ejb3.user.User)
     */
    protected void handleAddUser(org.andromda.demo.ejb3.user.User user)
        throws Exception
    {
        getUserDao().create(user);
    }

    /**
     * @see org.andromda.demo.ejb3.user.UserManagerBase#getUser(java.lang.String)
     */
    protected org.andromda.demo.ejb3.user.User handleGetUser(java.lang.String principalId)
        throws Exception
    {
        return getUserDao().load(principalId);
    }

    /**
     * @see org.andromda.demo.ejb3.user.UserManagerBase#deleteUser(java.lang.String)
     */
    protected void handleDeleteUser(java.lang.String principalId)
        throws Exception
    {
        getUserDao().remove(principalId);
    }


    // -------- Lifecycle Callback Impl --------------

}
