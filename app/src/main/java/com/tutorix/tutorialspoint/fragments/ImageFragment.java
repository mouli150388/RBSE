package com.tutorix.tutorialspoint.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tutorix.tutorialspoint.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {

ImageView img;
    public ImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        img=getView().findViewById(R.id.img);
        String path=getArguments().getString("img");
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  if(current.video_url==null||current.video_url.trim().length()<4)
                {
                    return;
                }
                if (AppStatus.getInstance(getActivity()).isOnline()) {
                    Intent i = new Intent(getActivity(), ExpertsProfileActivity.class);
                    i.putExtra(Constants.facultyName, current.full_name);
                    i.putExtra(Constants.introduction, current.introduction);
                    i.putExtra(Constants.photoUrl, current.photo_url);
                    i.putExtra(Constants.videoUrl, current.video_url);
                    i.putExtra(Constants.expertise, current.expertise);
                    startActivity(i);
                } else {
                    CommonUtils.showToast(getActivity(),getActivity().getString(R.string.no_internet));
                    // Toasty.info(context, "There is no internet.", Toast.LENGTH_SHORT, true).show();
                }*/
            }
        });
        // String videourl = current.video_thumb_url;
        Picasso.with(getActivity()).load(path).placeholder(R.drawable.circle_default_load).into(img);
    }
}
