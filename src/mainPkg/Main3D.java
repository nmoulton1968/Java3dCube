/*
 * Draw a 3D cube. With scaling, rotation, and transformation.
 */

package mainPkg;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

public class Main3D extends JFrame
{
    // Class Fields.

    // We don't use it, but required to prevent warning.
    public static final long serialVersionUID = 0xB2FE3694;

    Timer myTimer = new Timer();

    // Translate Controls.
    Label lblTranslate = new Label("Translate", Label.CENTER);
    Button btnMinusTX = new Button("-TX");
    TextField txtFieldTX = new TextField(8);
    Button btnPlusTX = new Button("+TX");
    Button btnMinusTY = new Button("-TY");
    TextField txtFieldTY = new TextField(8);
    Button btnPlusTY = new Button("+TY");

    // Scale Controls.
    Label lblScale = new Label("Scale", Label.CENTER);
    Button btnMinusS = new Button("-S");
    TextField txtFieldS = new TextField(8);
    Button btnPlusS = new Button("+S");

    // Rotation Controls.
    Label lblRotate = new Label("Rotate", Label.CENTER);
    Button btnMinusRX = new Button("-RX");
    TextField txtFieldRX = new TextField(8);
    Button btnPlusRX = new Button("+RX");
    Button btnMinusRY = new Button("-RY");
    TextField txtFieldRY = new TextField(8);
    Button btnPlusRY = new Button("+RY");
    Button btnMinusRZ = new Button("-RZ");
    TextField txtFieldRZ = new TextField(8);
    Button btnPlusRZ = new Button("+RZ");

    // Miscellaneous Controls.
    Button btnReset = new Button("Reset");
    Checkbox btnGhosting = new Checkbox("Ghosting", null, false);
    Checkbox btnOutline = new Checkbox("Black Outline", null, true);
    Checkbox btnAutoRotate = new Checkbox("Auto Rotate", null, false);

    // Define the window dimensions.
    private final int mWidthPane = 800;
    private final int mHeightPane = 800;

    // Flag set by anybody, read by paint().
    private boolean mNeedsRefresh = false;

    // Declare transform parameters.
    private double fTX;  // Translation in X+.
    private double fTY;  // Translation in Y+.
    private double fS;   // Scaling on all axis.
    private double fRX;  // Rotation about X axis.
    private double fRY;  // Rotation about Y axis.
    private double fRZ;  // Rotation about Z axis.

    // Define viewport offsets.
    private final int xOff = 150;
    private final int yOff = 150;

    // Define the 8 points of the cube.
    private Point3D p0 = new Point3D(-50,-50,-50);
    private Point3D p1 = new Point3D(50,-50,-50);
    private Point3D p2 = new Point3D(50, 50,-50);
    private Point3D p3 = new Point3D(-50, 50,-50);
    private Point3D p4 = new Point3D(-50,-50, 50);
    private Point3D p5 = new Point3D(50,-50, 50);
    private Point3D p6 = new Point3D(50, 50, 50);
    private Point3D p7 = new Point3D(-50, 50, 50);

    // Define the 6 cube face objects.
    private Polygon3D s0 = new Polygon3D();
    private Polygon3D s1 = new Polygon3D();
    private Polygon3D s2 = new Polygon3D();
    private Polygon3D s3 = new Polygon3D();
    private Polygon3D s4 = new Polygon3D();
    private Polygon3D s5 = new Polygon3D();

    // Define the transform matrix.
    private Matrix3D matrix = new Matrix3D();

    // Class Methods.

    // Default constructor.
    public Main3D()
    {
        super("3D Cube");
        setSize(mWidthPane, mHeightPane);
        setVisible(true);
    }

    // Entry point with exit logic.
    public static void main(String args[])
    {
        Main3D app = new Main3D();
        app.Initialize();

        app.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
    }

    // Perform all initial actions needed at program start-up.
    public void Initialize()
    {
        int width = 770;
        setLayout(null);

        ResetParams();
        TimedUpdate();
        UpdateCube();

        // Translate buttons.
        add(lblTranslate);
        add(btnMinusTX);
        add(txtFieldTX);
        add(btnPlusTX);
        add(btnMinusTY);
        add(txtFieldTY);
        add(btnPlusTY);

        lblTranslate.setBounds(width-140, 10, 140, 30);
        btnMinusTX.setBounds(width-125, 40, 30,  30);
        txtFieldTX.setBounds(width-90,  40, 50,  30);
        btnPlusTX.setBounds(width-35,  40, 30,  30);
        btnMinusTY.setBounds(width-125, 75, 30,  30);
        txtFieldTY.setBounds(width-90,  75, 50,  30);
        btnPlusTY.setBounds(width-35,  75, 30,  30);

        // Scale buttons.
        add(lblScale);
        add(btnMinusS);
        add(txtFieldS);
        add(btnPlusS);

        lblScale.setBounds(width-140, 105, 140, 30);
        btnMinusS.setBounds(width-125, 135, 30,  30);
        txtFieldS.setBounds(width-90,  135, 50,  30);
        btnPlusS.setBounds(width-35,  135, 30,  30);

        // Rotate buttons.
        add(lblRotate);
        add(btnMinusRX);
        add(txtFieldRX);
        add(btnPlusRX);
        add(btnMinusRY);
        add(txtFieldRY);
        add(btnPlusRY);
        add(btnMinusRZ);
        add(txtFieldRZ);
        add(btnPlusRZ);

        lblRotate.setBounds(width-140, 165, 140, 30);
        btnMinusRX.setBounds(width-125, 195,  30, 30);
        txtFieldRX.setBounds(width-90,  195,  50, 30);
        btnPlusRX.setBounds(width-35,  195,  30, 30);
        btnMinusRY.setBounds(width-125, 230,  30, 30);
        txtFieldRY.setBounds(width-90,  230,  50, 30);
        btnPlusRY.setBounds(width-35,  230,  30, 30);
        btnMinusRZ.setBounds(width-125, 265,  30, 30);
        txtFieldRZ.setBounds(width-90,  265,  50, 30);
        btnPlusRZ.setBounds(width-35,  265,  30, 30);

        // Miscellaneous buttons.
        add(btnReset);
        add(btnGhosting);
        add(btnOutline);
        add(btnAutoRotate);

        btnReset.setBounds(width-110, 310, 90,  30);
        btnGhosting.setBounds(width-110, 345, 140, 30);
        btnOutline.setBounds(width-110, 370, 120, 30);
        btnAutoRotate.setBounds(width-110, 395, 120, 30);

        // Set a property of text fields.
        txtFieldTX.setEditable(true);
        txtFieldTY.setEditable(true);
        txtFieldS.setEditable(true);
        txtFieldRX.setEditable(true);
        txtFieldRY.setEditable(true);
        txtFieldRZ.setEditable(true);
    }

    // Draw all screen elements on GUI.
    public void paint(Graphics g)
    {
        int iHighestZ;
        int iSidesRemain;

        // Ghosting means show previous images of cube, don't erase them.
        if(btnGhosting.getState() == false || mNeedsRefresh)
        {
            // No ghosting; clear entire draw pane.
            g.clearRect(0, 0, mWidthPane, mHeightPane);

            mNeedsRefresh = false;
        }

        s0.Clear();
        s0.AddPoint((int)p0.x+xOff, (int)p0.y+yOff, (int)p0.z);
        s0.AddPoint((int)p1.x+xOff, (int)p1.y+yOff, (int)p1.z);
        s0.AddPoint((int)p2.x+xOff, (int)p2.y+yOff, (int)p2.z);
        s0.AddPoint((int)p3.x+xOff, (int)p3.y+yOff, (int)p3.z);

        s1.Clear();
        s1.AddPoint((int)p4.x+xOff, (int)p4.y+yOff, (int)p4.z);
        s1.AddPoint((int)p5.x+xOff, (int)p5.y+yOff, (int)p5.z);
        s1.AddPoint((int)p6.x+xOff, (int)p6.y+yOff, (int)p6.z);
        s1.AddPoint((int)p7.x+xOff, (int)p7.y+yOff, (int)p7.z);

        s2.Clear();
        s2.AddPoint((int)p0.x+xOff, (int)p0.y+yOff, (int)p0.z);
        s2.AddPoint((int)p1.x+xOff, (int)p1.y+yOff, (int)p1.z);
        s2.AddPoint((int)p5.x+xOff, (int)p5.y+yOff, (int)p5.z);
        s2.AddPoint((int)p4.x+xOff, (int)p4.y+yOff, (int)p4.z);

        s3.Clear();
        s3.AddPoint((int)p1.x+xOff, (int)p1.y+yOff, (int)p1.z);
        s3.AddPoint((int)p2.x+xOff, (int)p2.y+yOff, (int)p2.z);
        s3.AddPoint((int)p6.x+xOff, (int)p6.y+yOff, (int)p6.z);
        s3.AddPoint((int)p5.x+xOff, (int)p5.y+yOff, (int)p5.z);

        s4.Clear();
        s4.AddPoint((int)p2.x+xOff, (int)p2.y+yOff, (int)p2.z);
        s4.AddPoint((int)p3.x+xOff, (int)p3.y+yOff, (int)p3.z);
        s4.AddPoint((int)p7.x+xOff, (int)p7.y+yOff, (int)p7.z);
        s4.AddPoint((int)p6.x+xOff, (int)p6.y+yOff, (int)p6.z);

        s5.Clear();
        s5.AddPoint((int)p0.x+xOff, (int)p0.y+yOff, (int)p0.z);
        s5.AddPoint((int)p3.x+xOff, (int)p3.y+yOff, (int)p3.z);
        s5.AddPoint((int)p7.x+xOff, (int)p7.y+yOff, (int)p7.z);
        s5.AddPoint((int)p4.x+xOff, (int)p4.y+yOff, (int)p4.z);

        iSidesRemain = 6;

        while(iSidesRemain > 0)
        {
            // sort sides by z value
            iHighestZ = Integer.MIN_VALUE;
            if((s0.zAvg > iHighestZ) && !s0.drawn)
                iHighestZ = s0.zAvg;
            if((s1.zAvg > iHighestZ) && !s1.drawn)
                iHighestZ = s1.zAvg;
            if((s2.zAvg > iHighestZ) && !s2.drawn)
                iHighestZ = s2.zAvg;
            if((s3.zAvg > iHighestZ) && !s3.drawn)
                iHighestZ = s3.zAvg;
            if((s4.zAvg > iHighestZ) && !s4.drawn)
                iHighestZ = s4.zAvg;
            if((s5.zAvg > iHighestZ) && !s5.drawn)
                iHighestZ = s5.zAvg;

            // Draw the highest side next.
            if(s0.zAvg == iHighestZ)
            {
                g.setColor(Color.blue);
                g.fillPolygon(s0.x, s0.y, 4);
                if(btnOutline.getState() == true)
                {
                    g.setColor(Color.black);
                    g.drawPolygon(s0.x, s0.y, 4);
                }
                s0.drawn = true;
            }
            if(s1.zAvg == iHighestZ)
            {
                g.setColor(Color.red);
                g.fillPolygon(s1.x, s1.y, 4);
                if(btnOutline.getState() == true)
                {
                    g.setColor(Color.black);
                    g.drawPolygon(s1.x, s1.y, 4);
                }
                s1.drawn = true;
            }
            if(s2.zAvg == iHighestZ)
            {
                g.setColor(Color.green);
                g.fillPolygon(s2.x, s2.y, 4);
                if(btnOutline.getState() == true)
                {
                    g.setColor(Color.black);
                    g.drawPolygon(s2.x, s2.y, 4);
                }
                s2.drawn = true;
            }
            if(s3.zAvg == iHighestZ)
            {
                g.setColor(Color.yellow);
                g.fillPolygon(s3.x, s3.y, 4);
                if(btnOutline.getState() == true)
                {
                    g.setColor(Color.black);
                    g.drawPolygon(s3.x, s3.y, 4);
                }
                s3.drawn = true;
            }
            if(s4.zAvg == iHighestZ)
            {
                g.setColor(Color.gray);
                g.fillPolygon(s4.x, s4.y, 4);
                if(btnOutline.getState() == true)
                {
                    g.setColor(Color.black);
                    g.drawPolygon(s4.x, s4.y, 4);
                }
                s4.drawn = true;
            }
            if(s5.zAvg == iHighestZ)
            {
                g.setColor(Color.magenta);
                g.fillPolygon(s5.x, s5.y, 4);
                if(btnOutline.getState() == true)
                {
                    g.setColor(Color.black);
                    g.drawPolygon(s5.x, s5.y, 4);
                }
                s5.drawn = true;
            }

            --iSidesRemain;
        }

        /*
        // Draw all cube side edges, showing even the non-visible ones.
        g.setColor(Color.black);
        g.drawLine((int)p0.x+xOff,(int)p0.y+yOff,(int)p1.x+xOff,(int)p1.y+yOff);
        g.drawLine((int)p1.x+xOff,(int)p1.y+yOff,(int)p2.x+xOff,(int)p2.y+yOff);
        g.drawLine((int)p2.x+xOff,(int)p2.y+yOff,(int)p3.x+xOff,(int)p3.y+yOff);
        g.drawLine((int)p3.x+xOff,(int)p3.y+yOff,(int)p0.x+xOff,(int)p0.y+yOff);
        g.drawLine((int)p4.x+xOff,(int)p4.y+yOff,(int)p5.x+xOff,(int)p5.y+yOff);
        g.drawLine((int)p5.x+xOff,(int)p5.y+yOff,(int)p6.x+xOff,(int)p6.y+yOff);
        g.drawLine((int)p6.x+xOff,(int)p6.y+yOff,(int)p7.x+xOff,(int)p7.y+yOff);
        g.drawLine((int)p7.x+xOff,(int)p7.y+yOff,(int)p4.x+xOff,(int)p4.y+yOff);
        g.drawLine((int)p0.x+xOff,(int)p0.y+yOff,(int)p4.x+xOff,(int)p4.y+yOff);
        g.drawLine((int)p1.x+xOff,(int)p1.y+yOff,(int)p5.x+xOff,(int)p5.y+yOff);
        g.drawLine((int)p2.x+xOff,(int)p2.y+yOff,(int)p6.x+xOff,(int)p6.y+yOff);
        g.drawLine((int)p3.x+xOff,(int)p3.y+yOff,(int)p7.x+xOff,(int)p7.y+yOff);
         */

        // Update the variable values back to the text controls.
        txtFieldTX.setText(""+fTX);
        txtFieldTY.setText(""+fTY);
        txtFieldS.setText(""+fS);
        txtFieldRX.setText(""+fRX);
        txtFieldRY.setText(""+fRY);
        txtFieldRZ.setText(""+fRZ);
    }

    // The Event handler gets called when user clicks any control.
    public boolean handleEvent(Event evt)
    {
        if(evt.id == Event.ACTION_EVENT)
        {
            if("Reset".equals(evt.arg))
            {
                ResetParams();
            }
            else if("+TX".equals(evt.arg))
            {
                fTX += 10;
                txtFieldTX.setText(""+fTX);
            }
            else if("-TX".equals(evt.arg))
            {
                fTX -= 10;
                txtFieldTX.setText(""+fTX);
            }
            else if("+TY".equals(evt.arg))
            {
                fTY += 10;
                txtFieldTY.setText(""+fTY);
            }
            else if("-TY".equals(evt.arg))
            {
                fTY -= 10;
                txtFieldTY.setText(""+fTY);
            }
            else if("+S".equals(evt.arg))
            {
                fS *= 1.1;
                txtFieldS.setText(""+fS);
            }
            else if("-S".equals(evt.arg))
            {
                fS /= 1.1;
                txtFieldS.setText(""+fS);
            }
            else if("+RX".equals(evt.arg))
            {
                fRX += 5;
                txtFieldRX.setText(""+fRX);
            }
            else if("-RX".equals(evt.arg))
            {
                fRX -= 5;
                txtFieldRX.setText(""+fRX);
            }
            else if("+RY".equals(evt.arg))
            {
                fRY += 5;
                txtFieldRY.setText(""+fRY);
            }
            else if("-RY".equals(evt.arg))
            {
                fRY -= 5;
                txtFieldRY.setText(""+fRY);
            }
            else if("+RZ".equals(evt.arg))
            {
                fRZ += 5;
                txtFieldRZ.setText(""+fRZ);
            }
            else if("-RZ".equals(evt.arg))
            {
                fRZ -= 5;
                txtFieldRZ.setText(""+fRZ);
            }

            // Regardless which action event, always update;
            UpdateCube();

            return true;
        }

        return false;
    }

    public void TimedUpdate()
    {
        myTimer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                if(btnAutoRotate.getState() == true)
                {
                    Double dbl;
                    String str;
                    str = txtFieldRZ.getText();
                    dbl = Double.valueOf(str);
                    dbl += 1;
                    txtFieldRZ.setText("" + dbl);
                    UpdateCube();
                }
            }
        }, 0, 50);
    }

    // Read values from all GUI inputs and use them
    // to apply transforms to cube points.
    public void UpdateCube()
    {
        String sBuff;
        Double DoubleTemp;

        try
        {
            sBuff = txtFieldTX.getText();
            DoubleTemp = Double.valueOf(sBuff);
            fTX = DoubleTemp.doubleValue();

            sBuff = txtFieldTY.getText();
            DoubleTemp = Double.valueOf(sBuff);
            fTY = DoubleTemp.doubleValue();

            sBuff = txtFieldS.getText();
            DoubleTemp = Double.valueOf(sBuff);
            fS = DoubleTemp.doubleValue();

            sBuff = txtFieldRX.getText();
            DoubleTemp = Double.valueOf(sBuff);
            fRX = DoubleTemp.doubleValue();

            sBuff = txtFieldRY.getText();
            DoubleTemp = Double.valueOf(sBuff);
            fRY = DoubleTemp.doubleValue();

            sBuff = txtFieldRZ.getText();
            DoubleTemp = Double.valueOf(sBuff);
            fRZ = DoubleTemp.doubleValue();
        }
        catch(Exception e)
        {
            ;
        }

        // Reset points.
        p0.Set(-50,-50,-50);
        p1.Set(50,-50,-50);
        p2.Set(50, 50,-50);
        p3.Set(-50, 50,-50);
        p4.Set(-50,-50, 50);
        p5.Set(50,-50, 50);
        p6.Set(50, 50, 50);
        p7.Set(-50, 50, 50);

        // Set initial transforms.
        matrix.Clear();
        //matrix.Rotate(45,45,10);

        // Apply the user transforms to matrix.
        matrix.Rotate(fRX,fRY,fRZ);
        matrix.Scale(fS);
        matrix.Translate(fTX,fTY,0);

        // Apply the current transform matrix.
        p0.Transform(matrix);
        p1.Transform(matrix);
        p2.Transform(matrix);
        p3.Transform(matrix);
        p4.Transform(matrix);
        p5.Transform(matrix);
        p6.Transform(matrix);
        p7.Transform(matrix);

        repaint();
    }

    // Reset size and position back to initial conditions.
    public void ResetParams()
    {
        fTX = 130;  // Translation in X.
        fTY = 160;  // Translation in Y.
        fS = 3;     // Scaling.
        fRX = 30;   // Rotation about X axis.
        fRY = 40;   // Rotation about Y axis.
        fRZ = 20.4; // Rotation about Z axis.

        txtFieldTX.setText(""+fTX);
        txtFieldTY.setText(""+fTY);
        txtFieldS.setText(""+fS);
        txtFieldRX.setText(""+fRX);
        txtFieldRY.setText(""+fRY);
        txtFieldRZ.setText(""+fRZ);

        mNeedsRefresh = true;
    }
}
