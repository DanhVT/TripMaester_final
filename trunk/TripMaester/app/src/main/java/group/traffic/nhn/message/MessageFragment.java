package group.traffic.nhn.message;

import java.util.ArrayList;

import vn.edu.hcmut.its.tripmaester.R;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MessageFragment extends Fragment {

	private ArrayList<MessageItem> mMessages;
	private ListView mViewDialogContent;
	private AlertDialog  mDialog = null;
	private MessageListAdapter mMessageDetailAdapter;
	public MessageFragment(){}
	
	/**
	 * load fiends
	 * @return
	 */
	private ArrayList<MessageItem> getMessagesOfUser(){
		ArrayList<MessageItem> result = new ArrayList<>();
		
		MessageItem item1 = new MessageItem(getActivity(),"Di choi cho vui di",R.drawable.user1, true, 10, "03 Mat","");
		MessageItem item2 = new MessageItem(getActivity(),"Co di dau khong",R.drawable.user2, true, 1, "01 Feb","");
		MessageItem item3 = new MessageItem(getActivity(),"Okie",R.drawable.user3, true, 1, "10 Mat","");
		MessageItem item4 = new MessageItem(getActivity(),"Co nhau khong?",R.drawable.user4, true, 20, "02 May 2014","");
		MessageItem item5 = new MessageItem(getActivity(),"Di da banh",R.drawable.user1, true, 5, "10 Mat","");
		MessageItem item6 = new MessageItem(getActivity(),"Thien bong",R.drawable.user_profile_temp, true, 6, "12 Mat","");
		MessageItem item7 = new MessageItem(getActivity(),"haha",R.drawable.user_profile_temp, true, 10, "20 Mat","");
		MessageItem item8 = new MessageItem(getActivity(),"yes",R.drawable.user_profile_temp, true, 10, "10 Feb","");
		
		result.add(item1);
		result.add(item2);
		result.add(item3);
		result.add(item4);
		result.add(item5);
		result.add(item6);
		result.add(item7);
		result.add(item8);
		
		mMessages = result;
		return result;
	}
	
	private void loadMessageOfOneUser(int position){
		ArrayList<MessageItem> result = new ArrayList<>();
		
		MessageItem item = mMessages.get(position);
		
		MessageItem item1 = new MessageItem(getActivity(),"Di choi cho vui di",item.getIcon(), true, 10, "03 Mat","");
		MessageItem item2 = new MessageItem(getActivity(),"Co di dau khong",R.drawable.user_profile_temp, true, 1, "01 Feb","");
		MessageItem item3 = new MessageItem(getActivity(),"Okie",item.getIcon(), true, 1, "10 Mat","");
		MessageItem item4 = new MessageItem(getActivity(),"Co nhau khong?",item.getIcon(), true, 20, "02 May 2014","");
		MessageItem item5 = new MessageItem(getActivity(),"Di da banh",R.drawable.user_profile_temp, true, 5, "10 Mat","");
		
		result.add(item1);
		result.add(item2);
		result.add(item3);
		result.add(item4);
		result.add(item5);
		
		mMessageDetailAdapter = new MessageListAdapter(getActivity().getApplicationContext(), result);
			
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);

		ListView mMessageFriend = (ListView) rootView.findViewById(R.id.list_messages);
        
        //load friends
        ArrayList<MessageItem> messages = getMessagesOfUser();
		MessageListAdapter mListAdapter = new MessageListAdapter(this.getActivity().getApplicationContext(), messages);
        mMessageFriend.setAdapter(mListAdapter);
        mMessageFriend.setOnItemClickListener(new MessageItemClickListener());
        
        mViewDialogContent = (ListView)inflater.inflate(R.layout.listview_message_detail, null);
       
        return rootView;
    }
	
	private boolean flag = false;
	private class MessageItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			AlertDialog.Builder alter = new AlertDialog.Builder(getActivity());
			alter.setTitle(R.string.title_friend_msg);
			
			loadMessageOfOneUser(position);
			if(!flag){
				alter.setView(mViewDialogContent);
				flag =true;
				mDialog = alter.create();
				mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			}
			
			mViewDialogContent.setAdapter(mMessageDetailAdapter);
			mMessageDetailAdapter.notifyDataSetChanged();

			mDialog.show();
		}
		
	}
}

