package se.pikzel.assignment2.ex3;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pontus Palmenäs
 */
public class AlarmFileHandler {
    private final String FILE_PATH = "alarms.bin";
    private final Context context;

    public AlarmFileHandler(Context context) {
        this.context = context;
    }

    public void save(List<Alarm> alarms) throws IOException {
        FileOutputStream fs = context.openFileOutput(FILE_PATH, Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fs);
        os.writeObject(alarms);
        os.close();
        fs.close();
    }

    @SuppressWarnings("unchecked") // For "Java type erasure"
    public List<Alarm> load() throws IOException, ClassNotFoundException {
        if (!context.getFileStreamPath(FILE_PATH).exists()) {
            save(new ArrayList<Alarm>());
        }

        FileInputStream fs = context.openFileInput(FILE_PATH);
        ObjectInputStream os = new ObjectInputStream(fs);
        List<Alarm> alarms = (ArrayList<Alarm>) os.readObject();
        os.close();
        fs.close();
        return alarms;
    }
}
