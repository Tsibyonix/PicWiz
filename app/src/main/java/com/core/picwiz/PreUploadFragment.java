package com.core.picwiz;


import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class PreUploadFragment extends android.app.Fragment {
    String image;
    private TouchImageView touchImageView;
    private TextView mTextViewLocation;
    private EditText mEditTextTags;

    private Bitmap imageRaw;
    private Bitmap imageScaled;

    private String imagePath;
    private Float Latitude, Longitude;
    Geocoder geocoder;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pre_upload, container, false);

        touchImageView = (TouchImageView) view.findViewById(R.id.touch_image_view_pre_upload);
        mTextViewLocation = (TextView) view.findViewById(R.id.text_view_location_pre_upload);
        mEditTextTags = (EditText) view.findViewById(R.id.edit_text_tag_pre_upload);

        image = getActivity().getIntent().getExtras().getString("image");
        imagePath = getRealPathFromURI(Uri.parse(image));
        try {
            imageRaw = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(image));
            new BitmapScalar(imageRaw).execute();
            new GetGeoLocation(imagePath).execute();

        } catch (IOException e) {
            e.printStackTrace();
            Log.i("Image: ", e.getMessage());
        }

        mEditTextTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) v.findViewById(R.id.edit_text_tag_pre_upload);
                String text = editText.getText().toString();
                if (text.isEmpty())
                    editText.setText("#");
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
        if(!showTick)
            menu.getItem(0).setEnabled(false);
        else
            menu.getItem(0).setEnabled(true);
    }

    public class BitmapScalar extends AsyncTask<Void, Void, Bitmap>
    {
        private Bitmap raw;
        BitmapScalar(Bitmap raw) {
            this.raw = raw;
        }
        public Bitmap scaleToFitWidth(Bitmap b, int width)
        {
            float factor = width / (float) b.getWidth();
            return Bitmap.createScaledBitmap(b, width, (int) (b.getHeight() * factor), true);
        }

        public Bitmap scaleToFitHeight(Bitmap b, int height)
        {
            float factor = height / (float) b.getHeight();
            return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factor), height, true);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap temp;
            temp = scaleToFitWidth(raw, 1080);
            temp = scaleToFitHeight(temp, 1440);
            return temp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            touchImageView.setImageBitmap(bitmap);
        }
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
                mTextViewLocation.setText(" "+addresses.get(0).getAddressLine(0) +", "+ addresses.get(0).getAddressLine(1) +", "+ addresses.get(0).getAddressLine(2));
                menu.getItem(0).setEnabled(true);
            }
            else {
                mTextViewLocation.setText(" Unable to fetch location, tap here to refresh.");
                menu.getItem(0).setEnabled(false);
            }
        }
    }
}
