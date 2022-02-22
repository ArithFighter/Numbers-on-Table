package com.arithfighter.ccg.card;

import com.arithfighter.ccg.CharacterList;
import com.badlogic.gdx.graphics.Texture;

public class CardTexturesExtractor {
    Texture[] cardTextures;

    public CardTexturesExtractor(Texture[] cardTextures){
        this.cardTextures = cardTextures;
    }
    
    public Texture[] getCardSet(CharacterList character){
        Texture[] cardSet = new Texture[4];
        
        switch (character){
            case KNIGHT:
                cardSet = getCardTextures(2,3,9,0,1);
                break;
            case ROGUE:
                cardSet = getCardTextures(12,3,8,0,1);
                break;
            case HUNTER:
                cardSet = getCardTextures(2,3,5,10,1);
                break;
            case PALADIN:
                cardSet = getCardTextures(12,2,7,11,1);
                break;
            case WARRIOR:
                cardSet = getCardTextures(14,13,12,15,1);
                break;
        }
        
        return cardSet;
    }

    private Texture[] getCardTextures(int min, int mid, int max, int reset, int skill){
        Texture[] cardSet = new Texture[5];

        cardSet[0] = cardTextures[min];
        cardSet[1] = cardTextures[mid];
        cardSet[2] = cardTextures[max];
        cardSet[3] = cardTextures[reset];
        cardSet[4] = cardTextures[skill];

        return cardSet;
    }

    public void dispose(){
        for (Texture texture:cardTextures)
            texture.dispose();
    }
}