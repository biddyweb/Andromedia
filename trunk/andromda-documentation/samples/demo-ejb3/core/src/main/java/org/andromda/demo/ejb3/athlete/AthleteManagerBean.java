// license-header java merge-point
//
// Generated by: SessionBeanImpl.vsl in andromda-ejb3-cartridge.
//
package org.andromda.demo.ejb3.athlete;

/**
 * @see org.andromda.demo.ejb3.athlete.AthleteManagerBean
 */
/**
 * Do not specify the javax.ejb.Stateless annotation
 * Instead, define the session bean in the ejb-jar.xml descriptor
 * @javax.ejb.Stateless
 */
/**
 * Uncomment to enable webservices for AthleteManagerBean
 *@javax.jws.WebService(endpointInterface = "org.andromda.demo.ejb3.athlete.AthleteManagerWSInterface")
 */
public class AthleteManagerBean
    extends org.andromda.demo.ejb3.athlete.AthleteManagerBase
{
    // --------------- Constructors ---------------

    public AthleteManagerBean()
    {
        super();
    }

    // -------- Business Methods Impl --------------

    /**
     * @see org.andromda.demo.ejb3.athlete.AthleteManagerBase#addTrackAthlete(org.andromda.demo.ejb3.athlete.TrackAthlete)
     */
    protected void handleAddTrackAthlete(org.andromda.demo.ejb3.athlete.TrackAthlete trackAthlete)
        throws java.lang.Exception
    {
        getTrackAthleteDao().create(trackAthlete);
    }

    /**
     * @see org.andromda.demo.ejb3.athlete.AthleteManagerBase#getAllAthletes()
     */
    protected java.util.Collection handleGetAllAthletes()
        throws java.lang.Exception
    {
        return getTrackAthleteDao().loadAll();
    }


    // -------- Lifecycle Callback Impl --------------

}
