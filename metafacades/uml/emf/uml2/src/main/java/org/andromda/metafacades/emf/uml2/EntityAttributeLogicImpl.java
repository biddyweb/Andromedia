package org.andromda.metafacades.emf.uml2;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.EnumerationFacade;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.EntityAttribute.
 *
 * @see org.andromda.metafacades.uml.EntityAttribute
 * @author Bob Fields
 */
public class EntityAttributeLogicImpl
    extends EntityAttributeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public EntityAttributeLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(EntityAttributeLogicImpl.class);

    /**
     * Overridden to provide name masking.
     * @return name
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    protected String handleGetName()
    {
        final String nameMask =
            String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.ENTITY_PROPERTY_NAME_MASK));
        return NameMasker.mask(
            super.handleGetName(),
            nameMask);
    }

    /**
     * @return columnLength
     * @see org.andromda.metafacades.uml.EntityAttribute#getColumnLength()
     */
    protected String handleGetColumnLength()
    {
        return (String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN_LENGTH);
    }

    /**
     * @return columnName
     * @see org.andromda.metafacades.uml.EntityAttribute#getColumnName()
     */
    protected String handleGetColumnName()
    {
        final Short maxSqlNameLength =
            Short.valueOf((String)this.getConfiguredProperty(UMLMetafacadeProperties.MAX_SQL_NAME_LENGTH));
        final String columnNamePrefix =
            this.isConfiguredProperty(UMLMetafacadeProperties.COLUMN_NAME_PREFIX)
            ? ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.COLUMN_NAME_PREFIX)) : null;
        return EntityMetafacadeUtils.getSqlNameFromTaggedValue(
            columnNamePrefix,
            this,
            UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN,
            maxSqlNameLength,
            this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR),
            this.getConfiguredProperty(UMLMetafacadeProperties.SHORTEN_SQL_NAMES_METHOD));
    }

    /**
     * @return getMappingsProperty(UMLMetafacadeProperties.JDBC_MAPPINGS_URI)
     * @see org.andromda.metafacades.uml.EntityAttribute#getJdbcMappings()
     */
    protected org.andromda.metafacades.uml.TypeMappings handleGetJdbcMappings()
    {
        return this.getMappingsProperty(UMLMetafacadeProperties.JDBC_MAPPINGS_URI);
    }

    /**
     * @return jdbcType
     * @see org.andromda.metafacades.uml.EntityAttribute#getJdbcType()
     */
    protected String handleGetJdbcType()
    {
        String value = null;
        if (this.getJdbcMappings() != null)
        {
            final ClassifierFacade type = this.getType();
            if (type != null)
            {
                final String typeName = type.getFullyQualifiedName(true);
                value = this.getJdbcMappings().getTo(typeName);
            }
        }

        return value;
    }

    /**
     * @return getMappingsProperty(UMLMetafacadeProperties.SQL_MAPPINGS_URI)
     * @see org.andromda.metafacades.uml.EntityAttribute#getSqlMappings()
     */
    protected org.andromda.metafacades.uml.TypeMappings handleGetSqlMappings()
    {
        return this.getMappingsProperty(UMLMetafacadeProperties.SQL_MAPPINGS_URI);
    }

    /**
     * @return sqlType
     * @see org.andromda.metafacades.uml.EntityAttribute#getSqlType()
     */
    protected String handleGetSqlType()
    {
        String value = null;
        if (this.getSqlMappings() != null)
        {
            final ClassifierFacade type = this.getType();
            if (type != null)
            {
                String typeName = type.getFullyQualifiedName(true);

                // if its an enumeration, the sql type is the literal type
                if (type.isEnumeration())
                {
                    ClassifierFacade literalType = ((EnumerationFacade)type).getLiteralType();
                    if (literalType != null)
                    {
                        typeName = literalType.getFullyQualifiedName(true);
                    }
                }
                value = this.getSqlMappings().getTo(typeName);
                final String columnLength = this.getColumnLength();
                if (StringUtils.isNotBlank(columnLength))
                {
                    value = EntityMetafacadeUtils.constructSqlTypeName(
                            value,
                            columnLength);
                }
            }
        }
        return value;
    }

    /**
     * @return hasStereotype(UMLProfile.STEREOTYPE_IDENTIFIER)
     * @see org.andromda.metafacades.uml.EntityAttribute#isIdentifier()
     */
    protected boolean handleIsIdentifier()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_IDENTIFIER);
    }

    /**
     * @return hasStereotype(UMLProfile.STEREOTYPE_UNIQUE)
     * @see org.andromda.metafacades.uml.EntityAttribute#isUnique()
     */
    protected boolean handleIsUnique()
    {
        // TODO: Do we consider isUnique from AttributeFacade ?
        return this.hasStereotype(UMLProfile.STEREOTYPE_UNIQUE);
    }

    /**
     * @return columnIndex
     * @see org.andromda.metafacades.uml.EntityAttribute#getColumnIndex()
     */
    protected String handleGetColumnIndex()
    {
        final String index = (String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN_INDEX);
        return index != null ? StringUtils.trimToEmpty(index) : null;
    }

    /**
     * Gets a Mappings instance from a property registered under the given
     * <code>propertyName</code>.
     *
     * @param propertyName
     *            the property name to register under.
     * @return the Mappings instance.
     */
    private TypeMappings getMappingsProperty(final String propertyName)
    {
        final Object property = this.getConfiguredProperty(propertyName);
        TypeMappings mappings = null;

        if (property instanceof String)
        {
            String uri = (String)property;
            try
            {
                mappings = TypeMappings.getInstance(uri);
                this.setProperty(
                    propertyName,
                    mappings);
            }
            catch (Throwable th)
            {
                String errMsg = "Error getting '" + propertyName + "' --> '" + uri + '\'';
                logger.error(
                    errMsg,
                    th);

                // don't throw the exception
            }
        }
        else
        {
            mappings = (TypeMappings)property;
        }
        return mappings;
    }

    /**
     * @see org.andromda.metafacades.emf.uml2.EntityAttributeLogic#handleIsTransient()
     */
    protected boolean handleIsTransient()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_TRANSIENT);
    }

    /**
     * @return uniqueGroup
     * @see org.andromda.metafacades.uml.EntityAttribute#getUniqueGroup()
     */
    protected String handleGetUniqueGroup() {
        final String group = (String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN_UNIQUE_GROUP);
        return group != null ? StringUtils.trimToEmpty(group) : null;
    }
}