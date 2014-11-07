package CardGame;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Kristján Tryggvason
 * Date: 8.3.2014
 * Time: 17:49
 * To change this template use File | Settings | File Templates.
 */
public class TerrainType {
    private Map<String,Integer> terrains = new HashMap<String,Integer>();

    TerrainType(Map _terrains) {
        terrains = _terrains;
    }
    TerrainType(TerrainType terrainType) {
        for(String terrain:terrainType.getTerrains()) terrains.put(terrain,terrainType.terrains.get(terrain));
    }

    public Set<String> getTerrains() { return terrains.keySet(); }
}
