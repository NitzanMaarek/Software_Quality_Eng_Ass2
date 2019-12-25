import org.junit.Before;
import org.junit.Test;
import system.Tree;

import java.util.HashMap;

import static org.junit.Assert.*;

public class TreeTest {

    private Tree tree_test = new Tree("Testing Tree");
    @Test
    public void getChildByName_ChildNotExistTest(){
        String name = "child1";
        Tree result_tree = tree_test.GetChildByName(name);
        String[] result_path = result_tree.getPath();
        String result_path_string = result_path[0];
        assertEquals("Path name should be child1.", "child1", result_path_string);
    }


    @Test
    public void getChildByNameTypeTest() {
        String name = "child1";
        Tree result_tree = tree_test.GetChildByName(name);
        assertEquals("Type should be Tree", "class system.Tree", result_tree.getClass().toString());
    }

}