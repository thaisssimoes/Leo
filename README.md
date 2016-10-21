# Leo (**Le**arning **O**ntologies System)

**Leo** is an acronym for **Le**arning **O**ntologies System, a tool aiming at automatically generating well-founded ontologies described in OntoUML, through Natural Language Processing Techniques. The system uses multiple natural language processing tools, such as:
 * StanforNLP;
 * OpenNLP;
 * LTH Semantic Mapper;
 * WordNet::SenseRelate;
 * WordNet.

This project is a part of a research conducted by Felipe Leão, a M.Sc. researcher at the graduation program in Informatics (PPGI) at UNIRIO (Universidade Federal do Estado do Rio de Janeiro). For more information, please go to [fleao.com.br](http://www.fleao.com.br/researches/).

## Disclaimer
This project is not intended for general use yet! Only a few of the initial steps have been developed.

## Editing and Building

The project uses maven as its dependencies manager. If you are planning on editing the code, make sure to run Eclipse`s "Maven > Update Project" before using or compiling it. For building a single executable JAR run maven either through Eclipse IDE or by command line using the command below.

```bash
# Building
$ mvn clean package
```

Maven will generate a `target/` folder containing the executable Jar with all dependencies packaged.

## Running the application

At the current moment the application does not chain all steps by itself, since it is necessary to run external systems, such as LTH Semantic Role Labelling system. Nonetheless, it provides ways of generating files that can be inputed to other systems and is able of reading the output of such systems.

#### Usage information
To display all required and optional parameters when running the application through command line.
```bash
# Use '-h' option
$ java -jar Leo-0.0.1-SNAPSHOT.jar -h

This HELP further explains parameters that should be passed to the application.
Flags indicated with "()" are required while flags indicated with "[]" are optional.

  [-p|--preprocess]
        Runs the application in Preprocess Mode, which use parameters to
        generate a SRL Input File (CoNLL 2008 format).

  (-i|--input_sentences) <input_sentences>
        The path to the input TXT file with domain description.

  (-o|--output_path) <output_file_path>
        The path LTH_SRL root where models and descriptors folders are.

  [(-l|--labeled_sentences) <labeled_sentences>]
        The path to the semantic role labeled sentences (CoNLL 2008 format).
        OBS: Should only be passed when the application is executed on Default
        Mode (whithout "-p").

  [-v|--verbose]
        Outputs verbose info.

  [-h|--help]
        Shows this help info.

```

#### Example of Preprocesing Mode
Preproccess the input domain description, generating a file that respects the CoNLL 2008 format and can be used as input to LTH Semantic Role Labelling System.
```bash
# the following command runs preprocessing mode, using the
# conference_domain.txt sentences as input and generates
# the output file in /Pesquisa/srl/ folder.
$ java -jar Leo-0.0.1-SNAPSHOT.jar -p -i /Pesquisa/srl/conference_domain.txt -o /Pesquisa/srl -v
```


#### Execute LTH_SRL (Command Line)

After preprocessing, the LTH Semantic Role Labelling system must be executed by itself. Go to LTH SRL path and run the script using the file outputted by LEO as its input.
```bash
# Move to LTH SRL installation folder and execute the script
$ sh scripts/run.sh < ../srlInputConll2008.tokens > ../srlOutput.output
```

#### Learning Mode
Uses the output of the semantic labeler as input to the Word Sense Disambiguation system to determine the corresponding Wordnet Synsets and outputs their supersenses.

```bash
$ java -jar Leo-0.0.1-SNAPSHOT.jar -i /Pesquisa/srl/conference_domain.txt -o /Pesquisa/ -l /Pesquisa/srl/srlOutput.output -v
```

OBS.: Currently the system does not outputs the Semantic Type Mappings.
