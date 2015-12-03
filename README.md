# Leo (**Le**arning **O**ntologies System)

**Leo** is an acronym for **Le**arning **O**ntologies System, a tool aiming at automatically generating well-founded ontologies described in OntoUML, through Natural Language Processing Techniques. The system uses multiple natural language processing tools, such as:
 * StanforNLP;
 * OpenNLP;
 * LTH Semantic Mapper;
 * WordNet::SenseRelate;
 * WordNet.

This project is a part of a research conducted by Felipe Leão, a M.Sc. researcher at the graduation program in Informatics (PPGI) at UNIRIO (Universidade Federal do Estado do Rio de Janeiro). For more information, please go to [fleao.com.br](http://www.fleao.com.br/researches/).

## Disclaimer
This project is not intended for general use yet! ONly a few of the initial steps have been developed. 

## Usage

#### Preprocesing Mode
-p -i /Pesquisa/srl/conference_domain.txt -o /Pesquisa/srl -s /Pesquisa/srl/lth_srl -v 


#### Execute LTH_SRL (Command Line) em /Pesquisa/srl/lth_srl
sh scripts/run.sh < ../srlInputConll2008.tokens > ../srlOutput.output


#### Learning Mode
-i /Pesquisa/srl/conference_domain.txt -o /Pesquisa/ -l /Pesquisa/srl/srlOutput.output -v

