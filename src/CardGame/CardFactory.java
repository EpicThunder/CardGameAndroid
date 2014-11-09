package CardGame;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kristján Tryggvason
 * Date: 30.3.2014
 * Time: 15:05
 * To change this template use File | Settings | File Templates.
 */
public class CardFactory {
    boolean failedToConnect;
    Connection connect = null;
    Statement stmt = null;
    private static int cardCount = 0;
    CardFactory() {
        failedToConnect = false;
        try {
            Class.forName("org.sqlite.JDBC");
            connect = DriverManager.getConnection("jdbc:sqlite:CardGame.db");
            connect.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = connect.createStatement();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            failedToConnect = true;
        }
    }

    public UnitCard getUnitCard(String unit) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        if(failedToConnect) return null;
        ResultSet rs = stmt.executeQuery( "SELECT * FROM UnitCards;" );
        int cost, health, mana, attackRange, move, weakspotChance;
        cost = health = mana = attackRange = move = weakspotChance = 0;
        String name = "", moveType = "", attackType = "", damageCalculation = "";
        while ( rs.next() ) {
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
        List<Defence> defences = new ArrayList<Defence>();
        while ( rs.next() ) {
            if(rs.getString("unitname_fk").equals(unit))
                defences.add(new Defence(rs.getInt("coverage"), rs.getInt("defense"),
                        rs.getInt("layer"), rs.getString("armortype")));
        }
        rs = stmt.executeQuery( "SELECT * FROM AttackType;" );
        List<Damage> attack = new ArrayList<Damage>();
        while ( rs.next() ) {
            if(rs.getString("name_fk").equals(unit)) {
                List<String> type = new ArrayList<String>(); type.add(rs.getString("attacktype"));
                attack.add(new Damage(type, rs.getInt("attack")));
            }
        }
        rs = stmt.executeQuery( "SELECT * FROM CardType;" );
        List<String> unitType = new ArrayList<String>();
        while ( rs.next() ) {
            if(rs.getString("name_fk").equals(unit))
                unitType.add(rs.getString("type"));
        }
        rs = stmt.executeQuery( "SELECT * FROM DamageReduction;" );
        List<DamageReduction> damageReductions = new ArrayList<DamageReduction>();
        while ( rs.next() ) {
            if(rs.getString("name_fk").equals(unit))
                damageReductions.add(new DamageReduction(rs.getString("type"),rs.getInt("reduction")));
        }
        rs = stmt.executeQuery( "SELECT * FROM Action;" );
        List<Action> abilities = new ArrayList<Action>();
        while ( rs.next() ) {
            if(rs.getString("name_fk").equals(unit))
                abilities.add((Action) Class.forName(rs.getString("action")).newInstance());
        }
        rs = stmt.executeQuery( "SELECT * FROM Effects;" );
        List<Effect> effects = new ArrayList<Effect>();
        while ( rs.next() ) {
            if(rs.getString("name_fk").equals(unit))
                effects.add((Effect) Class.forName(rs.getString("effect")).newInstance());
        }

        UnitCard uCard = new UnitCard(cardCount, name, cost, health, mana, attackRange,
                move, weakspotChance, unitType, attack, defences, damageReductions,
                (Move)Class.forName(moveType).newInstance(), (Attack)Class.forName(attackType).newInstance(),
                (DamageTaken)Class.forName(damageCalculation).newInstance(), abilities, effects);

        rs.close();
        cardCount++;
        return uCard;
    }

    public StructureCard getStructureCard(String structure) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        if(failedToConnect) return null;
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
        }

        StructureCard sCard = new StructureCard(cardCount, name, cost, health, mana, attackRange, moveRange, buildTime,
                structureType, attack, damageReductions, (Move)Class.forName(moveType).newInstance(),
                (Attack)Class.forName(attackType).newInstance(),
                (DamageTaken)Class.forName(damageCalculation).newInstance(), abilities, effects);
        rs.close();
        cardCount++;
        return sCard;
    }

    public UsableCard getUsableCard(String name) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        if(failedToConnect) return null;
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
        }
        UsableCard usableCard;
        if(isItem) usableCard = new UsableCard(cardCount, nameOfCard, "Item", cost, cardType, abilities);
        else usableCard = new UsableCard(cardCount, nameOfCard, "Spell", cost, cardType, abilities);
        rs.close();
        cardCount++;
        return usableCard;
    }

    public boolean isConnected() { if(failedToConnect) return false; else return true; }

    public void closeFactory() throws SQLException {
        if(!failedToConnect) {
            stmt.close();
            connect.close();
        }
    }
}
