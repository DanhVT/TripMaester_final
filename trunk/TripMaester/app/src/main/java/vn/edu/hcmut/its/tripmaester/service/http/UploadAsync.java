package vn.edu.hcmut.its.tripmaester.service.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;

import vn.edu.hcmut.its.tripmaester.R;

/**
 * Created by AnTD on 11/29/2016.
 */

public class UploadAsync extends AsyncTask<File, Void, JSONObject> {
    ProgressDialog mProgress;
    String  MIME, fileName;
    Context context;
    ProgressDialog PD;
    public UploadAsync(String fileName,  String MIME,Context context) {
        this.MIME = MIME;
        this.fileName = fileName;
        this.context= context;
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        PD = new ProgressDialog(context);
        PD.setTitle(context.getResources().getString((R.string.loading)));
        PD.setMessage(context.getResources().getString(R.string.wait));
        PD.setCancelable(false);
        PD.show();
    }

    @Override
    protected JSONObject doInBackground(File... files) {
        File mFile = files[0];
        JSONObject jsonResponse = null;
        jsonResponse = HttpManager.uploadImage(mFile, fileName, MIME);
        return jsonResponse;
    }
    @Override
    public void onPostExecute(JSONObject jsonResponse){
        super.onPostExecute(jsonResponse);

        PD.dismiss();

        if(jsonResponse == null){
            Toast.makeText(context, "Upload to server fail", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Uploaded successful", Toast.LENGTH_SHORT).show();
        }
    }

}
