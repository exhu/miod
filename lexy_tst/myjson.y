%{


%}

%token STRING

%%
json: object 

/*| array*/

object: '{' pairs '}'

pairs: pair |
    pair ',' pairs


pair: key ':' value

key: STRING
value: STRING

%%


