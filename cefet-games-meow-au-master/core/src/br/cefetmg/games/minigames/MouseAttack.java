package br.cefetmg.games.minigames;

import br.cefetmg.games.Config;
import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.minigames.weapons.Projetil;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;
import br.cefetmg.games.minigames.weapons.Shot;
import br.cefetmg.games.minigames.weapons.VortexWeapon;
import br.cefetmg.games.minigames.weapons.Weapon;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

/**
 *
 * @author
 */
public class MouseAttack extends MiniGame {

    
    private Array<Sprite> enemies;
    private Texture catTexture;
    private Cat2 cat;
    private Projetil projetil;

    private Texture monsterTexture;
    private Texture projetiltex;
    private Monster monster;

    private int enemiesKilled;
    private int spawnedEnemies;
    private Sprite target;
    private Texture cariesTexture;
    private Texture targetTexture;
    private Sound cariesAppearingSound;
    private Sound cariesDyingSound;
    private float initialEnemyScale;
    private float minimumEnemyScale;
    private int totalEnemies;
    private float spawnInterval;
    private float posX, posY;
    private float ScreenWidth;
    private float ScreenHeight;
    private final Array<Weapon> weapons;
    private int currentWeaponIndex;
    private long lastShotMillis;
    private final float speed;
    private final float scale;
    private Array<Shot> shots;
    private Array<Monster> monsters;
    private ShapeRenderer shapeRenderer;
    
    private static final int NUMBER_OF_TILED_BACKGROUND_TEXTURE = 7;
    private TextureRegion background;
    private boolean desenhapro=false;
    private Array<Projetil> aproj;
    
    private static final float[] VERTICES = new float[]{
        1.00f, 0.00f,
        0.50f, 0.85f, // origem do canhão laser direito
        0.25f, 0.15f,
        0.00f, 0.15f, // origem do canhão vortex
        -0.25f, 0.15f,
        -0.50f, 0.85f, // origem do canhão laser esquerdo
        -1.00f, 0.00f,
        -0.50f, -0.70f,
        0.00f, -0.50f,
        0.50f, -0.70f
    };

    private final Vector2 position;
    
    public MouseAttack(BaseScreen screen,
                       MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f,
                TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
        
        speed = 100;
        scale = 20;
        position = new Vector2(50,50);
        monsters = new Array<Monster>();
        
        weapons = new Array<Weapon>(new Weapon[]{
            new VortexWeapon(
            new Vector2(VERTICES[3 * 2] * scale, VERTICES[3 * 2 + 1] * scale)
            )
        });
    }

    @Override
    protected void onStart() {
        
        enemies = new Array<Sprite>();
        
        shapeRenderer = new ShapeRenderer();
        catTexture = assets.get("MouseAttack/sprite-cat.png",Texture.class);
        projetiltex = assets.get("MouseAttack/projetil.png",Texture.class);
        monsterTexture = assets.get("MouseAttack/sprite-mouse.png", Texture.class);

        cat = new Cat2(catTexture);
        enemies = new Array<Sprite>();
        
        
        
        //projetil = new Projetil(projetiltex);
        
        ScreenHeight = Gdx.graphics.getHeight();
        ScreenWidth = Gdx.graphics.getWidth();
        posX = (float)(ScreenWidth*0.2);
        posY = (float)(ScreenHeight*0.5);
        
        
        background = new TextureRegion(new Texture("menu-background.png"));
        // configura a textura para repetir caso ela ocupe menos espaço que o
        // espaço disponível
        background.getTexture().setWrap(
                Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        // define a largura da região de desenho de forma que ela seja repetida
        // um número de vezes igual a NUMBER_OF_TILED_BACKGROUND_TEXTURE 
        background.setRegionWidth(
                background.getTexture().getWidth()
                * NUMBER_OF_TILED_BACKGROUND_TEXTURE);
        // idem para altura, porém será repetida um número de vezes igual a 
        // NUMBER_OF_TILED_BACKGROUND_TEXTURE * razãoDeAspecto
        background.setRegionHeight(
                (int) (background.getTexture().getHeight()
                * NUMBER_OF_TILED_BACKGROUND_TEXTURE
                / Config.DESIRED_ASPECT_RATIO));
        
        
        //cat.setPosition(ScreenWidth/2, ScreenHeight/2);
        
       
        
        lastShotMillis = 0;
        shots = new Array<Shot>();
        aproj=new Array<Projetil>();
        
        enemiesKilled = 0;
        spawnedEnemies = 0;
        scheduleEnemySpawn();

    }

    Array<Shot> shoot() {
        
        if (TimeUtils.timeSinceMillis(lastShotMillis)
                >= weapons.get(0).getCadenceInMillis()) {
            lastShotMillis = TimeUtils.millis();
            
            shots = weapons.get(0).createShot(position);
        }
        return shots;
    }
    
  
    private void scheduleEnemySpawn() {
       Task t = new Task() {
            @Override
            public void run() {
                spawnEnemy();
                if (++spawnedEnemies < totalEnemies) {
                    System.out.println("here2");
                    scheduleEnemySpawn();
                }
            }
        };
        // spawnInterval * 15% para mais ou para menos
        float nextSpawnMillis = this.spawnInterval
                * (rand.nextFloat() / 3 + 0.15f);
        timer.scheduleTask(t, nextSpawnMillis);
    }

    int mul = 1;
    private void spawnEnemy() {
        // pega x e y entre 0 e 1
        Vector2 position = new Vector2(rand.nextFloat()-mul*(float)0.7, rand.nextFloat());
        mul=mul*(-1);
        // multiplica x e y pela largura e altura da tela
        position.scl(
                viewport.getWorldWidth() - monsterTexture.getWidth()
                * initialEnemyScale,
                viewport.getWorldHeight()
                - monsterTexture.getHeight() * initialEnemyScale);

        Sprite enemy = new Sprite(monsterTexture);
        enemy.setPosition(position.x, position.y);
        enemy.setScale(initialEnemyScale);
        enemies.add(enemy);

    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.initialEnemyScale = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 1.15f, 0.8f);
        this.minimumEnemyScale = DifficultyCurve.LINEAR_NEGATIVE
                .getCurveValueBetween(difficulty, 0.15f, 0.4f);
        this.spawnInterval = DifficultyCurve.S_NEGATIVE
                .getCurveValueBetween(difficulty, 0.5f, 1.5f);
        this.totalEnemies = (int) Math.ceil(maxDuration / spawnInterval) - 3;
    }

    private Vector2 posproj;
    @Override
    public void onHandlePlayingInput() { 
        Vector3 Posi;
        Posi = new Vector3(posX, posY, 0);
        viewport.unproject(Posi);
        cat.setCenter(viewport.getWorldWidth() / 2f, viewport.getWorldHeight()/ 2f);
       
        if(Gdx.input.isButtonPressed(Buttons.LEFT)){
                 Gdx.input.setInputProcessor(new InputAdapter() {
                 public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                     if (button == Buttons.LEFT) {
                        projetil = new Projetil(projetiltex);
                        aproj.add(projetil);
                        desenhapro=true; 
                        posproj = new Vector2(50, 50);
                        return true;
                     }
                     return false;
                 }
             });
           }
        
        
/*
        // atualiza a posição do alvo de acordo com o mouse
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(click);
        this.target.setPosition(click.x - this.target.getWidth() / 2,
                click.y - this.target.getHeight() / 2);

        // verifica se matou um inimigo
        if (Gdx.input.justTouched()) {
            // itera no array de inimigos
            for (int i = 0; i < enemies.size; i++) {
                Sprite sprite = enemies.get(i);
                // se há interseção entre o retângulo da sprite e do alvo,
                // o tiro acertou
                if (sprite.getBoundingRectangle().overlaps(
                        target.getBoundingRectangle())) {
                    // contabiliza um inimigo morto
                    this.enemiesKilled++;
                    // remove o inimigo do array
                    this.enemies.removeValue(sprite, true);
                    cariesDyingSound.play();
                    // se tiver matado todos os inimigos, o desafio
                    // está resolvido
                    if (this.enemiesKilled >= this.totalEnemies) {
                        super.challengeSolved();
                    }

                    // pára de iterar, porque senão o tiro pode pegar em mais
                    // de um inimigo
                    break;
                }
            }
        }*/
    }

    @Override
    public void onUpdate(float dt) {
        cat.update(dt);
        cat.update();
        if(desenhapro)
            projetil.update();
        if(Gdx.input.isButtonPressed(Buttons.LEFT)){
                 Gdx.input.setInputProcessor(new InputAdapter() {
                 public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                     if (button == Buttons.LEFT) {
                        projetil = new Projetil(projetiltex);
                        aproj.add(projetil);
                        desenhapro=true; 
                        posproj = new Vector2(50, 50);
                        return true;
                     }
                     return false;
                 }
             });
        }
        if(desenhapro)
            for(int i=0;i<aproj.size;i++)
                aproj.get(i).update();
        // vai diminuindo o tamanho das cáries existentes
        /*for (int i = 0; i < enemies.size; i++) {
            Sprite sprite = enemies.get(i);
            // diminui só até x% do tamanho da imagem
            if (sprite.getScaleX() > minimumEnemyScale) {
                sprite.setScale(sprite.getScaleX() - 0.3f * dt);
            }
        }*/
    }

    @Override
    public String getInstructions() {
        return "Mate todos os monstros!";
    }

    @Override
    public void onDrawGame() {
        for (int i = 0; i < enemies.size; i++) {
            Sprite sprite = enemies.get(i);
            sprite.draw(batch);
        }
        cat.draw(batch);
        if(desenhapro){
            for(int i=0; i<aproj.size;i++){
                aproj.get(i).draw(batch);
                aproj.get(i).setCenter(aproj.get(i).px, aproj.get(i).py);
            }
        }
            
       /*for (int i = 0; i < enemies.size; i++) {
            Sprite sprite = enemies.get(i);
            sprite.draw(batch);
        }
        target.draw(batch);*/
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }
    
    class Cat2 extends AnimatedSprite{
        
        static final int FRAME_WIDTH = 50;
        static final int FRAME_HEIGHT = 50;
        TextureRegion[][] quadrosDaAnimacao;
        Texture spriteSheet;
        float sx;
        float sy;
        
        float tempoDaAnimacao;
        
        Animation power;        
        Animation socar;
        Animation chutar;
        Animation morrer;
        Animation parado;
        
        int x = 0;
        
        public Cat2(final Texture cat) {
            
             super(new Animation(0.1f, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            cat, 50, 50);
                    super.addAll(new TextureRegion[]{
                        frames[4][0]
                    });
                }
            }));
            
            quadrosDaAnimacao = TextureRegion.split(cat, 50, 50);
            
            chutar = new Animation(0.1f,
            quadrosDaAnimacao[4][0], 
            quadrosDaAnimacao[4][1], 
            quadrosDaAnimacao[4][2],
            quadrosDaAnimacao[4][3],
            quadrosDaAnimacao[4][4],
            quadrosDaAnimacao[4][5],
            quadrosDaAnimacao[4][6],
            quadrosDaAnimacao[4][7],
            quadrosDaAnimacao[4][8],
            quadrosDaAnimacao[4][9]);
            
            power = new Animation(0.1f,
            quadrosDaAnimacao[1][0], 
            quadrosDaAnimacao[1][1], 
            quadrosDaAnimacao[1][2],
            quadrosDaAnimacao[1][3],
            quadrosDaAnimacao[1][4],
            quadrosDaAnimacao[1][5]);
            
            morrer = new Animation(0.1f,
            quadrosDaAnimacao[3][0], 
            quadrosDaAnimacao[3][1], 
            quadrosDaAnimacao[3][2],
            quadrosDaAnimacao[3][3]);
            
            parado = new Animation(0.1f,
            quadrosDaAnimacao[0][0]);
            
            socar = new Animation(0.1f,
            quadrosDaAnimacao[5][5], 
            quadrosDaAnimacao[5][6], 
            quadrosDaAnimacao[5][7],
            quadrosDaAnimacao[5][8],
            quadrosDaAnimacao[5][9]);
            
            
            //super.getAnimation().setPlayMode(Animation.PlayMode.NORMAL);
            //chutar.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
            //super.setAutoUpdate(false);
            //this.cat = cat;
        }

       
        Vector2 getHeadPosition() {
            return new Vector2(
                    this.getX() + this.getWidth() * 0.5f,
                    this.getY() + this.getHeight() * 0.8f);
        }

        float getHeadDistanceTo(float enemyX, float enemyY) {
            return getHeadPosition().dst(enemyX, enemyY);
        }
        
        public void changeAnimation(){
            
            x++;
            
            if(x%2==0)
                super.setAnimation(power);
            else
                super.setAnimation(parado);
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
            
        }
        public void update(){
            
            tempoDaAnimacao += Gdx.graphics.getDeltaTime();
            
            if(desenhapro){
                for(int i=0;i<aproj.size;i++){
                     aproj.get(i).update(sx, sy);
                 }
            }
                 
           if(Gdx.input.isButtonPressed(Buttons.LEFT)){
                 Gdx.input.setInputProcessor(new InputAdapter() {
                 public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                     if (button == Buttons.LEFT) {
                        changeAnimation();                         
                        projetil = new Projetil(projetiltex);
                        
                        projetil.px = viewport.getWorldWidth() / 2f;
                        projetil.py = viewport.getWorldHeight()/ 2f;
                        sx=screenX;
                        sy=screenY;
                        
                        aproj.add(projetil);
                        desenhapro=true; 
                        
                        
                        return true;
                     }
                     return false;
                 }
             });
           }
           
            
        /*if (Gdx.input.) {
            super.setAnimation(power);
                  
        }
        else{
        }*/
        }
    }

}
