package com.example.CardGameAndroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kristján on 22.9.2014.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "CARDGAME_DB";
    public static final int DB_VERSION = 1;

    //public static final String TablePuzzle = "puzzle";
    //public static final String[] TablePuzzleCols = {"_id", "pid", "solved", "min", "sec"};

    public static final String TableAction = "Action";
    public static final String TableArmor = "Armor";
    public static final String TableAttackType = "AttackType";
    public static final String TableCardType = "CardType";
    public static final String TableDamageReduction = "DamageReduction";
    public static final String TableEffects = "Effects";
    public static final String TableStructureCards = "StructureCards";
    public static final String TableUnitCards = "UnitCards";
    public static final String TableUsableCards = "UsableCards";

    public static final String[] TableActionCols = {"_id", "id", "Name_FK", "Action"};
    public static final String[] TableArmorCols = {"_id", "id", "Coverage", "Defense", "Layer", "ArmorType", "UnitName_FK"};
    public static final String[] TableAttackTypeCols = {"_id", "id", "Name_FK", "AttackType", "Attack"};
    public static final String[] TableCardTypeCols = {"_id", "id", "Name_FK", "Type"};
    public static final String[] TableDamageReductionCols = {"_id", "id", "Name_FK", "Type", "Reduction"};
    public static final String[] TableEffectsCols = {"_id", "id", "Name_FK", "Effect"};
    public static final String[] TableStructureCardsCols = {
            "_id", "Name", "Cost", "BuildTime", "Hp", "Mana", "AttackRange", "MoveRange", "MoveType", "AttackType",
            "DamageCalculation"
    };
    public static final String[] TableUnitCardsCols = {
            "_id", "Name", "Cost", "Hp", "AttackRange", "MoveRange", "WeakSpotHitChance", "MoveType", "AttackType",
            "DamageCalculation", "Mana"
    };
    public static final String[] TableUsableCardsCols = {"_id", "Name", "Item", "Cost"};

    /*public static final String sqlCreateTablePuzzle =
            "CREATE TABLE puzzle(" +
                    " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " pid INTEGER NOT NULL," +
                    " solved TEXT," +
                    " min INTEGER," +
                    " sec INTEGER " +
                    ");";*/

    public static final String sqlCreateTableAction =
            "CREATE TABLE Action(" +
                    " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " id INTEGER NOT NULL," +
                    " Name_FK VARCHAR(40)," +
                    " Action VARCHAR(50)" +
                    ");";

    public static final String sqlCreateTableArmor =
            "CREATE TABLE Armor(" +
                    " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " id INTEGER NOT NULL," +
                    " Coverage INTEGER," +
                    " Defense INTEGER," +
                    " Layer INTEGER," +
                    " ArmorType VARCHAR(30)," +
                    " UnitName_FK VARCHAR(40)" +
                    ");";

    public static final String sqlCreateTableAttackType =
            "CREATE TABLE AttackType(" +
                    " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " id INTEGER NOT NULL," +
                    " Name_FK VARCHAR(40)," +
                    " AttackType VARCHAR(50)," +
                    " Attack INTEGER" +
                    ");";

    public static final String sqlCreateTableCardType =
            "CREATE TABLE CardType(" +
                    " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " id INTEGER NOT NULL," +
                    " Name_FK VARCHAR(40)," +
                    " Type VARCHAR(50)" +
                    ");";

    public static final String sqlCreateTableDamageReduction =
            "CREATE TABLE DamageReduction(" +
                    " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " id INTEGER NOT NULL," +
                    " Name_FK VARCHAR(40)," +
                    " Type VARCHAR(20)," +
                    " Reduction INTEGER" +
                    ");";

    public static final String sqlCreateTableEffects =
            "CREATE TABLE Effects(" +
                    " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " id INTEGER NOT NULL," +
                    " Name_FK VARCHAR(40)," +
                    " Effect VARCHAR(50)" +
                    ");";

    public static final String sqlCreateTableStructureCards =
            "CREATE TABLE StructureCards(" +
                    " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " Name VARCHAR(40) NOT NULL," +
                    " Cost INTEGER," +
                    " BuildTime INTEGER," +
                    " Hp INTEGER," +
                    " Mana INTEGER," +
                    " AttackRange INTEGER," +
                    " MoveRange INTEGER," +
                    " MoveType VARCHAR(50)," +
                    " AttackType VARCHAR(50)," +
                    " DamageCalculation VARCHAR(50)" +
                    ");";

    public static final String sqlCreateTableUnitCards =
            "CREATE TABLE UnitCards(" +
                    " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " Name VARCHAR(40) NOT NULL," +
                    " Cost INTEGER," +
                    " Hp INTEGER," +
                    " AttackRange INTEGER," +
                    " MoveRange INTEGER," +
                    " WeakSpotHitChance INTEGER," +
                    " MoveType VARCHAR(50)," +
                    " AttackType VARCHAR(50)," +
                    " DamageCalculation VARCHAR(50)," +
                    " Mana INTEGER" +
                    ");";

    public static final String sqlCreateTableUsableCards =
            "CREATE TABLE UsableCards(" +
                    " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " Name VARCHAR(40) NOT NULL," +
                    " Item BOOL," +
                    " Cost INTEGER" +
                    ");";

    //public static final String sqlDropTablePuzzle = "DROP TABLE IF EXISTS puzzle;";

    public static final String sqlDropTableAction = "DROP TABLE IF EXISTS Action;";
    public static final String sqlDropTableArmor = "DROP TABLE IF EXISTS Armor;";
    public static final String sqlDropTableAttackType = "DROP TABLE IF EXISTS AttackType;";
    public static final String sqlDropTableCardType = "DROP TABLE IF EXISTS CardType;";
    public static final String sqlDropTableDamageReduction = "DROP TABLE IF EXISTS DamageReduction;";
    public static final String sqlDropTableEffects = "DROP TABLE IF EXISTS Effects;";
    public static final String sqlDropTableStructureCards = "DROP TABLE IF EXISTS StructureCards;";
    public static final String sqlDropTableUnitCards = "DROP TABLE IF EXISTS UnitCards;";
    public static final String sqlDropTableUsableCards = "DROP TABLE IF EXISTS UsableCards;";

    public DbHelper( Context context ) { super(context, DB_NAME, null, DB_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreateTableAction); db.execSQL(sqlCreateTableArmor); db.execSQL(sqlCreateTableAttackType);
        db.execSQL(sqlCreateTableCardType); db.execSQL(sqlCreateTableDamageReduction); db.execSQL(sqlCreateTableEffects);
        db.execSQL(sqlCreateTableStructureCards); db.execSQL(sqlCreateTableUnitCards); db.execSQL(sqlCreateTableUsableCards);
        //db.execSQL(sqlCreateTablePuzzle);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL(sqlDropTablePuzzle);
        db.execSQL(sqlDropTableAction); db.execSQL(sqlDropTableArmor); db.execSQL(sqlDropTableAttackType);
        db.execSQL(sqlDropTableCardType); db.execSQL(sqlDropTableDamageReduction); db.execSQL(sqlDropTableEffects);
        db.execSQL(sqlDropTableStructureCards); db.execSQL(sqlDropTableUnitCards); db.execSQL(sqlDropTableUsableCards);
        onCreate( db );
    }
}
