
File generatedJavaFile = new File( basedir, "target/artifact-info/com/mycompany/ArtifactInfo.java" );
assert generatedJavaFile.exists()
assert generatedJavaFile.isFile()

File generatedClassFile = new File( basedir, "target/classes/com/mycompany/ArtifactInfo.class" );
assert generatedClassFile.exists()
assert generatedClassFile.isFile()
