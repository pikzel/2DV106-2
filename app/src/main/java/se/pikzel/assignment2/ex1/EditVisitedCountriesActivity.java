package se.pikzel.assignment2.ex1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import se.pikzel.assignment2.Message;
import se.pikzel.assignment2.R;

public class EditVisitedCountriesActivity extends Activity {
    private long id;
    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_visited_countries);
        this.id = getIntent().getLongExtra(Visit.ID, -1);
        this.position = getIntent().getIntExtra(Visit.POSITION, -1);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEditCountrySave:
                onClickSave();
                break;
            case R.id.btnEditCountryCancel:
                setResult(RESULT_CANCELED);
                break;
        }
        finish();
    }

    private void onClickSave() {
        // Todo: Refactor and extract this to a common method to remove duplicate with AddCountryActivity
        int year;
        EditText editYear = (EditText) findViewById(R.id.editYear);
        String country = ((EditText) findViewById(R.id.editCountry)).getText().toString();
        try {
            year = Integer.parseInt(editYear.getText().toString());
        } catch (NumberFormatException e) {
            new Message(this).showErrorMessage("Please enter a year.");
            return;
        }
        if (!VisitedCountriesValidator.isInputValid(this, year, country)) {
            return;
        }

        Intent reply = new Intent();
        reply.putExtra(Visit.ID, id);
        reply.putExtra(Visit.YEAR, year);
        reply.putExtra(Visit.COUNTRY, country);
        reply.putExtra(Visit.POSITION, position);
        setResult(RESULT_OK, reply);
    }
}
