package org.ssrad.spacebit.nodes.screens;

import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.nodes.AbstractNode;

import java.util.ArrayList;

public class ScoreBar extends AbstractNode {

    private BitmapText score;

    public ScoreBar(Game game) {
        super(game);
    }

    @Override
    protected void init() {
        score = new BitmapText(game.getGuiFont(), false);
        score.setSize(game.getGuiFont().getCharSet().getRenderedSize() * 1.5f);      // font size
        score.setColor(ColorRGBA.White);                             // font color
        score.setLocalTranslation(0, -5, 0.1f); // position

        attachChild(score);
    }

    @Override
    public void update(float tpf) {
        score.setText("Score " + game.getShip().getScore() + "/" + Game.MUST_SCORE);
    }

    @Override
    public void onCollision(AbstractNode collidedWith) {
    }

    @Override
    public ArrayList<AbstractNode> collidesWith() {
        return null;
    }

}
