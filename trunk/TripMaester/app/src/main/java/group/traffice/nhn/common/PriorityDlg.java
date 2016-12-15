package group.traffice.nhn.common;



import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vn.edu.hcmut.its.tripmaester.R;

public class PriorityDlg extends Dialog {
	private Context context;
	private ListView dlg_priority_lvw = null;
	private List<Bitmap> lst_image = null;
	private ArrayList<HashMap<String, Object>> priorityList;
	private final SimpleAdapter.ViewBinder mViewBinder =
		    new SimpleAdapter.ViewBinder() {
		        @Override
		        public boolean setViewValue(
		                final View view,
		                final Object data,
		                final String textRepresentation) {

		            if (view instanceof ImageView) {
		                ((ImageView) view).setImageDrawable((Drawable) data);
		                return true;
		            }

		            return false;
		        }
		    };
	public PriorityDlg(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public PriorityDlg(Context context, int theme,List<Bitmap> lstImages) {
		super(context, theme);
		this.context = context;
		lst_image = lstImages;
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.custom_dialog);
		dlg_priority_lvw = (ListView) findViewById(R.id.dlg_priority_lvw);
		// ListView
		
		priorityList = new ArrayList<HashMap<String, Object>>();
		getPriorityList();
				    
		SimpleAdapter adapter = new SimpleAdapter(context, priorityList,
				R.layout.custom_listview, new String[] { "list_priority_img",
						"list_priority_value" }, new int[] {
						R.id.list_priority_img, R.id.list_priority_value });
		
		adapter.setViewBinder(mViewBinder);
		dlg_priority_lvw.setAdapter(adapter);
		// ListView
		dlg_priority_lvw
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@SuppressLint("NewApi")
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						ImageView image_view = new ImageView(context);
						image_view.setMinimumHeight(400);
						image_view.setMinimumHeight(300);
//						Drawable image_view_drawable = new BitmapDrawable(context.getResources(), lst_image.get(position));
						Bitmap large_image = lst_image.get(position);
//						large_image.setHeight(400);
//						large_image.setWidth(400);
						image_view.setImageBitmap(large_image);//setImageDrawable(image_view_drawable);

//						LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
//						image_view.setLayoutParams(layoutParams);
//						image_view.requestLayout();
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
						builder.setView(image_view).setNegativeButton("Close", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
							}
						});
						AlertDialog alertDialog = builder.create();
						
//						   WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//						    lp.copyFrom(alertDialog.getWindow().getAttributes());
//						    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//						    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
						    alertDialog.show();
//						    alertDialog.getWindow().setAttributes(lp);
						    
//						alertDialog.getWindow().setLayout(600, 800);
//						alertDialog.show();
						    
					}
				});
	}

	private void getPriorityList() {
		
		for (int i= 0;i < lst_image.size();i++){
			Drawable image_view_drawable = new BitmapDrawable(context.getResources(), lst_image.get(i));
			HashMap<String, Object> map1 = new HashMap<String, Object>();
			map1.put("list_priority_img",image_view_drawable);// priority_not_important);
			map1.put("list_priority_value", "");
			priorityList.add(map1);
		}
		
	}
}
