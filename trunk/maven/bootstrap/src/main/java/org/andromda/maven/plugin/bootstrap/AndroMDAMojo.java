package org.andromda.maven.plugin.bootstrap;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.andromda.core.AndroMDA;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.configuration.Configuration;
import org.andromda.core.configuration.Model;
import org.andromda.core.configuration.Repository;
import org.andromda.maven.plugin.configuration.AbstractConfigurationMojo;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;


/**
 * This is exactly the same as the regular AndroMDAMojo in the
 * andromda-maven-plugin, however this is the <em>bootstrap</em> plugin which
 * is used to run AndroMDA in bootstrap mode (with the bootstrap artifacts).
 *
 * @author Chad Brandon
 * @goal run
 * @phase generate-sources
 * @requiresDependencyResolution runtime
 */
public class AndroMDAMojo
    extends AbstractConfigurationMojo
{
    /**
     * This is the URI to the AndroMDA configuration file.
     *
     * @parameter expression="file:${basedir}/conf/andromda.xml"
     * @required
     */
    private String configurationUri;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * @parameter expression="${project.build.filters}"
     */
    private List<String> propertyFiles;

    /**
     * Whether or not a last modified check should be performed before running
     * AndroMDA again.
     *
     * @parameter expression="false"
     * @required
     */
    private boolean lastModifiedCheck;

    /**
     * The directory to which the build source is located (any generated
     * source).
     *
     * @parameter expression="${project.build.directory}/src/main/java"
     */
    private File buildSourceDirectory;

    /**
     * The directory where the model generation output history is located
     * (Modelname file containing a list of files generated by that model).
     *
     * @parameter expression="${project.build.directory}/history"
     */
    private File modelOutputHistory;

    /**
     * The current user system settings for use in Maven. (allows us to pass the
     * user settings to the AndroMDA configuration).
     *
     * @parameter expression="${settings}"
     * @required
     * @readonly
     */
    private Settings settings;

    /**
     * @component role="org.apache.maven.artifact.factory.ArtifactFactory"
     * @required
     * @readonly
     */
    private ArtifactFactory factory;

    /**
     * The registered plugin implementations.
     *
     * @parameter expression="${project.build.plugins}"
     * @required
     * @readonly
     */
    protected List<Plugin> plugins;

    /**
     * @parameter expression="${localRepository}"
     * @required
     * @readonly
     */
    protected ArtifactRepository localRepository;

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException
    {
        try
        {
            final URL configurationUri = ResourceUtils.toURL(this.configurationUri);
            if (configurationUri == null)
            {
                throw new MojoExecutionException("Configuration could not be loaded from '" + this.configurationUri +
                        '\'');
            }
            final Configuration configuration = this.getConfiguration(configurationUri);
            boolean execute = true;
            if (this.lastModifiedCheck)
            {
                execute = ResourceUtils.modifiedAfter(
                        ResourceUtils.getLastModifiedTime(configurationUri),
                        this.buildSourceDirectory);
                if (!execute)
                {
                    //TODO This duplicates functionality in ModelProcessor. It also doesn't work if files are generated to multiple different project locations.
                    final Repository[] repositories = configuration.getRepositories();
                    int repositoryCount = repositories.length;
                    for (int ctr = 0; ctr < repositoryCount; ctr++)
                    {
                        final Repository repository = repositories[ctr];
                        if (repository != null)
                        {
                            final Model[] models = repository.getModels();
                            final int modelCount = models.length;
                            for (int ctr2 = 0; ctr2 < modelCount; ctr2++)
                            {
                                final Model model = models[ctr2];
                                execute = ResourceUtils.modifiedAfter(
                                        model.getLastModified(),
                                        this.buildSourceDirectory);
                                if (execute)
                                {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (execute)
            {
                this.initializeClasspathFromClassPathElements(this.project.getRuntimeClasspathElements());
                final AndroMDA andromda = AndroMDA.newInstance();
                andromda.run(configuration, lastModifiedCheck, this.modelOutputHistory.getAbsolutePath());
                andromda.shutdown();
            }
            else
            {
                this.getLog().info("Files are up-to-date, skipping AndroMDA execution");
            }
            if (this.buildSourceDirectory != null)
            {
                this.getProject().addCompileSourceRoot(this.buildSourceDirectory.toString());
            }
        }
        catch (Throwable throwable)
        {
            String message = "Error running AndroMDA";
            throwable = ExceptionUtils.getRootCause(throwable);
            if (throwable instanceof MalformedURLException || throwable instanceof FileNotFoundException)
            {
                message = "Configuration is not valid '" + this.configurationUri + '\'';
            }
            throw new MojoExecutionException(message, throwable);
        }
    }

    /**
     * @param configurationUri The configurationUri to set.
     */
    public void setConfigurationUri(String configurationUri)
    {
        this.configurationUri = configurationUri;
    }

    /**
     * @param project The project to set.
     */
    public void setProject(MavenProject project)
    {
        this.project = project;
    }

    /**
     * Sets the current settings for this Mojo.
     *
     * @param settings The settings to set.
     */
    public void setSettings(Settings settings)
    {
        this.settings = settings;
    }

    /**
     * Sets the property files for this project.
     *
     * @param propertyFiles
     */
    public void setPropertyFiles(List<String> propertyFiles)
    {
        this.propertyFiles = propertyFiles;
    }

    /**
     * @see org.andromda.maven.plugin.configuration.AbstractConfigurationMojo#getProject()
     */
    protected MavenProject getProject()
    {
        return this.project;
    }

    /**
     * @see org.andromda.maven.plugin.configuration.AbstractConfigurationMojo#getPropertyFiles()
     */
    protected List<String> getPropertyFiles()
    {
        return this.propertyFiles;
    }

    /**
     * @see org.andromda.maven.plugin.configuration.AbstractConfigurationMojo#getSettings()
     */
    protected Settings getSettings()
    {
        return this.settings;
    }

    /**
     * @see org.andromda.maven.plugin.configuration.AbstractConfigurationMojo#getFactory()
     */
    protected ArtifactFactory getFactory()
    {
        return this.factory;
    }

    /**
     * @see org.andromda.maven.plugin.configuration.AbstractConfigurationMojo#getPlugins()
     */
    protected List<Plugin> getPlugins()
    {
        return this.plugins;
    }

    /**
     * @see org.andromda.maven.plugin.configuration.AbstractConfigurationMojo#getLocalRepository()
     */
    protected ArtifactRepository getLocalRepository()
    {
        return this.localRepository;
    }
}