import java.util.ArrayList;

/**
 * Created by asd on 4/26/2018.
 */
public class Hypothesis {


    private ArrayList<String> attributeTypeName;
    private ArrayList<String > attributeDecision;
    private int attributeIndex;
    private double splitPoint = 0;
    private boolean isContinuous = false;



    public Hypothesis(ArrayList<String> attributeTypeName, ArrayList<String > attributeDecision, int attributeIndex, double splitPoint, boolean isContinuous) {
        this.attributeTypeName = attributeTypeName;
        this.attributeDecision = attributeDecision;
        this.attributeIndex = attributeIndex;
        this.splitPoint = splitPoint;
        this.isContinuous = isContinuous;
    }


    public double getSplitPoint() {
        return splitPoint;
    }

    public void setSplitPoint(double splitPoint) {
        this.splitPoint = splitPoint;
    }

    public ArrayList<String> getAttributeTypeName() {
        return attributeTypeName;
    }

    public void setAttributeTypeName(ArrayList<String> attributeTypeName) {
        this.attributeTypeName = attributeTypeName;
    }

    public ArrayList<String> getAttributeDecision() {
        return attributeDecision;
    }

    public void setAttributeDecision(ArrayList<String> attributeDecision) {
        this.attributeDecision = attributeDecision;
    }

    public int getAttributeIndex() {
        return attributeIndex;
    }

    public void setAttributeIndex(int attributeIndex) {
        this.attributeIndex = attributeIndex;
    }

    public boolean isContinuous() {
        return isContinuous;
    }

    public void setContinuous(boolean continuous) {
        isContinuous = continuous;
    }



    public void print() {
        System.out.println("======== Hypothesis information ========" );
        System.out.println("Attribute Index : " + attributeIndex);
        System.out.println("Decision Map");

        for(int i=0; i<attributeDecision.size(); i++){
            System.out.println(attributeTypeName.get(i) + " " + attributeDecision.get(i));
        }

        if(isContinuous) {
            System.out.println("Continuous!");
            System.out.println("Split point : " + splitPoint);
        }
        else {
            System.out.println("Discontinuous");
        }
    }

}
