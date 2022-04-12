package com.arithfighter.not.entity;

import com.arithfighter.not.font.Font;
import com.arithfighter.not.pojo.Point;
import com.arithfighter.not.pojo.ValueHolder;
import com.arithfighter.not.widget.ArrowButtons;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ControlNumber {
    private final Font number;
    private final ArrowButtons arrows;
    private final int fontSize;
    private boolean isButtonLock = false;
    private final ValueHolder valueHolder;
    private int initValue;

    public ControlNumber(Texture[] textures) {
        valueHolder = new ValueHolder();

        arrows = new ArrowButtons(textures, 0.8f);

        fontSize = 25;
        number = new Font(fontSize);
    }

    public boolean isButtonActive(){
        return arrows.isLeftActive()||arrows.isRightActive();
    }

    public int getValue() {
        return valueHolder.getValue();
    }

    public void setPosition(int x, int y) {
        Point point = new Point();
        point.set(x,y);
        arrows.setPoint(point);
    }



    public void update() {
        int i = valueHolder.getValue();
        valueHolder.updateValue();

        if (!isButtonLock) {
            if (arrows.isLeftActive() && i >= 0)
                valueHolder.decreaseValue();
            if (arrows.isRightActive() && i < valueHolder.getMaxValue())
                valueHolder.increaseValue();
        }
        isButtonLock = arrows.isLeftActive() || arrows.isRightActive();
    }

    public void activate(float x, float y) {
        arrows.activate(x, y);
    }

    public void deactivate() {
        arrows.deactivate();
    }

    public void dispose() {
        number.dispose();
        arrows.dispose();
    }
}
