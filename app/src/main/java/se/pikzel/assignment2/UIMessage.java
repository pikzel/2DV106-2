package se.pikzel.assignment2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * @author Pontus Palmenäs
 */
public class UIMessage {

    private Context context;

    public UIMessage(Context context) {
        this.context = context;
    }

    public void showErrorMessage(String message) {
        showMessage("Error", message);
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
