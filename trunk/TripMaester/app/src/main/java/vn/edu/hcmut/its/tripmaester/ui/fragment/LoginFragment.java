package vn.edu.hcmut.its.tripmaester.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.koushikdutta.ion.Ion;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.lang.reflect.Array;

import butterknife.Bind;
import butterknife.ButterKnife;
import group.traffic.nhn.user.User;
import vn.edu.hcmut.its.tripmaester.R;
import vn.edu.hcmut.its.tripmaester.controller.manager.LoginManager;
import vn.edu.hcmut.its.tripmaester.service.http.HttpManager;
import vn.edu.hcmut.its.tripmaester.utility.FacebookHelper;

/**
 * // TODO: 12/24/15 Why there are 2 login function in the LoginFragment & MainActivity?
 */
public class LoginFragment extends Fragment {
    private static final String TAG = LoginFragment.class.getSimpleName();
    private static final String[] PERMISSIONS = {"public_profile", "email", "user_friends"};
    private static final String PARAMETERS = "id,name,cover,timezone,email,picture,first_name,last_name,gender,birthday,location";
    //    GameRequestDialog requestDialog;
//    AccessTokenTracker accessTokenTracker;
    CallbackManager mCallbackManager;

    @Bind(R.id.fb_avatar)
    ImageView mUserImage;

    @Bind(R.id.txt_select_account)
    TextView mTxtName;

    public LoginFragment() {
    }

    private void logInFB() {
        GraphRequest request = GraphRequest.newMeRequest(
                LoginManager.getInstance().getUserToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("LoginActivity", response.toString());
                        try {
                            if (object != null) {
                                // set permission to get picture
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                        .permitAll().build();
                                StrictMode.setThreadPolicy(policy);

                                String name = null, picture=null, cover  =null, user_fb_id = null, first_name = null, last_name = null, birthday = null, email = null, updated_time = null, gender = null, local = null, verified = null, timezone = null, link = null, imei = null;
                                System.out.println(object);
                                if (!object.isNull("name")) {
                                    name = object.getString("name");
                                }
                                if (!object.isNull("cover")) {
                                    cover = response.getJSONObject().getJSONObject("cover").getString("source");
                                }
                                if (!object.isNull("picture")) {
                                    picture = response.getJSONObject().getJSONObject("picture").getJSONObject("data").getString("url");
                                }
                                if (!object.isNull("id")) {
                                    user_fb_id = object.getString("id");
                                }
                                if (!object.isNull("first_name")) {
                                    first_name = object.getString("first_name");
                                }
                                if (!object.isNull("last_name")) {
                                    last_name = object.getString("last_name");
                                }
                                if (!object.isNull("birthday")) {
                                    birthday = object.getString("birthday");
                                }
                                if (!object.isNull("email")) {
                                    email = object.getString("email");
                                }
                                if (!object.isNull("updated_time")) {
                                    updated_time = object
                                            .getString("updated_time");
                                }
                                if (!object.isNull("gender")) {
                                    gender = object.getString("gender");
                                }
                                if (!object.isNull("local")) {
                                    local = object.getString("local");
                                }
                                if (!object.isNull("verified")) {
                                    verified = object.getString("verified");
                                }
                                if (!object.isNull("timezone")) {
                                    timezone = object.getString("timezone");
                                }
                                if (!object.isNull("link")) {
                                    link = object.getString("link");
                                }
                                if (!object.isNull("imei")) {
                                    imei = object.getString("imei");
                                }

                                LoginManager.getInstance().setUser(new User("", user_fb_id,
                                        name, first_name, last_name, cover, picture, birthday,
                                        email, updated_time, gender, local,
                                        verified, timezone, link, imei, false));
                                updateNameAndImage();

//								InputStream response_stream = Utilities.sendJson_H
//								//http://traffic.hcmut.edu.vn/ITS/rest/user/login
//								String str_response = Utilities
//										.readStringFromInputStream(response_stream);
                                JSONObject json_result = HttpManager.login();
                                if (!json_result.isNull("tokenId")) {
                                    String tokenId = json_result.get("tokenId").toString();
                                    LoginManager.getInstance().getUser().setTokenId(tokenId);
                                }
                                if (!json_result.isNull("status")) {
                                    boolean status = json_result.getBoolean("status");
                                    LoginManager.getInstance().getUser().setStatus(status);
                                }
                                Logger.t("tokenId").d(LoginManager.getInstance().getUser().getTokenId());
                                //get list friend-in-app of user
                                LoginManager.getInstance().getUser().getListFriend();

                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
//							e.printStackTrace();
//							Log.i("error", e.getMessage());
                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", PARAMETERS);
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         FacebookSdk.sdkInitialize(this.getActivity().getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        if(AccessToken.getCurrentAccessToken()==null){
            com.facebook.login.LoginManager.getInstance().logOut();
        }
        // App Invite
//		 String appLinkUrl, previewImageUrl;

//		appLinkUrl = "https://fb.me/389015721285893";
//		// previewImageUrl = "https://www.mydomain.com/my_invite_image.jpg";
//
//		if (AppInviteDialog.canShow()) {
//			AppInviteContent content = new AppInviteContent.Builder()
//					.setApplinkUrl(appLinkUrl)
//					// .setPreviewImageUrl(previewImageUrl)
//					.build();
//			AppInviteDialog.show(this, content);
//		}

        // Game IRequest
        // requestDialog = new GameRequestDialog(this);
        // requestDialog.registerCallback(mCallbackManager, new
        // FacebookCallback<GameRequestDialog.Result>() {
        // public void onSuccess(GameRequestDialog.Result result) {
        // // String id = result.getId();
        // }
        //
        // public void onCancel() {}
        //
        // public void onError(FacebookException error) {}
        // });
        //
        // GameRequestContent content = new GameRequestContent.Builder()
        // .setMessage("Come play this level with me")
        // .build();
        // requestDialog.show(content);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        rootView.setBackgroundColor(Color.WHITE);

        ButterKnife.bind(this, rootView);

        LoginButton authButton = (LoginButton) rootView.findViewById(R.id.authButton);
        authButton.setReadPermissions(PERMISSIONS);

        authButton.setFragment(this);

        // Callback registration
        authButton.registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        exception.printStackTrace();
                    }

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        LoginManager.getInstance().setUserToken(loginResult.getAccessToken());
                        logInFB();
                    }
                });

        //login update
        if (LoginManager.getInstance().isLogin()) {
            updateNameAndImage();
        }

        return rootView;
    }

    private void updateNameAndImage() {
        try {
            // TODO: 12/23/15 Change MainActivity.isLogin into a controller function
            if (LoginManager.getInstance().isLogin()) {
                mTxtName.setText(LoginManager.getInstance().getUser().getName());
                String image_value = FacebookHelper.getFacebookProfileImage(LoginManager.getInstance().getUser().getId());
                // TODO: 12/23/15 Get image from profile --- redoing this
                Ion.with(getContext())
                        .load(image_value)
                        .withBitmap()
                        .intoImageView(mUserImage);
//                InputStream input_stream = (InputStream) image_value.getContent();
//                Bitmap user_fb_icon = BitmapFactory.decodeStream(input_stream);
//                mUserImage.setImageBitmap(user_fb_icon);
            }
        } catch (Exception ex) {
            Log.i(TAG, "updateNameAndImage", ex);
        }
    }


}

