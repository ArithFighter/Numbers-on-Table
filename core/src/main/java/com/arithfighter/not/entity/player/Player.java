package com.arithfighter.not.entity.player;

import com.arithfighter.not.card.NumberCard;
import com.arithfighter.not.widget.bar.EnergyBar;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {
    private final Hand hand;
    private final CharacterList character;
    private final EnergyBarController energyBarController;

    private enum SkillState {NEUTRAL, READY}

    private SkillState skillState = SkillState.NEUTRAL;

    public Player(Texture[] cards, CharacterList character) {
        energyBarController = new EnergyBarController(character);

        hand = new Hand(cards, character);

        this.character = character;
    }

    public void setEnergyBarToController(EnergyBar energyBar) {
        energyBarController.setEnergyBar(energyBar);
    }

    public Hand getHand() {
        return hand;
    }

    public void init() {
        energyBarController.reset();
    }

    public boolean isCardActive(){
        return hand.isCardActive();
    }

    public NumberCard getActiveCard() {
        return hand.getActiveCard();
    }

    public final void activateCard(int mouseX, int mouseY) {
        for (NumberCard card : hand.getCards())
            card.activateCard(mouseX, mouseY);
    }

    public final void updateWhenDrag(int mouseX, int mouseY) {
        for (NumberCard card : hand.getCards()){
            if (card.isActive())
                card.setPosition(mouseX - card.getShape().getWidth() / 2,
                            mouseY - card.getShape().getHeight() / 2);
        }
    }

    public final void draw(SpriteBatch batch) {
        energyBarController.draw(batch);

        hand.draw(batch);
    }

    public void updateWhenTouchCard(int mouseX, int mouseY) {
        for (NumberCard card : hand.getCards()) {
            if (card.isOnCard(mouseX, mouseY)) {
                playTouchedAnimation(card);
            } else
                card.initCard();
        }
    }

    private void playTouchedAnimation(NumberCard card){
        int movingDistance = 30;
        float speed = 3;

        if (card.getPoint().getY() < card.getInitPoint().getY() + movingDistance)
            card.setPosition(card.getPoint().getX(), card.getPoint().getY()+speed);
    }

    public void setSkillStateToNeutral(){
        skillState = SkillState.NEUTRAL;
    }

    public final void initHand() {
        for (NumberCard card : hand.getCards())
            card.initCard();
    }

    public final void playCard() {
        if (hand.isCardActive()) {
            doWhenAnyCardPlayed();

            energyBarController.update();

            if (hand.isResettingCard()){
                checkResettingCardPlay();
                skillState = SkillState.READY;
            }
            else{
                if (skillState == SkillState.READY)
                    skillState = SkillState.NEUTRAL;
                checkNumberCardPlayed();
            }
        }
    }

    public void doWhenAnyCardPlayed() {

    }

    public void checkNumberCardPlayed() {
    }

    private void checkResettingCardPlay() {
        if (isSkillReady()) {
            castSkill(character);
            energyBarController.reset();
            skillState = SkillState.NEUTRAL;
        } else {
            doWhenResettingCardPlay();
        }
    }

    private boolean isSkillReady() {
        return skillState == SkillState.READY &&
                energyBarController.isMaxEnergy();
    }

    public void castSkill(CharacterList character) {
    }

    public void doWhenResettingCardPlay() {
    }
}

