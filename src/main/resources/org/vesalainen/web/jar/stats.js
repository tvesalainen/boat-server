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

    sse.addHandler("graph", function(target, array){
        var x2;
        var y2;
        var sec = target.attr("data-seconds");
        var lastLine = target.children("line").last();
        if (lastLine.length > 0)
        {
            var x2 = lastLine.attr("x2");
            var y2 = lastLine.attr("y2");
        }
        else
        {
            var browserNow = new Date().getTime()/1000;
            var serverNow = array[array.length-2];
            var offset = serverNow - browserNow;
            target.attr("data-offset", offset);
        }
        var i;
        for (i=0;i<array.length;i+=2)
        {
            var x = array[i];
            var y = array[i+1];
            if (!x2)
            {
                x2 = x;
                y2 = y;
            }
            var line = $(document.createElementNS("http://www.w3.org/2000/svg", "line"));
            line.attr("x1", x2);
            line.attr("y1", y2);
            line.attr("x2", x);
            line.attr("y2", y);
            line.attr("data-ttl", sec);
            //sse.register(line);
            target.append(line);
            x2 = x;
            y2 = y;
        }
    });

    var moveId = setInterval(move, 1000);

});    

function move()
{
    $("[data-moving]").each(function(){
        var offset = Number($(this).attr("data-offset"));
        if (offset)
        {
            var browserNow = new Date().getTime()/1000;
            var tr = -(browserNow + offset);
            $(this).attr("transform", "translate("+tr+")");
        }
    });
    
}
