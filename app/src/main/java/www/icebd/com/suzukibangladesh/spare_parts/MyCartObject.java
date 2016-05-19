package www.icebd.com.suzukibangladesh.spare_parts;

/**
 * Created by Momen Dewan on 4/29/2016.
 */
public class MyCartObject
{

    private SparePartsListObject.SparePartsItem sparePartsListObject;
    private int quantity;

    public MyCartObject(){}
    public MyCartObject(SparePartsListObject.SparePartsItem sparePartsListObject, int quantity)
    {
        this.sparePartsListObject = sparePartsListObject;
        this.quantity = quantity;
    }

    public void setSparePartsListObject(SparePartsListObject.SparePartsItem sparePartsListObject)
    {
        this.sparePartsListObject = sparePartsListObject;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public SparePartsListObject.SparePartsItem getSparePartsListObject()
    {
        return sparePartsListObject;
    }

    public int getQuantity()
    {
        return quantity;
    }
}
