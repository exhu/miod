local pp = require 'pp'
local M = {}


function M.new_int_type(bits, sign)
    return {bits = bits, sign = sign}
end

M.int_types = {
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
    if a == M.int_types.cardinal or b == M.int_types.cardinal then
        return true
    end

    return false
end

pp(M.int_types)

assert(M.is_comparable(M.int_types.int8, M.int_types.uint32) == false)
assert(M.is_comparable(M.int_types.int8, M.int_types.cardinal))
assert(M.is_comparable(M.int_types.int8, M.int_types.int32))
assert(M.is_comparable(M.int_types.int32, M.int_types.cardinal))


M.value_type_id = {'unit', 'bool', 'integer', 'float'}
M.value_flags = {'var_ref'}

function M.promote(a, b)
    if a == M.int_types.cardinal then
        if b.sign and b.bits <= 32 then 
            return M.int_types.int32
        end
        return b
    else
        if b == M.int_types.cardinal then
            if a.sign and a.bits <= 32 then 
                return M.int_types.int32
            end
            return a
        end
    end
    if a.bits >= b.bits then return a end
    return b
end

assert(M.promote(M.int_types.int8, M.int_types.cardinal) == M.int_types.int32)
assert(M.promote(M.int_types.int32, M.int_types.cardinal) == M.int_types.int32)
assert(M.promote(M.int_types.int64, M.int_types.cardinal) == M.int_types.int64)
assert(M.promote(M.int_types.cardinal, M.int_types.int16) == M.int_types.int32)

