package slicer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SlicerController {

    @Autowired
    SlicerService slicerService;

    @PostMapping("/slice")
    public void createNewSlice(@RequestBody Slice slice){

    }
}
