package vn.edu.hcmut.its.tripmaester.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.faizmalkani.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;
import org.osmdroid.bonuspack.routing.Road;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cse.its.helper.Constant;
import group.traffic.nhn.leftmenu.LeftMenuListAdapter;
import group.traffic.nhn.map.MapFragment;
import group.traffic.nhn.message.MessageListAdapter;
import group.traffic.nhn.rightmenu.RightMapMenuExpandableListAdapter;
import group.traffic.nhn.rightmenu.RightMenuGroup;
import group.traffic.nhn.rightmenu.RightMenuListAdapter;
import group.traffic.nhn.trip.EditTripActivity;
import group.traffic.nhn.trip.NewTripActivity;
import group.traffic.nhn.trip.TripsFragment;
import group.traffic.nhn.user.User;
import group.traffic.nhn.user.UserFragment;
import group.traffice.nhn.common.Constants;
import group.traffice.nhn.common.StaticVariable;
import vn.edu.hcmut.its.tripmaester.R;
import vn.edu.hcmut.its.tripmaester.controller.manager.LoginManager;
import vn.edu.hcmut.its.tripmaester.model.Trip;
import vn.edu.hcmut.its.tripmaester.service.http.HttpManager;
import vn.edu.hcmut.its.tripmaester.setting.TMPref;
import vn.edu.hcmut.its.tripmaester.ui.IMainScreen;
import vn.edu.hcmut.its.tripmaester.ui.fragment.FriendsFragment;
import vn.edu.hcmut.its.tripmaester.ui.fragment.LoginFragment;

// TODO: 12/16/15 Not review yet
public class MainActivity extends FragmentActivity implements IMainScreen {
    public static final int LEFT_MENU__USER = 1;
    public static final int LEFT_MENU__MAP = 2;
    public static final int LEFT_MENU__TRIP = 3;
    public static final int LEFT_MENU__FRIEND = 4;
    public static final int LEFT_MENU__LOGOUT = 5;
    public static SharedPreferences mSharedPreferences;
    // TODO: 12/20/15 Why public static?
    public static FloatingActionButton fab_btn_capture;
    public static boolean isDraw = false;
    @Bind(R.id.drawer_layout_main_screen)
    DrawerLayout mDrawerLayoutMain;
    private String[] mLeftMenuItemTexts;
    private ListView mDrawerOtherMenu;
    private ListView mDrawerLeftMenu;
    private ExpandableListView mDrawerRightMapMenu;
    private RightMapMenuExpandableListAdapter mapMenuAdapter;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private Fragment mapFragment;
    private Fragment fragment = null;

    @Override
    public void changeToMapFragments(Trip trip) {
        boolean isNew = false;

        ArrayList<Road> lstRoads = new ArrayList<>();
        Road road1 = new Road(trip.getLstWayPoints());
        lstRoads.add(road1);

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }

        if (mapFragment == null) {
            mapFragment = new MapFragment();
            isNew = true;
        }
        isDraw = true;
        if (isNew) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_contener, mapFragment).commit();
        } else {
            // setTitle(mLeftMenuItemTexts[1]);//set text and color for left
            // menu item => click
            // update selected item and title, then close the drawer
            mDrawerLeftMenu.setItemChecked(1, true);

            // draw marker and road
            ((MapFragment) mapFragment).setMarkerForTrip(trip);
            ((MapFragment) mapFragment).drawTrip(lstRoads);
            // ====================

            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            }
            getSupportFragmentManager().beginTransaction().show(mapFragment).commit();
        }
    }

    /**
     * close right menu
     */
    private void closeRightMenu() {
        if (null != mDrawerRightMapMenu && mDrawerLayoutMain.isDrawerOpen(mDrawerRightMapMenu)) {
            mDrawerLayoutMain.closeDrawer(mDrawerRightMapMenu);
        }

        if (null != mDrawerOtherMenu && mDrawerLayoutMain.isDrawerOpen(mDrawerOtherMenu)) {
            mDrawerLayoutMain.closeDrawer(mDrawerOtherMenu);
        }
    }

    /**
     * display view by position
     *
     * @param position
     */
    private void displayView(int position) {
        // Fragment fragment = null;
        boolean isNew = false;
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        switch (position) {
            case LEFT_MENU__USER:
                mDrawerLayoutMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                fragment = new UserFragment();
                break;
            case LEFT_MENU__MAP:
                mDrawerLayoutMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                if (mapFragment == null) {
                    mapFragment = new MapFragment();
                    isNew = true;
                }
                break;
            case LEFT_MENU__TRIP:
                mDrawerLayoutMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                fragment = new TripsFragment();
                break;
            case LEFT_MENU__FRIEND:
                mDrawerLayoutMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                fragment = new FriendsFragment();
                break;
            case LEFT_MENU__LOGOUT:
                mDrawerLayoutMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                fragment = new LoginFragment();
                break;
            default:
                Log.d("aaa", "default");
                mDrawerLayoutMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                if (mapFragment == null) {
                    mapFragment = new MapFragment();
                    isNew = true;
                }
                break;
                // fragment = new LoginFragment();
                // break;
        }
        // if (null != fragment){
        // FragmentManager fragmentManager = getFragmentManager();
        // fragmentManager.beginTransaction().replace(R.id.frame_contener,
        // fragment).commit();
        // }

        if (position == LEFT_MENU__MAP) {
            // map fragment
            if (isNew) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_contener, mapFragment).commit();
            } else {
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().hide(fragment).commit();
                }
                getSupportFragmentManager().beginTransaction().show(mapFragment).commit();
            }

        } else {
            if (null != fragment) {
                getSupportFragmentManager().beginTransaction().add(R.id.frame_contener, fragment).commit();
                getSupportFragmentManager().beginTransaction().hide(mapFragment).commit();
                getSupportFragmentManager().beginTransaction().show(fragment).commit();
                // getSupportFragmentManager().beginTransaction()
                // .replace(R.id.frame_contener, fragment).commit();
            }
        }

    }

    /**
     * the function handle user action on friend menu (right menu)
     *
     * @param position
     */
    private void friendMenuItemHandler(int position) {
        mDrawerOtherMenu.setItemChecked(position, true);

        mDrawerLayoutMain.closeDrawer(mDrawerOtherMenu);
    }

    private void initFB() {
        // FB tracker
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                stopTracking();
                // if users logout
                LoginManager.getInstance().setUserToken(currentAccessToken);
                if (currentAccessToken == null) {
                    final TextView name = (TextView) findViewById(R.id.txt_select_account);
                    if (name != null) {
                        name.setText(R.string.login_prompt_select_account);
                    }
                    LoginManager.getInstance().setUser(null);
                } else {
                    GraphRequest request = GraphRequest.newMeRequest(LoginManager.getInstance().getUserToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    Log.d("user", object.toString());
                                    try {
                                        //TODO: VULAM Change this using GSON
                                        if (object != null) {
                                            // set permission to get picture
                                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                                                    .build();
                                            StrictMode.setThreadPolicy(policy);

                                            String name = "", picture ="", cover="",  user_fb_id = "", first_name = "", last_name = "", birthday = "",
                                                    email = "", updated_time = "", gender = "", local = "", verified = "",
                                                    timezone = "", link = "", imei = "";

                                            if (!object.isNull("name")) {
                                                name = object.getString("name");
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
                                                updated_time = object.getString("updated_time");
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
                                            if (!object.isNull("cover")) {
                                                cover = response.getJSONObject().getJSONObject("cover").getString("source");
                                            }
                                            if (!object.isNull("picture")) {
                                                picture = response.getJSONObject().getJSONObject("picture").getJSONObject("data").getString("url");
                                            }
                                            LoginManager.getInstance().setUser(new User("", user_fb_id, name, first_name, last_name, cover,picture,
                                                    birthday, email, updated_time, gender, local, verified, timezone, link,
                                                    imei, false));
                                            // TODO: WTF ??? KenK11
                                            JSONObject json = new JSONObject();
                                            json.put("name", LoginManager.getInstance().getUser().getName());
                                            json.put("userId", LoginManager.getInstance().getUser().getName());
                                            json.put("first_name", LoginManager.getInstance().getUser().getName());
                                            json.put("last_name", LoginManager.getInstance().getUser().getName());
                                            json.put("birthday", LoginManager.getInstance().getUser().getName());
                                            json.put("email", LoginManager.getInstance().getUser().getName());
                                            json.put("updated_time", LoginManager.getInstance().getUser().getName());
                                            json.put("gender", LoginManager.getInstance().getUser().getName());
                                            json.put("local", LoginManager.getInstance().getUser().getName());
                                            json.put("verified", LoginManager.getInstance().getUser().getName());
                                            json.put("link", LoginManager.getInstance().getUser().getName());
                                            json.put("timezone", LoginManager.getInstance().getUser().getName());
                                            json.put("imei", LoginManager.getInstance().getUser().getName());

                                            JSONObject json_result = HttpManager.login();
                                            if (!json_result.isNull("tokenId")) {
                                                String tokenId = json_result.get("tokenId").toString();
                                                LoginManager.getInstance().getUser().setTokenId(tokenId);
                                            }
                                            if (!json_result.isNull("status")) {
                                                boolean status = json_result.getBoolean("status");
                                                LoginManager.getInstance().getUser().setStatus(status);
                                            }

                                            LoginManager.getInstance().getUser().getListFriend();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.i("error", e.getMessage());
                                    }
                                }
                            });
                    Bundle parameters = new Bundle();
                    // parameters.putString("fields",
                    // "id,name,email,gender, birthday,first_name");
                    // parameters.putString("permission",
                    // "user_friends");
                    request.setParameters(parameters);
                    request.executeAsync();
                }
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                // App code
            }
        };
    }

    /**
     * check right menu open
     *
     * @return
     */
    private boolean isRightMenuOpen() {
        boolean isMapMenuOpen = false;
        boolean isOtherRightMenuOpen = false;
        if (null != mDrawerRightMapMenu) {
            isMapMenuOpen = mDrawerLayoutMain.isDrawerOpen(mDrawerRightMapMenu);
        }

        if (null != mDrawerOtherMenu) {
            isOtherRightMenuOpen = mDrawerLayoutMain.isDrawerOpen(mDrawerOtherMenu);
        }

        return (isMapMenuOpen || isOtherRightMenuOpen);
    }

    /**
     * left menu mode
     *
     * @param positionLeft
     */
    private void loadRightMenu(int positionLeft) {
        switch (positionLeft) {
            case LEFT_MENU__MAP: // map menu
                onCreateMapMenu();
                break;
            case LEFT_MENU__TRIP: // trip menu
                onCreateTripMenu();
                break;
            case LEFT_MENU__FRIEND: // friend menu
                onCreateFriendMenu();
                break;
            case LEFT_MENU__LOGOUT: // message menu
                onCreateMessageMenu();
                break;
            default:
        }

    }

    /**
     * the function handle user action on map menu (map right menu)
     *
     * @param position
     */
    private void mapMenuItemHandler(int position) {
        mDrawerRightMapMenu.setItemChecked(position, true);

        // close right map menu
        mDrawerLayoutMain.closeDrawer(mDrawerRightMapMenu);
    }

    private void messageMenuItemHandler(int position) {
        mDrawerOtherMenu.setItemChecked(position, true);

        mDrawerLayoutMain.closeDrawer(mDrawerOtherMenu);
    }

    public void onClickLeftMenu(View v) {
        mDrawerLayoutMain.openDrawer(Gravity.LEFT);
    }

    /**
     * right menu click listener
     */
    public void onClickRightMenu(View v) {
        int selectMode = TMPref.getInstances().getInt(Constants.LEFT_MENU_MODE, -1);
        boolean drawerRightOpen = isRightMenuOpen();
        if (drawerRightOpen) {
            closeRightMenu();
        } else {
            // TODO: 12/21/15 refactor these constants
            if(selectMode == LEFT_MENU__MAP) {
                if (null != mDrawerRightMapMenu)
                    mDrawerLayoutMain.openDrawer(mDrawerRightMapMenu);
            }
            else{
                if (null != mDrawerOtherMenu)
                    mDrawerLayoutMain.openDrawer(mDrawerOtherMenu);

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initFB();

        // create share preference
        mSharedPreferences = getSharedPreferences(Constant.PREFERENCES_NAME, Context.MODE_PRIVATE);

        // create left menu
        onCreateLeftMenu();
        // load map menu
        onCreateRightMapMenuGroups();

        if (savedInstanceState == null) {
            selectLeftMenuItem(LEFT_MENU__MAP);
        }
    }

    /**
     * create menu for friend
     */
    private void onCreateFriendMenu() {
        mDrawerOtherMenu = (ListView) findViewById(R.id.right_drawer_menu);
        String[] menuItemFriendTexts = getResources().getStringArray(R.array.friend_menu_items);
        TypedArray menuItemFriendIcons = getResources().obtainTypedArray(R.array.friend_menu_icons);

        RightMenuListAdapter leftAdapter = new RightMenuListAdapter(getApplicationContext(), menuItemFriendTexts,
                menuItemFriendIcons);
        mDrawerOtherMenu.setAdapter(leftAdapter);

        mDrawerOtherMenu.setOnItemClickListener(new DrawerRightMenuItemClickListener());
    }

    /**
     * create left menu
     */
    private void onCreateLeftMenu() {
        //
        mDrawerLeftMenu = (ListView) findViewById(R.id.left_drawer_menu);
        View header = getLayoutInflater().inflate(R.layout.header, null);
        mDrawerLeftMenu.addHeaderView(header);
        mLeftMenuItemTexts = getResources().getStringArray(R.array.left_meunu_items);
        TypedArray leftmenuIcons = getResources().obtainTypedArray(R.array.left_menu_icons);

        LeftMenuListAdapter leftAdapter = new LeftMenuListAdapter(getApplicationContext(), mLeftMenuItemTexts,
                leftmenuIcons);
        mDrawerLeftMenu.setAdapter(leftAdapter);
        mDrawerLeftMenu.setOnItemClickListener(new DrawerLeftMenuItemClickListener());
    }

    /**
     * create menu for map
     */
    private void onCreateMapMenu() {
        mDrawerRightMapMenu = (ExpandableListView) findViewById(R.id.right_map_menu_expand_items);

        mapMenuAdapter = new RightMapMenuExpandableListAdapter(this, null);
        mDrawerRightMapMenu.setAdapter(mapMenuAdapter);
        mDrawerRightMapMenu.setOnChildClickListener(new DrawerRightMapChildMenuItemClickListener());
        mDrawerRightMapMenu.setOnGroupClickListener(new DrawerRightMapGroupMenuItemClickListener());
        //
    }

    /**
     *
     */
    private void onCreateMessageMenu() {
        mDrawerOtherMenu = (ListView) findViewById(R.id.right_drawer_menu);
        String[] menuItemMsgTexts = getResources().getStringArray(R.array.msg_menu_items);
        TypedArray menuItemMsgIcons = getResources().obtainTypedArray(R.array.msg_menu_icons);

        RightMenuListAdapter leftAdapter = new RightMenuListAdapter(getApplicationContext(), menuItemMsgTexts,
                menuItemMsgIcons);
        mDrawerOtherMenu.setAdapter(leftAdapter);
        mDrawerOtherMenu.setOnItemClickListener(new DrawerRightMenuItemClickListener());
    }

    /**
     * create right menu of map
     */
    private void onCreateRightMapMenuGroups() {
        String[] rightMapMenuItems = getResources().getStringArray(R.array.map_menu_items);
        String[] routingAlgorithms = getResources().getStringArray(R.array.map_algorithm_menu_items);
        String[] vehicleItems = getResources().getStringArray(R.array.map_vehicle_menu_items);

        // the number of right menu items
        int itemCount = rightMapMenuItems.length;

        // the number of vehicles items
        int trans = vehicleItems.length;

        // the number of algorithm items
        int rout = routingAlgorithms.length;

        StaticVariable.GROUP_MAP_MENU_ITEMS.clear();
        for (int i = 0; i < itemCount; ++i) {
            RightMenuGroup group = new RightMenuGroup(rightMapMenuItems[i]);
            switch (i) {
                // TODO: 12/21/15 refactor these constants 
                case 0: // vehicle menu items
                {
                    for (String vehicleItem : vehicleItems) {
                        group.children.add(vehicleItem);
                        group.setCheck_index(StaticVariable.INT_TRANSPORTATION_MODE);
                    }
                    break;
                }
                case 1: // algorithm items
                {
                    for (String routingAlgorithm : routingAlgorithms) {
                        group.children.add(routingAlgorithm);
                        group.setCheck_index(StaticVariable.INT_ROUTING_MODE);
                    }
                    break;
                }

                default:

                    break;
            }

            StaticVariable.GROUP_MAP_MENU_ITEMS.add(i, group);
        }

        // TODO: 12/21/15 refactor constansts 
        StaticVariable.GROUP_MAP_MENU_ITEMS.get(2).check = StaticVariable.VOICE; // voice
        StaticVariable.GROUP_MAP_MENU_ITEMS.get(3).check = StaticVariable.NAVIGATE; // navigate
        StaticVariable.GROUP_MAP_MENU_ITEMS.get(4).check = StaticVariable.SHOW_TRAFFIC_INFO; // traffic
        // information
        StaticVariable.GROUP_MAP_MENU_ITEMS.get(5).check = StaticVariable.SHOW_WARNING_INFO; // warming

    }

    /**
     * create menu for trip
     */
    private void onCreateTripMenu() {
        mDrawerOtherMenu = (ListView) findViewById(R.id.right_drawer_menu);
        String[] menuItemTripTexts = getResources().getStringArray(R.array.trip_menu_items);
        TypedArray menuItemTripIcons = getResources().obtainTypedArray(R.array.trip_menu_icons);
        FacebookSdk.sdkInitialize(getApplicationContext());

        RightMenuListAdapter leftAdapter = new RightMenuListAdapter(getApplicationContext(), menuItemTripTexts,
                menuItemTripIcons);
        mDrawerOtherMenu.setAdapter(leftAdapter);
        mDrawerOtherMenu.setOnItemClickListener(new DrawerRightMenuItemClickListener());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // LoginManager.getInstance().logOut();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // ### remove location listener
        MapFragment.Instance.removeUpdates();

        // ### save shared preferences
        Editor editor = mSharedPreferences.edit();
        editor.putInt(Constant.STR_TRANS_KEY, StaticVariable.INT_TRANSPORTATION_MODE);
        editor.putInt(Constant.STR_ROUTING_MODE_KEY, StaticVariable.INT_ROUTING_MODE);
        editor.putInt(Constant.STR_LANGUAGE_KEY, StaticVariable.INT_LANGUAGE_MODE);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != MapFragment.Instance)
            MapFragment.Instance.mapResume();
    }

    public void runThreadMessageList(final MessageListAdapter mesListAdapter) {
        runOnUiThread(new Thread(new Runnable() {
            public void run() {
                mesListAdapter.notifyDataSetChanged();
            }
        }));
    }

    /**
     * select left menu item
     *
     * @param position
     */
    private void selectLeftMenuItem(int position) {

        // update selected item and title, then close the drawer
        mDrawerLeftMenu.setItemChecked(position, true);
        // set left menu mode
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(Constants.LEFT_MENU_MODE, position);
        editor.apply();

        // load right menu of left menu mode
        loadRightMenu(position);
        // set icon and text in action bar
        setTitle(mLeftMenuItemTexts[position -1]);
        // setIcon(mLeftmenuIcons.getResourceId(position, -1));

        // close left menu
        mDrawerLayoutMain.closeDrawer(mDrawerLeftMenu);
        if (null != mDrawerOtherMenu)
            mDrawerLayoutMain.closeDrawer(mDrawerOtherMenu);

        if (null != mDrawerRightMapMenu)
            mDrawerLayoutMain.closeDrawer(mDrawerRightMapMenu);

        // load content view
        displayView(position);
    }

    /**
     * select right menu item
     *
     * @param position
     */
    private void selectRightItem(int position) {
        int leftMode = mSharedPreferences.getInt(Constants.LEFT_MENU_MODE, -1);
        // TODO: 12/21/15 refactor constants
        switch (leftMode) {
            case LEFT_MENU__MAP: {
                mapMenuItemHandler(position);
                break;
            }
            case LEFT_MENU__TRIP:
                tripMenuItemHandler(position);
                break;
            case LEFT_MENU__FRIEND:
                friendMenuItemHandler(position);
                break;
            case LEFT_MENU__USER: {
                messageMenuItemHandler(position);
                break;
            }
        }
    }

    /**
     * this function handle user action on trip menu (right menu)
     *
     * @param position
     */
    private void tripMenuItemHandler(int position) {
        mDrawerOtherMenu.setItemChecked(position, true);
        switch (position) {
            // TODO: 12/21/15 refactor constants

            case 0:
                Intent trip = new Intent(this, NewTripActivity.class);
                startActivity(trip);
                break;
            case 1:
                Intent editTrip = new Intent(this, EditTripActivity.class);
                startActivity(editTrip);
            default:
                break;
        }
        mDrawerLayoutMain.closeDrawer(mDrawerOtherMenu);
    }

    /**
     * The click listener for ListView in the navigation drawer
     *
     * @author Vo tinh
     */
    private class DrawerLeftMenuItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectLeftMenuItem(position);
        }
    }

    /**
     * right menu item handler
     *
     * @author Vo tinh
     */
    private class DrawerRightMenuItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectRightItem(position);
        }
    }

    /**
     * click listener on group menu item of map
     *
     * @author Vo tinh
     */
    private class DrawerRightMapGroupMenuItemClickListener implements ExpandableListView.OnGroupClickListener {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            return MapFragment.Instance.onClickGroupMapMenuItem(groupPosition);
        }
    }

    /**
     * click listener on child item of map
     *
     * @author Vo tinh
     */
    private class DrawerRightMapChildMenuItemClickListener implements ExpandableListView.OnChildClickListener {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            MapFragment.Instance.onChildClick(groupPosition, childPosition);
            StaticVariable.GROUP_MAP_MENU_ITEMS.get(groupPosition).setCheck_index(childPosition);
            mapMenuAdapter.notifyDataSetChanged();
            return false;
        }
    }
}
