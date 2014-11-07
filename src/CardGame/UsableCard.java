package CardGame;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kristján Tryggvason
 * Date: 8.3.2014
 * Time: 21:34
 * To change this template use File | Settings | File Templates.
 */
public class UsableCard extends Card {
    UsableCard(int id, String cardName, String _category, int _cost, List<String> _cardType, List<Action> _actions) {
        hCode = new Double(Integer.MAX_VALUE * Math.random()).intValue();
        ID = id; name = cardName; category = _category; cost = _cost; cardType = _cardType; actions = _actions;
        canActivateCard = canTargetCard = canTargetLocation = false;
        for(Action action:actions) {
            if(action.actionTypeIs("Activate")) canActivateCard = true;
            if(action.actionTypeIs("TargetCard")) canTargetCard = true;
            if(action.actionTypeIs("TargetLocation")) canTargetLocation = true;
        }
    }
    UsableCard(UsableCard card) throws IllegalAccessException, InstantiationException {
        ID = card.getID(); name = card.getName(); category = card.getCardCategory(); cost = card.getCost();
        cardType = Support.copyStrings(card.getCardType()); actions = Support.copyActions(card.getActions());
        inflictedEffects = Support.copyEffects(card.getInflictedEffects()); canTargetLocation = card.canTargetLocation();
        canTargetCard = card.canTargetCard(); canActivateCard = card.canActivateCard();
        ownedBy = card.getOwner();
        location = card.getCurrentLocation();
        hCode = card.hashCode();
    }

    public int hashCode() { return hCode; }

    public boolean equals(Object o) {
        if(o == null || !o.getClass().getSimpleName().equals("UsableCard")) return false;
        UsableCard u = (UsableCard)o;
        return (ID == u.getID());
    }
}
