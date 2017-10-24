package br.cefetmg.games.collision;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Utilitário para verificação de colisão.
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class Collision {

    /**
     * Verifica se dois círculos em 2D estão colidindo.
     * @param c1 círculo 1
     * @param c2 círculo 2
     * @return true se há colisão ou false, do contrário.
     */
    
    public static final boolean circlesOverlap(Circle c1, Circle c2) {
        float distance = c1.radius+c2.radius;
        
        Vector2 v1 = new Vector2(c1.x, c1.y);
        Vector2 v2 = new Vector2(c2.x, c2.y);
        
        if(v1.dst(v2) < distance ){
            return true;
        }            
        else
            return false;
    }

    /**
     * Verifica se dois retângulos em 2D estão colidindo.
     * Esta função pode verificar se o eixo X dos dois objetos está colidindo
     * e então se o mesmo ocorre com o eixo Y.
     * @param r1 retângulo 1
     * @param r2 retângulo 2
     * @return true se há colisão ou false, do contrário.
     */
    public static final boolean rectsOverlap(Rectangle r1, Rectangle r2) {
        
        float distance1 = r1.width/2 + r2.width/2;
        float distance2 = r1.height/2 + r2.height/2;
        
        Vector2 v1 = new Vector2(r1.x, r1.y);
        Vector2 v2 = new Vector2(r2.x, r2.y);
        
        if(v1.dst(v2)<distance1 && v1.dst(v2)<distance2){
            return true;
        }            
        else
            return false;
    }
}
