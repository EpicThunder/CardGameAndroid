package CardGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Kristján Tryggvason
 * Date: 21.3.2014
 * Time: 14:18
 * To change this template use File | Settings | File Templates.
 */
public interface Action {
    void newTurn(Card card);
    void endTurn(Card card);
    int range();
    void useActionOn(List<Card> targetCards, Card yourCard) throws IllegalAccessException, InstantiationException;
    void activateAction(Card card);
    void targetLocation(Cell cell, Card card);
    void triggerLocationEffect(Card movingCard, Card yourCard);
    boolean actionTypeIs(String type);
    int targetNumber();
    boolean continuousAction();
}

abstract class AbstractAction {

    protected boolean usedThisTurn;

    public void newTurn(Card card) { }
    public void endTurn(Card card) { }
    public int range() { return 1; }
    public void useActionOn(List<Card> targetCards, Card yourCard) throws IllegalAccessException, InstantiationException {}
    public void activateAction(Card card) {}
    public void targetLocation(Cell cell, Card card) {}
    public void triggerLocationEffect(Card movingCard, Card yourCard) {}
    public boolean actionTypeIs(String type) { if(type.equals(specificAction())) return true; else return false; }
    public String specificAction() { return "Activate"; }
    public int targetNumber() { return 1; }
    public boolean continuousAction() { return false; }

}

class FireBall extends AbstractAction implements Action {

    @Override
    public void useActionOn(List<Card> targetCards, Card yourCard) {
        List<Damage> attack = new ArrayList<Damage>(); List<String> type = new ArrayList<String>(); type.add("Fire");
        attack.add(new Damage(type, 15));
        List<CardThatCanBattle> battleCards = new ArrayList<CardThatCanBattle>();
        for(Card target:targetCards) if(Support.isBattleCard(target)) battleCards.add((CardThatCanBattle)target);
        if (Support.isBattleCard(yourCard)) {
            int currentMana = ((CardThatCanBattle) yourCard).getMana();
            if(currentMana >= 5) {
                for (CardThatCanBattle target : battleCards) {
                    target.takeHit(yourCard, ((CardThatCanBattle) yourCard).getPreferredPlacement(),
                                    target.getPreferredPlacement(), attack);
                }
                ((CardThatCanBattle) yourCard).setMana(currentMana - 5);
            }
        } else {
            if(yourCard.canPay()) {
                for (CardThatCanBattle target : battleCards)
                    target.takeHit(yourCard, null, target.getPreferredPlacement(), attack);
                yourCard.payCost();
            }
        }
    }

    @Override
    public String specificAction() { return "TargetCard"; }
}

class SkyAirBlast extends AbstractAction implements Action {

    @Override
    public int range() { return 2; }

    @Override
    public void useActionOn(List<Card> targetCards, Card yourCard) {
        List<Damage> attack = new ArrayList<Damage>(); List<String> type = new ArrayList<String>(); type.add("Air");
        attack.add(new Damage(type, 10));
        List<CardThatCanBattle> battleCards = new ArrayList<CardThatCanBattle>();
        for(Card target:targetCards) if(Support.isBattleCard(target)) battleCards.add((CardThatCanBattle)target);
        if (yourCard.getClass().getSuperclass().toString().equals("CardThatCanBattle")) {
            int currentMana = ((CardThatCanBattle) yourCard).getMana();
            if(currentMana >= 2) {
                for (CardThatCanBattle target : battleCards) if(target.getCardType().contains("Flying"))
                    target.takeHit(yourCard, ((CardThatCanBattle) yourCard).getPreferredPlacement(),
                            target.getPreferredPlacement(), attack);
                ((CardThatCanBattle) yourCard).setMana(currentMana - 2);
            }
        } else {
            if(yourCard.canPay()) {
                for (CardThatCanBattle target : battleCards)
                    target.takeHit(yourCard, null, target.getPreferredPlacement(), attack);
                yourCard.payCost();
            }
        }
    }

    @Override
    public String specificAction() { return "TargetCard"; }
}

class Quake extends AbstractAction implements Action {

    @Override
    public void useActionOn(List<Card> targetCards, Card yourCard) {
        List<Damage> attack = new ArrayList<Damage>(); List<String> type = new ArrayList<String>(); type.add("Earth");
        attack.add(new Damage(type, 200));
        List<StructureCard> structureCards = new ArrayList<StructureCard>();
        for(Card target:targetCards)
            if(target.getCardCategory().equals("Structure")) structureCards.add((StructureCard)target);
        if(yourCard.getClass().getSuperclass().toString().equals("CardThatCanBattle")) {
            int currentMana = ((CardThatCanBattle)yourCard).getMana();
            if(currentMana >= 5) {
                for (StructureCard target : structureCards)
                    target.takeHit(yourCard, ((CardThatCanBattle) yourCard).getPreferredPlacement(),
                            target.getPreferredPlacement(), attack);
                ((CardThatCanBattle)yourCard).setMana(currentMana - 5);
            }
        }
        else {
            if(yourCard.canPay()) {
                for (StructureCard target : structureCards)
                    target.takeHit(yourCard, null, target.getPreferredPlacement(), attack);
                yourCard.payCost();
            }
        }
    }

    @Override
    public String specificAction() { return "TargetCard"; }
}

class Pillage extends AbstractAction implements Action {

    @Override
    public int range() { return 0; }

    @Override
    public void useActionOn(List<Card> targetCards, Card yourCard) {
        for(Card target:targetCards)
            if(target.getName() == "Tax depository") {
                if(target.getOwner().getGold() < 60) {
                    yourCard.getOwner().setGold(yourCard.getOwner().getGold()+ target.getOwner().getGold());
                    target.getOwner().setGold(0);
                }
                else {
                    yourCard.getOwner().setGold(yourCard.getOwner().getGold()+60);
                    target.getOwner().setGold(target.getOwner().getGold()-60);
                }
            }
    }

    @Override
    public String specificAction() { return "TargetCard"; }
}

class ProvideGold extends AbstractAction implements Action {

    @Override
    public void newTurn(Card card) {
        card.getOwner().changeGold(60);
    }
}

class Outpost extends AbstractAction implements Action {

    @Override
    public void activateAction(Card card)
    {
        Cell cell = card.getCurrentLocation();
        for(int row=cell.getRowPos()-1; row<cell.getRowPos()+2; row++)
            for(int col=cell.getColPos()-1; col<cell.getColPos()+2; col++)
                Support.getState().getCell(row, col).setInfluence(card.getOwner());
    }
}

class ProtectFromRanged extends AbstractAction implements Action {

    ImmuneToRanged immuneToRanged = new ImmuneToRanged();

    @Override
    public void activateAction(Card card) {
        List<Card> cardsThatCanBattle = new ArrayList<Card>(),
            cards = Support.getState().getCell(card.getCurrentLocation().getRowPos(), card.getCurrentLocation().getColPos()).getCards();
        for(Card aCard:cards)
            if(Support.isBattleCard(aCard)) cardsThatCanBattle.add(aCard);
        immuneToRanged.setEffect(cardsThatCanBattle);
    }

    @Override
    public void triggerLocationEffect(Card movingCard, Card yourCard) {
        if(movingCard.getCurrentLocation().getRowPos() == yourCard.getCurrentLocation().getRowPos() &&
           movingCard.getCurrentLocation().getColPos() == yourCard.getCurrentLocation().getColPos() &&
           Support.isBattleCard(movingCard))
        {
            List<Card> cards = new ArrayList<Card>(); cards.add(movingCard);
            immuneToRanged.setEffect(cards);
        }
    }
}

class ProvideMana extends AbstractAction implements Action {

    @Override
    public void endTurn(Card card) {
        card.getOwner().changeMana(5);
        List<Card> cards = Support.getState().getCell(card.getCurrentLocation().getRowPos(), card.getCurrentLocation().getColPos()).getCards();
        for(Card aCard:cards)
            if(Support.isBattleCard(aCard)) {
                CardThatCanBattle cardThatCanBattle = (CardThatCanBattle)aCard;
                if(cardThatCanBattle.getMana()+5 > cardThatCanBattle.getMaxMana()) cardThatCanBattle.setMana(cardThatCanBattle.getMaxMana());
                else cardThatCanBattle.setMana(cardThatCanBattle.getMana()+5);
            }
    }
}

class IncreaseAttackRange extends AbstractAction implements Action {

    IncreasedAttackRange increasedAttackRange = new IncreasedAttackRange();

    @Override
    public void activateAction(Card card) {
        List<Card> cardsThatCanBattle = new ArrayList<Card>(),
        cards = Support.getState().getCell(card.getCurrentLocation().getRowPos(), card.getCurrentLocation().getColPos()).getCards();
        for(Card aCard:cards)
            if(Support.isBattleCard(aCard)) cardsThatCanBattle.add(aCard);
        IncreasedAttackRange increasedAttackRange = new IncreasedAttackRange();
        increasedAttackRange.setEffect(cardsThatCanBattle);
    }

    @Override
    public void triggerLocationEffect(Card movingCard, Card yourCard) {
        if(movingCard.getCurrentLocation().getRowPos() == yourCard.getCurrentLocation().getRowPos() &&
           movingCard.getCurrentLocation().getColPos() == yourCard.getCurrentLocation().getColPos() &&
           movingCard.getClass().getSuperclass().toString().equals("CardThatCanBattle"))
        {
            List<Card> cards = new ArrayList<Card>(); cards.add(movingCard);
            increasedAttackRange.setEffect(cards);
        }
    }
}

class Carry extends AbstractAction implements Action {

    List<Card> carried = new ArrayList<Card>();

    @Override
    public void activateAction(Card card) {
        List<Card> canCarry = new ArrayList<Card>();
        Player currentPlayer = Support.getState().getCurrentPlayer();
        State state = Support.getState();
        Cell cell = card.getCurrentLocation(); int pos;
        Support.addPlayersUnitCardsFromCell(cell, currentPlayer, canCarry);
        pos = card.getCurrentLocation().getRowPos();
        if(pos > 0) Support.addPlayersUnitCardsFromCell(state.getCell(pos-1, cell.getColPos()), currentPlayer, canCarry);
        pos = card.getCurrentLocation().getRowPos();
        if(pos < state.boardRowSize()-1) Support.addPlayersUnitCardsFromCell(state.getCell(pos+1, cell.getColPos()), currentPlayer, canCarry);
        pos = card.getCurrentLocation().getColPos();
        if(pos > 0) Support.addPlayersUnitCardsFromCell(state.getCell(cell.getRowPos(), pos-1), currentPlayer, canCarry);
        pos = card.getCurrentLocation().getColPos();
        if(pos < state.boardColSize()-1) Support.addPlayersUnitCardsFromCell(state.getCell(cell.getRowPos(), pos+1), currentPlayer, canCarry);
        System.out.println("Cards that you are carrying:");
        for(Card aCard:carried) System.out.println(aCard.getName()+". ID("+aCard.getID()+");");
        System.out.println("Cards that you can carry");
        for(Card aCard:canCarry) System.out.println(aCard.getName()+". ID("+aCard.getID()+");");
        System.out.println("\nDo you want to load or unload a card");
        if(Support.getInput().toLowerCase().equals("load")) {
            System.out.print("Select ID of card to carry:");
            int id = Integer.parseInt(Support.getInput());
            for(Card aCard:canCarry) if(aCard.getID() == id) { carried.add(aCard); aCard.removedFromLocation(); }
        }
        else {
            System.out.print("Select ID of card to unload and location to land, in the form of ID,row,col:");
            String[] input = Support.getInput().split(","); if(input.length < 3) return;
            int id = Integer.parseInt(input[0]), row = Integer.parseInt(input[1]), col = Integer.parseInt(input[2]);
            Set<String> targetTerrains = state.getCell(row, col).getTerrain().getTerrains();
            for(Card aCard:carried) {
                Set<String> newSet = new HashSet<String>(targetTerrains); newSet.retainAll(((UnitCard)aCard).canStandOn());
                if(aCard.getID() == id && newSet.size() > 0) {
                    carried.remove(aCard); state.getCell(row, col).placeMovedCard(aCard);
                }
            }
        }
    }
}

class Heal extends AbstractAction implements Action {

    @Override
    public void useActionOn(List<Card> targetCards, Card yourCard) throws IllegalAccessException, InstantiationException {
        if(yourCard.canPay()) {
            for (Card target : targetCards)
                if (Support.isBattleCard(target))
                    ((CardThatCanBattle) target).setHealth(((CardThatCanBattle) target).getHealth() + 10);
            yourCard.payCost();
        }
    }

    @Override
    public String specificAction() { return "TargetCard"; }
}


class BurnDown extends AbstractAction implements Action {

    @Override
    public void useActionOn(List<Card> targetCards, Card yourCard) throws IllegalAccessException, InstantiationException {
        if(yourCard.canPay()) {
            List<Damage> attack = new ArrayList<Damage>(); List<String> type = new ArrayList<String>(); type.add("Fire");
            attack.add(new Damage(type, 500));
            List<StructureCard> structureCards = new ArrayList<StructureCard>();
            for(Card target:targetCards)
                if(target.getCardCategory().equals("Structure")) structureCards.add((StructureCard)target);
            for (StructureCard target : structureCards)
                target.takeHit(yourCard, null, target.getPreferredPlacement(), attack);
            yourCard.payCost();
        }
    }

    @Override
    public String specificAction() { return "TargetCard"; }
}

class FireArrow extends AbstractAction implements Action {

    @Override
    public void useActionOn(List<Card> targetCards, Card yourCard) throws IllegalAccessException, InstantiationException {
        if(yourCard.canPay()) {
            List<Damage> attack = new ArrayList<Damage>(); List<String> type = new ArrayList<String>(); type.add("Fire");
            attack.add(new Damage(type, 10));
            List<CardThatCanBattle> battleCards = new ArrayList<CardThatCanBattle>();
            for(Card target:targetCards) if(Support.isBattleCard(target)) battleCards.add((CardThatCanBattle)target);
            for (CardThatCanBattle target : battleCards)
                if (target.getCurrentLocation().hasInfluenceOver(yourCard.getOwner()))
                    target.takeHit(yourCard, null, target.getPreferredPlacement(), attack);
            yourCard.payCost();
        }
    }

    @Override
    public String specificAction() { return "TargetCard"; }
}

class Slow extends AbstractAction implements Action {

    @Override
    public void useActionOn(List<Card> targetCards, Card yourCard) throws IllegalAccessException, InstantiationException {
        Effect slowed = new Slowed();
        List<Card> battleCards = new ArrayList<Card>();
        for(Card target:targetCards) if(Support.isBattleCard(target)) battleCards.add(target);
        if(yourCard.canPay() && slowed.setEffect(battleCards)) {
            slowed.setSource(yourCard); Support.getState().getActiveCards().add(yourCard);
            yourCard.payCost();
        }
    }

    @Override
    public String specificAction() { return "TargetCard"; }
}

class Ground extends AbstractAction implements Action {

    @Override
    public void useActionOn(List<Card> targetCards, Card yourCard) throws IllegalAccessException, InstantiationException {
        Effect grounded = new Grounded();
        List<Card> battleCards = new ArrayList<Card>();
        for(Card target:targetCards) if(Support.isBattleCard(target)) battleCards.add(target);
        if(yourCard.canPay() && grounded.setEffect(battleCards)) {
            grounded.setSource(yourCard); Support.getState().getActiveCards().add(yourCard);
            yourCard.payCost();
        }
    }

    @Override
    public String specificAction() { return "TargetCard"; }
}

class NewArmor extends AbstractAction implements Action {

    @Override
    public void useActionOn(List<Card> targetCards, Card yourCard) throws IllegalAccessException, InstantiationException {
        if(yourCard.canPay()) {
            List<Defence> defenses = null;
            int layer, layer2;
            for (Card target : targetCards)
                if (target.getCardCategory().equals("Unit")) {
                    defenses = ((UnitCard) target).getDefences();
                    layer = -2;
                    layer2 = -1;
                    for (Defence defense : new ArrayList<Defence>(defenses)) {
                        if (defense.getLayer() > layer2) layer2 = defense.getLayer();
                        if (defense.getType().equals("Defence")) {
                            layer = defense.getLayer();
                            defenses.remove(defense);
                        }
                    }
                    layer2++;
                    if (layer != -2) layer2 = layer;
                    defenses.add(new Defence(50, 6, layer2, "Defence"));
                }
            yourCard.payCost();
        }
    }

    @Override
    public String specificAction() { return "TargetCard"; }
}

class Antidote extends AbstractAction implements Action {

    @Override
    public void useActionOn(List<Card> targetCards, Card yourCard) throws IllegalAccessException, InstantiationException {
        if(yourCard.canPay()) {
            List<Effect> effects = null;
            for (Card target : targetCards) {
                effects = target.getInflictedEffects();
                for (Effect effect : effects) {
                    if (effect.getClass().toString().equals("Neurotoxin")) {
                        effect.removeEffectFrom(target);
                        effects.remove(effect);
                    }
                }
            }
            yourCard.payCost();
        }
    }

    @Override
    public String specificAction() { return "TargetCard"; }
}

class SteelJawTrap extends AbstractAction implements Action {

    @Override
    public void triggerLocationEffect(Card movingCard, Card yourCard) {
        if(movingCard.getCurrentLocation().getRowPos() == yourCard.getCurrentLocation().getRowPos() &&
           movingCard.getCurrentLocation().getColPos() == yourCard.getCurrentLocation().getColPos() &&
           movingCard.getCardCategory().equals("Unit") && Math.random() < 0.5)
        {
            List<Card> cards = new ArrayList<Card>(); cards.add(movingCard);
            Immobile immobile = new Immobile();
            immobile.setEffect(cards);
            immobile.setSource(yourCard);
        }
    }

    @Override
    public String specificAction() { return "TargetLocation"; }
}

class ContinuousAttack extends AbstractAction implements Action {

    @Override
    public void useActionOn(List<Card> targetCards, Card yourCard) throws IllegalAccessException, InstantiationException {
        if(targetCards == null || targetCards.size() < 1) return;
        boolean hasInBattle = false;
        for(Effect effect:targetCards.get(0).getInflictedEffects())
            if(effect.getClass().getName().equals("InBattle") && effect.isSource(yourCard)) { hasInBattle = true; break; }
        if(!hasInBattle) return;
        CardThatCanBattle targetBattleCard = (CardThatCanBattle)targetCards.get(0);
        CardThatCanBattle yourBattleCard = (CardThatCanBattle)yourCard;
        List<String> terrains = Support.terrainsUsedForBattle(yourBattleCard, targetBattleCard);
        List<Damage> attack = yourBattleCard.whatIsYourAttack(targetBattleCard, terrains.get(1), yourBattleCard, terrains.get(0));
        targetBattleCard.takeHit(yourBattleCard, terrains.get(0), terrains.get(1), attack);
        if(targetBattleCard.getHealth() == 0) { yourBattleCard.acted(); return; }
        attack = targetBattleCard.whatIsYourAttack(yourBattleCard, terrains.get(0), targetBattleCard, terrains.get(1));
        yourBattleCard.takeHit(targetBattleCard, terrains.get(1), terrains.get(0), attack);
    }

    @Override
    public String specificAction() { return "TargetCard"; }

    @Override
    public boolean continuousAction() { return true; }
}