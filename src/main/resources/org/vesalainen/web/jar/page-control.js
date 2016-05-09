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
  
    $("body").on("click", ".post-grid", function(){
        var form = $(this).parent();
        var gridId = form.attr("data-grid-id");
        var action = form.attr("action");
        var array = form.serializeArray();
        $.post(action, array, function(data, status){
            if (status === "success")
            {
                $("#"+gridId).parent().html(data);
                window.history.back();
                register($("#"+gridId));
            }
        });
    });
    
    $("body").on("click", ".set-grid", function(){
        var gridId = $(this).attr("data-grid-id");
        var href = $(this).attr("href");
        var base = window.location.pathname;
        $(href).find("form").each(function(){
            $(this).attr("data-grid-id", gridId);
            replaceAttr($(this), "action", base+"?assign="+gridId);
        });
        $(href).find("[name]").each(function(){
            replaceAttr($(this), "name", gridId);
        });
    });
    
    $(".add-page").on("click", function(){
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

function replaceAttr(e, at, prefix)
{
    var text = e.attr(at);
    var idx = text.lastIndexOf("-");
    if (idx !== -1)
    {
        text = text.slice(idx+1);
    }
    e.attr(at, prefix+"-"+text);
}
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