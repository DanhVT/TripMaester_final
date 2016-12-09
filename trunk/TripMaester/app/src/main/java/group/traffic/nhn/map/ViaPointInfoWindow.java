package group.traffic.nhn.map;

import java.util.ArrayList;
import java.util.List;

import vn.edu.hcmut.its.tripmaester.ui.activity.MainActivity;
import vn.edu.hcmut.its.tripmaester.R;
import group.traffic.nhn.message.MessageItem;
import group.traffice.nhn.common.PriorityDlg;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.MarkerInfoWindow;
import org.osmdroid.views.MapView;

/**
 * A customized InfoWindow handling "itinerary" points (start, destination and
 * via-points). We inherit from MarkerInfoWindow as it already provides most of
 * what we want. And we just add support for a "remove" button.
 *
 * @author M.Kergall
 */
@SuppressLint("NewApi")
public abstract class ViaPointInfoWindow extends MarkerInfoWindow {

    public abstract void onClickRemovePoint(int p);

    int mSelectedPoint;
    List<Bitmap> lstImage;
    private Button btnDelete;
    private ArrayList<MessageItem> result;
    private MainActivity mainActivity;

    public ViaPointInfoWindow(int layoutResId, final MapView mapView,
                              Bitmap bitmap, MainActivity mainActivity) {
        super(layoutResId, mapView);
        this.mainActivity = mainActivity;
        btnDelete = (Button) (mView.findViewById(R.id.bubble_delete));

        btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                System.out.println("Danh");

                // open gallery Image
                if (lstImage.size() > 1) {
//					new AlertDialog.Builder(mapView.getContext())
//							.setTitle("SHARE")
//
//							.setSingleChoiceItems(mMessageDetailAdapter, 0, new View.OnClickListener() {
//
//								@Override
//								public void onClick(View v) {
//									// TODO Auto-generated method stub
//
//								}
//							})
//							.setPositiveButton(R.string.alert_dialog_share,
//									new DialogInterface.OnClickListener() {
//										public void onClick(
//												DialogInterface dialog,
//												int whichButton) {
//
//											/* User clicked Yes so do some stuff */
//										}
//									})
//							.setNegativeButton(R.string.alert_dialog_cancel,
//									new DialogInterface.OnClickListener() {
//										public void onClick(
//												DialogInterface dialog,
//												int whichButton) {
//
//											/* User clicked No so do some stuff */
//										}
//									}).show();
                    PriorityDlg dlg = new PriorityDlg(mapView.getContext(), R.style.dlg_priority, lstImage);
                    dlg.show();

                }
            }
        });

        lstImage = new ArrayList<Bitmap>();
        if (null != bitmap) {
            lstImage.add(bitmap);
            Drawable btn_delete_drawable = new BitmapDrawable(mainActivity.getResources(), lstImage.get(0));
            btnDelete.setBackground(btn_delete_drawable);
        } else {
            btnDelete.setVisibility(View.INVISIBLE);
        }
    }

    public void addImage(Bitmap bitmap) {
        if (lstImage != null) {
            lstImage.add(bitmap);
        }
    }

    public void setLastImageButton() {
        if (lstImage != null && lstImage.size() > 0) {
            Drawable btn_delete_drawable = new BitmapDrawable(mainActivity.getResources(), lstImage.get(lstImage.size() - 1));
            btnDelete.setBackground(btn_delete_drawable);
        }
    }

    @Override
    public void onOpen(Object item) {
        Marker eItem = (Marker) item;
        // mSelectedPoint = (Integer)eItem.getRelatedObject();

        super.onOpen(item);
    }

    public void alertFormElements() {

	    /*
	     * Inflate the XML view. activity_main is in
	     * res/layout/form_elements.xml
	     */

        LayoutInflater inflater = (LayoutInflater) mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.image_listview_infowindow,
                null, false);


        // You have to list down your form elements
        final CheckBox myCheckBox = (CheckBox) formElementsView
                .findViewById(R.id.myCheckBox);

        final RadioGroup genderRadioGroup = (RadioGroup) formElementsView
                .findViewById(R.id.genderRadioGroup);

        final EditText nameEditText = (EditText) formElementsView
                .findViewById(R.id.nameEditText);


        //TODO: KenK11 "What is this???"
        // the alert dialog
        new AlertDialog.Builder(mMapView.getContext()).setView(formElementsView)
                .setTitle("Form Elements")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        String toastString = "";
	 
	                    /*
	                     * Detecting whether the checkbox is checked or not.
	                     */
                        if (myCheckBox.isChecked()) {
                            toastString += "Happy is checked!\n";
                        } else {
                            toastString += "Happy IS NOT checked.\n";
                        }
	 
	                    /*
	                     * Getting the value of selected RadioButton.
	                     */
                        // get selected radio button from radioGroup
                        int selectedId = genderRadioGroup
                                .getCheckedRadioButtonId();

                        // find the radiobutton by returned id
                        RadioButton selectedRadioButton = (RadioButton) formElementsView
                                .findViewById(selectedId);

                        toastString += "Selected radio button is: "
                                + selectedRadioButton.getText() + "!\n";
	 
	                    /*
	                     * Getting the value of an EditText.
	                     */
                        toastString += "Name is: " + nameEditText.getText()
                                + "!\n";

                        //showToast(toastString);

                        dialog.cancel();
                    }

                }).show();
    }

}
