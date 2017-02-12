
File generatedJavaFile = new File( basedir, "target/artifact-info/org/test/another/ArtifactInfo.java" );
assert generatedJavaFile.exists()
assert generatedJavaFile.isFile()

File generatedClassFile = new File( basedir, "target/classes/org/test/another/ArtifactInfo.class" );
assert generatedClassFile.exists()
assert generatedClassFile.isFile()
