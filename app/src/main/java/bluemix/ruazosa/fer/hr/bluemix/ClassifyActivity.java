package bluemix.ruazosa.fer.hr.bluemix;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.http.ServiceCallback;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.AudioFormat;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ImageClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassifyActivity extends AppCompatActivity {

    private ImageView imgCamera;
    private TextView txtClass;
    private String fileName;
    private CategoryItem selectedGender;
    private CategoryItem selectedLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);

        imgCamera = (ImageView) findViewById(R.id.img_camera);
        txtClass = (TextView) findViewById(R.id.txt_class);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            fileName = extras.getString(Constants.FILE);
            selectedGender = (CategoryItem) extras.getSerializable(Constants.GENDER);
            selectedLanguage = (CategoryItem) extras.getSerializable(Constants.LANGUAGE);
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
        return new File(fileName);
    }

    private void displayImage(File image) throws FileNotFoundException {
        Bitmap bitmapImage = BitmapFactory.decodeFile(image.getPath());
        int nh = (int) (bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()));
        Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
        imgCamera.setImageBitmap(scaled);
    }

    private void classifyImage(File image) {
        VisualRecognition service = new VisualRecognition(Constants.API_DATE);
        service.setApiKey(Constants.API_KEY);

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept-Language", selectedLanguage.getCode());
        service.setDefaultHeaders(headers);

        final ClassifyImagesOptions classifyImagesOptions = new ClassifyImagesOptions.Builder()
                .images(image)
                .build();

        service.classify(classifyImagesOptions).enqueue(new ServiceCallback<VisualClassification>() {
            @Override
            public void onResponse(final VisualClassification response) {
                try {
                    List<ImageClassification> images = response.getImages();
                    if(images.size() > 0) {
                        List<VisualClassifier> classifiers = images.get(0).getClassifiers();

                        if(classifiers.size() > 0) {
                            final List<VisualClassifier.VisualClass> classes = classifiers.get(0).getClasses();

                            if(classes.size() > 0) {
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
                } catch(final Exception e) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(ClassifyActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    });
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtClass.setText(R.string.classification_unknown);
            }
        });
    }

    private View.OnClickListener textToSpeech() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextToSpeech service = new TextToSpeech();
                service.setUsernameAndPassword(Constants.USERNAME, Constants.PASSWORD);

                String text = txtClass.getText().toString();
                service.synthesize(text, Constants.VOICES.get(
                        String.format("%s_%s", selectedGender.getCode(), selectedLanguage.getCode())),
                        AudioFormat.OGG).enqueue(new ServiceCallback<InputStream>() {
                    @Override
                    public void onResponse(InputStream response) {
                        try {

                            InputStream in = response;

                            File outputFile = new File(Environment.getExternalStorageDirectory(), "sound.ogg");
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
                        } catch (final Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ClassifyActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
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
