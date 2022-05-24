package com.arithfighter.not.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Font {
    private final FreeTypeFontGenerator fontGenerator;
    private final FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private final BitmapFont font;

    public Font(int size){
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fontstyle/pcsenior.ttf"));

        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameter.size = size;

        font = fontGenerator.generateFont(fontParameter);
    }

    public int getSize(){
        return fontParameter.size;
    }

    public void setColor(Color color){
        font.setColor(color);
    }

    public void draw(SpriteBatch batch, String content, float x, float y){
        font.draw(batch, content, x,y);
    }

    public void dispose(){
        font.dispose();
        fontGenerator.dispose();
    }
}
