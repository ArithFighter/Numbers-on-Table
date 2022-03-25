package com.arithfighter.ccg.scene;

import com.arithfighter.ccg.entity.GameComponent;
import com.arithfighter.ccg.entity.CharacterList;
import com.arithfighter.ccg.entity.GameDataAccessor;
import com.arithfighter.ccg.entity.Player;
import com.arithfighter.ccg.entity.SkillHandler;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Game {
    private final Player[] players;
    private final GameComponent gameComponent;
    private final GameDataAccessor dataAccessor;
    private final int characterQuantity = CharacterList.values().length;

    public Game(Texture[] textures, Texture[] cards){
        dataAccessor = new GameDataAccessor();

        players = new Player[characterQuantity];

        gameComponent = new GameComponent(textures, dataAccessor);

        addPlayers(textures, cards);
    }

    private void addPlayers(Texture[] textures, Texture[] cards) {
        SkillHandler skillHandler = new SkillHandler(gameComponent.getNumberBoxDisplacer());

        for (int i = 0; i < characterQuantity; i++)
            players[i] = new Player(
                    textures,
                    cards,
                    CharacterList.values()[i]) {
                @Override
                public void doWhenCardPlayed() {
                    dataAccessor.updatePlayTimes();
                }

                @Override
                public void castSkill(CharacterList character) {
                    skillHandler.cast(character);
                }
            };
    }

    public boolean isReturnToMenu(){
        return gameComponent.isReturnToMenu();
    }

    public void init(){
        gameComponent.init();
        dataAccessor.resetRecorder();
    }

    public void update(int mouseX, int mouseY){
        gameComponent.update(mouseX, mouseY);
    }

    public void draw(SpriteBatch batch){
        gameComponent.draw(batch);
    }

    public void drawData(SpriteBatch batch, int mouseX, int mouseY, int index){
        dataAccessor.draw(mouseX, mouseY, players[index].getEnergy(), batch);//for dev
    }

    public void setCurrentPlayerToGame(int i){
        gameComponent.setPlayer(players[i]);
    }

    public void touchDown(int mouseX, int mouseY){
        gameComponent.getPlayer().activateCard(mouseX, mouseY);

        gameComponent.getReturnButton().activate(mouseX, mouseY);
    }

    public void touchDragged(int mouseX, int mouseY){
        gameComponent.getPlayer().updateWhenDrag(mouseX, mouseY);
    }

    public void touchUp(int mouseX, int mouseY){
        gameComponent.getBoardArea().playCardOnBoard(mouseX, mouseY);

        gameComponent.getReturnButton().deactivate();
    }

    public void dispose(){
        dataAccessor.dispose();
        gameComponent.dispose();
        for (Player player : players) player.dispose();
    }
}