// license-header java merge-point
//
// Generated by SessionBeanImpl.vsl in andromda-ejb3-cartridge on 08/05/2014 14:24:56.
// Modify as necessary. If deleted it will be regenerated.
//
package org.andromda.timetracker.service;

import java.util.List;
import org.andromda.timetracker.vo.TimecardSearchCriteriaVO;
import org.andromda.timetracker.vo.TimecardSummaryVO;

/**
 * @see TimeTrackingServiceBase
 *
 * Remember to manually configure the local business interface this bean implements if originally you only
 * defined the remote business interface.  However, this change is automatically reflected in the ejb-jar.xml.
 *
 * Do not specify the javax.ejb.Stateless annotation
 * Instead, define the session bean in the ejb-jar.xml descriptor
 * @javax.ejb.Stateless
 */
// Uncomment to enable webservices for TimeTrackingServiceBean
// @javax.jws.WebService(endpointInterface = "org.andromda.timetracker.service.TimeTrackingServiceWSInterface", serviceName = "TimeTrackingService")
public class TimeTrackingServiceBean
    extends TimeTrackingServiceBase
    implements TimeTrackingServiceRemote
{
    // --------------- Constructors ---------------

    /**
     * Default constructor extending base class default constructor
     */
    public TimeTrackingServiceBean()
    {
        super();
    }

    // -------- Business Methods Impl --------------

    /**
     * @see TimeTrackingServiceBase#findTimecards(TimecardSearchCriteriaVO)
     */
    @Override
    protected TimecardSummaryVO[] handleFindTimecards(TimecardSearchCriteriaVO criteria)
        throws Exception
    {
        List<?> timecards = this.getTimecardDao().findByCriteria(criteria);
        this.getTimecardDao().toTimecardSummaryVOCollection(timecards);
        return timecards.toArray(new TimecardSummaryVO[0]);
    }

    // -------- Lifecycle Callback Implementation --------------
}
