package com.arithfighter.not.entity;

import com.arithfighter.not.TextureService;
import com.arithfighter.not.animate.AnimationPos;
import com.arithfighter.not.animate.se.SpecialEffect;
import com.arithfighter.not.audio.SoundManager;
import com.arithfighter.not.card.CardAnimate;
import com.arithfighter.not.card.CardAnimationService;
import com.arithfighter.not.entity.numberbox.NumberBoxEntity;
import com.arithfighter.not.entity.player.Player;
import com.arithfighter.not.entity.sumbox.SumBoxEntity;
import com.arithfighter.not.entity.sumbox.SumBoxModel;
import com.arithfighter.not.font.Font;
import com.arithfighter.not.gecko.*;
import com.arithfighter.not.pojo.Point;
import com.arithfighter.not.widget.bar.EnergyBar;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GamePlayComponent {
    private final NumberBoxEntity numberBoxEntity;
    private Player player;
    private CardAnimate cardAnimate;
    private boolean isCardDragging = false;
    private SpriteBatch batch;
    private boolean isReadyToResetSum = false;
    private GeckoEntity gecko;
    private final SumBoxEntity sumBoxEntity;

    public GamePlayComponent(TextureService textureService, SoundManager soundManager, Font font) {
        Texture[] textures = textureService.getTextures(textureService.getKeys()[0]);
        Texture[] spriteSheets = textureService.getTextures(textureService.getKeys()[3]);

        createCardAnimate(spriteSheets);

        numberBoxEntity = new NumberBoxEntity(textures, font) {
            @Override
            public void doWhenSumAndNumMatched() {
                soundManager.playScoreSound();
            }
        };

        createGecko(spriteSheets);

        sumBoxEntity = new SumBoxEntity(textures[2], font);
    }

    private void createCardAnimate(Texture[] spriteSheets){
        CardAnimationService cas = new CardAnimationService(spriteSheets);

        cardAnimate = new CardAnimate(cas.getVisualEffects());
        for (SpecialEffect ve: cardAnimate.getVisualEffects())
            ve.setScale(16);
    }

    public void createGecko(Texture[] spriteSheets){
        GeckoSprite geckoSprite = new GeckoSprite(spriteSheets) {
            @Override
            public void initCardPosition() {
                cardAnimate.getCardReset().setStart();
                player.initHand();
            }

            @Override
            public void checkCardPlayed() {
                player.playCard();
                cardAnimate.getCardFadeOut().setStart();
                changeGeckoStateWhenPlayCard();
            }
        };

        gecko = new GeckoEntity(geckoSprite, spriteSheets);
    }

    public SumBoxModel getSumBoxModel() {
        return sumBoxEntity.getSumBoxModel();
    }

    private void changeGeckoStateWhenPlayCard() {
        if (sumBoxEntity.isCapacityWarning()) {
            gecko.setFullEating();
        } else {
            gecko.setEating();
        }
    }

    public void setBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void init() {
        gecko.setNeutral();
        sumBoxEntity.init();
        numberBoxEntity.init();
        player.init();
        cardAnimate.getCardFadeOut().init();
        cardAnimate.getCardReset().init();
        isReadyToResetSum = false;
    }

    public NumberBoxEntity getNumberBoxDisplacer() {
        return numberBoxEntity;
    }

    public void setNumberQuantity(int quantity) {
        numberBoxEntity.setBoxQuantity(quantity);
    }

    public void update(int mouseX, int mouseY) {
        int sum = sumBoxEntity.getSumBoxModel().getSum();
        numberBoxEntity.update(sum);

        player.updateWhenTouchCard(mouseX, mouseY);
    }

    public void draw() {
        numberBoxEntity.draw(batch);

        sumBoxEntity.draw(batch);

        gecko.draw(batch);

        player.draw(batch);

        drawCardAnimate();
    }

    private void drawCardAnimate() {
        cardAnimate.getCardFadeOut().draw(batch, AnimationPos.CENTER);
        cardAnimate.getCardReset().draw(batch, AnimationPos.TOP_RIGHT);
    }

    public void touchDown(int mouseX, int mouseY) {
        isCardDragging = false;
        player.activateCard(mouseX, mouseY);
        cardAnimate.getCardReset().setLastMousePoint(player.getActiveCard().getInitPoint());

        if (sumBoxEntity.isCapacityWarning()) {
            gecko.setTooFull();
        }
        if (isReadyToResetSum) {
            gecko.setSpitting();
            sumBoxEntity.init();
            player.setSkillStateToNeutral();
            isReadyToResetSum = false;
        }
    }

    public void touchDragged(int mouseX, int mouseY) {
        if (player.isCardActive())
            isCardDragging = true;
        player.updateWhenDrag(mouseX, mouseY);
    }

    public void touchUp(int mouseX, int mouseY) {
        if (isCardDragging) {
            gecko.touchUp(mouseX, mouseY);
            cardAnimate.getCardFadeOut().setLastMousePoint(new Point(mouseX, mouseY));
        }
        if (sumBoxEntity.isCapacityFull())
            isReadyToResetSum = true;
    }
}