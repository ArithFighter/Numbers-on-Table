package com.arithfighter.not.entity.player;

import com.arithfighter.not.pojo.Recorder;
import com.arithfighter.not.widget.bar.EnergyBar;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlayerEnergyBar {
    private final EnergyBar energyBar;
    private final Recorder energyRecorder;
    private int energyGain;

    public PlayerEnergyBar(Texture[] textures, CharacterList character) {
        energyRecorder = new Recorder();

        energyBar = new EnergyBar(textures);

        setEnergyGain(character);
    }

    private void setEnergyGain(CharacterList character) {
        //Rogue gain more energy than other characters when play card
        if (character == CharacterList.ROGUE)
            energyGain = 8;
        else
            energyGain = 5;
    }

    public void reset() {
        energyRecorder.reset();
    }

    public int getEnergy() {
        return energyRecorder.getRecord();
    }

    public void update() {
        if (energyBar.isNotFull())
            energyRecorder.update(energyGain);
    }

    public void draw(SpriteBatch batch) {
        energyBar.setEnergy(energyRecorder.getRecord());
        energyBar.draw(batch);
    }

    public boolean isMaxEnergy() {
        return energyRecorder.getRecord() >= energyBar.getMax();
    }

    public void dispose() {
        energyBar.dispose();
    }
}
