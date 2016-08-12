package com.example.mayuresh.lasttry;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class TPO_Notices extends base {
    private Toolbar mtoolbar;
    ArrayAdapter adapter;
    int clickCounter=0;
    ArrayList listItems=new ArrayList();
    private File[] imagelist;
    String[] pdflist;
    private DrawerLayout drawerLayout;
    private ListView mainListView ;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_notices);
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
        CustomList adapter1=new CustomList(TPO_Notices.this , pdflist , R.drawable.pdf_icon);
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



        drawerLayout=(DrawerLayout)

                findViewById(R.id.navigation_drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null)

        {
            setupNavigationDrawerContent(navigationView);
        }

    }


    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.department:
                                menuItem.setChecked(true);
                                Intent i = new Intent(TPO_Notices.this, DepartmentalNotices.class);
                                startActivity(i);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.scholarship:
                                menuItem.setChecked(true);
                                Intent i1 = new Intent(TPO_Notices.this, Sholarship_Notices.class);
                                startActivity(i1);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.general:
                                menuItem.setChecked(true);
                                Intent i2 = new Intent(TPO_Notices.this, GeneralNotices.class);
                                startActivity(i2);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.tpo:
                                menuItem.setChecked(true);
                                Intent i3 = new Intent(TPO_Notices.this, TPO_Notices.class);
                                startActivity(i3);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.contact:
                                menuItem.setChecked(true);
                                Toast.makeText(TPO_Notices.this, menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                Intent intent = new Intent(TPO_Notices.this, Contact_And_Feedback.class);
                                startActivity(intent);
                                return true;
                            case R.id.logout:
                                menuItem.setChecked(true);
                                SharedPreferences checkObj = getSharedPreferences("LogIn", 0);
                                SharedPreferences.Editor editor = checkObj.edit();
                                editor.putBoolean("LogIn", false);
                                editor.commit();
                                Toast.makeText(TPO_Notices.this, "Logged Out!!", Toast.LENGTH_SHORT).show();
                                Intent q=new Intent(TPO_Notices.this,LogIn.class);
                                startActivity(q);
                                drawerLayout.closeDrawer(GravityCompat.START);

                                return true;
                        }
                        return true;
                    }
                });

    }


}