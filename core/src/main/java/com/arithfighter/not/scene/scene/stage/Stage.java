package com.arithfighter.not.scene.scene.stage;

import com.arithfighter.not.TextureService;
import com.arithfighter.not.WindowSetting;
import com.arithfighter.not.audio.SoundManager;
import com.arithfighter.not.entity.*;
import com.arithfighter.not.entity.player.CharacterList;
import com.arithfighter.not.CursorPositionAccessor;
import com.arithfighter.not.entity.player.PlayerService;
import com.arithfighter.not.font.Font;
import com.arithfighter.not.font.FontService;
import com.arithfighter.not.pojo.Recorder;
import com.arithfighter.not.scene.MouseEvent;
import com.arithfighter.not.scene.SceneComponent;
import com.arithfighter.not.scene.SceneEvent;
import com.arithfighter.not.time.TimeHandler;
import com.arithfighter.not.widget.button.SceneControlButton;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Stage extends SceneComponent implements SceneEvent, MouseEvent {
    private final PlayerService playerService;
    private final GamePlayComponent gamePlayComponent;
    private final SceneControlButton pauseButton;
    private final PauseMenu pauseMenu;
    private final StageMessage stageMessage;
    private int numberBoxQuantity;
    private final CardLimitManager cardLimitManager;

    public Stage(TextureService textureService, SoundManager soundManager, FontService fontService) {
        Texture[] textures = textureService.getTextures(textureService.getKeys()[0]);
        Texture[] cards = textureService.getTextures(textureService.getKeys()[1]);

        gamePlayComponent = new GamePlayComponent(textureService, soundManager, fontService.getFont32());

        cardLimitManager = new CardLimitManager(fontService.getFont22());

        pauseMenu = new PauseMenu(textures, soundManager, fontService.getFont20());

        playerService = new PlayerService();
        playerService.setCharacterQuantity(CharacterList.values().length);
        playerService.setNumberBoxDisplacer(gamePlayComponent.getNumberBoxDisplacer());
        playerService.setPlayRecord(cardLimitManager.getPlayRecord());
        playerService.setSumBoxModel(gamePlayComponent.getSumBoxModel());
        playerService.createPlayers(textures,cards);

        pauseButton = new SceneControlButton(textures[6], 1.8f);
        pauseButton.getButton().setPosition(1000, 600);
        pauseButton.getButton().setFont(fontService.getFont22());

        stageMessage = new StageMessage(450, 500, fontService.getFont45()) {
            @Override
            public boolean isExceedCardLimitAndStageNotComplete() {
                return cardLimitManager.isExceedCardLimit() && !gamePlayComponent.getNumberBoxDisplacer().isAllNumZero();
            }

            @Override
            public boolean isStageComplete() {
                return gamePlayComponent.getNumberBoxDisplacer().isAllNumZero();
            }
        };
    }

    public void setCardLimit(int limit) {
        cardLimitManager.setCardLimit(limit);
    }

    public PauseMenu getPauseMenu() {
        return pauseMenu;
    }

    public StageMessage getStageMessage() {
        return stageMessage;
    }

    public CardLimitManager getCardLimitManager() {
        return cardLimitManager;
    }

    public void init() {
        gamePlayComponent.init();
        pauseMenu.init();
        pauseButton.init();
        stageMessage.init();
    }

    public void setNumberBoxQuantity(int quantity) {
        numberBoxQuantity = quantity;
    }

    private void update() {
        if (pauseButton.isStart()) {
            pauseMenu.update();

            if (pauseMenu.isResume()) {
                pauseButton.init();
                pauseMenu.init();
            }
        } else {
            pauseButton.update();

            gamePlayComponent.setNumberQuantity(numberBoxQuantity);

            gamePlayComponent.update(getCursorPos().getX(), getCursorPos().getY());
        }
    }

    public void render() {
        update();

        SpriteBatch batch = getBatch();
        if (stageMessage.isNeutral()) {
            cardLimitManager.draw(batch);

            gamePlayComponent.setBatch(batch);
            gamePlayComponent.draw();

            if (pauseButton.isStart()){
                pauseMenu.draw(batch);
            }
            else
                pauseButton.getButton().draw(batch, "Pause");
        }

        stageMessage.draw(batch);
    }

    public void setSelectedPlayerToGame(int i) {
        gamePlayComponent.setPlayer(playerService.getPlayers()[i]);
    }

    public void touchDown() {
        CursorPositionAccessor cursorPos = getCursorPos();
        int x = cursorPos.getX();
        int y = cursorPos.getY();

        if (isPlaying() && stageMessage.isNeutral()) {
            if (pauseButton.isStart())
                pauseMenu.touchDown(x, y);
            else {
                pauseButton.getButton().on(x, y);
                gamePlayComponent.touchDown(x, y);
            }
        }
    }

    public void touchDragged() {
        if (isPlaying() && stageMessage.isNeutral()) {
            if (pauseButton.isStart())
                pauseMenu.touchDragged();
            else {
                pauseButton.getButton().off();
                gamePlayComponent.touchDragged(getCursorPos().getX(), getCursorPos().getY());
            }
        }
    }

    public void touchUp() {
        if (isPlaying() && stageMessage.isNeutral()) {
            if (pauseButton.isStart())
                pauseMenu.touchUp();
            else {
                pauseButton.getButton().off();
                gamePlayComponent.touchUp(getCursorPos().getX(), getCursorPos().getY());
            }
        }
    }

    private boolean isPlaying() {
        return !stageMessage.isStageComplete() && !stageMessage.isExceedCardLimitAndStageNotComplete();
    }
}

