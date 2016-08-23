package com.example.mmataraz.game.state;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;

import com.example.mmataraz.framework.util.Painter;
import com.example.mmataraz.framework.util.UIButton;
import com.example.mmataraz.projectsol.Assets;

/**
 * Created by Michael on 8/22/2016.
 */
public class BriefingState extends State {

    private UIButton beginLevel;
    private PlayStateLevel currentLevel;

    // PlayState Constructor
    public BriefingState(PlayStateLevel level) {
        currentLevel = level;
    }

    @Override
    public void init() {
        beginLevel = new UIButton(250, 50, 250 + Assets.begin.getWidth(),
                50 + Assets.begin.getHeight(), Assets.begin, Assets.beginDown);
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onExit() {
        Assets.unloadMenuAssets();
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render(Painter g) {
        g.drawImage(Assets.welcome, 0, 0);

        g.setColor(Color.WHITE);
        g.setFont(Typeface.DEFAULT_BOLD, 32);

        // draw applicable text and graphics
        switch (currentLevel) {
            case EARTH:
                g.drawString("JUNO CONFLICT --- EARTH I:", 300, 50);
                g.setFont(Typeface.DEFAULT_BOLD, 18);
                g.drawString("COMMANDER, WE'VE BEEN ORDERED TO REMAIN NEUTRAL IN THE JUNO CONFLICT. HOWEVER, YOU AND YOUR TEAM ARE TO TAKE POSITIONS IN THE ASTEROID BELT.\n" +
                        "DO NOT ENGAGE ANY MARTIAN OR SATURNIAN FORCES UNLESS ATTACKED FIRST. OUR FORCES ARE STRETCHED THIN AND WE CANNOT AFFORD ANY PROLONGED COMBAT ENGAGEMENTS.\n" +
                        "WE ALSO WANT TO REMAIN A REASONABLE INTERMEDIARY BETWEEN THE MARTIANS AND THE SATURNIANS, WHO ARE HELL BENT IN DESTROYING THE OTHER.", 300, 160);
                break;

            case MARS:
                g.drawString("JUNO CONFLICT --- MARS I:", 300, 50);
                g.setFont(Typeface.DEFAULT_BOLD, 18);
                g.drawString("COMMANDER, A51 HAS BEEN TASKED TO HAUL ASS TO JUPITER AND SECURE AS MUCH OF THE SYSTEM AS POSSIBLE BEFORE IT FALLS TO THE SATURNIANS.\n" +
                        "WE NEED YOU AND YOUR FLIGHT TEAM TO PUNCH IN FIRST AND ESTABLISH SPACE SUPERIORITY IN SECTOR 6B BEFORE THE REST OF THE GROUP ARRIVES.\n" +
                        "SO GET GOING!! WE'VE JUST DETECTED NEARBY HOSTILES THAT ARE ALREADY TRYING TO STOP US IN OUR OWN BACKYARD.\n" +
                        "PROBABLY SATURNIAN SCOUTS THAT WERE HIDING IN THE ASTEROID BELT, BUT KEEP AN EYE OUT FOR PIRATES AND ROGUE EARTH FIGHTERS.\n" +
                        "GOOD LUCK!!", 300, 160);

                break;

            default:

                break;
        }

        beginLevel.render(g);
    }

    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            beginLevel.onTouchDown(scaledX, scaledY);
        }

        if (e.getAction() == MotionEvent.ACTION_UP) {
            // If the play button is active and the release was within the play button:
            if (beginLevel.isPressed(scaledX, scaledY)) {
                // Button has been released.
                beginLevel.cancel();
                // Perform an action here!
                Log.d("BriefingState", "Begin level!");
                setCurrentState(new LoadState(this, new PlayState(/*PlayStateLevel.EARTH*/ currentLevel)));

                // If score button is active and the release was within the score button:
            } else {
                // Cancel all actions.
                beginLevel.cancel();
            }
        }

        return true;
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

}
