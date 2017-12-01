## Grouper
grouper is an application that read a TAB delimited file (like csv) 
and group lines based on columns specification

## Design Consideration
The architecture of grouper is: read -> sign -> sort -> squash.

So the diagram is:

 GrouperMain -> Grouper 

## Implementation
 1. code in Java 8 and maven
 2. TODO: input value validation

## build step: (on MacOS)

```
In terminal:

$git clone git@github.com:yaohuang2005/grouper.git
$cd grouper
$mvn clean package

This command will generate grouper-1.0-SNAPSHOT.jar under the target directory

## test
$cd target
$cp ../src/main/resources/bin/grouper.sh .
$cp ../src/test/resources/input_file .

## test 1 by redirection console to a file from directory
$./grouper.sh y m d < input_file
2016	3	28	100
2016	3	29	123
2016	3	30	50
2016	4	1	50
2016	3	28	100
2016	3	29	123
2016	3	30	50
2016	3		273
2016	4	1	50
2016	4		50
2016			323
			323
Finished reading


Grouped by: y m d
 2016	3		273
 2016	3	28	200
 2016	3	29	246
 2016	3	30	100
 2016	4		50
 2016	4	1	100
 2016			323
			323

## test 1 by input from console (please input header row first)
WTL-EN-EEG8WL:target yhuang$ ./grouper.sh y m d
y m d value
2016	3       20	50
2016	3	20	50
2016	3	20	70
2016	3	20	70
2017	11	3 	10
2017	11	3	10
2017	11      15	20
2017	11	15	20
Finished reading


Grouped by: y m d
 2016	3	20	120
 2017	11	15	20
 2017	11	3	10