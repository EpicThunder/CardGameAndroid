package CardGame;

import java.util.*;

/**
 * Created by Kristján on 11.4.2014.
 */
public interface Effect {

    float changeValue(float value, List<String> typeOfValue);
    boolean setEffect(List<Card> cards);
    void removeEffectFrom(Card card);
    void removeEffect();
    void moved(Card card);
    void setSource(Card card);
    boolean isSource(Card card);
    void endTurn();
    int timeLeft();
    void whenAttacked(Card target, Card source);
    boolean hasSource();
    AbstractEffect returnCopy() throws IllegalAccessException, InstantiationException;
}

abstract class AbstractEffect implements Effect{
    Card source = null;
    List<Card> targets = new ArrayList<Card>();
    int duration = -2;
    boolean didSet = false;

    public void setValues(Card newSource, List<Card> newTargets, int newDuration, boolean newDidSet) {
        source = newSource; targets = newTargets; duration = newDuration; didSet = newDidSet;
    }
    public void setTarget(Card card) { targets.add(card); }
    public void setDuration(int newDuration) { duration = newDuration; }

    public float changeValue(float value, List<String> typeOfValue) { return value; }
    abstract public boolean setEffect(List<Card> cards);
    public void endTurn() { specificEndTurn(); duration--; if(duration == -1) removeEffect(); }
    public void specificEndTurn() {}
    public void removeEffectFrom(Card card) {
        if(targets.contains(card)) {
            removeSpecificEffectFrom(card);
            targets.remove(card);
        }
        if(targets.isEmpty()) { if(!source.isEffecting()) Support.getState().removeActiveCard(source); source = null; }
    }
    public void removeEffect() {
        for(Card target:new ArrayList<Card>(targets)) { removeSpecificEffectFrom(target); targets.remove(target); }
        if(source != null && !source.isEffecting()) Support.getState().removeActiveCard(source); source = null;
    }
    abstract public void removeSpecificEffectFrom(Card card);
    public void moved(Card card) { if(targets.contains(card)) { specificMoved(card); } }
    public void specificMoved(Card card) {}
    public void setSource(Card card) {
        source = card; for(Card target:targets) { target.setInfluencingCard(source); source.setTarget(target); }
    }
    public boolean isSource(Card card) { return (card.equals(source)); }
    public int timeLeft() { return duration; }
    public void whenAttacked(Card target, Card source) {}
    public boolean hasSource() { return false; }
    public AbstractEffect returnCopy() throws IllegalAccessException, InstantiationException {
        AbstractEffect newEffect = this.getClass().newInstance();
        newEffect.setValues(source, targets, duration, didSet);
        return newEffect;
    }
}

class Neurotoxin extends AbstractEffect implements Effect {

     @Override
    public boolean setEffect(List<Card> cards) {
        Neurotoxin effect = new Neurotoxin(); effect.setDuration(6); didSet = false;
        for(Card card:cards) {
            if (card.getCardCategory().equals("Unit") && !Support.isImmune("neurotoxin", card.getCardType())) {
                effect.setTarget(card); card.setInflictedEffect(effect); didSet = true;
            }
        }
        return didSet;
    }

    @Override
    public void specificEndTurn() {
        float damage = 2;
        List<String> type = new ArrayList<String>(); type.add("Damage"); type.add("Toxin");
        for(Card target:targets) {
            for (Card card : target.getInfluencingCards())
                for (Effect effect : card.getInflictedEffects()) damage = effect.changeValue(damage, type);
            if(damage<0) damage = 0;
            ((CardThatCanBattle)target).setHealth(((CardThatCanBattle)target).getHealth()-(int)damage);
            damage = 2;
        }
    }

    @Override
    public void removeSpecificEffectFrom(Card card) { card.removeInflictedEffect(this); }
}

class Grounded extends AbstractEffect implements Effect {

    @Override
    public boolean setEffect(List<Card> cards) {
        Grounded effect = new Grounded(); effect.setDuration(3); didSet = false;
        List<String> cardType;
        for(Card card:cards) {
            cardType = card.getCardType();
            if(cardType.contains("Flying")) {
                cardType.remove("Flying");
                effect.setTarget(card); card.setInflictedEffect(effect); didSet = true;
            }
        }
        return didSet;
    }

    @Override
    public void removeSpecificEffectFrom(Card card) {
        card.getCardType().add("Flying"); card.removeInflictedEffect(this);
    }
}

class Slowed extends AbstractEffect implements Effect {

    @Override
    public boolean setEffect(List<Card> cards) {
        Slowed effect = new Slowed(); effect.setDuration(4); didSet = false;
        for(Card card:cards) if(Support.isBattleCard(card) && ((CardThatCanBattle)card).getMoveRange() > 1) {
                ((CardThatCanBattle) card).changeMoveRange(-1);
            effect.setTarget(card); card.setInflictedEffect(effect); didSet = true;
        }
        return didSet;
    }

    @Override
    public void removeSpecificEffectFrom(Card card) {
        ((CardThatCanBattle)card).changeMoveRange(1); card.removeInflictedEffect(this);
    }
}

class IncreasedAttackRange extends AbstractEffect implements Effect {

    @Override
    public boolean setEffect(List<Card> cards) {
        cards.removeAll(targets);
        for(Card card:cards) if(Support.isBattleCard(card)) {
            ((CardThatCanBattle)card).changeAttackRange(1);
            card.setInflictedEffect(this);
            didSet = true;
        }
        targets.addAll(cards);
        return didSet;
    }

    @Override
    public void specificMoved(Card card) {
        ((CardThatCanBattle)card).changeAttackRange(-1);
        card.removeInflictedEffect(this); targets.remove(card);
    }

    @Override
    public void removeSpecificEffectFrom(Card card) { card.removeInflictedEffect(this); }
}

class ImmuneToRanged extends AbstractEffect implements Effect {

    DamageReduction damageReduction = new DamageReduction("Ranged", 100);

    @Override
    public boolean setEffect(List<Card> cards) {
        cards.removeAll(targets);
        for(Card card:cards) if(Support.isBattleCard(card) && !((CardThatCanBattle)card).getDamageReductions().contains(damageReduction)) {
            ((CardThatCanBattle)card).getDamageReductions().add(damageReduction);
            card.setInflictedEffect(this); targets.add(card); didSet = true;
        }
        return didSet;
    }

    @Override
    public void specificMoved(Card card) {
        ((CardThatCanBattle)card).getDamageReductions().remove(damageReduction);
        card.removeInflictedEffect(this); targets.remove(card);
    }

    @Override
    public void removeSpecificEffectFrom(Card card) { card.removeInflictedEffect(this); }
}

class Immobile extends AbstractEffect implements Effect {

    @Override
    public boolean setEffect(List<Card> cards) {
        List<String> cardType;
        for(Card card:cards) {
            cardType = card.getCardType();
            if(!cardType.contains("Immobile")) {
                cardType.add("Immobile");
                targets.add(card);
                card.setInflictedEffect(this);
                didSet = true;
            }
        }
        return didSet;
    }

    @Override
    public void removeSpecificEffectFrom(Card card) {
        card.getCardType().remove("Immobile"); card.removeInflictedEffect(this);
    }
}

class InBattle extends AbstractEffect implements Effect {

    @Override
    public boolean setEffect(List<Card> cards) {
        for(Card card:cards) {
            targets.add(card);
            card.setInflictedEffect(this);
            didSet = true;
        }
        duration = 0;
        return didSet;
    }

    @Override
    public void removeSpecificEffectFrom(Card card) {
        card.removeInflictedEffect(this);
    }

    @Override
    public void whenAttacked(Card target, Card source) {
        source.stopInfluencingEffect(this.toString());
    }

    @Override
    public boolean hasSource() { return true; }
}