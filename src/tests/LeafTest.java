import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import system.FileSystem;
import system.Leaf;

import static org.junit.Assert.*;

public class LeafTest {

    FileSystem fs;
    Integer fsSize = 10;

    @Before
    public void setUp() throws Exception {
        this.fs = new FileSystem(fsSize);

        String[] path1 = {"root", "dir1", "dir2"};
        this.fs.dir(path1);

        String[] filePath = {"root", "dir1", "dir2", "image"};
        this.fs.file(filePath, 5);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getPath() throws Exception {
        String[] filePath = {"root", "dir1", "dir2", "image"};
        Leaf[] blocks = fs.fileStorage.getAlloc();
        Leaf leaf = blocks[0];
        String[] path = leaf.getPath();

        for (int i = 0; i < path.length; i++) {
            assertEquals("Paths should be equal.", path[i], filePath[i]);
        }
    }
}