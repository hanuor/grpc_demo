package app.gamezoptest.gamezoptest.Utils;
/*
 * Created by Han
 *Vamos!
 *
 */

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipTask {
    private File _zipFile;
    private InputStream _zipFileStream;
    private Context context;
    private static final String ROOT_LOCATION = "/sdcard";
    private static final String TAG = "UNZIPUTIL";

    public UnzipTask(Context context, File zipFile) {
        _zipFile = zipFile;
        this.context = context;

        _dirChecker("");
    }

    public UnzipTask(Context context, InputStream zipFile) {
        _zipFileStream = zipFile;
        this.context = context;

        _dirChecker("");
    }

    public String unzip(int position) {
        try {
            Log.i(TAG, "Starting to unzip");
            InputStream fin = _zipFileStream;
            if (fin == null) {
                fin = new FileInputStream(_zipFile);
            }
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            String storeName = null;
            while ((ze = zin.getNextEntry()) != null) {
                Log.v(TAG, "Unzipping " + ze.getName());
                if (storeName == null){
                    storeName = ze.getName();
                }
                if (ze.isDirectory()) {
                    _dirChecker(ROOT_LOCATION + "/gamezoptest/unzips/" + ze.getName()+ "/");

                } else {

                    File file = new File(ROOT_LOCATION + "/gamezoptest/unzips/", ze.getName());
                    if (file.exists ()) file.delete ();
                    file.getParentFile().mkdirs();
                    FileOutputStream fout = new FileOutputStream(file);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int count;

                    // reading and writing
                    while ((count = zin.read(buffer)) != -1) {
                        baos.write(buffer, 0, count);
                        byte[] bytes = baos.toByteArray();
                        fout.write(bytes);
                        baos.reset();
                    }

                    fout.close();
                    zin.closeEntry();

                }

            }
            zin.close();
            return storeName;
        } catch (Exception e) {
            Log.e(TAG, "Unzip Error", e);
        }
        return null;
    }

    private void _dirChecker(String dir) {
        File f = new File(dir);
        Log.i(TAG, "creating dir " + dir);

        if (dir.length() >= 0 && !f.isDirectory()) {
            f.mkdirs();
        }
    }
}