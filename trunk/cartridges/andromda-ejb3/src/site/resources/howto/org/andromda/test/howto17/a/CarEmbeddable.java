// license-header java merge-point
//
// Attention: Generated code! Do not modify by hand!
// Generated by: EntityEmbeddable.vsl in andromda-ejb3-cartridge.
//
package org.andromda.howto2.rental;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * Autogenerated POJO EJB mapped super class for Car containing the
 * bulk of the entity implementation.
 *
 * This is a mapped super class and autogenerated by AndroMDA using the EJB3
 * cartridge.
 *
 * DO NOT MODIFY this class.
 *
 *
 *
 */
@MappedSuperclass
public abstract class CarEmbeddable
    implements Serializable
{
    private static final long serialVersionUID = 8776337268494473937L;

    // ----------- Attribute Definitions ------------

    private String serial;
    private String name;
    private CarType type;
    private Long id;


    // --------- Relationship Definitions -----------

    private Person owner;

    // ---- Manageable Display Attributes (Transient) -----

    private String ownerLabel;       // Manageable display attribute

    // --------------- Constructors -----------------

    /**
     * Default empty constructor
     */
    public CarEmbeddable()
    {
        // default null constructor
    }

    /**
     * Implementation for the constructor with all POJO attributes except auto incremented identifiers.
     * This method sets all POJO fields defined in this class to the values provided by
     * the parameters.
     *
     * @param serial Value for the serial property
     * @param name Value for the name property
     * @param type Value for the type property
     */
    public CarEmbeddable(String serial, String name, CarType type)
    {
        setSerial(serial);
        setName(name);
        setType(type);
    }

    /**
     * Constructor with all POJO attribute values and CMR relations.
     *
     * @param serial Value for the serial property
     * @param name Value for the name property
     * @param type Value for the type property
     * @param owner Value for the owner relation role
     */
    public CarEmbeddable(String serial, String name, CarType type, Person owner)
    {
        setSerial(serial);
        setName(name);
        setType(type);

        setOwner(owner);
    }


    // -------- Attribute Accessors ----------

    /**
     * Get the serial property.
     *
     * @return String The value of serial
     */
    @Column(name = "SERIAL", unique = true, nullable = false, insertable = true, updatable = true, length = 50)
    public String getSerial()
    {
        return serial;
    }

    /**
     * Set the serial property.
     * @param value the new value
     */
    public void setSerial(String value)
    {
        this.serial = value;
    }

    /**
     * Get the name property.
     *
     * @return String The value of name
     */
    @Column(name = "NAME", nullable = false, insertable = true, updatable = true, length = 50)
    public String getName()
    {
        return name;
    }

    /**
     * Set the name property.
     * @param value the new value
     */
    public void setName(String value)
    {
        this.name = value;
    }

    /**
     * Get the type property.
     *
     * @return CarType The value of type
     */
    @Column(name = "TYPE", nullable = false, insertable = true, updatable = true, columnDefinition = "VARCHAR(20) NOT NULL")
    @Enumerated(EnumType.STRING)
    public CarType getType()
    {
        return type;
    }

    /**
     * Set the type property.
     * @param value the new value
     */
    public void setType(CarType value)
    {
        this.type = value;
    }

    /**
     * Get the id property.
     *
     * @return Long The value of id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false, insertable = true, updatable = true)
    public Long getId()
    {
        return id;
    }

    /**
     * Set the id property.
     * @param value the new value
     */
    public void setId(Long value)
    {
        this.id = value;
    }


    // ------------- Relations ------------------

    /**
     * Get the owner
     *
     * @return Person
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "OWNER")
    public Person getOwner()
    {
        return this.owner;
    }

   /**
    * Set the owner
    *
    * @param owner
    */
    public void setOwner(Person owner)
    {
        this.owner = owner;
    }

    // -------- Manageable Attribute Display -----------

    /**
     * Get the ownerLabel
     *
     * @return String
     */
    @Transient
    public String getOwnerLabel()
    {
        return this.ownerLabel;
    }

    /**
     * Set the ownerLabel
     *
     * @param ownerLabel
     */
    public void setOwnerLabel (String ownerLabel)
    {
        this.ownerLabel = ownerLabel;
    }

    // -------- Common Methods -----------

    /**
     * Indicates if the argument is of the same type and all values are equal.
     *
     * @param object The target object to compare with
     * @return boolean True if both objects a 'equal'
     */
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof CarEmbeddable))
        {
            return false;
        }
        final CarEmbeddable that = (CarEmbeddable)object;
        if (this.getId() == null || that.getId() == null || !this.getId().equals(that.getId()))
        {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code value for the object
     *
     * @return int The hash code value
     */
    public int hashCode()
    {
        int hashCode = 0;
        hashCode = 29 * hashCode + (getId() == null ? 0 : getId().hashCode());

        return hashCode;
    }

    /**
     * Returns a String representation of the object
     *
     * @return String Textual representation of the object displaying name/value pairs for all attributes
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CarEmbeddable(=");
        sb.append("serial: ");
        sb.append(getSerial());
        sb.append(", name: ");
        sb.append(getName());
        sb.append(", type: ");
        sb.append(getType());
        sb.append(", id: ");
        sb.append(getId());
        sb.append(")");
        return sb.toString();
    }
}
