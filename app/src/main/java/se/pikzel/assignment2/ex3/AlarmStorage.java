package se.pikzel.assignment2.ex3;

import android.content.Context;

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
public class AlarmStorage {
    private final String path = "alarms.bin";
    Context context;

    public AlarmStorage(Context context) {
        this.context = context;
    }

    public void save(List<Alarm> alarms) throws IOException {
        FileOutputStream fs = context.openFileOutput(path, Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fs);
        os.writeObject(alarms);
        os.close();
        fs.close();
    }

    @SuppressWarnings("unchecked") // For "Java type erasure"
    public List<Alarm> load() throws IOException, ClassNotFoundException {
        FileInputStream fs = context.openFileInput(path);
        ObjectInputStream os = new ObjectInputStream(fs);
        List<Alarm> alarms = (ArrayList<Alarm>) os.readObject();
        os.close();
        fs.close();
        return alarms;
    }

}
