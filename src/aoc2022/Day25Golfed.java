package aoc2022;
class Y{
String s(boolean p,String[]I){
long a=0;
for(var l:I){
long v=0;
for(var c:l.toCharArray())v=v*5+"=-012".indexOf(c)-2;
a+=v;
}
var r="";
for(;a>0;a=(a+2)/5)r="012=-".charAt((int)(a%5))+r;
return r;
}
}
