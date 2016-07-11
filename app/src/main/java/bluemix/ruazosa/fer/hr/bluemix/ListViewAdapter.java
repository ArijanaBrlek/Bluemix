package bluemix.ruazosa.fer.hr.bluemix;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by arijana on 7/11/16.
 */
public class ListViewAdapter extends BaseAdapter {

    private Category category;
    private Context context;

    public ListViewAdapter(Context context, Category category) {
        this.context = context;
        this.category = category;
    }

    @Override
    public int getCount() {
        return category.getItems().length;
    }

    public CategoryItem getCategoryItem(int position) {
        return category.getItems()[position];
    }

    @Override
    public String getItem(int position) {
        return category.getItems()[position].getValue();
    }

    @Override
    public long getItemId(int position) {
        return category.getItems()[position].hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.list_item, container, false);
        }
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(getItem(position));
        return convertView;
    }

}

