/*
 * Used to compute the average z value of the geometric center of each
 * polygon (square). This is intended to be used to sort polygons from
 * bottom to top in the z direction so bottom polygons can be drawn first
 * to provide realistic 3D surface hiding of obscured (hidden) lines and
 * faces.
 */

package mainPkg;

class Polygon3D
{
    // Class Fields.

    int x[];
    int y[];
    int zAvg;
    boolean drawn;
    int index;

    // Class Methods.

    // Default Constructor.
    Polygon3D()
    {
        x = new int[4];
        y = new int[4];
        zAvg = 0;
        index = 0;
        drawn = false;
    }

    // Add data for a single point.
    void AddPoint(int xNew, int yNew, int zNew)
    {
        x[index] = xNew;
        y[index] = yNew;
        ++index;

        // Contrary to the textbook algorithm,
        // the zAvg actually holds the sum,
        zAvg += zNew;

        drawn = false;
    }

    // Clear all polygon data.
    void Clear()
    {
        index = 0;
        zAvg = 0;
        drawn = false;
    }
}
