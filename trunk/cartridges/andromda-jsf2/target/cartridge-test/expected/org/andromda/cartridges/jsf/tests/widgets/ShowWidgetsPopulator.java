// license-header java merge-point
// Generated by andromda-jsf cartridge (flow\ViewPopulator.java.vsl) DO NOT EDIT!
package org.andromda.cartridges.jsf.tests.widgets;

import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.faces.el.VariableResolver;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.andromda.presentation.jsf.AdfFacesContextWrapper;
import org.andromda.presentation.jsf.FacesContextUtils;
import org.andromda.presentation.jsf.FormPopulator;
import org.andromda.presentation.jsf.JsfUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.myfaces.trinidad.context.RequestContext;

/**
 * This filter handles the population of forms for the <em>show widgets</code>
 * view.
 */
public class ShowWidgetsPopulator
    implements Filter
{
    private FilterConfig config;

    /**
     * Initialize the filter
     *
     * @param configIn the configuration
     * @see javax.servlet.Filter#init(FilterConfig)
     */
    public void init(FilterConfig configIn)
    {
        this.config = configIn;
    }

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain) throws IOException, ServletException
    {
        // - we need to retrieve the faces context differently since we're outside of the
        //   faces servlet
        populateFormAndViewVariables(FacesContextUtils.getFacesContext(request, response), null);
        chain.doFilter(request, response);
    }

    /**
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy()
    {
        this.config = null;
    }

    /**
     * @param facesContext
     * @param form
     * @throws ServletException
     */
    public static void populateFormAndViewVariables(final FacesContext facesContext, Object form)
        throws ServletException
    {
        final HttpServletRequest request = (HttpServletRequest)facesContext.getExternalContext().getRequest();
        final HttpSession session = (HttpSession)facesContext.getExternalContext().getSession(true);
        RequestContext adfContext = RequestContext.getCurrentInstance();
        final VariableResolver variableResolver = facesContext.getApplication().getVariableResolver();
        if (form == null)
        {
            // - first try getting the form from the ADF PageFlowScope
            form = adfContext.getPageFlowScope().get("form");
            // - if the form is null, try getting the current adfContext from the session (and then remove it from the session)
            if (form == null)
            {
                final AdfFacesContextWrapper contextWrapper =
                    (AdfFacesContextWrapper)session.getAttribute("AndroMDAADFContext");
                adfContext = contextWrapper != null ? contextWrapper.getCurrentInstance() : null;
                form = adfContext != null ? adfContext.getPageFlowScope().get("form") : null;
                if (form == null)
                {
                    form = session.getAttribute("form");
                    session.removeAttribute("form");
                }
                // - if the form is still null, see if we can get it from a serialized state
                if (form == null)
                {
                    form = JsfUtils.getSerializedForm(facesContext);
                }
                if (form != null)
                {
                    // - add the form to the current process scope since it wasn't in the current one to begin with
                    RequestContext.getCurrentInstance().getPageFlowScope().put("form", form);
                }
            }
            else
            {
                // - remove the ADF context in the event that its present
                session.removeAttribute("AndroMDAADFContext");
            }
        }
        else
        {
            // - since the form argument is not null, set it as the "form" in the PageFlowScope
            //   (to replace the existing "form" attribute)
            adfContext.getPageFlowScope().put("form", form);
        }
        try
        {
            // - populate the forms
            if (form != null)
            {
                ShowWidgetsSubmitFormImpl widgetsActivityShowWidgetsSubmitForm =
                    (ShowWidgetsSubmitFormImpl)variableResolver.resolveVariable(
                    facesContext,
                    "widgetsActivityShowWidgetsSubmitForm");
                // - populate the widgetsActivityShowWidgetsSubmitForm with any parameters from the previous form
                FormPopulator.populateForm(form, widgetsActivityShowWidgetsSubmitForm, false);
                request.setAttribute("widgetsActivityShowWidgetsSubmitForm", widgetsActivityShowWidgetsSubmitForm);
            }
            // - serialize the form
            if (form != null)
            {
                JsfUtils.serializeForm(facesContext, form);
            }
            // - populate the view variables
            if (form != null)
            {
                final boolean radioButtonsTestBackingListReadable = PropertyUtils.isReadable(form, "radioButtonsTestBackingList");
                if (radioButtonsTestBackingListReadable)
                {
                    Object backingList = PropertyUtils.getProperty(form, "radioButtonsTestBackingList");
                    request.setAttribute("radioButtonsTestBackingList", backingList);
                }
                final boolean radioButtonsTest2BackingListReadable = PropertyUtils.isReadable(form, "radioButtonsTest2BackingList");
                if (radioButtonsTest2BackingListReadable)
                {
                    Object backingList = PropertyUtils.getProperty(form, "radioButtonsTest2BackingList");
                    request.setAttribute("radioButtonsTest2BackingList", backingList);
                }
                final boolean radioButtonsTest3BackingListReadable = PropertyUtils.isReadable(form, "radioButtonsTest3BackingList");
                if (radioButtonsTest3BackingListReadable)
                {
                    Object backingList = PropertyUtils.getProperty(form, "radioButtonsTest3BackingList");
                    request.setAttribute("radioButtonsTest3BackingList", backingList);
                }
                final boolean selectTestBackingListReadable = PropertyUtils.isReadable(form, "selectTestBackingList");
                if (selectTestBackingListReadable)
                {
                    Object backingList = PropertyUtils.getProperty(form, "selectTestBackingList");
                    request.setAttribute("selectTestBackingList", backingList);
                }
            }
        }
        catch (final Throwable throwable)
        {
            throw new ServletException(throwable);
        }
    }
}