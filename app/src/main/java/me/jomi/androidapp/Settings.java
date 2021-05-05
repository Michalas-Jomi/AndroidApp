package me.jomi.androidapp;

import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Tutaj będą przechowywane dane uzytkownika, które nie muszą być koniecznie w bazie
 */
public class Settings {
    private static int music;
    private static int sfx;


    public static int getMusic() {
        return music;
    }
    public static void setMusic(int music) {
        if (Settings.music != music) {
            Settings.music = music;
            save();
        }
    }

    public static int getSfx() {
        return sfx;
    }
    public static void setSfx(int sfx) {
        if (Settings.sfx != sfx) {
            Settings.sfx = sfx;
            save();
        }
    }


    static long lastSave = 0L;
    static boolean needSave = false;
    private static File getDataFile() {
        File file = new File(MainActivity.instance.getFilesDir(), "settings");
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return file;
    }
    public static void save() {
        if (lastSave + 20_000L > System.currentTimeMillis()) {
            needSave = true;
        } else {
            saveNow();
        }
    }
    static void saveNow() {
        lastSave = System.currentTimeMillis();
        needSave = false;

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (needSave)
                    save();
            }
        }, 21_000L);


        System.out.println("Zapisywanie ustawień użytkownika");
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(getDataFile()));
            out.writeInt(music);
            out.writeInt(sfx);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void load() {
        System.out.println("Wczytywanie ustawień użytkownika");
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(getDataFile()));
            music = in.readInt();
            sfx = in.readInt();
            in.close();
        } catch (IOException e) {
            System.err.println("Nie udało się wczytać ustawień");
            e.printStackTrace();
        }

    }
}
