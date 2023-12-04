package org.example;

import java.io.IOException;

public interface Task {
    long solve(String filePath) throws IOException;
}
