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

"use strict";

$(document).ready(function () {
  
    $(".add-page").click(function(){
        var pattern = $(this).attr("data-pattern");
        var base = window.location.pathname;
        $.post(base+"/add="+pattern, {pattern: pattern, sendFragment: "true"}, function(data, status){
            if (status === "success")
            {
                $("body").append(data);
                $("body").pagecontainer("change", "#"+lastPage());
            }
        });
    });
});

function nextPage(e)
{
    var p = pages();
    var id = thisPageId(e);
    if (id)
    {
        var index = p.indexOf(id);
        if (index !== -1)
        {
            return p[(index+1)%p.length];
        }
    }
}
function prevPage(e)
{
    var p = pages();
    var id = thisPageId(e);
    if (id)
    {
        var index = p.indexOf(id);
        if (index !== -1)
        {
            return p[(index-1+p.length)%p.length];
        }
    }
}
function thisPageId(e)
{
    while (e)
    {
        var role = e.attr("data-role");
        if (role === "page")
        {
            return e.attr("id");
        }
        e = e.parent();
    }
    return undefined;
}
function lastPage()
{
    var p = pages();
    return p.pop();
}
function pages()
{
    var res = [];
    $("[data-role='page'").each(function(){
        var id = $(this).attr("id");
        res.push(id);
    });
    res.sort();
    return res;
}