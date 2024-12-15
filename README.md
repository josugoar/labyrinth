# labyrinth

<p align="center">
  <img src="assets/labyrinth.png" alt="labyrinth" width="640" height="320" />
</p>

<p align="center">
  <img src="https://img.shields.io/badge/author-josugoar-blue" alt="labyrinth" />
  <img src="https://img.shields.io/badge/license-MIT-green" alt="labyrinth" />
</p>

<h1></h1>

Java (Swing, AWT) interactive and responsive Graphical User Interface (GUI) maze editor, generator and solver desktop application, implementing custom pathfinder and generator self-made generic algorithmic listener structure.

<p align="center">
  <img alt="labyrinth" src="assets/labyrinth.gif" width="300" height="350" />
</p>

## Installation

1. Install [Red Hat Open JDK](https://developers.redhat.com/products/openjdk/download?sc_cid=701f2000000RWTnAAO)

2. Dowload **labyrinth.jar**

## Usage

Run **labyrinth.jar** from terminal execute it from the file explorer

```sh
$ java -jar labyrinth.jar
```

## Building

```sh
$ javac -d bin/ $(find -name "*.java")
$ jar --create --file labyrinth.jar --manifest META-INF/MANIFEST.mf -C bin/ .
```
