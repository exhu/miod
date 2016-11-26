--[[
Modelling compile-time expression evaluation.
--]]

local pp = require 'pp'

local M = {}

-- utils --

local function enum(t)
    local newt = {}
    for i = 1, #t do
        newt[t[i]] = i
    end
    return newt
end

-- === ---

-- constant folding phase, compile-time evaluation of expressions
-- 'value' means known constant or literal
-- 'unknown' means undefined variable for 1st pass or error for 2nd pass
M.node_value_kind = enum { 'unknown', 'runtime', 'value', 'generic' }

pp(M.node_kind)

function M.create_type_struct(type_name) 
    -- value_ops = table of 'equals', 'greater', 'plus' etc. functions
    return { name = type_name, value_ops = {} }
end

function M.create_node_result(kind, v_type)
    assert(M.node_value_kind[kind])
    return { value_kind = kind, value_type = v_type }
end

--[[
function M.create_ast_node(name)
    return { name = name, result = M.create_node_result('unknown', nil) }
end
--]]

local sample_ast = { {'plus', nodes = { {value_kind = 'value', value = 3 },
    {value_kind = 'value', value = 4}} } }

local numeric_ops = enum { 'neg', 'bnot', 'mul', 'div', 'mod', 'plus', 'minus',
    'bor', 'band', 'xor', 'shl', 'shr', 'gt', 'lt', 'ge', 'le', 'eq', 'ne' } 

local function is_string(a,b)
    return a.value_type == b.value_type and a.value_type == 'string'
end

local numeric_types = enum { 'cardinal', 'int32', 'uint32', 'int64', 'float', 'double' }

local function is_numeric(a,b)
    return numeric_types[a.value_type] and numeric_types[b.value_type]
end

local function calc_values(op, a,b)
    if a.value_type == 'unknown' or b.value_type == 'unknown' then
        return M.create_node_result('unknown', nil)
    end

    if is_string(a,b) then
        if op == 'eq' or op == 'ne' or op == 'plus' then
            if a.value_kind == 'value' and b.value_kind == 'value' then
                if op == 'plus' then
                    -- concatenate strings
                    return M.create_node_result('value', 'string')
                else
                    return "unsupported operation"
                end
            end
            return M.create_node_result('runtime', 'string')
        end
    else
        if is_numeric(a,b) then
            if numeric_ops[op] == nil then
                return "unsupported operation"
            end
            if is_comparable(a,b) then
                local new_type = promote(a,b)
                local res = M.create_node_result('value', new_type)
                -- do operation
                res.value = "new value"
                return res
            else
                return "incompatible numeric types"
            end
        else
            if is_bool(a,b) then
                -- todo validate op
            else
                return "unsupported operation for the types given"
            end
        end
    end

    return M.create_node_result('unknown', nil)
end

----------------
return M
