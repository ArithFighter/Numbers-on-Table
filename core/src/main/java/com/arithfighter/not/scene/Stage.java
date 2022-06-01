package com.arithfighter.not.scene;

import com.arithfighter.not.TextureService;
import com.arithfighter.not.WindowSetting;
import com.arithfighter.not.audio.SoundManager;
import com.arithfighter.not.entity.*;
import com.arithfighter.not.entity.player.CharacterList;
import com.arithfighter.not.CursorPositionAccessor;
import com.arithfighter.not.font.Font;
import com.arithfighter.not.font.FontService;
import com.arithfighter.not.pojo.Recorder;
import com.arithfighter.not.time.TimeHandler;
import com.arithfighter.not.widget.button.SceneControlButton;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Stage extends SceneComponent implements SceneEvent, MouseEvent {
    private final PlayerCollection playerCollection;
    private final GamePlayComponent gamePlayComponent;
    private final SceneControlButton pauseButton;
    private final PauseMenu pauseMenu;
    private final StageMessage stageMessage;
    private int numberBoxQuantity;
    private final CardLimitManager cardLimitManager;

    public Stage(TextureService textureService, SoundManager soundManager, FontService fontService) {
        Texture[] textures = textureService.getTextures(textureService.getKeys()[0]);
        Texture[] cards = textureService.getTextures(textureService.getKeys()[1]);

        cardLimitManager = new CardLimitManager(fontService.getFont22());

        gamePlayComponent = new GamePlayComponent(textureService, soundManager, fontService.getFont32());

        pauseMenu = new PauseMenu(textures, soundManager, fontService.getFont20());

        playerCollection = new PlayerCollection(textures, cards);
        playerCollection.setCharacterQuantity(CharacterList.values().length);
        playerCollection.setNumberBoxDisplacer(gamePlayComponent.getNumberBoxDisplacer());
        playerCollection.setPlayRecord(cardLimitManager.getPlayRecord());

        pauseButton = new SceneControlButton(textures[6], 1.8f);
        pauseButton.getButton().setPosition(1000, 600);
        pauseButton.getButton().setFont(fontService.getFont22());

        stageMessage = new StageMessage(450, 500) {
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
        gamePlayComponent.setPlayer(playerCollection.getPlayers()[i]);
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

    public void dispose() {
    }
}

class CardLimitManager {
    private final Recorder playRecord;
    private final Font cardLimitText;
    private int cardLimit;

    public CardLimitManager(Font font) {
        cardLimitText = font;

        playRecord = new Recorder();
    }

    public void setCardLimit(int cardLimit) {
        this.cardLimit = cardLimit;
    }

    public Recorder getPlayRecord() {
        return playRecord;
    }

    public void draw(SpriteBatch batch) {
        cardLimitText.setColor(Color.WHITE);
        cardLimitText.draw(
                batch,
                "cards: " + (cardLimit - playRecord.getRecord()),
                WindowSetting.GRID_X * 8,
                WindowSetting.GRID_Y * 8 + WindowSetting.CENTER_Y);
    }

    public boolean isExceedCardLimit() {
        return playRecord.getRecord() >= cardLimit;
    }
}

class StageMessage {
    private final Font text;

    enum State {WIN, LOSE, NEUTRAL, READY}

    private State state = State.READY;
    private final TimeHandler transitionHandler;
    private final float x;
    private final float y;

    public StageMessage(float x, float y) {
        this.x = x;
        this.y = y;
        text = new Font(45);
        text.setColor(Color.WHITE);

        transitionHandler = new TimeHandler();
    }

    public final boolean isNeutral() {
        return state == State.NEUTRAL;
    }

    public final boolean isWin() {
        return state == State.WIN;
    }

    public final boolean isLose() {
        return state == State.LOSE;
    }

    public final void init() {
        state = State.READY;
        transitionHandler.resetPastedTime();
    }

    public final void draw(SpriteBatch batch) {
        if (state == State.READY)
            showReady(batch);

        if (isStageComplete() || isExceedCardLimitAndStageNotComplete())
            showEnd(batch);
    }

    private void showReady(SpriteBatch batch) {
        transitionHandler.updatePastedTime();

        float r = 1.5f;
        float a = 2.5f;

        if (transitionHandler.getPastedTime() < r)
            text.draw(batch, "Game Ready", x, y);
        if (transitionHandler.getPastedTime() > r && transitionHandler.getPastedTime() < a)
            text.draw(batch, "Action", x, y);
        if (transitionHandler.getPastedTime() > a)
            state = State.NEUTRAL;
    }

    private void showEnd(SpriteBatch batch) {
        transitionHandler.updatePastedTime();

        float time = 5f;
        if (transitionHandler.getPastedTime() < time)
            text.draw(batch, getMessage(), x, y);
        else {
            setFinalState();
        }
    }

    private String getMessage() {
        String message = "";
        if (isStageComplete())
            message = "Complete";
        if (isExceedCardLimitAndStageNotComplete())
            message = "Exceed limit";
        return message;
    }

    private void setFinalState() {
        if (isStageComplete())
            state = State.WIN;
        if (isExceedCardLimitAndStageNotComplete())
            state = State.LOSE;
    }

    public boolean isExceedCardLimitAndStageNotComplete() {
        return false;
    }

    public boolean isStageComplete() {
        return false;
    }

    public final void dispose() {
        text.dispose();
    }
}