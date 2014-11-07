package CardGame;

import java.util.ArrayList;

/**
 * Created by Kristján on 6.4.2014.
 */
public class Base extends Card {
    public Base(Player player) {
        name = category = "Base"; cost = 0; ownedBy = player; actions = new ArrayList<Action>(); ID = -1;
    }

    Base(Base base) {
        name = category = "Base"; cost = 0; ownedBy = base.getOwner(); actions = new ArrayList<Action>();
        location = base.getCurrentLocation(); ID = base.getID();
    }

    public boolean equals(Object o) {
        if(o == null || !o.getClass().getSimpleName().equals("Base")) return false;
        Base b = (Base)o;
        return (ownedBy.getID() == b.getOwner().getID());
    }
}
