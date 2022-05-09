package com.arithfighter.not.animate;

import com.arithfighter.not.time.TimeHandler;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

public class LoopAnimation extends AnimationComponent{
    private int drawTime;
    private boolean isEnd = false;
    private int ratePerMin;

    public LoopAnimation(Texture spriteSheet, int cols, int rows) {
        setProcessor(new AnimationProcessor(spriteSheet, cols, rows));
        getProcessor().setSpeed(0.08f);

        setTimeHandler(new TimeHandler());
    }

    public void setRatePerMin(int ratePerMin) {
        this.ratePerMin = ratePerMin;
    }

    public void setDrawTime(int millisecond) {
        this.drawTime = millisecond;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setScale(int scale){
        getProcessor().setScale(scale);
    }

    public void draw(SpriteBatch batch) {
        getProcessor().setBatch(batch);

        long timeGap = 60000 / ratePerMin;

        if (TimeUtils.millis() % (timeGap+drawTime) < drawTime) {
                handleAnimation();
                isEnd = false;
        } else{
            isEnd = true;
            resetTimeAndAnimation();
        }
    }

    private void handleAnimation() {
        getTimeHandler().updatePastedTime();

        getProcessor().setPoint(getDrawPoint());
        getProcessor().draw(getDrawPoint().getX(), getDrawPoint().getY());
    }

    private void resetTimeAndAnimation() {
        getTimeHandler().resetPastedTime();
        getProcessor().init();
    }
}
