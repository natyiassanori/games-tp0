/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.weapons;

import br.cefetmg.games.collision.Collidable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

/**
 *
 * @author NataliaNatsumy
 */
public class Projetil extends AnimatedSprite {
    
    static final int FRAME_WIDTH = 16;
        static final int FRAME_HEIGHT = 19;
        TextureRegion[][] quadrosDaAnimacao;
        Texture spriteSheet;
        
        float tempoDaAnimacao;
        
        public float px;
        public float py;
        
        Animation play; 
        
        int x = 0;
        
        public Projetil(final Texture proj) {
            
            super(new Animation(0.1f, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            proj, 16, 19);
                    super.addAll(new TextureRegion[]{
                        frames[0][2]
                    });
                }
            }));
            
            this.px=px;
            this.py=py;
            quadrosDaAnimacao = TextureRegion.split(proj, 16, 19);
            
            play = new Animation(0.1f,
            quadrosDaAnimacao[0][0], 
            quadrosDaAnimacao[0][1], 
            quadrosDaAnimacao[0][2]);
            
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
            
        }

       
        Vector2 getHeadPosition() {
            return new Vector2(
                    this.getX() + this.getWidth() * 0.5f,
                    this.getY() + this.getHeight() * 0.8f);
        }

        float getHeadDistanceTo(float enemyX, float enemyY) {
            return getHeadPosition().dst(enemyX, enemyY);
        }
            
        public void update(float x, float y){
            
            tempoDaAnimacao += Gdx.graphics.getDeltaTime();
            px = (float) (px*(0.45) + 10);
            py = (float) (px*(0.45) + 10);
        
        }
}

