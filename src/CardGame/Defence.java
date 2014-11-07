package CardGame;

/**
 * Created with IntelliJ IDEA.
 * User: Kristján Tryggvason
 * Date: 24.3.2014
 * Time: 22:17
 * To change this template use File | Settings | File Templates.
 */
public class Defence {
    private int defenceCoverage, defenceValue, defenceLayer;
    private String defenceType;

    Defence(int dc, int dv, int dl, String dt) {
        defenceCoverage = dc; defenceValue = dv; defenceLayer = dl; defenceType = dt;
    }
    Defence(Defence defence) {
        defenceCoverage = defence.getCoverage(); defenceValue = defence.getDefence();
        defenceLayer = defence.getLayer(); defenceType = defence.getType();
    }

    public int getCoverage() { return defenceCoverage; }
    public int getDefence() { return defenceValue; }
    public int getLayer() { return defenceLayer; }
    public String getType() { return defenceType; }

    public void setDefence(int defense) { defenceValue = defense; }
}
