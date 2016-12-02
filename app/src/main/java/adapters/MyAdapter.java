package adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aleksandr.aleksandrov.clinic.R;

import java.util.List;

/**
 * Created by Aleksandr on 9/5/2016.
 */
public class MyAdapter extends BaseAdapter {

    private List<StringWithTag> myList;

    private Activity parentActivity;
    private LayoutInflater inflater;

    public MyAdapter(Activity parent, List<StringWithTag> l) {
        parentActivity = parent;
        myList = l;
        inflater = (LayoutInflater) parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public StringWithTag getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null)
            view = inflater.inflate(R.layout.sd, null);


        TextView text2 = (TextView) view.findViewById(R.id.text2);
        StringWithTag myObj = myList.get(position);
        //text1.setText(String.valueOf(myObj.getTag()));
        text2.setText(myObj.getString());
        text2.setSelected(true);
        return view;
    }
}