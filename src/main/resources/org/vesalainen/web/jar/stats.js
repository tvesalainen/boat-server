/* 
 * Copyright (C) 2016 tkv
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/* global sse */

"use strict";

$(document).ready(function () {

    sse.addHandler("path", function(target, data){
        var sec = target.attr("data-seconds");
        var path = $(document.createElementNS("http://www.w3.org/2000/svg", "path"));
        path.attr("d", data);
        path.attr("data-ttl", sec);
        var tr = target.attr("transform");
        if (tr)
        {
            var mov = Number(/[\-0-9]+/.exec(tr));
            mov = -mov;
            path.attr("transform", "translate("+mov+")");
        }
        else
        {
            path.attr("transform", "translate(0)");
        }
        sse.register(path);
        target.append(path);
    });

    var moveId = setInterval(move, 1000);

});    

function move()
{
    $("[data-moving]").each(function(){
        var tr = $(this).attr("transform");
        if (tr)
        {
            var mov = Number(/[\-0-9]+/.exec(tr));
            mov--;
            $(this).attr("transform", "translate("+mov+")");
        }
    });
    
}
