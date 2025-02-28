package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalExpenses.ANNIVERSARY;
import static seedu.address.testutil.TypicalExpenses.HALLOWEEN;
import static seedu.address.testutil.TypicalExpenses.INVESTMENT;
import static seedu.address.testutil.TypicalExpenses.getTypicalMooLah;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.MooLah;
import seedu.address.model.ReadOnlyMooLah;

public class JsonMooLahStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonMooLahStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readMooLah_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readMooLah(null));
    }

    private java.util.Optional<ReadOnlyMooLah> readMooLah(String filePath) throws Exception {
        return new JsonMooLahStorage(Paths.get(filePath)).readMooLah(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readMooLah("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataConversionException.class, () -> readMooLah("notJsonFormatMooLah.json"));
    }

    @Test
    public void readMooLah_invalidExpenseMooLah_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readMooLah("invalidExpenseMooLah.json"));
    }

    @Test
    public void readMooLah_invalidAndValidExpenseMooLah_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readMooLah("invalidAndValidExpenseMooLah.json"));
    }


    @Test
    public void readAndSaveMooLah_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempMooLah.json");
        MooLah original = getTypicalMooLah();
        JsonMooLahStorage jsonMooLahStorage = new JsonMooLahStorage(filePath);

        // Save in new file and read back
        jsonMooLahStorage.saveMooLah(original, filePath);
        ReadOnlyMooLah readBack = jsonMooLahStorage.readMooLah(filePath).get();
        assertEquals(original, new MooLah(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addExpense(HALLOWEEN);
        original.removeExpense(ANNIVERSARY);
        jsonMooLahStorage.saveMooLah(original, filePath);
        readBack = jsonMooLahStorage.readMooLah(filePath).get();
        assertEquals(original, new MooLah(readBack));

        // Save and read without specifying file path
        original.addExpense(INVESTMENT);
        jsonMooLahStorage.saveMooLah(original); // file path not specified
        readBack = jsonMooLahStorage.readMooLah().get(); // file path not specified
        assertEquals(original, new MooLah(readBack));

    }


    @Test
    public void saveBookMooLah_nullMooLah_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveMooLah(null, "SomeFile.json"));
    }

    /**
     * Saves {@code mooLah} at the specified {@code filePath}.
     */
    private void saveMooLah(ReadOnlyMooLah mooLah, String filePath) {
        try {
            new JsonMooLahStorage(Paths.get(filePath))
                    .saveMooLah(mooLah, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveMooLah_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveMooLah(new MooLah(), null));
    }
}
