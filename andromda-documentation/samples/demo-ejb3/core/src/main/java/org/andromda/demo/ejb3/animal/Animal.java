// license-header java merge-point
//
// Attention: Generated code! Do not modify by hand!
// Generated by: Entity.vsl in andromda-ejb3-cartridge.
//
package org.andromda.demo.ejb3.animal;

/**
 * Autogenerated POJO EJB3 implementation class for Animal.
 *
 * Add any manual implementation within this class.  This class will NOT
 * be overwritten with incremental changes.
 */

@javax.persistence.Entity
// Uncomment to enable seam component name
// @org.jboss.seam.annotations.Name("animal")
// Uncomment to set specific component scope type
//@org.jboss.seam.annotations.Scope(org.jboss.seam.ScopeType.CONVERSATION)
@javax.persistence.Table(name = "ANIMAL")
@javax.persistence.EntityListeners({org.andromda.demo.ejb3.animal.AnimalListener.class})
// Uncomment to enable caching for Animal
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL)
@javax.persistence.NamedQueries
({
    @javax.persistence.NamedQuery(name = "Animal.findAll", query = "select animal from Animal AS animal"),
    @javax.persistence.NamedQuery(name = "Animal.findByType", query = "from Animal as animal where animal.type = :type"),
    @javax.persistence.NamedQuery(name = "Animal.deleteByCarnivor", query = "delete from Animal as a where a.carnivor = :carnivor")
})
public class Animal
    extends org.andromda.demo.ejb3.animal.AnimalEmbeddable
    implements java.io.Serializable, Comparable<Animal>
{
    /**
     * The serial version UID of this class required for serialization.
     */
    private static final long serialVersionUID = -5959640054204400691L;

    // --------------- constructors -----------------

    /**
     * Default Animal constructor
     */
    public Animal()
    {
        super();
    }

    /**
     * Implementation for the constructor with all POJO attributes except auto incremented identifiers.
     * This method sets all POJO fields defined in this/super class to the
     * values provided by the parameters.
     *
     */
    public Animal(String name, String type, boolean carnivor)
    {
        super(name, type, carnivor);
    }

    // -------------- Entity Methods -----------------

    // --------------- Lifecycle callbacks -----------------

    /**
     *
     */
    @javax.persistence.PrePersist
    public void prePersist()
    {
        System.out.println("pre persist...");
    }

    /**
     *
     */
    @javax.persistence.PreUpdate
    public void preUpdate()
    {
        System.out.println("pre update...");
    }

    /**
     * @see Comparable#compareTo(T)
     */
    public int compareTo(Animal o)
    {
        int cmp = 0;
        if (this.getName() != null)
        {
            cmp = this.getName().compareTo(o.getName());
        }
        /*else
        {
            if (this.getType() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getType().compareTo(o.getType()));
            }
        }*/
        return cmp;
    }
}