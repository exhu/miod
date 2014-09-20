Implementation details
======================

Scope stack
-----------

Scope type:

    - module
    - proc
    - block (if, block etc.), static_if does not create a scope
    - struct
    - enum
    - class

Dotted name resolution:
    
    1) split by dot
    2) descend from top scope by name


