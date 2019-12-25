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

        fs.dir(path1);
        assertNotNull(fs.DirExists(path1));

        try{
            fs.dir(badPath);
        }
        catch (Exception e){
            assertEquals(BadFileNameException.class, e.getClass());
        }

        fs.rmdir(path1);
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
        }
        assertEquals("Disk allocated space should be 9.", 9, spaceAllocated);
    }

    @Test
    public void file() {

    }

    @Test
    public void lsdir() {
    }

    @Test
    public void rmfile() {
    }

    @Test
    public void rmdir() {
    }

    @Test
    public void fileExists() {
    }

    @Test
    public void dirExists() {
    }
}