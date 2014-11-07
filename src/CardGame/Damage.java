package CardGame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kristján Tryggvason
 * Date: 30.3.2014
 * Time: 19:52
 * To change this template use File | Settings | File Templates.
 */
public class Damage {
    List<String> type = new ArrayList<String>();
    float damage;

    Damage(List<String> _type, int _damage) { type = _type; damage = _damage; }
    Damage(Damage _damage) { type = Support.copyStrings(_damage.getType()); damage = _damage.getDamage(); }

    public List<String> getType() { return type; }
    public float getDamage() { return damage; }
    public void setDamage(float newDamage) { damage = newDamage; }
    public void addType(String string) { type.add(string); }
    public void removeType(String string) { type.remove(string); }
}
