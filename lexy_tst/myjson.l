%option stack
%{
    typedef enum tagToken
    {
        STRING = 258

    } Token;

    char scanString[1024];
    int scanStringLen;
%}

%x c_string
%%
\" {yy_push_state(c_string); scanStringLen = 0; scanString[0]=0;printf("start string\n");}
<c_string>[^"]+ {strcat(scanString, yytext); scanStringLen+=strlen(yytext);printf("added char\n");}
<c_string>\" {yy_pop_state(); scanString[scanStringLen]=0;printf("str = %s\n", scanString);return STRING;}

%%


