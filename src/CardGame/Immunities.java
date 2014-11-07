package CardGame;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Kristján Tryggvason
 * Date: 21.3.2014
 * Time: 14:35
 * To change this template use File | Settings | File Templates.
 */
public class Immunities {
    private HashMap<String,Set<String>> immunities = new HashMap<String, Set<String>>();

    Immunities() {
        Set<String> set = new HashSet<String>();
        set.add("Undead"); set.add("Inanimate");
        immunities.put("neurotoxin",new HashSet<String>(set)); set.clear();
    }

    public boolean isImmune(String debuff, List<String> unitType) {
        if(unitType != null && immunities.containsKey(debuff))
            for(String type:unitType) if(immunities.get(debuff).contains(type))
                return true;
        return false;
    }
}
