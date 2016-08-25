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
        beginLevel = new UIButton(50, 400, 50 + Assets.begin.getWidth(),
                400 + Assets.begin.getHeight(), Assets.begin, Assets.beginDown);
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
                g.drawString("JUNO CONFLICT --- EARTH I:", 100, 50);
                g.setFont(Typeface.DEFAULT_BOLD, 18);
                g.drawString("COMMANDER, WE'VE BEEN ORDERED TO REMAIN NEUTRAL IN THE JUNO CONFLICT.", 40, 100);
                g.drawString("HOWEVER, YOU AND YOUR TEAM ARE TO TAKE POSITIONS IN THE ASTEROID BELT.", 40, 120);
                g.drawString("DO NOT ENGAGE ANY MARTIAN OR SATURNIAN FORCES UNLESS ATTACKED FIRST.", 40, 140);
                g.drawString("OUR FORCES ARE STRETCHED THIN AND WE CANNOT AFFORD ANY PROLONGED COMBAT ENGAGEMENTS.", 40, 160);
                g.drawString("WE ALSO WANT TO REMAIN A REASONABLE INTERMEDIARY BETWEEN THE MARTIANS AND THE", 40, 180);
                g.drawString("SATURNIANS, WHO ARE HELL BENT IN DESTROYING THE OTHER.", 40, 200);
                g.drawString("ALERT!! MARTIAN INFILTRATORS HAVE APPEARED OVER HIGH EARTH ORBIT! GET TO YOUR SHIPS!!", 40, 240);
                break;

            case MARS:
                g.drawString("JUNO CONFLICT --- MARS I:", 100, 50);
                g.setFont(Typeface.DEFAULT_BOLD, 18);
                g.drawString("COMMANDER, A51 HAS BEEN TASKED TO HAUL ASS TO JUPITER AND SECURE", 40, 100);
                g.drawString("AS MUCH OF THE SYSTEM AS POSSIBLE BEFORE IT FALLS TO THE SATURNIANS.", 40, 120);
                g.drawString("WE NEED YOU AND YOUR FLIGHT TEAM TO PUNCH IN FIRST AND ESTABLISH", 40, 140);
                g.drawString("SPACE SUPERIORITY IN SECTOR 6B BEFORE THE REST OF THE GROUP ARRIVES.", 40, 160);
                g.drawString("RED ALERT!! WE'VE JUST DETECTED NEARBY HOSTILES THAT ARE ALREADY TRYING TO", 40, 200);
                g.drawString("STOP US IN OUR OWN BACKYARD. PROBABLY SATURNIAN SCOUTS THAT WERE HIDING IN THE", 40, 220);
                g.drawString("ASTEROID BELT, BUT KEEP AN EYE OUT FOR PIRATES AND ROGUE EARTH FIGHTERS. GOOD LUCK!!", 80, 240);

                break;

            case SATURN:
                g.drawString("JUNO CONFLICT --- SATURN I:", 100, 50);
                g.setFont(Typeface.DEFAULT_BOLD, 18);
                g.drawString("COMMANDER, THE TIME HAS COME! WE HAVE AWAITED THIS OPPORTUNITY FOR", 40, 100);
                g.drawString("SEVERAL YEARS. EARTH AND MARS ARE IN A DISADVANTAGED POSITION, AND CANNOT", 40, 120);
                g.drawString("CONTINUE TO RELY ON THE ASTEROID BELT TO STAGE OPERATIONS.", 40, 140);
                g.drawString("THE JOVIAN COLLAPSE IS OUR CHANCE TO ESTABLISH A BUFFER TO THE", 40, 160);
                g.drawString("INNER PLANETS, AND DECLARE NAVAL DOMINANCE IN THE OUTER SOLAR SYSTEM.", 40, 180);
                g.drawString("ALL HANDS!! A FACTION OF JOVIAN SECUTIRY FORCES ARE APPROACHING OUR", 40, 220);
                g.drawString("DEFENSE POST OVER ENCELADUS. THEY MAY BE IN LEAGUE WITH ROGUE EARTH FORCES TRYING TO", 40, 240);
                g.drawString("STOP OUR ADVANCE. GET OUT THERE AND LEAD US TO VICTORY, COMMANDER!!", 80, 260);

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
