package bunos.study.practiceapplication.service;

import bunos.study.practiceapplication.domain.model.Log;
import bunos.study.practiceapplication.domain.model.User;

import java.util.Date;
import java.util.List;

public interface LogService {
    Log getById(long id);
    Log getByTime(Date time);
    List<Log> getAllLogs();
    void log(String info, User user);
}
