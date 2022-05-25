package com.arithfighter.not.scene;

import com.arithfighter.not.CursorPositionAccessor;
import com.arithfighter.not.TextureService;
import com.arithfighter.not.WindowSetting;
import com.arithfighter.not.audio.SoundManager;
import com.arithfighter.not.pojo.Point;
import com.arithfighter.not.pojo.Rectangle;
import com.arithfighter.not.system.RandomNumListProducer;
import com.arithfighter.not.system.RandomNumProducer;
import com.arithfighter.not.widget.BetBrowser;
import com.arithfighter.not.widget.button.Button;
import com.arithfighter.not.widget.button.SceneControlButton;
import com.arithfighter.not.font.Font;
import com.arithfighter.not.pojo.TextProvider;
import com.arithfighter.not.widget.dialog.Dialog;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.List;

public class BetScreen extends SceneComponent implements SceneEvent, MouseEvent {
    private final BetBrowser betBrowser;
    private final SceneControlButton startButton;
    private final SoundManager soundManager;
    private final NumberBoxQuantityGenerator numberBoxQuantityGenerator;
    private final int cardLimit = 15;
    private final FontManager fontManager;
    private final GameCardCollection gameCards;
    private int yourTokens = 0;
    private final WarningDialog warningDialog;
    private int totalActiveGames;
    private final Font font;
    private final Font browserFont;

    public BetScreen(TextureService textureService, SoundManager soundManager) {
        Texture[] textures = textureService.getTextures(textureService.getKeys()[0]);
        this.soundManager = soundManager;

        font = new Font(22);
        font.setColor(Color.WHITE);

        browserFont = new Font(22);
        browserFont.setColor(Color.WHITE);

        betBrowser = new BetBrowser(textures);
        betBrowser.setFont(browserFont);
        betBrowser.setPosition(500, 200);
        int[] betList = {5, 10, 20, 50, 100};
        betBrowser.setBetList(betList);

        startButton = new SceneControlButton(textures[6], 2f);
        startButton.getButton().setFont(font);
        startButton.getButton().setPosition(1000, 80);

        fontManager = new FontManager();

        numberBoxQuantityGenerator = new NumberBoxQuantityGenerator();

        gameCards = new GameCardCollection(textures);

        warningDialog = new WarningDialog(textures[10]);
    }

    public void setYourTokens(int yourTokens) {
        this.yourTokens = yourTokens;
    }

    public int getCardLimit() {
        return cardLimit;
    }

    public int[] getNumberBoxQuantity() {
        int length = gameCards.getGameCards().length;
        int[] array = new int[length];

        for (int i = 0; i < length; i++) {
            if (gameCards.getGameCards()[i].isOn())
                array[i] = gameCards.getGameCards()[i].getBoxQuantity();
        }

        return array;
    }

    public int getBet() {
        int q = 0;

        for (int i = 0; i < gameCards.getGameCards().length; i++) {
            if (gameCards.getGameCards()[i].isOn())
                q++;
        }
        return betBrowser.getBet() * q;
    }

    public boolean isStartGame() {
        return startButton.isStart();
    }

    @Override
    public void touchDown() {
        CursorPositionAccessor cursorPos = getCursorPos();
        int x = cursorPos.getX();
        int y = cursorPos.getY();

        if (warningDialog.isNotShow()) {
            betBrowser.activate(x, y);

            startButton.getButton().on(x, y);
        } else {
            startButton.getButton().off();
            startButton.init();
            warningDialog.disable();
        }
    }

    @Override
    public void touchDragged() {
        if (warningDialog.isNotShow()) {
            betBrowser.deactivate();

            startButton.getButton().off();
        }
    }

    @Override
    public void touchUp() {
        if (warningDialog.isNotShow()) {
            if (betBrowser.isButtonActive())
                soundManager.playTouchedSound();

            if (startButton.getButton().isOn())
                soundManager.playAcceptSound();

            betBrowser.deactivate();

            startButton.getButton().off();

            for (GameCard card : gameCards.getGameCards())
                card.touchDown(getCursorPos().getX(), getCursorPos().getY());
        }
    }

    public void setNumberBoxQuantity() {
        numberBoxQuantityGenerator.init();

        int[] array = numberBoxQuantityGenerator.getQuantityArray();

        for (int i = 0; i < array.length; i++)
            gameCards.getGameCards()[i].setBoxQuantity(array[i]);
    }

    @Override
    public void init() {
        startButton.init();
        warningDialog.disable();
        for (GameCard card : gameCards.getGameCards())
            card.init();
    }

    @Override
    public void update() {
        startButton.update();
        betBrowser.update();

        totalActiveGames = getBet() / betBrowser.getBet();

        if (startButton.getButton().isOn()) {
            if (isBetMinimalThanYourTokens()) {
                warningDialog.setNoEnoughToken();
                warningDialog.setShow();
            }
            if (isNoGameCardOn()) {
                warningDialog.setNoGameChoose();
                warningDialog.setShow();
            }
        }
    }

    private boolean isBetMinimalThanYourTokens(){
        return getBet() > yourTokens;
    }

    private boolean isNoGameCardOn() {
        boolean flag = true;
        int length = gameCards.getGameCards().length;

        for (int i = 0; i < length; i++) {
            if (gameCards.getGameCards()[i].isOn())
                flag = false;
        }
        return flag;
    }

    @Override
    public void draw() {
        SpriteBatch batch = getBatch();
        TextProvider textProvider = new TextProvider();
        String[] texts = textProvider.getBetScreenTexts();

        setFontManager(texts);
        fontManager.draw(batch);

        betBrowser.draw(batch);

        startButton.getButton().draw(batch, texts[2]);

        for (GameCard card : gameCards.getGameCards())
            card.draw(batch);

        warningDialog.draw(batch);
    }

    private void setFontManager(String[] texts){
        String totalBetCalculation =
                "total bet = " + betBrowser.getBet() + " X " + totalActiveGames + " = " + getBet();

        fontManager.setCardLimit(texts[0] + cardLimit);
        fontManager.setBetHint(texts[1]);
        fontManager.setTokens("Your tokens: " + yourTokens);
        fontManager.setTotalBet(totalBetCalculation);
    }

    @Override
    public void dispose() {
        fontManager.dispose();
        for (GameCard card : gameCards.getGameCards())
            card.dispose();
        warningDialog.dispose();
        font.dispose();
        browserFont.dispose();
    }
}

class GameCardCollection {
    private final GameCard[] gameCards;

    public GameCardCollection(Texture[] textures) {
        int totalCards = 3;
        gameCards = new GameCard[totalCards];

        String[] codeArray = {"A", "B", "C"};
        for (int i = 0; i < totalCards; i++) {
            gameCards[i] = new GameCard(textures[1]);
            gameCards[i].setCardCode(codeArray[i]);
        }

        Point point = new Point(100, 400);
        gameCards[0].setPoint(point);

        int margin = 15;
        float x = gameCards[0].getRectangle().getWidth() + margin;

        for (int i = 1; i < totalCards; i++)
            gameCards[i].setPoint(new Point(point.getX() + x * i, point.getY()));
    }

    public GameCard[] getGameCards() {
        return gameCards;
    }
}

class WarningDialog {
    private final Dialog dialog;
    private final Font font;
    private boolean isShow = false;

    public WarningDialog(Texture texture) {
        font = new Font(20);
        font.setColor(Color.BLACK);

        dialog = new Dialog(texture, 35);
        dialog.setFont(font);
        dialog.getPoint().set(
                WindowSetting.CENTER_X - dialog.getDialog().getWidget().getWidth() / 2,
                WindowSetting.CENTER_Y - dialog.getDialog().getWidget().getHeight() / 2
        );
    }

    public void setShow() {
        isShow = true;
    }

    public void disable() {
        isShow = false;
    }

    public boolean isNotShow() {
        return !isShow;
    }

    public void setNoEnoughToken() {
        dialog.setContent1("You don't have enough ");
        dialog.setContent2("tokens.");
    }

    public void setNoGameChoose() {
        dialog.setContent1("Please choose one game ");
        dialog.setContent2("at least.");
    }

    public void draw(SpriteBatch batch) {
        if (isShow)
            dialog.drawDialog(batch);
    }

    public void dispose() {
        font.dispose();
    }
}

class GameCard {
    private final Font codeFont;
    private final Font cardFont;
    private final Button gameCard;
    private final Rectangle rectangle;
    private Point point;
    private int boxQuantity;
    private String cardCode;

    public GameCard(Texture texture) {
        cardFont = new Font(24);
        cardFont.setColor(Color.WHITE);

        gameCard = new Button(texture, 3f);
        gameCard.setFont(cardFont);

        rectangle = new Rectangle(texture.getWidth() * 3, texture.getHeight() * 3);

        codeFont = new Font(36);
        codeFont.setColor(Color.PURPLE);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setPoint(Point point) {
        this.point = point;
        gameCard.setPosition(point.getX(), point.getY());
    }

    public Point getPoint() {
        return point;
    }

    public int getBoxQuantity() {
        return boxQuantity;
    }

    public boolean isOn() {
        return gameCard.isOn();
    }

    public void setBoxQuantity(int boxQuantity) {
        this.boxQuantity = boxQuantity;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public void draw(SpriteBatch batch) {
        gameCard.draw(
                batch,
                boxQuantity + " box"
        );

        int fontSize = codeFont.getSize();
        codeFont.draw(
                batch,
                cardCode,
                point.getX() + rectangle.getWidth() - fontSize,
                point.getY() + rectangle.getHeight() - fontSize / 2f
        );
    }

    public void init() {
        gameCard.off();
    }

    public void touchDown(float x, float y) {
        if (gameCard.isOnButton(x, y)) {
            if (gameCard.isOn())
                gameCard.off();
            else
                gameCard.on(x, y);
        }
    }

    public void dispose() {
        codeFont.dispose();
        cardFont.dispose();
    }
}

class FontManager {
    private final Font font;
    private String cardLimit;
    private String betHint;
    private String tokens;
    private String totalBet;

    public FontManager() {
        font = new Font(24);
        font.setColor(Color.WHITE);
    }

    public void setCardLimit(String cardLimit) {
        this.cardLimit = cardLimit;
    }

    public void setBetHint(String betHint) {
        this.betHint = betHint;
    }

    public void setTokens(String tokens) {
        this.tokens = tokens;
    }

    public void setTotalBet(String totalBet) {
        this.totalBet = totalBet;
    }

    public void draw(SpriteBatch batch) {
        font.draw(batch, cardLimit, 800, 650);

        font.draw(batch, tokens, 800, 600);

        font.draw(batch, betHint, 400, 300);

        font.draw(batch, totalBet, 100, 150);
    }

    public void dispose() {
        font.dispose();
    }
}

class NumberBoxQuantityGenerator {
    private final int[] quantityCandidates;
    private final RandomNumListProducer indexListProducer;
    private final int quantityArrayLength = 3;

    public NumberBoxQuantityGenerator() {
        quantityCandidates = new int[]{2, 4, 7, 9};

        RandomNumProducer indexPicker = new RandomNumProducer(quantityCandidates.length - 1, 0);

        indexListProducer = new RandomNumListProducer(indexPicker);
        indexListProducer.setMaxQuantity(quantityArrayLength);
    }

    public int[] getQuantityArray() {
        int[] quantityArray = new int[quantityArrayLength];
        List<Integer> indexArray = indexListProducer.getNumbers();

        for (int i = 0; i < quantityArrayLength; i++)
            quantityArray[i] = quantityCandidates[indexArray.get(i)];

        return quantityArray;
    }

    public void init() {
        indexListProducer.clear();
    }
}