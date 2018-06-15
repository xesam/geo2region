package io.github.xesam.geo2district;

import io.github.xesam.geo.Point;
import io.github.xesam.geo2district.data.BoundarySource;
import io.github.xesam.geo2district.data.FileBoundarySource;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.Optional;

/**
 * @author xesamguo@gmail.com
 */
public class DistrictTreeTest {
    static DistrictTree districtTree;
    static BoundarySource boundarySource;

    @BeforeClass
    public static void beforeClass() {
        File skeletonFile = new File("d:/data.center/district/unified/skeleton.json");
        districtTree = DistrictTree.from(skeletonFile);
        boundarySource = new FileBoundarySource(new File("d:/data.center/district/unified"));
    }

    @Test
    public void getTreeByNameWuhan() {
        Optional<DistrictTree> sub = districtTree.getTreeByName("湖北省", "武汉市");

        Assert.assertTrue(sub.isPresent());
        DistrictTree skeleton = sub.get();
        District wuhan = skeleton.getDistrict();

        Assert.assertEquals("420100", wuhan.getAdcode());
        Assert.assertEquals("武汉市", wuhan.getName());
        Assert.assertEquals(114.305469, wuhan.getCenter().getLng(), 0.00001);
        Assert.assertEquals(30.593175, wuhan.getCenter().getLat(), 0.00001);
    }

    @Test
    public void getTreeByNameWuhanHongshan() {
        Optional<DistrictTree> sub = districtTree.getTreeByName("湖北省", "武汉市", "洪山区");

        Assert.assertTrue(sub.isPresent());
        DistrictTree skeleton = sub.get();
        District hongshan = skeleton.getDistrict();

        Assert.assertEquals("洪山区", hongshan.getName());
    }

    @Test
    public void getTreeByNameNotFound() {
        Optional<DistrictTree> sub = districtTree.getTreeByName("湖北省", "北京市");

        Assert.assertFalse(sub.isPresent());
    }

    @Test
    public void getTreeByPointInChina() {
        districtTree.inflateBoundaryWithDepth(boundarySource, 2);
        Optional<DistrictTree> wuhanSkeletonOptional = districtTree.getTreeByPoint(new Point(114.305469, 30.593175));

        Assert.assertTrue(wuhanSkeletonOptional.isPresent());

        DistrictTree skeleton = wuhanSkeletonOptional.get();
        District wuhan = skeleton.getDistrict();

        Assert.assertEquals("武汉市", wuhan.getName());
    }

    @Test
    public void getTreeByPointInWuhan() {

        Optional<DistrictTree> subOptional = districtTree.getTreeByName("湖北省", "武汉市");
        DistrictTree sub = subOptional.get();
        sub.inflateBoundaryWithDepth(boundarySource, 0);
        Optional<DistrictTree> wuhanSkeletonOptional = sub.getTreeByPoint(new Point(114.305469, 30.593175));

        Assert.assertTrue(wuhanSkeletonOptional.isPresent());

        DistrictTree skeleton = wuhanSkeletonOptional.get();
        District wuhan = skeleton.getDistrict();

        Assert.assertEquals("武汉市", wuhan.getName());
    }
}