/**
 *
 */
package io.github.bbe78.maven.plugins.artifactinfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;


/**
 * Maven mojo that generates the source including project information
 *
 * @author Benoît BERTHONNEAU
 * @since 8 févr. 2017
 */
@Mojo( name = "generate",
       defaultPhase = LifecyclePhase.GENERATE_SOURCES,
       requiresProject = true )
public class ArtifactInfoMojo extends AbstractMojo {

    /** The parent Maven project. */
    @Parameter( defaultValue = "${project}", required = true, readonly = true )
    private MavenProject project;

    /** Parent output directory. */
    @Parameter( defaultValue = "${project.build.directory}/artifact-info", required = true )
    private File outputDirectory;

    /** The java package name of the generated class. */
    @Parameter( defaultValue = "${project.groupId}", required = true )
    private String packageName;

    /** The java class name of the generated class. */
    @Parameter( defaultValue = "ArtifactInfo", required = true )
    private String className;


    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        if (!("jar".equals(project.getPackaging()) || "war".equals(project.getPackaging()))) {
            getLog().info("skipping artifact-info generation (project packaging is not jar|war)");
            return;
        }

        Map<String, String> props = new HashMap<>();
        props.put("artifact-info.packageName", packageName);
        props.put("artifact-info.className", className);
        props.put("artifact-info.groupId", project.getGroupId());
        props.put("artifact-info.artifactId", project.getArtifactId());
        props.put("artifact-info.version", project.getVersion());
        props.put("artifact-info.name", ArtifactInfoUtils.extractProjectName(project));
        props.put("artifact-info.description", ArtifactInfoUtils.extractProjectDescription(project));
        props.put("artifact-info.builtBy", ArtifactInfoUtils.getUserName());
        props.put("artifact-info.buildDate", ArtifactInfoUtils.getCurrentDateTimeInUTC());
        props.put("artifact-info.buildHost", ArtifactInfoUtils.getHostName());

        String template = getTemplateContent();
        String classContent = ArtifactInfoUtils.applyTemplate(template, props);

        String dirPath = ArtifactInfoUtils.translatePackageNameToFilePath(packageName);
        File parentDir = new File(outputDirectory, dirPath);
        ArtifactInfoUtils.createOutputStructure(parentDir);

        File classFile = new File(parentDir, className + ".java");
        writeFile(classFile, classContent);

        project.addCompileSourceRoot(outputDirectory.getAbsolutePath());
        if (getLog().isDebugEnabled()) {
            getLog().debug("output directory added to project sources");
        }

        getLog().info("artifact info successfully generated: " + classFile.getAbsolutePath());
    }


    /**
     * Load from the classpath the content of the source template.
     * 
     * @return the template content
     * @throws MojoExecutionException if the template could not be found
     */
    private String getTemplateContent() throws MojoExecutionException {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("ArtifactInfo.tpl");
        try {
            return IOUtil.toString(is);
        } catch (IOException e) {
            throw new MojoExecutionException("Error reading template file", e);
        }
    }


    /**
     * Writes the class file on the file system
     * 
     * @param classFile the output file
     * @param content the file content
     * @throws MojoExecutionException if the file could not be written
     */
    private void writeFile(final File classFile, final String content) throws MojoExecutionException {
        try {
            FileUtils.fileWrite(classFile, content);
        }
        catch (IOException e)  {
            throw new MojoExecutionException("Error creating file " + classFile.getAbsolutePath(), e);
        }
    }

}
