package org.andromda.core.dbmapping;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;

import org.andromda.core.common.DbMappingTable;

/**
 * <p>This is a lookup table for the mapping from Java data types
 * to JDBC and SQL data types.</p>
 * 
 * <p>It uses the mapping info that was read from the XML
 * configuration file.</p>
 * 
 * @author Matthias Bohlen
 *
 */
public class JAXBDbMappingTable
	implements DbMappingTable
{
    private HashMap map = new HashMap();

    /**
     * Initializes the lookup table with a set of mappings
     * that have been read from an XML file.
     * 
     * @param xmlMappings the set of mappings
     */
    public JAXBDbMappingTable(Mappings xmlMappings)
    {
        for (Iterator it = xmlMappings.getMappings().iterator(); it.hasNext();)
        {
            Mapping m = (Mapping) it.next();

            // Register the mapping under each Java type name.
            for (Iterator it2 = m.getTypes().iterator(); it2.hasNext();)
            {
                String typeName = (String) it2.next();
                map.put(typeName, m);
            }
        }
    }

    /**
     * Returns the JDBC type for the given Java type.
     * 
     * @param javaType name of the Java type (e.g. "java.util.Date")
     * @return String name of the JDBC type (e.g. "DATE")
     */
    public String getJDBCType(String javaType)
    {
        Mapping m = (Mapping) map.get(javaType);
        if (null == m)
        {
            return "** MISSING JDBC type mapping for " + javaType;
        }

        return m.getJdbcType().getName();
    }

    /**
     * Returns the SQL type for the fiven Java type.
     * See your database docs for this syntax.
     * @param javaType name of the Java type (e.g. "java.math.BigDecimal")
     * @param sqlFieldLength desired field length in the database table
     * @return String the complete SQL field syntax (e.g. "DECIMAL(9)")
     */
    public String getSQLType(String javaType, String desiredFieldLength)
    {
        Mapping m = (Mapping) map.get(javaType);
        if (null == m)
        {
            return "** MISSING SQL type mapping for " + javaType;
        }
        SQLType sqlType = m.getSqlType();

        String pattern = sqlType.getPattern();

        String fieldLength =
            (null == desiredFieldLength)
                || ("".equals(desiredFieldLength))
                    ? sqlType.getDefaultLength()
                    : desiredFieldLength;
        Object[] arguments = { fieldLength };

        return MessageFormat.format(pattern, arguments);
    }
}
