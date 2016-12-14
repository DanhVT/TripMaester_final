package group.traffic.nhn.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import vn.edu.hcmut.its.tripmaester.R;
import vn.edu.hcmut.its.tripmaester.controller.manager.LoginManager;
import vn.edu.hcmut.its.tripmaester.helper.ImageLoaderHelper;

public class UserFragment extends Fragment {
    private static String GRAPH_FB_URL = "https://graph.facebook.com/";

    public UserFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_user, container, false);

        TextView textViewBirthday = (TextView) rootView.findViewById(R.id.txtbirthday);
        TextView textViewNameUser = (TextView) rootView.findViewById(R.id.txtFirstLastName);
        TextView textViewEmail = (TextView) rootView.findViewById(R.id.txtEmail);
        TextView textViewGender = (TextView) rootView.findViewById(R.id.txtGender);
        final ImageView imageViewAvartaUser = (ImageView) rootView.findViewById(R.id.imguser);
        final ImageView imgcover = (ImageView) rootView.findViewById(R.id.imgcover);

        if (LoginManager.getInstance().isLogin()) {
            textViewBirthday.setText(LoginManager.getInstance().getUser().getBirthday());
            textViewEmail.setText(LoginManager.getInstance().getUser().getEmail());
            textViewNameUser.setText(LoginManager.getInstance().getUser().getFirst_name() + LoginManager.getInstance().getUser().getLast_name());
            textViewGender.setText(LoginManager.getInstance().getUser().gender);
            ImageLoaderHelper.displayImage(LoginManager.getInstance().getUser().picture, imageViewAvartaUser);
            ImageLoaderHelper.displayImage(LoginManager.getInstance().getUser().cover, imgcover);

        }
        return rootView;
    }

}

