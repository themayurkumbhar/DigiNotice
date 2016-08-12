package com.example.mayuresh.lasttry;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class DepartmentalNotices extends AppCompatActivity {
    private Toolbar mtoolbar;
    ArrayAdapter adapter;
    int clickCounter=0;
    ArrayList listItems=new ArrayList();
    private File[] imagelist;
    String[] pdflist;

    private ListView mainListView ;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_notices);

        mainListView = (ListView) findViewById( R.id.list );
        mtoolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mtoolbar);
        File images =  new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + "Download/");
        imagelist = images.listFiles(new FilenameFilter(){
            public boolean accept(File dir, String name)
            {
                return ((name.endsWith("cse.pdf")));
            }
        });
        pdflist = new String[imagelist.length];
        for(int i = 0;i<imagelist.length;i++)
        {
            pdflist[i] = imagelist[i].getName();
        }
        CustomList adapter1=new CustomList(DepartmentalNotices.this , pdflist , R.drawable.pdf_icon);
        mainListView.setAdapter(adapter1);
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                PackageManager packageManager = getPackageManager();
                Intent testIntent = new Intent(Intent.ACTION_VIEW);
                testIntent.setType("application/pdf");
                List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
                if (list.size() > 0 && imagelist[(int) id].isFile()) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    Uri uri = Uri.fromFile(imagelist[(int) id].getAbsoluteFile());
                    intent.setDataAndType(uri, "application/pdf");
                    startActivity(intent);
                }
            }

        });
    }

}