package com.arithfighter.ccg.widget;

import com.arithfighter.ccg.WindowSetting;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Hand {
    TemplateCard whiteCard, greenCard;
    TemplateCard[] cards;
    float gridX = WindowSetting.GRID_X;
    float initX = WindowSetting.CENTER_X;
    float initY = 0;
    float cardSize = gridX*3.5f;
    float padding = cardSize*1.2f;

    public void initHand(Texture texture){
        whiteCard = new TemplateCard(initX,initY,cardSize, Color.WHITE, texture);
        greenCard = new TemplateCard(initX+padding,initY,cardSize,Color.GREEN, texture);

        cards = new TemplateCard[]{whiteCard, greenCard};
    }

    public void draw(SpriteBatch batch, float x , float y){
        for (TemplateCard card: cards)
            card.draw(batch, x, y);
    }

    public void checkActive(float x, float y){
        for (TemplateCard card: cards)
                card.checkActive(x, y);
    }

    public void updateWhenDrag(float x , float y){
        for (TemplateCard card: cards)
                card.updateWhenDrag(x, y);
    }

    public void resetHand(){
        for (TemplateCard card: cards)
                card.resetCard();
    }

    public boolean isCardActive(){
        int condition = 0;
        for (int i = 0; i< cards.length;i++)
            if (cards[i].isActive())
                condition = i;

        return cards[condition].isActive();
    }
}
