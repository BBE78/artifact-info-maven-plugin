
File generatedJavaFile = new File( basedir, "target/artifact-info/com/mycompany/Dummy.java" );
assert generatedJavaFile.exists()
assert generatedJavaFile.isFile()

File generatedClassFile = new File( basedir, "target/classes/com/mycompany/Dummy.class" );
assert generatedClassFile.exists()
assert generatedClassFile.isFile()
