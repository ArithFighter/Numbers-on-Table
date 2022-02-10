package com.arithfighter.ccg;

import com.arithfighter.ccg.component.NumberCard;
import com.arithfighter.ccg.component.NumberCardCollection;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Hand {
    NumberCard[] cards;
    NumberCardCollection numberCardCollection;

    public Hand(Texture[] textures, CharacterList character) {
        CharacterSetCollection csc = new CharacterSetCollection();

        int[] numberSet = csc.getCharacterSet(character);

        numberCardCollection = new NumberCardCollection(numberSet[0],numberSet[1],numberSet[2], numberSet[3],textures);

        cards = numberCardCollection.getCards();
    }

    public void draw(SpriteBatch batch) {
        for (NumberCard card : cards)
            card.draw(batch);
    }

    public int getCardNumber(){
        return cards[getActiveCardIndex()].getNumber();
    }

    public boolean isResetCard(){
        return getActiveCardIndex() == 3;
    }

    public void checkTouchingCard(float x, float y) {
        for (NumberCard card : cards)
            card.checkTouchingCard(x, y);
    }

    public void checkActive(float x, float y) {
        for (NumberCard card : cards)
            card.checkActive(x, y);
    }

    public void updateWhenDrag(float x, float y) {
        for (NumberCard card : cards)
            card.updateWhenDrag(x, y);
    }

    public void resetHand() {
        for (NumberCard card : cards)
            card.resetPosition();
    }

    public boolean isCardActive() {
        return cards[getActiveCardIndex()].isActive();
    }

    private int getActiveCardIndex(){
        int index = 0;
        for (int i = 0; i < cards.length; i++){
            if (cards[i].isActive())
                index = i;
        }
        return index;
    }

    public void dispose() {
        for (NumberCard card : cards)
            card.dispose();
    }
}