package org.andromda.maven.plugin.andromdanetapp;

import org.andromda.maven.plugin.andromdapp.AndroMDAppMojo;

import org.andromda.andromdapp.AndroMDApp;
import org.andromda.core.common.ResourceUtils;
import org.apache.maven.plugin.MojoExecutionException;
import java.net.URL;

/**
 * The AndroMDAapp mojo (this should be extended by any Mojo that
 * executes AndroMDApp.
 *
 * @author Chad Brandon
 * @goal generate
 * @requiresProject false
 */
public class AndroMDANetAppMojo
    extends AndroMDAppMojo
{
    /**
     * An AndroMDApp configuration that contains some internal configuration information (like the AndroMDA
     * version, etc).
     */
    private static final String INTERNAL_CONFIGURATION_URI = "META-INF/andromdapp/configuration.xml";


    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException
    {
        try
        {
            AndroMDApp andromdapp = new AndroMDApp();

            final URL internalConfiguration = ResourceUtils.getResource(INTERNAL_CONFIGURATION_URI);
            if (internalConfiguration == null)
            {
                throw new MojoExecutionException("No configuration could be loaded from --> '" +
                    INTERNAL_CONFIGURATION_URI + "'");
            }
            andromdapp.addConfigurationUri(internalConfiguration.toString());
            final String configuration = this.getConfigurationContents();
            if (configuration != null)
            {
                andromdapp.addConfiguration(this.getConfigurationContents());
            }
            andromdapp.run();

        }
        catch (final Throwable throwable)
        {
            if (throwable instanceof MojoExecutionException)
            {
                throw (MojoExecutionException)throwable;
            }
            throw new MojoExecutionException("An error occurred while attempting to generate an application", throwable);
        }
    }

}
