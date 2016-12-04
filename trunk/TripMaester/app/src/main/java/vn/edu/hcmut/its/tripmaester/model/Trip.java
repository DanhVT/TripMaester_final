/**
 *
 */
package vn.edu.hcmut.its.tripmaester.model;

import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author BienDoan
 */

@SuppressWarnings("serial")
public class Trip implements Serializable {
    String userName;
    String dateOpenTrip;
    String timeStartTrip;
    String timeEndTrip;
    String placeStartTrip;
    String placeEndTrip;
    String numberLikeTrip;
    String numberCommentTrip;
    String privacy;
    int avaUserCreateTrip;
    private String tripId;
    private String dateTime;
    private String userIdOwner;
    private ArrayList<GeoPoint> lstWayPoints = new ArrayList<>();

    public int getAvaUserCreateTrip() {
        return avaUserCreateTrip;
    }

    public String getDateOpenTrip() {
        return dateOpenTrip;
    }

    public String getDateTime() {
        return dateTime;
    }

    public GeoPoint getEndPoint() {
        if (lstWayPoints.size() > 0) {
            return lstWayPoints.get(lstWayPoints.size() - 1);
        }
        return null;
    }

    public ArrayList<GeoPoint> getLstWayPoints() {
        return lstWayPoints;
    }

    public String getNumberCommentTrip() {
        return numberCommentTrip;
    }

    public String getNumberLikeTrip() {
        return numberLikeTrip;
    }

    public String getPlaceEndTrip() {
        return placeEndTrip;
    }

    public String getPlaceStartTrip() {
        return placeStartTrip;
    }

    public GeoPoint getStartPoint() {
        if (lstWayPoints.size() > 0) {
            return lstWayPoints.get(0);
        }
        return null;
    }

    public String getTimeEndTrip() {
        return timeEndTrip;
    }

    public String getTimeStartTrip() {
        return timeStartTrip;
    }

    public String getTripId() {
        return tripId;
    }

    public String getUserIdOwner() {
        return userIdOwner;
    }

    public String getUserName() {
        return userName;
    }

    public void setAvaUserCreateTrip(int avaUserCreateTrip) {
        this.avaUserCreateTrip = avaUserCreateTrip;
    }

    public void setDateOpenTrip(String dateOpenTrip) {
        this.dateOpenTrip = dateOpenTrip;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setLstWayPoints(ArrayList<GeoPoint> lstWayPoints) {
        this.lstWayPoints = lstWayPoints;
    }

    public void setNumberCommentTrip(String numberCommentTrip) {
        this.numberCommentTrip = numberCommentTrip;
    }

    public void setNumberLikeTrip(String numberLikeTrip) {
        this.numberLikeTrip = numberLikeTrip;
    }

    public void setPlaceEndTrip(String placeEndTrip) {
        this.placeEndTrip = placeEndTrip;
    }

    public void setPlaceStartTrip(String placeStartTrip) {
        this.placeStartTrip = placeStartTrip;
    }

    public void setTimeEndTrip(String timeEndTrip) {
        this.timeEndTrip = timeEndTrip;
    }

    public void setTimeStartTrip(String timeStartTrip) {
        this.timeStartTrip = timeStartTrip;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public void setUserIdOwner(String userIdOwner) {
        this.userIdOwner = userIdOwner;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getPrivacy() {
        return privacy;
    }
}

