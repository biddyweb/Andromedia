/*
 */
package org.andromda.cartridges.hibernate.metafacades;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.andromda.cartridges.hibernate.HibernateProfile;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.EntityAttributeFacade;
import org.andromda.metafacades.uml.EntityFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.OperationFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;

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
 * Also provides support for not generating the entity factory which is useful
 * when using subclass mode.
 * </p>
 * 
 * @author Chad Brandon
 * @author Martin West
 * @author Carlos Cuenca
 */
public class HibernateEntityLogicImpl
    extends HibernateEntityLogic
{

    public HibernateEntityLogicImpl(
        java.lang.Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * Value for one table per root class
     */
    private static final String INHERITANCE_STRATEGY_CLASS = "class";

    /**
     * Value for joined-subclass
     */
    private static final String INHERITANCE_STRATEGY_SUBCLASS = "subclass";

    /**
     * Value for one table per concrete class
     */
    private static final String INHERITANCE_STRATEGY_CONCRETE = "concrete";

    /**
     * Value make entity an interface, delegate attributes to subclasses.
     */
    private static final String INHERITANCE_STRATEGY_INTERFACE = "interface";

    /**
     * Stores the valid inheritance strategies.
     */
    private static final Collection inheritanceStrategies = new ArrayList();

    static
    {
        inheritanceStrategies.add(INHERITANCE_STRATEGY_CLASS);
        inheritanceStrategies.add(INHERITANCE_STRATEGY_SUBCLASS);
        inheritanceStrategies.add(INHERITANCE_STRATEGY_CONCRETE);
        inheritanceStrategies.add(INHERITANCE_STRATEGY_INTERFACE);
    }

    /**
     * Return all the business operations (ones that are inherited as well as
     * directly on the entity).
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
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getHibernateInheritanceStrategy()
     */
    protected String handleGetHibernateInheritanceStrategy()
    {
        String inheritance = this.getInheritance(this);
        for (HibernateEntity superEntity = this.getSuperEntity(); superEntity != null
            && StringUtils.isBlank(inheritance);)
        {
            inheritance = superEntity.getHibernateInheritanceStrategy();
        }
        if (StringUtils.isBlank(inheritance)
            || !inheritanceStrategies.contains(inheritance))
        {
            inheritance = this.getDefaultInheritanceStrategy();
        }
        return inheritance;
    }

    /**
     * Stores the default hibernate inheritance strategy.
     */
    private static final String INHERITANCE_STRATEGY = "hibernateInheritanceStrategy";

    /**
     * Gets the default hibernate inhertance strategy.
     * 
     * @return the default hibernate inheritance strategy.
     */
    private String getDefaultInheritanceStrategy()
    {
        return String.valueOf(this.getConfiguredProperty(INHERITANCE_STRATEGY));
    }

    /**
     * Return the inheritance tagged value for for given <code>entity</code>.
     * 
     * @param the HibernateEntity from which to retrieve the inheritance tagged
     *        value.
     * @return String inheritance tagged value.
     */
    private String getInheritance(HibernateEntity entity)
    {
        String inheritance = null;
        if (entity != null)
        {
            Object value = entity
                .findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_INHERITANCE);
            if (value != null)
            {
                inheritance = String.valueOf(value);
            }
        }
        return inheritance;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getProperties()
     */
    public java.util.Collection getProperties()
    {
        Collection properties = this.getAttributes();
        Collection connectingEnds = this.getAssociationEnds();
        CollectionUtils.transform(connectingEnds, new Transformer()
        {
            public Object transform(Object object)
            {
                return ((AssociationEndFacade)object).getOtherEnd();
            }
        });
        class NavigableFilter
            implements Predicate
        {
            public boolean evaluate(Object object)
            {
                AssociationEndFacade end = (AssociationEndFacade)object;
                return end.isNavigable()
                    || (end.getOtherEnd().isChild() && isForeignHibernateGeneratorClass());
            }
        }
        CollectionUtils.filter(connectingEnds, new NavigableFilter());
        properties.addAll(connectingEnds);
        return properties;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isHibernateInheritanceClass()
     */
    protected boolean handleIsHibernateInheritanceClass()
    {
        return this.getHibernateInheritanceStrategy().equalsIgnoreCase(
            INHERITANCE_STRATEGY_CLASS);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isHibernateInheritanceInterface()
     */
    protected boolean handleIsHibernateInheritanceInterface()
    {
        return this.getHibernateInheritanceStrategy().equalsIgnoreCase(
            INHERITANCE_STRATEGY_INTERFACE);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isHibernateInheritanceSubclass()
     */
    protected boolean handleIsHibernateInheritanceSubclass()
    {
        return this.getHibernateInheritanceStrategy().equalsIgnoreCase(
            INHERITANCE_STRATEGY_SUBCLASS);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isHibernateInheritanceConcrete()
     */
    protected boolean handleIsHibernateInheritanceConcrete()
    {
        return this.getHibernateInheritanceStrategy().equalsIgnoreCase(
            INHERITANCE_STRATEGY_CONCRETE);
    }

    /**
     * Stores the hibernate entity cache value.
     */
    private static final String HIBERNATE_ENTITY_CACHE = "hibernateEntityCache";

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getHibernateCacheType()
     */
    protected String handleGetHibernateCacheType()
    {
        String cacheType = (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_ENTITY_CACHE);
        if (cacheType == null)
        {
            cacheType = String.valueOf(this
                .getConfiguredProperty(HIBERNATE_ENTITY_CACHE));
        }
        return cacheType;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getFullyQualifiedEntityName()
     */
    protected String handleGetFullyQualifiedEntityName()
    {
        return HibernateMetafacadeUtils.getFullyQualifiedName(this
            .getPackageName(), this.getEntityName(), null);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getFullyQualifiedEntityImplementationName()
     */
    protected String handleGetFullyQualifiedEntityImplementationName()
    {
        return HibernateMetafacadeUtils.getFullyQualifiedName(
            this.getPackageName(),
            this.getEntityName(),
            HibernateGlobals.IMPLEMENTATION_SUFFIX);
    }

    /**
     * The namespace property storing the hibernate default-cascade value for an
     * entity.
     */
    private static final String HIBERNATE_DEFAULT_CASCADE = "hibernateDefaultCascade";

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getHibernateDefaultCascade()
     */
    protected String handleGetHibernateDefaultCascade()
    {
        return StringUtils.trimToEmpty(String.valueOf(this
            .getConfiguredProperty(HIBERNATE_DEFAULT_CASCADE)));
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getHibernateGeneratorClass()
     */
    protected String handleGetHibernateGeneratorClass()
    {
        String hibernateGeneratorClass;
        // if the entity is using a foreign identifier, then
        // we automatically set the identifier generator
        // class to be foreign
        if (this.isUsingForeignIdentifier())
        {
            hibernateGeneratorClass = HIBERNATE_GENERATOR_CLASS_FOREIGN;
        }
        else
        {
            hibernateGeneratorClass = (String)this
                .findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_GENERATOR_CLASS);
            if (StringUtils.isBlank(hibernateGeneratorClass))
            {
                hibernateGeneratorClass = (String)this
                    .getConfiguredProperty("defaultHibernateGeneratorClass");
            }
        }
        return hibernateGeneratorClass;
    }

    private static final String HIBERNATE_GENERATOR_CLASS_FOREIGN = "foreign";

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isForeignHibernateGeneratorClass()
     */
    protected boolean handleIsForeignHibernateGeneratorClass()
    {
        // check to see if the entity is using a foreign identifier
        // OR if the actual hibernate generator class is set to foreign
        return this.isUsingForeignIdentifier()
            || this.getHibernateGeneratorClass().equalsIgnoreCase(
                HIBERNATE_GENERATOR_CLASS_FOREIGN);
    }

    private static final String HIBERNATE_GENERATOR_CLASS_SEQUENCE = "sequence";

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isSequenceHibernateGeneratorClass()
     */
    protected boolean handleIsSequenceHibernateGeneratorClass()
    {
        return this.getHibernateGeneratorClass().equalsIgnoreCase(
            HIBERNATE_GENERATOR_CLASS_SEQUENCE);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getEntityName()
     */
    protected String handleGetEntityName()
    {
        String entityNamePattern = (String)this
            .getConfiguredProperty("entityNamePattern");
        return MessageFormat.format(entityNamePattern, new Object[]
        {
            StringUtils.trimToEmpty(this.getName())
        });
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getEntityImplementationName()
     */
    protected String handleGetEntityImplementationName()
    {
        return this.getEntityName() + HibernateGlobals.IMPLEMENTATION_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getHibernateDiscriminatorColumn()
     */
    protected String handleGetHibernateDiscriminatorColumn()
    {
        return "class";
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getHibernateDiscriminatorType()
     */
    protected String handleGetHibernateDiscriminatorType()
    {
        return "string";
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getHibernateDiscriminatorLength()
     */
    protected int handleGetHibernateDiscriminatorLength()
    {
        return 1;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getEntityBusinessOperations()
     */
    protected Collection handleGetEntityBusinessOperations()
    {
        // operations that are not finders and not static
        Collection finders = this.getQueryOperations();
        Collection operations = this.getOperations();

        Collection nonFinders = CollectionUtils.subtract(operations, finders);
        return new FilteredCollection(nonFinders)
        {
            public boolean evaluate(Object object)
            {
                return !((OperationFacade)object).isStatic();
            }
        };
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isEntityBusinessOperationsPresent()
     */
    protected boolean handleIsEntityBusinessOperationsPresent()
    {
        final Collection allBusinessOperations = this
            .getAllBusinessOperations();
        return allBusinessOperations != null
            && !allBusinessOperations.isEmpty();
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isHibernateProxy()
     */
    protected boolean handleIsHibernateProxy()
    {
        String hibernateProxy = (String)this
            .findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_PROXY);
        if (hibernateProxy == null)
        {
            hibernateProxy = (String)this
                .getConfiguredProperty("hibernateProxy");
        }
        return Boolean.valueOf(hibernateProxy).booleanValue();
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getEhCacheMaxElementsInMemory()
     */
    protected int handleGetEhCacheMaxElementsInMemory()
    {
        String maxElements = null;
        maxElements = (String)this
            .findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_EHCACHE_MAX_ELEMENTS);
        if (StringUtils.isBlank(maxElements))
        {
            maxElements = (String)this
                .getConfiguredProperty(HibernateGlobals.HIBERNATE_EHCACHE_MAX_ELEMENTS);
        }
        return Integer.parseInt(maxElements);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isEhCacheEternal()
     */
    protected boolean handleIsEhCacheEternal()
    {
        String eternal = (String)this
            .findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_EHCACHE_ETERNAL);
        if (eternal == null)
        {
            eternal = (String)this
                .getConfiguredProperty(HibernateGlobals.HIBERNATE_EHCACHE_ETERNAL);
        }
        return Boolean.valueOf(eternal).booleanValue();
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getEhCacheTimeToIdleSeconds()
     */
    protected int handleGetEhCacheTimeToIdleSeconds()
    {
        String timeToIdle = null;
        timeToIdle = (String)this
            .findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_EHCACHE_TIME_TO_IDLE);
        if (StringUtils.isBlank(timeToIdle))
        {
            timeToIdle = (String)this
                .getConfiguredProperty(HibernateGlobals.HIBERNATE_EHCACHE_TIME_TO_IDLE);
        }
        return Integer.parseInt(timeToIdle);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getEhCacheTimeToLiveSeconds()
     */
    protected int handleGetEhCacheTimeToLiveSeconds()
    {
        String timeToLive = null;
        timeToLive = (String)this
            .findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_EHCACHE_TIME_TO_LIVE);
        if (StringUtils.isBlank(timeToLive))
        {
            timeToLive = (String)this
                .getConfiguredProperty(HibernateGlobals.HIBERNATE_EHCACHE_TIME_TO_LIVE);
        }
        return Integer.parseInt(timeToLive);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isEhCacheOverflowToDisk()
     */
    protected boolean handleIsEhCacheOverflowToDisk()
    {
        String eternal = (String)this
            .findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_EHCACHE_OVERFLOW_TO_DISK);
        if (eternal == null)
        {
            eternal = (String)this
                .getConfiguredProperty(HibernateGlobals.HIBERNATE_EHCACHE_OVERFLOW_TO_DISK);
        }
        return Boolean.valueOf(eternal).booleanValue();
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isTableRequired()
     */
    protected boolean handleIsTableRequired()
    {
        return !this.isHibernateInheritanceClass()
            || (this.isHibernateInheritanceClass() && this.getGeneralization() == null);
    }

    /**
     * The "class" mapping name.
     */
    private static final String CLASS_MAPPING_NAME = "class";

    /**
     * The "joined-subclass" mapping name.
     */
    private static final String JOINED_SUBCLASS_MAPPING_NAME = "joined-subclass";

    /**
     * The "subclass" mapping name.
     */
    private static final String SUBCLASS_MAPPING_NAME = "subclass";

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getMappingClassName()
     */
    protected String handleGetMappingClassName()
    {
        String mappingClassName = CLASS_MAPPING_NAME;
        final HibernateEntity superEntity = this.getSuperEntity();
        if (superEntity != null
            && !superEntity.isHibernateInheritanceInterface()
            && !superEntity.isHibernateInheritanceConcrete())
        {
            mappingClassName = JOINED_SUBCLASS_MAPPING_NAME;
            if (this.isHibernateInheritanceClass())
            {
                mappingClassName = SUBCLASS_MAPPING_NAME;
            }
        }
        return mappingClassName;
    }

    /**
     * Gets the super entity for this entity (if one exists). If a
     * generalization does not exist OR if it's not an instance of
     * HibernateEntity then return null.
     * 
     * @return the super entity or null if one doesn't exist.
     */
    private HibernateEntity getSuperEntity()
    {
        HibernateEntity superEntity = null;
        if (this.getGeneralization() != null
            && this.getGeneralization() instanceof HibernateEntity)
        {
            superEntity = (HibernateEntity)this.getGeneralization();
        }
        return superEntity;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getSubclassKeyColumn()
     */
    protected String handleGetSubclassKeyColumn()
    {
        String column = null;
        // first we seach for an explicit identifier on the entity
        final HibernateEntity superEntity = this.getSuperEntity();
        if (superEntity != null && superEntity.isHibernateInheritanceSubclass())
        {
            column = ((EntityAttributeFacade)this.getIdentifiers().iterator()
                .next()).getColumnName();
        }
        return column;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isRequiresMapping()
     */
    protected boolean handleIsRequiresMapping()
    {
        final HibernateEntity superEntity = this.getSuperEntity();
        return this.isRoot()
            && (!this.isHibernateInheritanceInterface() || (superEntity != null
                && superEntity.isHibernateInheritanceInterface()));
    }

    /**
     * Indicates if this entity as a <code>root</code> entity (meaning it
     * doesn't specialize anything).
     */
    private boolean isRoot()
    {
        final HibernateEntity superEntity = this.getSuperEntity();
        return this.getSuperEntity() == null
            || (superEntity.isHibernateInheritanceInterface() || superEntity
                .isHibernateInheritanceConcrete());
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityLogic#handleIsRequiresSpecializationMapping()
     */
    protected boolean handleIsRequiresSpecializationMapping()
    {
        return this.isRoot()
            && (this.isHibernateInheritanceSubclass() || this
                .isHibernateInheritanceClass());
    }
}