package org.cellprofiler.imageset;

import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TestFileNameMetadataExtractor {
	@Test
	public void testFileURL() {
		final String filename = "foo.jpg";
		File path = new File(new File(System.getProperty("user.home")), filename);
		ImageFile imageFile = new ImageFile(path.toURI());
		FileNameMetadataExtractor extractor = new FileNameMetadataExtractor(new MetadataExtractor<String>() {

			public Map<String, String> extract(String source) {
				assertEquals(source, filename);
				return emptyMap;
			}
			public List<String> getMetadataKeys() {
				fail();
				return null;
			}
		});
		extractor.extract(imageFile);
	}
	@Test
	public void testHTTPURL() {
		ImageFile imageFile;
		try {
			imageFile = new ImageFile(new URI("http://cellprofiler.org/linked_files/foo.jpg"));
			FileNameMetadataExtractor extractor = new FileNameMetadataExtractor(new MetadataExtractor<String>() {
	
				public Map<String, String> extract(String source) {
					assertEquals(source, "foo.jpg");
					return emptyMap;
				}

				public List<String> getMetadataKeys() {
					fail();
					return null;
				}
			});
			extractor.extract(imageFile);
		} catch (URISyntaxException e) {
			fail();
		}
	}
}
