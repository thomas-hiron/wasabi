package project.gobelins.wasabi.utils;

/**
 * Created by ThomasHiron on 21/05/2015.
 */
public class Hypo
{
    /**
     * @param width  La taille
     * @param height La hauteur
     * @return Le rayon de fin
     */
    public static float hypo(int width, int height)
    {
        return (float) Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
    }
}
