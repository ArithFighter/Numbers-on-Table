package com.arithfighter.ccg;

import com.arithfighter.ccg.widget.Desk;
import com.arithfighter.ccg.widget.Hand;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameCore {
    AssetManager assetManager = new AssetManager();
    CounterAssetsManager myAssetManager = new CounterAssetsManager();
    FileLibrary fileLibrary = new FileLibrary();
    AUD aud;
    Hand hand;
    Desk desk;
    int mouseX;
    int mouseY;
    SpriteBatch batch;
    Texture[] textures;

    int score = 0;

    InputAdapter mouseAdapter = new InputAdapter() {
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            hand.checkActive(mouseX,mouseY);
            return true;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            hand.updateWhenDrag(mouseX,mouseY);
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            if (desk.isOnDesk(mouseX,mouseY)){
                if (hand.isCardActive())
                    score++;
            }
            hand.resetHand();
            return true;
        }
    };

    public void create() {
        myAssetManager.loadTexture(assetManager, fileLibrary.getTextureFile());
        assetManager.finishLoading();

        aud = new AUD();

        storeTextures();

        hand = new Hand(textures[0]);

        batch = new SpriteBatch();

        Gdx.input.setInputProcessor(mouseAdapter);

        desk = new Desk(textures[1], 18);
    }

    private void storeTextures(){
        textures = new Texture[fileLibrary.getTextureFile().length];
        for (int i = 0; i<fileLibrary.getTextureFile().length;i++)
            textures[i] = assetManager.get(fileLibrary.getTextureFile()[i]);
    }

    public void render() {
        assetManager.update(17);

        updateMousePosition();

        batch.begin();
        drawComponent();
        batch.end();
    }

    private void updateMousePosition() {
        mouseX = Gdx.input.getX();
        mouseY = Gdx.graphics.getHeight()-Gdx.input.getY();
    }

    private void drawComponent() {
        desk.draw(batch);
        hand.draw(batch);
        hand.checkTouchingCard(mouseX,mouseY);
        aud.showData(mouseX,mouseY, batch);
        aud.showScore(String.valueOf(score), batch);
    }

    public void dispose() {
        hand.dispose();
        aud.dispose();
        batch.dispose();
    }
}
