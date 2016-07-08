package bluemix.ruazosa.fer.hr.bluemix;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.http.ServiceCallback;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.AudioFormat;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(textToSpeech());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ListView listViewGender = (ListView) navigationView.findViewById(R.id.list_gender);
        listViewGender.setAdapter(new ListViewAdapter(
                new Category("Male", "Female")));

        ListView listViewLanguage = (ListView) navigationView.findViewById(R.id.list_language);
        listViewLanguage.setAdapter(new ListViewAdapter(
                new Category("English", "Spanish", "Japanese")));
    }

    private View.OnClickListener textToSpeech() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextToSpeech service = new TextToSpeech();
                service.setUsernameAndPassword("2e17aa3c-40ec-4b32-b276-6bce254e4911", "uWyTHqcWVC1P");

                String text = "harry potter and philosopher's stone";
                service.synthesize(text, Voice.GB_KATE, AudioFormat.WAV).enqueue(new ServiceCallback<InputStream>() {

                    @Override
                    public void onResponse(InputStream response) {
                        try {

                            InputStream in = WaveUtils.reWriteWaveHeader(response);
                            File outputDir =  getCacheDir();
                            File outputFile = File.createTempFile("hello_word", ".wav", outputDir);
                            OutputStream out = new FileOutputStream(outputFile);

                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = in.read(buffer)) > 0) {
                                out.write(buffer, 0, length);
                            }
                            out.close();
                            in.close();
                            response.close();

                            FileInputStream fis = new FileInputStream(outputFile);
                            MediaPlayer mediaPlayer = new MediaPlayer();
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(fis.getFD());
                            mediaPlayer.prepare();
                            mediaPlayer.start();

                        } catch (Exception e) {
                            Log.d("ERROR: ", e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });

            }
        };

    }

    private class ListViewAdapter extends BaseAdapter {

        private Category category;

        public ListViewAdapter(Category category) {
            this.category = category;
        }

        @Override
        public int getCount() {
            return category.getItems().length;
        }

        @Override
        public String getItem(int position) {
            return category.getItems()[position];
        }

        @Override
        public long getItemId(int position) {
            return category.getItems()[position].hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item, container, false);
            }
            ((TextView) convertView.findViewById(android.R.id.text1))
                    .setText(getItem(position));
            return convertView;
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.list_gender) {
            Log.d("MENU: ", "dskljfsdkjfsdk");
        }

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
