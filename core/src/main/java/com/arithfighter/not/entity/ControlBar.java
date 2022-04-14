package com.arithfighter.not.entity;

import com.arithfighter.not.font.Font;
import com.arithfighter.not.pojo.Point;
import com.arithfighter.not.pojo.ValueHolder;
import com.arithfighter.not.widget.ArrowButtons;
import com.arithfighter.not.widget.SpriteWidget;
import com.arithfighter.not.widget.VisibleWidget;
import com.arithfighter.not.widget.Widget;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ControlBar {
    private final VisibleWidget[] grids;
    private final Font font;
    private final ArrowButtons arrows;
    private final int fontSize;
    private boolean isButtonLock = false;
    private final ValueHolder valueHolder;
    private final String content;

    public ControlBar(Texture[] textures, String content) {
        this.content = content;

        int max = 6;
        valueHolder = new ValueHolder(max);

        grids = new VisibleWidget[valueHolder.getMaxValue()];

        for (int i = 0; i < grids.length; i++)
            grids[i] = new SpriteWidget(textures[5], 0.5f);

        arrows = new ArrowButtons(textures, 0.8f);

        fontSize = 25;
        font = new Font(fontSize);
    }

    public boolean isButtonActive(){
        return arrows.isLeftActive()||arrows.isRightActive();
    }

    public int getValue() {
        return valueHolder.getValue();
    }

    public void setPosition(int x, int y) {
        for (int i = 0; i < grids.length; i++) {
            grids[i].setPosition(x + grids[i].getWidget().getWidth() * i + 10 * i, y);
        }
    }

    public void draw(SpriteBatch batch) {
        for (int i = 0; i < valueHolder.getValue(); i++)
            grids[i].draw(batch);

        addComponent(batch);
    }

    private void addComponent(SpriteBatch batch) {
        Widget widget = grids[0].getWidget();
        Point point = widget.getPoint();

        font.draw(batch, content, point.getX() - content.length() * fontSize - 50, point.getY() + fontSize / 2f);

        Point arrowPoint = new Point(point.getX()-50, point.getY());
        arrows.setPoint(arrowPoint);

        arrows.drawLeftArrow(batch);
        arrows.drawRightArrow(batch, (widget.getWidth() + 20) * grids.length);
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
        font.dispose();
        arrows.dispose();
    }
}