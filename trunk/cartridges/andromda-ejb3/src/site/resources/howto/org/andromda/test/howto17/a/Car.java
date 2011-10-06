// license-header java merge-point
//
// Attention: Generated code! Do not modify by hand!
// Generated by: Entity.vsl in andromda-ejb3-cartridge.
//
package org.andromda.howto2.rental;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Autogenerated POJO EJB3 implementation class for Car.
 *
 * Add any manual implementation within this class.  This class will NOT
 * be overwritten with incremental changes.
 *
 *
 *
 */

@Entity
// Uncomment to enable seam component name
// @org.jboss.seam.annotations.Name("car")
// Uncomment to set specific component scope type
//@org.jboss.seam.annotations.Scope(org.jboss.seam.ScopeType.CONVERSATION)
@Table(name = "CAR")
// Uncomment to enable entity listener for Car
// @javax.persistence.EntityListeners({org.andromda.howto2.rental.CarListener.class})
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries
({
    @NamedQuery(name = "Car.findAll", query = "select car from Car AS car"),
    @NamedQuery(name = "Car.findByType", query = "from Car as car where car.type = :type")
})
public class Car
    extends CarEmbeddable
    implements Serializable
{
    /**
     * The serial version UID of this class required for serialization.
     */
    private static final long serialVersionUID = 8776337268494473937L;

    // --------------- constructors -----------------

    /**
     * Default Car constructor
     */
    public Car()
    {
        super();
    }

    /**
     * Implementation for the constructor with all POJO attributes except auto incremented identifiers.
     * This method sets all POJO fields defined in this/super class to the
     * values provided by the parameters.
     *
     */
    public Car(String serial, String name, CarType type)
    {
        super(serial, name, type);
    }

    /**
     * Constructor with all POJO attribute values and CMR relations.
     *
     * @param serial Value for the serial property
     * @param name Value for the name property
     * @param type Value for the type property
     * @param owner Value for the owner relation role
     */
    public Car(String serial, String name, CarType type, Person owner)
    {
        super(serial, name, type, owner);
    }


    // -------------- Entity Methods -----------------

    /**
     *
     */
    @Transient
    public boolean isRented()
    {
        // TODO put your implementation here.
        return false;
    }

    /**
     *
     */
    @Transient
    public static boolean allCarsAreRented()
    {
        // TODO put your implementation here.
        return false;
    }


    // --------------- Lifecycle callbacks -----------------

}