package com.example.mayuresh.lasttry;

import android.os.Environment;

import java.io.File;

/**
 * Created by Mayuresh on 31-Dec-15.
 */
public class CreateFolders {
    public CreateFolders(){}
    public boolean create()
    {
        if(isExternalStorageWritrable())
        {
            File f=new File(Environment.getExternalStorageDirectory(),"/DigiNotice/");
            if(!f.exists())
            {
                if(f.mkdirs()) {
                 f=new File(Environment.getExternalStorageDirectory(),"/DigiNotice/Department/");
                    f.mkdirs();
                    f=new File(Environment.getExternalStorageDirectory(),"/DigiNotice/Tpo/");
                    f.mkdirs();
                    f=new File(Environment.getExternalStorageDirectory(),"/DigiNotice/Scholarship/");
                    f.mkdirs();
                    f=new File(Environment.getExternalStorageDirectory(),"/DigiNotice/ExamCell/");
                    f.mkdirs();
                    f=new File(Environment.getExternalStorageDirectory(),"/DigiNotice/General/");
                    f.mkdirs();
                    return true;
                }
                else
                    return false;
            }

        }
        return  true;
    }
    public boolean isExternalStorageWritrable()
    {
        String state= Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state))
            return true;
        else
            return false;
    }
    boolean isExternalStorageReadable()
    {
        String state=Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)||Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
            return true;
        else
            return false;

    }
}
