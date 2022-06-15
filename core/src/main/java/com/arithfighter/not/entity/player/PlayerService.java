package com.arithfighter.not.entity.player;

import com.arithfighter.not.entity.numberbox.NumberBoxEntity;
import com.arithfighter.not.entity.sumbox.SumBoxModel;
import com.arithfighter.not.system.GameNumProducer;
import com.arithfighter.not.system.RandomNumProducer;
import com.arithfighter.not.pojo.Recorder;
import com.arithfighter.not.widget.bar.EnergyBar;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class PlayerService {
    private Player[] players;
    private Recorder playRecord;
    private NumberBoxEntity numberBoxEntity;
    private SumBoxModel sumBoxModel;
    private int characterQuantity;

    public PlayerService() {
    }

    public void setSumBoxModel(SumBoxModel sumBoxModel) {
        this.sumBoxModel = sumBoxModel;
    }

    public void setNumberBoxDisplacer(NumberBoxEntity numberBoxEntity) {
        this.numberBoxEntity = numberBoxEntity;
    }

    public void setCharacterQuantity(int characterQuantity) {
        this.characterQuantity = characterQuantity;
    }

    public void setPlayRecord(Recorder playRecord) {
        this.playRecord = playRecord;
    }

    public void createPlayers(Texture[] textures, Texture[] cards) {
        players = new Player[characterQuantity];
        SkillHandler skillHandler = new SkillHandler(numberBoxEntity);

        EnergyBar energyBar = new EnergyBar(textures);

        for (int i = 0; i < characterQuantity; i++){
            players[i] = new Player(cards, CharacterList.values()[i]) {
                @Override
                public void checkNumberCardPlayed() {
                    sumBoxModel.update(getHand().getCardNumber());
                }

                @Override
                public void doWhenResettingCardPlay() {
                    sumBoxModel.init();
                    sumBoxModel.update(getHand().getCardNumber());
                }

                @Override
                public void doWhenAnyCardPlayed() {
                    playRecord.update(1);
                }

                @Override
                public void castSkill(CharacterList character) {
                    skillHandler.cast(character);
                }
            };
            players[i].setEnergyBarToController(energyBar);
        }

    }

    public Player[] getPlayers() {
        return players;
    }
}

class SkillHandler {
    private final NumberBoxEntity numberBoxEntity;
    private final NumberBoxPicker numberBoxPicker;

    public SkillHandler(NumberBoxEntity numberBoxEntity) {
        this.numberBoxEntity = numberBoxEntity;
        numberBoxPicker = new NumberBoxPicker(numberBoxEntity);
    }

    public void cast(CharacterList character) {
        switch (character) {
            case KNIGHT:
                changeANumberBoxValue(9);
                break;
            case ROGUE:
                changeANumberBoxValue(8);
                break;
            case HUNTER:
                changeANumberBoxValue(14);
                break;
            case PALADIN:
                changeANumberBoxValue(22);
                break;
            case WARRIOR:
                increaseAllValueBy11();
                break;
        }
    }

    private void increaseAllValueBy11() {
        int gain = 11;

        for (int i = 0; i < numberBoxEntity.getMaxQuantity(); i++) {
            if (getNumberBoxValue(i) > 0 &&
                    getNumberBoxValue(i) < new GameNumProducer().getMaxNumber() - gain)
                numberBoxEntity.set(i, getNumberBoxValue(i) + gain);
        }
    }

    private void changeANumberBoxValue(int value){
        numberBoxEntity.set(numberBoxPicker.getRandomNonZeroValueIndex(), value);
    }

    private int getNumberBoxValue(int i) {
        return numberBoxEntity.getNumberBoxValue(i);
    }
}

class NumberBoxPicker {
    private final NumberBoxEntity numberBoxEntity;

    public NumberBoxPicker(NumberBoxEntity numberBoxEntity) {
        this.numberBoxEntity = numberBoxEntity;
    }

    public int getRandomNonZeroValueIndex() {
        ArrayList<Integer> indexList = new ArrayList<>();

        for (int i = 0; i < numberBoxEntity.getMaxQuantity(); i++) {
            if (numberBoxEntity.getNumberBoxValue(i) > 0)
                indexList.add(i);
        }
        RandomNumProducer rnp = new RandomNumProducer(indexList.size() - 1,0);
        int indexPick = rnp.getRandomNum();

        return indexList.get(indexPick);
    }
}
