package se.pikzel.assignment2.ex1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import se.pikzel.assignment2.UIMessage;
import se.pikzel.assignment2.R;

/**
 * @author Pontus Palmenäs
 */
public class AddVisitActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_visit);
    }

    public void onClick(View view) {
        int year;
        EditText editYear = (EditText) findViewById(R.id.editYear);
        String country = ((EditText) findViewById(R.id.editCountry)).getText().toString();
        try {
            year = Integer.parseInt(editYear.getText().toString());
        } catch (NumberFormatException e) {
            new UIMessage(this).showErrorMessage("Please enter a year.");
            return;
        }
        if (!VisitsValidator.isInputValid(this, year, country)) {
            return;
        }

        Intent reply = new Intent();
        reply.putExtra(Visit.YEAR, year);
        reply.putExtra(Visit.COUNTRY, country);
        setResult(RESULT_OK, reply);
        finish();
    }

}
