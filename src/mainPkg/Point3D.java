/*
 * Holds a point in 3 floating point coordinates and provides simple
 * manipulation methods. Allows simple public access to its x, y, z fields.
 */

package mainPkg;

class Point3D
{
    // Class Fields.

    // 3D coordinates.
    public double x;
    public double y;
    public double z;

    // Class Methods.

    // Default Constructor.
    Point3D()
    {
        this(0, 0, 0);
    }

    // Constructor with arguments.
    Point3D(double x0, double y0, double z0)
    {
        x = x0;
        y = y0;
        z = z0;
    }

    // Set all 3 coordinates.
    void Set(double x0, double y0, double z0)
    {
        x = x0;
        y = y0;
        z = z0;
    }

    // Set point to origin.
    void Clear()
    {
        x = 0;
        y = 0;
        z = 0;
    }

    // Transform the point by the given matrix.
    void Transform(Matrix3D matrix)
    {
        // Save current values, for use in calculations.
        double x0 = x;
        double y0 = y;
        double z0 = z;

        // Multiply the point vector by the transform matrix.
        x = x0*matrix.c00() + y0*matrix.c01() + z0*matrix.c02() + matrix.c03();
        y = x0*matrix.c10() + y0*matrix.c11() + z0*matrix.c12() + matrix.c13();
        z = x0*matrix.c20() + y0*matrix.c21() + z0*matrix.c22() + matrix.c23();
    }

    // Output for debugging.
    void Print()
    {
        System.out.println("Point3D Output:");
        System.out.println("x= "+x+"\ty= "+y+"\tz= "+z);
    }
}
