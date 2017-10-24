package br.cefetmg.games.minigames.weapons;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Uma arma que cria tiros de vórtice.
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class VortexWeapon implements Weapon {

    private final Vector2 origin;

    public VortexWeapon(Vector2 origin) {
        this.origin = origin;
    }

    /*public VortexWeapon(Vector2 vector2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public VortexWeapon(Vector2 vector2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public VortexWeapon(Vector2 vector2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public VortexWeapon(Vector2 vector2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/

    public Array<Shot> createShot(Vector2 position) {
        return new Array<Shot>(new Shot[]{
            new VortexShot(new Vector2(origin).add(position))
        });
    }

    public long getCadenceInMillis() {
        return 600;
    }
}
