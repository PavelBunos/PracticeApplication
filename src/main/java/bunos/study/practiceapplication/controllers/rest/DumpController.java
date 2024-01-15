package bunos.study.practiceapplication.controllers.rest;

import bunos.study.practiceapplication.services.DumpService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/dump")
public class DumpController {

    private final DumpService dumpService;

    @GetMapping("/create")
    public void dump() {
        try {
            dumpService.createDump();
        } catch (Exception e) {
            System.out.println("Dump process failure: " + e.getMessage());
        }
    }

    @GetMapping("/restore/to/{dumpName}")
    public void restoreFromDump(@PathVariable String dumpName) {
        try {
            dumpService.restore(dumpName);
        } catch (Exception e) {
            System.out.println("Restore from dump process failure: " + e);
        }
    }

}
