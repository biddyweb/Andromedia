package org.andromda.core.cartridge;

import java.io.File;
import java.text.MessageFormat;

import org.andromda.core.common.Namespaces;
import org.andromda.core.common.Property;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class implements the <code>&lt;template&gt;</code> tag
 * in a cartridge descriptor file.
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * @author Anthony Mowers
 * @author Chad Brandon
 */
public class TemplateConfiguration
{

    /**
     * The default constructor used by the XmlObjectFactory to 
     * instantiate the template configuration.
     */
    public TemplateConfiguration() {
    	this.supportedModelElements = new TemplateModelElements();
    }

    /**
     * Tells us which VelocityTemplateEngine stylesheet to use as a template.
     * @param sheet points to the script
     */
    public void setSheet(String sheet)
    {
        this.sheet = sheet;
    }

    /**
     * Tells us which VelocityTemplateEngine stylesheet to use as a template.
     * @return File points to the script
     */
    public String getSheet()
    {
        return sheet;
    }

    /**
     * Sets the pattern that is used to build the
     * name of the output file.
     * @param outputPattern the pattern in java.text.MessageFormat syntax
     */
    public void setOutputPattern(String outputPattern)
    {
        this.outputPattern = outputPattern;
    }

    /**
     * Gets the pattern that is used to build the
     * name of the output file.
     * @return String the pattern in java.text.MessageFormat syntax
     */
    public String getOutputPattern()
    {
        return outputPattern;
    }

    /**
     * Sets the outlet where the output file that is generated from this
     * template should be placed,
     * @param outlet points to the outlet
     */
    public void setOutlet(String outlet)
    {
        this.outlet = outlet;
    }

    /**
     * Gets the outlet where the output file that is generated from this
     * template should be placed.
     * @return String the outlet alias name
     */
    public String getOutlet()
    {
        return outlet;
    }

    /**
     * Tells us whether output files generated by this
     * template should be overwritten if they already exist.
     * @param overWrite overWrite the file yes/no
     */
    public void setOverWrite(boolean overWrite)
    {
        this.overWrite = overWrite;
    }

    /**
     * Tells us whether output files generated by this
     * template should be overwritten if they already exist.
     * @return boolean
     */
    public boolean isOverWrite()
    {
        Property property = 
            Namespaces.instance().findNamespaceProperty(
                this.getCartridge().getName(), 
                "overWrite", 
                false);
        if (property != null) 
        {
            this.overWrite = Boolean.valueOf(
                property.getValue()).booleanValue();   
        }
        return this.overWrite;
    }

    /**
     * Tells us whether output files should be generated if this
     * template does not produce any output.
     * @param generateEmptyFiles generate files for empty output yes/no
     */
    public void setGenerateEmptyFiles(boolean generateEmptyFiles)
    {
        this.generateEmptyFiles = generateEmptyFiles;
    }

    /**
     * Tells us whether output files are generated by this
     * template if the template produces empty output.
     * @return boolean
     */
    public boolean isGenerateEmptyFiles()
    {
        return generateEmptyFiles;
    }

    /**
     * Returns the fully qualified output file, that means:
     * <ul>
     *     <li>the output pattern has been translated</li>
     *     <li>the output dir name has been prepended</li>
     * </ul>
     * 
     * @param inputClassName name of the class from the UML model
     * @param inputPackageName name of the package from the UML model 
     *                         in which the class is contained
     * @param directoryUri the directory URI 
     * @return File absolute directory.
     */
    public File getOutputLocation(
        String inputClassName,
        String inputPackageName,
        File directory) 
    {
        int dotIndex = sheet.indexOf(".");
        String sheetBaseName = sheet.substring(0, dotIndex);
    
        //clean the strings since they could be null
        inputClassName = StringUtils.trimToEmpty(inputClassName);
        inputPackageName = StringUtils.trimToEmpty(inputPackageName);
        
        File file = null;
        if (directory != null) 
        {
            Object[] arguments = {
                inputPackageName.replace('.', File.separatorChar),
                inputClassName,
                sheetBaseName
            };
            
            String outputFileName;
            //if singleFileOutput is set to true, then
            //just use the output pattern as the file to
            //output to, otherwise we replace using message format.
            if (this.isOutputToSingleFile()) 
            {
                outputFileName = outputPattern;
            } else 
            {
                outputFileName = MessageFormat.format(outputPattern, arguments);
            }
            
            file = new File(directory, outputFileName);
        }
        return file;
    }
    
    /**
     * Tells us the model elements that are supported by 
     * this template (i.e. will be processed by this template)
     * 
     * @return TemplateModelElements all the model elements that
     *         should be processed by thsi template
     * 
     * @see org.andromda.core.cartridge.TemplateModelElements
     */
    public TemplateModelElements getSupportedModeElements()
    {
        final String methodName = "TemplateConfiguration.getModelElements";
        if (this.supportedModelElements == null) {
        	throw new TemplateConfigurationException(methodName
                + " - supportedModelElements is null!");
        }
        return this.supportedModelElements;
    }
    
    /**
     * Sets the model elements that are suported by this template.
     * 
     * @param modelElements the TemplateModelElements instance
     * 
     * @see org.andromda.core.cartridge.TemplateModelElements
     */
    public void setSupportedModelElements(TemplateModelElements supportedModelElements) 
    {
        this.supportedModelElements = supportedModelElements;
    }    
    
    /**
     * If output to single file is <code>true</code>
     * then all model elements found by the processor (i.e.
     * all those having matching modelElements) will be 
     * output to one file.
     * 
     * @return Returns the outputToSingleFile.
     */
    public boolean isOutputToSingleFile() {
        return outputToSingleFile;
    }
    
    /**
     * @param outputToSingleFile The outputToSingleFile to set.
     */
    public void setOutputToSingleFile(boolean outputToSingleFile) {
        this.outputToSingleFile = outputToSingleFile;
    }
    
    /**
     * The cartridge that owns this template configuration.
     * 
     * @return Returns the owning cartridge.
     */
    public Cartridge getCartridge() {
        return cartridge;
    }
    
    /**
     * @param cartridge the parent Cartridge to set.
     */
    public void setCartridge(Cartridge cartridge) {
        this.cartridge = cartridge;
    }
    
    /**
     * Just for debugging.
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }

    private TemplateModelElements supportedModelElements = null;
    private String sheet;
    private String outputPattern;
    private String outlet;
    private boolean overWrite;
    private boolean generateEmptyFiles;
    private boolean outputToSingleFile = false;
    private Cartridge cartridge;

}
