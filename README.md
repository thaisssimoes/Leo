## Preprocesing Mode
-p -i /Pesquisa/srl/conference_domain.txt -o /Pesquisa/srl -s /Pesquisa/srl/lth_srl -v 


## Execute LTH_SRL (Command Line) em /Pesquisa/srl/lth_srl
sh scripts/run.sh < ../srlInputConll2008.tokens > ../srlOutput.output


##Learning Mode
-i /Pesquisa/srl/conference_domain.txt -o /Pesquisa/ -l /Pesquisa/srl/srlOutput.output -v

