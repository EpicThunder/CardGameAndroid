package CardGame;

import java.util.Set;

/**
 * Created by Kristján on 7.4.2014.
 */
public interface TerrainPlacement {
    String terrainPlacement(TerrainType terrainType);
}

class StandardTerrainPlacement implements TerrainPlacement {

    @Override
    public String terrainPlacement(TerrainType terrainType) {
        Set<String> terrains = terrainType.getTerrains();
        double select = terrains.size() * Math.random();
        int i=1;
        for(String terrain:terrains) {
            if(select < i) return terrain;
            i++;
        }
        return null;
    }
}