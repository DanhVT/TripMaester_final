package cse.its.dbhelper;

import vn.edu.hcmut.its.tripmaester.R;
import android.content.Context;

/**
 * @author SinhHuynh
 * @Tag Segment (traffic data type)
 */
public class SegDrawable {
	
	public static final Integer STRAIGTH = 0;
	public static final Integer LEFT = 1;
	public static final Integer RIGHT = 2;

	public double id;
	public double Slong;
	public double Slat;
	public double Elong;
	public double Elat;
	public double speed;
	public long id_st;
	public int CellReal;
	

	public SegDrawable() {
		// TODO Auto-generated constructor stub
		// NodeDrawable node = new NodeDrawable();

	}
	public SegDrawable(NodeDrawable sNode, NodeDrawable eNode){
		this.Elat = eNode.lat;
		this.Elong = eNode.lon;
		this.Slat = sNode.lat;
		this.Slong = sNode.lon;
	}
	public SegDrawable(double id, double Elat, double Elon, double Slat,
			double Slon, double speed, long id_street) {
		// TODO Auto-generated constructor stub
		// NodeDrawable node = new NodeDrawable();
		this.id = id;
		this.Elat = Elat;
		this.Elong = Elon;
		this.Slat = Slat;
		this.Slong = Slon;
		this.speed = speed;
		this.id_st = id_street;
	}

	public SegDrawable(double id, double Elat, double Elon, double Slat,
			double Slon, double speed, long id_street, int RealCell) {
		// TODO Auto-generated constructor stub
		// NodeDrawable node = new NodeDrawable();
		this.id = id;
		this.Elat = Elat;
		this.Elong = Elon;
		this.Slat = Slat;
		this.Slong = Slon;
		this.speed = speed;
		this.id_st = id_street;
		this.CellReal = RealCell;
	}

	public double getSlong() {
		return Slong;
	}

	public void setSlong(double slong) {
		Slong = slong;
	}

	public int getCellReal() {
		return CellReal;
	}

	public void setCellReal(int cellReal) {
		CellReal = cellReal;
	}

	public double getSlat() {
		return Slat;
	}

	public void setSlat(double slat) {
		Slat = slat;
	}

	public double getElong() {
		return Elong;
	}

	public void setElong(double elong) {
		Elong = elong;
	}

	public double getElat() {
		return Elat;
	}

	public void setElat(double elat) {
		Elat = elat;
	}

	public long getId_st() {
		return id_st;
	}

	public void setId_st(long id_st) {
		this.id_st = id_st;
	}

	public void setId(double id) {
		this.id = id;
	}

	public double getId() {
		return this.id;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public double getSpeed() {
		return this.speed;
	}

	public double distance(NodeDrawable node) {
		double result = 0;
		double b = 0, c = 0;
		b = (Slong - Elong) / (Elat - Slat);
		c = -Slong - b * Slat;
		result = Math.abs(node.lon + b * node.lat + c) / Math.sqrt(1 + b * b);
		return result;
	}

	// khoang cach den duong trung truc cua segment
	public double distance1(NodeDrawable node) {
		double result = 0;
		double b = 0, c = 0;
		double Mx = (Slong + Elong) / 2;
		double My = (Slat + Elat) / 2;
		b = -1 / ((Slong - Elong) / (Elat - Slat));
		c = -Mx - b * My;
		result = Math.abs(node.lon + b * node.lat + c) / Math.sqrt(1 + b * b);
		return result;
	}

	// length of segment
	public double length() {
		double length = 0;
		length = Math.sqrt((Slong - Elong) * (Slong - Elong) + (Slat - Elat)
				* (Slat - Elat));
		return length;
	}

	// test if node on segment
	public boolean nodeTest(NodeDrawable node) {
		// Log.i("Test if node on segment",this.distance(node)*110000 + "  " +
		// this.distance1(node));
		// presume that the width of
// street is 120m
		return (this.distance(node) * 110000 <= 60)// presume that the width of
				// street is 120m
				&& this.distance1(node) <= this.length() / 2;
	}

	// ### angle of 2 segments
	public static double angle(SegDrawable seg1, SegDrawable seg2) {
		double angle = 0;

		double a1 = 1, a2 = 1;
		double b1 = 0, b2 = 0;
		if(seg1.Elat != seg1.Slat)
			b1 = (seg1.Slong - seg1.Elong) / (seg1.Elat - seg1.Slat);
		else{
			b1 = 1;
			a1 = 0;
		}
		if(seg2.Elat != seg2.Slat)
			b2 = (seg2.Slong - seg2.Elong) / (seg2.Elat - seg2.Slat);
		else{
			b2 = 1;
			a2 = 0;
		}
		double cos = (a1 * a2 + b1 * b2) / Math.sqrt(a1 * a1 + b1 * b1)
				/ Math.sqrt(a2 * a2 + b2 * b2);
		angle = Math.toDegrees(Math.acos(cos));
		
		return angle;
	}
	
	// ### detect side  (left - right)
	// presume that user moves from start node to end node
	public boolean isLeft(NodeDrawable node){
	     return (((Elong - Slong)*(node.lat - Slat) - (Elat - Slat)*(node.lon - Slong)) > 0);
	}
	
	// ### detect direction
	public String direction(SegDrawable seg, Context context){
		String direction = "";
		double angle = angle(this, seg);
		if((angle >= 40 && angle <= 140) || (angle >= -140 && angle <= 40)){
			if(isLeft(new NodeDrawable(0, seg.Elong, seg.Elat)))
				direction = context.getString(R.string.turn_left);
			else direction = context.getString(R.string.turn_right);
		}
		else direction = context.getString(R.string.go_straight_ahead);
		
		return direction;
	}
	
	public int directionMode(SegDrawable seg, Context context){
		int direction = STRAIGTH;
		double angle = angle(this, seg);
		if((angle >= 40 && angle <= 140) || (angle >= -140 && angle <= 40)){
			if(isLeft(new NodeDrawable(0, seg.Elong, seg.Elat)))
				direction = LEFT;
			else direction = RIGHT;
		}	
		return direction;
	}
}
