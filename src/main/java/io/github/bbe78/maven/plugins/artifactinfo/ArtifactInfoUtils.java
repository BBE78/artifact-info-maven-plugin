/**
 *
 */
package io.github.bbe78.maven.plugins.artifactinfo;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;


/**
 *
 *
 * @author Benoît BERTHONNEAU
 * @since 7 févr. 2017
 */
final class ArtifactInfoUtils {

    /** The token property pattern. */
    private static final String PROPERTY_TOKEN_FORMAT = "@%s@";


    /**
     * Private constructor.
     */
    private ArtifactInfoUtils() {
        throw new UnsupportedOperationException("Utility class, not instanciable");
    }


    /**
     * Returns the project name (or artifactId) from the Maven project
     * 
     * @param project the Maven project
     * @return the name, or the artifactId if name is <code>null</code>
     */
    static String extractProjectName(final MavenProject project) {
        if (project.getModel().getName() != null) {
            return project.getModel().getName();
        } else {
            return project.getArtifactId();
        }
    }


    /**
     * Return the project description from the Maven project.
     * 
     * @param project the Maven project
     * @return the project description or an empty string
     */
    static String extractProjectDescription(final MavenProject project) {
        if (project.getDescription() != null) {
            return project.getDescription();
        } else {
            return "";
        }
    }


    /**
     * Replace a property by its value in the specified buffer
     * 
     * @param buffer the buffer to be replaced
     * @param propertyName the name of the property
     * @param propertyValue the property value
     * @return the buffer replaced with values
     */
    static String replacePropertyWithValue(final String buffer,  final String propertyName, final String propertyValue) {
        if (buffer == null) {
            throw new IllegalArgumentException("input buffer could not be null");
        } else if (propertyName == null) {
            throw new IllegalArgumentException("input property name could not be null");
        } else if (propertyValue == null) {
            throw new IllegalArgumentException("input property value could not be null");
        } else {
            String propertyToken = String.format(PROPERTY_TOKEN_FORMAT, propertyName);
            return buffer.replace(propertyToken, propertyValue);
        }
    }


    /**
     * Replace all property values from the template
     * 
     * @param template the template to use
     * @param props the map of properties to be replaced
     * @return the template replaced with values
     */
    static String applyTemplate(final String template, final Map<String, String> props) {
        String data = template;
        Set<Entry<String, String>> entries = props.entrySet();
        for (Entry<String, String> entry : entries) {
            data = replacePropertyWithValue(data, entry.getKey(), entry.getValue());
        }
        return data;
    }


    /**
     * Translate a Java package name into a file path
     * 
     * @param packageName the Java package name
     * @return the translated package name into file path
     */
    static String translatePackageNameToFilePath(final String packageName) {
        if (packageName == null) {
            throw new IllegalArgumentException("input package name could not be null");
        } else {
            return packageName.replace('.', File.separatorChar);
        }
    }


    /**
     * Returns the current machine hostname, or "unknown" is an error occured
     * 
     * @return the hostname
     */
    static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }


    /**
     * Returns the current user name
     * 
     * @return the user name
     */
    static String getUserName() {
        return System.getProperty("user.name");
    }


    /**
     * Returns the current UTC time formatted as "YYY-MM-DD HH:mm:ss z"
     * 
     * @return the formatted date
     */
    static String getCurrentDateTimeInUTC() {
        TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
        Date now = Calendar.getInstance(utcTimeZone).getTime();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss z");
        formatter.setTimeZone(utcTimeZone);
        return formatter.format(now);
    }


    /**
     * Creates the specified dir (recursive) if needed
     * 
     * @param dir the directory to create
     * @throws MojoExecutionException if the directories could not be created.
     */
    static void createOutputStructure(final File dir) throws MojoExecutionException {
        if (!dir.exists() && !dir.mkdirs()) {
            throw new MojoExecutionException("could not create output directory " + dir.getAbsolutePath());
        }
    }

}
