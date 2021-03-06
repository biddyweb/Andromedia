package org.andromda.maven.plugin;

import org.andromda.core.AndroMDAServer;
import org.andromda.core.configuration.Configuration;

/**
 * Provides the ability to start the AndroMDA server.
 *
 * @author Chad Brandon
 * @goal start-server
 * @requiresProject false
 * @requiresDependencyResolution runtime
 */
public class AndroMDAServerStartMojo
    extends AbstractAndroMDAMojo
{
    /**
     * @see org.andromda.maven.plugin.AbstractAndroMDAMojo#execute(org.andromda.core.configuration.Configuration)
     */
    public void execute(final Configuration configuration)
    {
        this.allowMultipleRuns = true;
        final AndroMDAServer server = AndroMDAServer.newInstance();
        server.start(configuration);
    }
}