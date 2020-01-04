[![Build Status](https://travis-ci.com/phillima/asniffer.svg?branch=master)](https://travis-ci.com/phillima/asniffer)

Annotation Sniffer
==================

Annotation Sniffer is a tool that extracts code annotation metrics from java source code. 

### How to download

Download the source code and generate an executable jar file. Or download the jar file provided with the latest release.

```
mvn clean install
```
### How to use

```
java -jar asniffer.jar <path to project> <path to xml report> <single/multi>
```

The "path to project" is mandatory, and should be the path to the java project to be analyzed (i.e, contains the source code files). Considering that only one java project is being analyzed, the directory should have the arrangement below.

    .
    ├── project                # Directory containing the source file for the project. This is the path provided

In this case the ASniffer will consider that every ```.java``` file inside the directory ```project``` belongs to the same project.

The ASniffer can also analyze multiple projects at once. In this case, the user should provide a directory with the arrangement described below.

    .
    ├── projects                # Root directory for projects. This is the path to be provided
        ├── project1            # Contains the source files for project1
        ├── project2            # Contains the source files for project2
        └── ...         

In this case the directory ```projects``` is a root folder, and the sub-directories ```project1```, ```project2``` and so forth, are each different java projects. They can be completely different projects. The user should manually arrange their projects directories to fit the arrangement described above, in order to use this ASniffer feature.

The second parameter, "path to xml", is optional. It tells the ASniffer where to store the XML report file. If no path is provided, the ASniffer will place the report in the "path to project". This parameter is a path to a directoy and should not include any ```.xml``` in its name. The XML file will be generated by the ASniffer, with the projects name being the name of the XML file, i.e., ```projectsName.xml```. The ASniffer assumes that the name of the root directory is the name of the project. In the case several projects are being analyzed, the ASniffer considers that each sub-directory (inside the provided root directory) is the name of a separate project, and each project will have its own XML report placed in the "path to xml" (if provided, or in the "path to project" otherwise).

The third parameter (single/multi) informs the ASniffer if the "path to project" contains only one project (i.e, every ```.java``` file belongs to only one project) or several projects (i.e, the root directory contains several sub-directories, with each being a separate project).


Annotation Metrics
==================

The Annotations Sniffer was developed to aid research in code annotations analysis. It collects 9 annotation metrics. These metrics were proposed and defined in the the paper [A Metrics Suite for Code Annoation Assessment](https://www.sciencedirect.com/science/article/pii/S016412121730273X)

### Collected metrics

* AC: Annotations in Class
* UAC: Unique Annotations in Class
* ASC: Annotation Schema in Class
* AED: Annotation in Element Declaration
* AA: Attributes in Annotation
* ANL: Annotation Nesting Level
* LOCAD: LOC in Annotation Declaration
* NEC: Number of Elements in Class
* NAEC: Number of Annotated Elements in Class

### XML Output Format

* Class Metrics: These metrics have one value per class, they are AC, UAC, ASC, NAEC and NEC
* Code Element Metrics: These metrics have one value per code element (method, field, enum, type). Our suite has one metric, AED (Annotations in Element Declaration), that measures the number of annotations declared in any given code element.
* Annotation Metrics: These metrics have one value values per annotation declared in the class. They evaluate the annotation itself (AA, LOCAD, ANL). 

* For each code element, the report contains the element name, type (field, method, enum, etc), the source code line where the element is located and "code element metric values" (for now, only AED fits this category)
* If the AED is greater than zero, then the code element contains annotations, and so the "annotation metrics" values printed on the XML. The report has the annotation name,source-code line and the values for AA, ANL and LOCAD.
                   
* In case of multiple projects, one XML file is generated for each one of them

* Following is an example of an XML report

```
<project name="project1">
    <package name="pacakge1">
        <class name="pacakge1.Class1" type="class">
            <schema>java.lang</schema>
            <schema>javax.persistence</schema>
            ...
            <metric name="LOC" value="50"/>
            <metric name="ASC" value="5"/>
            <metric name="AC" value="28"/>
            <metric name="NAEC" value="16"/>
            <metric name="UAC" value="18"/>
            <metric name="NEC" value="32"/>
            <code-elements>
                <code-element name="method1" type="method" code-line="20" aed="1"/>
                    <annotation name="Override" code-line="201" schema="java.lang">
                        <annotation-metrics>
                            <item metric="AA" value="0"/>
                            <item metric="LOCAD" value="1"/>
                            <item metric="ANL" value="0"/>
                        </annotation-metrics>
                    </annotation>
                </code-element>
            ...
            </codelements>
        ...
        </class>
    ...
    </package>
...
</project>  

```

### Creating a new Metric for Annotation Sniffer

The Annotation Sniffer uses Reflection to know which metrics it should collect. If you wish to use Annotation Sniffer on your project and create you owrn custom metrics, follow these steps:

* Class Metrics: If you wish to create your own Metric Class, your class must:
    - Extend ASTVisitor (to visit the compilation unit)
    - Implement the ```IClassMetricCollector```interface. It contains two methods, ```execute(CompilationUnit, MetricResult, AMReport)``` and ```setResult(MetricResult)```. The ```MetricResult``` class is where you want to store your value, as well as the name of your custom metric. Check the code for AC, ASC and UAC for examplee
    - Annotate the class with ```@ClassMetric```.


* If you wish to create new Annotation Metrics, then you need to:
    - Annotate the class with @AnnotationMetric.
    - Implement the interface ```IAnnotationMetricCollector```. This interface has only one method, ```execute(CompilationUnit, AnnotationMetricModel, Annotation)```. The ```AnnotionMetricModel``` class is where you will store the metric value and name. The ```Annotation``` class is the JDT (Java Development Tools) representation of the annotation that you can perform your analysis. Check the code for: ANL, AA and LOCAD for more examples.

* Check the metrics included in the package br.inpe.cap.asniffer.metric for more information.
