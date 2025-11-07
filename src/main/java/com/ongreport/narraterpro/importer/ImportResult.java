package com.ongreport.narraterpro.importer;

import java.util.ArrayList;
import java.util.List;

public class ImportResult {
    private int successCount = 0;
    private final List<String> errors = new ArrayList<>();

    public void addSuccess() {
        this.successCount++;
    }

    public void addError(String error) {
        this.errors.add(error);
    }

    public int getSuccessCount() {
        return successCount;
    }

    public List<String> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
