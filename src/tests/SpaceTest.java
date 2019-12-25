import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import system.*;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class SpaceTest {

    Leaf[] blocks;
    LinkedList<Integer> freeBlocks;
    static FileSystem fs;
    static String[][] files;
    static int diskSize = 100;


    @Before
    public void initializingBeforeAlloc() throws BadFileNameException, OutOfSpaceException {
        fs = new FileSystem(diskSize);    //Disk space initialized to 100 blocks.
        int n = diskSize/2;
        files = new String[n][];
        for (int i = 0; i < n; i++){
            String[] filePath = new String[2];
            filePath[0] = "root";
            filePath[1] = "file"+i;
            files[i] = filePath;
        }
        for (int i = 0; i < n; i++) {
            fs.file(files[i],2);
        }
    }

    @Test
    public void alloc() throws OutOfSpaceException {
        int spaceAllocated = 0;
        String[][] disk = fs.disk();
        for(int i=0; i < disk.length; i++){
            if(disk[i] != null){
                spaceAllocated++;
            }
        }
        assertEquals("Disk allocated space should be 100.", diskSize, spaceAllocated);
    }

    @After
    public void cleanFilesAfterAllocTest(){
        int n = diskSize/2;
        for (int i = 0; i < n; i=i+2){
            fs.rmfile(files[i]);
            n--;
        }
    }

    @Test
    public void dealloc() {
    }

    @Test
    public void countFreeSpace() {
    }

    @Test
    public void getAlloc() {
    }
}