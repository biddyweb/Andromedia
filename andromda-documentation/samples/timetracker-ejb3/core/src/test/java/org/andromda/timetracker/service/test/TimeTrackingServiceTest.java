// license-header java merge-point
//
// Generated by: test/SessionTest.vsl in andromda-ejb3-cartridge.
//
package org.andromda.timetracker.service.test;

//import org.andromda.timetracker.test.EJB3Container;
import org.andromda.timetracker.test.EJB3Container;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.Test;

/**
 * Service test class TimeTrackingServiceTest for testing with TestNG
 * Check the testng.xml for initialization of the EJB3Container before running any tests.
 */
public class TimeTrackingServiceTest
{
    private static final Log logger = LogFactory.getLog(TimeTrackingServiceTest.class);

    /**
     *
     */
    @Test
    public void testFindTimecards()
    {
        try
        {
            org.andromda.timetracker.service.TimeTrackingServiceRemote timeTrackingService = (org.andromda.timetracker.service.TimeTrackingServiceRemote)EJB3Container.getInitialContext("user","password").lookup("TimeTrackingServiceBean/remote");
            //test implementation
        }
        catch (Exception ex)
        {
            logger.warn("Failed test testFindTimecards()", ex);
        }
    }
}