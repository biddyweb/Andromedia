// license-header java merge-point
// Generated by andromda-jsf cartridge (controllers\ControllerImpl.java.vsl) on 02/15/2011 16:31:52-0200
package org.andromda.timetracker.web.timecardsearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.andromda.timetracker.domain.TimecardStatus;
import org.andromda.timetracker.vo.TimecardSearchCriteriaVO;
import org.andromda.timetracker.vo.TimecardSummaryVO;
import org.andromda.timetracker.vo.UserVO;
import org.andromda.timetracker.vo.UserVOComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @see org.andromda.timetracker.web.timecardsearch.SearchController
 */
public class SearchControllerImpl
    extends SearchController
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 3544295024145788800L;

    private static final Log logger = LogFactory.getLog(SearchController.class);

    private static final String ALL_STRING = "-- All --";

    /**
     * @see org.andromda.timetracker.web.timecardsearch.SearchController#populateSearchScreen(org.andromda.timetracker.web.timecardsearch.PopulateSearchScreenForm)
     */
    @Override
    public void populateSearchScreen(PopulateSearchScreenForm form)
    {
        logger.debug("form: " + form);

        // Get list of users and add the "All" option at the top
        UserVO[] users = getUserService().getAllUsers();
        Arrays.sort(users, new UserVOComparator());
        //List<UserVO> users = new ArrayList<UserVO>(getUserService().getAllUsers());
        //Collections.sort(users, new UserVOComparator());
        List<UserVO> userList = new ArrayList<UserVO>(Arrays.asList(users));
        userList.add(0, new UserVO(null, ALL_STRING, null, null));

        // Populate submitter and approver dropdowns
        form.setSubmitterBackingList(userList, "id", "username");
        form.setApproverBackingList(userList, "id", "username");

        // Populate status dropdown
        List<String> statusLabels = new ArrayList<String>(TimecardStatus.literals());
        List<String> statusValues = new ArrayList<String>(TimecardStatus.literals());
        statusLabels.add(0, ALL_STRING);
        statusValues.add(0, "");
        form.setStatusLabelList(statusLabels.toArray());
        form.setStatusValueList(statusValues.toArray());

        // Populate timecard summaries
        TimecardStatus status = null;
        if (StringUtils.isNotBlank(form.getStatus())) {
            status = TimecardStatus.fromString(form.getStatus());
        }
        TimecardSearchCriteriaVO criteria = new TimecardSearchCriteriaVO(
                form.getSubmitter(),
                form.getApprover(),
                status,
                form.getStartDateMinimum(),
                form.getStartDateMaximum());

        TimecardSummaryVO[] timecards = getTimeTrackingService().findTimecards(criteria);
        //Collection<? extends TimecardSummaryVO> timecards = getTimeTrackingService().findTimecards(criteria);
        form.setTimecardSummaries(timecards);
        //form.setTimecardSummaries((TimecardSummaryVO[]) timecards.toArray());
    }

    /**
     * @see org.andromda.timetracker.web.timecardsearch.SearchController#initializeTimecardId(org.andromda.timetracker.web.timecardsearch.InitializeTimecardIdForm
)
     */
    @Override
    public void initializeTimecardId(InitializeTimecardIdForm form)
    {
        form.setTimecardId(form.getId());
    }
}