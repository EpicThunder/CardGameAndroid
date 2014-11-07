package CardGame;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Kristján Tryggvason
 * Date: 31.3.2014
 * Time: 20:11
 * To change this template use File | Settings | File Templates.
 */
public abstract class CardThatCanBattle extends Card {
    int maxHealth, health, maxMana, mana, attackRange, moveRange;
    protected TerrainPlacement preferredPlacement;
    protected Move howToMove;
    protected Attack howToAttack;
    protected DamageTaken howDamageIsCalculated;
    protected List<Damage> attack;
    protected List<DamageReduction> damageReductions;

    public int getHealth() { return health; }
    public void setHealth(int newHealth) {
        if(newHealth>maxHealth) health = maxHealth; else if(newHealth<0) health = 0; else health = newHealth;
    }
    public int getMana() { return mana; }
    public void setMana(int newMana)
    {
        if(newMana>maxMana) mana = maxMana; else if(newMana<0) mana = 0; else mana = newMana;
    }
    public int getMaxHealth() { return maxHealth; }
    public int getMaxMana() { return maxMana; }
    public int getAttackRange() { return attackRange; }
    public void setAttackRange(int _attackRange) { attackRange = _attackRange; }
    public void changeAttackRange(int changeRange) { attackRange += changeRange; }
    public int getMoveRange() { return moveRange; }
    public void setMoveRange(int newMoveRange) { moveRange = newMoveRange; }
    public void changeMoveRange(int changeRange) { moveRange += changeRange; }
    public Move getHowToMove() { return howToMove; }
    public Attack getHowToAttack() { return howToAttack; }
    public DamageTaken getHowDamageIsCalculated() { return howDamageIsCalculated; }
    public String getPreferredPlacement() { return preferredPlacement.terrainPlacement(location.getTerrain()); }
    public List<Damage> getAttack() { return attack; }
    public List<DamageReduction> getDamageReductions() { return damageReductions; }

    public List<String> canStandOn() { return howToMove.getCanStandOn(); }
    public boolean isMoveLegal(int newRowPos, int newColPos) {
        return howToMove.legalMove(location.getRowPos(),location.getColPos(),newRowPos,newColPos,moveRange,ownedBy);
    }
    public List<Damage> whatIsYourAttack(CardThatCanBattle opponentCard, String opponentTerrain,
                                         CardThatCanBattle yourCard, String yourTerrain)
    {
        return howToAttack.whatIsYourAttack(opponentCard,opponentTerrain,yourCard,yourTerrain);
    }
    public Map<List<Damage>, Float> whatAreThePossibleAttacks(CardThatCanBattle opponentCard, CardThatCanBattle yourCard)
    {
        return howToAttack.possibleAttacks(opponentCard, yourCard);
    }

    public boolean inRange(Card yourCard, Card opponentCard) {
        int yourRow = yourCard.getCurrentLocation().getRowPos(), yourCol = yourCard.getCurrentLocation().getColPos(),
            opponentsRow = opponentCard.getCurrentLocation().getRowPos(), opponentsCol = opponentCard.getCurrentLocation().getColPos();
        int rowDist = yourRow-opponentsRow, colDist = yourCol-opponentsCol;
        if(colDist<0) colDist = -1*colDist; if(rowDist<0) rowDist = -1*rowDist;
        return !(colDist+rowDist > attackRange);
    }

    public void takeHit(Card attackerCard, String attackerTerrain, String targetTerrain, List<Damage> attack)
    {
        int damage = howDamageIsCalculated.whatIsTheDamageTaken(
                attackerCard, attackerTerrain, this, targetTerrain, attack);
        int newHealth = getHealth()-damage;
        List<Card> list = new ArrayList<Card>(); list.add(this);
        List<Effect> effects = attackerCard.getInflictingEffects();
        if(effects != null) for(Effect effect: attackerCard.getInflictingEffects()) {
            effect.whenAttacked(this, attackerCard);
            effect.setEffect(list);
            if(effect.hasSource()) effect.setSource(attackerCard);
        }
        setHealth(newHealth);
    }

    public List<List<Float>> possibleDamage(Card attackerCard, Map<List<Damage>, Float> attacks) {
        List<List<Float>> listOfPossibleDamageTaken = new ArrayList<List<Float>>();
        for(List<Damage> attack:attacks.keySet()) {
            List<List<Float>> allPossibleDamage = howDamageIsCalculated.allPossibleDamage(attackerCard, this, attack);
            //System.out.print("DamageTakenSize:"+allPossibleDamage.size()+". ");
            //System.out.print(". ");
            for(List<Float> possibleDamage:allPossibleDamage) {
                possibleDamage.set(0, possibleDamage.get(0) * attacks.get(attack));
                //System.out.print("DN:"+possibleDamage.get(1)+". ");
            }
            listOfPossibleDamageTaken.addAll(allPossibleDamage);
        }
        //System.out.print("AllDamageTakenSize:"+listOfPossibleDamageTaken.size());
        Set<Float> setOfDamageValues = new HashSet<Float>();
        List<List<Float>> newListOfPossibleDamageTaken = new ArrayList<List<Float>>();
        List<Float> possibleDamageTaken = new ArrayList<Float>();
        for(int i=0; i<listOfPossibleDamageTaken.size(); i++) {
            //System.out.print(". DN:"+listOfPossibleDamageTaken.get(i).get(1));
            if(setOfDamageValues.contains(listOfPossibleDamageTaken.get(i).get(1))) { /*System.out.print(". cont");*/ continue;}
            else {
                possibleDamageTaken.clear(); possibleDamageTaken.add(listOfPossibleDamageTaken.get(i).get(0));
                possibleDamageTaken.add(listOfPossibleDamageTaken.get(i).get(1));
                setOfDamageValues.add(possibleDamageTaken.get(1));
            }
            for(int j=i+1; j<listOfPossibleDamageTaken.size(); j++) {
                if(listOfPossibleDamageTaken.get(i).get(1).intValue() == listOfPossibleDamageTaken.get(j).get(1).intValue()) {
                    possibleDamageTaken.set(0, possibleDamageTaken.get(0) + listOfPossibleDamageTaken.get(j).get(0));
                }
            }
            //System.out.print(". NewAllDamageTakenSize:"+newListOfPossibleDamageTaken.size());
            newListOfPossibleDamageTaken.add(new ArrayList<Float>(possibleDamageTaken));
        }
        //System.out.println(". NewAllDamageTakenSize:"+newListOfPossibleDamageTaken.size()+". end. ");
        return newListOfPossibleDamageTaken;
    }
}
