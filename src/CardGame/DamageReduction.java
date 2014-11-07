package CardGame;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kristján Tryggvason
 * Date: 8.3.2014
 * Time: 21:22
 * To change this template use File | Settings | File Templates.
 */
public class DamageReduction {
    private String type;
    private int reduction;

    DamageReduction(String _type, int _reduction) { type = _type; reduction = _reduction; }
    DamageReduction(DamageReduction damageReduction) {
        type = damageReduction.getType(); reduction = damageReduction.getReduction();
    }

    public boolean isType(List<String> _type) { if(_type.contains(type)) return true; else return false; }
    public String getType() { return type; }
    public float reduction(float attack) { return attack*((100-reduction)/100); }
    public int getReduction() { return reduction; }
}
