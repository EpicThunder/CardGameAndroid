package com.example.CardGameAndroid;

import CardGame.DeckFactory;
import CardGame.Decks;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.sql.SQLException;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    Decks decks = new Decks();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void buttonClick(View view) {
        startActivity(new Intent(this, Match.class));
    }

    public void setDatabase() {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(this);
        databaseAdapter.insertAction(1, "Raider", "Pillage");
        databaseAdapter.insertAction(2, "Guard dog", "ContinuousAttack");
        databaseAdapter.insertAction(3, "War gator", "ContinuousAttack");
        databaseAdapter.insertAction(4, "War shark", "ContinuousAttack");
        databaseAdapter.insertAction(5, "Mage apprentice", "FireBall");
        databaseAdapter.insertAction(6, "Mage apprentice", "SkyAirBlast");
        databaseAdapter.insertAction(7, "Mage apprentice", "Quake");
        databaseAdapter.insertAction(8, "Outpost", "Outpost");
        databaseAdapter.insertAction(9, "Wooden bunker", "ProtectFromRange");
        databaseAdapter.insertAction(10, "Wooden ship", "Carry");
        databaseAdapter.insertAction(11, "Mana well", "ProvideMana");
        databaseAdapter.insertAction(12, "Tax depository", "ProvideGold");
        databaseAdapter.insertAction(13, "Scout tower", "IncreaseAttackRange");
        databaseAdapter.insertAction(14, "Armor", "NewArmor");
        databaseAdapter.insertAction(15, "Antidote", "Antidote");
        databaseAdapter.insertAction(16, "Steel jaw trap", "SteelJawTrap");
        databaseAdapter.insertAction(17, "Heal", "Heal");
        databaseAdapter.insertAction(18, "Burn down", "BurnDown");
        databaseAdapter.insertAction(19, "Fire arrow", "FireArrow");
        databaseAdapter.insertAction(20, "Slow", "Slow");
        databaseAdapter.insertAction(21, "Ground", "Ground");

        databaseAdapter.insertArmor(1, 40, 5, 1, "Armor", "Soldier");
        databaseAdapter.insertArmor(2, 55, 5, 1, "Armor", "Mercenary");
        databaseAdapter.insertArmor(3, 35, 4, 1, "Armor", "Guerrilla fighter");
        databaseAdapter.insertArmor(4, 37, 3, 1, "Armor", "Raider");
        databaseAdapter.insertArmor(5, 65, 4, 1, "Armor", "Archer");
        databaseAdapter.insertArmor(6, 60, 3, 1, "Armor", "Guard dog");
        databaseAdapter.insertArmor(7, 66, 6, 1, "Armor", "Guard");
        databaseAdapter.insertArmor(8, 20, 12, 2, "Shield", "Guard");
        databaseAdapter.insertArmor(9, 25, 3, 1, "Armor", "Scout");
        databaseAdapter.insertArmor(10, 40, 5, 1, "Armor", "Horseman soldier");
        databaseAdapter.insertArmor(11, 60, 5, 1, "Armor", "Battle eagle");
        databaseAdapter.insertArmor(12, 40, 5, 1, "Armor", "War shark");

        databaseAdapter.insertAttackType(1, "Soldier", "Cut", 10);
        databaseAdapter.insertAttackType(2, "Mercenary", "Cut", 14);
        databaseAdapter.insertAttackType(3, "Guerrilla fighter", "Cut", 15);
        databaseAdapter.insertAttackType(4, "Raider", "Cut", 13);
        databaseAdapter.insertAttackType(5, "Lesser beholder", "Cut", 4);
        databaseAdapter.insertAttackType(6, "Horseman soldier", "Cut", 15);
        databaseAdapter.insertAttackType(7, "Battle eagle", "Cut", 6);
        databaseAdapter.insertAttackType(8, "Skeleton", "Cut", 10);
        databaseAdapter.insertAttackType(9, "Large desert scorpion", "Piercing", 7);
        databaseAdapter.insertAttackType(10, "Archer", "Piercing", 12);
        databaseAdapter.insertAttackType(11, "Scout", "Piercing", 8);
        databaseAdapter.insertAttackType(12, "Guard dog", "Bite", 9);
        databaseAdapter.insertAttackType(13, "War gator", "Bite", 13);
        databaseAdapter.insertAttackType(14, "War shark", "Bite", 15);
        databaseAdapter.insertAttackType(15, "Guard", "Blunt", 9);
        databaseAdapter.insertAttackType(16, "Mage apprentice", "Blunt", 4);

        databaseAdapter.insertCardType(1, "Soldier", "Human");
        databaseAdapter.insertCardType(2, "Mercenary", "Human");
        databaseAdapter.insertCardType(3, "Guerrilla fighter", "Human");
        databaseAdapter.insertCardType(4, "Raider", "Human");
        databaseAdapter.insertCardType(5, "Archer", "Human");
        databaseAdapter.insertCardType(6, "Guard", "Human");
        databaseAdapter.insertCardType(7, "Scout", "Human");
        databaseAdapter.insertCardType(8, "Horseman soldier", "Human");
        databaseAdapter.insertCardType(9, "Mage apprentice", "Human");
        databaseAdapter.insertCardType(10, "Lesser beholder", "Monster");
        databaseAdapter.insertCardType(11, "Large desert scorpion", "Insect");
        databaseAdapter.insertCardType(12, "Guard dog", "Animal");
        databaseAdapter.insertCardType(13, "Battle eagle", "Animal");
        databaseAdapter.insertCardType(14, "Battle eagle", "Flying");
        databaseAdapter.insertCardType(15, "War gator", "Animal");
        databaseAdapter.insertCardType(16, "War shark", "Animal");
        databaseAdapter.insertCardType(17, "Skeleton", "Undead");
        databaseAdapter.insertCardType(18, "Tax depository", "Storage");

        databaseAdapter.insertDamageReduction(1, "Battle eagle", "Blunt", 20);
        databaseAdapter.insertDamageReduction(2, "Skeleton", "Cut", 50);
        databaseAdapter.insertDamageReduction(3, "Skeleton", "Piercing", 90);

        databaseAdapter.insertEffects(1, "Large desert scorpion", "Neurotoxin");
        databaseAdapter.insertEffects(2, "Guard dog", "InBattle");
        databaseAdapter.insertEffects(3, "War gator", "InBattle");
        databaseAdapter.insertEffects(4, "War shark", "InBattle");

        databaseAdapter.insertStructureCards("Outpost", 200, 2, 500, 0, 0, 0, "CanNotMove", "StandardAttack", "StandardDamageTaken");
        databaseAdapter.insertStructureCards("Wooden bunker", 50, 1, 400, 0, 0, 0, "CanNotMove", "StandardAttack", "StandardDamageTaken");
        databaseAdapter.insertStructureCards("Wooden ship", 150, 1, 300, 0, 0, 2, "MoveOnlyInWater", "StandardAttack", "StandardDamageTaken");
        databaseAdapter.insertStructureCards("Mana well", 200, 2, 400, 0, 0, 0, "CanNotMove", "StandardAttack", "StandardDamageTaken");
        databaseAdapter.insertStructureCards("Tax depository", 200, 2, 300, 0, 0, 0, "CanNotMove", "StandardAttack", "StandardDamageTaken");
        databaseAdapter.insertStructureCards("Scout tower", 100, 1, 300, 0, 0, 0, "CanNotMove", "StandardAttack", "StandardDamageTaken");

        databaseAdapter.insertUnitCards("Soldier", 100, 15, 0, 1, 10, "StandardMove", "StandardAttack", "StandardDamageTaken", 0);
        databaseAdapter.insertUnitCards("Mercenary", 130, 15, 0, 1, 10, "StandardMove", "StandardAttack", "StandardDamageTaken", 0);
        databaseAdapter.insertUnitCards("Guerrilla fighter", 130, 14, 0, 1, 10, "TerritoryTraverseMove", "StandardAttack", "StandardDamageTaken", 0);
        databaseAdapter.insertUnitCards("Raider", 130, 14, 0, 1, 10, "StandardMove", "StandardAttack", "StandardDamageTaken", 0);
        databaseAdapter.insertUnitCards("Lesser beholder", 70, 10, 0, 1, 20, "StandardMove", "StandardAttack", "StandardDamageTaken", 0);
        databaseAdapter.insertUnitCards("Large desert scorpion", 100, 8, 0, 1, 1, "StandardMove", "LargeDesertScorpionAttack", "LargeDesertScorpionDamageTaken", 0);
        databaseAdapter.insertUnitCards("Archer", 120, 12, 1, 1, 10, "StandardMove", "StandardAttack", "StandardDamageTaken", 0);
        databaseAdapter.insertUnitCards("Guard dog", 130, 12, 0, 2, 10, "StandardMove", "StandardAttack", "StandardDamageTaken", 0);
        databaseAdapter.insertUnitCards("Guard", 140, 16, 0, 1, 10, "StandardMove", "StandardAttack", "StandardDamageTaken", 0);
        databaseAdapter.insertUnitCards("Scout", 100, 12, 0, 2, 10, "StandardMove", "StandardAttack", "StandardDamageTaken", 0);
        databaseAdapter.insertUnitCards("Horseman soldier", 130, 15, 0, 2, 10, "StandardMove", "StandardAttack", "StandardDamageTaken", 0);
        databaseAdapter.insertUnitCards("Battle eagle", 120, 10, 0, 3, 5, "MoveOverAll", "StandardAttack", "StandardDamageTaken", 0);
        databaseAdapter.insertUnitCards("War gator", 130, 14, 0, 1, 7, "MoveOverAll", "StandardAttack", "StandardDamageTaken", 0);
        databaseAdapter.insertUnitCards("War shark", 120, 13, 0, 2, 3, "MoveOnlyInWater", "StandardAttack", "StandardDamageTaken", 0);
        databaseAdapter.insertUnitCards("Mage apprentice", 150, 10, 0, 1, 10, "StandardMove", "StandardAttack", "StandardDamageTaken", 20);
        databaseAdapter.insertUnitCards("Skeleton", 110, 12, 0, 1, 0, "StandardMove", "StandardAttack", "StandardDamageTaken", 0);

        databaseAdapter.insertUsableCards("Armor", true, 50);
        databaseAdapter.insertUsableCards("Antidote", true, 30);
        databaseAdapter.insertUsableCards("Steel jaw trap", true, 50);
        databaseAdapter.insertUsableCards("Heal", false, 10);
        databaseAdapter.insertUsableCards("Burn down", false, 30);
        databaseAdapter.insertUsableCards("Fire arrow", false, 10);
        databaseAdapter.insertUsableCards("Slow", false, 10);
        databaseAdapter.insertUsableCards("Ground", false, 20);
    }
}
