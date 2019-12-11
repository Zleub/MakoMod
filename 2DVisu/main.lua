local grid_width, grid_height = 200, 200
local cell_width, cell_height = 8, 8

function circle(r)
    return function (x, y)
        local cx, cy = math.floor(grid_width / 2), math.floor(grid_height / 2)
        --return math.floor(  (x - cx) ^ 2 + (y - cy) ^ 2 ) <= r ^ 2
        --    and math.floor(  (x - cx) ^ 2 + (y - cy) ^ 2 ) >= (r - 0.5) ^ 2
        return math.cos(math.floor(  (x - cx) ^ 2 + (y - cy) ^ 2 )) * math.cos(math.floor(  (x - cx) ^ 2 + (y - cy) ^ 2 )) > r / 10
        --return math.cos(x) * math.cos(y) < r /10
    end
end

local d = 0

local m = {
    { x = math.floor(grid_width / 2), y = math.floor(grid_height / 2) }
}
function insert(value)
    for i, v in ipairs(m) do
        if (value.x == v.x and value.y == v.y) then
            return
        end
    end
    table.insert(m, value)
end

for i = 0, d do
    local _m = {}
    for i, v in ipairs(m) do
        local x, y = v.x, v.y
        table.insert(_m, { x = x + 2, y = y + 1 })
        table.insert(_m, { x = x + 2, y = y - 1 })
        table.insert(_m, { x = x - 2, y = y + 1 })
        table.insert(_m, { x = x - 2, y = y - 1 })
        table.insert(_m, { x = x + 1, y = y + 2 })
        table.insert(_m, { x = x + 1, y = y - 2 })
        table.insert(_m, { x = x - 1, y = y - 2 })
        table.insert(_m, { x = x - 1, y = y + 2 })
    end
    for i, v in ipairs(_m) do
        insert(v)
    end
end

function knight(d)
    return function (x, y)
        for i, v in ipairs(m) do
            if v.x == x and v.y == y then
                return true
            end
        end
    end
end

local mTweak = {
    { x = math.floor(grid_width / 2), y = math.floor(grid_height / 2) }
}
function insert(value)
    for i, v in ipairs(mTweak) do
        if (value.x == v.x and value.y == v.y) then
            return
        end
    end
    table.insert(mTweak, value)
end

for i = 0, d do
    local _m = {}
    for i, v in ipairs(mTweak) do
        local x, y = v.x, v.y
        table.insert(_m, { x = x + 3, y = y + 1 })
        --table.insert(_m, { x = x + 3, y = y - 1 })
        --table.insert(_m, { x = x - 3, y = y + 1 })
        table.insert(_m, { x = x - 3, y = y - 1 })
        table.insert(_m, { x = x + 1, y = y + 3 })
        --table.insert(_m, { x = x + 1, y = y - 3 })
        table.insert(_m, { x = x - 1, y = y - 3 })
        --table.insert(_m, { x = x - 1, y = y + 3 })
    end
    for i, v in ipairs(_m) do
        insert(v)
    end
end

function knightTweak(d)
    return function (x, y)
        for i, v in ipairs(mTweak) do
            if v.x == x and v.y == y then
                return true
            end
        end
    end
end

local t = 0

function getTime()
    return math.floor(t / 200)
end

function love.draw()
    local window_width, window_height = love.graphics.getDimensions()

    love.graphics.print(getTime())
    love.graphics.translate((window_width - grid_width * cell_width) / 2, (window_height - grid_height * cell_height) / 2)

    for i = 0, grid_width - 1 do
        for j = 0, grid_height - 1 do
            local x, y = i * cell_width, j * cell_height
            if circle( getTime() )(i, j) then
                love.graphics.setColor(255, 0, 0, 255)
                love.graphics.rectangle("fill", x, y, cell_width - 3, cell_height - 3)
            end
            --if knight( 1 )(i, j) then
            --    love.graphics.setColor(0, 255, 0, 255)
            --    love.graphics.rectangle("fill", x, y, cell_width - 3, cell_height - 3)
            --end
            --if knightTweak( getTime() )(i, j) then
            --    love.graphics.setColor(0, 0, 255, 255)
            --    love.graphics.rectangle("fill", x, y, cell_width - 3, cell_height - 3)
            --end
            --if knight( getTime() )(i, j) and knightTweak( getTime() )(i, j) then
            --    love.graphics.setColor(0, 127, 127, 255)
            --    love.graphics.rectangle("fill", x, y, cell_width - 3, cell_height - 3)
            --else
            --    love.graphics.setColor(255, 255, 255, 255)
            --    love.graphics.rectangle("line", x, y, cell_width - 3, cell_height - 3)
            --end
        end
    end

    if not love.keyboard.isDown( "lshift" ) then
        t = t + 1
    end
    if getTime() > 10 then
        t = 0
    end
end