package com.arithfighter.not.entity.player;

import com.arithfighter.not.pojo.GameNumProducer;

public enum CharacterList {
    KNIGHT(new int[]{2,3,9,0}, new int[]{2,3,9,0}),
    ROGUE(new int[] {-1,3,8,0}, new int[]{12,3,8,0}),
    HUNTER(new int[] {2,3,5,12}, new int[]{2,3,5,10}),
    PALADIN( new int[]{-1,2,7,15}, new int[]{12,2,7,11}),
    WARRIOR(new int[]{-7,-3,-1, new GameNumProducer().getMaxNumber()}, new int[]{14,13,12,15});

    final int[] numberSet;
    final int[] textureMap;

    CharacterList(int[] numberSet, int[] textureMap) {
        this.numberSet = numberSet;
        this.textureMap = textureMap;
    }

    public int[] getNumberSet() {
        return numberSet;
    }

    public int[] getTextureMap() {
        return textureMap;
    }
}
