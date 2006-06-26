package org.andromda.cartridges.ejb3.metafacades;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.cartridges.ejb3.EJB3Profile;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.TaggedValueFacade;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * <p/>
 * Represents an EJB association end. </p> 
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacade
 */
public class EJB3AssociationEndFacadeLogicImpl
    extends EJB3AssociationEndFacadeLogic
{
    
    /**
     * The default composite association cascade property
     */
    private static final String ENTITY_DEFAULT_COMPOSITE_CASCADE = "entityCompositeCascade";
    
    /**
     * The default aggregation association cascade property
     */
    private static final String ENTITY_DEFAULT_AGGREGATION_CASCADE = "entityAggergationCascade";
    
    /**
     * The namespace property storing default collection type for associations
     */
    private static final String ASSOCIATION_COLLECTION_TYPE = "associationCollectionType";
    
    /**
     * A flag indicating whether or not specific (java.util.Set, java.util.List,
     * etc) collection interfaces should be used in assocation mutators and
     * accessors or whether the generic java.util.Collection interface should be
     * used.
     */
    private static final String SPECIFIC_COLLECTION_INTERFACES = "specificCollectionInterfaces";
    
    /**
     * The property that defines the default collection interface, this is the
     * interface used if the property defined by
     * {@link #SPECIFIC_COLLECTION_INTERFACES} is false.
     */
    private static final String DEFAULT_COLLECTION_INTERFACE = "defaultCollectionInterface";
    
    /**
     * Stores the default collection index name.
     */
    private static final String COLLECTION_INDEX_NAME = "associationEndCollectionIndexName";

    /**
     * Stores the default collection index type.
     */
    private static final String COLLECTION_INDEX_TYPE = "associationEndCollectionIndexType";
    
    /**
     * Represents the EJB3 <code>ALL</code> cascade option and fully qualified representation.
     */
    private static final String ENTITY_CASCADE_ALL = "ALL";
    private static final String ENTITY_CASCADE_ALL_FQN = "javax.persistence.CascadeType.ALL";
    
    /**
     * Represents the EJB3 <code>PERSIST</code> cascade option.
     */
    private static final String ENTITY_CASCADE_PERSIST = "PERSIST";
    private static final String ENTITY_CASCADE_PERSIST_FQN = "javax.persistence.CascadeType.PERSIST";
    
    /**
     * Represents the EJB3 <code>MERGE</code> cascade option.
     */
    private static final String ENTITY_CASCADE_MERGE = "MERGE";
    private static final String ENTITY_CASCADE_MERGE_FQN = "javax.persistence.CascadeType.MERGE";
    
    /**
     * Represents the EJB3 <code>REMOVE</code> cascade option.
     */
    private static final String ENTITY_CASCADE_REMOVE = "REMOVE";
    private static final String ENTITY_CASCADE_REMOVE_FQN = "javax.persistence.CascadeType.REMOVE";

    /**
     * Represents the EJB3 <code>REFRESH</code> cascade option.
     */
    private static final String ENTITY_CASCADE_REFRESH = "REFRESH";
    private static final String ENTITY_CASCADE_REFRESH_FQN = "javax.persistence.CascadeType.REFRESH";
    
    /**
     * Represents the value used to represents NO cascade option.
     */
    private static final String ENTITY_CASCADE_NONE = "NONE";
    
    /**
     * Stores the cascade map of fully qualified cascade types
     */
    private static final Hashtable cascadeTable = new Hashtable();
    
    static
    {
        cascadeTable.put(ENTITY_CASCADE_ALL, ENTITY_CASCADE_ALL_FQN);
        cascadeTable.put(ENTITY_CASCADE_PERSIST, ENTITY_CASCADE_PERSIST_FQN);
        cascadeTable.put(ENTITY_CASCADE_MERGE, ENTITY_CASCADE_MERGE_FQN);
        cascadeTable.put(ENTITY_CASCADE_REMOVE, ENTITY_CASCADE_REMOVE_FQN);
        cascadeTable.put(ENTITY_CASCADE_REFRESH, ENTITY_CASCADE_REFRESH_FQN);
    }
    
    /**
     * Value for set
     */
    private static final String COLLECTION_TYPE_SET = "set";

    /**
     * Value for map
     */
    private static final String COLLECTION_TYPE_MAP = "map";

    /**
     * Value for list
     */
    private static final String COLLECTION_TYPE_LIST = "list";

    /**
     * Value for collections
     */
    private static final String COLLECTION_TYPE_COLLECTION = "collection";

    /**
     * Stores the valid collection types
     */
    private static final Collection collectionTypes = new ArrayList();

    static
    {
        collectionTypes.add(COLLECTION_TYPE_SET);
        collectionTypes.add(COLLECTION_TYPE_MAP);
        collectionTypes.add(COLLECTION_TYPE_LIST);
        collectionTypes.add(COLLECTION_TYPE_COLLECTION);
    }
    
    /**
     * Stores the property indicating whether or not composition should define
     * the eager loading strategy and aggregation define lazy loading strategy.
     */
    private static final String COMPOSITION_DEFINES_EAGER_LOADING = "compositionDefinesEagerLoading";

    /**
     * The property that stores whether relationship collection caching is enabled.
     */
    private static final String HIBERNATE_ASSOCIATION_ENABLE_CACHE = "hibernateEnableAssociationsCache";
    
    /**
     * Stores the default cache strategy for relationship Collections.
     */
    private static final String HIBERNATE_ASSOCIATION_CACHE = "hibernateAssociationCache";

    /**
     * The 'list' type implementation to use.
     */
    private static final String LIST_TYPE_IMPLEMENTATION = "listTypeImplementation";

    /**
     * The 'set' type implementation to use.
     */
    private static final String SET_TYPE_IMPLEMENTATION = "setTypeImplementation";

    /**
     * The 'map' type implementation to use.
     */
    private static final String MAP_TYPE_IMPLEMENTATION = "mapTypeImplementation";
    
    // ---------------- constructor -------------------------------
	
    public EJB3AssociationEndFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    // --------------- methods ---------------------
    

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getGetterSetterTypeName()
     */
    public String getGetterSetterTypeName()
    {
        String getterSetterTypeName = null;
        
        if (this.isMany())
        {
            final boolean specificInterfaces =
                Boolean.valueOf(
                    ObjectUtils.toString(this.getConfiguredProperty(SPECIFIC_COLLECTION_INTERFACES))).booleanValue();
            
            final TypeMappings mappings = this.getLanguageMappings();
            if (mappings != null)
            {
                if (this.isMap())
                {
                    getterSetterTypeName = mappings.getTo(UMLProfile.MAP_TYPE_NAME);
                }
                else if (specificInterfaces)
                {
                    if (this.isSet())
                    {
                        getterSetterTypeName = mappings.getTo(UMLProfile.SET_TYPE_NAME);
                    }
                    else if (this.isList())
                    {
                        getterSetterTypeName = mappings.getTo(UMLProfile.LIST_TYPE_NAME);
                    }
                }
                else
                {
                    getterSetterTypeName = this.getDefaultCollectionInterface();
                }
            }
            else
            {
                getterSetterTypeName = this.getDefaultCollectionInterface();
            }
        }
        else
        {
            final ClassifierFacade type = this.getType();
            if (type instanceof EJB3EntityFacade)
            {
                final String typeName = ((EJB3EntityFacade)type).getFullyQualifiedEntityName();
                if (StringUtils.isNotEmpty(typeName))
                {
                    getterSetterTypeName = typeName;
                }
            }
        }

        if (getterSetterTypeName == null)
        {
            getterSetterTypeName = super.getGetterSetterTypeName();
        }
        else if (this.isMany())
        {
            /**
             * Set this association end's type as a template parameter if required
             */
            if ("true".equals(this.getConfiguredProperty(UMLMetafacadeProperties.ENABLE_TEMPLATING)))
            {
                getterSetterTypeName = getterSetterTypeName + "<" + this.getType().getFullyQualifiedName() + ">";
            }
        }
        return getterSetterTypeName;
    }

    /**
     * Overridden to provide handling of inheritance.
     *
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isRequired()
     */
    public boolean isRequired()
    {
        boolean required = super.isRequired();
        Object type = this.getOtherEnd().getType();

        if ((type != null) && EJB3EntityFacade.class.isAssignableFrom(type.getClass()))
        {
            EJB3EntityFacade entity = (EJB3EntityFacade)type;

            /**
             * Excluse ONLY if single table inheritance exists
             */
            if (entity.isRequiresGeneralizationMapping() && entity.isInheritanceSingleTable()
                    && !entity.isEmbeddableSuperclassGeneralizationExists())
            {
                required = false;
            }
        }

        return required;
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacade#getFetchType()
     */
	protected String handleGetFetchType() 
	{
		String fetchType = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_FETCH_TYPE);

		if (StringUtils.isBlank(fetchType))
		{
            // check whether or not composition defines eager loading is turned on
            boolean compositionDefinesEagerLoading =
                Boolean.valueOf(String.valueOf(this.getConfiguredProperty(COMPOSITION_DEFINES_EAGER_LOADING)))
                       .booleanValue();

            if (compositionDefinesEagerLoading)
            {
                if (this.getOtherEnd().isComposition())
                {
                    fetchType = EJB3Globals.FETCH_TYPE_EAGER;
                }
                else if (this.getOtherEnd().isAggregation())
                {
                    fetchType = EJB3Globals.FETCH_TYPE_LAZY;
                }
            }
		}
		
        /**
         * Go for defaults if blank
         */
        if (StringUtils.isBlank(fetchType))
        {
            if (this.getOtherEnd().isOne2Many() || this.getOtherEnd().isMany2Many())
            {
                fetchType = EJB3Globals.FETCH_TYPE_LAZY;
            }
            else
            {
                fetchType = EJB3Globals.FETCH_TYPE_EAGER;
            }
        }
		return fetchType;
	}

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleIsEager()
     */
    protected boolean handleIsEager()
    {
        boolean isEager = false;
        if (StringUtils.isNotBlank(this.getFetchType()))
        {
            if (this.getFetchType().equalsIgnoreCase(EJB3Globals.FETCH_TYPE_EAGER))
            {
                isEager = true;
            }
        }
        return isEager;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleIsLazy()
     */
    protected boolean handleIsLazy()
    {
        boolean isLazy = false;
        if (StringUtils.isNotBlank(this.getFetchType()))
        {
            if (this.getFetchType().equalsIgnoreCase(EJB3Globals.FETCH_TYPE_LAZY))
            {
                isLazy = true;
            }
        }
        return isLazy;
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleIsOwning()
     */
    protected boolean handleIsOwning()
    {
        boolean owning = false;
        if (this.isAggregation() || this.isComposition()) 
        {
            owning = true;
        }
        return owning;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleIsOptional()
     */
    protected boolean handleIsOptional()
    {
        boolean optional = true;
        String optionalString = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_OPTIONAL);

        if (StringUtils.isBlank(optionalString))
        {
            optional = !this.isRequired();
        }
        else
        {
            optional = Boolean.valueOf(optionalString).booleanValue();
        }
        return optional;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleGetOrderByClause()
     */
    protected String handleGetOrderByClause()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_ORDERBY);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleGetColumnDefinition()
     */
    protected String handleGetColumnDefinition()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_COLUMN_DEFINITION);
    }

    /**
     * Returns true if the tagged name exists for this association end.
     * 
     * @param name The tagged name to lookup.
     * @return boolean True if the tagged name exists.  False otherwise.
     * 
     * @see org.andromda.cartridges.ejb3.metafacades.
     *          EJB3AssociationEndFacadeLogic#handleHasTaggedValue(java.lang.String)
     */
    protected boolean handleHasTaggedValue(String name)
    {
        boolean exists = false;
        if (StringUtils.isNotBlank(name)) 
        {
            // trim to remove leading/trailing spaces
            name = StringUtils.trimToEmpty(name);
            
            // loop over tagged values and matche the argument tagged value name
            for (final Iterator iter = this.getTaggedValues().iterator(); iter.hasNext(); ) 
            {
                final TaggedValueFacade taggedValue = (TaggedValueFacade)iter.next();
                
                // return with true on the first match found
                if (name.equals(taggedValue.getName())) 
                {
                    exists = true;
                    break;
                }
            }
        }
        return exists;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleGetCascadeType()
     */
    protected String handleGetCascadeType()
    {
        String cascade = null;
        final Collection taggedValues = this.findTaggedValues(EJB3Profile.TAGGEDVALUE_PERSISTENCE_CASCADE_TYPE);
        if (taggedValues != null && !taggedValues.isEmpty())
        {
            StringBuffer buf = null;
            for (final Iterator iter = taggedValues.iterator(); iter.hasNext(); )
            {
                if (buf == null) 
                {
                    buf = new StringBuffer();
                }
                else
                {
                    buf.append(", ");
                }
                final String value = (String)iter.next();
                if (StringUtils.isNotBlank(value))
                {
                    buf.append(cascadeTable.get(value));
                }
            }
            cascade = buf.toString();
        }
        else if (this.isChild())
        {
            cascade = (String)cascadeTable.get(ENTITY_CASCADE_REMOVE);
            
            if (this.getOtherEnd() != null)
            {
                if (this.getOtherEnd().isComposition())
                {
                    if (StringUtils.isBlank(this.getCompositionCascadeType()))
                    {
                        if (this.getType() instanceof EJB3EntityFacade)
                        {
                            EJB3EntityFacade entity = (EJB3EntityFacade)this.getType();
                            cascade = (entity.getDefaultCascadeType().equalsIgnoreCase(ENTITY_CASCADE_NONE) ? 
                                    null : (String)cascadeTable.get(entity.getDefaultCascadeType()));
                        }
                    }
                    else
                    {
                        cascade = (this.getCompositionCascadeType().equalsIgnoreCase(ENTITY_CASCADE_NONE) ? 
                                null : (String)cascadeTable.get(this.getCompositionCascadeType()));
                    }
                }
                else if (this.getOtherEnd().isAggregation())
                {
                    if (StringUtils.isBlank(this.getAggregationCascadeType()))
                    {
                        if (this.getType() instanceof EJB3EntityFacade)
                        {
                            EJB3EntityFacade entity = (EJB3EntityFacade)this.getType();
                            cascade = (entity.getDefaultCascadeType().equalsIgnoreCase(ENTITY_CASCADE_NONE) ? 
                                    null : (String)cascadeTable.get(entity.getDefaultCascadeType()));
                        }
                    }
                    else
                    {
                        cascade = (this.getAggregationCascadeType().equalsIgnoreCase(ENTITY_CASCADE_NONE) ?
                                null : (String)cascadeTable.get(this.getAggregationCascadeType()));
                    }
                }
            }
        }
        else if (this.isComposition())
        {
            /**
             * On the composite side of the relationship, always enforce no cascade delete
             * property indicating no cascadable propogation - overriding a default cascade 
             * value
             */
            cascade = null;
        }
        else if (this.isAggregation())
        {
            /**
             * On the aggregation side of the relationship, always enforce no cascade delete
             * property indicating no cascadable propogation - overriding a default cascade
             * value
             */
            cascade = null;
        }
        return cascade;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleGetCompositionCascadeType()
     */
    protected String handleGetCompositionCascadeType()
    {
        return StringUtils.trimToEmpty(
                ObjectUtils.toString(this.getConfiguredProperty(ENTITY_DEFAULT_COMPOSITE_CASCADE)));
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleGetAggregationCascadeType()
     */
    protected String handleGetAggregationCascadeType()
    {
        return StringUtils.trimToEmpty(
                ObjectUtils.toString(this.getConfiguredProperty(ENTITY_DEFAULT_AGGREGATION_CASCADE)));
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleGetCollectionType()
     */
    protected String handleGetCollectionType()
    {
        String collectionType = this.getSpecificCollectionType();
        if (!collectionTypes.contains(collectionType))
        {
            if (this.isOrdered())
            {
                collectionType = COLLECTION_TYPE_LIST;
            }
            else
            {
                collectionType =
                    (String)this.getConfiguredProperty(ASSOCIATION_COLLECTION_TYPE);
            }
        }
        return collectionType;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleGetCollectionTypeImplementation()
     */
    protected String handleGetCollectionTypeImplementation()
    {
        return this.getCollectionTypeImplementation(null);
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleGetCollectionTypeImplementation(java.lang.String)
     */
    protected String handleGetCollectionTypeImplementation(String arg)
    {
        StringBuffer implementation = new StringBuffer();
        if (this.isMany())
        {
            implementation.append("new ");
            if (this.isSet())
            {
                implementation.append(this.getConfiguredProperty(SET_TYPE_IMPLEMENTATION));
            }
            else if (this.isMap())
            {
                implementation.append(this.getConfiguredProperty(MAP_TYPE_IMPLEMENTATION));
            }
            else if (this.isList() || this.isCollection())
            {
                implementation.append(this.getConfiguredProperty(LIST_TYPE_IMPLEMENTATION));
            }

            // set this association end's type as a template parameter if required
            if (Boolean.valueOf(String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.ENABLE_TEMPLATING)))
                       .booleanValue())
            {
                implementation.append("<");
                if (this.isMap())
                {
                    implementation.append(this.getCollectionIndexType());
                    implementation.append(", ");
                }
                implementation.append(this.getType().getFullyQualifiedName());
                implementation.append(">");
            }
            implementation.append("(");
            if (StringUtils.isNotBlank(arg))
            {
                implementation.append(arg);
            }
            implementation.append(")");
        }

        return implementation.toString();
    }
    
    /**
     * Gets the collection type defined on this association end.
     *
     * @return the specific collection type.
     */
    private String getSpecificCollectionType()
    {
        return ObjectUtils.toString(
            this.findTaggedValue(EJB3Profile.TAGGEDVALUE_ASSOCIATION_COLLECTION_TYPE));
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleGetCollectionIndexType()
     */
    protected String handleGetCollectionIndexType()
    {
        Object value = this.findTaggedValue(EJB3Profile.TAGGEDVALUE_ASSOCIATION_INDEX_TYPE);
        if (value == null)
        {
            value = this.getConfiguredProperty(COLLECTION_INDEX_TYPE);
            if (StringUtils.isBlank(ObjectUtils.toString(value)))
            {
                value = null;
            }
        }

        if (value != null)
        {
            if (value instanceof String)
            {
                value = this.getRootPackage().findModelElement((String)value);
            }
            if (value instanceof EJB3TypeFacade)
            {
                value = ((EJB3TypeFacade)value).getFullyQualifiedEJB3Type();
            }
        }
        return (value != null) ? ObjectUtils.toString(value) : null;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleGetCollectionIndexName()
     */
    protected String handleGetCollectionIndexName()
    {
        Object value = this.findTaggedValue(EJB3Profile.TAGGEDVALUE_ASSOCIATION_INDEX);
        if ((value == null) && this.isConfiguredProperty(COLLECTION_INDEX_NAME))
        {
            value = this.getConfiguredProperty(COLLECTION_INDEX_NAME);
            if (StringUtils.isBlank(ObjectUtils.toString(value)))
            {
                value = null;
            }
        }

        if (value != null)
        {
            return ObjectUtils.toString(value);
        }
        final String otherEntityName = ((EJB3EntityFacade)this.getOtherEnd().getType()).getEntityName();
        final Object separator = this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR);
        return EntityMetafacadeUtils.toSqlName(
            otherEntityName,
            separator) + separator + EntityMetafacadeUtils.toSqlName(
            this.getName(),
            separator) + separator + "IDX";
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleIsMap()
     */
    protected boolean handleIsMap()
    {
        boolean isMap = this.getCollectionType().equalsIgnoreCase(COLLECTION_TYPE_MAP);
        if (isMap && StringUtils.isBlank(this.getSpecificCollectionType()))
        {
            isMap = !this.isOrdered();
        }
        return isMap;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleIsList()
     */
    protected boolean handleIsList()
    {
        boolean isList = this.getCollectionType().equalsIgnoreCase(COLLECTION_TYPE_LIST);
        if (!isList && StringUtils.isBlank(this.getSpecificCollectionType()))
        {
            isList = this.isOrdered();
        }
        return isList;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleIsSet()
     */
    protected boolean handleIsSet()
    {
        boolean isSet = this.getCollectionType().equalsIgnoreCase(COLLECTION_TYPE_SET);
        if (isSet && StringUtils.isBlank(this.getSpecificCollectionType()))
        {
            isSet = !this.isOrdered();
        }
        return isSet;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleIsCollection()
     */
    protected boolean handleIsCollection()
    {
        boolean isCollection = this.getCollectionType().equalsIgnoreCase(COLLECTION_TYPE_COLLECTION);
        if (!isCollection && StringUtils.isBlank(this.getSpecificCollectionType()))
        {
            isCollection = this.isOrdered();
        }
        return isCollection;
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleGetLabelName()
     */
    protected String handleGetLabelName()
    {
        String labelNamePattern = (this.isMany() ? 
                (String)this.getConfiguredProperty(EJB3Globals.LABEL_COLLECTION_NAME_PATTERN) :
                    (String)this.getConfiguredProperty(EJB3Globals.LABEL_SINGLE_NAME_PATTERN));

        return MessageFormat.format(
                labelNamePattern,
            new Object[] {StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleGetGetterLabelName()
     */
    protected String handleGetGetterLabelName()
    {
        return UMLMetafacadeUtils.getGetterPrefix(this.getType()) + StringUtils.capitalize(this.getLabelName());
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleGetSetterLabelName()
     */
    protected String handleGetSetterLabelName()
    {
        return "set" + StringUtils.capitalize(this.getLabelName());
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleGetCacheType()
     */
    protected String handleGetCacheType()
    {
        String cacheType = (String)findTaggedValue(EJB3Profile.TAGGEDVALUE_HIBERNATE_ASSOCIATION_CACHE);
        if (StringUtils.isBlank(cacheType))
        {
            cacheType = String.valueOf(this.getConfiguredProperty(HIBERNATE_ASSOCIATION_CACHE));
        }
        return StringUtils.trimToEmpty(cacheType);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleIsAssociationCacheEnabled()
     */
    protected boolean handleIsAssociationCacheEnabled()
    {
        return BooleanUtils.toBoolean(String.valueOf(this.getConfiguredProperty(HIBERNATE_ASSOCIATION_ENABLE_CACHE)));
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#
     *      handleGetForeignKeyConstraintName(java.lang.String)
     */
    protected String handleGetForeignKeyConstraintName(String suffix)
    {
        String constraintName;

        final Object taggedValueObject = findTaggedValue(
                UMLProfile.TAGGEDVALUE_PERSISTENCE_FOREIGN_KEY_CONSTRAINT_NAME);

        /**
         * Construct our own foreign key constraint name here
         */
        StringBuffer buffer = new StringBuffer();

        if (taggedValueObject == null)
        {
            final ClassifierFacade type = getOtherEnd().getType();
            if (type instanceof Entity)
            {
                Entity entity = (Entity)type;
                buffer.append(entity.getTableName());
            }
            else
            {
                // should not happen
                buffer.append(type.getName().toUpperCase());
            }
        }
        else
        {
            // use the tagged value
            buffer.append(taggedValueObject.toString());
        }

        buffer.append(this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR));
        
        /**
         * Add the suffix - which is the name of the identifier pk column if not blank
         * otherwise use the column name of the relationship
         */
        if (StringUtils.isNotBlank(suffix))
        {
            buffer.append(suffix);
        }
        else
        {
            buffer.append(this.getColumnName());
        }
        constraintName = buffer.toString();
        
        final String constraintSuffix = 
            ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.CONSTRAINT_SUFFIX)).trim();
        
        /**
         * we take into consideration the maximum length allowed
         */
        final String maxLengthString = (String)getConfiguredProperty(UMLMetafacadeProperties.MAX_SQL_NAME_LENGTH);
        final short maxLength = (short)(Short.valueOf(maxLengthString).shortValue() - constraintSuffix.length());
        buffer = new StringBuffer(
                EntityMetafacadeUtils.ensureMaximumNameLength(constraintName, new Short(maxLength)));
        buffer.append(constraintSuffix);
        return buffer.toString();
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleGetDefaultCollectionInterface()
     */
    protected String handleGetDefaultCollectionInterface()
    {
        return ObjectUtils.toString(this.getConfiguredProperty(DEFAULT_COLLECTION_INTERFACE));
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacadeLogic#handleIsDefaultCollectionInterfaceSortedSet()
     */
    protected boolean handleIsDefaultCollectionInterfaceSortedSet()
    {
        boolean isDefaultSortedSet = false;
        if (StringUtils.equals(this.getDefaultCollectionInterface(), EJB3Globals.COLLECTION_INTERFACE_SORTED_SET))
        {
            isDefaultSortedSet = true;
        }
        return isDefaultSortedSet;
    }
}