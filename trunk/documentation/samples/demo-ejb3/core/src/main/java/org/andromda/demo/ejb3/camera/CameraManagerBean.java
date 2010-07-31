// license-header java merge-point
//
// Generated by: SessionBeanImpl.vsl in andromda-ejb3-cartridge.
//
package org.andromda.demo.ejb3.camera;

/**
 * @see org.andromda.demo.ejb3.camera.CameraManagerBean
 */
/**
 * Do not specify the javax.ejb.Stateless annotation
 * Instead, define the session bean in the ejb-jar.xml descriptor
 * @javax.ejb.Stateless
 */
/**
 * Uncomment to enable webservices for CameraManagerBean
 *@javax.jws.WebService(endpointInterface = "org.andromda.demo.ejb3.camera.CameraManagerWSInterface")
 */
public class CameraManagerBean 
    extends org.andromda.demo.ejb3.camera.CameraManagerBase 
{
    // --------------- Constructors ---------------
    
    public CameraManagerBean()
    {
        super();
    }

    // -------- Business Methods Impl --------------
    
    /**
     * @see org.andromda.demo.ejb3.camera.CameraManagerBase#addCamera(org.andromda.demo.ejb3.camera.Camera)
     */
    protected void handleAddCamera(org.andromda.demo.ejb3.camera.Camera camera)
        throws java.lang.Exception
    {
        getCameraDao().create(camera);
    }

    /**
     * @see org.andromda.demo.ejb3.camera.CameraManagerBase#getCamera(java.lang.String, java.lang.String)
     */
    protected org.andromda.demo.ejb3.camera.Camera handleGetCamera(java.lang.String make, java.lang.String model)
        throws java.lang.Exception
    {
        return getCameraDao().load(make, model);
    }

    /**
     * @see org.andromda.demo.ejb3.camera.CameraManagerBase#deleteCamera(java.lang.String, java.lang.String)
     */
    protected void handleDeleteCamera(java.lang.String make, java.lang.String model)
        throws java.lang.Exception
    {
        getCameraDao().remove(make, model);
    }


    // -------- Lifecycle Callback Impl --------------
    
}
