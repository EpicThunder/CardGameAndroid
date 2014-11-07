package CardGame;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kristján Tryggvason
 * Date: 8.3.2014
 * Time: 20:58
 * To change this template use File | Settings | File Templates.
 */
public class UnitCard extends CardThatCanBattle {
    private int weakSpotChance;
    private List<Defence> defences;

    UnitCard(int id, String cardName, int _cost, int _health, int _mana,
             int _attackRange, int _moveRange, int _weakSpotChance, List<String> _cardType,
             List<Damage> _attack, List<Defence> _defences, List<DamageReduction> _damageReductions,
             Move _howToMove, Attack _howToAttack, DamageTaken _howDamageIsCalculated,
             List<Action> _actions, List<Effect> _effects)
    {
        hCode = new Double(Integer.MAX_VALUE * Math.random()).intValue();
        ID = id; name = cardName; category = "Unit"; cost = _cost; maxHealth = health = _health;
        maxMana = mana = _mana; attackRange = _attackRange; moveRange = _moveRange; weakSpotChance = _weakSpotChance;
        cardType = _cardType; attack = _attack; defences = _defences; damageReductions = _damageReductions;
        howToMove = _howToMove; howToAttack = _howToAttack; howDamageIsCalculated = _howDamageIsCalculated;
        actions = _actions; inflictingEffects = _effects; preferredPlacement = new StandardTerrainPlacement();
        canActivateCard = canTargetCard = canTargetLocation = false;
        for(Action action:actions) {
            if(action.actionTypeIs("Activate")) canActivateCard = true;
            if(action.actionTypeIs("TargetCard")) canTargetCard = true;
            if(action.actionTypeIs("TargetLocation")) canTargetLocation = true;
        }
    }
    UnitCard(UnitCard card) throws InstantiationException, IllegalAccessException {
        ID = card.getID(); name = card.getName(); category = "Unit"; cost = card.getCost();
        maxHealth = card.getMaxHealth(); health = card.getHealth(); maxMana = card.getMaxMana(); mana = card.getMana();
        attackRange = card.getAttackRange(); moveRange = card.getMoveRange(); weakSpotChance = card.getWeakSpotChance();
        cardType = Support.copyStrings(card.getCardType()); attack = Support.copyDamages(card.getAttack());
        defences = Support.copyDefences(card.getDefences());
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
        if(o == null || !o.getClass().getSimpleName().equals("UnitCard")) return false;
        UnitCard u = (UnitCard)o;
        return (ID == u.getID() && health == u.health && mana == u.mana && attackRange == u.attackRange &&
                moveRange == u.moveRange && cardType.containsAll(u.cardType) && u.cardType.containsAll(cardType));
    }

    public int getWeakSpotChance() { return weakSpotChance; }
    public List<Defence> getDefences() { return defences; }

}
