package CardGame;

import android.database.Cursor;
import com.example.CardGameAndroid.DatabaseAdapter;
import com.example.CardGameAndroid.DbHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kristján Tryggvason
 * Date: 30.3.2014
 * Time: 15:05
 * To change this template use File | Settings | File Templates.
 */
public class CardFactory2 {
    private static int cardCount = 0;

    DatabaseAdapter databaseAdapter;
    CardFactory2(DatabaseAdapter DA) {
        databaseAdapter = DA;
    }

    public UnitCard getUnitCard(String unit) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        int cost, health, mana, attackRange, move, weakspotChance;
        String name, moveType, attackType, damageCalculation;

        Cursor cursor = databaseAdapter.queryTableRow(unit, "Unit", DbHelper.TableUnitCardsCols[1]);
        cursor.moveToFirst();

        name = unit; cost = Integer.getInteger(cursor.getString(2)); health = Integer.getInteger(cursor.getString(3));
        attackRange = Integer.getInteger(cursor.getString(4)); move = Integer.getInteger(cursor.getString(5));
        weakspotChance = Integer.getInteger(cursor.getString(6)); moveType = cursor.getString(7);
        attackType = cursor.getString(8); damageCalculation = cursor.getString(9);
        mana = Integer.getInteger(cursor.getString(10));

        List<Defence> defences = new ArrayList<Defence>();
        cursor = databaseAdapter.queryTableRow(unit, "Armor", DbHelper.TableArmorCols[6]);
        cursor.moveToFirst();
        while (true) {
            defences.add(new Defence(Integer.getInteger(cursor.getString(2)), Integer.getInteger(cursor.getString(3)),
                    Integer.getInteger(cursor.getString(4)), cursor.getString(5)));
            if(cursor.isLast()) break;
            cursor.moveToNext();
        }

        List<Damage> attack = new ArrayList<Damage>();
        cursor = databaseAdapter.queryTableRow(unit, "AttackType", DbHelper.TableAttackTypeCols[2]);
        cursor.moveToFirst();
        while (true) {
            List<String> type = new ArrayList<String>(); type.add(cursor.getString(3));
            attack.add(new Damage(type, Integer.getInteger(cursor.getString(4))));
            if(cursor.isLast()) break;
            cursor.moveToNext();
        }

        List<String> unitType = new ArrayList<String>();
        cursor = databaseAdapter.queryTableRow(unit, "CardType", DbHelper.TableCardTypeCols[2]);
        cursor.moveToFirst();
        while (true) {
            unitType.add(cursor.getString(3));
            if(cursor.isLast()) break;
            cursor.moveToNext();
        }

        List<DamageReduction> damageReductions = new ArrayList<DamageReduction>();
        cursor = databaseAdapter.queryTableRow(unit, "DamageReduction", DbHelper.TableDamageReductionCols[2]);
        cursor.moveToFirst();
        while (true) {
            damageReductions.add(new DamageReduction(cursor.getString(3), Integer.getInteger(cursor.getString(4))));
            if(cursor.isLast()) break;
            cursor.moveToNext();
        }

        List<Action> abilities = new ArrayList<Action>();
        cursor = databaseAdapter.queryTableRow(unit, "Action", DbHelper.TableActionCols[2]);
        cursor.moveToFirst();
        while (true) {
            abilities.add((Action) Class.forName(cursor.getString(3)).newInstance());
            if(cursor.isLast()) break;
            cursor.moveToNext();
        }

        List<Effect> effects = new ArrayList<Effect>();
        cursor = databaseAdapter.queryTableRow(unit, "Effects", DbHelper.TableEffectsCols[2]);
        cursor.moveToFirst();
        while (true) {
            effects.add((Effect) Class.forName(cursor.getString(3)).newInstance());
            if(cursor.isLast()) break;
            cursor.moveToNext();
        }

        UnitCard uCard = new UnitCard(cardCount, name, cost, health, mana, attackRange,
                move, weakspotChance, unitType, attack, defences, damageReductions,
                (Move)Class.forName("CardGame."+moveType).newInstance(), (Attack)Class.forName("CardGame."+attackType).newInstance(),
                (DamageTaken)Class.forName("CardGame."+damageCalculation).newInstance(), abilities, effects);
        cardCount++;
        return uCard;
    }

    public StructureCard getStructureCard(String structure) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        int cost, health, mana, attackRange, move, buildTime;
        String name, moveType, attackType, damageCalculation;

        Cursor cursor = databaseAdapter.queryTableRow(structure, "Structure", DbHelper.TableUnitCardsCols[1]);
        cursor.moveToFirst();
        name = structure; cost = Integer.getInteger(cursor.getString(2)); buildTime = Integer.getInteger(cursor.getString(3));
        health = Integer.getInteger(cursor.getString(4)); mana = Integer.getInteger(cursor.getString(5));
        attackRange = Integer.getInteger(cursor.getString(6)); move = Integer.getInteger(cursor.getString(7));
        moveType = cursor.getString(8); attackType = cursor.getString(9);
        damageCalculation = cursor.getString(10);

        List<Damage> attack = new ArrayList<Damage>();
        cursor = databaseAdapter.queryTableRow(structure, "AttackType", DbHelper.TableAttackTypeCols[2]);
        cursor.moveToFirst();
        while (true) {
            List<String> type = new ArrayList<String>(); type.add(cursor.getString(3));
            attack.add(new Damage(type, Integer.getInteger(cursor.getString(4))));
            if(cursor.isLast()) break;
            cursor.moveToNext();
        }

        List<String> structureType = new ArrayList<String>();
        cursor = databaseAdapter.queryTableRow(structure, "CardType", DbHelper.TableCardTypeCols[2]);
        cursor.moveToFirst();
        while (true) {
            structureType.add(cursor.getString(3));
            if(cursor.isLast()) break;
            cursor.moveToNext();
        }

        List<DamageReduction> damageReductions = new ArrayList<DamageReduction>();
        cursor = databaseAdapter.queryTableRow(structure, "DamageReduction", DbHelper.TableDamageReductionCols[2]);
        cursor.moveToFirst();
        while (true) {
            damageReductions.add(new DamageReduction(cursor.getString(3), Integer.getInteger(cursor.getString(4))));
            if(cursor.isLast()) break;
            cursor.moveToNext();
        }

        List<Action> abilities = new ArrayList<Action>();
        cursor = databaseAdapter.queryTableRow(structure, "Action", DbHelper.TableActionCols[2]);
        cursor.moveToFirst();
        while (true) {
            abilities.add((Action) Class.forName(cursor.getString(3)).newInstance());
            if(cursor.isLast()) break;
            cursor.moveToNext();
        }

        List<Effect> effects = new ArrayList<Effect>();
        cursor = databaseAdapter.queryTableRow(structure, "Effects", DbHelper.TableEffectsCols[2]);
        cursor.moveToFirst();
        while (true) {
            effects.add((Effect) Class.forName(cursor.getString(3)).newInstance());
            if(cursor.isLast()) break;
            cursor.moveToNext();
        }

        StructureCard sCard = new StructureCard(cardCount, name, cost, health, mana, attackRange, move, buildTime,
                structureType, attack, damageReductions, (Move)Class.forName("CardGame."+moveType).newInstance(),
                (Attack)Class.forName("CardGame."+attackType).newInstance(),
                (DamageTaken)Class.forName("CardGame."+damageCalculation).newInstance(), abilities, effects);
        cardCount++;
        return sCard;
    }

    public UsableCard getUsableCard(String name) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        int cost;
        String nameOfCard;
        boolean isItem;

        Cursor cursor = databaseAdapter.queryTableRow(name, "Usable", DbHelper.TableUsableCardsCols[1]);
        cursor.moveToFirst();
        nameOfCard = name; cost = Integer.getInteger(cursor.getString(3));
        if(cursor.getString(2) == "0") isItem = false; else isItem = true;

        List<String> cardType = new ArrayList<String>();
        cursor = databaseAdapter.queryTableRow(name, "CardType", DbHelper.TableCardTypeCols[2]);
        cursor.moveToFirst();
        while (true) {
            cardType.add(cursor.getString(3));
            if(cursor.isLast()) break;
            cursor.moveToNext();
        }

        List<Action> abilities = new ArrayList<Action>();
        cursor = databaseAdapter.queryTableRow(name, "Action", DbHelper.TableActionCols[2]);
        cursor.moveToFirst();
        while (true) {
            abilities.add((Action) Class.forName(cursor.getString(3)).newInstance());
            if(cursor.isLast()) break;
            cursor.moveToNext();
        }

        UsableCard usableCard;
        if(isItem) usableCard = new UsableCard(cardCount, nameOfCard, "Item", cost, cardType, abilities);
        else usableCard = new UsableCard(cardCount, nameOfCard, "Spell", cost, cardType, abilities);
        cardCount++;
        return usableCard;
    }
}
