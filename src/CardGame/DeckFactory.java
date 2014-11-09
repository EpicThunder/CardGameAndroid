package CardGame;

import android.widget.TextView;
import com.example.CardGameAndroid.DatabaseAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kristján on 3.11.2014.
 */
public class DeckFactory {

    static CardFactory cardFactory;

    public static List<Card> getDeck1() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        cardFactory = new CardFactory();
        List<Card> deck = new ArrayList<Card>();
        for(int i=0; i<5; i++) deck.add(cardFactory.getUnitCard("Guard"));
        for(int i=0; i<5; i++) deck.add(cardFactory.getUnitCard("Skeleton"));
        for(int i=0; i<4; i++) deck.add(cardFactory.getUnitCard("Guard dog"));
        for(int i=0; i<4; i++) deck.add(cardFactory.getUnitCard("Archer"));
        for(int i=0; i<4; i++) deck.add(cardFactory.getUsableCard("Heal"));
        for(int i=0; i<3; i++) deck.add(cardFactory.getUnitCard("Mage apprentice"));
        for(int i=0; i<3; i++) deck.add(cardFactory.getUsableCard("Steel jaw trap"));
        for(int i=0; i<3; i++) deck.add(cardFactory.getUsableCard("Slow"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getUnitCard("Mercenary"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getUnitCard("Battle eagle"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getStructureCard("Wooden bunker"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getStructureCard("Tax depository"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getStructureCard("Scout tower"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getUsableCard("Armor"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getUsableCard("Antidote"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getUsableCard("Ground"));
        deck.add(cardFactory.getUnitCard("Soldier"));
        deck.add(cardFactory.getStructureCard("Mana well"));
        deck.add(cardFactory.getStructureCard("Outpost"));
        deck.add(cardFactory.getUsableCard("Burn down"));
        deck.add(cardFactory.getUsableCard("Fire arrow"));
        cardFactory.closeFactory();
        return deck;
    }

    public static List<Card> getDeck2() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        cardFactory = new CardFactory();
        List<Card> deck = new ArrayList<Card>();
        for(int i=0; i<5; i++) deck.add(cardFactory.getUnitCard("Mercenary"));
        for(int i=0; i<4; i++) deck.add(cardFactory.getUsableCard("Fire arrow"));
        for(int i=0; i<4; i++) deck.add(cardFactory.getUnitCard("Battle eagle"));
        for(int i=0; i<4; i++) deck.add(cardFactory.getUnitCard("Archer"));
        for(int i=0; i<3; i++) deck.add(cardFactory.getUnitCard("Guard dog"));
        for(int i=0; i<3; i++) deck.add(cardFactory.getUsableCard("Armor"));
        for(int i=0; i<3; i++) deck.add(cardFactory.getUsableCard("Burn down"));
        for(int i=0; i<3; i++) deck.add(cardFactory.getUnitCard("Horseman soldier"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getUnitCard("Skeleton"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getUsableCard("Heal"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getStructureCard("Outpost"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getStructureCard("Tax depository"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getStructureCard("Scout tower"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getUnitCard("Guerrilla fighter"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getUnitCard("Raider"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getUnitCard("Large desert scorpion"));
        deck.add(cardFactory.getUsableCard("Ground"));
        deck.add(cardFactory.getUsableCard("Antidote"));
        deck.add(cardFactory.getUnitCard("Soldier"));
        deck.add(cardFactory.getStructureCard("Mana well"));
        deck.add(cardFactory.getUnitCard("Guard"));
        deck.add(cardFactory.getStructureCard("Wooden bunker"));
        deck.add(cardFactory.getUnitCard("Mage apprentice"));
        cardFactory.closeFactory();
        return deck;
    }

    public static List<Card> getDeck1Second() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        List<Card> deck = new ArrayList<Card>();
        List<Defence> defences;
        List<Damage> attack;
        List<String> type;
        List<DamageReduction> damageReductions;
        List<Action> actions;
        List<Effect> effects;
        Move sMove = new StandardMove(), cMove = new CanNotMove(), oMove = new MoveOverAll();
        Attack sAttack = new StandardAttack();
        DamageTaken sDamageTaken = new StandardDamageTaken();
        for(int i=0; i<5; i++) {
            type = new ArrayList<String>(); type.add("Blunt");
            attack = new ArrayList<Damage>(); attack.add(new Damage(type,9)); type = new ArrayList<String>(); type.add("Human");
            defences = new ArrayList<Defence>(); defences.add(new Defence(66,6,1,"Armor")); defences.add(new Defence(20,12,2,"Shield"));
            actions = new ArrayList<Action>(); effects = new ArrayList<Effect>();
            deck.add(new UnitCard(i, "Guard", 140, 16, 0, 0, 1, 10, type, attack, defences, new ArrayList<DamageReduction>(), sMove, sAttack, sDamageTaken, actions, effects));
        }
        for(int i=0; i<5; i++) {
            type = new ArrayList<String>(); type.add("Cut");
            attack = new ArrayList<Damage>(); attack.add(new Damage(type,10)); type = new ArrayList<String>(); type.add("Undead");
            defences = new ArrayList<Defence>(); damageReductions = new ArrayList<DamageReduction>();
            damageReductions.add(new DamageReduction("Cut",50)); damageReductions.add(new DamageReduction("Piercing",90));
            actions = new ArrayList<Action>(); effects = new ArrayList<Effect>();
            deck.add(new UnitCard(5+i,"Skeleton",110,12,0,0,1,0,type,attack,defences,damageReductions,sMove,sAttack,sDamageTaken,actions,effects));
        }
        for(int i=0; i<4; i++) {
            type = new ArrayList<String>(); type.add("Bite");
            attack = new ArrayList<Damage>(); attack.add(new Damage(type,9)); type = new ArrayList<String>(); type.add("Animal");
            defences = new ArrayList<Defence>(); defences.add(new Defence(66,6,1,"Armor"));
            actions = new ArrayList<Action>(); actions.add(new ContinuousAttack());
            effects = new ArrayList<Effect>(); effects.add(new InBattle());
            deck.add(new UnitCard(10+i,"Guard dog",130,12,0,0,2,10,type,attack,defences,new ArrayList<DamageReduction>(),sMove,sAttack,sDamageTaken,actions,effects));
        }
        for(int i=0; i<4; i++) {
            type = new ArrayList<String>(); type.add("Piercing");
            attack = new ArrayList<Damage>(); attack.add(new Damage(type,12)); type = new ArrayList<String>(); type.add("Human");
            defences = new ArrayList<Defence>(); defences.add(new Defence(65,4,1,"Armor"));
            actions = new ArrayList<Action>(); effects = new ArrayList<Effect>();
            deck.add(new UnitCard(14+i,"Archer",120,12,0,1,1,10,type,attack,defences,new ArrayList<DamageReduction>(),sMove,sAttack,sDamageTaken,actions,effects));
        }
        for(int i=0; i<4; i++) {
            type = new ArrayList<String>(); actions = new ArrayList<Action>(); actions.add(new Heal());
            deck.add(new UsableCard(18+i,"Heal","Spell",10,type,actions));
        }
        for(int i=0; i<3; i++) {
            type = new ArrayList<String>(); type.add("Blunt");
            attack = new ArrayList<Damage>(); attack.add(new Damage(type,4)); type = new ArrayList<String>(); type.add("Human");
            defences = new ArrayList<Defence>();
            actions = new ArrayList<Action>(); actions.add(new FireBall()); actions.add(new SkyAirBlast());
            actions.add(new Quake()); effects = new ArrayList<Effect>();
            deck.add(new UnitCard(22+i,"Mage apprentice",150,10,0,0,1,10,type,attack,defences,new ArrayList<DamageReduction>(),sMove,sAttack,sDamageTaken,actions,effects));
        }
        for(int i=0; i<3; i++) {
            type = new ArrayList<String>(); actions = new ArrayList<Action>(); actions.add(new SteelJawTrap());
            deck.add(new UsableCard(25+i,"Steel jaw trap","Item",50,type,actions));
        }
        for(int i=0; i<3; i++) {
            type = new ArrayList<String>(); actions = new ArrayList<Action>(); actions.add(new Slow());
            deck.add(new UsableCard(28+i,"Slow","Spell",10,type,actions));
        }
        for(int i=0; i<2; i++) {
            type = new ArrayList<String>(); type.add("Cut");
            attack = new ArrayList<Damage>(); attack.add(new Damage(type,14)); type = new ArrayList<String>(); type.add("Human");
            defences = new ArrayList<Defence>(); defences.add(new Defence(55,5,1,"Armor"));
            actions = new ArrayList<Action>(); effects = new ArrayList<Effect>();
            deck.add(new UnitCard(31+i,"Mercenary",130,15,0,0,1,10,type,attack,defences,new ArrayList<DamageReduction>(),sMove,sAttack,sDamageTaken,actions,effects));
        }
        for(int i=0; i<2; i++) {
            type = new ArrayList<String>(); type.add("Cut");
            attack = new ArrayList<Damage>(); attack.add(new Damage(type,6)); type = new ArrayList<String>(); type.add("Animal");
            defences = new ArrayList<Defence>(); defences.add(new Defence(60,5,1,"Armor"));
            damageReductions = new ArrayList<DamageReduction>(); damageReductions.add(new DamageReduction("Blunt",20));
            actions = new ArrayList<Action>(); effects = new ArrayList<Effect>();
            deck.add(new UnitCard(33+i,"Battle eagle",120,10,0,0,3,5,type,attack,defences,damageReductions,oMove,sAttack,sDamageTaken,actions,effects));
        }
        for(int i=0; i<2; i++) {
            attack = new ArrayList<Damage>(); type = new ArrayList<String>();
            actions = new ArrayList<Action>(); actions.add(new ProtectFromRanged()); effects = new ArrayList<Effect>();
            deck.add(new StructureCard(35+i,"Wooden bunker",50,400,0,0,0,1,type,attack,new ArrayList<DamageReduction>(),cMove,sAttack,sDamageTaken,actions,effects));
        }
        for(int i=0; i<2; i++) {
            attack = new ArrayList<Damage>(); type = new ArrayList<String>(); type.add("Storage");
            actions = new ArrayList<Action>(); actions.add(new ProvideGold()); effects = new ArrayList<Effect>();
            deck.add(new StructureCard(37+i,"Tax depository",200,300,0,0,0,2,type,attack,new ArrayList<DamageReduction>(),cMove,sAttack,sDamageTaken,actions,effects));
        }
        for(int i=0; i<2; i++) {
            attack = new ArrayList<Damage>(); type = new ArrayList<String>();
            actions = new ArrayList<Action>(); actions.add(new IncreaseAttackRange()); effects = new ArrayList<Effect>();
            deck.add(new StructureCard(39+i,"Scout tower",100,300,0,0,0,1,type,attack,new ArrayList<DamageReduction>(),cMove,sAttack,sDamageTaken,actions,effects));
        }
        for(int i=0; i<2; i++) {
            type = new ArrayList<String>(); actions = new ArrayList<Action>(); actions.add(new NewArmor());
            deck.add(new UsableCard(41+i,"Armor","Item",50,type,actions));
        }
        for(int i=0; i<2; i++) {
            type = new ArrayList<String>(); actions = new ArrayList<Action>(); actions.add(new Antidote());
            deck.add(new UsableCard(43+i,"Antidote","Item",30,type,actions));
        }
        for(int i=0; i<2; i++) {
            type = new ArrayList<String>(); actions = new ArrayList<Action>(); actions.add(new Ground());
            deck.add(new UsableCard(45+i,"Ground","Spell",20,type,actions));
        }
        type = new ArrayList<String>(); type.add("Cut");
        attack = new ArrayList<Damage>(); attack.add(new Damage(type,10)); type = new ArrayList<String>(); type.add("Human");
        defences = new ArrayList<Defence>(); defences.add(new Defence(40,5,1,"Armor"));
        actions = new ArrayList<Action>(); effects = new ArrayList<Effect>();
        deck.add(new UnitCard(47,"Soldier",100,15,0,0,1,10,type,attack,defences,new ArrayList<DamageReduction>(),sMove,sAttack,sDamageTaken,actions,effects));

        attack = new ArrayList<Damage>(); type = new ArrayList<String>();
        actions = new ArrayList<Action>(); actions.add(new ProvideMana()); effects = new ArrayList<Effect>();
        deck.add(new StructureCard(48,"Mana well",200,400,0,0,0,2,type,attack,new ArrayList<DamageReduction>(),cMove,sAttack,sDamageTaken,actions,effects));

        attack = new ArrayList<Damage>(); type = new ArrayList<String>();
        actions = new ArrayList<Action>(); actions.add(new Outpost()); effects = new ArrayList<Effect>();
        deck.add(new StructureCard(49,"Outpost",200,500,0,0,0,2,type,attack,new ArrayList<DamageReduction>(),cMove,sAttack,sDamageTaken,actions,effects));

        type = new ArrayList<String>(); actions = new ArrayList<Action>(); actions.add(new BurnDown());
        deck.add(new UsableCard(50,"Burn down","Spell",30,type,actions));

        type = new ArrayList<String>(); actions = new ArrayList<Action>(); actions.add(new FireArrow());
        deck.add(new UsableCard(51,"Fire arrow","Spell",10,type,actions));
        return deck;
    }

    public static List<Card> getDeck2Second() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        List<Card> deck = new ArrayList<Card>();
        List<Defence> defences;
        List<Damage> attack;
        List<String> type;
        List<DamageReduction> damageReductions;
        List<Action> actions;
        List<Effect> effects;
        Move sMove = new StandardMove(), cMove = new CanNotMove(), oMove = new MoveOverAll(), tMove = new TerritoryTraverseMove();
        Attack sAttack = new StandardAttack(), lAttack = new LargeDesertScorpionAttack();
        DamageTaken sDamageTaken = new StandardDamageTaken(), lDamageTaken = new LargeDesertScorpionDamageTaken();
        for(int i=0; i<5; i++) {
            type = new ArrayList<String>(); type.add("Cut");
            attack = new ArrayList<Damage>(); attack.add(new Damage(type,14)); type = new ArrayList<String>(); type.add("Human");
            defences = new ArrayList<Defence>(); defences.add(new Defence(55,5,1,"Armor"));
            actions = new ArrayList<Action>(); effects = new ArrayList<Effect>();
            deck.add(new UnitCard(52+i,"Mercenary",130,15,0,0,1,10,type,attack,defences,new ArrayList<DamageReduction>(),sMove,sAttack,sDamageTaken,actions,effects));
        }
        for(int i=0; i<4; i++) {
            type = new ArrayList<String>();
            actions = new ArrayList<Action>(); actions.add(new FireArrow());
            deck.add(new UsableCard(57+i,"Fire arrow","Spell",10,type,actions));
        }
        for(int i=0; i<4; i++) {
            type = new ArrayList<String>(); type.add("Cut");
            attack = new ArrayList<Damage>(); attack.add(new Damage(type,6)); type = new ArrayList<String>(); type.add("Animal");
            defences = new ArrayList<Defence>(); defences.add(new Defence(60,5,1,"Armor"));
            damageReductions = new ArrayList<DamageReduction>(); damageReductions.add(new DamageReduction("Blunt",20));
            actions = new ArrayList<Action>(); effects = new ArrayList<Effect>();
            deck.add(new UnitCard(61+i,"Battle eagle",120,10,0,0,3,5,type,attack,defences,damageReductions,oMove,sAttack,sDamageTaken,actions,effects));
        }
        for(int i=0; i<4; i++) {
            type = new ArrayList<String>(); type.add("Piercing");
            attack = new ArrayList<Damage>(); attack.add(new Damage(type,12)); type = new ArrayList<String>(); type.add("Human");
            defences = new ArrayList<Defence>(); defences.add(new Defence(65,4,1,"Armor"));
            actions = new ArrayList<Action>(); effects = new ArrayList<Effect>();
            deck.add(new UnitCard(65+i,"Archer",120,12,0,1,1,10,type,attack,defences,new ArrayList<DamageReduction>(),sMove,sAttack,sDamageTaken,actions,effects));
        }
        for(int i=0; i<3; i++) {
            type = new ArrayList<String>(); type.add("Bite");
            attack = new ArrayList<Damage>(); attack.add(new Damage(type,9)); type = new ArrayList<String>(); type.add("Animal");
            defences = new ArrayList<Defence>(); defences.add(new Defence(66,6,1,"Armor"));
            actions = new ArrayList<Action>(); actions.add(new ContinuousAttack());
            effects = new ArrayList<Effect>(); effects.add(new InBattle());
            deck.add(new UnitCard(69+i,"Guard dog",130,12,0,0,2,10,type,attack,defences,new ArrayList<DamageReduction>(),sMove,sAttack,sDamageTaken,actions,effects));
        }
        for(int i=0; i<3; i++) {
            type = new ArrayList<String>(); actions = new ArrayList<Action>(); actions.add(new NewArmor());
            deck.add(new UsableCard(72+i,"Armor","Item",50,type,actions));
        }
        for(int i=0; i<3; i++) {
            type = new ArrayList<String>(); actions = new ArrayList<Action>(); actions.add(new BurnDown());
            deck.add(new UsableCard(75,"Burn down","Spell",30,type,actions));
        }
        for(int i=0; i<3; i++) {
            type = new ArrayList<String>(); type.add("Cut");
            attack = new ArrayList<Damage>(); attack.add(new Damage(type,15)); type = new ArrayList<String>(); type.add("Human");
            defences = new ArrayList<Defence>(); defences.add(new Defence(40,5,1,"Armor"));
            actions = new ArrayList<Action>(); effects = new ArrayList<Effect>();
            deck.add(new UnitCard(78+i,"Horseman soldier",130,15,0,0,2,10,type,attack,defences,new ArrayList<DamageReduction>(),sMove,sAttack,sDamageTaken,actions,effects));
        }
        for(int i=0; i<2; i++) {
            type = new ArrayList<String>(); type.add("Cut");
            attack = new ArrayList<Damage>(); attack.add(new Damage(type,10)); type = new ArrayList<String>(); type.add("Undead");
            defences = new ArrayList<Defence>(); damageReductions = new ArrayList<DamageReduction>();
            damageReductions.add(new DamageReduction("Cut",50)); damageReductions.add(new DamageReduction("Piercing",90));
            actions = new ArrayList<Action>(); effects = new ArrayList<Effect>();
            deck.add(new UnitCard(81+i,"Skeleton",110,12,0,0,1,0,type,attack,defences,damageReductions,sMove,sAttack,sDamageTaken,actions,effects));
        }
        for(int i=0; i<2; i++) {
            type = new ArrayList<String>();
            actions = new ArrayList<Action>(); actions.add(new Heal());
            deck.add(new UsableCard(83+i,"Heal","Spell",10,type,actions));
        }
        for(int i=0; i<2; i++) {
            attack = new ArrayList<Damage>(); type = new ArrayList<String>();
            actions = new ArrayList<Action>(); actions.add(new Outpost());
            effects = new ArrayList<Effect>();
            deck.add(new StructureCard(85,"Outpost",200,500,0,0,0,2,type,attack,new ArrayList<DamageReduction>(),cMove,sAttack,sDamageTaken,actions,effects));
        }
        for(int i=0; i<2; i++) {
            attack = new ArrayList<Damage>(); type = new ArrayList<String>(); type.add("Storage");
            actions = new ArrayList<Action>(); actions.add(new ProvideGold());
            effects = new ArrayList<Effect>();
            deck.add(new StructureCard(87+i,"Tax depository",200,300,0,0,0,2,type,attack,new ArrayList<DamageReduction>(),cMove,sAttack,sDamageTaken,actions,effects));
        }
        for(int i=0; i<2; i++) {
            attack = new ArrayList<Damage>(); type = new ArrayList<String>();
            actions = new ArrayList<Action>(); actions.add(new IncreaseAttackRange());
            effects = new ArrayList<Effect>();
            deck.add(new StructureCard(89+i,"Scout tower",100,300,0,0,0,1,type,attack,new ArrayList<DamageReduction>(),cMove,sAttack,sDamageTaken,actions,effects));
        }
        for(int i=0; i<2; i++) {
            type = new ArrayList<String>(); type.add("Cut");
            attack = new ArrayList<Damage>(); attack.add(new Damage(type,15)); type = new ArrayList<String>(); type.add("Human");
            defences = new ArrayList<Defence>(); defences.add(new Defence(35,4,1,"Armor"));
            actions = new ArrayList<Action>(); effects = new ArrayList<Effect>();
            deck.add(new UnitCard(91+i,"Guerrilla fighter",130,14,0,0,1,10,type,attack,defences,new ArrayList<DamageReduction>(),tMove,sAttack,sDamageTaken,actions,effects));
        }
        for(int i=0; i<2; i++) {
            type = new ArrayList<String>(); type.add("Cut");
            attack = new ArrayList<Damage>(); attack.add(new Damage(type,13)); type = new ArrayList<String>(); type.add("Human");
            defences = new ArrayList<Defence>(); defences.add(new Defence(37,3,1,"Armor"));
            actions = new ArrayList<Action>(); actions.add(new Pillage()); effects = new ArrayList<Effect>();
            deck.add(new UnitCard(93+i,"Raider",130,14,0,0,1,10,type,attack,defences,new ArrayList<DamageReduction>(),sMove,sAttack,sDamageTaken,actions,effects));
        }
        for(int i=0; i<2; i++) {
            type = new ArrayList<String>(); type.add("Piercing");
            attack = new ArrayList<Damage>(); attack.add(new Damage(type,7)); type = new ArrayList<String>(); type.add("Insect");
            defences = new ArrayList<Defence>(); actions = new ArrayList<Action>();
            effects = new ArrayList<Effect>(); effects.add(new Neurotoxin());
            deck.add(new UnitCard(95+i,"Large desert scorpion",100,8,0,0,1,1,type,attack,defences,new ArrayList<DamageReduction>(),sMove,lAttack,lDamageTaken,actions,effects));
        }
        type = new ArrayList<String>(); actions = new ArrayList<Action>(); actions.add(new Ground());
        deck.add(new UsableCard(97,"Ground","Spell",20,type,actions));

        type = new ArrayList<String>(); actions = new ArrayList<Action>(); actions.add(new Antidote());
        deck.add(new UsableCard(98,"Antidote","Item",30,type,actions));

        type = new ArrayList<String>(); type.add("Cut");
        attack = new ArrayList<Damage>(); attack.add(new Damage(type,10)); type = new ArrayList<String>(); type.add("Human");
        defences = new ArrayList<Defence>(); defences.add(new Defence(40,5,1,"Armor"));
        actions = new ArrayList<Action>(); effects = new ArrayList<Effect>();
        deck.add(new UnitCard(99,"Soldier",100,15,0,0,1,10,type,attack,defences,new ArrayList<DamageReduction>(),sMove,sAttack,sDamageTaken,actions,effects));

        attack = new ArrayList<Damage>(); type = new ArrayList<String>();
        actions = new ArrayList<Action>(); actions.add(new ProvideMana()); effects = new ArrayList<Effect>();
        deck.add(new StructureCard(100,"Mana well",200,400,0,0,0,2,type,attack,new ArrayList<DamageReduction>(),cMove,sAttack,sDamageTaken,actions,effects));

        type = new ArrayList<String>(); type.add("Blunt");
        attack = new ArrayList<Damage>(); attack.add(new Damage(type,9)); type = new ArrayList<String>(); type.add("Human");
        defences = new ArrayList<Defence>(); defences.add(new Defence(66,6,1,"Armor")); defences.add(new Defence(20,12,2,"Shield"));
        actions = new ArrayList<Action>(); effects = new ArrayList<Effect>();
        deck.add(new UnitCard(101,"Guard",140,16,0,0,1,10,type,attack,defences,new ArrayList<DamageReduction>(),sMove,sAttack,sDamageTaken,actions,effects));

        attack = new ArrayList<Damage>(); type = new ArrayList<String>();
        actions = new ArrayList<Action>(); actions.add(new ProtectFromRanged()); effects = new ArrayList<Effect>();
        deck.add(new StructureCard(102,"Wooden bunker",50,400,0,0,0,1,type,attack,new ArrayList<DamageReduction>(),cMove,sAttack,sDamageTaken,actions,effects));

        type = new ArrayList<String>(); type.add("Blunt");
        attack = new ArrayList<Damage>(); attack.add(new Damage(type,4)); type = new ArrayList<String>(); type.add("Human");
        defences = new ArrayList<Defence>();
        actions = new ArrayList<Action>(); actions.add(new FireBall()); actions.add(new SkyAirBlast());
        actions.add(new Quake()); effects = new ArrayList<Effect>();
        deck.add(new UnitCard(103,"Mage apprentice",150,10,0,0,1,10,type,attack,defences,new ArrayList<DamageReduction>(),sMove,sAttack,sDamageTaken,actions,effects));
        return deck;
    }
}
