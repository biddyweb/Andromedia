/*
 */
package org.andromda.cartridges.hibernate.metafacades;

import java.util.ArrayList;
import java.util.Collection;

import org.andromda.cartridges.hibernate.HibernateProfile;
import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ExceptionRecorder;
import org.andromda.metafacades.uml.EntityAttributeFacade;
import org.andromda.metafacades.uml.EntityFacade;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * <p>
 * Provides support for the hibernate inheritance strategies of class (table per
 * hierarchy), subclass (table per subclass in hierarchy) and concrete (table
 * per class). With concrete the strategy can be changed lower down. Also
 * provides for the root class being defined as an interface and the attributes
 * remapped to the subclasses. This is useful in the concrete case becuase it
 * has limitations in the associations.
 * </p>
 * <p>
 * Also provides support for not generating the impls which is useful when using
 * subclass mode.
 * </p>
 * <p>
 * The tagged value of <code>@andromda.hibernate.inheritance</code> 
 * is set on the base/root class. All subclasses must then follow the same
 * strategy. NB if the strategy is changed after the initial generation, the 
 * impl classes have to be hand modified.
 * </p>
 * @author Martin West
 * @author Carlos Cuenca
 */
public class HibernateEntityLogicImpl
    extends HibernateEntityLogic
    implements org.andromda.cartridges.hibernate.metafacades.HibernateEntity
{

    public HibernateEntityLogicImpl(
        java.lang.Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * Value for one Table per root class
     */
    private static final String INHERITANCE_STRATEGY_CLASS = "class";

    /**
     * Value for joined-subclass
     */
    private static final String INHERITANCE_STRATEGY_SUBCLASS = "subclass";

    /**
     * Value for one Table per concrete class
     */
    private static final String INHERITANCE_STRATEGY_CONCRETE = "concrete";

    /**
     * Value make Entity an interface, delegate attributes to subclasses.
     */
    private static final String INHERITANCE_STRATEGY_INTERFACE = "interface";

    /**
     * Return all the business operations, used when leafImpl true.
     * 
     * @return all business operations
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getAllBusinessOperations()
     */
    protected Collection handleGetAllBusinessOperations()
    {
        EntityFacade superElement = (EntityFacade)this.getGeneralization();

        Collection result = super.getBusinessOperations();
        while (superElement != null)
        {
            result.addAll(superElement.getBusinessOperations());
            superElement = (EntityFacade)superElement.getGeneralization();
        }
        return result;
    }

    /**
     * Return true if this Entity is a root in terms of Hibernate, eq has a
     * hbm.xml file. interface - false
     * 
     * @return true if this Entity is a root
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isRootInheritanceEntity()
     */
    protected boolean handleIsRootInheritanceEntity()
    {
        if (logger.isDebugEnabled())
            logger.debug(">>> handleIsRootInheritanceEntity start:" + this
                + " : " + getInheritance(this));
        boolean result = false;
        GeneralizableElementFacade superElement = this.getGeneralization();
        if (superElement == null)
        {
            String inheritance = getInheritance(this);
            // We are a root if we are the base class and not interface
            // inheritance
            result = (inheritance == null)
                || !inheritance.equals(INHERITANCE_STRATEGY_INTERFACE);
        }
        else
        {
            // We are a subclass
            GeneralizableElementFacade root = getRootInheritanceEntity();
            String inheritance = getInheritance(root);
            // Are we the subclass element
            result = root.getFullyQualifiedName().equals(
                getFullyQualifiedName());
            if (!result && inheritance != null
                && inheritance.equals(INHERITANCE_STRATEGY_SUBCLASS))
            {
                // If not check if we are a subclass
                result = superElement.getFullyQualifiedName().equals(
                    root.getFullyQualifiedName());
            }
        }
        if (logger.isDebugEnabled())
            logger.debug("<<< handleIsRootInheritanceEntity return:" + result);
        return result;
    }

    /**
     * Return the entity which is the root in Hibernate terms. If we have class
     * there is one table from where the first Entity which is defined as class.
     * If subclass there are 1 + number of subclasses tables. So if we are the
     * subclass defined Entity or the subclass of a subclass defined Entity we
     * are a root. If concrete we are a root.
     */
    private GeneralizableElementFacade getRootInheritanceEntity()
    {
        if (logger.isDebugEnabled())
            logger.debug(">>> getRootInheritanceEntity start:" + this + " : "
                + getInheritance(this));
        GeneralizableElementFacade result = null;
        GeneralizableElementFacade superElement = this.getGeneralization();
        ArrayList hierarchy = new ArrayList();
        while (superElement != null)
        {
            if (logger.isDebugEnabled())
                logger.debug("*** getSuperInheritance element:" + superElement
                    + " : " + getInheritance(superElement));
            hierarchy.add(superElement);
            superElement = superElement.getGeneralization();
        }
        String inheritance;
        GeneralizableElementFacade[] superclasses;
        superclasses = new GeneralizableElementFacade[hierarchy.size()];
        superclasses = (GeneralizableElementFacade[])hierarchy
            .toArray(superclasses);
        int rootIndex = hierarchy.size() - 1;
        for (int i = rootIndex; i > -1; i--)
        {
            inheritance = getInheritance(superclasses[i]);
            if (inheritance == null)
            {
                // Default = class
                result = superclasses[i];
                break;
            }
            if (inheritance.equals(INHERITANCE_STRATEGY_SUBCLASS))
            {
                result = superclasses[i];
                break;
            }
            if (inheritance.equals(INHERITANCE_STRATEGY_CLASS))
            {
                result = superclasses[i];
                break;
            }
        }
        if (result == null)
        {
            // Must be all concrete, odd
            result = this;
        }
        if (logger.isDebugEnabled())
            logger.debug("<<< getRootInheritanceEntity return:" + result);
        return result;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getHibernateInheritanceStrategy()
     */
    protected String handleGetHibernateInheritanceStrategy()
    {
        String result = null;
        if (logger.isDebugEnabled())
            logger.debug(">>> handleGetInheritanceStrategy start:" + this);

        try
        {
            result = getSuperInheritance();
            if (result == null)
            {
                result = getInheritance(this);
            }
            if (result == null)
            {
                result = INHERITANCE_STRATEGY_CLASS;
            }
        }
        catch (Exception ex)
        {
            String errorMessage = "*** " + getClass().getName()
                + " handleGetInheritanceStrategy exception:" + ex;
            ExceptionRecorder.record(errorMessage, ex, "hibernate");
            logger.error(errorMessage);
        }
        if (logger.isDebugEnabled())
            logger.debug("<<< handleGetInheritanceStrategy return:" + result);
        return result;
    }

    /**
     * Scan back up the generalization hierarchy looking for a INHERITANCE
     * strategy specification. Cases: super subclass CLASS None Allowed SUBCLASS
     * None Allowed CONCRETE CLASS | SUBCLASS
     * 
     * @return the super inheritance strategy
     */
    private String getSuperInheritance()
    {
        if (logger.isDebugEnabled())
            logger.debug(">>> getSuperInheritance start:" + this + " : "
                + getInheritance(this));
        String rootInheritance = null;
        GeneralizableElementFacade superElement = this.getGeneralization();
        ArrayList hierarchy = new ArrayList();
        while (superElement != null)
        {
            if (logger.isDebugEnabled())
                logger.debug("*** getSuperInheritance element: " + superElement
                    + " : " + getInheritance(superElement));
            hierarchy.add(superElement);
            superElement = superElement.getGeneralization();
        }
        if (hierarchy.size() > 0)
        {
            GeneralizableElementFacade[] superclasses;
            superclasses = new GeneralizableElementFacade[hierarchy.size()];
            superclasses = (GeneralizableElementFacade[])hierarchy
                .toArray(superclasses);
            int rootIndex = hierarchy.size() - 1;
            rootInheritance = getInheritance(superclasses[rootIndex]);
            if (rootInheritance == null
                || rootInheritance.equals(INHERITANCE_STRATEGY_CLASS))
            {
                validateNoInheritance(superclasses);
            }
            else if (rootInheritance.equals(INHERITANCE_STRATEGY_SUBCLASS))
            {
                validateNoInheritance(superclasses);
            }
            else if (rootInheritance.equals(INHERITANCE_STRATEGY_CONCRETE))
            {
                rootInheritance = validateConcreteInheritance(superclasses);
            }
            else if (rootInheritance.equals(INHERITANCE_STRATEGY_INTERFACE))
            {
                rootInheritance = validateInterfaceInheritance(superclasses);
            }
        }
        if (logger.isDebugEnabled())
            logger.debug("<<< getSuperInheritance return:" + rootInheritance);
        return rootInheritance;
    }

    /**
     * Check no classes have an inheritance tag.
     * 
     * @param superclasses
     */
    private void validateNoInheritance(GeneralizableElementFacade[] superclasses)
    {
        for (int i = 0; i < superclasses.length - 1; i++)
        {
            String inheritance = getInheritance(superclasses[i]);
            if (inheritance != null)
            {
                AndroMDALogger.warn("Inheritance tagged value:" + inheritance
                    + " on " + superclasses[i] + " ignored.");
            }
        }
    }

    /**
     * Check if an intermediate class has a class or subclass tag, if so return
     * that as the inheritance strategy. This is the only permitted mixed case.
     * Also check subclass/class mixes.
     * 
     * @param superclasses
     * @return String inheritance strategy
     */
    private String validateConcreteInheritance(
        GeneralizableElementFacade[] superclasses)
    {
        if (logger.isDebugEnabled())
            logger.debug(">>> validateConcreteInheritance:" + this);
        String result = null;
        String rootInheritance = INHERITANCE_STRATEGY_CONCRETE;
        // Search from root class but 1 to lowest.
        for (int i = superclasses.length - 1; i > -1; i--)
        {
            String inheritance = getInheritance(superclasses[i]);
            if (inheritance != null)
            {
                if (result == null)
                {
                    // Dont at this point care which strategy is specified.
                    result = inheritance;
                }
                else
                {
                    if (!result.equals(inheritance))
                    {
                        // If we are still on concrete we can change
                        if (!result.equals(rootInheritance))
                        {
                            AndroMDALogger
                                .warn("Cannot mix inheritance super inheritance:"
                                    + result
                                    + " with "
                                    + inheritance
                                    + " on "
                                    + superclasses[i] + " ignored.");
                        }
                        else
                        {
                            result = inheritance;
                        }
                    }
                }
            }
        }
        if (logger.isDebugEnabled())
            logger.debug("<<< validateConcreteInheritance:" + result);
        return result;
    }

    /**
     * Get the inheritance below the interface inheritance class, currently only
     * support one level of interface.
     * 
     * @param superclasses
     * @return String inheritance strategy
     */
    private String validateInterfaceInheritance(
        GeneralizableElementFacade[] superclasses)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug(">>> validateInterfaceInheritance:" + this);
            logger.debug("*** validateInterfaceInheritance superclasses:"
                + superclasses.length);
        }
        String result = null;
        int rootSubclassIndex = superclasses.length - 2;
        if (rootSubclassIndex > 0)
        {
            result = getInheritance(superclasses[rootSubclassIndex]);
            if (logger.isDebugEnabled())
                logger.debug("*** validateInterfaceInheritance rootSubclass:"
                    + result);
        }
        if (logger.isDebugEnabled())
            logger.debug("<<< validateInterfaceInheritance:" + result);
        return result;
    }

    /**
     * Return the inheritance tagged value for facade.
     * 
     * @param facade
     * @return String inheritance tagged value.
     */
    private String getInheritance(GeneralizableElementFacade facade)
    {
        return (String)facade
            .findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_INHERITANCE);
    }

    /**
     * Returns the SQL id column name. Invoked from vsl template as
     * $class.identifierColumn.
     * 
     * @return String the name of the SQL id column
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getIdentifierColumn()
     */
    protected String handleGetIdentifierColumn()
    {
        EntityAttributeFacade attribute = null;
        String columnName = null;
        Collection attributes = getAttributes();
        Predicate pred = new Predicate()
        {
            String defaultIdentifier = getDefaultIdentifier();

            public boolean evaluate(Object o)
            {
                boolean result = false;
                try
                {
                    EntityAttributeFacade a = (EntityAttributeFacade)o;
                    logger
                        .debug("*** handleGetIdentifierColumn.evaluate check:"
                            + a);
                    result = a.isIdentifier();
                }
                catch (Exception ex)
                {
                    // ignore
                }
                return result;
            }
        };
        attribute = (EntityAttributeFacade)CollectionUtils.find(
            attributes,
            pred);
        if (logger.isDebugEnabled())
            logger.debug("*** handleGetIdentifierColumn return:"
                + (attribute == null ? null : attribute.getColumnName()));
        columnName = attribute == null ? "ID" : attribute.getColumnName();
        return columnName;
    }

    private String getDefaultIdentifier()
    {
        return (String)getConfiguredProperty(UMLMetafacadeProperties.DEFAULT_IDENTIFIER);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityLogic#toString()
     */
    public String toString()
    {
        return getClass().getName() + "[" + getFullyQualifiedName() + "]";
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isHibernateInheritanceClass()
     */
    protected boolean handleIsHibernateInheritanceClass()
    {
        String strategy = getHibernateInheritanceStrategy();
        return INHERITANCE_STRATEGY_CLASS.equals(strategy);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isHibernateInheritanceInterface()
     */
    protected boolean handleIsHibernateInheritanceInterface()
    {
        String strategy = getHibernateInheritanceStrategy();
        return INHERITANCE_STRATEGY_INTERFACE.equals(strategy);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isHibernateInheritanceSubclass()
     */
    protected boolean handleIsHibernateInheritanceSubclass()
    {
        String strategy = getHibernateInheritanceStrategy();
        return INHERITANCE_STRATEGY_SUBCLASS.equals(strategy);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isHibernateInheritanceConcrete()
     */
    protected boolean handleIsHibernateInheritanceConcrete()
    {
        String strategy = getHibernateInheritanceStrategy();
        return INHERITANCE_STRATEGY_CONCRETE.equals(strategy);
    }

    private static final String HIBERNATE_ENTITY_CACHE="hibernateEntityCache";
    /* (non-Javadoc)
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityL#handleGetHibernateCacheType()
     */
    protected String handleGetHibernateCacheType() 
    {
        String cacheType=(String)findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_ENTITY_CACHE);
        if (cacheType==null)
        {
            return String.valueOf(this.getConfiguredProperty(HIBERNATE_ENTITY_CACHE));
        }
        else
        {
            return cacheType;
        }
    }

}