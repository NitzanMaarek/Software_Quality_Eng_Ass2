import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import system.FileSystem;
import system.Leaf;
import system.OutOfSpaceException;

import static org.junit.Assert.*;

public class SpaceTest {

    FileSystem fs;
    Integer fsSize = 10;

    @Before
    public void setUp() throws Exception {
        this.fs = new FileSystem(fsSize);

        String[] path1 = {"root", "dir1", "dir2"};
        this.fs.dir(path1);

        String[] path2 = {"root", "dir3", "dir4"};
        this.fs.dir(path2);

        String[] firstFilePath = {"root", "dir3", "dir4", "video"};
        this.fs.file(firstFilePath, 2);

        String[] secondFilePath = {"root", "dir1", "dir2", "image"};
        this.fs.file(secondFilePath, 5);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void firstAllocTest() {
        Leaf[] blocks = fs.fileStorage.getAlloc();
        String[][] disk = fs.disk();
        // Check allocations for the 2 files that has been created
        assertEquals("Allocations sizes should be equal.",disk.length, blocks.length);
    }

    @Test
    public void secondAllocTest() throws OutOfSpaceException {
        String[] filePath = {"root", "dir1", "dir2", "sound"};
        try {
            fs.file(filePath, 5);
        } catch (Exception e) {
            assertEquals("OutOfSpaceException should be thrown.", OutOfSpaceException.class, e.getClass());
        }

    }

    @Test
    public void thirdAllocTest() {
        String[] filePath = {"root", "dir1", "dir2", "image"};
        fs.rmfile(filePath);
        String[] newFile= {"root", "dir1", "dir2", "exe"};
        try {
            fs.file(newFile, 8);
            assertEquals("FreeSpace should be 0.", fs.fileStorage.countFreeSpace(), 0);
        } catch (Exception e) {
        }
    }

    @Test
    public void dealloc() {
        String[] filePath = {"root", "dir1", "dir2", "image"};
        fs.rmfile(filePath);
        try {
            assertEquals("FreeSpace should be 8.", fs.fileStorage.countFreeSpace(), 8);
        } catch (Exception e) {
        }
    }

    @Test
    public void countFreeSpace() {
        assertEquals("FreeSpace should be 8.", fs.fileStorage.countFreeSpace(), 3);
    }

    @Test
    public void secondCountFreeSpace() {
        String[] filePath = {"root", "dir1", "dir2", "kitty_picture"};
        try {
            fs.file(filePath, 1);
        } catch (Exception e) {
        }
        assertEquals("FreeSpace should be 8.", fs.fileStorage.countFreeSpace(), 2);
    }

    @Test
    public void getAlloc() {
        Leaf[] blocks = fs.fileStorage.getAlloc();
        // First 7 blocks should be full, other should be null
        for (int i = 0; i < blocks.length; i++) {
            if (i < 7){
                assertNotNull(blocks[i]);
            } else {
                assertNull("Shouldn't be null.", blocks[i]);
            }

        }
    }
}