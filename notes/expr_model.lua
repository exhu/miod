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
    {value_kind = 'value', value = 4}} }


----------------
return M
