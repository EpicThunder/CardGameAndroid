package CardGame;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Kristján Tryggvason
 * Date: 18.3.2014
 * Time: 00:36
 * To change this template use File | Settings | File Templates.
 */
public interface Attack {
    List<Damage> whatIsYourAttack(CardThatCanBattle targetCard, String targetTerrain, CardThatCanBattle attackerCard, String attackerTerrain);
    Map<List<Damage>, Float> possibleAttacks(CardThatCanBattle targetCard, CardThatCanBattle attackerCard);
}

abstract class AbstractAttack {

    List<Damage> damageBonus(CardThatCanBattle targetCard, String targetTerrain,
                             CardThatCanBattle attackerCard, String attackerTerrain, List<Damage> attack) {
        return attack;
    }
    Map<List<Damage>, Float> possibleDamageBonus(CardThatCanBattle targetCard, CardThatCanBattle attackerCard, List<Damage> attack) {
        Map<List<Damage>, Float> attacks = new HashMap<List<Damage>, Float>();
        attacks.put(Support.copyDamages(attack), (float)1);
        return attacks;
    }
    List<Damage> damageMultiply(CardThatCanBattle targetCard, String targetTerrain,
                                CardThatCanBattle attackerCard, String attackerTerrain, List<Damage> attack) {
        return attack;
    }
    Map<List<Damage>, Float> possibleDamageMultiply(CardThatCanBattle targetCard, CardThatCanBattle attackerCard, Map<List<Damage>, Float> attacks) {
        return attacks;
    }

    public List<Damage> whatIsYourAttack(CardThatCanBattle targetCard, String targetTerrain, CardThatCanBattle attackerCard, String attackerTerrain) {
        List<Damage> attack = attackerCard.getAttack();
        attack = damageBonus(targetCard, targetTerrain, attackerCard, attackerTerrain, attack);
        return damageMultiply(targetCard, targetTerrain, attackerCard, attackerTerrain, attack);
    }

    public Map<List<Damage>, Float> possibleAttacks(CardThatCanBattle targetCard, CardThatCanBattle attackerCard) {
        List<Damage> attack = attackerCard.getAttack();
        Map<List<Damage>, Float> attacks = possibleDamageBonus(targetCard, attackerCard, Support.copyDamages(attack));
        return possibleDamageMultiply(targetCard, attackerCard, attacks);
    }
}

class StandardAttack extends AbstractAttack implements Attack {}

class LargeDesertScorpionAttack extends AbstractAttack implements Attack {

    @Override
    List<Damage> damageBonus(CardThatCanBattle targetCard, String targetTerrain,
                             CardThatCanBattle attackerCard, String attackerTerrain, List<Damage> attack) {
        List<Damage> newAttack = Support.copyDamages(attack);
        if(attackerTerrain.equals("Desert"))
            for(Damage damage:newAttack) if(damage.getType().contains("Piercing")) damage.setDamage(damage.getDamage()+2);
        return newAttack;
    }
    @Override
    Map<List<Damage>, Float> possibleDamageBonus(CardThatCanBattle targetCard, CardThatCanBattle attackerCard, List<Damage> attack) {
        Map<List<Damage>, Float> attacks = new HashMap<List<Damage>, Float>();
        TerrainType tt = attackerCard.getCurrentLocation().getTerrain();
        if(!tt.getTerrains().contains("Desert")) { attacks.put(attack, (float)1); return attacks; }
        int size = tt.getTerrains().size();
        attacks.put(Support.copyDamages(attack), (float)((size-1)/size));
        attacks.put(damageBonus(targetCard, "", attackerCard, "Desert", Support.copyDamages(attack)), (float)(1/size));
        return attacks;
    }
}
