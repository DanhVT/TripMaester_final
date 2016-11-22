package group.traffic.nhn.user;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

import vn.edu.hcmut.its.tripmaester.R;
import vn.edu.hcmut.its.tripmaester.controller.manager.LoginManager;

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
        ImageView imageViewAvartaUser = (ImageView) rootView.findViewById(R.id.imguser);

        if (LoginManager.getInstance().isLogin()) {
            textViewBirthday.setText(LoginManager.getInstance().getUser().getBirthday());
            textViewEmail.setText(LoginManager.getInstance().getUser().getEmail());
            textViewNameUser.setText(LoginManager.getInstance().getUser().getFirst_name() + LoginManager.getInstance().getUser().getLast_name());
            textViewGender.setText(LoginManager.getInstance().getUser().gender);
            try {
                URL image_value = new URL(GRAPH_FB_URL + LoginManager.getInstance().getUser().getId() + "/picture");
                InputStream input_stream = (InputStream) image_value.getContent();
                Bitmap user_fb_icon = BitmapFactory.decodeStream(input_stream);
                imageViewAvartaUser.setImageBitmap(user_fb_icon);

            } catch (Exception ex) {
                Log.i("User Info image", ex.getMessage());
            }
        }
        return rootView;
    }
}

