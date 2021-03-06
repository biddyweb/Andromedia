package org.andromda.repositories.emf.uml22;

import java.util.Set;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * Special resource set implementation that can resolve proxies
 * created by Rational Software Modeler. RSM uses a special syntax
 * (e.g. "resource.emx#someXmiId?someNonstandardString") that cannot
 * be resolved by standard EMF resource sets.
 *
 * @author Matthias Bohlen
 * @author Bob Fields Check for timeouts only once, add logger
 *
 */
public class EMXProxyResolvingResourceSet extends ResourceSetImpl
{
    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(EMXProxyResolvingResourceSet.class);

    /**
     *
     */
    private static Set<String> connectExceptions = new TreeSet<String>();
    /**
     * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#getEObject(org.eclipse.emf.common.util.URI, boolean)
     */
    public EObject getEObject(URI uri, boolean loadOnDemand)
    {
        EObject possiblyResolvedObject = null;
        // We see ConnectExceptions when loading metamodels behind a proxy server. Don't try more than once.
        // No need to validate external schema elements, it's just a performance drag.
        if (connectExceptions.contains(uri.toString()))
        {
            return possiblyResolvedObject;
        }
        long now = System.currentTimeMillis();
        try
        {
            // Allow both UML2 v2 and v3+ profile references. Changed from Standard.profile.uml to StandardL2.profile.uml and StandardL3.profile.uml in v3
            //if (uri.toString().contains("Standard.profile.uml"))
            //{
                /*String newUri = uri.toString().replace("Standard.profile.uml", "StandardL2.profile.uml");
                possiblyResolvedObject = super.getEObject(URI.createURI(newUri), loadOnDemand);*/
                //uri = URI.createURI(uri.toString().replace("Standard.profile.uml", "StandardL3.profile.uml"));
            //}
            possiblyResolvedObject = super.getEObject(uri, loadOnDemand);
        }
        catch (Exception e)
        {
            // This will point out invalid model references....
            if (uri.toString().indexOf('?')<0)
            {
                if (e.getMessage().contains("FileNotFoundException"))
                {
                    // MagicDraw and Standard Profiles generally not needed, exporting them takes lots of space
                    // Don't need entire stack trace if referenced file is not found, message is enough.
                    logger.warn("Referenced model FileNotFound: " + uri + " " + (System.currentTimeMillis() - now) + " ms: " +
                            uri.toString() + " " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
                }
                else
                {
                    logger.error("Could not load referenced model uri " + " " + (System.currentTimeMillis() - now) + "ms: " +
                            uri.toString() + " " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()), e.getCause());
                }
            }
            if (!connectExceptions.contains(uri.toString()))
            {
                connectExceptions.add(uri.toString());
            }
        }
        if (possiblyResolvedObject == null)
        {
            // if it is still a proxy, try this special fix for RSM:
            String uriString = uri.toString();
            int separatorIndex = uriString.lastIndexOf('?');
            if (separatorIndex > 0)
            {
                uriString = uriString.substring(0, separatorIndex);
                if (!connectExceptions.contains(uriString))
                {
                    try
                    {
                        possiblyResolvedObject = super.getEObject(URI.createURI(uriString), loadOnDemand);
                    }
                    catch (Exception e)
                    {
                        // No need to log this - it doesn't matter for andromda anyway.
                        //logger.warn("ConnectException "  + (System.currentTimeMillis() - now) + "ms for uri " + uriString);
                        if (!connectExceptions.contains(uriString))
                        {
                            connectExceptions.add(uri.toString());
                        }
                    }
                }
            }
        }
        return possiblyResolvedObject;
    }
}
