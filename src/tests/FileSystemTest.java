import org.junit.Before;
import org.junit.Test;
import system.BadFileNameException;
import system.FileSystem;
import system.OutOfSpaceException;

import java.nio.file.DirectoryNotEmptyException;

import static org.junit.Assert.*;

public class FileSystemTest {
    FileSystem fs;
    @Before
    public void init(){
        fs = new FileSystem(10);
    }

    @Test
    public void dir() throws BadFileNameException, DirectoryNotEmptyException {
        String[] badPath = {"not root", "dir1"};
        String[] path1 = {"root", "dir1", "dir2"};
        String[] path2 = {"root", "dir1"};
//        String[] path2 = {"root", "dir1"};

        fs.dir(path1);
        assertNotNull(fs.DirExists(path1));

        try{
            fs.dir(badPath);
        }
        catch (Exception e){
            assertEquals(BadFileNameException.class, e.getClass());
        }

        boolean dirPathFlag = false;
        try{
            fs.dir(path2);
        }
        catch(Exception e){
            System.out.println(e.toString());
            dirPathFlag = true;
        }
        finally{
            assertFalse("Bug found-FileExists should return Node instead of Tree", dirPathFlag);
        }

    }

    @Test
    public void disk() throws BadFileNameException, OutOfSpaceException {
        int spaceAllocated = 0;
        String[] file1 = {"root", "file1"};
        String[] file2 = {"root", "file2"};
        fs.file(file1, 7);
        fs.file(file2, 2);
        String[][] allocatedDisk = fs.disk();
        for(int i=0; i < allocatedDisk.length; i++){
            if(allocatedDisk[i] != null){
                spaceAllocated ++;
            }
            if(i < 7){
                assertEquals(file1[0], allocatedDisk[i][0]);
                assertEquals(file1[1], allocatedDisk[i][1]);
            }
            if(i >= 7 && i < 9){
                assertEquals(file2[0], allocatedDisk[i][0]);
                assertEquals(file2[1], allocatedDisk[i][1]);
            }
        }
        assertEquals("Disk allocated space should be 9.", 9, spaceAllocated);
    }

    @Test
    public void file() throws BadFileNameException, OutOfSpaceException{
        String[] file1 = {"root", "dir1", "dir2", "file1"};
        String[] file2 = {"root", "dir1", "dir2", "file2"};
        String[] file3 = {"root", "dir2", "file1"};

        fs.file(file1,6);
        fs.file(file2, 2);
        fs.file(file3, 2);

        //Modify file size
        fs.file(file1, 5);

        try{
            String[] badNamePath = {"imbad", "imbad", "youknowit"};
            fs.file(badNamePath, 1);
        }
        catch(Exception e){
            assertEquals(BadFileNameException.class, e.getClass());
        }
        try{
            String[] exceedingFile = {"root", "imhuuge"};
            fs.file(exceedingFile, 3);
        }
        catch(Exception e){
            assertEquals(OutOfSpaceException.class, e.getClass());
        }
        try{
            String[] badName = {"root", "dir1"};
            fs.file(badName, 1);
        }
        catch(Exception e){
            assertEquals(BadFileNameException.class, e.getClass());
        }
        try{
            fs.file(file1, 8);
        }
        catch(Exception e){
            assertEquals("Bug found-Should throw OutOfSpaceException instead of NullPointer.", OutOfSpaceException.class, e.getClass());
        }
    }

    @Test
    public void lsdir() throws BadFileNameException, OutOfSpaceException{
        String[] path = {"root", "dir1", "dir2", "file1"};
        String[] dirPath1 = {"root", "dir1"};
        String[] dirPath2 = {"root", "dir1", "dir2"};
        String[] expected = {"dir2"};
        fs.file(path, 1);
        String[] result = fs.lsdir(dirPath1);
        assertArrayEquals(expected, result);

        expected[0] = "file1";
        assertArrayEquals(expected, fs.lsdir(dirPath2));

        String[] pathDoesntExist = {"root", "DoesntExist"};
        assertNull(fs.lsdir(pathDoesntExist));
    }

    @Test
    public void rmfile() throws BadFileNameException, OutOfSpaceException{
        String[] path = {"root", "dir1", "dir2", "file1"};
        fs.file(path,1);
        fs.rmfile(path);
        assertNull(fs.FileExists(path));

        String[] pathDoesntExist = {"root", "doesntexist"};
        fs.rmfile(pathDoesntExist);
        assertNull(fs.FileExists(pathDoesntExist));

    }

    @Test
    public void rmdir() throws BadFileNameException, OutOfSpaceException, DirectoryNotEmptyException{
        String[] dirPath = {"root", "dir1"};
        String[] file = {"root", "dir2", "file1"};
        String[] notEmptyPath = {"root", "dir2"};

        fs.dir(dirPath);
        fs.file(file, 1);

        fs.rmdir(dirPath);

        assertNull(fs.DirExists(dirPath));

        boolean notEmptyDirFlag = false;
        try{
            fs.rmdir(notEmptyPath);
        }
        catch(DirectoryNotEmptyException e){
            notEmptyDirFlag = true;
        }
        finally{
            assertTrue("Removing dir should fail-Not empty dir.", notEmptyDirFlag);
        }

        String[] pathDoesntExist = {"root", "doesntexist"};
        fs.rmdir(pathDoesntExist);
        assertNull(fs.FileExists(pathDoesntExist));
    }

    @Test
    public void fileExists() throws BadFileNameException, OutOfSpaceException {
        String[] file = {"root", "dir2", "file1"};
        String[] dirPath = {"root", "dir2"};
        fs.file(file, 1);

        assertNotNull(fs.FileExists(file));
        boolean dirPathFileExistsFlag = false;
        try{
            fs.FileExists(dirPath);
        }
        catch(Exception e){
            dirPathFileExistsFlag = true;
        }
        finally{
            assertFalse("Bug found-FileExists should return Node instead of Tree", dirPathFileExistsFlag);
        }
    }

    @Test
    public void dirExists() throws BadFileNameException{
        String[] pathExists = {"root", "dir1", "dir2"};
        String[] partialPath = {"root", "dir1"};
        String[] pathDoesntExist = {"root", "path", "doesnt", "exist"};
        fs.dir(pathExists);
        assertNotNull(fs.DirExists(pathExists));
        assertNotNull(fs.DirExists(partialPath));
        assertNull(fs.DirExists(pathDoesntExist));
    }
}