/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 *
 * @author NataliaNatsumy
 */
public class GameScreen implements Screen{
      
      MyGame mg;
      
      
      SpriteBatch batch;
      Sprite s;
      
      
      
      float timeStep=1f/60f;
      float turretX=10f;
      float turretY=10f;
      float bulletSpeed=0.1f; 
      
      float bulletX=turretX;
      float bulletY=turretY;
      
      float dirX= 100 - turretX;
      float dirY= 100 - turretY;
      
      public GameScreen(MyGame mg) {
         this.mg = mg;
         
         
         float dirLength= (float) Math.sqrt(dirX*dirX + dirY*dirY);
         dirX=dirX/dirLength;
         dirY=dirY/dirLength;
         
         
         
         
      }

      @Override
      public void render(float delta) {
         Gdx.gl.glClearColor(1F, 1F, 1F, 1F);
          Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
         update(delta);
         
         batch.begin();
            batch.draw(s, bulletX, bulletY);
         batch.end();
         
      }
      
      public void update(float delta) {
         if(Gdx.input.isKeyPressed(Keys.SPACE)) {
         dirX= Gdx.input.getX() - turretX;
         dirY= Gdx.input.getY() - turretY;
         }
         
         bulletX=bulletX+(dirX*bulletSpeed*timeStep);
         bulletY=bulletY+(dirY*bulletSpeed*timeStep);
         
         
         
      }

      @Override
      public void resize(int width, int height) {
         // TODO Auto-generated method stub
         
      }

      @Override
      public void show() {
         batch = new SpriteBatch();
          s = new Sprite(new Texture(Gdx.files.internal("HUDS/button_Back.png")));
          
      }

      @Override
      public void hide() {
         // TODO Auto-generated method stub
         
      }

      @Override
      public void pause() {
         // TODO Auto-generated method stub
         
      }

      @Override
      public void resume() {
         // TODO Auto-generated method stub
         
      }

      @Override
      public void dispose() {
         // TODO Auto-generated method stub
         
      }

   }