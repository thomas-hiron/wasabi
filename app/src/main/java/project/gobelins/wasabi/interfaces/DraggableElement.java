package project.gobelins.wasabi.interfaces;

/**
 * Created by ThomasHiron on 13/05/2015.
 */
public interface DraggableElement
{
    /**
     * @return La largeur en fonction du scale
     */
    public int getCustomWidth();

    /**
     * @return La hauteur en fonction du scale
     */
    public int getCustomHeight();

    /**
     * Scale down pour préparer la suppression
     */
    public void scaleToDelete(int eventX, int eventY);

    /**
     * Scale normal
     */
    public void scaleToNormal(int eventX, int eventY);

    /**
     * Si l'objet est en train d'être supprimé au drop
     */
    public boolean isDeleting();

    /**
     * Supprime l'élément
     */
    public void delete(float eventX, float eventY);
}
