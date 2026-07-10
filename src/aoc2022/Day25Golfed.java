package aoc2022;
class Y{
String s(boolean p,String I){
long a=0;
for(var l:I.split("\n")){
long v=0;
for(char c:l.toCharArray())v=v*5+"=-012".indexOf(c)-2;
a+=v;
}
var r="";
for(;a>0;a/=5){
r="012=-".charAt((int)(a%5))+r;
if(a%5>2)a+=5;
}
return r;
}
}
