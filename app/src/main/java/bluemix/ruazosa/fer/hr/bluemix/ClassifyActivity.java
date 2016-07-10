package bluemix.ruazosa.fer.hr.bluemix;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.ibm.watson.developer_cloud.http.ServiceCallback;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.AudioFormat;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ImageClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier;

import java.io.*;
import java.util.List;

public class ClassifyActivity extends AppCompatActivity {

    private static final String API_DATE = "2016-07-03";
    private static final String API_KEY = "48a48cd9251f53e09f099795245896557f7488f3";

    private ImageView imgCamera;
    private TextView txtClass;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);

        imgCamera = (ImageView) findViewById(R.id.img_camera);
        txtClass = (TextView) findViewById(R.id.txt_class);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            fileName = extras.getString("file");
        }

        try {
            File image = loadImage();
            displayImage(image);
            classifyImage(image);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(textToSpeech());
    }

    private File loadImage() {
//        return new File("/storage/emulated/0/Download", "gx6romp.jpg");
        return new File(fileName);
    }

    private void displayImage(File image) throws FileNotFoundException {
        Bitmap bitmapImage = BitmapFactory.decodeFile(image.getPath());
        int nh = (int) (bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()));
        Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
        imgCamera.setImageBitmap(scaled);

//        Bitmap b = BitmapFactory.decodeFile(fileName);
//        imgCamera.setImageBitmap(b);
    }

    private void classifyImage(File image) {
        VisualRecognition service = new VisualRecognition(API_DATE);
        service.setApiKey(API_KEY);

        final ClassifyImagesOptions classifyImagesOptions = new ClassifyImagesOptions.Builder()
                .images(image)
                .build();

        service.classify(classifyImagesOptions).enqueue(new ServiceCallback<VisualClassification>() {
            @Override
            public void onResponse(final VisualClassification response) {
                List<ImageClassification> images = response.getImages();
                if(images.size() > 0) {
                    List<VisualClassifier> classifiers = images.get(0).getClassifiers();

                    if(classifiers.size() > 0) {
                        final List<VisualClassifier.VisualClass> classes = classifiers.get(0).getClasses();

                        if(classes.size() > 0) {
                            Log.d("SLIKA:", response.getImages().get(0).getClassifiers().get(0).toString());
                            Log.d("SLIKA:", response.getImages().get(0).getClassifiers().get(0).getClasses().get(0).getName());

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txtClass.setText(classes.get(0).getName());
                                }
                            });
                        } else {
                            classificationUnknown();
                        }
                    } else {
                        classificationUnknown();
                    }
                } else {
                    classificationUnknown();
                }

            }

            @Override
            public void onFailure(Exception e) {
                txtClass.setText(R.string.classification_error);
            }
        });
    }

    private void classificationUnknown() {
        txtClass.setText(R.string.classification_unknown);
    }

    private View.OnClickListener textToSpeech() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextToSpeech service = new TextToSpeech();
                service.setUsernameAndPassword("2e17aa3c-40ec-4b32-b276-6bce254e4911", "uWyTHqcWVC1P");

                String text = txtClass.getText().toString();
                service.synthesize(text, Voice.GB_KATE, AudioFormat.WAV).enqueue(new ServiceCallback<InputStream>() {

                    @Override
                    public void onResponse(InputStream response) {
                        try {

                            InputStream in = WaveUtils.reWriteWaveHeader(response);

                            File outputDir = Environment.getExternalStorageDirectory();
//                            File outputFile = File.createTempFile("hello_word", ".wav", outputDir);
                            File outputFile = new File(Environment.getExternalStorageDirectory(), "hello_world.wav");
                            outputFile.setReadable(true, false);
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

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ClassifyActivity.this, "BOK SVEN", Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (final Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ClassifyActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                            Log.d("ERROR: ", e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(final Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ClassifyActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });

            }
        };

    }
}
