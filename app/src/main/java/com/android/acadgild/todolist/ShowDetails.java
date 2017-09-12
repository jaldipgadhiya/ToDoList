package com.android.acadgild.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.acadgild.todolist.adapter.CustomAdapter;
import com.android.acadgild.todolist.database.DBHelper;
import com.android.acadgild.todolist.model.ToDoData;
import com.android.acadgild.todolist.utils.CommonUtilities;
import com.android.acadgild.todolist.utils.Constants;

import java.util.ArrayList;

/**
 * Created by Jal on 28-07-2017.
 * This is activity class to display all completed tasks.
 */

public class ShowDetails extends AppCompatActivity implements AdapterView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    DBHelper dbHelper;
    ArrayList<ToDoData> model= new ArrayList<>();
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_completed);
        dbHelper= CommonUtilities.getDBObject(this);
        Intent intentObject = getIntent();

        list= (ListView)findViewById(R.id.toDoCompletedList);

        //To get list of all completed tasks and set list custom adapter for same
        model = dbHelper.getAllCompletedToDos();
        CustomAdapter adapter= new CustomAdapter(this, model);
        list.setAdapter(adapter);

        // setOnItemLongClickListener event of list to delete details of completed tasks
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                //Get id to be deleted
                int deletingId = model.get(pos).getKeyId();

                //call method to delete record
                dbHelper.deleteRecords(Constants.TO_DO_RECORD, "key_id = "+deletingId,null);


                Toast.makeText(ShowDetails.this, "Row Deleted: ", Toast.LENGTH_LONG).show();

                //To get list of all completed tasks and set list custom adapter for same
                model = dbHelper.getAllCompletedToDos();
                CustomAdapter adapter= new CustomAdapter(ShowDetails.this, model);
                list.setAdapter(adapter);

                Log.v("long clicked","pos: " + pos);

                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
