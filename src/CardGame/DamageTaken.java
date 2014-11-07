package CardGame;

import javax.swing.undo.CannotRedoException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Kristján Tryggvason
 * Date: 24.3.2014
 * Time: 22:37
 * To change this template use File | Settings | File Templates.
 */
public interface DamageTaken {
    int whatIsTheDamageTaken(Card opponentCard, String opponentTerrain,
                             CardThatCanBattle yourCard, String yourTerrain, List<Damage> attack);
    List<List<Float>> allPossibleDamage(Card opponentCard, CardThatCanBattle yourCard, List<Damage> attack);
}

abstract class AbstractDamageTaken {

    int damageReductionBonus(Card opponentCard, String opponentTerrain, CardThatCanBattle yourCard, String yourTerrain)
    { return 0; }
    List<List<Float>> allPossibleDamageReductionBonus(Card opponentCard, CardThatCanBattle yourCard) {
        List<Float> li = new ArrayList<Float>(); li.add(1f); li.add(0f);
        List<List<Float>> lli = new ArrayList<List<Float>>(); lli.add(li); return lli;
    }
    float damageReductionMultiply(Card opponentCard, String opponentTerrain, CardThatCanBattle yourCard, String yourTerrain)
    { return 1; }
    List<List<Float>> allPossibleDamageReductionMultiply(Card opponentCard, CardThatCanBattle yourCard) {
        List<Float> li = new ArrayList<Float>(); li.add(1f); li.add(1f);
        List<List<Float>> lli = new ArrayList<List<Float>>(); lli.add(li); return lli;
    }

    boolean weakspotHit(Card opponentCard, String opponentTerrain, UnitCard yourCard, String yourTerrain) {
        return (Math.random()*100 < yourCard.getWeakSpotChance());
    }

    List<List<Float>> weakspotOutcomes(Card opponentCard, UnitCard yourCard, List<List<Float>> damage) {
        List<List<Float>> damageOutcomes = new ArrayList<List<Float>>();
        List<Float> damageOutcome = new ArrayList<Float>();
        for(List<Float> entry:damage) {
            damageOutcome.clear();
            if(yourCard.getWeakSpotChance() != 0) {
                damageOutcome.add(entry.get(0) * (yourCard.getWeakSpotChance() * 0.01f));
                damageOutcome.add(entry.get(1) * 3);
                damageOutcomes.add(new ArrayList<Float>(damageOutcome)); damageOutcome.clear();
            }
            if(yourCard.getWeakSpotChance() != 100) {
                damageOutcome.add(entry.get(0) * ((100 - yourCard.getWeakSpotChance()) * 0.01f));
                damageOutcome.add(entry.get(1));
                damageOutcomes.add(new ArrayList<Float>(damageOutcome));
            }
        } return damageOutcomes;
    }

    public int totalDamage(List<Damage> attack) {
        int totalDamage = 0;
        for(Damage damage:attack) {
            totalDamage += damage.getDamage();
        }
        return totalDamage;
    }

    public int whatIsTheDamageTaken(Card opponentCard, String opponentTerrain,
                                    CardThatCanBattle yourCard, String yourTerrain, List<Damage> attack) {

        // Damage is separated by types of damage
        List<Damage> calculatedAttacks = Support.copyDamages(attack);
        int totalDamage;

        // If target is a Unit then we may hit some armor defenses that mitigate damage of attack
        if(yourCard.getCardCategory().equals("Unit")) {
            Map<Integer,List<Defence>> armorLayers = new HashMap<Integer, List<Defence>>();
            List<Defence> defences;
            for(Defence defence :((UnitCard)yourCard).getDefences()) {
                if(!armorLayers.containsKey(defence.getLayer())) defences = new ArrayList<Defence>();
                else defences = armorLayers.get(defence.getLayer());
                defences.add(defence);
                armorLayers.put(defence.getLayer(), defences);
            }
            int coverage, defense = 0; double random;
            for(Integer layer:armorLayers.keySet()) {
                coverage = 0; random = Math.random()*100;
                for(Defence defence :armorLayers.get(layer)) {
                    coverage += defence.getCoverage();
                    if(random <= coverage) { defense += defence.getDefence(); break; }
                }
            }
            totalDamage = totalDamage(calculatedAttacks);
            for(Damage damage:calculatedAttacks)
                damage.setDamage(damage.getDamage()-(defense*(damage.getDamage()/totalDamage)));
        }

        // If a card possesses some sort of damage reduction ability then reduce the damage
        int damageReduction = damageReductionBonus(opponentCard, opponentTerrain, yourCard, yourTerrain);
        totalDamage = totalDamage(calculatedAttacks);
        for(Damage damage:calculatedAttacks)
            damage.setDamage(damage.getDamage()-(damageReduction*(damage.getDamage()/totalDamage)));
        for(Damage damage:calculatedAttacks) if(damage.getDamage() < 0) damage.setDamage(0);

        // Other damage resistances mitigate damage
        for(Damage damage:calculatedAttacks) damage.setDamage(
                damage.getDamage()*damageReductionMultiply(opponentCard, opponentTerrain, yourCard, yourTerrain));
        for(DamageReduction reduction:yourCard.getDamageReductions()) {
            for(Damage damage:calculatedAttacks)
                if(reduction.isType(damage.getType()) ||
                    (opponentCard != null && reduction.isType(opponentCard.getCardType())))
                {
                    damage.setDamage(reduction.reduction(damage.getDamage()));
                }
        }

        // As all calculation that mitigates damage according to damage type is over,
        // we combine the separated damage into one
        float totalDamageFloat=0;
        for(Damage damage:calculatedAttacks) totalDamageFloat += damage.getDamage();

        // Damage bonus if we hit a weak spot of a unit
        if(yourCard.getCardCategory().equals("Unit") &&
                weakspotHit(opponentCard, opponentTerrain, (UnitCard)yourCard, yourTerrain)) totalDamageFloat *= 3;

        totalDamage = (int)totalDamageFloat; // Returned damage is given as non fractional number
        return totalDamage; // Return the damage given to the card
    }

    public List<List<Float>> allPossibleDamage(Card attackerCard, CardThatCanBattle targetCard, List<Damage> attack) {

        // Damage is separated by types of damage
        List<Damage> calculatedAttacks = Support.copyDamages(attack);
        Map<List<Damage>, Float> calculatedAllAttacks = new HashMap<List<Damage>, Float>();
        int totalDamage;

        //debug
        List<Integer> listOfSizes = new ArrayList<Integer>();

        // If target is a Unit then we may hit some armor defenses that mitigate damage of attack
        if(targetCard.getCardCategory().equals("Unit")) {
            Map<Integer,List<Defence>> armorLayers = new HashMap<Integer, List<Defence>>();
            List<Defence> defences;
            for(Defence defence :Support.copyDefences(((UnitCard)targetCard).getDefences())) {
                if(!armorLayers.containsKey(defence.getLayer())) defences = new ArrayList<Defence>();
                else defences = armorLayers.get(defence.getLayer());
                defences.add(defence);
                armorLayers.put(defence.getLayer(), defences);
            }
            List<Float> oneDefenceSetup = new ArrayList<Float>(); oneDefenceSetup.add(1f); oneDefenceSetup.add(0f);
            List<List<Float>> allDefenceSetup = new ArrayList<List<Float>>(); allDefenceSetup.add(new ArrayList<Float>(oneDefenceSetup));
            List<List<Float>> iterativeFindDefence = new ArrayList<List<Float>>();
            for(Integer layer:armorLayers.keySet()) {
                for(Defence defence:armorLayers.get(layer)) {
                    for(List<Float> oneDefence:allDefenceSetup) {
                        oneDefenceSetup.clear();
                        oneDefenceSetup.add((0.01f * defence.getCoverage()) * oneDefence.get(0));
                        oneDefenceSetup.add(defence.getDefence() + oneDefence.get(1));
                        iterativeFindDefence.add(new ArrayList<Float>(oneDefenceSetup));
                        if(defence.getCoverage() != 100) {
                            oneDefenceSetup.clear();
                            oneDefenceSetup.add((0.01f * (100 - defence.getCoverage())) * oneDefence.get(0));
                            oneDefenceSetup.add(oneDefence.get(1));
                            iterativeFindDefence.add(new ArrayList<Float>(oneDefenceSetup));
                        }
                    }
                }
                allDefenceSetup = iterativeFindDefence; iterativeFindDefence = new ArrayList<List<Float>>();
            }
            Set<Float> setOfDefenceValues = new HashSet<Float>();
            List<List<Float>> newAllDefenceSetup = new ArrayList<List<Float>>();
            for(int i=0; i<allDefenceSetup.size(); i++) {
                if(setOfDefenceValues.contains(allDefenceSetup.get(i).get(1))) continue;
                else {
                    oneDefenceSetup.clear(); oneDefenceSetup.add(allDefenceSetup.get(i).get(0));
                    oneDefenceSetup.add(allDefenceSetup.get(i).get(1));
                    setOfDefenceValues.add(oneDefenceSetup.get(1));
                }
                for(int j=i+1; j<allDefenceSetup.size(); j++) {
                    if(allDefenceSetup.get(i).get(1).intValue() == allDefenceSetup.get(j).get(1).intValue()) {
                        oneDefenceSetup.set(0, oneDefenceSetup.get(0) + allDefenceSetup.get(j).get(0));
                    }
                }
                newAllDefenceSetup.add(new ArrayList<Float>(oneDefenceSetup));
            }

            totalDamage = totalDamage(calculatedAttacks);
            List<Damage> newCalculatedAttacks;
            for(List<Float> oneDefence:newAllDefenceSetup) {
                newCalculatedAttacks = Support.copyDamages(calculatedAttacks);
                for(Damage damage:newCalculatedAttacks) {
                    damage.setDamage(damage.getDamage()-(oneDefence.get(1)*(damage.getDamage()/totalDamage)));
                }
                calculatedAllAttacks.put(newCalculatedAttacks, oneDefence.get(0));
            }
        } else calculatedAllAttacks.put(new ArrayList<Damage>(calculatedAttacks), 1f);
        listOfSizes.add(calculatedAllAttacks.size());

        /*float totalD;
        for(List<Damage> calculatedAttack:calculatedAllAttacks.keySet()) {
            totalD = 0;
            for(Damage damage:calculatedAttack) totalD += damage.getDamage();
            System.out.print("DAA:"+totalD+". ");
        }*/

        // If a card possesses some sort of damage reduction ability then reduce the damage
        List<List<Float>> allDamageReductions = allPossibleDamageReductionBonus(attackerCard, targetCard);
        Map<List<Damage>, Float> newCalculatedAllAttacks = new HashMap<List<Damage>, Float>();
        for(Map.Entry<List<Damage>, Float> entry:calculatedAllAttacks.entrySet()) {
            for(List<Float> reduction:allDamageReductions) {
                List<Damage> listOfDamage = Support.copyDamages(entry.getKey());
                totalDamage = totalDamage(listOfDamage);
                for (Damage damage : listOfDamage) {
                    damage.setDamage(damage.getDamage() - (reduction.get(1) * (damage.getDamage() / totalDamage)));
                    if(damage.getDamage() < 0) damage.setDamage(0);
                }
                newCalculatedAllAttacks.put(listOfDamage, reduction.get(0) * entry.getValue());
            }
        } calculatedAllAttacks = newCalculatedAllAttacks;
        listOfSizes.add(calculatedAllAttacks.size());

        /*for(List<Damage> calculatedAttack:calculatedAllAttacks.keySet()) {
            totalD = 0;
            for(Damage damage:calculatedAttack) totalD += damage.getDamage();
            System.out.print("DBR:"+totalD+". ");
        }*/

        // Other damage resistances mitigate damage
        allDamageReductions = allPossibleDamageReductionMultiply(attackerCard, targetCard);
        newCalculatedAllAttacks = new HashMap<List<Damage>, Float>();
        for(Map.Entry<List<Damage>, Float> entry:calculatedAllAttacks.entrySet()) {
            for(List<Float> reduction:allDamageReductions) {
                List<Damage> listOfDamage = Support.copyDamages(entry.getKey());
                for (Damage damage : listOfDamage) {
                    damage.setDamage(damage.getDamage() * reduction.get(1));
                }
                newCalculatedAllAttacks.put(listOfDamage, reduction.get(0) * entry.getValue());
            }
        } calculatedAllAttacks = newCalculatedAllAttacks;
        for(DamageReduction reduction:targetCard.getDamageReductions()) {
            for(Map.Entry<List<Damage>, Float> entry:calculatedAllAttacks.entrySet()) {
                for(Damage damage : entry.getKey()) {
                    if (reduction.isType(damage.getType()) ||
                            (attackerCard != null && reduction.isType(attackerCard.getCardType()))) {
                        damage.setDamage(reduction.reduction(damage.getDamage()));
                    }
                }
            }
        }
        listOfSizes.add(calculatedAllAttacks.size());

        // As all calculation that mitigates damage according to damage type is over,
        // we combine the separated damage into one
        List<List<Float>> listOfTotalDamageFloat = new ArrayList<List<Float>>();
        List<Float> oneTotalDamageFloat = new ArrayList<Float>();
        float totalDamageFloat;
        for(Map.Entry<List<Damage>, Float> entry:calculatedAllAttacks.entrySet()) {
            totalDamageFloat = 0;
            for(Damage damage:entry.getKey()) totalDamageFloat += damage.getDamage();
            oneTotalDamageFloat.clear();
            oneTotalDamageFloat.add(entry.getValue()); oneTotalDamageFloat.add(totalDamageFloat);
            listOfTotalDamageFloat.add(new ArrayList<Float>(oneTotalDamageFloat));
        }
        listOfSizes.add(listOfTotalDamageFloat.size());

        //for(List<Float> aTotalDamageFloat:listOfTotalDamageFloat) System.out.print("DF:"+aTotalDamageFloat.get(1)+". ");

        // Possible damage bonus from hitting a weak spot of a unit added to damage list
        if(targetCard.getCardCategory().equals("Unit"))
            listOfTotalDamageFloat = weakspotOutcomes(attackerCard, (UnitCard)targetCard, listOfTotalDamageFloat);
        listOfSizes.add(listOfTotalDamageFloat.size());

        //for(List<Float> aTotalDamageFloat:listOfTotalDamageFloat) System.out.print("DAW:"+aTotalDamageFloat.get(1)+". ");

        // Returned damage is given as non fractional number
        List<List<Float>> listOfTotalDamage = new ArrayList<List<Float>>();
        for(List<Float> entry:listOfTotalDamageFloat) {
            oneTotalDamageFloat.clear();
            oneTotalDamageFloat.add(entry.get(0)); oneTotalDamageFloat.add((float)entry.get(1).intValue());
            listOfTotalDamage.add(new ArrayList<Float>(oneTotalDamageFloat));
        }
        listOfSizes.add(listOfTotalDamage.size());

        //System.out.print("DamageTakenReturnedSize:"+listOfTotalDamage.size());
        //System.out.print("S after A mitigation:" + listOfSizes.get(0) + " S after D R ability:" + listOfSizes.get(1) + " S after O resistances:" + listOfSizes.get(2) + " Combined:" + listOfSizes.get(3) + " A weak:" + listOfSizes.get(4) + " F:" + listOfSizes.get(5));

        return listOfTotalDamage;
    }
}

class StandardDamageTaken extends AbstractDamageTaken implements DamageTaken {}

class LargeDesertScorpionDamageTaken extends AbstractDamageTaken implements DamageTaken {

    @Override
    boolean weakspotHit(Card opponentCard, String opponentTerrain, UnitCard yourCard, String yourTerrain) {
        return (!yourTerrain.equals("Desert") && Math.random()*100 < yourCard.getWeakSpotChance());
    }
    @Override
    List<List<Float>> weakspotOutcomes(Card opponentCard, UnitCard yourCard, List<List<Float>> damage) {
        Set<String> terrains = yourCard.getCurrentLocation().getTerrain().getTerrains();
        List<List<Float>> damageOutcomes = new ArrayList<List<Float>>();
        List<Float> damageOutcome = new ArrayList<Float>();
        if(!terrains.contains("Desert")) {
            for(List<Float> entry:damage) {
                damageOutcome.clear();
                damageOutcome.add(entry.get(0)*yourCard.getWeakSpotChance()*0.01f);
                damageOutcome.add(entry.get(1)*3);
                damageOutcomes.add(new ArrayList<Float>(damageOutcome)); damageOutcome.clear();
                if(yourCard.getWeakSpotChance() != 0 && yourCard.getWeakSpotChance() != 100) {
                    damageOutcome.add(entry.get(0) * (100 - yourCard.getWeakSpotChance()) * 0.01f);
                    damageOutcome.add(entry.get(1));
                    damageOutcomes.add(damageOutcome);
                }
            } return damageOutcomes;
        }
        float probabilityDesert = 1/terrains.size();
        for(List<Float> entry:damage) {
            damageOutcome.clear();
            damageOutcome.add(entry.get(0)*(1-probabilityDesert)*yourCard.getWeakSpotChance()*0.01f);
            damageOutcome.add(entry.get(1)*3);
            damageOutcomes.add(new ArrayList<Float>(damageOutcome)); damageOutcome.clear();
            if(yourCard.getWeakSpotChance() != 0 && yourCard.getWeakSpotChance() != 100) {
                damageOutcome.add(entry.get(0)*(probabilityDesert+(1-probabilityDesert)*(100 - yourCard.getWeakSpotChance())*0.01f));
                damageOutcome.add(entry.get(1));
                damageOutcomes.add(damageOutcome);
            }
        } return damageOutcomes;
    }
}
