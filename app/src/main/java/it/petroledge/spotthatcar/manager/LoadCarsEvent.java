package it.petroledge.spotthatcar.manager;

import java.util.Date;

public class LoadCarsEvent {
    private Date mEndFeedDate;
    private Date mStartFeedDate;

    public LoadCarsEvent(Date startFeedDate, Date endFeedDate) {
        mStartFeedDate = startFeedDate;
        mEndFeedDate = endFeedDate;
    }

    public Date getEndFeedDate() {
        return mEndFeedDate;
    }

    public Date getStartFeedDate() {
        return mStartFeedDate;
    }
}
