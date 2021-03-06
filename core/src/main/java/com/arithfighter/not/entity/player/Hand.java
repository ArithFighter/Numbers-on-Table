package com.arithfighter.not.entity.player;

import com.arithfighter.not.WindowSetting;
import com.arithfighter.not.card.NumberCard;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Hand {
    private final NumberCard[] cards;
    private final CardTexturesExtractor texturesExtractor;

    public Hand(Texture[] textures, CharacterList character) {
        texturesExtractor = new CardTexturesExtractor(textures);

        cards = new NumberCard[character.getTextureMap().length];

        createCardList(character);
    }

    private void createCardList(CharacterList character) {
        float initX = WindowSetting.CENTER_X + WindowSetting.GRID_X * 1.2f;
        float initY = -WindowSetting.GRID_Y;
        Texture[] cardSet = texturesExtractor.getCardTextures(character);

        for (int i = 0; i < cards.length; i++)
            cards[i] = new NumberCard(
                    initX + i * getPadding(cardSet),
                    initY,
                    cardSet[i],
                    character.getNumberSet()[i]
            );
    }

    private float getPadding(Texture[] textures) {
        NumberCard sample = new NumberCard(0, 0, textures[0], 0);
        return sample.getShape().getWidth() + WindowSetting.GRID_X * 0.6f;
    }

    public void draw(SpriteBatch batch) {
        for (NumberCard card : cards)
            card.draw(batch);
    }

    public NumberCard getActiveCard(){
        return cards[getActiveCardIndex()];
    }

    public int getCardNumber() {
        return cards[getActiveCardIndex()].getNumber();
    }

    public boolean isResettingCard() {
        return getActiveCardIndex() == cards.length-1;
    }

    public NumberCard[] getCards(){
        return cards;
    }

    public boolean isCardActive() {
        return cards[getActiveCardIndex()].isActive();
    }

    private int getActiveCardIndex() {
        int index = 0;
        for (int i = 0; i < cards.length; i++) {
            if (cards[i].isActive())
                index = i;
        }
        return index;
    }
}

class CardTexturesExtractor {
    private final Texture[] cardTextures;
    private final CharacterList[] characters = CharacterList.values();
    private final int quantity = characters[0].getTextureMap().length;

    public CardTexturesExtractor(Texture[] cardTextures) {
        this.cardTextures = cardTextures;
    }

    public Texture[] getCardTextures(CharacterList player) {
        Texture[] cardSet = new Texture[quantity];

        for (CharacterList character : characters)
            if (player == character) {
                for (int j = 0; j < quantity; j++)
                    cardSet[j] = cardTextures[character.getTextureMap()[j]];
            }

        return cardSet;
    }
}
