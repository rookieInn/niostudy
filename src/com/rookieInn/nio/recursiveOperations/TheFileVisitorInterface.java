package com.rookieInn.nio.recursiveOperations;

/*
The FileVisitor Interface
the FileVisitor interface provides the support for recursively traversing a file tree.
The methods of this interface represent key points in the traversal process,
enabling you to take control when a file is visited,
before a directory is accessed, after a directory is accessed, and when a failure occurs.
Once you have control (at any of these key points), you can choose how to process the visited file and decide what should happen to it next by indicating a visit result through the FileVisitResult enum, Which contains four enum constants:
    a. FileVisitResult.CONTINUE: This visit result indicates that the traversal process should continue.
                                 It can be translated into different actions depending on which FileVisitor method is returned.
                                 For example, the traversal process may continue by visiting the next file, visiting a directory's entries, or skipping a failure.
    b. FileVisitResult.SKIP_SIBLINGS: This visit result indicates that the traversal process should continue without visiting the siblings of this file or directory.
    c. FileVisitResult.SKIP_SUBTREE: This visit result indicates that the traversal process should continue without visiting the rest of the entries in this directory.
    d. FileVisitResult.TERMINATE: This visit result indicates that the traversal process should terminate.

The constants of this enum type can be iterated as follows:
    for (FileVisitResult constant : FileVisitResult.values())
        System.out.println(constant);
The following subsections discuss how you can control the traversal process by implementing the various FileVisitor methods.
 */

/*
FileVisitor.visitFile() Method
The visitFile() method is invoked for a file in a directory.
Usually, this method returns a CONTINUE result or a TERMINATE result.
For example, when searching for a file, this method should return CONTINUE until the file is found(or the tree is completely traversed) and TERMINATE after the file is found.

When this method is invoked, it receives a reference to the file and the file's basic attributes.
If an I/O error occurs, then it throws an IOException exception.
The following is the signature of the method:
    FileVisitResult visitFile(T file, BasicAttributes attr) throws IOException
*/

/*
FileVisitor.preVisitDirectory() Method
The preVisitDirectory() method is invoked for a directory before visiting its entries.
The entries will be visited if the method returns CONTINUE and will not be visited if it returns SKIP_SUBTREE
(The later visit result is meaningful only when it is returned from this method).
Also, you can skip visiting the sibling of this file or directory (and any descendants) by returning the SKIP_SIBLINGS result.

When this method is invoked, it gets a reference to the directory and the directory's basic attributes.
If an I/O error occurs, then it throws an IOException exception.
The following is the signature of the method:
    FileVisitResult preVisitDirectory(T dir, BasicFileAttributes attrs) throws IOException
 */

/*
  FileVisitor.postVisitDirectory() Method
The postVisitDirectory() method is invoked after all entries in the directory (and any descendants)
have been visited or the visit has ended suddenly (that is, an I/O error has occurred or the visit has
programmatically aborted). When this method is invoked, it gets a reference to the directory and
IOException object—it will be null if no error occurred during the visit or it will return the corresponding
error if one occurred. If an I/O error occurs, then it throws an IOException exception. The following is
the signature of this method
    FileVisitResult postVisitDirectory(T dir, IOException exc) throws IOException
*/

/*
FileVisitor.visitFileFailed() Method
The visitFileFailed() method is invoked when the file cannot be accessed for any of several different
reasons, such as the file’s attributes cannot be read or a directory cannot be opened. When this method
is invoked, it gets a reference to the file and the exception that occurred while trying to visit that file. If an
I/O error occurs, then it throws an IOException exception. The following is the signature of this method:
    FileVisitResult visitFileFailed(T file, IOException exc) throws IOException
*/
public class TheFileVisitorInterface {


}
