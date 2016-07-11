package bluemix.ruazosa.fer.hr.bluemix;

import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

import java.util.HashMap;

/**
 * Created by arijana on 7/11/16.
 */
public class Constants {

    public static final String API_DATE = "2016-07-03";
    public static final String API_KEY = "48a48cd9251f53e09f099795245896557f7488f3";
    public static final String USERNAME = "2e17aa3c-40ec-4b32-b276-6bce254e4911";
    public static final String PASSWORD = "uWyTHqcWVC1P";
    public static final String FILE = "file";
    public static final String GENDER = "gender";
    public static final String LANGUAGE = "language";
    public static HashMap<String, Voice> VOICES;
    public static Category GENDER_ITEMS;
    public static Category LANGUAGE_ITEMS;

    static {
        VOICES = new HashMap<>();
        VOICES.put("male_en", Voice.EN_MICHAEL);
        VOICES.put("female_en", Voice.EN_LISA);
        VOICES.put("female_es", Voice.ES_LAURA);
        VOICES.put("male_es", Voice.ES_ENRIQUE);
        VOICES.put("female_ja", Voice.JA_EMI);
        VOICES.put("male_ja", Voice.JA_EMI);

        GENDER_ITEMS = new Category(
                new CategoryItem("Male", "male"),
                new CategoryItem("Female", "female"));

        LANGUAGE_ITEMS = new Category(
                        new CategoryItem("English", "en"),
                        new CategoryItem("Spanish", "es"),
                        new CategoryItem("Japanese", "ja"));

    }

}
