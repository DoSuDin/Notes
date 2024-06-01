package com.example.fna;

import android.content.Intent;
import android.view.View;

public class UpdateActivity extends AddActivity{

    @Override
    public void onClick(View v) {
        Notes notes = new Notes(MyNotesID,etTitle.getText().toString(),etText.getText().toString(), Tag.getText().toString());
        DBNotes dbNotes = new DBNotes(this);
        dbNotes.update(notes);

        Intent intent=getIntent();
        intent.putExtra("Notes", notes);
        setResult(RESULT_OK,intent);
        finish();
    }

}
