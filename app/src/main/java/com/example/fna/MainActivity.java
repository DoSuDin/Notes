package com.example.fna;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    DBNotes mDBConnector;
    Context mContext;
    ListView mListView;
    SimpleCursorAdapter scAdapter;
    Cursor cursor;
    myListAdapter myAdapter;
    public Button add, delete, searchButton;
    public EditText search;
    private DBNotes DataBase;


    int ADD_ACTIVITY = 0;
    int UPDATE_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mDBConnector = new DBNotes(this);
        mListView = findViewById(R.id.List);
        myAdapter = new myListAdapter(mContext, mDBConnector.selectAll(null, null));
        mListView.setAdapter(myAdapter);
        registerForContextMenu(mListView);

        add = findViewById(R.id.add);
        delete = findViewById(R.id.deleteAll);
        search  = findViewById(R.id.search);
        searchButton = findViewById(R.id.searchButton);

        add.setOnClickListener(this);
        delete.setOnClickListener(this);
        searchButton.setOnClickListener(this);

        DataBase = new DBNotes(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mContext = this;
        mDBConnector = new DBNotes(this);
        mListView = findViewById(R.id.List);
        myAdapter = new myListAdapter(mContext, mDBConnector.selectAll(null, null));
        mListView.setAdapter(myAdapter);
        registerForContextMenu(mListView);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add) {
            Intent intent = new Intent(this, AddActivity.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.deleteAll) {
            mDBConnector.deleteAll();
            updateList();
        }
        else if(view.getId() == R.id.searchButton){
            String Search = search.getText().toString();
            String[] s = {Search};
            mContext = this;
            mDBConnector = new DBNotes(this);
            mListView = findViewById(R.id.List);
            myAdapter = new myListAdapter(mContext, mDBConnector.selectAll("TAG = ?", s));
            mListView.setAdapter(myAdapter);
            registerForContextMenu(mListView);
            myAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(item.getItemId() == R.id.edit){
            Intent i = new Intent(mContext, UpdateActivity.class);
            Notes md = mDBConnector.select(info.id);
            i.putExtra("Notes", md);
            startActivity(i);
            return true;
        }
        else if(item.getItemId() == R.id.delete){
            mDBConnector.delete(info.id);
            updateList();
            return true;
        }
        else{
            return super.onContextItemSelected(item);
        }
    }

    private void updateList() {
        myAdapter.setArrayMyData(mDBConnector.selectAll(null,null));
        myAdapter.notifyDataSetChanged();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            Notes md = (Notes) data.getExtras().getSerializable("Notes");
            if (requestCode == UPDATE_ACTIVITY)
                mDBConnector.update(md);
            else
                mDBConnector.insert(md.getTitle(), md.getTexte(), md.getTag());
            updateList();
        }
    }

    class myListAdapter extends BaseAdapter {
        private LayoutInflater mLayoutInflater;
        private ArrayList<Notes> arrayMyNotes;

        public myListAdapter(Context ctx, ArrayList<Notes> arr) {
            mLayoutInflater = LayoutInflater.from(ctx);
            setArrayMyData(arr);
        }

        public ArrayList<Notes> getArrayMyData() {
            return arrayMyNotes;
        }

        public void setArrayMyData(ArrayList<Notes> arrayMyData) {
            this.arrayMyNotes = arrayMyData;
        }

        public int getCount() {
            return arrayMyNotes.size();
        }

        public Object getItem(int position) {

            return position;
        }

        public long getItemId(int position) {
            Notes md = arrayMyNotes.get(position);
            if (md != null) {
                return md.getId();
            }
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null)
                convertView = mLayoutInflater.inflate(R.layout.item, null);

            TextView vTitle = convertView.findViewById(R.id.Title);
            TextView vText = convertView.findViewById(R.id.Text);


            Notes md = arrayMyNotes.get(position);
            vTitle.setText(md.getTitle());
            vText.setText(md.getTexte());

            return convertView;
        }
    }
}