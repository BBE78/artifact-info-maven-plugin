/**
 * 
 */
package org.bbe.maven.plugins.artifactinfo;

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
    
    private static final String PROPERTY_TOKEN_FORMAT = "@%s@";
    
    
    /**
     * Private constructor.
     */
    private ArtifactInfoUtils() {
        
        throw new UnsupportedOperationException("Utility class, not instanciable");
    }
    
    
    static String extractProjectName(final MavenProject project) {
        
        if (project.getModel().getName() != null) {
            return project.getModel().getName();
        } else {
            return project.getArtifactId();
        }
    }
    
    
    static String extractProjectDescription(final MavenProject project) {
        
        if (project.getDescription() != null) {
            return project.getDescription();
        } else {
            return "";
        }
    }
    
    
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
    
    
    static String applyTemplate(final String template, final Map<String, String> props) {
        
        String data = template;
        
        Set<Entry<String, String>> entries = props.entrySet();
        for (Entry<String, String> entry : entries) {
            data = replacePropertyWithValue(data, entry.getKey(), entry.getValue());
        }
        
        return data;
    }
    
    
    static String translatePackageNameToFilePath(final String packageName) {
        
        if (packageName == null) {
            throw new IllegalArgumentException("input package name could not be null");
        } else {
            return packageName.replace('.', File.separatorChar);
        }
    }
    
    
    static String getHostName() {
        
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }
    
    
    static String getUserName() {
        
        return System.getProperty("user.name");
    }
    
    
    static String getCurrentDateTimeInUTC() {
        
        TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
        
        DateFormat formatter = new SimpleDateFormat("YYY-MM-DD HH:mm:ss z");
        formatter.setTimeZone(utcTimeZone);
        
        Date now = Calendar.getInstance(utcTimeZone).getTime();
        
        return formatter.format(now);
    }
    
    
    static void createOutputStructure(final File dir) throws MojoExecutionException {
        
        if (!dir.exists() && !dir.mkdirs()) {
            throw new MojoExecutionException("could not create output directory " + dir.getAbsolutePath());
        }
    }
    
}
