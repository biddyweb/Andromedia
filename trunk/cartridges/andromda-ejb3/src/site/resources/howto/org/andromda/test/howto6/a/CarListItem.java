// license-header java merge-point
//
// Attention: Generated code! Do not modify by hand!
// Generated by: ValueObject.vsl in andromda-java-cartridge.
//
package org.andromda.test.howto6.a;

/**
 *
 */
public class CarListItem
    implements java.io.Serializable
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -2232633419024752787L;

    public CarListItem()
    {
        this.name = null;
        this.serial = null;
        this.ownerName = null;
    }

    public CarListItem(String name, String serial, String ownerName)
    {
        this.name = name;
        this.serial = serial;
        this.ownerName = ownerName;
    }

    /**
     * Copies constructor from other CarListItem
     *
     * @param otherBean, cannot be <code>null</code>
     * @throws java.lang.NullPointerException if the argument is <code>null</code>
     */
    public CarListItem(CarListItem otherBean)
    {
        this(otherBean.getName(), otherBean.getSerial(), otherBean.getOwnerName());
    }

    /**
     * Copies all properties from the argument value object into this value object.
     */
    public void copy(CarListItem otherBean)
    {
        this.setName(otherBean.getName());
        this.setSerial(otherBean.getSerial());
        this.setOwnerName(otherBean.getOwnerName());
    }

    private String name;

    /**
     *
     */
    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    private String serial;

    /**
     *
     */
    public String getSerial()
    {
        return this.serial;
    }

    public void setSerial(String serial)
    {
        this.serial = serial;
    }

    private String ownerName;

    /**
     *
     */
    public String getOwnerName()
    {
        return this.ownerName;
    }

    public void setOwnerName(String ownerName)
    {
        this.ownerName = ownerName;
    }

}