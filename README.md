# Point Inside Triangle

Since you won't stop asking if your point is inside that dang triangle.

![pointinsidetriangle](https://user-images.githubusercontent.com/37934912/54874066-3631cd00-4dba-11e9-88a6-75e5fc53c357.png)

This is a simple Java Swing application to calculate and display whether a point lies inside, on, or outside a triangle. As it currently stands, the program has the following noteworthy features:

- Supports device and logical coordinate systems
- Supports canvas-clearing for multiple uses
- Supports tracking of current point coordinates
- Can identify **which** edge of the triangle a point lies on
- Can identify if the first 3 points do not form a triangle
- Considers a point in a narrow range around the triangle edges as being on an edge (as it would be an essentially impossible outcome otherwise)
  - Range is based on the area of the triangle, negating any effects of window resizing on its calculation

## Getting started

### **Download and Usage from JAR**

Executable JAR files for all versions are available under the `_releases` directory. To always get the most recent version, click [here](<https://github.com/jacob-ressler/point-inside-triangle/raw/master/_releases/PointInsideTriangle(v1.0).jar>).

To launch the application, simply double-click the file. The following command can also be used to launch the application from a terminal:

```bash
java -jar "PointInsideTriangle(v1.0).jar"
```

_**Note** that the version number in the command should be identical to version number of the JAR file you have downloaded for it to work properly._

### **Building from Source**

After downloading the `src` folder, use the following command to generate the class files:

```bash
javac Tools.java TripointCanvas.java TripointFrame.java
```

To launch the application, use the command:

```bash
java TripointFrame
```

### **How to Use**

All you have to do after launching the application is select 4 points on the canvas. The first 3 points will form a triangle and the last point will be marked with a red X. The program will then analyze the triangle and X to see if the X lies inside, on, or outside the triangle.

## Project Information

- Developed by Jacob Ressler
- CIS 457 Computer Graphics
- Cleveland State University
- Spring 2019
