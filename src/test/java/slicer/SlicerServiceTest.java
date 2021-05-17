package slicer;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import help.TestHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URL;

public class SlicerServiceTest {

    SlicerService slicerService;

    @Test
    public void testSlice() throws IOException{
        URL url = this.getClass().getResource("/slice.xml");
        File file = new File(url.getFile());
        XmlMapper xmlMapper = new XmlMapper();
        String xml = TestHelper.inputStreamToString(new FileInputStream(file));

        Slice slice = xmlMapper.readValue(xml, Slice.class);
        Assertions.assertThat(slice.getName().equals("SliceName"));

        slicerService = new SlicerService();
        slicerService.serveSlice(slice);

    }
}
