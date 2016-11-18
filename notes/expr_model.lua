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

-- 'value' means known constant or literal
M.node_kind = enum { 'runtime', 'value' }

pp(M.node_kind)

----------------
return M
