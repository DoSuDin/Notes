package com.example.fna;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends Activity implements View.OnClickListener {
    private Button btSave,btCancel;
    public EditText etTitle, etText, Tag;
    private Context context;
    public long MyNotesID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        etTitle = findViewById(R.id.Title);
        etText = findViewById(R.id.Text);
        btSave = findViewById(R.id.butSave);
        btCancel = findViewById(R.id.butCancel);
        Tag = findViewById(R.id.Tag);

        registerForContextMenu(etTitle);
        registerForContextMenu(etText);

        if(getIntent().hasExtra("Notes")){
            Notes notes = (Notes)getIntent().getSerializableExtra("Notes");
            etTitle.setText(notes.getTitle());
            etText.setText(notes.getTexte());
            Tag.setText(notes.getTag());
            MyNotesID=notes.getId();
        }
        else
        {
            MyNotesID=-1;
        }
        btSave.setOnClickListener(this);

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

        @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public void onClick(View v) {
        Notes notes = new Notes(MyNotesID,etTitle.getText().toString(),etText.getText().toString(), Tag.getText().toString());
        DBNotes dbNotes = new DBNotes(this);
        dbNotes.insert(notes.getTitle(), notes.getTexte(), notes.getTag());

        Intent intent=getIntent();
        intent.putExtra("Notes", notes);
        setResult(RESULT_OK,intent);
        finish();
    }
}
