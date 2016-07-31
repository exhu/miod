-- parsed result model in lua.

local M = {}

local function make_enum(t)
    local newt = {}
    for i = 1, #t do
        newt[t[i]] = i
    end
    return newt
end

-- builtin types are added to global symbol table before parsing the unit
M.builin_symbol_table = {
    parent = nil,
    ['int8'] = {typeid = 'INT8', symbol_loc = nil},
}

symbol_loc = { unit_name = 'pkg::subpkg::unit', line = 3, col = 4 }

unit_symbol_table = { parent = M.builtin_symbol_table,
    ['mypkg::myunit'] = {typeid = 'UNIT_DEF', symbol_loc = {unit_name = '' }
}


M.value_type_id = make_enum {
    'UNIT_DEF', -- symbolname, location, type = {'UNIT_DEF'}
    'ALIAS', -- 'ALIAS', symbol
    'INT8', -- 'INT8'
    'INT16',
    'INT32',
    'INT64',
    'CARDINAL',
    'FLOAT',
    'DOUBLE',
    'UINT8',
    'UINT16',
    'UINT32',
    'UINT64',
    'NCHAR_LITERAL',
    'NCHAR',
    'STRING_LITERAL',
    'NSTRING',
    'STRING',
    'ARRAY_SIZED',
    'ARRAY_REF',
    'ENUM_DEF',
    'ENUM_VALUE',
    'STRUCT_DEF',
    'STRUCT_REF',
    'PROC_DEF',
    'PROC_REF',
    'CLASS_DEF',
    'CLASS_REF',
    'CLASS_WEAK',
    'CLASS_WEAK_REF',
    'METHOD_DEF',
    'METHOD_WITH_INSTANCE_REF',
    'VAR_REF',
    'OPAQUE',
}

M.primitive_type = {
    'INT8',
    'INT16',
    'INT32',
    'INT64',
    'CARDINAL',
    'FLOAT',
    'DOUBLE',
    'UINT8',
    'UINT16',
    'UINT32',
    'UINT64',
    'NCHAR',
    'NSTRING',
}

M.type_kind = make_enum {'SYMBOL', 'PRIMITIVE'}

function M.new_type_desc(type_id, )
    return { name = name, location = location, type_desc = type_desc }
end

function M.new_symbol(name, location, type_desc)
    return { name = name, location = location, type_desc = type_desc }
end

return M

