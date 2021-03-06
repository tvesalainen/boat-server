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

var pending = false;

$(document).ready(function () {

    $("body").on("click", ".post-grid", function(){
        if (!pending)
        {
            pending = true;
            var form = $(this).parent();
            var gridId = form.attr("data-grid-id");
            var action = form.attr("action");
            var array = form.serializeArray();
            $.post(action, array, function(data, status){
                if (status === "success")
                {
                    $("#"+gridId).parent().html(data);
                    window.history.back();
                    sse.register($("#"+gridId));
                }
                else
                {
                    throw status;
                }
                pending = false;
            });
        }
    });
    
    $("body").on("click", ".set-grid", function(){
        if (!pending)
        {
            pending = true;
            var gridId = $(this).attr("data-grid-id");
            var href = $(this).attr("href");
            var base = window.location.pathname;
            $.post(base, {refresh: ""}, function(data, status){
                if (status === "success")
                {
                    var arr = data.refresh;
                    $(href).find("[data-property]").each(function(){
                        var property = $(this).attr("data-property");
                        var idx = arr.indexOf(property);
                        if (idx !== -1)
                        {
                            $(this).show();
                        }
                        else
                        {
                            $(this).hide();
                        }
                    });
                }
                else
                {
                    throw status;
                }
                pending = false;
            });
            $(href).find("form").each(function(){
                $(this).attr("data-grid-id", gridId);
                replaceAttr($(this), "action", base+"?assign="+gridId);
            });
            $(href).find("[id]").each(function(){
                replaceAttr($(this), "id", gridId);
            });
            $(href).find("[for]").each(function(){
                replaceAttr($(this), "for", gridId);
            });
            $(href).find("[name]").each(function(){
                replaceAttr($(this), "name", gridId);
            });
        }
    });
    
    $(".add-page").on("click", function(){
        if (!pending)
        {
            pending = true;
            var pattern = $(this).attr("data-pattern");
            var base = window.location.pathname;
            $.post(base+"/add="+pattern, {pattern: pattern, sendFragment: "true"}, function(data, status){
                if (status === "success")
                {
                    $("body").append(data);
                    var lp = "#"+lastPage();
                    sse.register($(lp));
                    $("body").pagecontainer("change", lp);
                }
                else
                {
                    throw status;
                }
                pending = false;
            });
        }
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