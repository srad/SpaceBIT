package org.ssrad.spacebit.nodes.screens;

import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.ui.Picture;
import org.ssrad.spacebit.enums.GameLevel;
import org.ssrad.spacebit.game.Game;

/**
 * Title screen node.
 *
 * @author Saman Sedighi Rad
 */
public class TitleScreen extends AbstractScreen implements ActionListener {

    Picture background;
    boolean active = true;

    public TitleScreen(Game game) {
        super(game);
    }

    @Override
    protected void init() {
        super.init();

        background = new Picture("TitleScreen");

        background.setImage(game.getAssetManager(), "title-screen.png", true);
        background.setWidth(game.getSettings().getWidth());
        background.setHeight(game.getSettings().getHeight());

        background.setPosition(0, 0);
        game.getGuiNode().attachChild(background);
    }

    public void update(float tpf) {
    }

    @Override
    protected void bindKeys() {
        // You can map one or several inputs to one named action
        inputManager.addMapping("pause", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("help", new KeyTrigger(KeyInput.KEY_H));

        inputManager.addMapping("level_1", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("level_2", new KeyTrigger(KeyInput.KEY_2));

        inputManager.addMapping("copyright", new KeyTrigger(KeyInput.KEY_C));
        inputManager.addMapping("quit", new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addListener(this, new String[]{"pause", "level_1", "level_2", "quit", "help", "copyright"});
    }

    @Override
    public void show() {
        super.show();
        if (game.getGameMusic() != null) {
            game.getGameMusic().stop();
        }
    }

    @Override
    public void onAction(String name, boolean keyPressed, float tpf) {

        if (name.equals("help") && !keyPressed) {
            hide();
            game.getHelpScreen().show();
        }

        if (name.equals("copyright") && !keyPressed) {

            hide();
            game.getCopyrightScreen().show();
        }

        if ((name.equals("level_1") || name.equals("level_2")) && !keyPressed) {
            game.setLevel(name.equals("level_1") ? GameLevel.LEVEL_ONE : GameLevel.LEVEL_TWO);
            game.getLoadScreen().show();
        }

        if (name.equals("quit") && !keyPressed) {
            game.saveSettings();
            game.stop();
        }

        if (name.equals("pause") && !keyPressed) {
            if (game.isLaunched()) {
                if (game.isRunning()) {
                    game.pause();
                } else {
                    game.run();
                }
            }
        }

    }

}
