package app.gamezoptest.gamezoptest.Utils;
/*
 * Created by Han
 *Vamos!
 *
 */

import android.os.Environment;

import java.io.File;

public class Constants {
    public static String pathToFile = "file:///" + Environment.getExternalStorageDirectory().toString() + File.separator;
    public static String host = "ben.gamezop.io";
    public static String port = "50051";
}
