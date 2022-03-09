package com.arithfighter.ccg.widget;

import com.arithfighter.ccg.font.Font;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class NumberBox{
    private final Font text;
    private final Sprite box;
    private final SpriteWidget widget;
    private final WidgetAttribute attribute;

    public NumberBox(Texture texture) {
        widget = new SpriteWidget();

        widget.configWidget(texture, 3.5f);
        widget.fontSize = 32;

        text = new Font(widget.fontSize);

        box = new Sprite(texture);

        attribute = new WidgetAttribute(widget.fontSize);
    }

    public void setPosition(float x, float y){
        widget.posX = x;
        widget.posY = y;
    }

    public float getWidth() {
        return widget.width;
    }

    public float getHeight() {
        return widget.height;
    }

    public void draw(int number, SpriteBatch batch) {
        setSprite();

        box.draw(batch);

        addText(number, batch);
    }

    private void setSprite() {
        box.setSize(widget.width, widget.height);
        box.setPosition(widget.posX, widget.posY);
        box.setColor(0, 0.9f, 0.9f, 1);
    }

    private void addText(int number, SpriteBatch batch) {
        String content = String.valueOf(number);

        float textX = attribute.getCenterX(widget.posX, widget.width, content);
        float textY = attribute.getCenterY(widget.posY,widget.height);

        changeNumColor(number);

        text.draw(batch, content, textX, textY);
    }

    private void changeNumColor(int number) {
        int purpleNum = 15;
        int blueNum = 21;
        int yellowNum = 99;

        if (number < purpleNum)
            text.setColor(Color.PURPLE);

        else if (number <= blueNum)
            text.setColor(Color.BLUE);

        else if (number < yellowNum)
            text.setColor(Color.YELLOW);
    }

    public void dispose() {
        text.dispose();
    }
}
