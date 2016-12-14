package cse.its.listeners;

import group.traffice.nhn.common.StaticVariable;
import cse.its.helper.PostWarningInfor;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class OnContextItemClickListener implements OnItemClickListener{
	private Context context;
	public OnContextItemClickListener(Context context){
		this.context = context;
	}
	
	@Override
	public void onItemClick(AdapterView<?> argAdap, View argView, int position, long id) {
		
		StaticVariable.CONTEXT_MENU_DIAGLOG.dismiss();
		String type = "unknow";
		switch(position){
			case 0:
				type ="accident";
				break;
			case 1:
				type ="broken_light";
				break;
			case 2:
				type ="jam";
				break;
			case 3:
				type ="construction";
				break;
			case 4:
				type ="flood";
				break;
			case 5:
				type ="police";
				break;
		}
		
		if(null != StaticVariable.WARNING_POINT){
			StaticVariable.WARNING_POINT.setType(type);
			new PostWarningInfor(context).execute(StaticVariable.WARNING_POINT);
			StaticVariable.WARNING_POINT = null;
		}
	}
	

}
