package com.gerardivan.chess.screen;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gerardivan.chess.Main;
import com.gerardivan.chess.util.Utils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class OptionScreen implements Screen {

    private Main game;
    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;

    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 600;

    public OptionScreen(Main game) {
        this.game = game;
        this.batch = game.batch;

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        crearUI();
    }

    private void crearUI() {
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        Label titulo = new Label("OPCIONS DE TAULER", skin);
        titulo.setFontScale(2f);

        // Listas desplegables
        SelectBox<String> taulersBox = new SelectBox<>(skin);
        taulersBox.setItems(Utils.getNomTaulers()); // array con nombres de todos los tableros

        SelectBox<String> pecesBox = new SelectBox<>(skin);
        pecesBox.setItems(Utils.getNomPeces()); // array con nombres de piezas

        // Botones
        TextButton btnTornar = new TextButton("Tornar", skin);

        // Listeners
        btnTornar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getActualMenuScreen().recreateStage();
                game.setScreen(game.getActualMenuScreen()); // vuelve al juego
            }
        });

        taulersBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SelectBox<String> box = (SelectBox<String>) actor;
                String selected = box.getSelected();
                Utils.setTauler(selected);
            }
        });

        pecesBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SelectBox<String> box = (SelectBox<String>) actor;
                String selected = box.getSelected();
                Utils.setPeces(selected);
            }
        });


        // Añadir todo a la tabla
        table.add(titulo).padBottom(40);
        table.row();
        table.add(new Label("Tauler:", skin)).pad(10);
        table.add(taulersBox).width(200).pad(10);
        table.row();
        table.add(new Label("Peca:", skin)).pad(10);
        table.add(pecesBox).width(200).pad(10);
        table.row().padTop(20);
        table.row();
        table.add(btnTornar).width(200).height(50).colspan(2).pad(10);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0.8f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
