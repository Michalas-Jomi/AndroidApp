package me.jomi.androidapp.old;

import android.provider.BaseColumns;

public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {



        //TODO Nie moze byc za kazdym razem insert, bo baza zacznie robic nowe rekordy i sie zjebie. - do zmiany, przy tworzeniu trzeba zrobic cos, co tylko raz wstawi wartosc do id 0.
        public static final String createSql_EntiresAndPlayer = "CREATE TABLE IF NOT EXISTS gracz (ID INT DEFAULT 0, steps INT DEFAULT 0, cycling_distance DOUBLE DEFAULT 0, run_distance DOUBLE DEFAULT 0); INSERT INTO gracz (ID) VALUES('0');";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS gracz";
    }

    /*
            public static final String createSql_Entries = "CREATE TABLE IF NOT EXISTS gracz (ID INT DEFAULT 0, steps INT DEFAULT 0, cycling_distance DOUBLE DEFAULT 0, run_distance DOUBLE DEFAULT 0)";
        public static final String updateSql = "INSERT INTO gracz (ID) VALUES('0'))";
     */
}
