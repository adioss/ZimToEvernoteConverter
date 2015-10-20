# ZimToEvernoteConverter
A basic converter of Zim content to Evernote enex files

## Synopsis

Parse the zim data folder and extract it to compatible enex files:

* for each file  
    * extract title (=====)
    * create enex files 
    * replace "*" and "-" at the begin of a line by a <li>
    * replace source part by a table

## Code Example

* mvn clean package
* java -cp zimevernote-1.0-SNAPSHOT-jar-with-dependencies.jar com.adioss.zimevernote.ZimToEvernoteConverter -inputFolder "c:\\input\\" -outputFolder "d:\\output\\"

## Motivation

## Installation

    git clone https://github.com/adioss/ZimToEvernoteConverter.git

To edit code, import project based on maven using root pom.

## Tests

Describe and show how to run the tests with code examples.

## Contributors

## License

http://www.apache.org/licenses/LICENSE-2.0