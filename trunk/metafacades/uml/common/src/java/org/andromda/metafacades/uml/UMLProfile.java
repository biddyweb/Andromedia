package org.andromda.metafacades.uml;

import org.andromda.core.common.Profile;

/**
 * Contains the common UML AndroMDA profile. That is, it contains elements
 * "common" to all AndroMDA components (tagged values, and stereotypes).
 * 
 * @author Chad Brandon
 */
public class UMLProfile
{
    /* ----------------- Stereotypes -------------------- */

    /**
     * The Profile instance from which we retrieve the mapped profile names.
     */
    private static final Profile profile = Profile.instance();

    /**
     * Represents a persistent entity.
     */
    public static final String STEREOTYPE_ENTITY = profile.get("ENTITY");

    /**
     * Represents a finder method on an entity.
     */
    public static final String STEREOTYPE_FINDER_METHOD = profile
        .get("FINDER_METHOD");

    /**
     * Represents the primary key of an entity.
     */
    public static final String STEREOTYPE_IDENTIFIER = profile
        .get("IDENTIFIER");

    /**
     * If an attribute has this stereotype, it is considered unique.
     */
    public static final String STEREOTYPE_UNIQUE = profile.get("UNIQUE");

    /**
     * Represents a service.
     */
    public static final String STEREOTYPE_SERVICE = profile.get("SERVICE");

    /**
     * Represents a value object.
     */
    public static final String STEREOTYPE_VALUE_OBJECT = profile
        .get("VALUE_OBJECT");

    /**
     * <p>
     * Represents a web service. Stereotype a class with this stereotype when
     * you want everything on the class to be exposed as a web service.
     * </p>
     */
    public static final java.lang.String STEREOTYPE_WEBSERVICE = profile
        .get("WEBSERVICE");

    /**
     * <p>
     * Stereotype an operation on a <code>service</code> if you wish to expose
     * the operation.
     * </p>
     */
    public static final java.lang.String STEREOTYPE_WEBSERVICE_OPERATION = profile
        .get("WEBSERVICE_OPERATION");

    /**
     * The base exception stereotype. If a model element is stereotyped with
     * this (or one of its specializations), then the exception can be generated
     * by a cartridge and a dependency to it from an operation will add a throws
     * clause.
     */
    public static final String STEREOTYPE_EXCEPTION = profile.get("EXCEPTION");

    /**
     * Represents an enumeration type.
     */
    public static final String STEREOTYPE_ENUMERATION = profile
        .get("ENUMERATION");

    /**
     * Represents exceptions thrown during normal application processing (such
     * as business exceptions). It extends the base exception stereotype.
     */
    public static final String STEREOTYPE_APPLICATION_EXCEPTION = profile
        .get("APPLICATION_EXCEPTION");

    /**
     * Represents unexpected exceptions that can occur during application
     * processing. This that a caller isn't expected to handle.
     */
    public static final String STEREOTYPE_UNEXPECTED_EXCEPTION = profile
        .get("UNEXPECTED_EXCEPTION");

    /**
     * Represents a reference to an exception model element. Model dependencies
     * to unstereotyped exception model elements can be stereotyped with this.
     * This allows the user to create a custom exception class since the
     * exception itself will not be generated but the references to it will be
     * (i.e. the throws clause within an operation).
     */
    public static final String STEREOTYPE_EXCEPTION_REF = profile
        .get("EXCEPTION_REF");

    /**
     * Used to indicate whether or not a parameter is nullable (since parameters
     * do <strong>NOT </strong> allow specification of multiplicity.
     */
    public static final String STEREOTYPE_NULLABLE = profile.get("NULLABLE");

    /**
     * Represents a role played by a user within a system.
     */
    public static final String STEREOTYPE_ROLE = profile.get("ROLE");

    /* ----------------- Tagged Values -------------------- */

    /**
     * Represents documentation stored as a tagged value
     */
    public static final String TAGGEDVALUE_DOCUMENTATION = profile
        .get("DOCUMENTATION");

    /**
     * Represents a relational table name for entity persistence.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_TABLE = profile
        .get("PERSISTENCE_TABLE");

    /**
     * Represents a relational table column name for entity persistence.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN = profile
        .get("PERSISTENCE_COLUMN");

    /**
     * Represents a relational table column length
     */
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_LENGTH = profile
        .get("PERSISTENCE_COLUMN_LENGTH");

    /**
     * Represents a relational table column index name.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_INDEX = profile
        .get("PERSISTENCE_COLUMN_INDEX");

    /**
     * Stores the style of a web service (document, wrapped, rpc).
     */
    public static final String TAGGEDVALUE_WEBSERVICE_STYLE = profile
        .get("WEBSERVICE_STYLE");

    /**
     * Stores the use of a web service (literal, encoded).
     */
    public static final String TAGGEDVALUE_WEBSERVICE_USE = profile
        .get("WEBSERVICE_USE");

    /**
     * Stores the provider of the web service (RPC, EJB).
     */
    public static final String TAGGEDVALUE_WEBSERVICE_PROVIDER = profile
        .get("WEBSERVICE_PROVIDER");

    /**
     * Stores the name of the role (if it's different than the name of the actor
     * stereotyped as role)
     */
    public static final String TAGGEDVALUE_ROLE_NAME = profile.get("ROLE_NAME");

    /**
     * Stores the service method transaction type.
     */
    public static final String TAGGEDVALUE_TRANSACTION_TYPE = profile
        .get("TRANSACTION_TYPE");

    /**
     * Used to identify collection types in the model, any other type that will
     * be identified as a collection must specialize this type.
     */
    public static final String COLLECTION_TYPE_NAME = profile
        .get("COLLECTION_TYPE");

    /**
     * Used to identify a list type in the model, any other type that will be
     * identified as a list must specialize this type.
     */
    public static final String LIST_TYPE_NAME = profile.get("LIST_TYPE");

    /**
     * Used to identify a set type in the model, any other type that will be
     * identified as a set must specialize this type.
     */
    public static final String SET_TYPE_NAME = profile.get("SET_TYPE");

    /**
     * Used to identify date types in the model, any other type that will be
     * identified as a date must specialize this type.
     */
    public static final String DATE_TYPE_NAME = profile.get("DATE_TYPE");

    /**
     * Used to indentify a boolean type in the model, any other type that will
     * be identified as a booleon type must specialize this type.
     */
    public static final String BOOLEAN_TYPE_NAME = profile.get("BOOLEAN_TYPE");

    /**
     * Used to indentify a boolean type in the model, any other type that will
     * be identified as a booleon type must specialize this type.
     */
    public static final String FILE_TYPE_NAME = profile.get("FILE_TYPE");

    /**
     * Used to indentify a boolean type in the model, any other type that will
     * be identified as a booleon type must specialize this type.
     */
    public static final String MAP_TYPE_NAME = profile.get("MAP_TYPE");
}