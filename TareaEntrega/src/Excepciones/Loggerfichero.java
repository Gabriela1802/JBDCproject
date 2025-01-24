package Excepciones;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Loggerfichero {
    private static Loggerfichero instance;
    private BufferedWriter writer;
    private SimpleDateFormat dateFormat;

    private Loggerfichero() throws IOException {
        String ruta = System.getProperty("user.dir") + "//log//log.txt";
        File logFile = new File(ruta);
        writer = new BufferedWriter(new FileWriter(ruta, true));
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    public static Loggerfichero getInstance() throws IOException {
        if (instance == null) {
            instance = new Loggerfichero();
        }
        return instance;
    }

    public void writeSmg(String msg) {
        try {
            String timestamp = dateFormat.format(new Date());
            writer.write("[" + timestamp + "] " + msg);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}