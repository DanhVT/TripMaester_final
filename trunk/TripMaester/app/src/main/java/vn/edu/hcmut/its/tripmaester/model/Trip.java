/**
 *
 */
package vn.edu.hcmut.its.tripmaester.model;

import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author BienDoan
 */

@SuppressWarnings("serial")
public class Trip implements Serializable {
    private String userName;
    private String dateOpenTrip;
    private String timeStartTrip;
    private String timeEndTrip;
    private String placeStartTrip;
    private String placeEndTrip;
    private String numberLikeTrip;
    private String numberCommentTrip;
    private String privacy;
    private String tripName;
    private String emotion;
    private String linkImage;

    int avaUserCreateTrip;
    private String tripId;
    private String dateTime;
    private String userIdOwner;
    private ArrayList<GeoPoint> lstWayPoints = new ArrayList<>();
    public List<String> lstUserIdLike = new ArrayList<>();

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

    public void setTripName(String name){
        this.tripName = name;
    }
    public String getTripName(){
        return this.tripName;
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

    public void setLinkImage(String linkImage){
        this.linkImage = linkImage;
    }
    public String getLinkImage(){
        return this.linkImage;
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
    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }
    public String getPrivacy() {
        return privacy;
    }
    public String getEmotion() {
        return emotion;
    }

    public void setLsitUserIdLike(ArrayList<String> lstUserIdLike){
        this.lstUserIdLike = lstUserIdLike;
    }
    public boolean isUserLikeTrip(String userId){
//        for (int i=0; i< this.lstUserIdLike.size(); i++){
//            if(this.lstUserIdLike.get(i) == userId){
//                return true;
//            }
//        }
//        return false;
        return this.lstUserIdLike.contains(userId);
    }
}

