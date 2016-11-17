package vn.edu.hcmut.its.tripmaester.model;

import java.util.ArrayList;

/**
 * Created by Ken on 12/19/2015.
 */
public class TripComment {
    private String mTripId;
    private String mCommentId;
    private ArrayList<String> mListContent = new ArrayList<String>();

    public TripComment(String tripId) {
        mTripId = tripId;
    }

    public void addComment(String comment) {
        mListContent.add(comment);
    }

    public void setTripId(String tripId) {
        mTripId = tripId;
    }

    public ArrayList<String> getListContent() {
        return mListContent;
    }

    public void setCommentId(String commentId) {
        mCommentId = commentId;
    }

    public String getCommentId() {
        return mCommentId;
    }

    public int getCount() {
        return mListContent.size();
    }

}
