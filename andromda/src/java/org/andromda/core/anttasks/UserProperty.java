package org.andromda.core.anttasks;

/**
 * This class represents user properties which are defined
 * in the ant build.xml file as nested tags within the 
 * <code>&lt;uml2ejb&gt;</code> tag. Uml2EjbGenTask will collect the
 * UserProperty objects and put them into the Velocity
 * context. This makes the name-value-pairs available to
 * templates.
 * <p />
 * An example usage could be:
 * <blockquote><pre>
 * &lt;uml2ejb ...&gt;
 *   &lt;userProperty name="foreignKeySuffix" value="_FK" /&gt;
 * &lt;/uml2ejb&gt;
 * </pre></blockquote>
 * 
 * @author Matthias Bohlen
 *
 */
public class UserProperty {
    private String name;
    private String value;
    
    /**
     * Returns the name.
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the value.
     * @return String
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the name.
     * @param name The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the value.
     * @param value The value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

}
