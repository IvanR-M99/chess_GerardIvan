package com.gerardivan.chess;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Game;
import com.gerardivan.chess.screen.GameScreen;
import com.gerardivan.chess.screen.MenuScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    public SpriteBatch batch;

    private GameScreen actualGameScreen;

    private MenuScreen actualMenuScreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        MenuScreen ms = new MenuScreen(this);
        setActualMenuScreen(ms);
        setScreen(ms);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public void setActualGameScreen(GameScreen gameScreen) {
        this.actualGameScreen = gameScreen;
    }

    public GameScreen getActualGameScreen() {
        return actualGameScreen;
    }

    public void setActualMenuScreen(MenuScreen menuScreen) {
        this.actualMenuScreen = menuScreen;
    }

    public MenuScreen getActualMenuScreen() {
        return actualMenuScreen;
    }

}
