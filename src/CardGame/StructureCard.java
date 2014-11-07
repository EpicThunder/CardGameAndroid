package CardGame;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kristján Tryggvason
 * Date: 8.3.2014
 * Time: 21:27
 * To change this template use File | Settings | File Templates.
 */
public class StructureCard extends CardThatCanBattle {
    private int buildTime, timeLeft;

    StructureCard(int id, String cardName, int _cost, int _health, int _mana, int _attackRange,
                  int _moveRange, int _buildTime, List<String> _cardType, List<Damage> _attack,
                  List<DamageReduction> _damageReductions, Move _howToMove, Attack _howToAttack,
                  DamageTaken _howDamageIsCalculated, List<Action> _actions, List<Effect> _effects)
    {
        hCode = new Double(Integer.MAX_VALUE * Math.random()).intValue();
        ID = id; name = cardName; category = "Structure"; cost = _cost; maxHealth = health = _health;
        maxMana = mana = _mana; attackRange = _attackRange; moveRange = _moveRange; buildTime = _buildTime;
        cardType = _cardType; attack = _attack; damageReductions = _damageReductions; howToMove = _howToMove; howToAttack = _howToAttack;
        howDamageIsCalculated = _howDamageIsCalculated; actions = _actions; inflictingEffects = _effects; preferredPlacement = new StandardTerrainPlacement();
        canActivateCard = canTargetCard = canTargetLocation = false;
        for(Action action:actions) {
            if(action.actionTypeIs("Activate")) canActivateCard = true;
            if(action.actionTypeIs("TargetCard")) canTargetCard = true;
            if(action.actionTypeIs("TargetLocation")) canTargetLocation = true;
        }
    }

    StructureCard(StructureCard card) throws InstantiationException, IllegalAccessException {
        ID = card.getID(); name = card.getName(); category = "Structure"; cost = card.getCost();
        maxHealth = card.getMaxHealth(); health = card.getHealth(); maxMana = card.getMaxMana(); mana = card.getMana();
        attackRange = card.getAttackRange(); moveRange = card.getMoveRange();
        cardType = Support.copyStrings(card.getCardType()); attack = Support.copyDamages(card.getAttack());
        buildTime = card.getBuildTime(); timeLeft = card.getTimeLeft();
        damageReductions = Support.copyDamageReductions(card.getDamageReductions()); howToMove = card.getHowToMove();
        howToAttack = card.getHowToAttack(); howDamageIsCalculated = card.getHowDamageIsCalculated();
        actions = Support.copyActions(card.getActions()); inflictingEffects = card.getInflictingEffects();
        inflictedEffects = Support.copyEffects(card.getInflictedEffects());
        preferredPlacement = new StandardTerrainPlacement();
        canTargetLocation = card.canTargetLocation(); canTargetCard = card.canTargetCard();
        canActivateCard = card.canActivateCard();
        ownedBy = card.getOwner();
        location = card.getCurrentLocation();
        hCode = card.hashCode();
    }

    public int hashCode() { return hCode; }

    public boolean equals(Object o) {
        if(o == null || !o.getClass().getSimpleName().equals("StructureCard")) return false;
        StructureCard s = (StructureCard)o;
        return (ID == s.getID() && health == s.health && mana == s.mana && attackRange == s.attackRange &&
                moveRange == s.moveRange && cardType.containsAll(s.cardType) && s.cardType.containsAll(cardType));
    }

    public int getBuildTime() { return buildTime; }
    public int getTimeLeft() { return timeLeft; }
    public void startBuilding() { timeLeft = getBuildTime(); }
    public void endTurn() { if(timeLeft > 0) timeLeft--; super.endTurn(); }
}
