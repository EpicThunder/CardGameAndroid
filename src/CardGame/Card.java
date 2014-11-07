package CardGame;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Kristján Tryggvason
 * Date: 8.3.2014
 * Time: 15:55
 * To change this template use File | Settings | File Templates.
 */
public abstract class Card {
    protected Player ownedBy;
    protected int ID, cost, hCode;
    protected Cell location;
    protected String name, category;
    protected boolean canTargetLocation, canTargetCard, canActivateCard, hasMoved, hasActed;
    protected List<String> cardType;
    protected List<Card> influencingCards = new ArrayList<Card>(), targetCards = new ArrayList<Card>();
    protected List<Action> actions;
    protected List<Effect> inflictingEffects = null;
    protected List<Effect> inflictedEffects = new ArrayList<Effect>();

    public int getID() { return ID; }
    public String getName() { return name; }
    public String getCardCategory() { return category; }
    public int getCost() { return cost; }
    public boolean canPay() { return (ownedBy.canPay(this)); }
    public void payCost() { ownedBy.payCost(this); }
    public Player getOwner() { return ownedBy; }
    public void setOwner(Player owner) { ownedBy = owner; }
    public List<String> getCardType() { return cardType; }
    public boolean canTargetLocation() { return canTargetLocation; }
    public boolean canTargetCard() { return canTargetCard; }
    public boolean canActivateCard() { return canActivateCard; }
    public void acted() { hasActed = true; }
    public boolean hasActed() { return hasActed; }
    public void moved() { hasMoved = true; for(Effect effect:inflictedEffects) effect.moved(this); }
    public boolean hasMoved() { return hasMoved; }

    public Cell getCurrentLocation() { return location; }
    public void setCurrentLocation(Cell cell) { location = cell; }
    public void removedFromLocation() { location.removeCard(this); }
    public boolean isOnBoard() { if(location==null) return false; else return location.hasCard(ID); }

    public boolean playCard() throws InstantiationException, IllegalAccessException {
        if(!canPay()) { System.out.println("Can not pay"); return false; }
        int count = 0; if(canTargetLocation) count++; if(canTargetCard) count++; if(canActivateCard) count++;
        if(category.equals("Unit") || category.equals("Structure")) count = 1;
        String select = "";
        if(count > 1) {
            Support.getState().allCardsInPlay();
            if(canTargetLocation) { System.out.println("Select p:To place the card on the board"); }
            if(canTargetCard) System.out.println("Select t:To target a card");
            if(canActivateCard) System.out.println("Select a:To activate your card");
            select = Support.getInput();
        }
        if(select.equals("p") || category.equals("Unit") || category.equals("Structure") || (count == 1 && canTargetLocation)) {
            System.out.println("Select location to place the card, in the form of: row,col");
            String[] input = Support.getInput().split(","); if(input.length < 2) return false;
            int row=Integer.parseInt(input[0]), col=Integer.parseInt(input[1]);
            if(!Support.withinTheBoard(row, col)) return false;
            return placeCard(row, col);
        }
        if(select.equals("t") || (count == 1 && canTargetCard)) {
            List<Action> targetActions = new ArrayList<Action>();
            for(Action action:actions) if(action.actionTypeIs("TargetCard")) targetActions.add(action);
            if(targetActions.size() < 1) return false;
            if(targetActions.size() > 1) {
                System.out.println("Select a number to pick a action");
                for (int i = 1, size = targetActions.size() + 1; i < size; i++) {
                    System.out.println(i+":"+targetActions.get(i).toString());
                } int input = Integer.parseInt(Support.getInput());
                return targetCards(targetActions.get(input));
            }
            else { return targetCards(targetActions.get(0)); }
        }
        if(select.equals("a") || (count == 1 && canActivateCard)) return activateAction();
        return false;
    }
    public boolean placeCard(int row, int col) { // Place a card on to the board
        Support.getState().setUpBoardInfluence();
        return Support.getState().addCardToBoard(this, row, col);
    }
    public boolean targetCards(Action action) throws InstantiationException, IllegalAccessException {
        System.out.print("Select " + action.targetNumber() + " target");
        if(action.targetNumber() != 1) System.out.print("s");
        System.out.println(", a target is in the form of: cardID");
        Support.getState().allCardsInPlay();
        String[] input = Support.getInput().split(",");
        List<Card> targets = new ArrayList<Card>();
        Card enemy;
        for(String id:input) {
            enemy = Support.getState().findCard(Integer.parseInt(id));
            if(enemy == null) return false;
            targets.add(enemy);
        }
        action.useActionOn(targets, this);
        return true;
    }
    public boolean activateAction() {
        List<Action> activatingActions = new ArrayList<Action>();
        for(Action action:new ArrayList<Action>(actions)) if(action.actionTypeIs("Activate")) activatingActions.add(action);
        if(activatingActions.size() < 1) return false;
        if(activatingActions.size() > 1) {
            System.out.println("Select a number to pick a action");
            for (int i = 1, size = activatingActions.size() + 1; i < size; i++) {
                System.out.println(i+":"+activatingActions.get(i).toString());
            } int input = Integer.parseInt(Support.getInput());
            if(input < 1 || input > activatingActions.size()) return false;
            activatingActions.get(input).activateAction(this);
            this.payCost(); return true;
        }
        else { activatingActions.get(0).activateAction(this); this.payCost(); return true; }
    }

    public List<Action> getActions() { return actions; }
    public Action getAction(String theAction) {
        for(Action action:new ArrayList<Action>(actions)) if(action.getClass().getName().equals(theAction)) return action;
        return null;
    }

    public List<Card> getTargetCards() { return targetCards; }
    public void setTarget(Card card) { targetCards.add(card); }
    public void removeTarget(Card card) { targetCards.remove(card); }
    public List<Card> getInfluencingCards() { return influencingCards; }
    public void setInfluencingCard(Card card) { influencingCards.add(card); }
    public void removeInfluencingCard(Card card) { influencingCards.remove(card); }

    public List<Effect> getInflictingEffects() { return inflictingEffects; }
    public List<Effect> getInflictedEffects() { return inflictedEffects; }
    public void setInflictedEffect(Effect effect) { inflictedEffects.add(effect); }
    public void removeInflictedEffect(Effect effect) { inflictedEffects.remove(effect); }
    public void removeInflictedEffect(String effect, Card source) {
        for(Effect aEffect:new ArrayList<Effect>(inflictedEffects))
            if(aEffect.toString().equals(effect) && aEffect.isSource(source)) inflictedEffects.remove(aEffect);
        if(!isEffectedBy(source)) { source.removeTarget(this); influencingCards.remove(source); }
    }
    public void removeInflictedEffects(Card source) {
        for(Effect effect:new ArrayList<Effect>(inflictedEffects))
            if(effect.isSource(source)) { effect.removeEffectFrom(this); inflictedEffects.remove(effect); }
    }
    public void stopInfluencingEffect(String effect) {
        for(Card target:new ArrayList<Card>(targetCards)) target.removeInflictedEffect(effect, this);
    }

    public boolean isEffecting() {
        for(Card target:new ArrayList<Card>(targetCards)) if(target.isEffectedBy(this)) return true;
        return false;
    }
    public boolean isEffectedBy(Card card) {
        for(Effect effect:new ArrayList<Effect>(inflictedEffects)) if(effect.isSource(card)) return true;
        return false;
    }

    public void destroyed() {
        for(Card target:new ArrayList<Card>(targetCards)) { target.removeInflictedEffects(this);  target.removeInfluencingCard(this); }
        targetCards.clear();
        for(Card influencingCard:new ArrayList<Card>(influencingCards)) influencingCard.removeTarget(this);
        influencingCards.clear();
        for(Effect effect:new ArrayList<Effect>(inflictedEffects)) effect.removeEffectFrom(this);
        inflictedEffects.clear();
        Support.getState().removeActiveCard(this);
    }

    public void newTurn() { hasMoved = hasActed = false; for(Action action:actions) action.newTurn(this); }
    public void endTurn() {
        for(Effect effect:new ArrayList<Effect>(inflictedEffects)) effect.endTurn();
        for(Action action:new ArrayList<Action>(actions)) action.endTurn(this);
    }
}
