package se.pikzel.assignment2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * @author Pontus Palmenäs
 */
public class Message {

    private Context context;

    public Message(Context context) {
        this.context = context;
    }

    public void showErrorMessage(String message) {
        showMessage("Error", message); // Todo: Extract resource
    }

    /**
     * Show a message dialog to the user.
     */
    public void showMessage(String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

}
