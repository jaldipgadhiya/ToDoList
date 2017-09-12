package com.android.acadgild.todolist;

/*
This is main activity class, Launcher Class
to show Main List of To Do List  data details
Menu configuration
onItemClick of main list configuration
onItemClick of Navigation view list configuration
etOnItemLongClickListener to set the task done from undone
 */
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.acadgild.todolist.adapter.CustomAdapter;
import com.android.acadgild.todolist.database.DBHelper;
import com.android.acadgild.todolist.model.ToDoData;
import com.android.acadgild.todolist.utils.CommonUtilities;
import com.android.acadgild.todolist.utils.Constants;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView list;
    ArrayList<ToDoData> model= new ArrayList<>();
    DBHelper dbHelper;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        TextView tvTitle = (TextView) view.findViewById(R.id.toDoTitle) ;
        TextView tvDesc = (TextView) view.findViewById(R.id.toDoDesc) ;
        TextView tvDueDate = (TextView) view.findViewById(R.id.toDoDueDate) ;

        //To create dialog to update to do task details
        AlertDialog.Builder mBuilder =  new AlertDialog.Builder(MainActivity.this);
        final View mView = getLayoutInflater().inflate(R.layout.dialog_addtask,null);
        EditText mTitle = (EditText) mView.findViewById(R.id.etTitle);
        EditText mDesc = (EditText) mView.findViewById(R.id.etDesc);
        DatePicker mDueDate = (DatePicker) mView.findViewById(R.id.dtDueDate);
        mTitle.setText(tvTitle.getText().toString());
        mDesc.setText(tvDesc.getText().toString());

        int first_index = tvDueDate.getText().toString().indexOf("/");
        int last_index = tvDueDate.getText().toString().lastIndexOf("/");
        int date= Integer.parseInt(tvDueDate.getText().toString().substring(0,first_index));
        int month= Integer.parseInt(tvDueDate.getText().toString().substring(first_index+1,last_index));
        int year= Integer.parseInt(tvDueDate.getText().toString().substring(last_index+1));
        mDueDate.updateDate(year,month,date);
        Button mSave = (Button) mView.findViewById(R.id.btnSave);
        Button mCancel = (Button) mView.findViewById(R.id.btnCancel);

        // Set view to dialog
        mBuilder.setView(mView);
        //Create dialog
        final AlertDialog dialog = mBuilder.create();
        //Show dialog
        dialog.show();


        //Create on click listener for save button on Dialog to update details in DB
        mSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                final EditText mTitle = (EditText) mView.findViewById(R.id.etTitle);
                final EditText mDesc = (EditText) mView.findViewById(R.id.etDesc);
                final DatePicker mDueDate = (DatePicker) mView.findViewById(R.id.dtDueDate);

                //Update record method to update to do task record
                updateToDoRecord(mView,mTitle,mDesc,mDueDate,position );
                //to close dialog
                dialog.cancel();
            }
        });

        //to close dialog on cancel button
        mCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar();

        list= (ListView)findViewById(R.id.toDoList);

        // To get all To do tasks list on main screen
        dbHelper= CommonUtilities.getDBObject(this);
        //To get list of all completed tasks and set list custom adapter for same
        model = dbHelper.getAllToDos();
        final CustomAdapter adapter= new CustomAdapter(this, model);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);


        //setOnItemLongClickListener to set the task done from undone it will update the task as done in DB and change image thumb down
        // to thumb up.
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                String uri = "@drawable/thumbupblack";
                Drawable dbDrawable = getResources().getDrawable(R.drawable.thumbupblack);
                Bitmap bitmap = ((BitmapDrawable)dbDrawable).getBitmap();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                int updatingId = model.get(pos).getKeyId();
                ContentValues vals = new ContentValues();
                vals.put(Constants.KEY_STATUS, 1);
                vals.put(Constants.KEY_IMAGE, stream.toByteArray());
                Log.d("Long Click","updatingId: " + updatingId);
                long insId = dbHelper.updateRecords(Constants.TO_DO_RECORD, vals,"key_id = "+updatingId,null);

                if(insId != -1) {
                    model = dbHelper.getAllToDos();
                    Log.d("Long Click","image: " + model.get(0).getKeyPhotoImage());
                    CustomAdapter adapter= new CustomAdapter(MainActivity.this, model);
                    list.setAdapter(adapter);

                    Toast.makeText(MainActivity.this, "New row updated, row id: " + insId, Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_LONG).show();



                Log.v("long clicked","pos: " + pos);

                return true;
            }
        });
    }

    // To inflate options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater M = getMenuInflater();
        M.inflate(R.menu.main, menu);



        //  getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // To onOptionsItemSelected from menu when menu item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //when add menuitem is clicked
        if (id == R.id.menuAddToDo) {
            AlertDialog.Builder mBuilder =  new AlertDialog.Builder(MainActivity.this);
            final View mView = getLayoutInflater().inflate(R.layout.dialog_addtask,null);
            final EditText mTitle = (EditText) mView.findViewById(R.id.etTitle);
            final EditText mDesc = (EditText) mView.findViewById(R.id.etDesc);
            final DatePicker mDueDate = (DatePicker) mView.findViewById(R.id.dtDueDate);
            Button mSave = (Button) mView.findViewById(R.id.btnSave);
            Button mCancel = (Button) mView.findViewById(R.id.btnCancel);
            //set view to dialog
            mBuilder.setView(mView);
            //create dialog
            final AlertDialog dialog = mBuilder.create();
            //show dialog
            dialog.show();

            // on save button of Dialog insert records inside DB

            mSave.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // insert record method in DB
                    insertToDoRecord(mView,mTitle,mDesc,mDueDate );
                    dialog.cancel();
                }
            });
            // to close dialog
            mCancel.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });



            return true;
        }
        //when to show tasks done menu item is selected it will redirect to tasks done activity and display list of tasks done
        else if (id == R.id.menuGetTasksDone) {
            Intent openNewActivity = new Intent(MainActivity.this, ShowDetails.class);
            startActivity(openNewActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // method to insert records inside DB
    public void insertToDoRecord(View v,EditText vTitle,EditText vDesc,DatePicker vDueDate){

        dbHelper= CommonUtilities.getDBObject(this);
        String date = Integer.toString(vDueDate.getDayOfMonth())+"/"+
                Integer.toString(vDueDate.getMonth())+"/"+Integer.toString(vDueDate.getYear());
        Drawable dbDrawable = getResources().getDrawable(R.drawable.thumbdownblack);
        Bitmap bitmap = ((BitmapDrawable)dbDrawable).getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        if(!vTitle.getText().toString().isEmpty() && !vDesc.getText().toString().isEmpty())
        {
            ContentValues vals = new ContentValues();
            vals.put(Constants.KEY_TITLE, vTitle.getText().toString());
            vals.put(Constants.KEY_DESCRIPTION, vDesc.getText().toString());
            vals.put(Constants.KEY_DATE, date);
            vals.put(Constants.KEY_STATUS, 0);
            vals.put(Constants.KEY_IMAGE, stream.toByteArray());
            long insId = dbHelper.insertContentVals(Constants.TO_DO_RECORD, vals);
            Log.d("Insert","insId: " + insId);
            if(insId != -1){
                //To get list of all completed tasks and set list custom adapter for same
                model = dbHelper.getAllToDos();
                Log.d("Insert","image: " + model.get(0).getKeyPhotoImage());
                CustomAdapter adapter= new CustomAdapter(this, model);
                list.setAdapter(adapter);
                Toast.makeText(MainActivity.this, "New row added, row id: " + insId, Toast.LENGTH_SHORT).show();}

            else
                Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
        }
    }
    //method to update records inside DB
    public void updateToDoRecord(View v,EditText vTitle,EditText vDesc,DatePicker vDueDate,int pos){

        dbHelper= CommonUtilities.getDBObject(this);
        model = dbHelper.getAllToDos();
        int updatingId = model.get(pos).getKeyId();
        String date = Integer.toString(vDueDate.getDayOfMonth())+"/"+
                Integer.toString(vDueDate.getMonth())+"/"+Integer.toString(vDueDate.getYear());

        if(!vTitle.getText().toString().isEmpty() && !vDesc.getText().toString().isEmpty())
        {
            ContentValues vals = new ContentValues();
            vals.put(Constants.KEY_TITLE, vTitle.getText().toString());
            vals.put(Constants.KEY_DESCRIPTION, vDesc.getText().toString());
            vals.put(Constants.KEY_DATE, date);
            Log.d("Insert","updatingId Update: " + updatingId);
            long insId = dbHelper.updateRecords(Constants.TO_DO_RECORD, vals,"key_id = "+updatingId,null);

            if(insId != -1){
                //To get list of all completed tasks and set list custom adapter for same
                model = dbHelper.getAllToDos();
                Log.d("Update","image: " + model.get(0).getKeyPhotoImage());
                CustomAdapter adapter= new CustomAdapter(this, model);
                list.setAdapter(adapter);
                Toast.makeText(MainActivity.this, "New row updated, row id: " + insId, Toast.LENGTH_LONG).show();}
            else
                Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_LONG).show();
        }
    }
}
