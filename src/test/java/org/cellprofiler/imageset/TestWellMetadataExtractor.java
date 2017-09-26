package org.cellprofiler.imageset;

import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.*;

public class TestWellMetadataExtractor {
	private ImagePlaneDetails makeIPD(String [][] mapKv) {
		final ImagePlaneDetails ipd = Mocks.makeMockIPD();
		for (String []kv: mapKv) ipd.put(kv[0], kv[1]);
		return ipd;
	}
	private void testEmptyMapCase(String [][] mapKv) {
		assertEquals(0, new WellMetadataExtractor().extract(makeIPD(mapKv)).size());
	}

	private void testWellCase(String [][] mapKv, String expected) {
		Map<String, String> metadata = new WellMetadataExtractor().extract(makeIPD(mapKv));
		assertEquals(1, metadata.size());
		assertEquals(expected, metadata.get("Well"));
	}
	@Test
	public void testExtract() {
		testEmptyMapCase(new String[0][]);
		testEmptyMapCase(new String[][] {{"row", "A"}});
		testEmptyMapCase(new String[][] {{"col", "10"}});
		testEmptyMapCase(new String[][] {{"Well", "A01"},{"row", "A"}, {"col", "01"}});
		for (String row: new String [] { "wellrow", "well_row", "row" }) {
			for (String col: new String [] { "wellcol", "well_col", "wellcolumn", "well_column",
                    "column", "col" }) {
				testWellCase(new String[][] {{row, "A"}, { col, "01"}}, "A01");
			}
		}
		testWellCase(new String [][] {{ "rOW", "A" }, {"cOl", "01"}}, "A01");
		testWellCase(new String [][] {{ "row", "02"}, {"col", "04"}}, "B04");
		testWellCase(new String [][] {{ "row", "00"}, {"col", "01"}}, "0001");
		testWellCase(new String [][] {{ "row", "25"}, {"col", "01"}}, "2501");
	}
	@Test
	public void testMaybeYouNeedThis() {
		assertTrue(WellMetadataExtractor.maybeYouNeedThis(Arrays.asList("row", "column")));
		assertTrue(WellMetadataExtractor.maybeYouNeedThis(Arrays.asList("row", "COL")));
		assertTrue(WellMetadataExtractor.maybeYouNeedThis(Arrays.asList("Well_row", "COL")));
		assertTrue(WellMetadataExtractor.maybeYouNeedThis(Arrays.asList("Well_row", "Foo", "COL")));
		assertFalse(WellMetadataExtractor.maybeYouNeedThis(Arrays.asList("Well_row", "foo")));
	}
}
