package bugbusters;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Changelog {
    static final Logger logger = LogManager.getLogger("mainLogger");

    public static void logChange(String action) {
        logger.info(action);
    }
}