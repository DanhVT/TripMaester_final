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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_user, container, false);

        TextView lblBirthday = (TextView) rootView.findViewById(R.id.txtbirthday);
        TextView lblFirstLastName = (TextView) rootView.findViewById(R.id.txtFirstLastName);
        TextView lblEmail = (TextView) rootView.findViewById(R.id.txtEmail);
        TextView lblGender = (TextView) rootView.findViewById(R.id.txtGender);
        ImageView user_image = (ImageView) rootView.findViewById(R.id.imguser);
        if (LoginManager.getInstance().isLogin()) {
            lblBirthday.setText(LoginManager.getInstance().getUser().getBirthday());
            lblEmail.setText(LoginManager.getInstance().getUser().getEmail());
            lblFirstLastName.setText(LoginManager.getInstance().getUser().getFirst_name() + LoginManager.getInstance().getUser().getLast_name());
            lblGender.setText(LoginManager.getInstance().getUser().gender);
            try {
                if (LoginManager.getInstance().isLogin()) {
                    URL image_value = new URL(GRAPH_FB_URL + LoginManager.getInstance().getUser().getId() + "/picture");
                    InputStream input_stream = (InputStream) image_value
                            .getContent();
                    Bitmap user_fb_icon = BitmapFactory.decodeStream(input_stream);
                    user_image.setImageBitmap(user_fb_icon);
                }
            } catch (Exception ex) {
                Log.i("User Info image", ex.getMessage());
            }
        }
        return rootView;
    }
}

