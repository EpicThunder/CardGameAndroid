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
    boolean failedToConnect;
    Connection connect = null;
    Statement stmt = null;
    private static int cardCount = 0;

    DatabaseAdapter databaseAdapter;
    CardFactory2(DatabaseAdapter DA) {
        databaseAdapter = DA;
        /*failedToConnect = false;
        try {
            Class.forName("org.sqlite.JDBC");
            connect = DriverManager.getConnection("jdbc:sqlite:CardGame.db");
            connect.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = connect.createStatement();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            failedToConnect = true;
        }*/
    }

    public UnitCard getUnitCard(String unit) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        //if(failedToConnect) return null;
        //ResultSet rs = stmt.executeQuery( "SELECT * FROM UnitCards;" );
        int cost, health, mana, attackRange, move, weakspotChance;
        //cost = health = mana = attackRange = move = weakspotChance = 0;
        String name = "", moveType = "", attackType = "", damageCalculation = "";

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
                    Integer.getInteger(cursor.getString(4)), cursor.getString(2)));
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

        /*while ( rs.next() ) {
            if(rs.getString("name").equals(unit)) {
                name = rs.getString("name");
                cost = rs.getInt("cost");
                health = rs.getInt("hp");
                mana = rs.getInt("mana");
                attackRange = rs.getInt("attackrange");
                move = rs.getInt("moverange");
                weakspotChance = rs.getInt("weakspothitchance");
                moveType = rs.getString("movetype");
                attackType = rs.getString("attacktype");
                damageCalculation = rs.getString("damagecalculation");
            }
        }

        rs = stmt.executeQuery( "SELECT * FROM Armor;" );
        //List<Defence> defences = new ArrayList<Defence>();
        while ( rs.next() ) {
            if(rs.getString("unitname_fk").equals(unit))
                defences.add(new Defence(rs.getInt("coverage"), rs.getInt("defense"),
                        rs.getInt("layer"), rs.getString("armortype")));
        }
        rs = stmt.executeQuery( "SELECT * FROM AttackType;" );
        //List<Damage> attack = new ArrayList<Damage>();
        while ( rs.next() ) {
            if(rs.getString("name_fk").equals(unit)) {
                List<String> type = new ArrayList<String>(); type.add(rs.getString("attacktype"));
                attack.add(new Damage(type, rs.getInt("attack")));
            }
        }
        rs = stmt.executeQuery( "SELECT * FROM CardType;" );
        while ( rs.next() ) {
            if(rs.getString("name_fk").equals(unit))
                unitType.add(rs.getString("type"));
        }
        rs = stmt.executeQuery( "SELECT * FROM DamageReduction;" );
        //List<DamageReduction> damageReductions = new ArrayList<DamageReduction>();
        while ( rs.next() ) {
            if(rs.getString("name_fk").equals(unit))
                damageReductions.add(new DamageReduction(rs.getString("type"),rs.getInt("reduction")));
        }
        rs = stmt.executeQuery( "SELECT * FROM Action;" );
        //List<Action> abilities = new ArrayList<Action>();
        while ( rs.next() ) {
            if(rs.getString("name_fk").equals(unit))
                abilities.add((Action) Class.forName(rs.getString("action")).newInstance());
        }
        rs = stmt.executeQuery( "SELECT * FROM Effects;" );
        //List<Effect> effects = new ArrayList<Effect>();
        while ( rs.next() ) {
            if(rs.getString("name_fk").equals(unit))
                effects.add((Effect) Class.forName(rs.getString("effect")).newInstance());
        }*/

        UnitCard uCard = new UnitCard(cardCount, name, cost, health, mana, attackRange,
                move, weakspotChance, unitType, attack, defences, damageReductions,
                (Move)Class.forName(moveType).newInstance(), (Attack)Class.forName(attackType).newInstance(),
                (DamageTaken)Class.forName(damageCalculation).newInstance(), abilities, effects);

        //rs.close();
        cardCount++;
        return uCard;
    }

    public StructureCard getStructureCard(String structure) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        int cost, health, mana, attackRange, move, buildTime;
        String name = "", moveType = "", attackType = "", damageCalculation = "";

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

        /*if(failedToConnect) return null;
        ResultSet rs = stmt.executeQuery( "SELECT * FROM StructureCards;" );
        String name = "", moveType = "", attackType = "", damageCalculation = "";
        int cost = 0, buildTime = 0, health = 0, mana = 0, attackRange = 0, moveRange = 0;
        while ( rs.next() ) {
            if(rs.getString("name").equals(structure)) {
                name = rs.getString("name");
                cost = rs.getInt("cost");
                buildTime = rs.getInt("buildtime");
                health = rs.getInt("hp");
                mana = rs.getInt("mana");
                attackRange = rs.getInt("attackrange");
                moveRange = rs.getInt("moverange");
                moveType = rs.getString("movetype");
                attackType = rs.getString("attacktype");
                damageCalculation = rs.getString("damagecalculation");
            }
        }

        rs = stmt.executeQuery( "SELECT * FROM AttackType;" );
        List<Damage> attack = new ArrayList<Damage>();
        while ( rs.next() ) {
            if(rs.getString("name_fk").equals(structure)) {
                List<String> type = new ArrayList<String>(); type.add(rs.getString("attacktype"));
                attack.add(new Damage(type, rs.getInt("attack")));
            }
        }
        rs = stmt.executeQuery( "SELECT * FROM CardType;" );
        List<String> structureType = new ArrayList<String>();
        while ( rs.next() ) {
            if(rs.getString("name_fk").equals(structure))
                structureType.add(rs.getString("type"));
        }
        rs = stmt.executeQuery( "SELECT * FROM DamageReduction;" );
        List<DamageReduction> damageReductions = new ArrayList<DamageReduction>();
        while ( rs.next() ) {
            if(rs.getString("name_fk").equals(structure))
                damageReductions.add(new DamageReduction(rs.getString("type"),rs.getInt("reduction")));
        }
        rs = stmt.executeQuery( "SELECT * FROM Action;" );
        List<Action> abilities = new ArrayList<Action>();
        while ( rs.next() ) {
            if(rs.getString("name_fk").equals(structure))
                abilities.add((Action) Class.forName(rs.getString("action")).newInstance());
        }
        rs = stmt.executeQuery( "SELECT * FROM Effects;" );
        List<Effect> effects = new ArrayList<Effect>();
        while ( rs.next() ) {
            if(rs.getString("name_fk").equals(structure))
                effects.add((Effect) Class.forName(rs.getString("effect")).newInstance());
        }*/

        StructureCard sCard = new StructureCard(cardCount, name, cost, health, mana, attackRange, move, buildTime,
                structureType, attack, damageReductions, (Move)Class.forName(moveType).newInstance(),
                (Attack)Class.forName(attackType).newInstance(),
                (DamageTaken)Class.forName(damageCalculation).newInstance(), abilities, effects);
        //rs.close();
        cardCount++;
        return sCard;
    }

    public UsableCard getUsableCard(String name) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        int cost;
        String nameOfCard = "";
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

        /*if(failedToConnect) return null;
        ResultSet rs = stmt.executeQuery( "SELECT * FROM UsableCards;" );
        String nameOfCard = "";
        int cost = 0;
        boolean isItem = true;
        while ( rs.next() ) {
            if(rs.getString("name").equals(name)) {
                nameOfCard = rs.getString("name");
                isItem = rs.getBoolean("item");
                cost = rs.getInt("cost");
            }
        }

        rs = stmt.executeQuery( "SELECT * FROM CardType;" );
        List<String> cardType = new ArrayList<String>();
        while ( rs.next() ) {
            if(rs.getString("name_fk").equals(name))
                cardType.add(rs.getString("type"));
        }
        rs = stmt.executeQuery( "SELECT * FROM Action;" );
        List<Action> abilities = new ArrayList<Action>();
        while ( rs.next() ) {
            if(rs.getString("name_fk").equals(name))
                abilities.add((Action) Class.forName(rs.getString("action")).newInstance());
        }*/
        UsableCard usableCard;
        if(isItem) usableCard = new UsableCard(cardCount, nameOfCard, "Item", cost, cardType, abilities);
        else usableCard = new UsableCard(cardCount, nameOfCard, "Spell", cost, cardType, abilities);
        //rs.close();
        cardCount++;
        return usableCard;
    }

    public boolean isConnected() { if(failedToConnect) return false; else return true; }

    public void closeFactory() throws SQLException {
        stmt.close();
        connect.close();
    }
}
