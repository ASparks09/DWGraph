import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestJson extends DWGraph {
    /**File 1 ***********************
    @Test
    public void TestJsonSize(){
        DWGraph result = new DWGraph("src\\16-node-sample.json");
        assertEquals(16, result.size());
    }
    @Test
    public void TestJsonNodes(){
        DWGraph result = new DWGraph("src\\16-node-sample.json");
        System.out.println(result.nodes());
        assertEquals("[Ray, Cray, Noita, Athens, Beck, Cory, Cinnamon," +
                " Paxton, Skya, Lanni, Odelle, Ophelia, Felix, Benoit, Constance, Rowena]", result.nodes().toString());
    }
    @Test
    public void TestJsonEdges(){
        DWGraph result = new DWGraph("C:\\Users\\littl\\Downloads\\DWGraph\\src\\16-node-sample.json");
        System.out.println(result.edges("Ray"));
        assertEquals("[Cray, Noita, Athens]", result.edges("Ray").toString());
    }

    @Test
    public void TestJsonDensity(){
        DWGraph result = new DWGraph("src\\16-node-sample.json");
        System.out.println(result.density("Ray"));
        assertEquals(0.125, result.density("Ray"));
    }
    /**File 2 **********************
    @Test
    public void TestJson2Size(){
        DWGraph result = new DWGraph("src\\16-node-disjoint.json");
        System.out.println(result.toString());
        assertEquals(16, result.size());
    }
    @Test
    public void TestJson2Nodes(){
        DWGraph result = new DWGraph("src\\16-node-disjoint.json");
        System.out.println(result.nodes());
        assertEquals("[Opie, Toby, Sheldon, Sazerac, Jade, Sylas, " +
                "Mitzi, Koach, Dr.SheldonCooper, Shtalvi, Kaiser, Butkus, Caspian, Abby, Angel, Bogart]", result.nodes().toString());
    }
    @Test
    public void TestJson2Edges(){
        DWGraph result = new DWGraph("src\\16-node-disjoint.json");
        assertEquals("[Kaiser, Butkus, Caspian, Abby, Angel]", result.edges("Bogart").toString());
    }
    @Test
    public void TestJson2Density(){
        DWGraph result = new DWGraph("src\\16-node-disjoint.json");
        assertEquals(0.31, result.density());
    }

    /**Edge cases **********************
    @Test
    public void TestJsonEmpty(){
        DWGraph result = new DWGraph("C:\\Users\\littl\\Downloads\\DWGraph\\src\\empty.json");
        System.out.println(result.toString());
        assertEquals(0, result.size());
    }
    @Test
    public void TestJsonNull(){
        DWGraph result = new DWGraph(null);
        System.out.println(result.toString());
        assertEquals(0, result.size());
    }
**/
    @Test
    public void TestBellman(){
        DWGraph result = new DWGraph("src\\16-node-sample.json");

        System.out.print(result.search("Ray", "Rowena"));
        System.out.println("\n" + Arrays.toString(result.parsePath(result.search("Ray", "Rowena"))));
       // result.search("Ray", "Rowena", result);
      //  System.out.println(result.search("Ray", "Rowena", result));

    }
}
