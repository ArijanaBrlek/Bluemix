package bluemix.ruazosa.fer.hr.bluemix;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.ibm.watson.developer_cloud.http.ServiceCallback;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ImageClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class ClassifyActivity extends AppCompatActivity {

    private static final String API_DATE = "2016-07-03";
    private static final String API_KEY = "48a48cd9251f53e09f099795245896557f7488f3";

    private ImageView imgCamera;
    private TextView txtClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);

        imgCamera = (ImageView) findViewById(R.id.img_camera);
        txtClass = (TextView) findViewById(R.id.txt_class);

        try {
            File image = loadImage();
            displayImage(image);
            classifyImage(image);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private File loadImage() {
        return new File("/storage/emulated/0/Download", "gx6romp.jpg");
    }

    private void displayImage(File image) throws FileNotFoundException {
        Bitmap b = BitmapFactory.decodeStream(new FileInputStream(image));
        imgCamera.setImageBitmap(b);
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
}
