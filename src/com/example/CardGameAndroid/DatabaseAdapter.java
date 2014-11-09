package com.example.CardGameAndroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Kristján on 22.9.2014.
 */
public class DatabaseAdapter {

    SQLiteDatabase db;
    DbHelper dbHelper;
    Context context;

    public DatabaseAdapter(Context c) {
        context = c;
    }

    public DatabaseAdapter openToRead() {
        dbHelper = new DbHelper( context );
        db = dbHelper.getReadableDatabase();
        return this;
    }

    public DatabaseAdapter openToWrite() {
        dbHelper = new DbHelper( context );
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    /*public long insertPuzzle( int pid, String solved, int min, int sec ) {
        String[] cols = DbHelper.TablePuzzleCols;
        ContentValues contentValues = new ContentValues();
        contentValues.put( cols[1], ((Integer)pid).toString() );
        contentValues.put( cols[2], solved );
        contentValues.put( cols[3], ((Integer)min).toString() );
        contentValues.put( cols[4], ((Integer)sec).toString() );
        openToWrite();
        long value = db.insert(DbHelper.TablePuzzle, null, contentValues );
        close();
        return value;
    }*/

    public long insertAction(int id, String nameFK, String action) {
        String[] cols = DbHelper.TableActionCols;
        ContentValues contentValues = new ContentValues();
        contentValues.put( cols[1], ((Integer)id).toString() );
        contentValues.put( cols[2], nameFK );
        contentValues.put( cols[3], action );
        openToWrite();
        long value = db.insert(DbHelper.TableAction, null, contentValues );
        close();
        return value;
    }
    public long insertArmor(int id, int coverage, int defense, int layer, String armorType, String unitNameFK) {
        String[] cols = DbHelper.TableArmorCols;
        ContentValues contentValues = new ContentValues();
        contentValues.put( cols[1], ((Integer)id).toString() );
        contentValues.put( cols[2], ((Integer)coverage).toString() );
        contentValues.put( cols[3], ((Integer)defense).toString() );
        contentValues.put( cols[4], ((Integer)layer).toString() );
        contentValues.put( cols[5], armorType );
        contentValues.put( cols[6], unitNameFK );
        openToWrite();
        long value = db.insert(DbHelper.TableArmor, null, contentValues );
        close();
        return value;
    }
    public long insertAttackType(int id, String nameFK, String attackType, int attack) {
        String[] cols = DbHelper.TableAttackTypeCols;
        ContentValues contentValues = new ContentValues();
        contentValues.put( cols[1], ((Integer)id).toString() );
        contentValues.put( cols[2], nameFK );
        contentValues.put( cols[3], attackType );
        contentValues.put( cols[4], ((Integer)attack).toString() );
        openToWrite();
        long value = db.insert(DbHelper.TableAttackType, null, contentValues );
        close();
        return value;
    }
    public long insertCardType(int id, String nameFK, String type) {
        String[] cols = DbHelper.TableCardTypeCols;
        ContentValues contentValues = new ContentValues();
        contentValues.put( cols[1], ((Integer)id).toString() );
        contentValues.put( cols[2], nameFK );
        contentValues.put( cols[3], type );
        openToWrite();
        long value = db.insert(DbHelper.TableCardType, null, contentValues );
        close();
        return value;
    }
    public long insertDamageReduction(int id, String nameFK, String type, int reduction) {
        String[] cols = DbHelper.TableDamageReductionCols;
        ContentValues contentValues = new ContentValues();
        contentValues.put( cols[1], ((Integer)id).toString() );
        contentValues.put( cols[2], nameFK );
        contentValues.put( cols[3], type );
        contentValues.put( cols[4], reduction );
        openToWrite();
        long value = db.insert(DbHelper.TableDamageReduction, null, contentValues );
        close();
        return value;
    }
    public long insertEffects(int id, String nameFK, String effect) {
        String[] cols = DbHelper.TableEffectsCols;
        ContentValues contentValues = new ContentValues();
        contentValues.put( cols[1], ((Integer)id).toString() );
        contentValues.put( cols[2], nameFK );
        contentValues.put( cols[3], effect );
        openToWrite();
        long value = db.insert(DbHelper.TableEffects, null, contentValues );
        close();
        return value;
    }
    public long insertStructureCards(String name, int cost, int buildTime, int hp, int mana, int attackRange,
                                     int moveRange, String moveType, String attackType, String damageCalculation) {
        String[] cols = DbHelper.TableStructureCardsCols;
        ContentValues contentValues = new ContentValues();
        contentValues.put( cols[1], name );
        contentValues.put( cols[2], ((Integer)cost).toString() );
        contentValues.put( cols[3], ((Integer)buildTime).toString() );
        contentValues.put( cols[4], ((Integer)hp).toString() );
        contentValues.put( cols[5], ((Integer)mana).toString() );
        contentValues.put( cols[6], ((Integer)attackRange).toString() );
        contentValues.put( cols[7], ((Integer)moveRange).toString() );
        contentValues.put( cols[8], moveType );
        contentValues.put( cols[9], attackType );
        contentValues.put( cols[10], damageCalculation );
        openToWrite();
        long value = db.insert(DbHelper.TableStructureCards, null, contentValues );
        close();
        return value;
    }
    public long insertUnitCards(String name, int cost, int hp, int attackRange, int moveRange, int weakSC,
                                String moveType, String attackType, String damageCalculation, int mana) {
        String[] cols = DbHelper.TableUnitCardsCols;
        ContentValues contentValues = new ContentValues();
        contentValues.put( cols[1], name );
        contentValues.put( cols[2], ((Integer)cost).toString() );
        contentValues.put( cols[3], ((Integer)hp).toString() );
        contentValues.put( cols[4], ((Integer)attackRange).toString() );
        contentValues.put( cols[5], ((Integer)moveRange).toString() );
        contentValues.put( cols[6], ((Integer)weakSC).toString() );
        contentValues.put( cols[7], moveType );
        contentValues.put( cols[8], attackType );
        contentValues.put( cols[9], damageCalculation );
        contentValues.put( cols[10], ((Integer)mana).toString() );
        openToWrite();
        long value = db.insert(DbHelper.TableUnitCards, null, contentValues );
        close();
        return value;
    }
    public long insertUsableCards(String name, boolean isItem, int cost) {
        String[] cols = DbHelper.TableUsableCardsCols;
        ContentValues contentValues = new ContentValues();
        contentValues.put( cols[1], name );
        if(isItem) contentValues.put( cols[2], "1" ); else contentValues.put( cols[2], "0" );
        contentValues.put( cols[3], ((Integer)cost).toString() );
        openToWrite();
        long value = db.insert(DbHelper.TableUsableCards, null, contentValues );
        close();
        return value;
    }

    /*public long updatePuzzle( int pid, String solved, int min, int sec ) {
        Cursor cursor = queryPuzzle(pid);
        cursor.moveToFirst();
        int currMin = cursor.getInt(3), currSec = cursor.getInt(4);
        if(currMin > min || (currMin == min && currSec > sec) || (currMin == 0 && currSec == 0)) {
            String[] cols = DbHelper.TablePuzzleCols;
            ContentValues contentValues = new ContentValues();
            contentValues.put(cols[1], ((Integer)pid).toString());
            contentValues.put(cols[2], solved);
            contentValues.put(cols[3], ((Integer)min).toString());
            contentValues.put(cols[4], ((Integer)sec).toString());
            openToWrite();
            long value = db.update(DbHelper.TablePuzzle, contentValues, cols[1] + "=" + pid, null);
            close();
            return value;
        }
        return -1;
    }

    public Cursor queryPuzzles() {
        openToRead();
        Cursor cursor = db.query( DbHelper.TablePuzzle,
                DbHelper.TablePuzzleCols, null, null, null, null, null);
        return cursor;
    }

    public Cursor queryPuzzle( int pid) {
        openToRead();
        String[] cols = DbHelper.TablePuzzleCols;
        Cursor cursor = db.query( DbHelper.TablePuzzle,
                cols, cols[1] + "=" + pid, null, null, null, null);
        return cursor;
    }*/

    public Cursor queryTable(String table) {
        openToRead();
        String[] cols;
        String tableSelected;
        if(table == "Action") {
            cols = DbHelper.TableActionCols;
            tableSelected = DbHelper.TableAction;
        }
        else if(table == "Armor") {
            cols = DbHelper.TableArmorCols;
            tableSelected = DbHelper.TableArmor;
        }
        else if(table == "AttackType") {
            cols = DbHelper.TableAttackTypeCols;
            tableSelected = DbHelper.TableAttackType;
        }
        else if(table == "CardType") {
            cols = DbHelper.TableCardTypeCols;
            tableSelected = DbHelper.TableCardType;
        }
        else if(table == "DamageReduction") {
            cols = DbHelper.TableDamageReductionCols;
            tableSelected = DbHelper.TableDamageReduction;
        }
        else if(table == "Effects") {
            cols = DbHelper.TableEffectsCols;
            tableSelected = DbHelper.TableEffects;
        }
        else if(table == "Structure") {
            cols = DbHelper.TableStructureCardsCols;
            tableSelected = DbHelper.TableStructureCards;
        }
        else if(table == "Unit") {
            cols = DbHelper.TableUnitCardsCols;
            tableSelected = DbHelper.TableUnitCards;
        }
        else if(table == "Usable") {
            cols = DbHelper.TableUsableCardsCols;
            tableSelected = DbHelper.TableUsableCards;
        } else return null;
        Cursor cursor = db.query( tableSelected,
                cols, null, null, null, null, null);
        return cursor;
    }

    public Cursor queryTableRow( String id, String table, String column) {
        openToRead();
        String[] cols;
        String tableSelected;
        if(table == "Action") {
            cols = DbHelper.TableActionCols;
            tableSelected = DbHelper.TableAction;
        }
        else if(table == "Armor") {
            cols = DbHelper.TableArmorCols;
            tableSelected = DbHelper.TableArmor;
        }
        else if(table == "AttackType") {
            cols = DbHelper.TableAttackTypeCols;
            tableSelected = DbHelper.TableAttackType;
        }
        else if(table == "CardType") {
            cols = DbHelper.TableCardTypeCols;
            tableSelected = DbHelper.TableCardType;
        }
        else if(table == "DamageReduction") {
            cols = DbHelper.TableDamageReductionCols;
            tableSelected = DbHelper.TableDamageReduction;
        }
        else if(table == "Effects") {
            cols = DbHelper.TableEffectsCols;
            tableSelected = DbHelper.TableEffects;
        }
        else if(table == "Structure") {
            cols = DbHelper.TableStructureCardsCols;
            tableSelected = DbHelper.TableStructureCards;
        }
        else if(table == "Unit") {
            cols = DbHelper.TableUnitCardsCols;
            tableSelected = DbHelper.TableUnitCards;
        }
        else if(table == "Usable") {
            cols = DbHelper.TableUsableCardsCols;
            tableSelected = DbHelper.TableUsableCards;
        } else return null;
        Cursor cursor = db.query(tableSelected,
                    cols, column + "='" + id + "'", null, null, null, null);
        return cursor;
    }
}
