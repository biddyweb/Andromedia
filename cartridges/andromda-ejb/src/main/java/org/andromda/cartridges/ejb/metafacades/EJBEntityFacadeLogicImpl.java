package org.andromda.cartridges.ejb.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.andromda.cartridges.ejb.EJBGlobals;
import org.andromda.cartridges.ejb.EJBProfile;
import org.andromda.core.common.ExceptionRecorder;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.MetafacadeUtils;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * <p>
 * Represents an entity EJB. </p> Metaclass facade implementation.
 * @author Bob Fields
 */
public class EJBEntityFacadeLogicImpl
    extends EJBEntityFacadeLogic
{
    private static final long serialVersionUID = 34L;
    // ---------------- constructor -------------------------------
    /**
     * @param metaObject
     * @param context
     */
    public EJBEntityFacadeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(EJBEntityFacadeLogicImpl.class);

    /**
     * @return identifiers
     */
    public Collection handleGetIdentifiers()
    {
        Collection identifiers = new ArrayList();
        Iterator iter = this.getSourceDependencies().iterator();
        while (iter.hasNext())
        {
            DependencyFacade dep = (DependencyFacade)iter.next();
            if (dep.hasStereotype(EJBProfile.STEREOTYPE_IDENTIFIER))
            {
                identifiers = ((ClassifierFacade)dep.getTargetElement()).getInstanceAttributes();
                MetafacadeUtils.filterByStereotype(
                    identifiers,
                    EJBProfile.STEREOTYPE_IDENTIFIER);
                return identifiers;
            }
        }

        // No PK dependency found - try a PK attribute
        if (super.getIdentifiers() != null && !super.getIdentifiers().isEmpty())
        {
            AttributeFacade attr = (AttributeFacade)super.getIdentifiers().iterator().next();
            identifiers.add(attr);
            return identifiers;
        }

        // Still nothing found - recurse up the inheritance tree
        EJBEntityFacade decorator = (EJBEntityFacade)this.getGeneralization();
        return decorator.getIdentifiers();
    }

    /**
     * @return getEntityRelations()
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityFacade#getAllEntityRelations()
     */
    protected Collection handleGetAllEntityRelations()
    {
        // Only concrete entities may have EJB relations. Return
        // an empty collection for everything else
        if (this.isAbstract())
        {
            return Collections.emptyList();
        }
        Collection result = new ArrayList();
        result.addAll(getEntityRelations());
        ClassifierFacade classifier = (ClassifierFacade)this.getGeneralization();
        while (classifier != null && classifier instanceof EJBEntityFacade && classifier.isAbstract())
        {
            EJBEntityFacade entity = (EJBEntityFacade)classifier;
            result.addAll(entity.getEntityRelations());
            classifier = (ClassifierFacade)classifier.getGeneralization();
        }
        return result;
    }

    /**
     * @return EJBMetafacadeUtils.getViewType(this)
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityFacade#getViewType()
     */
    protected String handleGetViewType()
    {
        return EJBMetafacadeUtils.getViewType(this);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityFacadeLogic#handleGetEntityRelations()
     */
    protected Collection handleGetEntityRelations()
    {
        Collection result = new ArrayList();
        for (final Iterator iterator = this.getAssociationEnds().iterator(); iterator.hasNext();)
        {
            EJBAssociationEndFacade associationEnd = (EJBAssociationEndFacade)iterator.next();
            ClassifierFacade target = associationEnd.getOtherEnd().getType();
            if (target instanceof EJBEntityFacade && associationEnd.getOtherEnd().isNavigable())
            {
                // Check the integrity constraint
                Object value =
                    associationEnd.getOtherEnd().getAssociation().findTaggedValue(EJBProfile.TAGGEDVALUE_GENERATE_CMR);
                String generateCmr = value == null ? null : value.toString();
                if (target.isAbstract() && !"false".equalsIgnoreCase(generateCmr))
                {
                    throw new IllegalStateException("Relation '" + associationEnd.getAssociation().getName() +
                        "' has the abstract target '" + target.getName() +
                        "'. Abstract targets are not allowed in EJB.");
                }
                result.add(associationEnd);
            }
        }

        return result;
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityFacadeLogic#handleGetAllInstanceAttributes()
     */
    protected List handleGetAllInstanceAttributes()
    {
        return EJBMetafacadeUtils.getAllInstanceAttributes(this);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityFacadeLogic#handleGetInheritedInstanceAttributes()
     */
    protected List handleGetInheritedInstanceAttributes()
    {
        return EJBMetafacadeUtils.getInheritedInstanceAttributes(this);
    }

    /**
     * @param follow
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityFacadeLogic#handleGetCreateMethods(boolean)
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityFacade#getCreateMethods(boolean)
     */
    protected Collection handleGetCreateMethods(boolean follow)
    {
        return EJBMetafacadeUtils.getCreateMethods(
            this,
            follow);
    }

    /**
     * @param follow
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityFacadeLogic#handleGetSelectMethods(boolean)
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityFacade#getSelectMethods(boolean)
     */
    protected Collection handleGetSelectMethods(boolean follow)
    {
        Collection retval = new ArrayList();
        EJBEntityFacade entity = null;
        do
        {
            Collection ops = this.getOperations();
            for (final Iterator i = ops.iterator(); i.hasNext();)
            {
                OperationFacade op = (OperationFacade)i.next();
                if (op.hasStereotype(EJBProfile.STEREOTYPE_SELECT_METHOD))
                {
                    retval.add(op);
                }
            }
            if (follow)
            {
                entity = (EJBEntityFacade)this.getGeneralization();
            }
            else
            {
                break;
            }
        }
        while (entity != null);
        return retval;
    }

    /**
     * @return getHomeInterfaceName
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityFacade#getHomeInterfaceName()
     */
    protected String handleGetHomeInterfaceName()
    {
        return EJBMetafacadeUtils.getHomeInterfaceName(this);
    }

    /**
     * @param follow
     * @return getEnvironmentEntries
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityFacade#getEnvironmentEntries(boolean)
     */
    protected Collection handleGetEnvironmentEntries(boolean follow)
    {
        return EJBMetafacadeUtils.getEnvironmentEntries(
            this,
            follow);
    }

    /**
     * @param follow
     * @return getConstants
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityFacade#getConstants(boolean)
     */
    protected Collection handleGetConstants(boolean follow)
    {
        return EJBMetafacadeUtils.getConstants(
            this,
            follow);
    }

    /**
     * @return jndiName
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityFacade#getJndiName()
     */
    protected String handleGetJndiName()
    {
        StringBuilder jndiName = new StringBuilder();
        String jndiNamePrefix = StringUtils.trimToEmpty(this.getJndiNamePrefix());
        if (StringUtils.isNotBlank(jndiNamePrefix))
        {
            jndiName.append(jndiNamePrefix);
            jndiName.append('/');
        }
        jndiName.append("ejb/");
        jndiName.append(this.getFullyQualifiedName());
        return jndiName.toString();
    }

    /**
     * Gets the <code>jndiNamePrefix</code> for this EJB.
     *
     * @return the EJB Jndi name prefix.
     */
    protected String getJndiNamePrefix()
    {
        String prefix = null;
        if (this.isConfiguredProperty(EJBGlobals.JNDI_NAME_PREFIX))
        {
            prefix = (String)this.getConfiguredProperty(EJBGlobals.JNDI_NAME_PREFIX);
        }
        return prefix;
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityFacadeLogic#handleIsSyntheticCreateMethodAllowed()
     */
    protected boolean handleIsSyntheticCreateMethodAllowed()
    {
        return EJBMetafacadeUtils.allowSyntheticCreateMethod(this);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityFacadeLogic#getBusinessOperations()
     */
    // TODO: Change return type to Collection<EJBOperationFacade> (metamodel change)
    public Collection<OperationFacade> getBusinessOperations()
    {
        Collection<OperationFacade> businessOperations = new ArrayList<OperationFacade>();
        for (OperationFacade operation : super.getBusinessOperations())
        {
            if (EJBOperationFacade.class.isAssignableFrom(operation.getClass()))
            {
                EJBOperationFacade businessOperation = (EJBOperationFacade)operation;
                if (businessOperation.isBusinessOperation())
                {
                    businessOperations.add(businessOperation);
                }
            }
        }
        return businessOperations;
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityFacadeLogic#handleGetValueDependencies()
     */
    protected Collection handleGetValueDependencies()
    {
        Collection dependencies = super.getSourceDependencies();
        CollectionUtils.filter(
            dependencies,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    boolean isValueRef = false;
                    if (object instanceof DependencyFacade)
                    {
                        DependencyFacade dep = (DependencyFacade)object;
                        isValueRef =
                            dep.getStereotypeNames().contains(EJBProfile.STEREOTYPE_VALUE_REF) &&
                            dep.getTargetElement().hasExactStereotype(EJBProfile.STEREOTYPE_VALUE_OBJECT);
                    }
                    return isValueRef;
                }
            });
        return dependencies;
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityFacadeLogic#handleIsIdentifierPresent(String)
     */
    protected boolean handleIsIdentifierPresent(String identifier)
    {
        for (ModelElementFacade attr : this.getIdentifiers(true))
        {
            if (attr.getName().equalsIgnoreCase(identifier))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityFacadeLogic#handleIsAttributePresent(String)
     */
    protected boolean handleIsAttributePresent(String strAttr)
    {
        Collection<AttributeFacade> collAttrib = this.getAttributes(true);
        for (AttributeFacade attr : collAttrib)
        {
            if (attr.getName().equalsIgnoreCase(strAttr))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityFacadeLogic#handleIsOperationPresent(String)
     */
    protected boolean handleIsOperationPresent(String op)
    {
        Collection<OperationFacade> collOps = this.getOperations();
        for (OperationFacade operation : collOps)
        {
            if (operation.getName().equalsIgnoreCase(op))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets a Mappings instance from a property registered under the given <code>propertyName</code>.
     *
     * @param propertyName the property name to register under.
     * @return the Mappings instance.
     */
    private TypeMappings getMappingsProperty(final String propertyName)
    {
        Object property = this.getConfiguredProperty(propertyName);
        TypeMappings mappings = null;
        String uri = null;
        if (property instanceof String)
        {
            uri = (String)property;
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
                logger.error(errMsg);
                // don't throw the exception
                ExceptionRecorder.instance().record(
                    errMsg,
                    th);
            }
        }
        else
        {
            mappings = (TypeMappings)property;
        }
        return mappings;
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityFacadeLogic#handleGetSqlType()
     */
    protected String handleGetSqlType()
    {
        String mpSql = this.getMappingsProperty(UMLMetafacadeProperties.SQL_MAPPINGS_URI).getMappings().getName();
        if (mpSql.startsWith("Oracle"))
        {
            mpSql = "ORACLE";
        }
        return mpSql;
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityFacadeLogic#handleGetTransactionType()
     */
    protected String handleGetTransactionType()
    {
        String transactionType = (String)this.findTaggedValue(EJBProfile.TAGGEDVALUE_EJB_TRANSACTION_TYPE);
        if (StringUtils.isBlank(transactionType))
        {
            transactionType = String.valueOf(this.getConfiguredProperty(EJBGlobals.TRANSACTION_TYPE));
        }
        return transactionType;
    }
}