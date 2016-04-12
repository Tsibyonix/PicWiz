package com.core.picwiz;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class PreUploadFragment extends android.app.Fragment {
    private String image;
    private LinearLayout baseLayout;
    private com.lyft.android.scissors.CropView touchImageView;
    private TextView mTextViewLocation;
    private CheckBox mCheckBoxGeneralLocation;
    private TextView mTextViewGeneralLocationInfo;
    private EditText mEditTextTags;
    private EditText mEditTextCaption;
    private CheckBox mCheckBoxPrivacy;
    private ProgressDialog progressDialog;
    private String privacy = "public";

    private boolean timeout = false;

    private SharedPreferences settings;
    private List<Address> location;
    private String toSendLocation = null;

    private Bitmap imageRaw;
    private Bitmap imageScaled;

    private String imagePath;
    private Float Latitude, Longitude;
    private Geocoder geocoder;



    private boolean showTick = false;
    private Menu menu;

    public PreUploadFragment() {
        // Required empty public constructor
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] split;
        split = contentUri.toString().split("/");
        String finalPath;
        finalPath = "/";
        for (int i = 3; i <= split.length - 1; i++) {
            finalPath = finalPath.concat(split[i]);
            if (i != split.length - 1)
                finalPath = finalPath.concat("/");
        }
        return finalPath;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        settings = getActivity().getSharedPreferences("config", getActivity().MODE_PRIVATE);
        progressDialog = new ProgressDialog(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pre_upload, container, false);

        baseLayout = (LinearLayout) view.findViewById(R.id.base_layout);
        touchImageView = (com.lyft.android.scissors.CropView) view.findViewById(R.id.touch_image_view_pre_upload);
        mTextViewLocation = (TextView) view.findViewById(R.id.text_view_location_pre_upload);
        mCheckBoxGeneralLocation = (CheckBox) view.findViewById(R.id.checkbox_general_location);
        mTextViewGeneralLocationInfo = (TextView) view.findViewById(R.id.text_view_location_info);
        mEditTextTags = (EditText) view.findViewById(R.id.edit_text_tag_pre_upload);
        mEditTextCaption = (EditText) view.findViewById(R.id.edit_text_caption_pre_upload);
        mCheckBoxPrivacy = (CheckBox) view.findViewById(R.id.checkbox_privacy);
        image = getActivity().getIntent().getExtras().getString("image");
        imagePath = getRealPathFromURI(Uri.parse(image));
        touchImageView.setImageURI(Uri.parse(image));
        touchImageView.setDrawingCacheEnabled(true);
        new GetGeoLocation(imagePath).execute();

        mCheckBoxGeneralLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toSendLocation = location.get(0).getAddressLine(1);
                    mTextViewLocation.setText(" "+toSendLocation);
                } else {
                    toSendLocation = location.get(0).getAddressLine(0)+ location.get(0).getAddressLine(1)+ location.get(0).getAddressLine(2);
                    mTextViewLocation.setText(" "+toSendLocation);
                }
            }
        });

        mCheckBoxPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    privacy = "private";
                else
                    privacy = "public";
            }
        });

        mEditTextTags.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText editText = (EditText) v.findViewById(R.id.edit_text_tag_pre_upload);
                String text = editText.getText().toString();
                if (text.isEmpty())
                    return true;
                switch (keyCode) {
                    case KeyEvent.KEYCODE_COMMA:
                        String textWithComma = editText.getText().toString();
                        textWithComma = textWithComma.trim();
                        textWithComma = textWithComma + " #";
                        editText.setText(textWithComma);
                        break;
                    case KeyEvent.KEYCODE_SPACE:
                        String textWithSpace = editText.getText().toString();
                        textWithSpace = textWithSpace.trim();
                        textWithSpace = textWithSpace + ", #";
                        editText.setText(textWithSpace);
                        break;
                }
                return true;
            }
        });

        mTextViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetGeoLocation(imagePath).execute();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.account_settings, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        //if(!showTick)
            //menu.getItem(0).setEnabled(false);
        //else
            //menu.getItem(0).setEnabled(true);
    }

    void accept() throws FileNotFoundException {
        final PicWizBackend picWizBackend = new PicWizBackend(getActivity());
        progressDialog.setTitle("Posting...");
        final CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.i("time", String.valueOf(millisUntilFinished));
                if (picWizBackend.getWait()) {
                    //progressDialog.hide();
                    this.cancel();
                    Toast.makeText(getActivity(), picWizBackend.getMessage(), Toast.LENGTH_SHORT).show();
                    if (picWizBackend.getSuccess() == 1) {
                        getActivity().finish();
                    } else {
                        Snackbar.make(baseLayout.getRootView(), picWizBackend.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFinish() {
                timeout = true;
                Log.i("time", "Clock finished");
                //progressDialog.hide();
                this.cancel();
                //getActivity().finish();
                Snackbar.make(baseLayout.getRootView(), "Upload Failed. Error: Request Timeout", Snackbar.LENGTH_LONG).show();
            }
        };
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/PicWiz/Saves/";
        File newDir = new File(dir);
        if (!newDir.isDirectory())
            newDir.mkdir();

        String userID = settings.getString("USER_ID", null);
        String tags = mEditTextTags.getText().toString().trim();
        String caption = mEditTextCaption.getText().toString().trim();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat curFormater = new SimpleDateFormat("dMy-hms");
        SimpleDateFormat timeStamp = new SimpleDateFormat("d-M-y-h-m-s");
        String date = curFormater.format(calendar.getTime()).trim();
        String pictureName = dir + "img-" + date + ".jpg";
        File pic = new File(pictureName);
        touchImageView.extensions().crop().quality(100).format(Bitmap.CompressFormat.JPEG).into(pic);
        Bitmap bitmap = touchImageView.crop();

        String toSendTimeStamp = timeStamp.format(calendar.getTime());
        if (!tags.isEmpty() || tags == "#")
            if (!caption.isEmpty()) {
                if (toSendLocation == null)
                    toSendLocation = "none";
                picWizBackend.createPost(userID, tags, caption, bitmap, toSendLocation, privacy, toSendTimeStamp);
                Log.v("Info: ", userID+": "+tags+": "+caption+": "+toSendLocation+": "+privacy+": "+toSendTimeStamp);
                countDownTimer.start();
            }
            else {
                mEditTextCaption.setError("Caption cannot be empty");
                mEditTextCaption.requestFocus();
            }
        else {
            mEditTextTags.setError("Tags cannot be empty");
            mEditTextTags.requestFocus();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_done:
                try {
                    //progressDialog.show();
                    accept();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.i("Save Pic: ", e.getMessage());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class GetGeoLocation extends AsyncTask<Void, Void, Void> {
        ExifInterface exif;
        List<Address> addresses;
        Boolean timeout_geo = false;

        public GetGeoLocation(String imagePath) {
            mTextViewLocation.setText(" Fetching Location.");
            try {
                exif = new ExifInterface(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private Float convertToDegree(String stringDMS){
            Float result = null;
            String[] DMS = stringDMS.split(",", 3);

            String[] stringD = DMS[0].split("/", 2);
            Double D0 = Double.valueOf(stringD[0]);
            Double D1 = Double.valueOf(stringD[1]);
            Double FloatD = D0/D1;

            String[] stringM = DMS[1].split("/", 2);
            Double M0 = Double.valueOf(stringM[0]);
            Double M1 = Double.valueOf(stringM[1]);
            Double FloatM = M0/M1;

            String[] stringS = DMS[2].split("/", 2);
            Double S0 = Double.valueOf(stringS[0]);
            Double S1 = Double.valueOf(stringS[1]);
            Double FloatS = S0/S1;

            result = new Float(FloatD + (FloatM/60) + (FloatS/3600));

            return result;
        }

        void run() {
            String LATITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String LATITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            String LONGITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String LONGITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            if((LATITUDE != null) &&( LATITUDE_REF != null) && (LONGITUDE != null) && (LONGITUDE_REF != null)) {
                if(LATITUDE_REF.equals("N")) {
                    Latitude = convertToDegree(LATITUDE);
                }
                else{
                    Latitude = 0 - convertToDegree(LATITUDE);
                }

                if(LONGITUDE_REF.equals("E")) {
                    Longitude = convertToDegree(LONGITUDE);
                }
                else{
                    Longitude = 0 - convertToDegree(LONGITUDE);
                }
            } else {
                float[] latLong = new float[2];
                exif.getLatLong(latLong);
                Latitude = latLong[0];
                Longitude = latLong[1];
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            run();
            try {
                addresses = geocoder.getFromLocation(Latitude, Longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
                timeout_geo = true;
                Log.i("geo: ", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("geo: ", Latitude + ":" + Longitude);
            if(!timeout_geo) {
                toSendLocation = addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getAddressLine(1) + ", " + addresses.get(0).getAddressLine(2);
                mTextViewLocation.setText(" "+toSendLocation);
                location = addresses;
                mCheckBoxGeneralLocation.setVisibility(View.VISIBLE);
                mTextViewGeneralLocationInfo.setVisibility(View.VISIBLE);
                //menu.getItem(0).setEnabled(true);
            }
            else {
                mTextViewLocation.setText(" Unable to fetch location, tap here to refresh.");
                //menu.getItem(0).setEnabled(false);
            }
        }
    }
}
