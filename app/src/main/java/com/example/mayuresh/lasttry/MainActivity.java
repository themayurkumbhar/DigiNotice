package com.example.mayuresh.lasttry;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ProgressDialog pDialog;
    private Toolbar mtoolbar;
    private DrawerLayout drawerLayout;
    private TextView textView;
    public static String newRegID = "";
    public static String settnewRegID = "";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final String TAG = "MainActivity";
    private FloatingActionButton fab;
    AlertDialog alert;

    String name,email,url;
    public static final int progress_bar_type=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
        cretaeTabs();
        SharedPreferences checkObj = getSharedPreferences("LogIn", 0);
        checkLogIn(checkObj);
        name=getIntent().getStringExtra("USERNAME");
        email=getIntent().getStringExtra("EMAIL");
        if(!new CreateFolders().create())
            Log.i("error","FolderErrors");


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        fab.setBackgroundResource(R.drawable.ic_camera_black_24dp);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                String sentToken = sharedPreferences
                        .getString(Constants.PREF_GCM_REG_ID, "");
                System.out.println("SERVER_SUCCESS")
                ;
                if (sentToken.equals("")) {
                    Toast.makeText(MainActivity.this, "Failed to save on server", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Now you can receive notifications!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        if (checkPlayServices()) {

            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        mtoolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mtoolbar);



    drawerLayout=(DrawerLayout)findViewById(R.id.navigation_drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null)

        {
            setupNavigationDrawerContent(navigationView);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Notice download");
        builder.setMessage("Do you want to Download the Notice?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener()

                {

                    public void onClick(DialogInterface dialog, int which) {
                        // Intent i = new Intent(Intent.ACTION_VIEW);
                        //i.setData(Uri.parse(url));
                        // startActivity(i);

                        String path = "";
                        if (url.contains("department"))
                            path = Environment.getExternalStorageDirectory() + "/DigiNotice/General/";
                        else if (url.contains("c02"))
                            path = Environment.getExternalStorageDirectory() + "/DigiNotice/Tpo/";
                        else if (url.contains("scholarship"))
                            path = Environment.getExternalStorageDirectory() + "DigiNotice/Scholarship/";
                        else if (url.contains("cse"))
                            path = Environment.getExternalStorageDirectory() + "/DigiNotice/Depatmental/";
                        else if (url.contains("examcell"))
                            path = Environment.getExternalStorageDirectory() + "/DigiNotice/ExamCell/";
                        else
                            path = Environment.getExternalStorageDirectory() + "/DigiNotice/General/";

                        new DownloadFileFromURL().execute(url, path);
                        dialog.dismiss();
                    }

                }

        );

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener()

                {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }

        );

    alert=builder.create();
    }

    private void cretaeTabs() {

        DatabaseHandler db=new DatabaseHandler(this);
        for (int i=0;i<4;i++) {
            db.addNotice(new Notices(i, "Title Of New Notice", "this is Description!!!!"));
        }



        final ViewPager viewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:

                        break;
                    case 1:



                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    private void setupNavigationDrawerContent(NavigationView navigationView) {
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {

                            switch (menuItem.getItemId()) {
                                case R.id.department:
                                    menuItem.setChecked(true);
                                    Intent i = new Intent(MainActivity.this, DepartmentalNotices.class);
                                    startActivity(i);
                                    drawerLayout.closeDrawer(GravityCompat.START);
                                    return true;
                                case R.id.scholarship:
                                    menuItem.setChecked(true);
                                    Intent i1 = new Intent(MainActivity.this, Sholarship_Notices.class);
                                    startActivity(i1);
                                    drawerLayout.closeDrawer(GravityCompat.START);
                                    return true;
                                case R.id.general:
                                    menuItem.setChecked(true);
                                    Intent i2 = new Intent(MainActivity.this, GeneralNotices.class);
                                    startActivity(i2);
                                    drawerLayout.closeDrawer(GravityCompat.START);
                                    return true;
                                case R.id.tpo:
                                    menuItem.setChecked(true);
                                    Intent i3 = new Intent(MainActivity.this, TPO_Notices.class);
                                    startActivity(i3);
                                    drawerLayout.closeDrawer(GravityCompat.START);
                                    return true;
                                case R.id.contact:
                                    menuItem.setChecked(true);
                                    Toast.makeText(MainActivity.this, menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                    drawerLayout.closeDrawer(GravityCompat.START);
                                    Intent intent = new Intent(MainActivity.this, Contact_And_Feedback.class);
                                    startActivity(intent);
                                    return true;
                                case R.id.wce:
                                    menuItem.setChecked(true);
                                    Toast.makeText(MainActivity.this, menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                    drawerLayout.closeDrawer(GravityCompat.START);
                                    Intent intent4 = new Intent(MainActivity.this, walchandnetwork.class);
                                    startActivity(intent4);
                                    return true;
                                case R.id.logout:
                                    menuItem.setChecked(true);
                                    SharedPreferences checkObj = getSharedPreferences("LogIn", 0);
                                    SharedPreferences.Editor editor = checkObj.edit();
                                    editor.putBoolean("LogIn", false);
                                    editor.commit();
                                    Toast.makeText(MainActivity.this, "Logged Out!!", Toast.LENGTH_SHORT).show();
                                    Intent q = new Intent(MainActivity.this, LogIn.class);
                                    startActivity(q);
                                    drawerLayout.closeDrawer(GravityCompat.START);

                                    return true;
                            }
                            return true;
                        }
                    });
        }




    private void checkLogIn(SharedPreferences checkObj) {
        boolean ch = checkObj.getBoolean("LogIn", false);
        if (ch) {
            return;
        } else {
           // Intent i = new Intent(getBaseContext(), LogIn.class);
           // startActivity(i);
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this,"settings",Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    @Override
    public void onClick(View view) {

        if (view == fab) {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                alert.show();
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                 url = contents;

            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Log.i("App", "Scan unsuccessful");
            }
        }
    }
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        9000).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constants.SERVER_SUCCESS));
    }

    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }




    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }




    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new DummyFragment(), "Notifications");
        adapter.addFrag(new DummyFragment(), "Saved Links");
        viewPager.setAdapter(adapter);
    }

    public static class DummyFragment extends Fragment {
        private RecyclerView recyclerView;
        private RecyclerView.Adapter adapter;
        String[] dataArray;

        public DummyFragment() {
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dummy_fragment, container, false);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());

            recyclerView = (RecyclerView) view.findViewById(R.id.dummyfrag_scrollableview);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(linearLayoutManager);

            DatabaseHandler db=new DatabaseHandler(getContext());
            List<Notices> obj=db.getAllNotices();
             adapter = new RecyclerAdapter(obj);
            Log.i("size",adapter.getItemCount()+"");

            recyclerView.setAdapter(adapter);
            return view;
        }
    }



    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                String path=f_url[1];
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                 OutputStream output = new FileOutputStream(path+"title_"+(int)Math.random()*100+".pdf");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);

        }

    }
}
