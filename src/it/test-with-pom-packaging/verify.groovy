
File generatedJavaFile = new File( basedir, "target/artifact-info/org/bbe/maven/plugins/it/ArtifactInfo.java" );
assert !generatedJavaFile.exists()

File generatedClassFile = new File( basedir, "target/classes/org/bbe/maven/plugins/it/ArtifactInfo.class" );
assert !generatedClassFile.exists()
