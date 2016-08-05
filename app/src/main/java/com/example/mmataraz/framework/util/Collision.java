package com.example.mmataraz.framework.util;

import android.util.Log;

import com.example.mmataraz.game.model.Asteroid;
import com.example.mmataraz.game.model.Player;

/**
 * Created by Michael on 6/26/2016.
 */
public class Collision {

    private static Vector2 collisionVector = new Vector2();
    private static Vector2 playerVelocity = new Vector2();
    private static Vector2 asteroidVelocity = new Vector2();

    public static void playerAsteroidCollision(Player p, Asteroid a) {
        // get collision vector from centers
        collisionVector = collisionVector.set(a.getRect().centerX() - p.getRect().centerX(),
                a.getRect().centerY() - p.getRect().centerY());

        // get difference between velocity vectors
        playerVelocity = playerVelocity.set(p.getVelX(), p.getVelY());
        asteroidVelocity = asteroidVelocity.set(Asteroid.getVelX(), 0);
        Vector2 velDiff = asteroidVelocity.sub(playerVelocity);

        // normalize collision vector
        Vector2 cUV = collisionVector.nor();

        // get dot product of normalized collision vector and velocity diff vector
        float dotProduct = cUV.dot(velDiff);

        // get mass ratio
        float massRatio = 2.0f;
        massRatio *= (a.getMass() / (float)(p.getMass() + a.getMass()));

        // get delta velocity vector for player
        // multiply cUV by massRatio*dotProduct
        Vector2 deltaV = cUV.mul(massRatio * dotProduct);

        Log.d("delta X vel", String.valueOf(deltaV.x));
        Log.d("delta Y vel", String.valueOf(deltaV.y));
        p.setVelX(p.getVelX() + deltaV.x);
        p.setVelY(p.getVelY() + deltaV.y);
    }

}
