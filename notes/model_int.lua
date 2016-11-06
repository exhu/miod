local pp = require 'pp'
local M = {}


function M.new_int_type(bits, sign)
    return {bits = bits, sign = sign}
end

M.intTypes = {
    int8 = M.new_int_type(8, true),
    int16 = M.new_int_type(16, true),
    int32 = M.new_int_type(32, true),
    int64 = M.new_int_type(64, true),
    uint8 = M.new_int_type(8, false),
    uint16 = M.new_int_type(16, false),
    cardinal = M.new_int_type(31, false),
    uint32 = M.new_int_type(32, false),
    uint64 = M.new_int_type(64, false),
}

function M.is_comparable(a, b)
    if a.sign == b.sign then return true end
    if a == M.intTypes.cardinal or b == M.intTypes.cardinal then
        return true
    end

    return false
end

pp(M.intTypes)

assert(M.is_comparable(M.intTypes.int8, M.intTypes.uint32) == false)
assert(M.is_comparable(M.intTypes.int8, M.intTypes.cardinal))
assert(M.is_comparable(M.intTypes.int8, M.intTypes.int32))
assert(M.is_comparable(M.intTypes.int32, M.intTypes.cardinal))

