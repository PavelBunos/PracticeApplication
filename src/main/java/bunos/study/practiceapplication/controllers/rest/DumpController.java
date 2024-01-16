package bunos.study.practiceapplication.controllers.rest;

import bunos.study.practiceapplication.services.DumpService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
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
            log.error("Dump process failure: ", e);
        }
    }

    @GetMapping("/restore/to/{dumpName}")
    public void restoreFromDump(@PathVariable String dumpName) {
        try {
            dumpService.restore(dumpName, "");
        } catch (Exception e) {
            log.error("Restore from dump process failure", e);
        }
    }

}
