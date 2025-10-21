package comp3350.teachreach.tests.utils;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

import comp3350.teachreach.application.TRData;

public
class TestUtils
{
    private static final File DB_SRC = new File("src/main/assets/db/TR.script");

    public static
    File copyDB() throws IOException
    {
        final File target = File.createTempFile("temp-db", ".script");
        Files.copy(DB_SRC, target);
        TRData.setDBPathName(target.getAbsolutePath().replace(".script", ""));
        return target;
    }
}
