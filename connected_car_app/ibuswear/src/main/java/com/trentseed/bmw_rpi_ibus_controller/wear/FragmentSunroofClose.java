package com.trentseed.bmw_rpi_ibus_controller.wear;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.trentseed.bmw_rpi_ibus_controller.common.IBUSWrapper;

public class FragmentSunroofClose extends Fragment {

    ImageView imgBtnDown;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ActivityMain.parent_page_id = IBUSGridPagerAdapter.PAGE_SUNROOF_CLOSE;
        View rootView = inflater.inflate(R.layout.window_action_down, container, false);
        imgBtnDown = rootView.findViewById(R.id.btn_down);
        imgBtnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    IBUSWrapper.toggleSunroof(false);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        return rootView;
    }

}
