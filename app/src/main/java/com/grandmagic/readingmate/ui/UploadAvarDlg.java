package com.grandmagic.readingmate.ui;

import android.content.Context;

import com.grandmagic.readingmate.R;

import java.util.ArrayList;

/**
 * Created by zhangmengqi on 2017/3/8.
 */

public class UploadAvarDlg extends ListDialog  {


    public UploadAvarDlg(Context context) {
        super(context);
        setTitleStr(context.getString(R.string.change_avar));
        ArrayList<String> data = new ArrayList<String>();
        data.add(context.getString(R.string.take_photo));
        data.add(context.getString(R.string.pick_from_album));
        data.add(context.getString(R.string.save_photo));
        setData(data);

    }

}
