/**
 * Created by Aanya on 6/11/2017.
 */

var quote = [];
var ans = [];
quote.push("What gets wetter the more it dries?");
ans.push("A towel.");
quote.push("When you look for something, why is it always in the last place you look?");
ans.push("Because when you find it, you stop looking!");
quote.push("What goes up and never comes down?");
ans.push("Your age.");

var x = Math.floor((Math.random() * 3) + 1);
console.log(x);
console.log(quote[x-1]);


var str = "<p> "+ quote[x-1] +"</p>";
$("#random_quote").append(str);
var answer = ans[x-1];
console.log(answer);