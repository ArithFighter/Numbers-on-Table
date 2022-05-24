package com.arithfighter.not.scene;

import com.arithfighter.not.CursorPositionAccessor;
import com.arithfighter.not.TextureManager;
import com.arithfighter.not.audio.SoundManager;
import com.arithfighter.not.font.Font;
import com.arithfighter.not.widget.ControlBar;
import com.arithfighter.not.widget.button.SceneControlButton;
import com.arithfighter.not.pojo.TextProvider;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class OptionMenu extends SceneComponent implements SceneEvent, MouseEvent {
    private final SceneControlButton leaveButton;
    private final ControlBar soundControl;
    private final ControlBar musicControl;
    private final SoundManager soundManager;
    private final TextProvider textProvider;
    private GameScene sceneTemp;
    private final Font buttonFont;
    private final Font controlFont;

    public OptionMenu(TextureManager textureManager, SoundManager soundManager){
        Texture[] textures = textureManager.getTextures(textureManager.getKeys()[0]);
        this.soundManager = soundManager;

        textProvider = new TextProvider();

        buttonFont = new Font(22);
        buttonFont.setColor(Color.WHITE);

        controlFont = new Font(22);
        controlFont.setColor(Color.WHITE);

        leaveButton = new SceneControlButton(textures[6], 1.8f);
        leaveButton.getButton().setFont(buttonFont);
        leaveButton.getButton().setPosition(500,120);

        soundControl = new ControlBar(textures, 6);
        soundControl.setFont(controlFont);
        soundControl.setPosition(500,600);

        musicControl = new ControlBar(textures, 6);
        musicControl.setFont(controlFont);
        musicControl.setPosition(500,400);

    }

    public void setSoundVolume(int i){
        soundControl.setValue(i);
    }

    public void setMusicVolume(int i){
        musicControl.setValue(i);
    }

    public GameScene getSceneTemp(){
        return sceneTemp;
    }

    public void setSceneTemp(GameScene scene){
        sceneTemp = scene;
    }

    public int getSoundVolume(){
        return soundControl.getValue();
    }

    public int getMusicVolume(){
        return musicControl.getValue();
    }

    public void draw() {
        String[] texts = textProvider.getOptionMenuTexts();
        SpriteBatch batch = getBatch();

        soundControl.draw(batch,texts[0]);
        musicControl.draw(batch,texts[1]);
        leaveButton.getButton().draw(batch, texts[2]);
    }

    public void update() {
        soundControl.update();
        musicControl.update();
        leaveButton.update();
    }

    public void init() {
        leaveButton.init();
    }

    public boolean isLeaving() {
        return leaveButton.isStart();
    }

    public void touchDown() {
        CursorPositionAccessor cursorPos = getCursorPos();
        int x= cursorPos.getX();
        int y = cursorPos.getY();

        soundControl.activate(x, y);
        musicControl.activate(x, y);
        leaveButton.getButton().on(x, y);
    }

    public void touchDragged(){
        soundControl.deactivate();
        musicControl.deactivate();
        leaveButton.getButton().off();
    }

    public void touchUp() {
        if (soundControl.isButtonActive()|| musicControl.isButtonActive())
            soundManager.playTouchedSound();

        if (leaveButton.getButton().isOn())
            soundManager.playReturnSound();

        soundControl.deactivate();
        musicControl.deactivate();
        leaveButton.getButton().off();
    }

    public void dispose() {
        buttonFont.dispose();
        controlFont.dispose();
    }
}
