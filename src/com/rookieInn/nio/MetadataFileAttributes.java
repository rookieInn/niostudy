package com.rookieInn.nio;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.List;
import java.util.Set;

/**
 * Metadata File Attributes
 *
 * BasicFileAttributeView: This is a view of basic attributes that must be supported by all file system implementations. The attribute view name is basic .
 * DosFileAttributeView: This view provides the standard four supported attributes on file systems that support the DOS attributes. The attribute view name is dos.
 * PosixFileAttributeView: This view extends the basic attribute view with attributes supported on file systems that support the POSIX (Portable Operating System Interface for Unix) family of standards, such as Unix.
 *                         The attribute view name is posix.
 * FileOwnerAttributeView: This view is supported by any file system implementation that supports the concept of a file owner. The attribute view name is owner.
 * AclFileAttributeView: This view supports reading or updating a file’s ACL. The NFSv4 ACL model is supported. The attribute view name is acl.
 * UserDefinedFileAttributeView: This view enables support of metadata that is user defined.
 *
 * Created by gxy on 2016/6/8.
 */
public class MetadataFileAttributes {

    @Test
    public void getSupportedFileAttributeViews() {
        FileSystem fs = FileSystems.getDefault();
        Set<String> views = fs.supportedFileAttributeViews();

        for (String view : views) {
            System.out.println(view);
        }

        for (FileStore store: fs.getFileStores()) {
            boolean supported = store.supportsFileAttributeView(BasicFileAttributeView.class);
            System.out.println(store.name() + "--" + supported);
        }

        Path path = Paths.get("E:/idea/nio/123.txt");
        try {
            FileStore store1 = Files.getFileStore(path);
            boolean supported1 = store1.supportsFileAttributeView("basic");
            System.out.println(store1.name() + "---" + supported1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//----------
//    Basic View
//------------
    /**
     * //Get Bulk Attributes with readAttributes()
     */
    @Test
    public void getBulkAttributes() {

        BasicFileAttributes attr = null;
        Path path = Paths.get("E:/idea/nio", "123.txt");
        try {
            attr = Files.readAttributes(path, BasicFileAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("File size:" + attr.size());
        System.out.println("File creation time:" + attr.creationTime());
        System.out.println("File was last accessed at:" + attr.lastAccessTime());
        System.out.println("File was last modified at:" + attr.lastModifiedTime());

        System.out.println("Is directory? " + attr.isDirectory());
        System.out.println("Is regular file? " + attr.isRegularFile());
        System.out.println("Is symbolic link? " + attr.isSymbolicLink());
        System.out.println("Is other? " + attr.isOther());
    }

    /**
     * Get a single Attribute with getAttribute()
     *
     * Basic attributes names ate listed here:
     * 1. lastModifiedTime
     * 2. lastAccessTime
     * 3. creationTime
     * 4. size
     * 5. isRegularFile
     * 6. isDirectory
     * 7. isSymbolicLink
     * 8. isOther
     * 9. fileKey
     * The generally accepted form for retrieving a single attribute is [view-name:]attribute-name.
     * The view-name is basic
     */
    @Test
    public void getSingleAttribute() {
        Path path = Paths.get("E:/idea/nio", "123.txt");
        try {
            long size = (long) Files.getAttribute(path, "basic:size", LinkOption.NOFOLLOW_LINKS);
            System.out.println("Size: " + size);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update a Basic Attribute
     * Updating any or all of the file’s last modified time,
     * last access time, and create time attributes can be accomplished with the setTimes() method,
     * which takes three arguments representing the last modified time,
     * last access time, and create time as instances of FileTime, which is a new class in Java 7 representing the value of a file’s timestamp attribute.
     * If any one of lastModifiedTime, lastAccessTime, or creationTime has the value null, then the corresponding timestamp is not changed.
     */
    @Test
    public void updateBasicAttribute() {
        Path path = Paths.get("E:/idea/nio", "123.txt");
        long time = System.currentTimeMillis();
        FileTime fileTime = FileTime.fromMillis(time);
        try {
            Files.getFileAttributeView(path, BasicFileAttributeView.class).setTimes(fileTime, fileTime, fileTime);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Updating the file's last modified time can also be accomplished with the Files.setLastModifiedTime() method.
        long time1 = System.currentTimeMillis();
        FileTime fileTime1 = FileTime.fromMillis(time1);
        try {
            Files.setLastModifiedTime(path, fileTime1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Updating the file's last modified time can also be accomplished with the setAttribute() method.
        //Actually, this method may be used to update the file's last modified time, last access time, or create time attributes as if by invoking the setTimes() method.
        try {
            Files.setAttribute(path, "basic:lastModifiedTime", fileTime1, LinkOption.NOFOLLOW_LINKS);
            Files.setAttribute(path, "basic:creationTime", fileTime1, LinkOption.NOFOLLOW_LINKS);
            Files.setAttribute(path, "basic:lastAccessTime", fileTime1, LinkOption.NOFOLLOW_LINKS);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Obviously, now you have to extract the three attributes' value to see the change.
        //You can do so by using the getAttribute() methos.
        try {
            FileTime lastModifiedTime = (FileTime)Files.getAttribute(path, "basic:lastModifiedTime", LinkOption.NOFOLLOW_LINKS);
            FileTime creationTime = (FileTime)Files.getAttribute(path, "basic:creationTime", LinkOption.NOFOLLOW_LINKS);
            FileTime lastAccessTime = (FileTime)Files.getAttribute(path, "basic:lastAccessTime", LinkOption.NOFOLLOW_LINKS);
            System.out.println("New last modified time:" + lastModifiedTime);
            System.out.println("New creation time:" + creationTime);
            System.out.println("New last access time:" + lastAccessTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//----------
//   DOS View
//------------
    /**
     * DOS View
     *
     * isReadOnly()
     * isHidden()
     * isArchive()
     * isSystem()
     */
    @Test
    public void dOSView() {
        DosFileAttributes attr = null;
        Path path = Paths.get("E:/idea/nio", "123.txt");

        try {
            attr = Files.readAttributes(path, DosFileAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Is read only ? " + attr.isReadOnly());
        System.out.println("Is Hidden ? " + attr.isHidden());
        System.out.println("Is archive ? " + attr.isArchive());
        System.out.println("Is system ? " + attr.isSystem());
    }

    /**
     * Dos attribute can be acquired with the following names:
     * hidden
     * readonly
     * system
     * archive
     * The generally accepted form is [view-name:]attribute-name.
     * The view-name is dos.
     */
    @Test
    public void setAndGetDOSAttribute() {
        Path path = Paths.get("E:/idea/nio", "123.txt");

        //setting the hidden attribute to true
        try {
            Files.setAttribute(path, "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //getting the hidden attribute
        try{
            boolean hidden = (Boolean) Files.getAttribute(path, "dos:hidden", LinkOption.NOFOLLOW_LINKS);
            System.out.println("Is hidden ? " + hidden);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//----------
//   File Owner View
//------------

    /**
     * Set a File Owner Using Files.setOwner()
     */
    @Test
    public void setFileOwner() {
        UserPrincipal owner = null;
        Path path = Paths.get("E:/idea/nio", "123.txt");
        try {
            owner = path.getFileSystem().getUserPrincipalLookupService().lookupPrincipalByName("apress");
            Files.setOwner(path, owner);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set a File Owner Using FileOwnerAttributeView.setOwner()
     */
    @Test
    public void setFileOwner1() {
        UserPrincipal owner = null;
        Path path = Paths.get("E:/idea/nio", "123.txt");
        FileOwnerAttributeView foav = Files.getFileAttributeView(path, FileOwnerAttributeView.class);
        try {
            owner = path.getFileSystem().getUserPrincipalLookupService().lookupPrincipalByName("apress");
            foav.setOwner(owner);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set a File Owner Using Files.setAttribute()
     */
    @Test
    public void setFileOwner2() {
        UserPrincipal owner = null;
        Path path = Paths.get("E:/idea/nio", "123.txt");
        try{
            owner = path.getFileSystem().getUserPrincipalLookupService().lookupPrincipalByName("apress");
            Files.setAttribute(path, "owner:owner", owner, LinkOption.NOFOLLOW_LINKS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a File Owner Using FileOwnerAttributeView.getOwner()
     */
    @Test
    public void getFileOwner() {
        Path path = Paths.get("E:/idea/nio", "123.txt");
        FileOwnerAttributeView foav =  Files.getFileAttributeView(path, FileOwnerAttributeView.class);
        try {
            String owner = foav.getOwner().getName();
            System.out.println(owner);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a File Owner Using Files.getAttribute()
     * The file owner attribute can be required with the following name:
     * owner
     * The generally accepted form is [view-name:]attribute-name.
     * The view-name is owner.
     */
    @Test
    public void getFileOwner1() {
        Path path = Paths.get("E:/idea/nio", "123.txt");
        try {
            UserPrincipal owner = (UserPrincipal) Files.getAttribute(path, "owner:owner", LinkOption.NOFOLLOW_LINKS);
            System.out.println(owner.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//----------
//    POSIX View
//------------

    /**
     *Good news for Unix fans!
     * POSIX extends the basic view with attributes supported by Unix and its flavors—file owner, group owner, and nine related access permissions (read, write, members of the same group, etc.).
     * POSIX attributes can be required with the following names:
     * group
     * permissions
     * The generally accepted form is [view-name:]attribute-name.
     * The view-name is posix.
     * Based on the PosixFileAttributes class, you can extract the POSIX attributes as follows:
     */
    @Test
    public void pOSIXView() {
        PosixFileAttributes attr = null;
        Path path = Paths.get("/root/doc/123.txt");
        try{
            attr = Files.readAttributes(path, PosixFileAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("File owner: " + attr.owner().getName());
        System.out.println("File group: " + attr.group().getName());
        System.out.println("File permissions: " + attr.permissions().toString());

        //Or you can use the "long way" by calling the Files.getFileAttributeView() method:
        try {
            attr = Files.getFileAttributeView(path, PosixFileAttributeView.class).readAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * POSIX Permissions
     * The permissions() method returns a collection of PosixFilePermissions objects.
     * PosixFilePermissions is a permissions helper class.
     * One of the most useful methods of this class is asFileAttribute(), which accepts a Set of file permissions and constructs a files attribute that can be passed to the Path.createFile() method or the Path.createDirectory() method.
     * For example, you can extract the POSIX permissions of a file and create another file with the same attributes as follows:
     */
    @Test
    public void pOSIXPermissions() {
        PosixFileAttributes attr = null;
        Path path = Paths.get("/root/doc/123.txt");
        try{
            attr = Files.readAttributes(path, PosixFileAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Path new_path = Paths.get("/root/hello/doc/123.txt");
        FileAttribute<Set<PosixFilePermission>> posixattrs = PosixFilePermissions.asFileAttribute(attr.permissions());
        try {
            Files.createFile(new_path, posixattrs);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Moreover, you can set a file's permissions as a hard-coded string by calling the formString() method:
        Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rw-r--r--");
        try{
            Files.setPosixFilePermissions(new_path, permissions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * POSIX Group Owner
     */
    @Test
    public void pOSIXGroupOwner() {
        Path path = Paths.get("/home/doc/123.txt");
        try{
            GroupPrincipal group = path.getFileSystem().getUserPrincipalLookupService().lookupPrincipalByGroupName("apressteam");
            Files.getFileAttributeView(path, PosixFileAttributeView.class).setGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //You can easily find out the group by calling the Files.getAttribute() method:
        try{
            GroupPrincipal group1 = (GroupPrincipal) Files.getAttribute(path, "posix:group", LinkOption.NOFOLLOW_LINKS);
            System.out.println(group1.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//----------
//    ACL View
//------------

    /**
     * Read an ACL Using Files.getFileAttributeView()
     * If you've never seen the content of an ACL, then try out the following code, which uses Files.getFileAttributeView() to extract the ACL as a List<AclEntry></>
     */
    @Test
    public void readAnACL() {
        List<AclEntry> acllist = null;
        Path path = Paths.get("E:/idea/nio/123.txt");
        AclFileAttributeView aclview = Files.getFileAttributeView(path, AclFileAttributeView.class);
        try{
            acllist = aclview.getAcl();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read an ACL Using Files.getAttribute()
     * ACL attributes can be required with the following names:
     * acl
     * owner
     * The generally accepted form is [view-name:]attribute-name.
     * The view-name is acl
     */
    @Test
    public void readAnAcl1() {
        List<AclEntry> acllist = null;
        Path path = Paths.get("E:/idea/nio/123.txt");
        try{
            acllist = (List<AclEntry>) Files.getAttribute(path, "acl:acl", LinkOption.NOFOLLOW_LINKS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read ACL Entries
     * AclEntry——a class that maps an entry from an ACL.
     * Each entry has four components:
     * 1. Type: Determines if the entry grants or denies access. It can be ALARM, ALLOW, AUDIT, or DENY.
     * 2. Principal: The identity to which the entry grants or denies access. This is mapped as a UserPrincipal.
     * 3. Permissions: A set of permissions. Mapped as Set<AclEntryPermission>.
     * 4. Flags: A set of flags to indicate how entries are inherited and propagated. Mapped as Set<AclEntryFlag>.
     * You can iterate over the list and extract each entry's components as follows:
     */
    @Test
    public void readACLEntries() {
        List<AclEntry> acllist = null;
        Path path = Paths.get("E:/idea/nio/123.txt");
        AclFileAttributeView aclview = Files.getFileAttributeView(path, AclFileAttributeView.class);
        try{
            acllist = aclview.getAcl();
            for (AclEntry aclentry: acllist) {
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
                System.out.println("Principal: " + aclentry.principal().getName());
                System.out.println("Type: " + aclentry.type().toString());
                System.out.println("Permissions: " + aclentry.permissions().toString());
                System.out.println("Flags: " + aclentry.flags().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Grant a New Access in an ACL
     * ACL entries are created using an associated AclEntry.Builder object by invoking its build() method.
     * For example, if you want to grant a new access to a principal, then you must follow this process:
     * 1. Look up the principal by calling the FileSystem.getUserPrincipalLookupService() method.
     * 2. Get the ACL view (as previously described).
     * 3. Create a new entry by using the AclEntry.Builder object.
     * 4. Read the ACL (as previous described).
     * 5. Insert the new entry(recommended before any DENY entry).
     * 6. Rewrite the ACL by using setAcl() or setAttribute().
     * Following these steps, you can write a code snippet for granting read data access and append data access to a principal named apress:
     */
    public void grantAccessInACL(){
        Path path = Paths.get("E:/idea/nio/123.txt");
        try {
            //Lookup for the principal
            UserPrincipal user = path.getFileSystem().getUserPrincipalLookupService().lookupPrincipalByName("apress");
            //Get the ACL view
            AclFileAttributeView view = Files.getFileAttributeView(path, AclFileAttributeView.class);
            //Create a new entry
            AclEntry entry = AclEntry.newBuilder().setType(AclEntryType.ALLOW)
                    .setPrincipal(user)
                    .setPermissions(AclEntryPermission.READ_DATA, AclEntryPermission.APPEND_DATA)
                    .build();
            //read ACL
            List<AclEntry> acl = view.getAcl();
            //Insert the new entry
            acl.add(0, entry);
            //rewrite ACL
            view.setAcl(acl);
            //or
            //Files.setAttribute(path, "acl:acl", acl,  LinkOption.NOFOLLOW_LINKS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//-------------------
//   File Store Attributes
//-------------------
    /**
     * Get Attributes of All File Stores
     */
    @Test
    public void getAttributesOfAllFileStores() {
        FileSystem fs = FileSystems.getDefault();
        for (FileStore store : fs.getFileStores()) {
            try {
                long total_space = store.getTotalSpace() / 1024;
                long used_space = (store.getTotalSpace() - store.getUnallocatedSpace()) / 1024;
                long available_space = store.getUsableSpace() / 1024;
                boolean is_read_only = store.isReadOnly();

                System.out.println("---" + store.name() + "---" + store.type());
                System.out.println("Total space: " + total_space);
                System.out.println("Used space: " + used_space);
                System.out.println("Available space: " + available_space);
                System.out.println("Is ready only?" + is_read_only);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get Attributes of the File Store in Which a File Resides
     * Files.getFileStore(path)
     */
    @Test
    public void getAttributesOfFileStoreInFileReside() {
        Path path = Paths.get("E:/idea/nio/123.txt");
        try{
            FileStore store = Files.getFileStore(path);
            long total_space = store.getTotalSpace() / 1024;
            long used_space = (store.getTotalSpace() - store.getUnallocatedSpace()) / 1024;
            long available_space = store.getUsableSpace() / 1024;
            boolean is_read_only = store.isReadOnly();

            System.out.println("---" + store.name() + "---" + store.type());
            System.out.println("Total space: " + total_space);
            System.out.println("Used space: " + used_space);
            System.out.println("Available space: " + available_space);
            System.out.println("Is ready only?" + is_read_only);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//-------------------
//   User-Defined File Attributes View
//-------------------
    @Test
    public void checkUserDefinedAttributesSupportability() {
        Path path = Paths.get("E:/idea/nio/123.txt");
        try{
            FileStore store = Files.getFileStore(path);
            if (!store.supportsFileAttributeView(UserDefinedFileAttributeView.class)) {
                System.out.println("The user defined attributes are not supported on: " + store);
            } else {
                System.out.println("The user defined attributes are supported on: " + store);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//-------------------
//   Operations on User-Defined Attributes
//-------------------

    /**
     * Define a User Attribute
     * To start, define an attribute named file.description that has the value "This file contains private information!".
     * After you get the view by calling Files.getFileAttributeView(), you can write this user-defined attribute as follows:
     */
    @Test
    public void defineAUserAttribute() {
        Path path = Paths.get("E:/idea/nio/123.txt");
        UserDefinedFileAttributeView udfav = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
        try {
            udfav.write("file.description", Charset.defaultCharset().encode("This file contains private information!"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * List User-Defined Attribute Names and Value Sizes
     * At any moment, you can see the list of user-defined attribute names and value sizes by calling the UserDefinedFileAttributeView.list() method.
     * The returned list is a collection of string that represents the attribute names.
     * Passing their names to the UserDefinedFileAttributeView.size() method will result in the sizes of attribute values.
     */
    @Test
    public void listUserDefinedAttributeNamesAndValueSizes() {
        Path path = Paths.get("E:/idea/nio/123.txt");
        UserDefinedFileAttributeView udfav = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
        try {
            for (String name : udfav.list()) {
                System.out.println(udfav.size(name) + "           " + name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the Value of a User-Defined Attribute
     */
    @Test
    public void getTheValueOfAUserDefinedAttribute() {
        Path path = Paths.get("E:/idea/nio/123.txt");
        UserDefinedFileAttributeView udfav = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
        try {
            int size = udfav.size("file.description");
            ByteBuffer bb = ByteBuffer.allocateDirect(size);
            udfav.read("file.description", bb);
            bb.flip();
            System.out.println(Charset.defaultCharset().decode(bb).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete a File's User-Defined Attribute
     */
    @Test
    public void deleteAUserDefinedAttribute() {
        Path path = Paths.get("E:/idea/nio/123.txt");
        UserDefinedFileAttributeView udfav = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
        try {
            udfav.delete("file.description");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
