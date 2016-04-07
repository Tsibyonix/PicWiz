package com.core.picwiz;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private RecyclerView mRecyclerViewHomeFragment;
    private HomeRecyclerViewAdapter homeRecyclerViewAdapter;
    private List<HomeRecyclerList> homeRecyclerLists;
    private FloatingActionButton mFloatingActionButtonAddPhoto;
    private int mScrollOffset = 4;

    private static final int CAPTURE_PHOTO = 1;

    private Uri outputFileUri;

    public static HomeFragment newInstance(int sectionNumber) {
        return new HomeFragment();
    }

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeRecyclerLists = new ArrayList<>();
        homeRecyclerLists.add(new HomeRecyclerList("chronix", "To infinity and beyond", "2 hour", "#these #are #the #tags", R.drawable.im_header, "This is the universal caption xD", " Pahar Gung, New Delhi", " 0"));
        homeRecyclerLists.add(new HomeRecyclerList("d4n73", "sigh its difficult to code", "3 hour", "#these #are #the #tags", R.drawable.im_header, "This is the universal caption xD", " Pahar Gung, New Delhi", " 0"));
        homeRecyclerLists.add(new HomeRecyclerList("07", "bam bam", "4 hour", "#these #are #the #tags", R.drawable.im_header, "This is the universal caption xD", " Pahar Gung, New Delhi", " 0"));
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mFloatingActionButtonAddPhoto = (FloatingActionButton) view.findViewById(R.id.fab_add_photo);
        mRecyclerViewHomeFragment = (RecyclerView) view.findViewById(R.id.recycler_view_home_fragment);
        homeRecyclerViewAdapter = new HomeRecyclerViewAdapter(getActivity(), homeRecyclerLists);
        mRecyclerViewHomeFragment.setAdapter(homeRecyclerViewAdapter);
        mRecyclerViewHomeFragment.setHasFixedSize(false);
        mRecyclerViewHomeFragment.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFloatingActionButtonAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat curFormater = new SimpleDateFormat("dMy-hms");
                String date = curFormater.format(calendar.getTime()).trim();

                final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/PicWiz/";
                File newDir = new File(dir);
                if (!newDir.isDirectory()) {
                    newDir.mkdirs();
                }

                String pictureName = dir + "img-" + date + ".jpg";
                File picture = new File(pictureName);

                outputFileUri = Uri.fromFile(picture);

                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                camera.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(camera, CAPTURE_PHOTO);
                //startActivityForResult();
            }
        });

        mRecyclerViewHomeFragment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) > mScrollOffset) {
                    if (dy > 0) {
                        mFloatingActionButtonAddPhoto.hide(true);
                    } else {
                        mFloatingActionButtonAddPhoto.show(true);
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_PHOTO) {
            if (resultCode == getActivity().RESULT_OK) {
                Intent preUpload = new Intent(getActivity(), SecondaryActivity.class);
                preUpload.putExtra("toLoad", "upload_picture");
                preUpload.putExtra("image", outputFileUri.toString());
                startActivity(preUpload);
            }
        }
    }
}
