package org.andromda.maven.plugin.cartridge;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * <p>
 * This is the entry point of the cartridge test suite for AndroMDA. The
 * test checks for a list of expected files that a file with the same name and
 * the same package was generated by AndroMDA and that the APIs of the expected
 * file and the generated file are equal. <code>CartridgeTest</code> acts as
 * the test director which creates the list of files to be compared. The actual
 * API comparison is carried out by instances of {@link FileComparator}.
 * </p>
 *
 * @author Ralf Wirdemann
 * @author Chad Brandon
 * @author Michail Plushnikov
 * @author Bob Fields
 */
public class CartridgeTest
    extends TestCase
{
    private static final Logger logger = Logger.getLogger(CartridgeTest.class);

    /**
     * Points to the directory where the expected files are stored which will be
     * compared to the generated ones.
     */
    public static final String EXPECTED_DIRECTORY = "expected.dir";

    /**
     * Points to the directory where the generated files are located.
     */
    public static final String ACTUAL_DIRECTORY = "actual.dir";

    /**
     * Defines the suffixes of binary files (files that will be not be
     * compared as strings).
     */
    public static final String BINARY_SUFFIXES = "binary.suffixes";
    
    /**
     * The shared instance of this class.
     */
    private static CartridgeTest instance;
    
    /**
     * Retrieves the shared instance of this class.
     * 
     * @return the shared instance.
     */
    public static final CartridgeTest instance()
    {
        if (instance == null)
        {
            instance = new CartridgeTest();
        }
        return instance;
    }
    
    private CartridgeTest()
    {
        super();
    }
    
    /**
     * Stores a comma separated list of binary suffixes.
     */
    private String binarySuffixes = StringUtils.trimToEmpty(System.getProperty(BINARY_SUFFIXES));
    
    /**
     * Sets the value of the suffixes that indicate a binary file (binary files
     * are not compared as text).
     * 
     * @param binarySuffixes a comma separated list of binary suffixes.
     */
    public void setBinarySuffixes(final String binarySuffixes)
    {
        this.binarySuffixes = binarySuffixes;
    }
    
    /**
     * Stores the actual directory.
     */
    private String actualOutputPath = StringUtils.trimToEmpty(System.getProperty(ACTUAL_DIRECTORY));
    
    /**
     * Sets the path to the <em>actual</em> directory (that is the 
     * directory which contains the actual output being tested.
     * 
     * @param actualOutputPath the path to the actual directory.
     */
    public void setActualOutputPath(final String actualOutputPath)
    {
        this.actualOutputPath = actualOutputPath;
    }
    
    /**
     * Stores the path to the excepted directory.
     */
    private String expectedOutputPath = StringUtils.trimToEmpty(System.getProperty(EXPECTED_DIRECTORY));
    
    /**
     * Sets the path to the <em>expected</em> directory.  This is the directory
     * to which the expected output is extracted.
     * 
     * @param expectedOutputPath the path to the expected output directory.
     */
    public void setExpectedOutputPath(final String expectedOutputPath)
    {
        this.expectedOutputPath = expectedOutputPath;
    }

    /**
     * @param name
     */
    public CartridgeTest(final String name)
    {
        super(name);
    }

    /**
     * @return suite
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        instance().addTests(suite);
        return suite;
    }

    /**
     * Adds tests which compare all actual generated files against the expected
     * files.
     *
     * @param suite the test suite to which we'll add the tests.
     */
    private void addTests(final TestSuite suite)
    {
        Collection<File> expectedFiles = Collections.emptyList();

        final File directory = this.getExpectedOutputDirectory();
        if(null!=directory && directory.exists())
        {
            expectedFiles = FileUtils.listFiles(
                    directory.isDirectory()? directory : directory.getParentFile(),
                    TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        }

        final Iterator<File> iterator = expectedFiles.iterator();
        logger.info(" --- Expecting " + expectedFiles.size() + " Generated Files --- ");
        logger.info("ExpectedOutputDirectory --> " + this.getExpectedOutputDirectory());
        final List<File> missingFiles = new ArrayList<File>();
        for (int ctr = 1; iterator.hasNext(); ctr++)
        {
            final File expectedFile = iterator.next();
            final File actualFile = getActualFile(expectedFile);
            if (!actualFile.exists())
            {
                missingFiles.add(actualFile);
            }
            final boolean binary = isBinary(actualFile);
            if (logger.isDebugEnabled())
            {
                logger.debug(ctr + ") binary = " + binary);
                logger.debug("expected --> '" + expectedFile + '\'');
                logger.debug("actual   --> '" + actualFile + '\'');
            }
            //Ignore 'generated on ' date comments different between expected/actual
            // Sometimes it says 'Autogenerated on 04/22/2010 14:49:09-0400 by AndroMDA', sometimes it says 'Generated by ' XX cartridge
            // Remove the rest of the line from the comparison.
            List<String> strings = new ArrayList<String>(2);
            strings.add("enerated by ");
            strings.add("enerated on ");
            suite.addTest(new FileComparator(
                    "testEquals",
                    expectedFile,
                    actualFile,
                    binary,
                    false,
                    strings));
        }
        if (!missingFiles.isEmpty())
        {
            Collections.sort(missingFiles);
            StringBuilder failureMessage = new StringBuilder("\n--- The following ");
            failureMessage.append(missingFiles.size());
            failureMessage.append(" expected files do not exist ----\n");
            Iterator<File> missingFileIterator = missingFiles.iterator();
            for (int ctr = 1; missingFileIterator.hasNext(); ctr++)
            {
                failureMessage.append(ctr).append(") ");
                failureMessage.append(missingFileIterator.next());//.append('\n');
                if (missingFileIterator.hasNext())
                {
                    failureMessage.append('\n');
                }
            }
            TestCase.fail(failureMessage.toString());
        }
    }

    /**
     * Constructs the expected file path from the <code>actualFile</code> and
     * the <code>expectedDir</code> path.
     *
     * @param actualFile the actual generated file
     * @return the new expected file.
     */
    private File getActualFile(final File expectedFile)
    {
        String actualFile;
        final File actualOutputDirectory = this.getActualOutputDirectory();
        final File expectedOutputDirectory = this.getExpectedOutputDirectory();
        final String path = expectedFile.getPath();
        if (expectedFile.getPath().startsWith(actualOutputDirectory.getPath()))
        {
            actualFile = path.substring(
                actualOutputDirectory.getPath().length(),
                    path.length());
            actualFile = expectedOutputDirectory + expectedFile.toString();
        }
        else
        {
            actualFile = path.substring(
                expectedOutputDirectory.getPath().length(),
                    path.length());
            actualFile = actualOutputDirectory + actualFile;
        }
        return new File(actualFile);
    }
    
    /**
     * The expected output directory.
     */
    private File expectedOutputDirectory;

    /**
     * Retrieves the expected output directory.
     * 
     * @return the file representing the directory.
     */
    private File getExpectedOutputDirectory()
    {
       if (this.expectedOutputDirectory == null)
       {
           this.expectedOutputDirectory = this.getDirectory(this.expectedOutputPath);
       }
       return this.expectedOutputDirectory;
    }
    
    /**
     * The actual output directory.
     */
    private File actualOutputDirectory;
    
    private File getActualOutputDirectory()
    {
        if (this.actualOutputDirectory == null)
        {
            this.actualOutputDirectory = this.getDirectory(this.actualOutputPath);
        }
        return this.actualOutputDirectory;
    }
    
    /**
     * Gets the directory from the system property key.
     *
     * @param path the system property key name.
     * @return the directory as a File instance.
     */
    private File getDirectory(final String path)
    {
        File directory = new File(path);
        if (!directory.exists() || !directory.isDirectory())
        {
            throw new RuntimeException("directory <" + path + "> doesn't exist");
        }
        return directory;
    }

    /**
     * Checks whether or not the <code>file</code> is a binary file. Does this
     * by checking to see if the suffix is found in the list of binary suffixes.
     *
     * @param file the file to check
     * @return true/false
     */
    private boolean isBinary(final File file)
    {
        String suffix = FilenameUtils.getExtension(file.getName());        
        return this.getBinarySuffixes().contains(suffix);
    }
    
    private Collection<String> binarySuffixCollection;

    /**
     * Gets the binary suffixes for the <code>binary.suffixes</code> system
     * property. Returns an empty collection if none are found.
     *
     * @return the Collection of binary suffixes. (ie. jpg, jar, zip, etc).
     */
    private Collection<String> getBinarySuffixes()
    {
        if (this.binarySuffixCollection == null)
        {
            final String suffixes = this.binarySuffixes != null ? this.binarySuffixes.trim() : "";
            final String[] suffixArray = suffixes.split("\\s*,\\s*");
            this.binarySuffixCollection = Arrays.asList(suffixArray);
        }
        return this.binarySuffixCollection;
    }
    
    /**
     * Releases any resources held by this cartridge test instance.
     */
    public void shutdown()
    {
        CartridgeTest.instance = null;
    }
}