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

import fragments.Utils;

/**
 * Created by Aleksandr on 9/5/2016.
 */
public class ReminderAdapter extends BaseAdapter {

    private List<RemindObject> myList;

    private Activity parentActivity;
    private LayoutInflater inflater;

    public ReminderAdapter(Activity parent, List<RemindObject> l) {
        parentActivity = parent;
        myList = l;
        inflater = (LayoutInflater) parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public RemindObject getItem(int position) {
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
            view = inflater.inflate(R.layout.list_item_reminder, null);


        TextView text1 = (TextView) view.findViewById(R.id.text1);
        TextView text2 = (TextView) view.findViewById(R.id.text2);
        RemindObject myObj = myList.get(position);
        text1.setText(myObj.getDoctor());
        text1.setSelected(true);
        //Log.d("myLogs", "time" + myObj.getTime());

        //text2.setText(Long.toString(myObj.getTime()));
        text2.setText(Utils.getFullDate(myObj.getTime()));
        text2.setSelected(true);
        return view;
    }
}