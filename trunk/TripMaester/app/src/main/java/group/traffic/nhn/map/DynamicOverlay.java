package group.traffic.nhn.map;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.*;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

/**
 * @author SinhHuynh
 * @Tag This class creates point or area circle overlay on map
 */
public class DynamicOverlay extends Overlay {
	GeoPoint geoPoint;
	float radius;
	boolean increase = true;
	AnimationListener drawAnimation;
	MapView mapView;
	boolean userLoc;

	public DynamicOverlay(Context ctx, double lon, double lat, final MapView mapView, boolean userLoc) {
		super(ctx);
		geoPoint = new GeoPoint(lat, lon);
		this.mapView = mapView;
		this.userLoc = userLoc;
		if(userLoc) radius = 10;// user location marker
		else radius = 40;// search location marker
		drawAnimation = new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation animation) {
				
//				draw(new Canvas(), mapView, true);
				
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		};
		
	}

	@Override
	protected void draw(Canvas canvas, MapView mapView, boolean arg2) {
		Projection project = mapView.getProjection();
		Point point = new Point();
		project.toPixels(geoPoint, point);
		
		Paint mPaint = new Paint();
		mPaint.setAntiAlias(true);
		if(userLoc)
			mPaint.setARGB(100, 66, 229, 255);
		else
			mPaint.setARGB(50, 255, 100, 100);
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(2);
		canvas.drawCircle(point.x, point.y, radius, mPaint);
//		mPaint.setARGB(255, 66, 229, 255);
//		canvas.drawCircle(point.x, point.y, radius, mPaint);
		mapView.invalidate();
	}

	
}
