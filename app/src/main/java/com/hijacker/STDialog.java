package com.hijacker;

/*
    Copyright (C) 2016  Christos Kyriakopoylos

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import static com.hijacker.MainActivity.getLastSeen;
import static com.hijacker.MainActivity.background;
import static com.hijacker.MainActivity.runInHandler;

public class STDialog extends DialogFragment {
    ST info_st;
    TextView st[] = {null, null, null, null, null, null, null};
    Runnable runnable;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.st_info, null);

        if(st[0]==null) {
            st[0] = (TextView)view.findViewById(R.id.mac_st);
            st[1] = (TextView)view.findViewById(R.id.bssid_st);
            st[2] = (TextView)view.findViewById(R.id.pwr_st);
            st[3] = (TextView)view.findViewById(R.id.frames_st);
            st[4] = (TextView)view.findViewById(R.id.lost_st);
            st[5] = (TextView)view.findViewById(R.id.manuf_st);
            st[6] = (TextView)view.findViewById(R.id.lastseen_st);
        }

        st[0].setText(info_st.mac);

        if(info_st.bssid==null) st[1].setText(R.string.not_connected);
        else if(AP.getAPByMac(info_st.bssid)!=null) st[1].setText(info_st.bssid + " (" + AP.getAPByMac(info_st.bssid).essid + ")");
        else st[1].setText(info_st.bssid);

        st[2].setText(Integer.toString(info_st.pwr));
        st[3].setText(Integer.toString(info_st.frames));
        st[4].setText(Integer.toString(info_st.lost));
        st[5].setText(info_st.manuf);
        st[6].setText(getLastSeen(info_st.lastseen));

        runnable = new Runnable(){
            @Override
            public void run(){
                st[0].setText(info_st.mac);

                if(info_st.bssid==null) st[1].setText(R.string.not_connected);
                else if(AP.getAPByMac(info_st.bssid)!=null) st[1].setText(info_st.bssid + " (" + AP.getAPByMac(info_st.bssid).essid + ")");
                else st[1].setText(info_st.bssid);

                st[2].setText(Integer.toString(info_st.pwr));
                st[3].setText(Integer.toString(info_st.frames));
                st[4].setText(Integer.toString(info_st.lost));
                st[5].setText(info_st.manuf);
                st[6].setText(getLastSeen(info_st.lastseen));
            }
        };
        new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    Thread.sleep(1000);
                    while(STDialog.this.isResumed()){
                        runInHandler(runnable);
                        Thread.sleep(1000);
                    }
                }catch(InterruptedException ignored){}
            }
        }).start();

        builder.setView(view);
        builder.setTitle(info_st.mac);
        builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //close
            }
        });
        return builder.create();
    }
    @Override
    public void show(FragmentManager fragmentManager, String tag){
        if(!background) super.show(fragmentManager, tag);
    }
}
