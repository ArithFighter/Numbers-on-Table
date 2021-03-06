package com.arithfighter.not;

import com.arithfighter.not.font.Font;
import com.arithfighter.not.font.FontService;
import com.arithfighter.not.widget.RawWidget;
import com.arithfighter.not.widget.SpriteWidget;
import com.arithfighter.not.widget.VisibleWidget;
import com.arithfighter.not.widget.dialog.ConversationDialog;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextAdventure{
    private final ConversationDialog conversationDialog;
    private int cursor = 0;
    private boolean isButtonLock = false;
    private final MugShot mugShot;
    private String[] conversations;

    public TextAdventure(Texture[] widgets, Texture[] panels, FontService fontService){
        Font font = fontService.getFont28();
        Font buttonFont = fontService.getFont22();

        conversationDialog = new ConversationDialog(widgets);
        conversationDialog.setFont(font);
        conversationDialog.setButtonFont(buttonFont);

        mugShot = new MugShot(widgets[3], panels[0]);

        RawWidget widget = conversationDialog.getDialog().getWidget();
        mugShot.setPosition(widget.getWidth()*9/10, widget.getHeight()*0.7f);
    }

    public void setConversations(String[] conversations){
        this.conversations = conversations;
    }

    public void init(){
        cursor-=cursor;
        conversationDialog.getSkipButton().off();
    }

    public void update(){
        handleSkipButton();

        //reset cursor with key R
        if (Gdx.input.isKeyJustPressed(Input.Keys.R))
            cursor = 0;

        try{
            conversationDialog.setOriginString(conversations[cursor]);
        }catch (ArrayIndexOutOfBoundsException ignored){}
    }

    public boolean isAllConversationFinished(){
        return cursor>=conversations.length;
    }

    private void handleSkipButton(){
        if (!isButtonLock){
            updateCursorWhenButtonActivate();
        }
        isButtonLock = conversationDialog.getSkipButton().isOn();
    }

    private void updateCursorWhenButtonActivate(){
        if (conversationDialog.getSkipButton().isOn() )
            cursor++;
    }

    public void draw(SpriteBatch batch){
        mugShot.draw(batch);
        conversationDialog.draw(batch);
    }

    public void touchDown(int x, int y) {
        conversationDialog.activate(x, y);
    }

    public void touchDragged() {
        conversationDialog.deactivate();
    }

    public void touchUp() {
        conversationDialog.deactivate();
    }
}

class MugShot{
    private final VisibleWidget mugShot;
    private final VisibleWidget panelBox;

    public MugShot(Texture texture, Texture panel){
        mugShot = new SpriteWidget(texture, 6f);

        panelBox = new SpriteWidget(panel, 0.9f);
    }

    public void setPosition(float x, float y){
        mugShot.setPosition(
                x-(mugShot.getWidget().getWidth()-panelBox.getWidget().getWidth())/2
                ,y-(mugShot.getWidget().getWidth()-panelBox.getWidget().getWidth())/2
        );
        panelBox.setPosition(x, y);
    }

    public void draw(SpriteBatch batch){
        mugShot.draw(batch);
        panelBox.draw(batch);
    }
}

