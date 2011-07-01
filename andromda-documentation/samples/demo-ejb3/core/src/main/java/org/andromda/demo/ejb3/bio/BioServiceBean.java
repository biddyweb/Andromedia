// license-header java merge-point
//
// Generated by: SessionBeanImpl.vsl in andromda-ejb3-cartridge.
//
package org.andromda.demo.ejb3.bio;

/**
 * @see org.andromda.demo.ejb3.bio.BioServiceBean
 */
/**
 * Do not specify the javax.ejb.Stateless annotation
 * Instead, define the session bean in the ejb-jar.xml descriptor
 * @javax.ejb.Stateless
 */
/**
 * Uncomment to enable webservices for BioServiceBean
 *@javax.jws.WebService(endpointInterface = "org.andromda.demo.ejb3.bio.BioServiceWSInterface")
 */
public class BioServiceBean
    extends org.andromda.demo.ejb3.bio.BioServiceBase
{
    // --------------- Constructors ---------------

    public BioServiceBean()
    {
        super();
    }

    // -------- Business Methods Impl --------------

    /**
     * @see org.andromda.demo.ejb3.bio.BioServiceBase#addBio(org.andromda.demo.ejb3.bio.Bio)
     */
    protected void handleAddBio(org.andromda.demo.ejb3.bio.Bio bio)
        throws java.lang.Exception
    {
        getBioDao().create(bio);
    }

    /**
     * @see org.andromda.demo.ejb3.bio.BioServiceBase#getBio(java.lang.Long)
     */
    protected org.andromda.demo.ejb3.bio.Bio handleGetBio(java.lang.Long id)
        throws java.lang.Exception
    {
        return getBioDao().load(id);
    }

    /**
     * @see org.andromda.demo.ejb3.bio.BioServiceBase#getAllBios()
     */
    protected java.util.Collection handleGetAllBios()
        throws java.lang.Exception
    {
        return getBioDao().loadAll();
    }


    // -------- Lifecycle Callback Impl --------------

}