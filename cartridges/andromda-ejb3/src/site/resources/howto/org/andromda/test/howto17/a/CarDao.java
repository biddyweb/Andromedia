// license-header java merge-point
//
// Attention: Generated code! Do not modify by hand!
// Generated by: SessionDaoLocal.vsl in andromda-ejb3-cartridge.
//
package org.andromda.howto2.rental;

/**
 * @see org.andromda.howto2.rental.Car
 */
public interface CarDao
{
    /**
     * This constant is used as a transformation flag; entities can be converted automatically into value objects
     * or other types, different methods in a class implementing this interface support this feature: look for
     * an <code>int</code> parameter called <code>transform</code>.
     * <p/>
     * This specific flag denotes no transformation will occur.
     */
    public static final int TRANSFORM_NONE = 0;

    /**
     * Loads an instance of org.andromda.howto2.rental.Car from the persistent store.
     * @param id the identifier of the entity to load.
     * @throws org.andromda.howto2.rental.CarDaoException
     */
    public org.andromda.howto2.rental.Car load(java.lang.Long id)
        throws org.andromda.howto2.rental.CarDaoException;

    /**
     * <p>
     *   Does the same thing as {@link #load(java.lang.Long)} with an
     *   additional flag called <code>transform</code>.  If this flag is set to <code>TRANSFORM_NONE</code> then
     *   the returned entity will <strong>NOT</strong> be transformed.  If this flag is any of the other constants
     *   defined in this class then the result <strong>WILL BE</strong> passed through an operation which can
     *   optionally transform the entity (into a value object for example).  By default, transformation does
     *   not occur.
     * </p>
     *
     * @param id the identifier of the entity to load.
     * @return either the entity or the object transformed from the entity.
     * @throws org.andromda.howto2.rental.CarDaoException
     */
    public Object load(int transform, java.lang.Long id)
        throws org.andromda.howto2.rental.CarDaoException;

    /**
     * Loads all entities of type {@link org.andromda.howto2.rental.Car}.
     *
     * @return the loaded entities.
     * @throws org.andromda.howto2.rental.CarDaoException
     */
    public java.util.Collection<org.andromda.howto2.rental.Car> loadAll()
        throws org.andromda.howto2.rental.CarDaoException;

    /**
     * <p>
     *   Does the same thing as {@link #loadAll()} with an
     *   additional flag called <code>transform</code>.  If this flag is set to <code>TRANSFORM_NONE</code> then
     *   the returned entity will <strong>NOT</strong> be transformed.  If this flag is any of the other constants
     *   defined here then the result <strong>WILL BE</strong> passed through an operation which can optionally
     *   transform the entity (into a value object for example).  By default, transformation does
     *   not occur.
     * </p>
     *
     * @param transform the flag indicating what transformation to use.
     * @return the loaded entities.
     * @throws org.andromda.howto2.rental.CarDaoException
     */
    public java.util.Collection loadAll(final int transform)
        throws org.andromda.howto2.rental.CarDaoException;

    /**
     * Creates an instance of org.andromda.howto2.rental.Car and adds it to the persistent store.
     * @throws org.andromda.howto2.rental.CarDaoException
     */
    public org.andromda.howto2.rental.Car create(org.andromda.howto2.rental.Car car)
        throws org.andromda.howto2.rental.CarDaoException;

    /**
     * <p>
     *   Does the same thing as {@link #create(org.andromda.howto2.rental.Car)} with an
     *   additional flag called <code>transform</code>.  If this flag is set to <code>TRANSFORM_NONE</code> then
     *   the returned entity will <strong>NOT</strong> be transformed.  If this flag is any of the other constants
     *   defined here then the result <strong>WILL BE</strong> passed through an operation which can optionally
     *   transform the entity (into a value object for example).  By default, transformation does
     *   not occur.
     * </p>
     *
     * @throws org.andromda.howto2.rental.CarDaoException
     */
    public Object create(int transform, org.andromda.howto2.rental.Car car)
        throws org.andromda.howto2.rental.CarDaoException;

    /**
     * Creates a new instance of org.andromda.howto2.rental.Car and adds
     * from the passed in <code>entities</code> collection
     *
     * @param entities the collection of org.andromda.howto2.rental.Car
     *        instances to create.
     *
     * @return the created instances.
     * @throws org.andromda.howto2.rental.CarDaoException
     */
    public java.util.Collection<org.andromda.howto2.rental.Car> create(java.util.Collection<org.andromda.howto2.rental.Car> entities)
        throws org.andromda.howto2.rental.CarDaoException;

    /**
     * <p>
     *   Does the same thing as {@link #create(org.andromda.howto2.rental.Car)} with an
     *   additional flag called <code>transform</code>.  If this flag is set to <code>TRANSFORM_NONE</code> then
     *   the returned entity will <strong>NOT</strong> be transformed.  If this flag is any of the other constants
     *   defined here then the result <strong>WILL BE</strong> passed through an operation which can optionally
     *   transform the entities (into value objects for example).  By default, transformation does
     *   not occur.
     * </p>
     *
     * @throws org.andromda.howto2.rental.CarDaoException
     */
    public java.util.Collection create(int transform, java.util.Collection<org.andromda.howto2.rental.Car> entities)
        throws org.andromda.howto2.rental.CarDaoException;

    /**
     * <p>
     *   Creates a new <code>org.andromda.howto2.rental.Car</code>
     *   instance from <strong>all</strong> attributes and adds it to
     *   the persistent store.
     * </p>
     */
    public org.andromda.howto2.rental.Car create(
        String serial,
        String name,
        org.andromda.howto2.rental.CarType type)
        throws org.andromda.howto2.rental.CarDaoException;

    /**
     * <p>
     *   Does the same thing as {@link #create(String, String, org.andromda.howto2.rental.CarType)} with an
     *   additional flag called <code>transform</code>.  If this flag is set to <code>TRANSFORM_NONE</code> then
     *   the returned entity will <strong>NOT</strong> be transformed.  If this flag is any of the other constants
     *   defined here then the result <strong>WILL BE</strong> passed through an operation which can optionally
     *   transform the entity (into a value object for example).  By default, transformation does
     *   not occur.
     * </p>
     *
     * @throws org.andromda.howto2.rental.CarDaoException
     */
    public Object create(
        int transform,
        String serial,
        String name,
        org.andromda.howto2.rental.CarType type)
        throws org.andromda.howto2.rental.CarDaoException;

    /**
     * <p>
     *  Creates a new <code>org.andromda.howto2.rental.Car</code>
     *  instance from only <strong>required</strong> properties (attributes
     *  and association ends) and adds it to the persistent store.
     * </p>
     *
     * @throws org.andromda.howto2.rental.CarDaoException
     */
    public org.andromda.howto2.rental.Car create(
        String name,
        org.andromda.howto2.rental.Person owner,
        String serial,
        org.andromda.howto2.rental.CarType type)
        throws org.andromda.howto2.rental.CarDaoException;

    /**
     * <p>
     *   Does the same thing as {@link #create(String, String, org.andromda.howto2.rental.CarType)} with an
     *   additional flag called <code>transform</code>.  If this flag is set to <code>TRANSFORM_NONE</code> then
     *   the returned entity will <strong>NOT</strong be transformed.  If this flag is any of the other constants
     *   defined here then the result <strong>WILL BE</strong> passed through an operation which can optionally
     *   transform the entity (into a value object for example).  By default, transformation does
     *   not occur.
     * </p>
     *
     * @throws org.andromda.howto2.rental.CarDaoException
     */
    public Object create(
        int transform,
        String name,
        org.andromda.howto2.rental.Person owner,
        String serial,
        org.andromda.howto2.rental.CarType type)
        throws org.andromda.howto2.rental.CarDaoException;

    /**
     * Updates the <code>car</code> instance in the persistent store.
     * @throws org.andromda.howto2.rental.CarDaoException
     */
    public void update(org.andromda.howto2.rental.Car car)
        throws org.andromda.howto2.rental.CarDaoException;

    /**
     * Updates all instances in the <code>entities</code> collection in the persistent store.
     * @throws org.andromda.howto2.rental.CarDaoException
     */
    public void update(java.util.Collection<org.andromda.howto2.rental.Car> entities)
        throws org.andromda.howto2.rental.CarDaoException;

    /**
     * Removes the instance of org.andromda.howto2.rental.Car from the persistent store.
     * @throws org.andromda.howto2.rental.CarDaoException
     */
    public void remove(org.andromda.howto2.rental.Car car)
        throws org.andromda.howto2.rental.CarDaoException;

    /**
     * Removes the instance of org.andromda.howto2.rental.Car having the given
     * <code>identifier</code> from the persistent store.
     * @throws org.andromda.howto2.rental.CarDaoException
     */
    public void remove(java.lang.Long id)
        throws org.andromda.howto2.rental.CarDaoException;

    /**
     * Removes all entities in the given <code>entities<code> collection.
     * @throws org.andromda.howto2.rental.CarDaoException
     */
    public void remove(java.util.Collection<org.andromda.howto2.rental.Car> entities)
        throws org.andromda.howto2.rental.CarDaoException;

    /**
     *
     */
    public java.util.List findByType(org.andromda.howto2.rental.CarType type)
        throws org.andromda.howto2.rental.CarDaoException;

    /**
     * <p>
     *   Does the same thing as {@link #findByType(org.andromda.howto2.rental.CarType)} with an
     *   additional argument called <code>queryString</code>.  This <code>queryString</code>
     *   argument allows you to override the query string defined in {@link #findByType(org.andromda.howto2.rental.CarType)}.
     * </p>
     */

    public java.util.List findByType(String queryString, org.andromda.howto2.rental.CarType type)
        throws org.andromda.howto2.rental.CarDaoException;

    /**
     * <p>
     *   Does the same thing as {@link #findByType(org.andromda.howto2.rental.CarType)} with an
     *   additional flag called <code>transform</code>.  If this flag is set to <code>TRANSFORM_NONE</code> then
     *   finder results will <strong>NOT</strong> be transformed during retrieval.
     *   If this flag is any of the other constants defined here
     *   then finder results <strong>WILL BE</strong> passed through an operation which can optionally
     *   transform the entities (into value objects for example).  By default, transformation does
     *   not occur.
     * </p>
     */

    public java.util.List findByType(int transform, org.andromda.howto2.rental.CarType type)
        throws org.andromda.howto2.rental.CarDaoException;

    /**
     * <p>
     *   Does the same thing as {@link #findByType(boolean, org.andromda.howto2.rental.CarType)} with an
     *   additional argument called <code>queryString</code>.  This <code>queryString</code>
     *   argument allows you to override the query string defined in {@link #findByType(int, org.andromda.howto2.rental.CarType type)}.
     * </p>
     */

    public java.util.List findByType(int transform, String queryString, org.andromda.howto2.rental.CarType type)
        throws org.andromda.howto2.rental.CarDaoException;

    /**
     *
     */
    public boolean allCarsAreRented();

}
