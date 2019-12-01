/**
 *
 */
package org.bbe.maven.plugins.artifactinfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;


/**
 *
 *
 * @author Benoît BERTHONNEAU
 * @since 11 févr. 2017
 */
public class ArtifactInfoUtilsTestCase {


    /**
     * Test method for {@link ArtifactInfoUtils} private constructor.
     *
     * @throws Throwable
     */
    @Test
    public void testPrivateConstructor() throws Throwable {

        Constructor<?>[] constructors = ArtifactInfoUtils.class.getDeclaredConstructors();
        assertEquals(1, constructors.length);

        Constructor<?> constructor = constructors[0];
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));

        // Just for better code coverage...
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
        } catch (InvocationTargetException e) {
            assertEquals(UnsupportedOperationException.class, e.getTargetException().getClass());
        }

        System.out.println();
    }


    /**
     * Test method for {@link ArtifactInfoUtils#replacePropertyWithValue(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testReplacePropertyWithValue() {

        String result = ArtifactInfoUtils.replacePropertyWithValue("Hello @name@, how are you?", "name", "Ken");
        assertEquals("Hello Ken, how are you?", result);
    }


    /**
     * Test method for {@link ArtifactInfoUtils#replacePropertyWithValue(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testReplacePropertyWithValueWithMultiplePropertyInstances() {

        String result = ArtifactInfoUtils.replacePropertyWithValue("Hello @name@, how are you @name@?", "name", "Ken");
        assertEquals("Hello Ken, how are you Ken?", result);
    }


    /**
     * Test method for {@link ArtifactInfoUtils#replacePropertyWithValue(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testReplacePropertyWithValueWithNullBuffer() {
        assertThrows(IllegalArgumentException.class, () -> {
            ArtifactInfoUtils.replacePropertyWithValue(null, "name", "Ken");
        });
    }


    /**
     * Test method for {@link ArtifactInfoUtils#replacePropertyWithValue(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testReplacePropertyWithValueWithNullPropertyName() {
        assertThrows(IllegalArgumentException.class, () -> {
            ArtifactInfoUtils.replacePropertyWithValue("Dummy", null, "Ken");
        });
    }


    /**
     * Test method for {@link ArtifactInfoUtils#replacePropertyWithValue(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testReplacePropertyWithValueWithNullPropertyValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            ArtifactInfoUtils.replacePropertyWithValue("Dummy", "name", null);
        });
    }


    /**
     * Test method for {@link ArtifactInfoUtils#translatePackageNameToFilePath(java.lang.String)}.
     */
    @Test
    public void testTranslatePackageNameToFilePath() {

        String actual = ArtifactInfoUtils.translatePackageNameToFilePath("org.bbe.test");
        String expected = "org" + File.separator + "bbe" + File.separator + "test";
        assertEquals(expected, actual);
    }


    /**
     * Test method for {@link ArtifactInfoUtils#translatePackageNameToFilePath(java.lang.String)}.
     */
    @Test
    public void testTranslatePackageNameToFilePathWithNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            ArtifactInfoUtils.translatePackageNameToFilePath(null);
        });
    }


    /**
     * Test method for {@link ArtifactInfoUtils#getUserName()}.
     */
    @Test
    public void testGetUserName() {

        String result = ArtifactInfoUtils.getUserName();
        assertNotNull(result);
    }


    /**
     * Test method for {@link ArtifactInfoUtils#getHostName()}.
     */
    @Test
    public void testGetHostName() {

        String result = ArtifactInfoUtils.getHostName();
        assertNotNull(result);
    }


    /**
     * Test method for {@link ArtifactInfoUtils#getCurrentDateTimeInUTC()}.
     */
    @Test
    public void testGetCurrentDateTimeInGMT() {

        String result = ArtifactInfoUtils.getCurrentDateTimeInUTC();
        assertNotNull(result);
        assertNotEquals("", result);
    }


    /**
     * Test method for {@link ArtifactInfoUtils#createOutputStructure(java.io.File)}.
     *
     * @throws MojoExecutionException
     */
    @Test
    public void testCreateOutputStructure() throws MojoExecutionException {

        File dir = new File("target", "temp/dummy/test");
        ArtifactInfoUtils.createOutputStructure(dir);
        assertTrue(dir.exists());
        dir.delete();
    }


    /**
     * Test method for {@link ArtifactInfoUtils#createOutputStructure(java.io.File)}.
     *
     * @throws MojoExecutionException
     */
    @Test
    public void testCreateOutputStructureWithExistingDir() throws MojoExecutionException {

        File dir = new File("target", "temp/dummy/test");
        assertTrue(dir.mkdirs());
        ArtifactInfoUtils.createOutputStructure(dir);
        assertTrue(dir.exists());
        dir.delete();
    }


    /**
     * Test method for {@link ArtifactInfoUtils#extractProjectName(org.apache.maven.project.MavenProject)}.
     */
    @Test
    public void testExtractProjectName() {

        MavenProject project = new MavenProject();
        project.setGroupId("com.mycompany");
        project.setArtifactId("my-app");
        project.setVersion("1.0");

        assertEquals("my-app", ArtifactInfoUtils.extractProjectName(project));

        project.setName("My App");
        assertEquals("My App", ArtifactInfoUtils.extractProjectName(project));

        project.setName("");
        assertEquals("", ArtifactInfoUtils.extractProjectName(project));

        project.setName(null);
        assertEquals("my-app", ArtifactInfoUtils.extractProjectName(project));
    }


    /**
     * Test method for {@link ArtifactInfoUtils#extractProjectDescription(org.apache.maven.project.MavenProject)}.
     */
    @Test
    public void testExtractProjectDescription() {

        MavenProject project = new MavenProject();

        assertEquals("", ArtifactInfoUtils.extractProjectDescription(project));

        project.setDescription("This is a dummy description");;
        assertEquals("This is a dummy description", ArtifactInfoUtils.extractProjectDescription(project));

        project.setDescription("");
        assertEquals("", ArtifactInfoUtils.extractProjectDescription(project));

        project.setDescription(null);
        assertEquals("", ArtifactInfoUtils.extractProjectDescription(project));
    }

}
