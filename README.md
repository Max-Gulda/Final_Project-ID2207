# Final_Project-ID2207

This is a repository that describes the final project of ID2207 Modern Methods of Software Development

## Content of the project 
Included are all the code files in the directory `SEP/src/`. Aswell as The `README.txt` in the `Final_Project-ID2207` folder.

## Compile and run 
The code is run using the openJDK21 JVM in the jetbrains intellij IDE. Standard operation of the program is done by running 
the `main.java` file in the `Final_Project-ID2207/SEP/src`. Test can be done by running the `AllTestsRunner` in the `Final_Project-ID2207\SEP\src\com\sep\system\tests` folder 

the possible accounts are listed below

| User                    | Username | Password |
|-------------------------|----------|----------|
| Customer Service        | cs       | cs       |
| Senior Customer Service | scso     | scso     |
| Financial Manager       | fm       | fm       |
| Administration Manager  | am       | am       |
| Production Manager      | pm       | pm       |
| Service Manager         | sm       | sm       |
| HR Team                 | hr       | hr       |
| Audio                   | audio    | audio    |


# Running the Program

## macOS

To compile and run this Java program on macOS, follow these steps in your Terminal:

## 1. Navigate to the Project Directory

Open Terminal and change to the root directory of your project:
```bash
cd /path/to/your/project
```

## 2. Compile the Java Files

Compile all Java files, specifying the bin directory for the compiled classes. This command includes both the main.java file in src (which contains the main method) and all other files in subdirectories:
```bash
javac -d bin src/main.java src/**/*.java
```

## 3. Run the Program

Run the compiled program by specifying the classpath (-cp) and the main class (in this case, Main):
```bash
java -cp bin Main
```


## Windows

To compile and run this Java program on Windows, follow these steps in Command Prompt:

### 1. Navigate to the Project Directory

Open Command Prompt and change to the root directory of your project:
```cmd
cd C:\path\to\your\project
```

### 2. Compile the Java Files

Compile all Java files, specifying the bin directory for the compiled classes. This command includes both the main.java file in src (which contains the main method) and all other files in subdirectories:
```cmd
javac -d bin src\main.java src\**\*.java
```

### 3. Run the Program

Run the compiled program by specifying the classpath (-cp) and the main class (in this case, Main):
```cmd
java -cp bin Main
```

Note: On Windows, use backslashes (`\`) for file paths instead of forward slashes (`/`). If you're using PowerShell instead of Command Prompt, forward slashes will work as well.
