package com.oubeichen.resourcemonitor;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity implements OnClickListener {

    private ListView mListView;
    private Button clearBtn;
    private List<String> list;
    
    public static final Uri CONTENT_URI = Uri.parse("content://com.oubeichen.resourcemonitor/camerausage");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.camera_usage_list);
        selectDB();
        btnInit();

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.clear_button:
            getContentResolver().delete(CONTENT_URI, null, null);
            list.clear();
            updateView();
            break;
        }
    }

    private void selectDB() {
        // camera usage
        list = selectAll("camerausage");
        updateView();
    }

    private void btnInit() {
        clearBtn = (Button) findViewById(R.id.clear_button);
        clearBtn.setOnClickListener(this);
    }

    private void updateView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);
        mListView.setAdapter(adapter);
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * return all usage data
     * 
     * @param table
     * @return
     */
    public List<String> selectAll(String table) {
        int ID;
        String packagename;
        String methodname;
        int type;
        String time;

        Cursor c = getContentResolver().query(CONTENT_URI, new String[] {
                UsageLog.Camera._ID,
                UsageLog.Camera.PACKAGENAME, UsageLog.Camera.METHODNAME, UsageLog.Camera.TYPE,
                UsageLog.Camera.TIME },
                null, null, null);
        List<String> list = new ArrayList<String>();
        while (c.moveToNext()) {
            ID = c.getInt(c.getColumnIndex(UsageLog.Camera._ID));
            packagename = c.getString(c.getColumnIndex(UsageLog.Camera.PACKAGENAME));
            methodname = c.getString(c.getColumnIndex(UsageLog.Camera.METHODNAME));
            type = c.getInt(c.getColumnIndex(UsageLog.Camera.TYPE));
            time = c.getString(c.getColumnIndex(UsageLog.Camera.TIME));
            String string = ID + " \t"
                    + packagename +" \t"
                    + methodname + " \t"
                    + type + " \t" +
                    time;
            list.add(string);
        }
        c.close();
        return list;
    }

}
