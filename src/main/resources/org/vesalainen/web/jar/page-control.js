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
        $.post("/boatserver/add="+pattern, {pattern: pattern, sendFragment: "true"}, function(data, status){
            if (status === "success")
            {
                var last = lastPage();
                if (!last)
                {
                    $("html").prepend(data);
                }
                else
                {
                    $("#"+last).after(data);
                }
                last = lastPage();
                $("body").pagecontainer("change", "#"+last);
            }
        });
    });
});

function lastPage()
{
    var res;
    $("[data-role='page'").each(function(){
        var id = $(this).attr("id");
        switch (id)
        {
            case "addPage":
            case "defPage":
                break;
            default:
                if (!res || id > res)
                {
                    res = id;
                }
                break;
        }
    });
    return res;
}